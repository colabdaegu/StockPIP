package ui;

import config.AppConstants;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class _PIP_Main {
    private double offsetX, offsetY;
    private double initWidth = 300;
    private double initHeight = 120;

    private boolean resizing = false;
    private final int RESIZE_MARGIN = 10;

    public void pip_On(Stage stage) {
        // 현재 설정된 폰트 크기 불러오기
        double fontSize = _PIP_SettingsFontSize.getFontSize();
        double baseFontSize = 28.0;
        double baseWidth = 300;
        double baseHeight = 120;
        double ratio = fontSize / baseFontSize;

        double newWidth = Math.max(baseWidth, baseWidth * ratio);
        double newHeight = Math.max(baseHeight, baseHeight * ratio);

        // 종목 제목 라벨
        //Label titleLabel = new Label(stockName);
        Label titleLabel = new Label("TSLL");
        titleLabel.setStyle("-fx-font-size: " + fontSize + "px; -fx-text-fill: white;");

        // 가격 라벨
        //Label priceLabel = new Label(stockPrice);
        Label priceLabel = new Label("$ 100");
        priceLabel.setStyle("-fx-font-size: " + fontSize + "px; -fx-text-fill: red;");

        // 제목 + 가격을 세로 정렬
        VBox centerBox = new VBox(5, titleLabel, priceLabel);
        centerBox.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        closeBtn.setOnAction(e -> stage.close());

        Button settingsBtn = new Button("⚙");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        settingsBtn.setOnAction(e -> {
            stage.close();
            try {
                Parent homeRoot = FXMLLoader.load(getClass().getResource("home.fxml"));
                Main.mainStage.setScene(new Scene(homeRoot, 1220, 740));
                Main.mainStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(8, settingsBtn, closeBtn);
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setPadding(new Insets(8));
        buttonBox.setVisible(false);

        StackPane center = new StackPane(centerBox);
        center.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(center, buttonBox);
        root.setStyle("-fx-background-color: transparent;");

        root.setOnMousePressed((MouseEvent e) -> {
            offsetX = e.getSceneX();
            offsetY = e.getSceneY();
            resizing = (offsetX > stage.getWidth() - RESIZE_MARGIN && offsetY > stage.getHeight() - RESIZE_MARGIN);
        });

        root.setOnMouseDragged((MouseEvent e) -> {
            if (resizing) {
                stage.setWidth(Math.max(150, e.getScreenX() - stage.getX()));
                stage.setHeight(Math.max(80, e.getScreenY() - stage.getY()));
            } else {
                stage.setX(e.getScreenX() - offsetX);
                stage.setY(e.getScreenY() - offsetY);
            }
        });

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

        Scene scene = new Scene(root, newWidth, newHeight);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("StockPipApp");
        stage.show();
    }
}