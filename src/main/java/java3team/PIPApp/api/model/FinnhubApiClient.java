package api.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import config.AppConstants;

import java.net.URI;
import java.net.http.*;
import java.io.IOException;
import java.util.Optional;

public class FinnhubApiClient {
    private static final String BASE_URL = "https://finnhub.io/api/v1";
    private static final String API_KEY = "d1nhhu9r01qovv8jaik0d1nhhu9r01qovv8jaikg"; // 또는 환경 변수로 설정

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson(); // Gson 인스턴스 생성

    // JSON 문자열 그대로 반환하는 원래 버전 (선택 사항)
    public Optional<String> getQuote(String symbol) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/quote?symbol=" + symbol + "&token=" + API_KEY))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Optional.ofNullable(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // StockQuote 객체로 반환하는 버전
    public Optional<StockQuote> getStockQuote(String symbol) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/quote?symbol=" + symbol + "&token=" + API_KEY))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            StockQuote quote = gson.fromJson(response.body(), StockQuote.class);
            return Optional.ofNullable(quote);

        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public Optional<CompanyProfile> fetchCompanyProfile(String symbol) {
        String url = AppConstants.API_BASE_URL + "/stock/profile2?symbol=" + symbol + "&token=" + AppConstants.API_KEY;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            CompanyProfile profile = gson.fromJson(response.body(), CompanyProfile.class);
            return Optional.ofNullable(profile);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}