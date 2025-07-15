package ui;

import com.gluonhq.charm.glisten.control.ToggleButtonGroup;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXToggleNode;
import config.AppConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Settings_Controller {
    @FXML private JFXToggleButton pipToggle;

    @FXML private Slider fontSizeSlider;

    @FXML private ToggleButton toastAlertButton;
    @FXML private ToggleButton inlineAlertButton;
    @FXML private ToggleButton soundAlertButton;

    @FXML RadioButton brightTheme, darkTheme;

    /// PIP 관련 설정(임시)
    @FXML
    private void handlePipToggle(ActionEvent event) {
        if (pipToggle.isSelected()) {
            // PIP 설정이 ON 상태일 때
            System.out.println("PIP 설정: ON");
        } else {
            // PIP 설정이 OFF 상태일 때
            System.out.println("PIP 설정: OFF");
        }
    }



    @FXML
    public void initialize() {
        fontSizeSlider.setValue(SettingsFontSize.getFontSize());

        /// PIP 폰트 사이즈 설정(연결 필요)
        fontSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double fontSize = newValue.doubleValue();
            SettingsFontSize.setFontSize(fontSize);
            System.out.println("PIP 폰트 크기: " + String.format("%.1f", fontSize));

            /// ▼ pip글자변수 지정 필요
            // pipLabel.setStyle("-fx-font-size: " + fontSize + "px;");
        });



        ///  알림 방식 설정(임시)
        // 각 버튼에 대한 선택 상태 리스너 추가
        toastAlertButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 토스트 알림이 선택됐을 때 (나머지 두 개 취소 구현도 필요)
                System.out.println("Toast 알림으로 설정됨");
            }
        });

        inlineAlertButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 인라인 알림이 선택됐을 때 (나머지 두 개 취소 구현도 필요)
                System.out.println("Inline 알림으로 설정됨");
            }
        });

        soundAlertButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // 사운드 알림이 선택됐을 때 (나머지 두 개 취소 구현도 필요)
                System.out.println("Sound 알림으로 설정됨");
            }
        });



        // 테마 설정
        ToggleGroup group = new ToggleGroup();
        brightTheme.setToggleGroup(group);
        darkTheme.setToggleGroup(group);

        group.selectedToggleProperty().addListener((observable_, oldValue_, newValue_) -> {
            if (newValue_ == brightTheme) {
                System.out.println("밝은 테마로 변경");
            } else if (newValue_ == darkTheme) {
                System.out.println("어두운 테마로 변경");
            }
        });
    }


    /// 기본값 설정 버튼의 이벤트(임시)
    @FXML
    private void defaultClick(ActionEvent event) {
        System.out.println("설정을 기본값으로 되돌림");

        // PIP 설정: 기본값으로 설정
        pipToggle.setSelected(false);

        // 폰트 크기 슬라이더: 기본값 20으로 설정
        fontSizeSlider.setValue(28.0);

        // 알림 방식 설정: 토스트 방식으로 설정
        toastAlertButton.setSelected(true);
        inlineAlertButton.setSelected(false);
        soundAlertButton.setSelected(false);

        // 테마 설정: 다크 테마로 설정
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
