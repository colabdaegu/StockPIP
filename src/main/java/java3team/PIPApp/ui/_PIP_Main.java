package ui;

import config.AppConstants;
import config.Stocks;
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

import java.util.concurrent.atomic.AtomicInteger;

public class _PIP_Main {

    private static final AtomicInteger openWindowCount = new AtomicInteger(0);  // 열린 창 수 추적
    private double offsetX, offsetY;
    private final int RESIZE_MARGIN = 10;

    public void pip_On(Stage stage, Stocks stock, int index) {
        openWindowCount.incrementAndGet();

        // 종목명 + 현재가 표시
        Label nameLabel = new Label(stock.getName());
        Label priceLabel = new Label("$ " + stock.currentPrice);

        double fontSize = _PIP_SettingsFontSize.getFontSize();
        nameLabel.setStyle("-fx-font-size: " + (fontSize * 0.7) + "px; -fx-text-fill: white;");
        priceLabel.setStyle("-fx-font-size: " + fontSize + "px; -fx-text-fill: red;");

        // 창 크기 계산
        double ratio = fontSize / 28.0;
        double newWidth = Math.max(300, 300 * ratio);
        double newHeight = Math.max(120, 120 * ratio);

        // 버튼
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        closeBtn.setOnAction(e -> {
            stage.close();
            int remaining = openWindowCount.decrementAndGet();
            if (remaining == 0) {
                Platform.exit();  // 마지막 창 닫으면 종료
            }
        });

        Button settingsBtn = new Button("⚙");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        settingsBtn.setOnAction(e -> {
            stage.close();
            openWindowCount.decrementAndGet();
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