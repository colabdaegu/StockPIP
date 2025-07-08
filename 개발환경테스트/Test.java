import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Test extends Application {

    private double offsetX, offsetY;
    private double initWidth = 300;
    private double initHeight = 120;

    private boolean resizing = false;
    private final int RESIZE_MARGIN = 10;

    @Override
    public void start(Stage stage) {
        Label priceLabel = new Label("₩ 10,000");
        priceLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: red;");

        // ✕ 닫기 버튼
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        closeBtn.setOnAction(e -> stage.close());

        // ⚙ 설정 버튼
        Button settingsBtn = new Button("⚙");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        settingsBtn.setOnAction(e -> SettingsWindow.show(stage));

        // 오른쪽 상단 버튼 묶음
        HBox buttonBox = new HBox(8, settingsBtn, closeBtn);
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setPadding(new Insets(8));
        buttonBox.setVisible(false); // 기본은 숨김

        // 중앙 텍스트
        StackPane center = new StackPane(priceLabel);
        center.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(center, buttonBox);
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root, initWidth, initHeight);
        scene.setFill(Color.TRANSPARENT);

        // 드래그로 이동 & 크기 조절
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

        // 마우스 올리면 배경 반투명 + 버튼 보이기
        root.setOnMouseEntered(e -> {
            root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1px;");
            buttonBox.setVisible(true);
        });

        // 마우스 벗어나면 배경 투명 + 버튼 숨김
        root.setOnMouseExited(e -> {
            root.setStyle("-fx-background-color: transparent;");
            buttonBox.setVisible(false);
        });

        // 창 설정
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("StockPipApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}