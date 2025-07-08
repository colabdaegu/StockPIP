
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox; // 예시로 VBox 임포트
// import com.yourteam.pipapp.ui.StockView; // StockView를 사용한다면 임포트

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 애플리케이션의 주 UI를 설정하는 부분입니다.
        // 예를 들어, StockView 인스턴스를 생성하고 표시할 수 있습니다.

        VBox root = new VBox(); // 임시 루트 컨테이너
        root.getChildren().add(new javafx.scene.control.Label("Welcome to StockPIPApp!")); // 임시 라벨

        Scene scene = new Scene(root, 800, 600); // 윈도우 크기 설정 (너비, 높이)
        primaryStage.setTitle("StockPIPApp"); // 윈도우 제목 설정
        primaryStage.setScene(scene); // Scene 설정
        primaryStage.show(); // 윈도우 표시
    }

    public static void main(String[] args) {
        // JavaFX 애플리케이션을 시작합니다.
        launch(args);
    }
}