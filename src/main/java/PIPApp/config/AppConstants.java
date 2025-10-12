package config;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

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


    // 환경설정용 변수
    public static int notificationOption = 0;       // 알림 설정
    public static int pipModeOption = 0;        // PIP 모드
    public static int pipModeDirectionOption = 0;     // PIP 모드 - 방향
    public static int pipDecimalPoint = 2;        // PIP 소수점 표시
    public static boolean pipOutlineOption = false; // PIP 테두리 고정
    public static double pipFontSize = 28.0;        // PIP 폰트 크기

    // AI 분석 - 임시 저장용 변수
    public static List<String> tx = new ArrayList<>();
}