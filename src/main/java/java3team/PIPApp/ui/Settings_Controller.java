package ui;

import com.jfoenix.controls.JFXToggleButton;
import config.*;
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

    @FXML RadioButton decimal_0, decimal_1, decimal_2, decimal_3, decimal_4, decimal_5;



    @FXML
    public void initialize() {
        /// ✅ AppConstants 값 → UI 컴포넌트 초기화
        // 알림 선택
        switch (AppConstants.notificationOption) {
            case 0 -> notification_1_Button.setSelected(true);
            case 1 -> notification_2_Button.setSelected(true);
            case 2 -> notification_3_Button.setSelected(true);
        }

        // 각 버튼에 대한 선택 상태 리스너 추가
        notification_1_Button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 팝업창 알림이 선택됐을 때
                AppConstants.notificationOption = 0;
                System.out.println("팝업창 알림으로 설정됨");
            }
        });

        notification_2_Button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 윈도우 시스템 알림이 선택됐을 때
                AppConstants.notificationOption = 1;
                System.out.println("윈도우 알림으로 설정됨");
            }
        });

        notification_3_Button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 사운드 알림이 선택됐을 때
                AppConstants.notificationOption = 2;
                System.out.println("알림 없음으로 설정됨");
            }
        });



        // PIP 소수점 표시
        ToggleGroup group = new ToggleGroup();
        decimal_0.setToggleGroup(group);
        decimal_1.setToggleGroup(group);
        decimal_2.setToggleGroup(group);
        decimal_3.setToggleGroup(group);
        decimal_4.setToggleGroup(group);
        decimal_5.setToggleGroup(group);

        switch (AppConstants.pipDecimalPoint) {
            case 0 -> decimal_0.setSelected(true);
            case 1 -> decimal_1.setSelected(true);
            case 2 -> decimal_2.setSelected(true);
            case 3 -> decimal_3.setSelected(true);
            case 4 -> decimal_4.setSelected(true);
            case 5 -> decimal_5.setSelected(true);
        }

        decimal_0.setOnAction(e -> {
            if (decimal_0.isSelected()) {
                AppConstants.pipDecimalPoint = 0;
                System.out.println("소수점 표시 안 함");
            }
        });
        decimal_1.setOnAction(e -> {
            if (decimal_1.isSelected()) {
                AppConstants.pipDecimalPoint = 1;
                System.out.println("소수점 1자리로 표시");
            }
        });
        decimal_2.setOnAction(e -> {
            if (decimal_2.isSelected()) {
                AppConstants.pipDecimalPoint = 2;
                System.out.println("소수점 2자리로 표시");
            }
        });
        decimal_3.setOnAction(e -> {
            if (decimal_3.isSelected()) {
                AppConstants.pipDecimalPoint = 3;
                System.out.println("소수점 3자리로 표시");
            }
        });
        decimal_4.setOnAction(e -> {
            if (decimal_4.isSelected()) {
                AppConstants.pipDecimalPoint = 4;
                System.out.println("소수점 4자리로 표시");
            }
        });
        decimal_5.setOnAction(e -> {
            if (decimal_5.isSelected()) {
                AppConstants.pipDecimalPoint = 5;
                System.out.println("소수점 5자리로 표시");
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
        AppConstants.notificationOption = 0;
        notification_1_Button.setSelected(true);
        notification_2_Button.setSelected(false);
        notification_3_Button.setSelected(false);

        // PIP 소수점 표시
        AppConstants.pipDecimalPoint = 2;
        decimal_0.setSelected(false);
        decimal_1.setSelected(false);
        decimal_2.setSelected(true);
        decimal_3.setSelected(false);
        decimal_4.setSelected(false);
        decimal_5.setSelected(false);

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