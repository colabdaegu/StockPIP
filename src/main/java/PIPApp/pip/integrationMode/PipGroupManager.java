package pip.integrationMode;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PipGroupManager {
    private static PipGroupManager instance;
    private Stage groupStage;
    private VBox container;
    private final List<PipIntegrationMain> pipWindows = new ArrayList<>();

    // 드래그 오프셋
    private double dragOffsetX;
    private double dragOffsetY;

    private PipGroupManager() {}

    public static PipGroupManager getInstance() {
        if (instance == null) {
            instance = new PipGroupManager();
        }
        return instance;
    }

    public void createGroupStage() {
        if (groupStage != null) return;
        container = new VBox(0);
        container.setStyle("-fx-background-color: rgba(0,0,0,0.0); -fx-border-color: transparent; -fx-border-width: 0px;");

        Scene scene = new Scene(container);
        scene.setFill(Color.TRANSPARENT);

        groupStage = new Stage();
        groupStage.initStyle(StageStyle.TRANSPARENT);
        groupStage.setAlwaysOnTop(true);
        groupStage.setScene(scene);
        groupStage.setTitle("StockPipApp");
        groupStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo/Stock_Logo_fill.png")));

        groupStage.setX(0);
        groupStage.setY(0);

        // 컨테이너 레벨에서 드래그 처리 (어디서든 드래그하면 그룹이 이동)
        container.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            // 왼쪽 버튼만
            if (e.isPrimaryButtonDown()) {
                dragOffsetX = e.getSceneX();
                dragOffsetY = e.getSceneY();
            }
        });
        container.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.isPrimaryButtonDown()) {
                groupStage.setX(e.getScreenX() - dragOffsetX);
                groupStage.setY(e.getScreenY() - dragOffsetY);
            }
        });
    }

    public void addPipWindow(PipIntegrationMain pipWindow) {
        // root 노드를 VBox에 추가
        container.getChildren().add(pipWindow.getRootNode());
        pipWindows.add(pipWindow);
    }

    public void showGroupStage() {
        if (groupStage != null && !groupStage.isShowing()) {
            groupStage.show();
        }
    }

    public void closeGroupStage() {
        if (groupStage != null) {
            groupStage.close();
            groupStage = null;
            container = null;
            pipWindows.clear();
        }
    }

    public Stage getGroupStage() {
        return groupStage;
    }
}
