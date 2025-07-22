package service;

import config.*;
import javafx.animation.PauseTransition; // ⭐ PauseTransition 임포트 추가
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Screen; // ⭐ Screen 임포트 추가
import javafx.stage.Stage; // ⭐ Stage 임포트 추가
import javafx.util.Duration; // ⭐ Duration 임포트 추가

import java.util.List; // List 인터페이스 임포트


public class AlertService {

    public static void checkPriceAndAlert() {

        // StockList에서 모든 Stocks 객체를 가져오기.
        List<Stocks> stocksToMonitor = StockList.getStockArray();

        // 각 종목에 대해 알림 조건을 확인합니다.
        for (Stocks stock : stocksToMonitor) {
            double currentPrice = stock.getCurrentPrice();
            double targetPrice = stock.getTargetPrice();
            double stopPrice = stock.getStopPrice();
            String companyName = stock.getName();

            // API로부터 실제 currentPrice를 받아오는 로직이 필요합니다.
            // 현재 stock.getCurrentPrice()는 초기값일 수 있으므로, 실제 시세 업데이트 로직이 필요합니다.
            // 예를 들어, StockService를 통해 API 호출 후 currentPrice를 업데이트하는 로직이 여기에 들어가야 합니다.
            // 예시: stock.setCurrentPrice(new StockService().getLiveStockQuote(stock.getTicker()).getC());

            if (currentPrice >= targetPrice && targetPrice != 0.0) {
                sendAlert(companyName + " 목표가 도달!", "현재가: " + currentPrice + ", 목표가: " + targetPrice);
            } else if (currentPrice <= stopPrice && stopPrice != 0.0) {
                sendAlert(companyName + " 손절가 도달!", "현재가: " + currentPrice + ", 손절가: " + stopPrice);
            }
        }
    }

    private static void sendAlert(String title, String message) {
        Platform.runLater(() -> {
            showAlertPopup(title, message);
        });
    }

    private static void showAlertPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // ⭐ 팝업을 띄울 Stage를 가져옵니다.
        // Alert는 Stage를 상속받지 않으므로, getDialogPane().getScene().getWindow()를 통해 접근합니다.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // ⭐ 화면 오른쪽 아래에 위치 설정
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // 팝업 창의 대략적인 크기를 고려하여 위치 조정 (정확한 크기는 런타임에 결정되므로 대략적인 값 사용)
        // 예를 들어, 너비 300px, 높이 150px 정도로 가정
        double alertWidth = 300;
        double alertHeight = 150;

        stage.setX(screenWidth - alertWidth - 20); // 화면 오른쪽에서 20px 안쪽
        stage.setY(screenHeight - alertHeight - 20); // 화면 아래에서 20px 안쪽

        // 🔔 내장 비프음 (설정에서 켜져있을 때만)
        if (AppConstants.AlertSound) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }

        // ⭐ 2초 뒤에 팝업이 자동으로 사라지도록 설정
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            alert.hide(); // 팝업 숨기기 (닫기)
        });

        alert.show(); // 팝업 표시
        delay.play(); // 딜레이 시작
    }
}