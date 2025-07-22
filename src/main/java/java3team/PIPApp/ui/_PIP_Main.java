package ui;

import config.AppConstants;
import config.Stocks;
import api.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;
import service.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class _PIP_Main {

    private static final AtomicInteger openWindowCount = new AtomicInteger(0);  // 열린 창 수 추적
    private double offsetX, offsetY;
    private final int RESIZE_MARGIN = 10;

    private Timeline refreshTimeline;  // 주기적 업데이트용 타임라인
    private double previousPrice = -1;  // 직전 값

    // 종목명 + 현재가 표시
    private Label nameLabel;
    private Label priceLabel;

    public void pip_On(Stage stage, Stocks stock, int index) {
        openWindowCount.incrementAndGet();

        // 종목명 + 현재가 표시
        nameLabel = new Label(stock.getTicker() + "(" + stock.getName() + ")");
        priceLabel = new Label("Loading...");

//        // 실시간 주가 갱신 타이머
//        Timer timer = new Timer(true); // 데몬 스레드
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                StockQuote quote = new StockService().getLiveStockQuote(stock.getTicker());
//                if (quote != null) {
//                    double price = quote.getCurrentPrice();
//                    Platform.runLater(() -> {
//                        priceLabel.setText("$ " + String.format("%,.2f", price));
//                        System.out.println("🔄 [" + stock.getTicker() + "] PIP 정보 자동 새로고침");
//                    });
//                }
//            }
//        }, 0, stock.getRefresh() * 1000); // 설정된 초 단위로 갱신


        double fontSize = _PIP_SettingsFontSize.getFontSize();
        nameLabel.setStyle("-fx-font-size: " + (fontSize * 0.65) + "px; -fx-text-fill: white;" +
                "-fx-effect: dropshadow(gaussian, black, 2, 0.3, 0, 0);");
        priceLabel.setStyle("-fx-font-size: " + fontSize + "px; -fx-text-fill: lightgray;" +
                "-fx-effect: dropshadow(gaussian, black, 2, 0.3, 0, 0);");

        // 타임라인 시작
        updateLabels(stock);
        timelineRefresh(stock);

        // 창 크기 계산
        double ratio = fontSize / 28.0;
        double newWidth = Math.max(300, 300 * ratio);
        double newHeight = Math.max(120, 120 * ratio);

        stage.setX(0);
        stage.setY(0 + (fontSize * 5) * index); // Y좌표도 같이 늘림

        // 버튼
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        closeBtn.setOnAction(e -> {
            stage.close();
            int remaining = openWindowCount.decrementAndGet();
            // 타임라인 정지
            if (refreshTimeline != null) {
                refreshTimeline.stop();
            }
            if (remaining == 0) {
                Platform.exit();  // 마지막 창 닫으면 종료
            }
        });

        Button settingsBtn = new Button("⚙");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        settingsBtn.setOnAction(e -> {
            stage.close();
            openWindowCount.decrementAndGet();
            // 타임라인 정지
            if (refreshTimeline != null) {
                refreshTimeline.stop();
            }
            try {
                Parent homeRoot = FXMLLoader.load(getClass().getResource("home.fxml"));
                Main.mainStage.setScene(new Scene(homeRoot, 1220, 740));
                Main.mainStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // 레이아웃 구성
        HBox buttonBox = new HBox(8, settingsBtn, closeBtn);
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setPadding(new Insets(8));
        buttonBox.setVisible(false);

        StackPane center = VBoxSpacing(nameLabel, priceLabel);
        center.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(center, buttonBox);
        applyOutlineStyle(root, buttonBox);

        // 드래그 & 리사이징
        root.setOnMousePressed(e -> {
            offsetX = e.getSceneX();
            offsetY = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            if (offsetX > stage.getWidth() - RESIZE_MARGIN && offsetY > stage.getHeight() - RESIZE_MARGIN) {
                stage.setWidth(Math.max(150, e.getScreenX() - stage.getX()));
                stage.setHeight(Math.max(80, e.getScreenY() - stage.getY()));
            } else {
                stage.setX(e.getScreenX() - offsetX);
                stage.setY(e.getScreenY() - offsetY);
            }
        });

        Scene scene = new Scene(root, newWidth, newHeight);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("StockPipApp");
        stage.show();
    }

    private void timelineRefresh(Stocks stock) {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }

        int refreshSeconds = stock.getRefresh();
        if (refreshSeconds <= 0) return;

        refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(refreshSeconds), event -> {
                    stock.refreshQuote();
                    updateLabels(stock);
                })
        );

        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    // 라벨 업데이트
    private void updateLabels(Stocks stock) {
        double current = stock.currentPrice;

        // 텍스트 색상 결정
        String color;
        if (previousPrice < 0) {
            color = "lightgray"; // 첫 표시
        } else if (current > previousPrice) {
            color = "red"; // 상승
        } else if (current < previousPrice) {
            color = "blue"; // 하락
        } else {
            color = "lightgray"; // 동일
        }

        priceLabel.setText("$ " + String.format("%,.2f", stock.currentPrice));
        priceLabel.setStyle("-fx-font-size: " + _PIP_SettingsFontSize.getFontSize() + "px; -fx-text-fill: " + color + ";" +
                "-fx-effect: dropshadow(gaussian, black, 2, 0.3, 0, 0);");

        previousPrice = current;

        System.out.println("🔄 [" + stock.getTicker() + "] PIP 정보 자동 새로고침");
    }

    // 테두리 설정 스타일 적용
    private void applyOutlineStyle(StackPane root, HBox buttonBox) {
        if (!AppConstants.pipOutlineOption) {
            root.setStyle("-fx-background-color: transparent;");
            buttonBox.setVisible(false);
            root.setOnMouseEntered(e -> {
                root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1px;");
                buttonBox.setVisible(true);
            });
            root.setOnMouseExited(e -> {
                root.setStyle("-fx-background-color: transparent;");
                buttonBox.setVisible(false);
            });
        } else {
            root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1px;");
            buttonBox.setVisible(true);
        }
    }

    // 수직 정렬을 위한 VBox 대체용
    private StackPane VBoxSpacing(Label top, Label bottom) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(top, bottom);
        return new StackPane(box);
    }
}