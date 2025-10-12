package PIPApp;

import config.StockList;
import config.Stocks;
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

        // 디폴트용 데이터 (종목이 0개일 시 defaultTicker 종목 하나 넣고 시작)
        startDefaultStock("AAPL", 1, 300, 200, 0, 10);

        /// ⭐ 알림 체크 스케줄링 시작 ///
        AlertServiceLauncher.startAll();

        Parent root = FXMLLoader.load(getClass().getResource("/ui/view/home.fxml"));

        //primaryStage.initStyle(StageStyle.UNDECORATED);   // 상단바 제거

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo/Stock_Logo.png")));


        primaryStage.setTitle("Mini-Stock");
        primaryStage.setScene(new Scene(root, 1220, 740));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        preferencesManager.saveSettings(); // 앱 종료 시 저장
        System.out.println("→ 주식 종목 설정이 JSON 파일에 저장되었습니다.");
        super.stop();
    }

    // 테스트용 데이터
    public void startDefaultStock(String defaultTicker, int toggleOption, double targetPrice, double stopPrice, int refreshMinute, int refreshSecond) {
//        boolean exists = StockList.getStockArray()
//                .stream()
//                .anyMatch(stock -> stock.getTicker().equalsIgnoreCase(defaultTicker));
        if (StockList.getStockArray().isEmpty()) {
            Stocks newStock = new Stocks(defaultTicker, toggleOption, targetPrice, stopPrice, refreshMinute, refreshSecond);
            StockList.getStockArray().add(newStock);
            System.out.println("\uD83C\uDD99 테스트 데이터 (" + defaultTicker + ") 추가");
        }
    }

    public static void main(String[] args) { launch(args); }
}