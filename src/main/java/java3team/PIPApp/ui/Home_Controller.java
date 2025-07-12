package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.Label;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

public class Home_Controller extends _GrobalVariableData {
    @FXML private TextField nameField;
    @FXML private TextField targetPriceField;
    @FXML private TextField stopPriceField;
    @FXML private TextField refreshField_Minute;
    @FXML private TextField refreshField_Second;

    @FXML private Label warningMessageLabel; // 경고 메시지용


    /// API 연동 및 보완 필요 . 회사이름 -> 자동 완성 모듈. 회사 이름만 받아와서 리스트 형태로 담고 있음
    @FXML
    public void initialize() {
        List<String> companyNames = List.of("Apple", "Alphabet Inc.", "Amazon", "Adobe"); // 임시(테스트용)

        // nameField에 자동완성 붙이기
        TextFields.bindAutoCompletion(nameField, companyNames);
    }


    // 저장 버튼의 이벤트
    @FXML
    private void saveClick(ActionEvent event) {
        // ✅ 경고 메시지 숨기고 시작
        warningMessageLabel.setVisible(false);
        warningMessageLabel.setText("");



        String name_Str = nameField.getText().trim();
        String targetPriceStr = targetPriceField.getText().trim();
        String stopPriceStr = stopPriceField.getText().trim();
        String refreshMinuteStr = refreshField_Minute.getText().trim();
        String refreshSecondStr = refreshField_Second.getText().trim();

        // 유효성 검사 - 빈칸 유무 (분이나 초는 둘 중에 하나만 입력돼도 됨)
        if (name_Str.isEmpty() || targetPriceStr.isEmpty() || stopPriceStr.isEmpty() || (refreshMinuteStr.isEmpty() && refreshSecondStr.isEmpty())){
            warningMessageLabel.setVisible(true);
            warningMessageLabel.setText("모든 항목을 입력해 주세요.");
            System.out.println("⚠⚠ 입력 누락\n\n");
            return;
        }


        // 기존 코드 Clear
        resetData();



        // 이름 유효성 검사
        ////(추가 필요)
        name = name_Str;
        /// 해당하는 이름의 회사가 존재하지 않으면 작업을 중단하는 예외처리 필요


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

        // 새로고림 주기-초는 입력된 경우에만 파싱 시도
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


        // 최종 결과 출력
        System.out.println("종목명: " + name);
        System.out.println("목표가: " + targetPrice);
        System.out.println("손절가: " + stopPrice);
        System.out.println("새로고침: " + refreshMinute + "분 " + refreshSecond + "초");
        System.out.println();

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



    // 초기화 버튼의 이벤트
    @FXML
    private void resetClick(ActionEvent event) {
        // ✅ 초기화 시 경고 메시지 숨김
        warningMessageLabel.setVisible(false);
        warningMessageLabel.setText("");


        // 사용자 입력 필드 초기화
        nameField.clear();
        targetPriceField.clear();
        stopPriceField.clear();
        refreshField_Minute.clear();
        refreshField_Second.clear();

        // 기존 코드 Clear
        resetData();

        System.out.println("초기화됨\n\n");
    }





    /// 사이드바 함수 ///
    // PIP 활성화
    @FXML
    private void pipClick(ActionEvent event) {
        // ✅ 경고 메시지 숨김
        warningMessageLabel.setVisible(false);
        warningMessageLabel.setText("");

        // 현재 메인 스테이지 닫기
        Main.mainStage.close();

        // 새 PIP 스테이지 열기
        Stage pipStage = new Stage();
        _PIP_test pipWindow = new _PIP_test();
        pipWindow.pip_On(pipStage);
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

        try {
            Desktop.getDesktop().browse(new URI("https://finnhub.io/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
