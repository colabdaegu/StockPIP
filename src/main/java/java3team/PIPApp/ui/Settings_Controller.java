package ui;

import com.jfoenix.controls.JFXToggleButton;
import config.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Settings_Controller {
    @FXML private JFXToggleButton pipToggle;

    @FXML private Slider fontSizeSlider;

    @FXML private ToggleButton popUpAlertButton;
    @FXML private ToggleButton systemAlertButton;
    @FXML private ToggleButton soundAlertButton;

    @FXML RadioButton brightTheme, darkTheme;



    @FXML
    public void initialize() {
        /// ✅ AppConstants 값 → UI 컴포넌트 초기화
        pipToggle.setSelected(AppConstants.pipOutlineOption);   // PIP 테두리 고정
        fontSizeSlider.setValue(AppConstants.pipFontSize);      // PIP 폰트

        switch (AppConstants.AlertOption) {                     // 알림 방식
            case 0 -> popUpAlertButton.setSelected(true);
            case 1 -> systemAlertButton.setSelected(true);
            case 2 -> soundAlertButton.setSelected(true);
        }



        ///  알림 방식 설정(임시)
        // 각 버튼에 대한 선택 상태 리스너 추가
        popUpAlertButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 팝업창 알림이 선택됐을 때
                AppConstants.AlertOption = 0;
                System.out.println("팝업창 알림으로 설정됨");
            }
        });

        systemAlertButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 윈도우 시스템 알림이 선택됐을 때
                AppConstants.AlertOption = 1;
                System.out.println("윈도우 시스템 알림으로 설정됨");
            }
        });

        soundAlertButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 사운드 알림이 선택됐을 때
                AppConstants.AlertOption = 2;
                System.out.println("소리로만 알림");
            }
        });




        // PIP 폰트 사이즈 설정
        fontSizeSlider.setValue(_PIP_SettingsFontSize.getFontSize());

        fontSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double fontSize = newValue.doubleValue();
            _PIP_SettingsFontSize.setFontSize(fontSize);
            System.out.println("PIP 폰트 크기: " + String.format("%.1f", fontSize));

            /// ▼ pip글자변수 지정 필요
            // pipLabel.setStyle("-fx-font-size: " + fontSize + "px;");
        });




        /// 테마 설정 (프로젝트 막바지에 설정 예정)
        ToggleGroup group = new ToggleGroup();
        brightTheme.setToggleGroup(group);
        darkTheme.setToggleGroup(group);

        switch (AppConstants.UI_theme) {                        // 테마 기본 설정
            case 0 -> brightTheme.setSelected(true);
            case 1 -> darkTheme.setSelected(true);
        }

        brightTheme.setOnAction(e -> {
            if (brightTheme.isSelected()) {
                AppConstants.UI_theme = 0;
                System.out.println("밝은 테마로 변경됨");
            }
        });
        darkTheme.setOnAction(e -> {
            if (darkTheme.isSelected()) {
                AppConstants.UI_theme = 1;
                System.out.println("어두운 테마로 변경됨");
            }
        });
    }


    // PIP 테두리 고정 설정
    @FXML
    private void handlePipToggle(ActionEvent event) {
        if (pipToggle.isSelected()) {
            // PIP 설정이 ON 상태일 때
            AppConstants.pipOutlineOption = true;
            System.out.println("PIP 테두리 고정: ON");
        } else {
            // PIP 설정이 OFF 상태일 때
            AppConstants.pipOutlineOption = false;
            System.out.println("PIP 테두리 고정: OFF");
        }
    }




    /// 기본값 설정
    @FXML
    private void defaultClick(ActionEvent event) {
        System.out.println("설정을 기본값으로 되돌림\n");

        // 알림 방식 설정: 토스트 방식으로 설정
        AppConstants.AlertOption = 0;
        popUpAlertButton.setSelected(true);
        systemAlertButton.setSelected(false);
        soundAlertButton.setSelected(false);

        // PIP 테두리 고정 설정: 기본값으로 설정
        AppConstants.pipOutlineOption = false;
        pipToggle.setSelected(false);

        // PIP 폰트 크기 설정: 기본값 28로 설정
        AppConstants.pipFontSize = 28.0;
        fontSizeSlider.setValue(28.0);

        // 테마 설정: 다크 테마로 설정
        AppConstants.UI_theme = 1;
        brightTheme.setSelected(false);
        darkTheme.setSelected(true);
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