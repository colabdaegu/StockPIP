package pip.basicMode;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import service.alert.AlertService;
import net.NetworkManager;
import config.manager.PreferencesManager;
import PIPApp.Main;
import config.*;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class PipBasicMain {
    private static final List<PipBasicMain> pipWindows = new ArrayList<>();

    private Stage stage;
    private double offsetX, offsetY;
    private final int RESIZE_MARGIN = 10;

    private Timeline refreshTimeline;
    private double previousPrice = -1;

    private Label nameLabel;
    private Label priceLabel;

    private String thisTicker;

    // main. Entry Point
    public void PipMain(Stage stage, Stocks stock, int index) {
        this.stage = stage;
        pipWindows.add(this);

        thisTicker = stock.getTicker();

        nameLabel = new Label(stock.getName() + "(" + stock.getTicker() + ")");
        priceLabel = new Label("Loading...");

        double fontSize = AppConstants.pipFontSize;
        styleLabels(fontSize); // 2.

        updateLabels(stock); // 3.
        timelineRefresh(stock); // 4.

        stage.setX(0);
        stage.setY(0 + (fontSize * 5) * index);

        HBox buttonBox = createButtonBar(); // 5.
        StackPane center = VBoxSpacing(nameLabel, priceLabel); // 6.
        center.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(center, buttonBox);
        applyOutlineStyle(root, buttonBox); // 7.
        enableDragAndResize(stage, root);   // 8.

        double ratio = fontSize / 28.0;
        double newWidth = Math.max(150, 300 * ratio);
        double newHeight = Math.max(60, 120 * ratio);

        setupStage(stage, root, newWidth, newHeight); // 9.


        AlertService.startMonitoring(stock);
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
        String name = stock.name;

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

//        // AAPL일 경우 무조건 RED 고정
//        if ("AAPL".equalsIgnoreCase(stock.getTicker())) {
//            color = Color.RED;
//        }

        switch (AppConstants.pipDecimalPoint) {
            case 0 -> priceLabel.setText(String.format("$ %,.0f", current));
            case 1 -> priceLabel.setText(String.format("$ %,.1f", current));
            case 2 -> priceLabel.setText(String.format("$ %,.2f", current));
            case 3 -> priceLabel.setText(String.format("$ %,.3f", current));
            case 4 -> priceLabel.setText(String.format("$ %,.4f", current));
            case 5 -> priceLabel.setText(String.format("$ %,.5f", current));
        }
        // 사용자가 설정한 소수점 자릿수보다 값이 적은 값에 대해서는 항상 해당하는 소수점대로 표시
        if ((current < 1 && current >= 0.1) && (AppConstants.pipDecimalPoint < 1)) { priceLabel.setText(String.format("$ %,.1f", current)); }
        else if ((current < 0.1 && current >= 0.01) && (AppConstants.pipDecimalPoint < 2)) { priceLabel.setText(String.format("$ %,.2f", current)); }
        else if ((current < 0.01 && current >= 0.001) && (AppConstants.pipDecimalPoint < 3)) { priceLabel.setText(String.format("$ %,.3f", current)); }
        else if ((current < 0.001 && current >= 0.0001) && (AppConstants.pipDecimalPoint < 4)) { priceLabel.setText(String.format("$ %,.4f", current)); }
        else if ((current < 0.0001 && current >= 0.00001) && (AppConstants.pipDecimalPoint < 5)) { priceLabel.setText(String.format("$ %,.5f", current)); }
        else if ((current < 0.00001 && current >= 0.000001) && (AppConstants.pipDecimalPoint <= 5)) { priceLabel.setText(String.format("$ %,.6f", current)); }

        priceLabel.setTextFill(color);
        previousPrice = current;

        nameLabel.setText(name + "(" + stock.getTicker() + ")");

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
                    // 네트워크 검사
                    if (!NetworkManager.isInternetAvailable()) {
                        priceLabel.setText("Network Failure");
                        priceLabel.setTextFill(Color.CORAL);
                        System.out.println("⚠ PIP 새로고침 중단 - 인터넷 연결 실패\n");
                        return;
                    }

                    stock.refreshQuote();
                    updateLabels(stock);    /// updateLabels 함수 동작

                    // 손절가 조건 체크
                    if (stock.getCurrentPrice() <= stock.getStopPrice() && stock.getCurrentPrice() != 0) {
                        System.out.println("[" + stock.getTicker() + "] 손절가 도달 → PIP 창 닫음");
                        stop(1); // 타임라인 중단 + Stage 닫기
                    }
                })
        );

        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    // 5. 버튼 생성 및 핸들러
    private HBox createButtonBar() {
        double fontSize = AppConstants.pipFontSize;
        double buttonFontSize = (fontSize * 0.71 > 10) ? fontSize * 0.71 : 10;

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: white; " +
                        "-fx-font-size: " + buttonFontSize + "px;");
        closeBtn.setOnAction(e -> {
            stop(1);
            AlertService.pauseTickerMonitoring(thisTicker);  // ✕ 눌린 종목에 한해서만 일시적으로 모니터링 중단

            if (pipWindows.isEmpty()) Platform.exit();
        });

        Button settingsBtn = new Button("⚙");
        settingsBtn.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: white; " +
                        "-fx-font-size: " + buttonFontSize + "px;");
        settingsBtn.setOnAction(e -> {
            for (PipBasicMain pip : new ArrayList<>(pipWindows)) {
                pip.stop(0);
            }
            pipWindows.clear();
            try {
                Parent homeRoot = FXMLLoader.load(getClass().getResource("/ui/view/home.fxml"));
                new PreferencesManager().saveSettings();
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo/Stock_Logo_fill.png")));

        stage.show();
    }

    // 10. 종료 시 타임라인 멈추고 창 닫기
    public void stop(int option) {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        if (stage != null) {
            stage.close();
        }

        if (option == 1) {
            pipWindows.remove(this);

            if (pipWindows.isEmpty()) {
                handleLastWindowClose();
            }
        }
    }

    // 마지막 창일 경우 (알림창 표시를 위한 더미 Stage)
    private static void handleLastWindowClose() {
        Stage tempStage = new Stage();
        tempStage.setTitle("Dummy");
        tempStage.setOpacity(0);
        tempStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            tempStage.close();
            Platform.exit();
        });
        delay.play();
    }


    public static List<PipBasicMain> getPipWindows() {
        return pipWindows;
    }

    public String getStockTicker() {
        return thisTicker;
    }
}