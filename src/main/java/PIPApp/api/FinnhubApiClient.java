package api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import api.model.*;
import config.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        //System.out.println("📡 getStockQuote 호출됨: " + symbol + " " + LocalDateTime.now());
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/quote?symbol=" + symbol + "&token=" + API_KEY))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || response.body() == null || response.body().isBlank()) {
                System.err.println("⚠ API 응답 이상: " + response.statusCode() + " " + symbol);
                return Optional.empty();
            }

            StockQuote quote = gson.fromJson(response.body(), StockQuote.class);

            // 0이면 API 오류로 간주
            if (quote.getCurrentPrice() == 0) {
                return Optional.empty();
            }

            return Optional.of(quote);

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
            String body = response.body();


            if (body == null || body.isBlank()) {
                System.err.println("⚠ Finnhub API로부터 빈 응답을 받았습니다. (" + symbol + ")");
                return Optional.empty();
            }

            body = body.trim();

            if (!body.startsWith("{")) {
                System.err.println("⚠ 예상치 못한 응답 형식입니다. JSON이 아닙니다. (" + symbol + "): " + body);
                return Optional.empty();
            }

            JsonElement element = JsonParser.parseString(body);
            if (!element.isJsonObject()) {
                System.err.println("⚠ 응답이 JSON 객체 형식이 아닙니다. (" + symbol + "): " + body);
                return Optional.empty();
            }

            CompanyProfile profile = gson.fromJson(element, CompanyProfile.class);
            return Optional.ofNullable(profile);

        } catch (IOException | InterruptedException e) {
            System.err.println("⚠ 종목 프로필을 가져오는 중 문제가 발생했습니다. (" + symbol + ")");
            e.printStackTrace();
            return Optional.empty();

        } catch (JsonSyntaxException e) {
            System.err.println("⚠ 응답을 CompanyProfile로 변환하는 중 오류가 발생했습니다. (" + symbol + ")");
            e.printStackTrace();
            return Optional.empty();
        }
    }


    // 회사명(키워드) → 티커 변환
    public Optional<String> searchSymbol(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = BASE_URL + "/search?q=" + encodedQuery + "&token=" + API_KEY;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var json = gson.fromJson(response.body(), com.google.gson.JsonObject.class);
            var results = json.getAsJsonArray("result");

            if (results == null || results.size() == 0) {
                return Optional.empty();
            }

            // 🔹 description과 완전히 일치하는 항목 검색
            for (var el : results) {
                var obj = el.getAsJsonObject();
                String description = obj.get("description").getAsString().toUpperCase().trim();
                if (description.equals(query.toUpperCase().trim())) {
                    return Optional.of(obj.get("symbol").getAsString());
                }
            }

            // 완전히 일치하는 항목 없으면 빈 반환
            return Optional.empty();

        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}