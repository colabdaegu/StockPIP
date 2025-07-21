package service;

import api.model.FinnhubApiClient;

public class FinnhubService {
    private final FinnhubApiClient client = new FinnhubApiClient();

    public String getStockQuote(String symbol) {
        return client.getQuote(symbol).orElse("오류 발생");
    }
}