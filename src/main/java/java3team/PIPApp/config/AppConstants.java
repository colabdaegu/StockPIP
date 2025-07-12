package config;

import java.net.http.HttpClient;
import com.google.gson.Gson; // Gson 라이브러리 임포트
import com.google.gson.GsonBuilder; // GsonBuilder 임포트 (Gson 객체 설정을 위함)

public final class AppConstants {
    private AppConstants() {
        // 인스턴스화 방지
    }

    public static final String API_BASE_URL = "https://finnhub.io/api/v1";
    public static final String API_KEY = "d1m5qppr01qvvurkadhgd1m5qppr01qvvurkadi0";
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();



    public static String name;
    public static double targetPrice;
    public static double stopPrice;
    public static int refresh;
    public static int refreshMinute;
    public static int refreshSecond;

    // 초기화 메서드
    public static void resetData() {
        name = "";
        targetPrice = 0.0;
        stopPrice = 0.0;
        refresh = 0;
        refreshMinute = 0;
        refreshSecond = 0;
    }
}
