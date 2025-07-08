import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsWindow {

    public static void show(Stage ownerStage) {
        Stage settingsStage = new Stage();
        settingsStage.initOwner(ownerStage);
        settingsStage.initModality(Modality.WINDOW_MODAL);
        settingsStage.setTitle("사용자 설정");

        TextField stockField = new TextField();
        stockField.setPromptText("예: AAPL");

        TextField upperLimitField = new TextField();
        upperLimitField.setPromptText("예: 30000");

        TextField lowerLimitField = new TextField();
        lowerLimitField.setPromptText("예: 20000");

        Button saveBtn = new Button("저장");
        saveBtn.setOnAction(e -> {
            System.out.println("✔ 종목: " + stockField.getText());
            System.out.println("✔ 목표가: " + upperLimitField.getText());
            System.out.println("✔ 손절가: " + lowerLimitField.getText());
            settingsStage.close();
        });

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("종목:"), 0, 0);
        grid.add(stockField, 1, 0);
        grid.add(new Label("목표 가격:"), 0, 1);
        grid.add(upperLimitField, 1, 1);
        grid.add(new Label("손절 가격:"), 0, 2);
        grid.add(lowerLimitField, 1, 2);
        grid.add(saveBtn, 1, 3);

        Scene scene = new Scene(grid, 300, 220);
        settingsStage.setScene(scene);
        settingsStage.show();
    }
}