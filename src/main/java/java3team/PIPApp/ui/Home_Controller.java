package ui;

import config.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.Label;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

public class Home_Controller {
    @FXML private ListView<String> listViewId;

    @FXML private TextField tickerField;
    @FXML private TextField targetPriceField;
    @FXML private TextField stopPriceField;
    @FXML private TextField refreshField_Minute;
    @FXML private TextField refreshField_Second;

    @FXML private Label warningMessageLabel; // 경고 메시지용

    private List<String> companyNames;
    private ObservableList<String> nameList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        for (Stocks p : StockList.stockArray) {
            nameList.add(p.getTicker());  // 이름만 추출해서 리스트에 추가
        }
        listViewId.setItems(nameList);


        // 자동완성 연결
        companyNames = FileLoader.loadLines("ticker/ticker_list.txt");
        TextFields.bindAutoCompletion(tickerField, companyNames);

        /// 종목 이름 리스트(테스트용 임시)
//        //List<String> companyNames = List.of("Apple", "Alphabet Inc.", "Amazon", "Adobe"); // 임시(테스트용)
//        companyNames = List.of(
//                "Apple", "Amazon", "Baidu", "Booking Holdings", "Cisco", "Citigroup",
//                "Dell", "Dropbox", "eBay", "Electronic Arts", "Facebook", "Ford",
//                "Google", "General Motors", "HP", "Honeywell", "IBM", "Intel",
//                "Johnson & Johnson", "JetBlue", "Kellogg", "Kraft Heinz", "LinkedIn", "Lyft",
//                "Microsoft", "Meta", "Netflix", "Nvidia", "Oracle", "OpenAI Inc.", "PayPal", "Pinterest",
//                "Qualcomm", "Qurate Retail", "Reddit", "Roku", "Salesforce", "Samsung",
//                "Tesla", "Twitter", "Uber", "Upstart", "Verizon", "Visa", "Walmart", "Workday",
//                "Xilinx", "Xiaomi", "Yahoo", "Yelp", "Zoom", "Zillow"
//        );
//        // nameField에 자동완성 붙이기
//        TextFields.bindAutoCompletion(nameField, companyNames);



