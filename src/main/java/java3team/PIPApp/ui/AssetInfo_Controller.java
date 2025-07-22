package ui;

import config.AppConstants;
import config.StockList;
import config.Stocks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AssetInfo_Controller {
    @FXML private Label nameLabel;      // 회사명
    @FXML private Label tickerLabel;    // 티커
    @FXML private Label industryLabel;  // 산업군
    @FXML private Label countryLabel;   // 국가
    @FXML private Label currencyLabel;  // 통화
    @FXML private Label exchangeLabel;  // 거래소
    @FXML private Label ipoDateLabel;   // IPO일
    @FXML private Label marketCapitalizationLabel;  // 시가총액

    @FXML private ImageView logoUrlLabel;   // 로고 이미지

    @FXML private ComboBox<String> comboBoxID;  // 콤보박스


    @FXML
    public void initialize() {
        // 콤보박스 초기화
        comboBoxID.getItems().clear();
        for (Stocks stock : StockList.getStockArray()) {
            comboBoxID.getItems().add(stock.getTicker());
        }

        // 콤보박스 선택 이벤트 핸들러
        comboBoxID.setOnAction(e -> {
            String selectedTicker = comboBoxID.getValue();
            if (selectedTicker == null) return;

            for (Stocks stock : StockList.getStockArray()) {
                if (stock.getTicker().equals(selectedTicker)) {
                    updateLabel(stock);
                    break;
                }
            }
        });

        // 초기 값 설정 (있다면)
        if (!StockList.getStockArray().isEmpty()) {
            Stocks firstStock = StockList.getStockArray().get(0);
            comboBoxID.getSelectionModel().select(firstStock.getTicker());
            updateLabel(firstStock);
        }
    }

    // 라벨 업데이트
    private void updateLabel(Stocks stock) {
        nameLabel.setText(stock.getName());
        tickerLabel.setText(String.valueOf(stock.ticker));
        industryLabel.setText(String.valueOf(stock.industry));
        countryLabel.setText(String.valueOf(stock.country));
        currencyLabel.setText(String.valueOf(stock.currency));
        exchangeLabel.setText(String.valueOf(stock.exchange));
        ipoDateLabel.setText(String.valueOf(stock.ipoDate));
        marketCapitalizationLabel.setText(String.valueOf(stock.marketCapitalization) + "M");

        if (stock.logoUrl != null && stock.logoUrl.getImage() != null) {
            logoUrlLabel.setVisible(true);
            logoUrlLabel.setImage(stock.logoUrl.getImage());
        } else {
            logoUrlLabel.setVisible(false);
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
            _PIP_Launcher.launchAllPipWindows();
        }
        else {
            System.out.println("⚠ 종목이 비어있어 PIP창을 활성화시킬 수 없습니다.\n\n");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("StockPIP");
            alert.setHeaderText(null);
            alert.setContentText("종목을 먼저 입력해 주십시오.");
            alert.showAndWait();
        }
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

        if (AppConstants.ExternalSiteOption == 0) {
            try {
                Desktop.getDesktop().browse(new URI("https://finnhub.io/"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (AppConstants.ExternalSiteOption == 1) {
            try {
                Desktop.getDesktop().browse(new URI("https://finviz.com/"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (AppConstants.ExternalSiteOption == 2) {
            try {
                Desktop.getDesktop().browse(new URI("https://kr.investing.com/markets/united-states/"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}