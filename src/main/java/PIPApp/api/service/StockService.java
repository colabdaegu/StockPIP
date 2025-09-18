package api.service;

import api.*;
import api.model.*;

import java.text.DecimalFormat;

public class StockService {

    private final FinnhubApiClient finnhubApiClient;

    public StockService() {
        this.finnhubApiClient = new FinnhubApiClient();
    }

    public StockQuote getLiveStockQuote(String symbol) {
        return finnhubApiClient.getStockQuote(symbol).orElse(null);
    }

    public CompanyProfile getCompanyProfile(String symbol) {
        try {
            return finnhubApiClient.fetchCompanyProfile(symbol).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}