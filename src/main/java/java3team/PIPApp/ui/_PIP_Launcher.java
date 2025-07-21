package ui;

import config.StockList;
import config.Stocks;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class _PIP_Launcher {

    // 중복 방지를 위한 static 맵 (종목 이름 -> Stage)
    private static final Map<String, Stage> pipWindows = new HashMap<>();

    public static void launchAllPipWindows() {
        int index = 0;
        for (Stocks stock : StockList.getStockArray()) {
            launchSinglePipWindow(stock, index);
            index++;
        }
    }

    public static void launchSinglePipWindow(Stocks stock, int index) {
        String key = stock.getName();

        if (pipWindows.containsKey(key)) {
            Stage existingStage = pipWindows.get(key);
            if (existingStage.isShowing()) {
                System.out.println("\u26a0 PIP 창 이미 열려 있음: " + key);
                return;
            } else {
                pipWindows.remove(key); // 창이 닫혔는데 여전히 map에 남아있는 경우
            }
        }

        Stage pipStage = new Stage();

        // 창이 닫힐 때 Map에서 제거하는 이벤트 핸들러 등록
        pipStage.setOnCloseRequest(event -> pipWindows.remove(key));

        _PIP_Main pipWindow = new _PIP_Main();
        pipWindow.pip_On(pipStage, stock, index);

        pipWindows.put(key, pipStage);
    }

    // 필요한 경우 외부에서 닫을 수 있도록 전체 PIP 창 접근 메서드 제공
    public static void closeAllPipWindows() {
        for (Stage stage : pipWindows.values()) {
            if (stage.isShowing()) {
                stage.close();
            }
        }
        pipWindows.clear();
    }

    public static boolean isPipWindowOpen(String stockName) {
        return pipWindows.containsKey(stockName);
    }
}