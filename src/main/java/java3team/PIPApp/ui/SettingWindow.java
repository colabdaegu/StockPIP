package ui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class SettingWindow {

    private Stage stage;

    public SettingWindow() {
        // 설정 창을 위한 새로운 Stage를 생성합니다.
        stage = new Stage();
        stage.setTitle("Settings");

        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Application Settings"));
        layout.getChildren().add(new Button("Save Settings"));
        // 여기에 API 키 입력, 테마 설정 등 다양한 설정 UI 요소를 추가합니다.

        Scene scene = new Scene(layout, 400, 300); // 설정 창 크기
        stage.setScene(scene);
    }

    public void show() {
        // 설정 창을 표시합니다.
        stage.show();
    }

    public void hide() {
        // 설정 창을 숨깁니다.
        stage.hide();
    }
}