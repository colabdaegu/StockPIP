package ui.controller;

import PIPApp.Main;
import ai.AiAnalysis;
import ai.AnalysisPreparer;
import config.AppConstants;
import config.StockList;
import config.Stocks;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.util.Duration;
import pip.PipLauncher;
import net.NetworkManager;
import config.manager.PreferencesManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AiController {
    @FXML private TextArea firstLabel;
    @FXML private TextArea secondLabel;
    @FXML private TextArea thirdLabel;
    @FXML private TextArea fourthLabel;
    @FXML private TextArea fifthLabel;
    @FXML private TextArea sixthLabel;

    @FXML private Button resetButton;

    private Alert alert;
    private String tx1 = "", tx2 = "", tx3 = "", tx4 = "", tx5 = "", tx6 = "";


    @FXML
    public void initialize() {
        // 정보 세팅
        setInitialStock();
    }


    // 정보 세팅
    private void setInitialStock() {
        if (!AppConstants.tx.isEmpty()) {
            tx1 = (AppConstants.tx.size() > 0) ? AppConstants.tx.get(0) : "";
            tx2 = (AppConstants.tx.size() > 1) ? AppConstants.tx.get(1) : "";
            tx3 = (AppConstants.tx.size() > 2) ? AppConstants.tx.get(2) : "";
            tx4 = (AppConstants.tx.size() > 3) ? AppConstants.tx.get(3) : "";
            tx5 = (AppConstants.tx.size() > 4) ? AppConstants.tx.get(4) : "";
            tx6 = (AppConstants.tx.size() > 5) ? AppConstants.tx.get(5) : "";

            // 라벨 갱신
            updateLabel(tx1, tx2, tx3, tx4, tx5, tx6);

            resetButton.setText("갱신");
        }
    }

    // AI 분석 최신화
    @FXML
    private void resetClick(ActionEvent event) {
        // 네트워크 검사
        if (!NetworkManager.isInternetAvailable()) {
            showAlert(Alert.AlertType.ERROR,"StockPIP", "서버와의 연결에 실패하였습니다.");
            System.out.println("⚠ 인터넷 연결 실패\n");
            return;
        }

        if (!AppConstants.tx.isEmpty()) {
            AppConstants.tx.clear();
        }
        updateLabel("","","","","","");
        showAlert(Alert.AlertType.INFORMATION,"Now Loading...", "⏳ 종목 분석 중...");

        // 랜덤 종목 - 6개 선정
        List<String> selectedTickers = randomTicker();

        // 학습 데이터 선정 (JSON에 저장 데이터 저장)
        for (String ticker : selectedTickers) {
            AnalysisPreparer.start(ticker);
        }

        // AI 분석 (JSON 불러와서 Gemini한테 넘김[이전 주가 주세 75% + 현재 이슈 25%])
        for (String ticker : selectedTickers) {
            AppConstants.tx.add(AiAnalysis.start(ticker));
        }

        // 최종 입력
        tx1 = (AppConstants.tx.size() > 0) ? AppConstants.tx.get(0) : "";
        tx2 = (AppConstants.tx.size() > 1) ? AppConstants.tx.get(1) : "";
        tx3 = (AppConstants.tx.size() > 2) ? AppConstants.tx.get(2) : "";
        tx4 = (AppConstants.tx.size() > 3) ? AppConstants.tx.get(3) : "";
        tx5 = (AppConstants.tx.size() > 4) ? AppConstants.tx.get(4) : "";
        tx6 = (AppConstants.tx.size() > 5) ? AppConstants.tx.get(5) : "";

//        /// 임시
//        String geminiAnswer = AnalysisPreparer.askGemini("애플 주식 전망 간단히 한 문장으로 설명해줘");
//        tx1 = "[Gemini AI 응답]\n" + geminiAnswer;

        // 라벨 갱신
        updateLabel(tx1, tx2, tx3, tx4, tx5, tx6);
        resetButton.setText("갱신");
        System.out.println("AI 갱신됨\n");

        // 데이터 분석용 임시 JSON 파일 지우기 (data/)
//        deleteJsonDataFiles();

        hidePopup();
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


    private List<String> randomTicker() {
        // StockList에 저장된 전체 종목 티커 모으기
        List<String> tickers = new ArrayList<>();
        for (Stocks stock : StockList.getStockArray()) {
            tickers.add(stock.getTicker());
        }

        // 뽑은 종목 저장할 리스트
        List<String> picked = new ArrayList<>();

        // 6개 종목 선정시 종료
        int i = 0;
        while (!tickers.isEmpty() && i < 6) {
            int randomIndex = (int) (Math.random() * tickers.size());
            String randomElement = tickers.get(randomIndex);
            picked.add(randomElement);
            tickers.remove(randomIndex);
            i++;
        }

        System.out.println(picked);
        return picked;
    }


    // 데이터 분석용 임시 JSON 파일 지우기 (data/)
    private void deleteJsonDataFiles() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            System.out.println("⚠ 'data' 폴더가 존재하지 않습니다. 임시 파일 삭제 불가");
            return;
        }

        File[] files = dataDir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("⚠ 'data' 폴더가 비어 있습니다.");
            return;
        }

        for (File file : files) { file.delete(); }
        System.out.println("🗑 임시 파일 삭제 완료");
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