        listViewId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                // 선택된 이름에 해당하는 Stocks 객체 검색
                for (Stocks s : StockList.getStockArray()) {
                    if (s.getTicker().equals(newValue)) {
                        // 필드에 값 세팅
                        tickerField.setText(s.getTicker());
                        targetPriceField.setText(String.format("%.10f", s.getTargetPrice()).replaceAll("\\.?0+$", ""));
                        stopPriceField.setText(String.format("%.10f", s.getStopPrice()).replaceAll("\\.?0+$", ""));
                        refreshField_Minute.setText(String.valueOf(s.getRefreshMinute()));
                        refreshField_Second.setText(String.valueOf(s.getRefreshSecond()));
                        break;
                    }
                }
            }
        });
    }


    // 저장 버튼의 이벤트
    @FXML
    private void saveClick(ActionEvent event) {
        // ✅ 경고 메시지 숨기고 시작
        warningMessageLabel.setVisible(false);
        warningMessageLabel.setText("");



        String ticker_Str = tickerField.getText().toUpperCase().trim();
        String targetPriceStr = targetPriceField.getText().trim();
        String stopPriceStr = stopPriceField.getText().trim();
        String refreshMinuteStr = refreshField_Minute.getText().trim();
        String refreshSecondStr = refreshField_Second.getText().trim();


        // 이름 유효성 검사
        String ticker = ticker_Str;
        if (!companyNames.contains(ticker)) {
            warningMessageLabel.setVisible(true);
            warningMessageLabel.setText("종목 이름이 유효하지 않습니다.");
            System.out.println("⚠ 존재하지 않는 종목 이름\n");
            return;
        }

        // 유효성 검사 - 빈칸 유무 (분이나 초는 둘 중에 하나만 입력돼도 됨)
        if (ticker_Str.isEmpty() || targetPriceStr.isEmpty() || stopPriceStr.isEmpty() || (refreshMinuteStr.isEmpty() && refreshSecondStr.isEmpty()) || ((!refreshMinuteStr.isEmpty() && !refreshMinuteStr.matches("\\d+")) || (!refreshSecondStr.isEmpty() && !refreshSecondStr.matches("\\d+"))) || ((refreshMinuteStr.isEmpty() ? 0 : Integer.parseInt(refreshMinuteStr)) + (refreshSecondStr.isEmpty() ? 0 : Integer.parseInt(refreshSecondStr)) == 0)){
            warningMessageLabel.setVisible(true);
            warningMessageLabel.setText("모든 항목을 올바르게 입력해 주세요.");
            System.out.println("⚠⚠ 입력 누락\n\n");
            return;
        }

        // 값 파싱
        double targetPrice, stopPrice;
        int refreshMinute = 0, refreshSecond = 0;


        // 목표가 유효성 검사
        try {
            targetPrice = Double.parseDouble(targetPriceStr);
        } catch (NumberFormatException e) {
            warningMessageLabel.setVisible(true);
            warningMessageLabel.setText("목표가는 숫자 형식으로 입력해 주세요.");
            System.out.println("⚠ 목표가 - 데이터 타입이 맞지 않음\n");
            return;
        }

        // 손절가 유효성 검사
        try {
            stopPrice = Double.parseDouble(stopPriceStr);
        } catch (NumberFormatException e) {
            warningMessageLabel.setVisible(true);
            warningMessageLabel.setText("손절가는 숫자 형식으로 입력해 주세요.");
            System.out.println("⚠ 손절가 - 데이터 타입이 맞지 않음\n");
            return;
        }

        // 새로고침 주기-분은 입력된 경우에만 파싱 시도
        if (!refreshMinuteStr.isEmpty()) {
            try {
                refreshMinute = Integer.parseInt(refreshMinuteStr);
            } catch (NumberFormatException e) {
                warningMessageLabel.setVisible(true);
                warningMessageLabel.setText("숫자(정수) 형식으로 입력해 주세요.");
                System.out.println("⚠ 새로고침(분) - 데이터 타입이 맞지 않음\n");
                return;
            }
        }
        // 새로고침 주기-초는 입력된 경우에만 파싱 시도
        if (!refreshSecondStr.isEmpty()) {
            try {
                refreshSecond = Integer.parseInt(refreshSecondStr);
            } catch (NumberFormatException e) {
                warningMessageLabel.setVisible(true);
                warningMessageLabel.setText("숫자(정수) 형식으로 입력해 주세요.");
                System.out.println("⚠ 새로고침(초) - 데이터 타입이 맞지 않음\n");
                return;
            }
        }
        // 새로고침 값이 0이면 유효성 처리
        if ((refreshMinute + refreshSecond) == 0) {
            warningMessageLabel.setVisible(true);
            warningMessageLabel.setText("새로고침 주기는 0이 될 수 없습니다.");
            System.out.println("⚠ 새로고침 주기는 0이 될 수 없음\n");
            return;
        }


        // 최종 결과 출력
        System.out.println("종목명: " + ticker);
        System.out.println("목표가: " + targetPrice);
        System.out.println("손절가: " + stopPrice);
        System.out.println("새로고침: " + refreshMinute + "분 " + refreshSecond + "초");
        System.out.println();


        // 동일 종목 이름이 있다면 기존 항목 삭제
        for (int i = 0; i < StockList.getStockArray().size(); i++) {
            Stocks s = StockList.getStockArray().get(i);
            if (s.getTicker().equals(ticker)) {
                StockList.getStockArray().remove(i);
                System.out.println(ticker + ", 업데이트됨\n");
                break;
            }
        }

        // // ✅ StockList에 저장
        Stocks newStock = new Stocks(ticker, targetPrice, stopPrice, refreshMinute, refreshSecond);
        StockList.getStockArray().add(newStock);
        //AlertService.startMonitoring(newStock);       /// ✅ 알림 이벤트 활성화

        // ✅ ListView 갱신
        nameList.clear();
        for (Stocks s : StockList.getStockArray()) {
            nameList.add(s.getTicker());
        }

        // 저장완료 팝업
        showAlert("StockPIP", "성공적으로 저장되었습니다!");
    }
    // 성공 팝업
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    // 삭제 버튼의 이벤트
    @FXML
    private void resetClick(ActionEvent event) {
        // ✅ 초기화 시 경고 메시지 숨김
        warningMessageLabel.setVisible(false);
        warningMessageLabel.setText("");


        // 현재 입력된 이름
        String currentName = tickerField.getText().trim();

        // NameList와 ListView에서 해당 이름이 있을 때만 삭제
        for (int i = 0; i < StockList.getStockArray().size(); i++) {
            Stocks s = StockList.getStockArray().get(i);
            if (s.getTicker().equals(currentName)) {
                //AlertService.stopMonitoring(currentName);     /// ✅ 해당 종목의 알림 이벤트 삭제
                nameList.remove(currentName);
                StockList.getStockArray().remove(i);
                listViewId.setItems(nameList);
                break;
            }
        }


        if (StockList.getStockArray().isEmpty()){
            // 사용자 입력 필드 초기화
            tickerField.clear();
            targetPriceField.clear();
            stopPriceField.clear();
            refreshField_Minute.clear();
            refreshField_Second.clear();
        }


        System.out.println("삭제됨\n\n");
    }





    /// 사이드바 함수 ///
    // PIP 활성화
    @FXML
    private void pipClick(ActionEvent event) {
        if (!StockList.getStockArray().isEmpty()){
            // ✅ 경고 메시지 숨김
            warningMessageLabel.setVisible(false);
            warningMessageLabel.setText("");

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
    private void handleHomeClick(MouseEvent event) { System.out.println("홈 클릭됨"); }

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