package pip;

import config.StockList;
import config.Stocks;
import javafx.stage.Stage;

public class PipLauncher {
    public static void launchAllPipWindows() {
        int index = 0;
        for (Stocks stock : StockList.getStockArray()) {
            Stage pipStage = new Stage();
            PipMain pipWindow = new PipMain();
            pipWindow.PipMain(pipStage, stock, index);
            index++;
        }
    }
}