package ui;

import config.AppConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class PriceInfo_Controller {
    @FXML private Label nameLabel;    // 티커

    @FXML private Label currentPriceLabel;  // 현재가
    @FXML private Label openPriceLabel;     // 시가
    @FXML private Label highPriceLabel;     // 당일 최고가
    @FXML private Label lowPriceLabel;      // 당일 최저가
    @FXML private Label previousClosePriceLabel;      // 전일 종가

    @FXML private Label refreshTimeLabel;   // 최근 갱신 시간

    @FXML private ComboBox<String> comboBoxID;  // 콤보박스


    /// API 연동 이후 빈칸 라벨에 셋

    @FXML
    public void initialize() {
        // ComboBox에 NameList 넣기 (이름 목록만 보여줌)
        comboBoxID.getItems().setAll(AppConstants.NameList);

        // 현재 선택된 name이 있다면 그것도 선택해줌 (선택 유지 목적)
        if (AppConstants.NameList.contains(AppConstants.name)) {
            comboBoxID.setValue(AppConstants.name);
        }



        nameLabel.setText(AppConstants.name);

        currentPriceLabel.setText(String.valueOf(AppConstants.currentPrice));
        openPriceLabel.setText(String.valueOf(AppConstants.openPrice));
        highPriceLabel.setText(String.valueOf(AppConstants.highPrice));
        lowPriceLabel.setText(String.valueOf(AppConstants.lowPrice));
        previousClosePriceLabel.setText(String.valueOf(AppConstants.previousClosePrice));

        refreshTimeLabel.setText(String.valueOf(AppConstants.refreshTime));





        // name이 있을 때만 항목에 보임
        if (!AppConstants.name.isEmpty()) {
            nameLabel.setVisible(true);
            currentPriceLabel.setVisible(true);
            openPriceLabel.setVisible(true);
            highPriceLabel.setVisible(true);
            lowPriceLabel.setVisible(true);
            previousClosePriceLabel.setVisible(true);
            refreshTimeLabel.setVisible(true);
        } else {
            nameLabel.setVisible(false);
            currentPriceLabel.setVisible(false);
            openPriceLabel.setVisible(false);
            highPriceLabel.setVisible(false);
            lowPriceLabel.setVisible(false);
            previousClosePriceLabel.setVisible(false);
            refreshTimeLabel.setVisible(false);
        }
    }





    /// 사이드바 함수 ///
    // PIP 활성화
    @FXML
    private void pipClick(ActionEvent event) {
        // 현재 메인 스테이지 닫기
        Main.mainStage.close();

        // 새 PIP 스테이지 열기
        Stage pipStage = new Stage();
        _PIP_Main pipWindow = new _PIP_Main();
        pipWindow.pip_On(pipStage);
    }

    // 홈으로 이동
    @FXML
    private void handleHomeClick(MouseEvent event) {
        System.out.println("홈 클릭됨");
        try {
            // 홈.fxml 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 종목 정보로 이동
    @FXML
    private void handleAssetInfoClick(MouseEvent event) {
        System.out.println("종목 정보 클릭됨");
        try {
            // 종목 정보.fxml 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("assetInfo.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 시세 정보로 이동
    @FXML
    private void handlePriceInfoClick(MouseEvent event) { System.out.println("시세 정보 클릭됨"); }

    // 설정으로 이동
    @FXML
    private void handleSettingsClick(MouseEvent event) {
        System.out.println("설정 클릭됨");
        try {
            // 홈.fxml 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 외부 사이트로 이동
    @FXML
    private void handleExternalClick(MouseEvent event) {
        System.out.println("외부 사이트 클릭됨");

        try {
            Desktop.getDesktop().browse(new URI("https://finnhub.io/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
