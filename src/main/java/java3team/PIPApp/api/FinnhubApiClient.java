package api;

// HTTP 통신을 위한 라이브러리 (예: java.net.http.HttpClient 또는 외부 라이브러리) 임포트
// JSON 파싱을 위한 라이브러리 (예: com.google.gson.Gson) 임포트
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import com.google.gson.Gson;
// import com.yourteam.pipapp.api.models.StockQuote; // 필요한 데이터 모델 임포트

import java.io.IOException;

public class FinnhubApiClient {

    private final String API_BASE_URL = "https://finnhub.io/api/v1";
    private final String API_KEY = "YOUR_FINNHUB_API_KEY"; // 실제 API 키로 교체하세요.

    // private final HttpClient httpClient;
    // private final Gson gson;

    public FinnhubApiClient() {
        // httpClient = HttpClient.newHttpClient(); // Java 11+ 내장 HTTP 클라이언트
        // gson = new Gson(); // Gson 라이브러리 인스턴스 생성
    }

    /**
     * 특정 주식 종목의 현재 시세를 가져옵니다.
     * @param symbol 주식 종목 코드 (예: "AAPL", "MSFT")
     * @return StockQuote 객체 (JSON 응답을 매핑한 것) 또는 null (실패 시)
     */
    // public StockQuote getStockQuote(String symbol) {
    //     try {
    //         String url = String.format("%s/quote?symbol=%s&token=%s", API_BASE_URL, symbol, API_KEY);
    //         HttpRequest request = HttpRequest.newBuilder()
    //                 .uri(java.net.URI.create(url))
    //                 .build();
    //
    //         HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    //
    //         if (response.statusCode() == 200) {
    //             return gson.fromJson(response.body(), StockQuote.class);
    //         } else {
    //             System.err.println("Finnhub API Error: " + response.statusCode() + " - " + response.body());
    //             return null;
    //         }
    //     } catch (IOException | InterruptedException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    // 추가적인 API 호출 메소드 (예: 회사 프로필, 실적 등)를 여기에 구현합니다.
    // public CompanyProfile getCompanyProfile(String symbol) { ... }
}