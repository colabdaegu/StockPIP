package alertService;

import config.StockList;
import config.Stocks;

import java.util.HashMap;
import java.util.Map;

public class AlertServiceLauncher {
    private final static Map<String, AlertService> serviceMap = new HashMap<>();

    // 모든 모니터링 시작
    public static void startAll() {
        stopAll(); // 혹시 기존 실행 중인 것 있으면 중단

        for (Stocks stock : StockList.getStockArray()) {
            AlertService alertService = new AlertService();
            alertService.startMonitoring(stock);
            serviceMap.put(stock.ticker, alertService);
        }
    }

    // 모든 모니터링 중단
    public static void stopAll() {
        for (AlertService alertService : serviceMap.values()) {
            alertService.stopAllMonitoring();
        }
        serviceMap.clear();
    }
}
