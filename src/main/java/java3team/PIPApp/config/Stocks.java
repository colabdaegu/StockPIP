package config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.scene.image.ImageView;


public class Stocks {
    // 사용자 입력 정보
    public String name = "";        // 이름

    public double targetPrice;   // 목표가
    public double stopPrice;     // 손절가
    public int refresh;          /// 새로고침 주기(분+초)
    public int refreshMinute;    // 새로고침 주기 - 분
    public int refreshSecond;    // 새로고침 주기 - 초

    // 종목 정보
    public String ticker;
    public String industry;
    public String country;
    public String currency;
    public String exchange;
    public LocalDate ipoDate;
    public double marketCapitalization;
    public ImageView logoUrl;

    // 시세 정보
    public double currentPrice;  // 현재가
    public double openPrice;     // 시가
    public double highPrice;     // 당일 최고가
    public double lowPrice;      // 당일 최저가
    public double previousClosePrice;      // 전일 종가
    public LocalDateTime api_refreshTime;   // 최근 갱신 시간


    public Stocks(String name, double targetPrice, double stopPrice, int refreshMinute, int refreshSecond) {
        this.name = name;

        this.targetPrice = targetPrice;   // 목표가
        this.stopPrice = stopPrice;     // 손절가
        this.refreshMinute = refreshMinute;    // 새로고침 주기 - 분
        this.refreshSecond = refreshSecond;     // 새로고침 주기 - 초


        this.refresh = (refreshMinute * 60) + refreshSecond; // 초 단위로 변환


        /// ✅ API 연동 이후 해당 종목에 대한 데이터 불러와서 업데이트해야 됨 (현재 임시 데이터 넣어놓음) ✅
        // 종목 정보
        ticker = "";   // 티커
        industry = "";  // 산업군
        country = "";   // 국가
        currency = "";  // 통화
        exchange = "";  // 거래소
        ipoDate = null;   // IPO일
        marketCapitalization = 0.0;  // 시가총액
        logoUrl = null;   // 로고 이미지

        // 시세 정보
        currentPrice = 0.0;  // 현재가
        openPrice = 0.0;     // 시가
        highPrice = 0.0;     // 당일 최고가
        lowPrice = 0.0;      // 당일 최저가
        previousClosePrice = 0.0;      // 전일 종가
        api_refreshTime = null;     // 최근 갱신 시간
    }


    @Override
    public String toString() {
        return name;  // ComboBox에 이름만 표시되도록
    }


    // Getter
    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public double getStopPrice() {
        return stopPrice;
    }

    public int getRefresh() {   /// 최종 사용할 값 (초 단위)
        return refresh;
    }

    public int getRefreshMinute() {
        return refreshMinute;
    }

    public int getRefreshSecond() {
        return refreshSecond;
    }
}