package PIPApp;

import javafx.stage.StageStyle;
import service.alert.AlertServiceLauncher;
import config.manager.PreferencesManager;

import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage mainStage;

    private PreferencesManager preferencesManager;

    public void start(Stage primaryStage) throws Exception{
        Font.loadFont(getClass().getResourceAsStream("/fonts/Pretendard-Regular.ttf"), 15);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Pretendard-Bold.ttf"), 15);

        mainStage = primaryStage;

        /// PreferencesManager를 초기화하고 설정을 로드 ///
        preferencesManager = new PreferencesManager();
        preferencesManager.loadSettings();

        /// ⭐ 알림 체크 스케줄링 시작 ///
        AlertServiceLauncher.startAll();

        Parent root = FXMLLoader.load(getClass().getResource("/ui/view/home.fxml"));

        //primaryStage.initStyle(StageStyle.UNDECORATED);   // 상단바 제거

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo/Stock_Logo.png")));


        primaryStage.setTitle("StockPIP-App");
        primaryStage.setScene(new Scene(root, 1220, 740));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        preferencesManager.saveSettings(); // 앱 종료 시 저장
        System.out.println("-주식 종목 설정이 JSON 파일에 저장되었습니다.");
        super.stop();
    }

    public static void main(String[] args) { launch(args); }
}