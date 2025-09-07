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

import java.util.ArrayList;
import java.util.List;

public class _PIP_Main {
    private static final List<_PIP_Main> pipWindows = new ArrayList<>();

    private Stage stage;
    private double offsetX, offsetY;
    private final int RESIZE_MARGIN = 10;

    private Timeline refreshTimeline;
    private double previousPrice = -1;

    private Label nameLabel;
    private Label priceLabel;

    // 1. Entry Point
    public void pip_On(Stage stage, Stocks stock, int index) {
        this.stage = stage;
        pipWindows.add(this);

        nameLabel = new Label(stock.getTicker() + "(" + stock.getName() + ")");
        priceLabel = new Label("Loading...");

        double fontSize = _PIP_SettingsFontSize.getFontSize();
        styleLabels(fontSize); // 2.

        updateLabels(stock); // 3.
        timelineRefresh(stock); // 4.

        double ratio = fontSize / 28.0;
        double newWidth = Math.max(300, 300 * ratio);
        double newHeight = Math.max(120, 120 * ratio);

        stage.setX(0);
        stage.setY(0 + (fontSize * 5) * index);

        HBox buttonBox = createButtonBar(); // 5.
        StackPane center = VBoxSpacing(nameLabel, priceLabel); // 6.
        center.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(center, buttonBox);
        applyOutlineStyle(root, buttonBox); // 7.
        enableDragAndResize(stage, root);   // 8.

        setupStage(stage, root, newWidth, newHeight); // 9.
    }

    // 2. 스타일 설정
    private void styleLabels(double fontSize) {
        nameLabel.setStyle("-fx-font-size: " + (fontSize * 0.65) + "px; -fx-text-fill: white;" +
                "-fx-effect: dropshadow(gaussian, black, 2, 0.3, 0, 0);");

        priceLabel.setStyle("-fx-font-size: " + fontSize + "px;" +
                "-fx-effect: dropshadow(gaussian, black, 2, 0.3, 0, 0);");
    }

    // 3. 현재가 표시 업데이트
    private void updateLabels(Stocks stock) {
        double current = stock.currentPrice;

        Color color;
        if (previousPrice < 0) {
            color = Color.LIGHTGRAY;
        } else if (current > previousPrice) {
            color = Color.RED;
        } else if (current < previousPrice) {
            color = Color.BLUE;
        } else {
            color = Color.LIGHTGRAY;
        }

        priceLabel.setText(String.format("$ %,.2f", current));
        priceLabel.setTextFill(color);
        previousPrice = current;

        System.out.println("🔄 [" + stock.getTicker() + "] PIP 정보 자동 새로고침");
    }

    // 4. 주기적 업데이트
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

    // 5. 버튼 생성 및 핸들러
    private HBox createButtonBar() {
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        closeBtn.setOnAction(e -> {
            stop();
            pipWindows.remove(this);
            if (pipWindows.isEmpty()) Platform.exit();
        });

        Button settingsBtn = new Button("⚙");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        settingsBtn.setOnAction(e -> {
            for (_PIP_Main pip : new ArrayList<>(pipWindows)) {
                pip.stop();
            }
            pipWindows.clear();
            try {
                Parent homeRoot = FXMLLoader.load(getClass().getResource("home.fxml"));
                Main.mainStage.setScene(new Scene(homeRoot, 1220, 740));
                Main.mainStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox box = new HBox(8, settingsBtn, closeBtn);
        box.setAlignment(Pos.TOP_RIGHT);
        box.setPadding(new Insets(8));
        box.setVisible(false);
        return box;
    }

    // 6. 레이블 수직 정렬용 VBox
    private StackPane VBoxSpacing(Label top, Label bottom) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(top, bottom);
        return new StackPane(box);
    }

    // 7. 마우스 진입 시 외곽선 스타일
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

    // 8. 창 드래그 & 리사이징
    private void enableDragAndResize(Stage stage, StackPane root) {
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
    }

    // 9. Stage 설정 및 띄우기
    private void setupStage(Stage stage, StackPane root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("StockPipApp");
        stage.show();
    }

    // 10. 종료 시 타임라인 멈추고 창 닫기
    public void stop() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        if (stage != null) {
            stage.close();
        }
    }
}