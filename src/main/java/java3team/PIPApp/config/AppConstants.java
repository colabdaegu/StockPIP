package config;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson; // Gson 라이브러리 임포트
import com.google.gson.GsonBuilder; // GsonBuilder 임포트 (Gson 객체 설정을 위함)
import javafx.scene.image.ImageView;

public final class AppConstants {
    public AppConstants() {
        // 인스턴스화 방지
    }

    public static final String API_BASE_URL = "https://finnhub.io/api/v1";
    public static final String API_KEY = "d1m5qppr01qvvurkadhgd1m5qppr01qvvurkadi0";
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();



    ///  사용자 입력 변수
    public static List<String> NameList = new ArrayList<>(); // 이름 목록

    public static String name = "";      // 회사명

    public static double targetPrice;   // 목표가
    public static double stopPrice;     // 손절가
    public static int refresh;          /// 새로고침 주기(분+초)
    public static int refreshMinute;    // 새로고침 주기 - 분
    public static int refreshSecond;    // 새로고침 주기 - 초



    //  종목 정보
    public static String ticker = "";   // 티커
    public static String industry;  // 산업군
    public static String country;   // 국가
    public static String currency;  // 통화
    public static String exchange;  // 거래소
    public static LocalDate ipoDate;   // IPO일
    public static double marketCapitalization;  // 시가총액

    public static ImageView logoUrl;   // 로고 이미지



    // 시세 정보
    public static double currentPrice;  // 현재가
    public static double openPrice;     // 시가
    public static double highPrice;     // 당일 최고가
    public static double lowPrice;      // 당일 최저가
    public static double previousClosePrice;      // 전일 종가

    public static LocalDateTime refreshTime;   // 최근 갱신 시간


    // 설정
    public static int AlertOption = 0;           // 알림 방식
    public static boolean pipOutlineOption = false; // PIP 테두리 고정
    public static double pipFontSize = 28.0;        // PIP 폰트 크기
    public static int UI_theme = 1;                 // 테마 설정




    // 초기화 메서드
    public static void resetData() {
        name = "";
        targetPrice = 0.0;
        stopPrice = 0.0;
        refresh = 0;
        refreshMinute = 0;
        refreshSecond = 0;


        // 종목 정보 초기화
        ticker = "";   // 티커
        industry = "";  // 산업군
        country = "";   // 국가
        currency = "";  // 통화
        exchange = "";  // 거래소
        ipoDate = null;   // IPO일
        marketCapitalization = 0.0;  // 시가총액

        logoUrl = null;   // 로고 이미지



        // 시세 정보 초기화
        currentPrice = 0.0;  // 현재가
        openPrice = 0.0;     // 시가
        highPrice = 0.0;     // 당일 최고가
        lowPrice = 0.0;      // 당일 최저가
        previousClosePrice = 0.0;      // 전일 종가

        refreshTime = null;     // 최근 갱신 시간
    }
}
