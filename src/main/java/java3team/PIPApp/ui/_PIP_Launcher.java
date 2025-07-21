package ui;

import config.StockList;
import config.Stocks;
import javafx.stage.Stage;

public class _PIP_Launcher {
    public static void launchAllPipWindows() {
        int index = 0;
        for (Stocks stock : StockList.getStockArray()) {
            Stage pipStage = new Stage();
            _PIP_Main pipWindow = new _PIP_Main();
            pipWindow.pip_On(pipStage, stock, index);
            index++;
        }
    }
}