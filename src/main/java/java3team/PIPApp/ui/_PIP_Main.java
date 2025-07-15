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
        Label priceLabel = new Label("₩ 10,000");

        double fontSize = _PIP_SettingsFontSize.getFontSize();  // 저장된 크기 불러오기
        priceLabel.setStyle("-fx-font-size: " + fontSize + "px; -fx-text-fill: red;");

        // 기준값: SettingsFontSize 내부의 기본값과 동일하게 28.0 사용
        double baseFontSize = 28.0;
        double baseWidth = 300;
        double baseHeight = 120;

        // 폰트 비율로 확대
        double ratio = fontSize / baseFontSize;

        // 오버레이 창 크기를 비례해서 확대
        double newWidth = Math.max(baseWidth, baseWidth * ratio);
        double newHeight = Math.max(baseHeight, baseHeight * ratio);

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

        StackPane center = new StackPane(priceLabel);
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

//        // 마우스 올리면 배경 반투명 + 버튼 보이기
//        root.setOnMouseEntered(e -> {
//            root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1px;");
//            buttonBox.setVisible(true);
//        });
//
//        // 마우스 벗어나면 배경 투명 + 버튼 숨김
//        root.setOnMouseExited(e -> {
//            root.setStyle("-fx-background-color: transparent;");
//            buttonBox.setVisible(false);
//        });
        if (!AppConstants.pipOutlineOption) {
            root.setStyle("-fx-background-color: transparent;");
            buttonBox.setVisible(false);

            // // 마우스 올리면 배경 반투명 + 버튼 보이기
            root.setOnMouseEntered(e -> {
                root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1px;");
                buttonBox.setVisible(true);
            });

            // 마우스 벗어나면 배경 투명 + 버튼 숨김
            root.setOnMouseExited(e -> {
                root.setStyle("-fx-background-color: transparent;");
                buttonBox.setVisible(false);
            });
        } else {    // 테두리 항상 보임
            root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1px;");
            buttonBox.setVisible(true);
        }

        // 창 설정
        Scene scene = new Scene(root, newWidth, newHeight);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("StockPipApp");
        stage.show();
    }
}