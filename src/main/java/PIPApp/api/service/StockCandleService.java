package api.service;

import api.model.StockCandleData;
import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;

public class StockCandleService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // Yahoo Finance로부터 최근 1개월(30일) 일별 주가 조회
    public Optional<StockCandleData> getRecentDailyCandles(String symbol) {
        try {
            String url = String.format(
                    "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=1mo&interval=1d",
                    symbol
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("⚠ Yahoo Finance API 호출 실패: " + response.statusCode());
                return Optional.empty();
            }

            JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray resultArray = root.getAsJsonObject("chart").getAsJsonArray("result");
            if (resultArray == null || resultArray.size() == 0) {
                System.err.println("⚠ 결과 데이터 없음: " + symbol);
                return Optional.empty();
            }

            JsonObject result = resultArray.get(0).getAsJsonObject();
            JsonArray timestamps = result.getAsJsonArray("timestamp");
            JsonObject quote = result.getAsJsonObject("indicators")
                    .getAsJsonArray("quote").get(0).getAsJsonObject();

            // ✅ Gson으로 List 변환
            List<Double> open = gson.fromJson(quote.get("open"), List.class);
            List<Double> high = gson.fromJson(quote.get("high"), List.class);
            List<Double> low = gson.fromJson(quote.get("low"), List.class);
            List<Double> close = gson.fromJson(quote.get("close"), List.class);
            List<Long> t = gson.fromJson(timestamps, List.class);

            // ✅ Setter를 이용해 값 세팅
            StockCandleData data = new StockCandleData();
            data.setOpenPrices(open);
            data.setHighPrices(high);
            data.setLowPrices(low);
            data.setClosePrices(close);
            data.setTimestamps(t);

            return Optional.of(data);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}