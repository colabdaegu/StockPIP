package pip.integrationMode;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import service.alert.AlertService;
import net.NetworkManager;
import config.manager.PreferencesManager;
import PIPApp.Main;
import config.*;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PipIntegrationMain {
    private static final List<PipIntegrationMain> pipWindows = new ArrayList<>();

    private Timeline refreshTimeline;
    private double previousPrice = -1;
    private Label nameLabel;
    private Label priceLabel;
    private String thisTicker;
    private StackPane root;

    private HBox buttonBox;
    private int myIndex;

    // 드래그 오프셋
    private double dragOffsetX;
    private double dragOffsetY;

    // main. Entry Point (integration mode)
    public void PipMainIntegration(Stocks stock, int index, Stage groupStage) {
        pipWindows.add(this);
        thisTicker = stock.getTicker();
        this.myIndex = index;

        nameLabel = new Label(stock.getName() + "(" + stock.getTicker() + ")");
        priceLabel = new Label("Loading...");
        double fontSize = AppConstants.pipFontSize;
        styleLabels(fontSize);

        updateLabels(stock);
        timelineRefresh(stock);

        buttonBox = createButtonBar();
        StackPane center = VBoxSpacing(nameLabel, priceLabel);
        center.setAlignment(Pos.CENTER);

        root = new StackPane(center, buttonBox);
        applyOutlineStyle(root, buttonBox);

        double ratio = fontSize / 28.0;
        double newWidth = Math.max(150, 300 * ratio);
        double newHeight = Math.max(60, 120 * ratio);
        root.setPrefSize(newWidth, newHeight);

        // 드래그시 그룹 노드 이동
        enableDragToMoveGroup(root, groupStage);

        AlertService.startMonitoring(stock);
    }

    // 드래그가 발생하면 그룹 Stage를 이동하게 함
    private void enableDragToMoveGroup(StackPane node, Stage targetStage) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                // 클릭 위치의 scene 좌표를 저장 (node가 포함된 scene 사용)
                dragOffsetX = e.getSceneX();
                dragOffsetY = e.getSceneY();
            }
        });
        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                // 화면 좌표에서 오프셋만큼 빼서 Stage 이동
                targetStage.setX(e.getScreenX() - dragOffsetX);
                targetStage.setY(e.getScreenY() - dragOffsetY);
            }
        });
    }

    public StackPane getRootNode() {
        return root;
    }

    // 2. 스타일 설정
    private void styleLabels(double fontSize) {
        nameLabel.setStyle("-fx-font-size: " + (fontSize * 0.65) + "px; -fx-text-fill: white;" +
                "-fx-effect: dropshadow(gaussian, black, 2, 0.3, 0, 0);");
        priceLabel.setStyle("-fx-font-size: " + fontSize + "px;" +
                "-fx-effect: dropshadow(gaussian, black, 2, 0.3, 0, 0);");
    }

    // 3. 현재가 표시 업데이트
    private void updateLabels(Stocks stock) {
        double current = stock.currentPrice;
        String name = stock.name;
        Color color;

        if (previousPrice < 0) {
            color = Color.LIGHTGRAY;
        } else if (current > previousPrice) {
            color = Color.RED;
        } else if (current < previousPrice) {
            color = Color.BLUE;
        } else {
            color = Color.LIGHTGRAY;
        }

        switch (AppConstants.pipDecimalPoint) {
            case 0 -> priceLabel.setText(String.format("$ %,.0f", current));
            case 1 -> priceLabel.setText(String.format("$ %,.1f", current));
            case 2 -> priceLabel.setText(String.format("$ %,.2f", current));
            case 3 -> priceLabel.setText(String.format("$ %,.3f", current));
            case 4 -> priceLabel.setText(String.format("$ %,.4f", current));
            case 5 -> priceLabel.setText(String.format("$ %,.5f", current));
        }

        // 작은 값 예외 처리
        if ((current < 1 && current >= 0.1) && (AppConstants.pipDecimalPoint < 1)) {
            priceLabel.setText(String.format("$ %,.1f", current));
        } else if ((current < 0.1 && current >= 0.01) && (AppConstants.pipDecimalPoint < 2)) {
            priceLabel.setText(String.format("$ %,.2f", current));
        } else if ((current < 0.01 && current >= 0.001) && (AppConstants.pipDecimalPoint < 3)) {
            priceLabel.setText(String.format("$ %,.3f", current));
        } else if ((current < 0.001 && current >= 0.0001) && (AppConstants.pipDecimalPoint < 4)) {
            priceLabel.setText(String.format("$ %,.4f", current));
        } else if ((current < 0.0001 && current >= 0.00001) && (AppConstants.pipDecimalPoint < 5)) {
            priceLabel.setText(String.format("$ %,.5f", current));
        } else if ((current < 0.00001 && current >= 0.000001) && (AppConstants.pipDecimalPoint <= 5)) {
            priceLabel.setText(String.format("$ %,.6f", current));
        }

        priceLabel.setTextFill(color);
        previousPrice = current;
        nameLabel.setText(name + "(" + stock.getTicker() + ")");
    }

    // 4. 주기적 업데이트
    private void timelineRefresh(Stocks stock) {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        int refreshSeconds = stock.getRefresh();
        if (refreshSeconds <= 0) return;

        refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(refreshSeconds), event -> {
                    if (!NetworkManager.isInternetAvailable()) {
                        priceLabel.setText("Network Failure");
                        priceLabel.setTextFill(Color.CORAL);
                        return;
                    }
                    stock.refreshQuote();
                    updateLabels(stock);
                    if (stock.getCurrentPrice() <= stock.getStopPrice() && stock.getCurrentPrice() != 0) {
                        stop(1);
                    }
                })
        );
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    // 5. 버튼 생성 및 핸들러
    private HBox createButtonBar() {
        // 첫 번째 PIP 창만 버튼 표시
        double fontSize = AppConstants.pipFontSize;
        double buttonFontSize = (fontSize * 0.71 > 10) ? fontSize * 0.71 : 10;

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: " + buttonFontSize + "px;");
        closeBtn.setOnAction(e -> {
            stop(1);
            Platform.exit();
        });

        Button settingsBtn = new Button("⚙");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: " + buttonFontSize + "px;");
        settingsBtn.setOnAction(e -> {
            // 모든 pip 창 정리 (통합 모드에서 group Stage 닫음)
            for (PipIntegrationMain pip : new ArrayList<>(pipWindows)) {
                pip.stop(0);
            }
            pipWindows.clear();

            PipGroupManager.getInstance().closeGroupStage();

            try {
                Parent homeRoot = FXMLLoader.load(getClass().getResource("/ui/view/home.fxml"));
                new PreferencesManager().saveSettings();
                Main.mainStage.setScene(new javafx.scene.Scene(homeRoot, 1220, 740));
                Main.mainStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox box = new HBox(8, settingsBtn, closeBtn);
        box.setAlignment(Pos.TOP_RIGHT);
        box.setPadding(new Insets(8));

        box.setVisible(false);
        return box;
    }

    // 6. 레이블 수직 정렬용 VBox
    private StackPane VBoxSpacing(Label top, Label bottom) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(top, bottom);
        return new StackPane(box);
    }

    // 7. 마우스 진입 시 외곽선 스타일
    private void applyOutlineStyle(StackPane root, HBox buttonBox) {
        if (!AppConstants.pipOutlineOption) {
            root.setStyle("-fx-background-color: transparent;");
            buttonBox.setVisible(false);

            // 전체 pipWindows 대상으로 마우스 이벤트
            root.setOnMouseEntered(e -> setAllPipOutlineVisible(true));
            root.setOnMouseExited(e -> setAllPipOutlineVisible(false));
        } else {
            if (AppConstants.pipModeDirectionOption == 0) {
                root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 0 1 0 1;");
                buttonBox.setVisible(myIndex == 0);
            } else if (AppConstants.pipModeDirectionOption == 1) {
                root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1 0 1 0;");
                for (int i = 0; i < pipWindows.size(); i++) {
                    PipIntegrationMain pip = pipWindows.get(i);
                    pip.buttonBox.setVisible(i == pipWindows.size() - 1);
                }
            }
        }
    }

    // pipWindows 전체에 외곽선과 버튼 보이기/숨기기
    private void setAllPipOutlineVisible(boolean visible) {
        for (int i = 0; i < pipWindows.size(); i++) {
            PipIntegrationMain pip = pipWindows.get(i);
            if (pip.root == null) continue;

            if (visible) {
                if (AppConstants.pipModeDirectionOption == 0) {
                    pip.root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 0 1 0 1;");
                } else if (AppConstants.pipModeDirectionOption == 1) {
                    pip.root.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-color: white; -fx-border-width: 1 0 1 0;");
                }
            } else {
                pip.root.setStyle("-fx-background-color: transparent;");
            }

            if (AppConstants.pipModeDirectionOption == 0) {
                pip.buttonBox.setVisible(visible && i == 0);
            } else if (AppConstants.pipModeDirectionOption == 1) {
                pip.buttonBox.setVisible(visible && i == pipWindows.size() - 1);
            }
        }
    }


    // 10. 종료 시 타임라인 멈추고 노드 제거
    public void stop(int option) {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }

        // 현재 root가 속한 parent에서 제거
        if (root != null) {
            if (root.getParent() instanceof Pane parentPane) {
                parentPane.getChildren().remove(root);

                // option == 1일 때만 마지막 PIP 처리
                if (option == 1 && parentPane.getChildren().isEmpty()) {
                    handleLastWindowClose();
                }
            } else {
                // 부모에 붙지 않은 경우
                if (option == 1 && root.getScene() != null && root.getScene().getWindow() instanceof Stage stage) {
                    stage.close();
                    handleLastWindowClose();
                }
            }
        }

        if (option == 1) {
            pipWindows.remove(this);
            reindexWindows();
        }
    }

    private static void reindexWindows() {
        for (int i = 0; i < pipWindows.size(); i++) {
            PipIntegrationMain pip = pipWindows.get(i);
            pip.myIndex = i;
            if (pip.buttonBox != null) {
                // pipOutlineOption이 true일 때만 항상 표시
                if (AppConstants.pipOutlineOption) {
                    if (AppConstants.pipModeDirectionOption == 0) {
                        pip.buttonBox.setVisible(i == 0);
                    } else if (AppConstants.pipModeDirectionOption == 1) {
                        pip.buttonBox.setVisible(i == pipWindows.size() - 1);
                    }
                } else {
                    pip.buttonBox.setVisible(false);
                }
            }
        }
    }


    // 마지막 창일 경우 (알림창 표시를 위한 더미 Stage)
    private static void handleLastWindowClose() {
        Stage tempStage = new Stage();
        tempStage.setTitle("Dummy");
        tempStage.setOpacity(0);
        tempStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            tempStage.close();
            Platform.exit();
        });
        delay.play();
    }

    public static List<PipIntegrationMain> getPipWindows() {
        return pipWindows;
    }

    public String getStockTicker() {
        return thisTicker;
    }
}