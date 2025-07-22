package config;

import java.util.ArrayList;
import java.util.List;

public class StockList {
    public static List<Stocks> stockArray = new ArrayList<>();

    public StockList(String ticker, double targetPrice, double stopPrice, int refreshMinute, int refreshSecond) {
        stockArray.add(new Stocks(ticker, targetPrice, stopPrice, refreshMinute, refreshSecond));
    }

    public static List<Stocks> getStockArray() { // ✅ 외부에서 접근 가능하게 getter 제공
        return stockArray;
    }
}
