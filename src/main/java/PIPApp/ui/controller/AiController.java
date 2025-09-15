package ui.controller;

import PIPApp.Main;
import config.StockList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import pip.PipLauncher;
import net.NetworkManager;
import config.manager.PreferencesManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AiController {
    @FXML private TextArea firstLabel;
    @FXML private TextArea secondLabel;
    @FXML private TextArea thirdLabel;
    @FXML private TextArea fourthLabel;
    @FXML private TextArea fifthLabel;
    @FXML private TextArea sixthLabel;

    @FXML private Button resetButton;

    private Alert alert;
    private String tx1, tx2, tx3, tx4, tx5, tx6;


    @FXML
    public void initialize() {
        // 정보 세팅
        setInitialStock();

        // updateLabel();
    }


    // 정보 세팅
    private void setInitialStock() {
        tx1 = "[AAPL]\n" +
                "**상승 예상**\n" +
                "\uD83D\uDCC8 $240 - $245 \uD83D\uDCC8\n\n" +
                "\uD83D\uDD0E 단기 전망: 아이폰 15 출시 기대감과 서비스 부문 성장으로 긍정적인 모멘텀이 이어질 가능성이 높아요. \uD83D\uDE80";
        tx2 = "[Microsoft Corp]\n" +
                "**하락 또는 조정 예상**\n" +
                "\uD83D\uDCC9 $500 - $510 \uD83D\uDCC9\n\n" +
                "\uD83D\uDD0E 단기 전망: 클라우드와 AI 성장세는 좋지만 최근 고점이라 단기 차익실현 매물이 나올 수 있어요. ⚠";
        tx3 = "[AMZN]\n" +
                "**하락 또는 정체 예상**\n" +
                "\uD83D\uDCC9 $220 - $230 \uD83D\uDCC9\n\n" +
                "\uD83D\uDD0E 단기 전망: 물류비와 리테일 둔화가 부담이라 단기 조정 가능성이 있어요. \uD83D\uDCE6";
    }

    // AI 분석 최신화
    @FXML
    private void resetClick(ActionEvent event) {
        // 네트워크 검사
        if (!NetworkManager.isInternetAvailable()) {
            updateLabel("","","","","","");
            showAlert(Alert.AlertType.ERROR,"StockPIP", "서버와의 연결에 실패하였습니다.");
            System.out.println("⚠ 인터넷 연결 실패\n");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION,"Now Loading...", "⏳ 종목 분석 중...");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            // 라벨 업데이트
            updateLabel(tx1, tx2, tx3, tx4, tx5, tx6);
            resetButton.setText("갱신");
            System.out.println("AI 갱신됨\n");

            hidePopup();
        }));
        timeline.play();
    }

    // 라벨 업데이트
    private void updateLabel(String tx1, String tx2, String tx3, String tx4, String tx5, String tx6) {
        firstLabel.setText(tx1);
        secondLabel.setText(tx2);
        thirdLabel.setText(tx3);
        fourthLabel.setText(tx4);
        fifthLabel.setText(tx5);
        sixthLabel.setText(tx6);
    }

    // 로딩 팝업
    private void showAlert(Alert.AlertType type, String title, String message) {
        alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    // 팝업 숨기기
    private void hidePopup() {
        if (alert != null) {
            alert.hide();
        }
    }


    /// 사이드바 함수 ///
    // PIP 활성화
    @FXML
    private void pipClick(ActionEvent event) {
        if (!StockList.getStockArray().isEmpty()){
            // 현재 메인 스테이지 닫기
            Main.mainStage.close();

            // 새 PIP 스테이지 열기
            PipLauncher.launchAllPipWindows();
        }
        else {
            System.out.println("⚠ 종목이 비어있어 PIP창을 활성화시킬 수 없습니다.\n\n");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("StockPIP");
            alert.setHeaderText(null);
            alert.setContentText("종목을 먼저 입력해 주십시오.");
            alert.showAndWait();
        }
        new PreferencesManager().saveSettings();
    }

    // 홈으로 이동
    @FXML
    private void handleHomeClick(MouseEvent event) {
        System.out.println("홈 클릭됨");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/home.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);

            new PreferencesManager().saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 종목 정보로 이동
    @FXML
    private void handleAssetInfoClick(MouseEvent event) {
        System.out.println("종목 정보 클릭됨");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/assetInfo.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);

            new PreferencesManager().saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 시세 정보로 이동
    @FXML
    private void handlePriceInfoClick(MouseEvent event) {
        System.out.println("시세 정보 클릭됨");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/priceInfo.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);

            new PreferencesManager().saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 로그로 이동
    @FXML
    private void handleLogClick(MouseEvent event) {
        System.out.println("로그 클릭됨");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/logInfo.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);

            new PreferencesManager().saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 설정으로 이동
    @FXML
    private void handleSettingsClick(MouseEvent event) {
        System.out.println("설정 클릭됨");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/settings.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);

            new PreferencesManager().saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 외부 사이트로 이동
    @FXML
    private void handleExternalClick(MouseEvent event) {
        System.out.println("외부 사이트 클릭됨");
        new PreferencesManager().saveSettings();

        try {
            Desktop.getDesktop().browse(new URI("https://finviz.com/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // AI 분석으로 이동
    @FXML
    private void handleAiClick(MouseEvent event) { System.out.println("AI 분석 클릭됨"); }
}