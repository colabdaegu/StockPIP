package PIPApp;

import service.*;

import javafx.scene.image.Image;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage mainStage;

    private AlertService alertService;
    private PreferencesManager preferencesManager;
    private Timeline dataUpdateTimeline;

    public void start(Stage primaryStage) throws Exception{
        Font.loadFont(getClass().getResourceAsStream("/fonts/Pretendard-Regular.ttf"), 15);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Pretendard-Bold.ttf"), 15);

        mainStage = primaryStage;

        // ⭐ PreferencesManager를 초기화하고 설정을 로드 ⭐
        preferencesManager = new PreferencesManager();
        preferencesManager.loadSettings();

        // ⭐ 알림 체크 스케줄링 시작 ⭐
        AlertServiceLauncher.startAll();

        Parent root = FXMLLoader.load(getClass().getResource("/ui/home.fxml"));

        //primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo/Stock_Logo.png")));

        primaryStage.setTitle("StockPIP-App");
        primaryStage.setScene(new Scene(root, 1220, 740));
        primaryStage.show();
//        // ⭐ 알림 체크 스케줄링
//        alertService = new AlertService();
//
//        dataUpdateTimeline = new Timeline(
//                new KeyFrame(Duration.seconds(5), event -> {
//                    System.out.println("⏰ 알림 체크 실행 중...");
////                    alertService.checkPriceAndAlert();
//                })
//        );
//        dataUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
//        dataUpdateTimeline.play();
    }


    @Override
    public void stop() throws Exception {
        preferencesManager.saveSettings(); // 앱 종료 시 저장
        System.out.println("-주식 종목 설정이 JSON 파일에 저장되었습니다.");
        super.stop();
    }
//    @Override
//    public void stop() throws Exception {
//        if (dataUpdateTimeline != null) {
//            dataUpdateTimeline.stop();
//            System.out.println("데이터 갱신 Timeline이 중지되었습니다.");
//        }
//
//        preferencesManager.saveSettings();
//        System.out.println("현재 주식 종목 설정이 JSON 파일에 저장되었습니다.");
//
//        super.stop();
//    }

    public static void main(String[] args) { launch(args); }
}