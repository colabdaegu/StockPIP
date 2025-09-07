package service;

import config.*;
import ui.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;


public class AlertService {
    // 종목별 모니터링 타이머 관리
    private static final Map<String, Timeline> monitoringMap = new HashMap<>();

    // 모니터링 시작
    public static void startMonitoring(Stocks stock) {
        String ticker = stock.getTicker();

        // 이미 모니터링 중이라면 중단 후 재시작
        if (monitoringMap.containsKey(ticker)) {
            stopMonitoring(ticker);
        }

        // 새 Timeline 생성
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(stock.getRefresh()), event -> {
            stock.alert_refreshQuote(); // 시세 갱신
            double currentPrice = stock.getCurrentPrice();
            LocalDateTime api_refreshTime = stock.getApi_refreshTime();
            double targetPrice = stock.getTargetPrice();
            double stopPrice = stock.getStopPrice();

            // 목표가 도달 시
            if (currentPrice >= targetPrice) {
                String logLine = formatLog(0, ticker, "목표가에 도달했습니다.", currentPrice, targetPrice);
                StockList.appendLog(logLine);
                LogInfo_Controller.appendToLogArea(logLine);
                if (AppConstants.NotificationOption == 0) {
                    showAlert(Alert.AlertType.INFORMATION, "📈 목표가 도달", logLine);
                } else if (AppConstants.NotificationOption == 1) { }
                beep();
                System.out.println(api_refreshTime + " - [" + ticker + "] 목표가 도달 / 현재가: $" + currentPrice + " 목표가: $" + targetPrice + "\n");
            }

            // 손절가 도달 시
            if (currentPrice <= stopPrice) {
                String logLine = formatLog(1, ticker, "손절가에 도달했습니다. 삭제됨", currentPrice, stopPrice);
                StockList.appendLog(logLine);
                LogInfo_Controller.appendToLogArea(logLine);
                if (AppConstants.NotificationOption == 0) {
                    showAlert(Alert.AlertType.NONE, "📉 손절가 도달", logLine);
                } else if (AppConstants.NotificationOption == 1) { }
                beep();
                System.out.println(api_refreshTime + " - [" + ticker + "] 손절가 도달 / 현재가: $" + currentPrice + " 목표가: $" + stopPrice + "\n");

                // 모니터링 종료 및 데이터 삭제
                stopMonitoring(ticker);
                StockList.getStockArray().removeIf(s -> s.getTicker().equals(ticker));
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        monitoringMap.put(ticker, timeline);
    }

    // 모니터링 중단
    public static void stopMonitoring(String ticker) {
        if (monitoringMap.containsKey(ticker)) {
            monitoringMap.get(ticker).stop();
            monitoringMap.remove(ticker);
        }
    }

    // 로그 포맷 함수
    private static String formatLog(int type, String ticker, String message, double currentPrice, double targetOrStopPrice) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        if (type == 0) {
            return timestamp + " - [" + ticker + "]이(가) " + message + " / 현재가: $" + currentPrice + " 목표가: $" + targetOrStopPrice;
        } else {
            return timestamp + " - [" + ticker + "]이(가) " + message + " / 현재가: $" + currentPrice + " --> 삭제됨";
        }
    }

    // 비프음
    private static void beep() {
        if (AppConstants.AlertSound) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

    // 알림 팝업 (AlertType을 매개변수로 받음)
    private static void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}