package pip;

import config.AppConstants;
import pip.basicMode.PipBasicMain;
import pip.integrationMode.PipGroupManager;
import pip.integrationMode.PipIntegrationMain;
import service.alert.AlertServiceLauncher;
import config.StockList;
import config.Stocks;
import javafx.stage.Stage;

public class PipLauncher {
    public static void launchAllPipWindows() {
        int index = 0;
        // ⭐ 알림 체크 스케줄링 시작 ⭐
        AlertServiceLauncher.startAll();

        if (AppConstants.pipModeOption == 0) {
            for (Stocks stock : StockList.getStockArray()) {
                Stage pipStage = new Stage();
                PipBasicMain pipWindow = new PipBasicMain();
                pipWindow.PipMain(pipStage, stock, index);
                index++;
            }
        }
        else if (AppConstants.pipModeOption == 1) {
            PipGroupManager groupManager = PipGroupManager.getInstance();
            groupManager.createGroupStage();

            Stage groupStage = groupManager.getGroupStage();

            for (Stocks stock : StockList.getStockArray()) {
                PipIntegrationMain pipWindow = new PipIntegrationMain();
                // groupStage 참조를 넘겨서 개별 노드에서 그룹을 드래그 가능하게 함
                pipWindow.PipMainIntegration(stock, index, groupStage);
                groupManager.addPipWindow(pipWindow);
                index++;
            }
            groupManager.showGroupStage();
        }
    }
}