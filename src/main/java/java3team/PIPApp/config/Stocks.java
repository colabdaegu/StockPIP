package config;

import javafx.scene.image.Image;
import service.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.scene.image.ImageView;


public class Stocks {
    // 사용자 입력 정보
    public String ticker = "";        // 티커

    public String name;         // 회사명

    public double targetPrice;   // 목표가
    public double stopPrice;     // 손절가
    public int refresh;          /// 새로고침 주기(분+초)
    public int refreshMinute;    // 새로고침 주기 - 분
    public int refreshSecond;    // 새로고침 주기 - 초

    // 종목 정보
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


    public Stocks(String ticker, double targetPrice, double stopPrice, int refreshMinute, int refreshSecond) {
        this.ticker = ticker;

        this.targetPrice = targetPrice;   // 목표가
        this.stopPrice = stopPrice;     // 손절가
        this.refreshMinute = refreshMinute;    // 새로고침 주기 - 분
        this.refreshSecond = refreshSecond;     // 새로고침 주기 - 초


        this.refresh = (refreshMinute * 60) + refreshSecond; // 초 단위로 변환


        // API 호출을 위한 서비스 객체 생성
        CompanyService companyService = new CompanyService();
        StockService stockService = new StockService();

        // 회사 프로필 조회
        companyService.getCompanyInfo(this.ticker).ifPresent(profile -> {
            name = profile.getName();       // 회사명

            industry = profile.getIndustry();   // 산업군
            country = profile.getCountry();     // 국가
            currency = profile.getCurrency();   // 통화
            exchange = profile.getExchange();   // 거래소
            // IPO일
            try {
                ipoDate = LocalDate.parse(profile.getIpoDate());
            } catch (Exception e) {
                ipoDate = null;
            }
            marketCapitalization = profile.getMarketCapitalization();   // 시가총액
            // 로고이미지
            String logo = profile.getLogoUrl();
            if (logo != null && !logo.isBlank()) {
                try {
                    Image image = new Image(logo, true); // background 로드
                    logoUrl = new ImageView(image);
                    logoUrl.setFitHeight(50);
                    logoUrl.setPreserveRatio(true);
                } catch (Exception e) {
                    System.out.println("⚠️ 로고 이미지 로딩 실패: " + logo);
                    logoUrl = null;
                }
            } else {
                logoUrl = null;
            }
        });

        // 시세 정보 조회
        var quote = stockService.getLiveStockQuote(this.ticker);
        if (quote != null) {
            currentPrice = quote.getCurrentPrice();     // 현재가
            openPrice = quote.getOpenPrice();           // 시가
            highPrice = quote.getHighPrice();           // 당일 최고가
            lowPrice = quote.getLowPrice();             // 당일 최저가
            previousClosePrice = quote.getPreviousClosePrice();     // 전일 종가
            api_refreshTime = LocalDateTime.now(); // 최근 갱신 시간
        }
    }

    public void refreshQuote() {
        StockService stockService = new StockService();
        var quote = stockService.getLiveStockQuote(this.ticker);
        if (quote != null) {
            currentPrice = quote.getCurrentPrice();
            openPrice = quote.getOpenPrice();
            highPrice = quote.getHighPrice();
            lowPrice = quote.getLowPrice();
            previousClosePrice = quote.getPreviousClosePrice();
            api_refreshTime = LocalDateTime.now();
        }
    }

    public void alert_refreshQuote() {
        StockService stockService = new StockService();
        var quote = stockService.getLiveStockQuote(this.ticker);
        if (quote != null) {
            currentPrice = quote.getCurrentPrice();
            api_refreshTime = LocalDateTime.now();
        }
    }


    @Override
    public String toString() {
        return ticker;
    }


    // Getter Setter
    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public double getTargetPrice() { return targetPrice; }
    public void setTargetPrice(double targetPrice) { this.targetPrice = targetPrice; }

    public double getStopPrice() { return stopPrice; }
    public void setStopPrice(double stopPrice) { this.stopPrice = stopPrice; }

    public int getRefresh() { return refresh; }
    public void setRefresh(int refresh) { this.refresh = refresh; }

    public int getRefreshMinute() { return refreshMinute; }
    public void setRefreshMinute(int refreshMinute) { this.refreshMinute = refreshMinute; }

    public int getRefreshSecond() { return refreshSecond; }
    public void setRefreshSecond(int refreshSecond) { this.refreshSecond = refreshSecond; }


    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }

    public LocalDate getIpoDate() { return ipoDate; }
    public void setIpoDate(LocalDate ipoDate) { this.ipoDate = ipoDate; }

    public double getMarketCapitalization() { return marketCapitalization; }
    public void setMarketCapitalization(double marketCapitalization) { this.marketCapitalization = marketCapitalization; }

    public ImageView getLogoUrl() { return logoUrl; }
    public void setLogoUrl(ImageView logoUrl) { this.logoUrl = logoUrl; }


    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public double getOpenPrice() { return openPrice; }
    public void setOpenPrice(double openPrice) { this.openPrice = openPrice; }

    public double getHighPrice() { return highPrice; }
    public void setHighPrice(double highPrice) { this.highPrice = highPrice; }

    public double getLowPrice() { return lowPrice; }
    public void setLowPrice(double lowPrice) { this.lowPrice = lowPrice; }

    public double getPreviousClosePrice() { return previousClosePrice; }
    public void setPreviousClosePrice(double previousClosePrice) { this.previousClosePrice = previousClosePrice; }

    public LocalDateTime getApi_refreshTime() { return api_refreshTime; }
    public void setApi_refreshTime(LocalDateTime api_refreshTime) { this.api_refreshTime = api_refreshTime; }
}