package ui.controller;

import javafx.scene.control.Label;
import pip.PipLauncher;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import PIPApp.Main;
import config.*;
import config.manager.PreferencesManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class SettingsController {
    @FXML private JFXToggleButton pipToggle;

    @FXML private Slider fontSizeSlider;

    @FXML private ToggleButton notification_1_Button;
    @FXML private ToggleButton notification_2_Button;
    @FXML private ToggleButton notification_3_Button;

    @FXML private Spinner<Integer> decimalSpinner;
    @FXML private Label decimalLabel;

    @FXML RadioButton mode_A, mode_B;
    @FXML RadioButton mode_row, mode_column;


    @FXML
    public void initialize() {
        /// ✅ AppConstants 값 → UI 컴포넌트 초기화
        // 알림 선택
        notificationSettings();

        // PIP 모드
        pipModeSettings();
        // PIP 모드 - 방향
        pipModeDirectionSettings();

        // PIP 소수점 표시
        pipDecimalPointSettings();

        // PIP 테두리 고정
        pipOutlineSettings();

        // PIP 폰트 사이즈 설정
        pipFontSettings();
    }


    // 알림 선택
    private void notificationSettings() {
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
    }

    // PIP 모드
    private void pipModeSettings() {
        ToggleGroup group = new ToggleGroup();
        mode_A.setToggleGroup(group);
        mode_B.setToggleGroup(group);

        switch (AppConstants.pipModeOption) {
            case 0 -> {
                mode_A.setSelected(true);
                setDirectionControlsEnabled(false);
            }
            case 1 -> {
                mode_B.setSelected(true);
                setDirectionControlsEnabled(true);
            }
        }

        mode_A.setOnAction(e -> {
            if (mode_A.isSelected()) {
                AppConstants.pipModeOption = 0;
                System.out.println("PIP - A모드로 전환");

                setDirectionControlsEnabled(false);
            }
        });
        mode_B.setOnAction(e -> {
            if (mode_B.isSelected()) {
                AppConstants.pipModeOption = 1;
                System.out.println("PIP - B모드로 전환");

                setDirectionControlsEnabled(true);
            }
        });
    }

    // PIP 모드 - 방향
    private void pipModeDirectionSettings() {
        ToggleGroup groupDirection = new ToggleGroup();
        mode_row.setToggleGroup(groupDirection);
        mode_column.setToggleGroup(groupDirection);

        switch (AppConstants.pipModeDirectionOption) {
            case 0 -> mode_row.setSelected(true);
            case 1 -> mode_column.setSelected(true);
        }

        mode_row.setOnAction(e -> {
            if (mode_row.isSelected()) {
                AppConstants.pipModeDirectionOption = 0;
                System.out.println("PIP 방향 - 세로로 전환");
            }
        });
        mode_column.setOnAction(e -> {
            if (mode_column.isSelected()) {
                AppConstants.pipModeDirectionOption = 1;
                System.out.println("PIP 방향 - 가로로 전환");
            }
        });
    }
    // PIP 모드 - 방향 : 라디오버튼 활성/비활성 제어
    private void setDirectionControlsEnabled(boolean enabled) {
        mode_row.setDisable(!enabled);
        mode_column.setDisable(!enabled);
    }

    // PIP 소수점 표시
    private void pipDecimalPointSettings() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, AppConstants.pipDecimalPoint);
        decimalSpinner.setValueFactory(valueFactory);

        decimalSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                AppConstants.pipDecimalPoint = newVal;
                if (newVal == 0) {
                    decimalLabel.setText("(소수점 표시 없음)");
                    System.out.println("소수점 표시 안 함");
                } else {
                    decimalLabel.setText("자리");
                    System.out.println("소수점 " + newVal + "자리로 표시");
                }
            }
        });
    }

    // PIP 테두리 고정 설정
    private void pipOutlineSettings() {
        // 초기 상태 반영
        pipToggle.setSelected(AppConstants.pipOutlineOption);

        // 이벤트 리스너
        pipToggle.setOnAction(event -> {
            if (pipToggle.isSelected()) {
                // PIP 설정이 ON 상태일 때
                AppConstants.pipOutlineOption = true;
                System.out.println("PIP 테두리 고정: ON");
            } else {
                // PIP 설정이 OFF 상태일 때
                AppConstants.pipOutlineOption = false;
                System.out.println("PIP 테두리 고정: OFF");
            }
        });
    }

    // PIP 폰트 사이즈 설정
    private void pipFontSettings() {
        fontSizeSlider.setValue(AppConstants.pipFontSize);      // PIP 폰트

        fontSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double fontSize = newValue.doubleValue();
            AppConstants.pipFontSize = fontSize;
            System.out.println("PIP 폰트 크기: " + String.format("%.1f", fontSize));
        });
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

        // PIP 모드
        AppConstants.pipModeOption = 0;
        mode_A.setSelected(true);
        mode_B.setSelected(false);
        // PIP 모드 - 방향
        AppConstants.pipModeDirectionOption = 0;
        mode_row.setSelected(true);
        mode_column.setSelected(false);
        // PIP 모드 - 방향 : 활성화 여부
        setDirectionControlsEnabled(false);

        // PIP 소수점 표시
        AppConstants.pipDecimalPoint = 2;
        decimalSpinner.getValueFactory().setValue(2);
        decimalLabel.setText("자리");

        // PIP 테두리 고정 설정: 기본값으로 설정
        AppConstants.pipOutlineOption = false;
        pipToggle.setSelected(false);

        // PIP 폰트 크기 설정: 기본값 28로 설정
        AppConstants.pipFontSize = 28.0;
        fontSizeSlider.setValue(28.0);


        // JSON 저장
        new PreferencesManager().saveSettings();
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
            alert.setTitle("Mini-Stock");
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
    private void handleAiClick(MouseEvent event) {
        System.out.println("AI 분석 클릭됨");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/ai.fxml"));
            Parent root = loader.load();

            // Main의 전역 Stage를 이용해서 화면 전환
            Main.mainStage.getScene().setRoot(root);

            new PreferencesManager().saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}