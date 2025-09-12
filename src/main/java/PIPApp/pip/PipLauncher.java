package pip;

import config.StockList;
import config.Stocks;
import service.*;
import javafx.stage.Stage;

public class PipLauncher {
    public static void launchAllPipWindows() {
        int index = 0;
        launchAlertServiceAll();
        for (Stocks stock : StockList.getStockArray()) {
            Stage pipStage = new Stage();
            PipMain pipWindow = new PipMain();
            pipWindow.PipMain(pipStage, stock, index);
            index++;
        }
    }

    private static final AlertServiceLauncher alertServiceLauncher = new AlertServiceLauncher();

    public static void launchAlertServiceAll() {
        // 기존 모니터링 중단 후 다시 시작
        alertServiceLauncher.startAll();
    }
}