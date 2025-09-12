package service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import config.*;
import pip.PipCloseManager;
import pip.PipMain;
import ui.*;

import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.Image;



public class AlertService {
    // 종목별 모니터링 타이머 관리
    private static final Map<String, Timeline> monitoringMap = new HashMap<>();
    // 종목별 알림창 최신화 관리
    private static final Map<String, Alert> alertMap = new HashMap<>();

    // 모니터링 시작
    public static void startMonitoring(Stocks stock) {
        String ticker = stock.getTicker();

        // 이미 모니터링 중이라면 중단 후 재시작
        if (monitoringMap.containsKey(ticker)) {
            stopMonitoring(ticker);
        }


        // 새 Timeline 생성
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(stock.getRefresh()), event -> {
            // 네트워크 검사
            if (!isInternetAvailable()) {
                System.out.println("⚠ 모니터링 중단 - 인터넷 연결 실패\n");
                return;
            }

            stock.alert_refreshQuote(); // 시세 갱신
            double currentPrice = stock.getCurrentPrice();
            DateTimeFormatter refreshTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String api_refreshTime = stock.getApi_refreshTime().format(refreshTime);
            //LocalDateTime api_refreshTime = stock.getApi_refreshTime();
            double targetPrice = stock.getTargetPrice();
            double stopPrice = stock.getStopPrice();
            String name = (stock.getToggleOption() == 0) ? stock.getName() : stock.getTicker();

            System.out.println("🔄🔄 [" + stock.getTicker() + "] 모니터링 자동 새로고침");

            // 목표가 도달 시
            if (currentPrice >= targetPrice && currentPrice != 0) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = LocalDateTime.now().format(formatter);
                String logLine = formatLog(0, timestamp, name, "목표가에 달성했습니다.", currentPrice, targetPrice);
                StockList.appendLog(logLine);
                LogInfoController.appendToLogArea(logLine);

                System.out.println(api_refreshTime + " - [" + ticker + "] 목표가 달성 / 현재가: $" + currentPrice + " 목표가: $" + targetPrice + "\n");
                if (AppConstants.notificationOption == 0) {
                    String AlertMessage = "(" + timestamp + ") " + name + "이(가) 목표가에 달성!  \n\n" + " 현재가: $" + currentPrice + "\n 목표가: $" + targetPrice;
                    showAlert(0, Alert.AlertType.INFORMATION, name, ticker, "📈 목표가 달성", AlertMessage, timestamp);
                    beep();
                } else if (AppConstants.notificationOption == 1) {
                    String NotificationMessage = name + "이(가) 목표가에 달성!  \n\n" + " 현재가: $" + currentPrice + "\n 목표가: $" + targetPrice;
                    showNotification("📈 목표가 달성!", NotificationMessage);

                    // 모니터링 종료 및 데이터 삭제
                    stopMonitoring(ticker);
                    StockList.getStockArray().removeIf(s -> s.getTicker().equals(ticker));

                    String logLineNotification = formatLog(0, timestamp, name);
                    StockList.appendLog(logLineNotification);
                    System.out.println("[" + ticker + "] - 삭제됨");
                }
            }

            // 손절가 도달 시
            if (currentPrice <= stopPrice && currentPrice != 0) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = LocalDateTime.now().format(formatter);
                String logLine = formatLog(1, timestamp, name, "손절가에 도달했습니다.", currentPrice, stopPrice);
                StockList.appendLog(logLine);
                LogInfoController.appendToLogArea(logLine);

                System.out.println(api_refreshTime + " - [" + ticker + "] 손절가 도달 / 현재가: $" + currentPrice + " 목표가: $" + stopPrice + "\n");
                if (AppConstants.notificationOption == 0) {
                    String AlertMessage = "(" + timestamp + ") " + name + "이(가) 손절가에 도달  \n\n" + " 현재가: $" + currentPrice + "\n 손절가: $" + stopPrice;
                    showAlert(1, Alert.AlertType.INFORMATION, name, ticker, "📉 손절가 도달", AlertMessage, timestamp);
                    beep();
                } else if (AppConstants.notificationOption == 1) {
                    String NotificationMessage = name + "이(가) 손절가에 도달  \n\n" + " 현재가: $" + currentPrice;
                    showNotification("📉 손절가 도달", NotificationMessage);

                    // 모니터링 종료 및 데이터 삭제
                    stopMonitoring(ticker);
                    StockList.getStockArray().removeIf(s -> s.getTicker().equals(ticker));

                    String logLineNotification = formatLog(1, timestamp, name);
                    StockList.appendLog(logLineNotification);
                    System.out.println("[" + ticker + "] - 삭제됨");
                }
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
    private static String formatLog(int type, String timestamp, String name, String message, double currentPrice, double targetOrStopPrice) {
        if (type == 0) {
            return timestamp + " - [" + name + "]이(가) " + message + " / 현재가: $" + currentPrice + " 목표가: $" + targetOrStopPrice;
        } else {
            return timestamp + " - [" + name + "]이(가) " + message + " / 현재가: $" + currentPrice + " --> 삭제됨";
        }
    }

    // 로그 포맷 함수 (윈도우 알림용)
    private static String formatLog(int type, String timestamp, String name) {
        if (type == 0) {
            return timestamp + " - [" + name + "]이(가) 삭제됨 (목표가 달성)";
        } else {
            return timestamp + " - [" + name + "]이(가) 삭제됨 (손절가 도달)";
        }
    }

    // 비프음
    private static void beep() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    // 알림 팝업 (AlertType을 매개변수로 받음)
    private static void showAlert(int type, Alert.AlertType warningType, String name, String ticker, String title, String message, String timestamp) {
        Platform.runLater(() -> {
            // 기존 알림창이 떠있으면 닫기
            if (alertMap.containsKey(name)) {
                Alert oldAlert = alertMap.get(name);
                if (oldAlert.isShowing()) {
                    oldAlert.close();
                }
            }

            Alert alert = new Alert(warningType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            javafx.scene.control.Label label = new javafx.scene.control.Label(message);
            label.setWrapText(true);

            alert.getDialogPane().setStyle("-fx-font-size: 15px;");
            alert.getDialogPane().setContent(label);

            if (type == 0) {
                ButtonType okButton = new ButtonType("종목 삭제", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("알림 중단", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(okButton, cancelButton);

                alert.resultProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == okButton) {
                        stopMonitoring(ticker);
                        StockList.getStockArray().removeIf(s -> s.getTicker().equals(ticker));
                        System.out.println("[" + name + "] 종목 삭제 버튼 눌림");

                        // 버튼 클릭 시점의 현재 시간
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String currentTime = now.format(formatter);

                        // 삭제 전용 매니저 호출
                        PipCloseManager closeManager = new PipCloseManager(PipMain.getPipWindows());
                        closeManager.closePipByTicker(ticker);


                        String logLineNotification = formatLog(0, currentTime, name);
                        StockList.appendLog(logLineNotification);
                        System.out.println("[" + ticker + "] - 삭제됨");
                    } else if (newVal == cancelButton) {
                        monitoringMap.get(ticker).stop();
                        System.out.println("[" + name + "] 알림 중단 버튼 눌림");
                    }
                });
            } else if (type == 1) {
                stopMonitoring(ticker);
                StockList.getStockArray().removeIf(s -> s.getTicker().equals(ticker));

                String logLineNotification = formatLog(1, timestamp, name);
                StockList.appendLog(logLineNotification);
                System.out.println("[" + ticker + "] - 삭제됨");
            }

            // 숨겨질 때 맵에서 제거
            alert.setOnHidden(e -> alertMap.remove(ticker));

            alertMap.put(ticker, alert);
            alert.show();
        });
    }


    // 네트워크 연결 진단
    private static boolean isInternetAvailable() {
        // 1차 검사 : Ping으로 빠르게 확인
        try {
            boolean pingSuccess = InetAddress.getByName("8.8.8.8").isReachable(1000);
            if (pingSuccess) {
                return true; // Ping 성공 → 인터넷 연결 확인
            }
        } catch (IOException e) {
            // Ping 도중 오류 → HTTP로 2차 확인 진행
        }

        // 2차 검사 : HTTP 요청으로 다시 확인
        try {
            URL url = new URL("https://www.google.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            int responseCode = connection.getResponseCode();

            // 응답 코드가 200~399면 성공으로 간주
            return (responseCode >= 200 && responseCode <= 399);
        } catch (IOException e) {
            // HTTP 요청 실패 → 인터넷 연결 안 됨
            return false;
        }
    }


    // Windows 알림 센터 알림
    public static void showNotification(String title, String message) {
        // SystemTray 지원 여부 확인
        if (!SystemTray.isSupported()) {
            System.out.println("경고: SystemTray가 지원되지 않는 시스템");
            return;
        }

        try {
            SystemTray tray = SystemTray.getSystemTray();

            // 아이콘 (없으면 작은 기본 이미지)
            Image image = Toolkit.getDefaultToolkit().createImage(new byte[0]);

            TrayIcon trayIcon = new TrayIcon(image, "Stock Alert");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Stock Alert Service");
            tray.add(trayIcon);

            // 알림 표시
            trayIcon.displayMessage(title, message, MessageType.INFO);

            // 잠시 뒤 Tray에서 제거 (안 해주면 중복 추가될 수 있음)
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    tray.remove(trayIcon);
                } catch (InterruptedException ignored) {}
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}