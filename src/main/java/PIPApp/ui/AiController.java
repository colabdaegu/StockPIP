package ui;

import config.StockList;
import config.Stocks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import pip.PipLauncher;
import service.NetworkManager;
import service.PreferencesManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AiController {
    @FXML private Label firstLabel;
    @FXML private Label secondLabel;
    @FXML private Label thirdLabel;
    @FXML private Label fourthLabel;
    @FXML private Label fifthLabel;
    @FXML private Label sixthLabel;

    @FXML private Button resetButton;


    @FXML
    public void initialize() {
        // 초기 값 세팅 (있다면)
        setInitialStock();

        // updateLabel();
    }


    // 초기 값 세팅 (있다면)
    private void setInitialStock() {

    }

    // AI 분석 최신화
    @FXML
    private void resetClick(ActionEvent event) {
        // 네트워크 검사
        if (!NetworkManager.isInternetAvailable()) {
            System.out.println("⚠ 모니터링 중단 - 인터넷 연결 실패\n");
            return;
        }

        resetButton.setText("갱신");
        // updateLabel();

        System.out.println("AI 갱신됨\n");
    }

    // 라벨 업데이트
    private void updateLabel(Stocks stock) {
        firstLabel.setText(stock.getName());
        secondLabel.setText(stock.getName());
        thirdLabel.setText(stock.getName());
        fourthLabel.setText(stock.getName());
        fifthLabel.setText(stock.getName());
        sixthLabel.setText(stock.getName());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("assetInfo.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("priceInfo.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logInfo.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
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