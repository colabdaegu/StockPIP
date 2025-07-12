package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AssetInfo_Controller extends _GrobalVariableData {
    @FXML private Label nameLabel;      // 회사명
    @FXML private Label tickerLabel;    // 티커
    @FXML private Label industryLabel;  // 산업군
    @FXML private Label countryLabel;   // 국가
    @FXML private Label currencyLabel;  // 통화
    @FXML private Label exchangeLabel;  // 거래소
    @FXML private Label ipoDateLabel;   // IPO일
    @FXML private Label marketCapitalizationLabel;  // 시가총액

    @FXML private ImageView logoUrlLabel;   // 로고 이미지


    /// API 연동 이후 빈칸 라벨에 셋
    /*
    @FXML
    public void initialize() {
        nameLabel.setText(name);
        tickerLabel.setText(ticker);
        industryLabel.setText(industry);
        countryLabel.setText(country);
        currencyLabel.setText(currency);
        exchangeLabel.setText(exchange);
        ipoDateLabel.setText(ipoDate);
        marketCapitalizationLabel.setText(marketCapitalization);


    }
    */




    /// 사이드바 함수 ///
    // PIP 활성화
    @FXML
    private void pipClick(ActionEvent event) {
        // 현재 메인 스테이지 닫기
        Main.mainStage.close();

        // 새 PIP 스테이지 열기
        Stage pipStage = new Stage();
        _PIP_test pipWindow = new _PIP_test();
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
    private void handleAssetInfoClick(MouseEvent event) { System.out.println("종목 정보 클릭됨"); }

    // 시세 정보로 이동
    @FXML
    private void handlePriceInfoClick(MouseEvent event) {
        System.out.println("시세 정보 클릭됨");
        try {
            // 홈.fxml 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("priceInfo.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
