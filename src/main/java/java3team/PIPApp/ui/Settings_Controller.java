package ui;

import com.jfoenix.controls.JFXToggleButton;
import config.StockList;
import config.AppConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Settings_Controller {
    @FXML private JFXToggleButton pipToggle;

    @FXML private Slider fontSizeSlider;

    @FXML private ToggleButton notification_1_Button;
    @FXML private ToggleButton notification_2_Button;
    @FXML private ToggleButton notification_3_Button;

    @FXML RadioButton soundOff, soundOn;



    @FXML
    public void initialize() {
        /// ✅ AppConstants 값 → UI 컴포넌트 초기화
        // 알림 선택
        switch (AppConstants.NotificationOption) {
            case 0 -> notification_1_Button.setSelected(true);
            case 1 -> notification_2_Button.setSelected(true);
            case 2 -> notification_3_Button.setSelected(true);
        }

        // 각 버튼에 대한 선택 상태 리스너 추가
        notification_1_Button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 팝업창 알림이 선택됐을 때
                AppConstants.NotificationOption = 0;
                System.out.println("팝업창 알림으로 설정됨");
            }
        });

        notification_2_Button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 윈도우 시스템 알림이 선택됐을 때
                AppConstants.NotificationOption = 1;
                System.out.println("윈도우 알림으로 설정됨");
            }
        });

        notification_3_Button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 사운드 알림이 선택됐을 때
                AppConstants.NotificationOption = 2;
                System.out.println("알림 없음으로 설정됨");
            }
        });



        // 소리 알림 설정
        ToggleGroup group = new ToggleGroup();
        soundOff.setToggleGroup(group);
        soundOn.setToggleGroup(group);

        if (AppConstants.AlertSound) {
            soundOn.setSelected(true);
        }
        else {
            soundOff.setSelected(true);
        }

        soundOff.setOnAction(e -> {
            if (soundOff.isSelected()) {
                AppConstants.AlertSound = false;
                System.out.println("소리 알림 끔");
            }
        });
        soundOn.setOnAction(e -> {
            if (soundOn.isSelected()) {
                AppConstants.AlertSound = true;
                System.out.println("소리 알림 킴");
            }
        });



        pipToggle.setSelected(AppConstants.pipOutlineOption);   // PIP 테두리 고정
        fontSizeSlider.setValue(AppConstants.pipFontSize);      // PIP 폰트

        // PIP 폰트 사이즈 설정
        fontSizeSlider.setValue(_PIP_SettingsFontSize.getFontSize());

        fontSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double fontSize = newValue.doubleValue();
            _PIP_SettingsFontSize.setFontSize(fontSize);
            System.out.println("PIP 폰트 크기: " + String.format("%.1f", fontSize));
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

        // 알림 설정
        AppConstants.NotificationOption = 0;
        notification_1_Button.setSelected(true);
        notification_2_Button.setSelected(false);
        notification_3_Button.setSelected(false);

        // 소리 알림 설정
        AppConstants.AlertSound = true;
        soundOff.setSelected(false);
        soundOn.setSelected(true);

        // PIP 테두리 고정 설정: 기본값으로 설정
        AppConstants.pipOutlineOption = false;
        pipToggle.setSelected(false);

        // PIP 폰트 크기 설정: 기본값 28로 설정
        AppConstants.pipFontSize = 28.0;
        fontSizeSlider.setValue(28.0);
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

    // 로그로 이동
    @FXML
    private void handleLogClick(MouseEvent event) {
        System.out.println("로그 클릭됨");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logInfo.fxml"));
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
            Desktop.getDesktop().browse(new URI("https://finviz.com/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}