package config;

import api.service.CompanyService;
import api.service.StockService;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;


public class Stocks {
    private List<Runnable> listeners = new ArrayList<>();

    // 사용자 입력 정보
    public String ticker = "";        // 티커
    public int toggleOption;

    public transient String name;         // 회사명

    public double targetPrice;   // 목표가
    public double stopPrice;     // 손절가
    public int refresh;          /// 새로고침 주기(분+초)
    public int refreshMinute;    // 새로고침 주기 - 분
    public int refreshSecond;    // 새로고침 주기 - 초

    // 종목 정보
    public transient String industry;
    public transient String country;
    public transient String currency;
    public transient String exchange;
    public transient LocalDate ipoDate;
    public transient double marketCapitalization;
    public transient ImageView logoUrl;

    // 시세 정보
    public transient double currentPrice;  // 현재가
    public transient double openPrice;     // 시가
    public transient double highPrice;     // 당일 최고가
    public transient double lowPrice;      // 당일 최저가
    public transient double previousClosePrice;      // 전일 종가
    public transient LocalDateTime api_refreshTime;   // 최근 갱신 시간


    public Stocks(String ticker, int toggleOption, double targetPrice, double stopPrice, int refreshMinute, int refreshSecond) {
        this.ticker = ticker;
        this.toggleOption = toggleOption;

        this.targetPrice = targetPrice;   // 목표가
        this.stopPrice = stopPrice;     // 손절가
        this.refreshMinute = refreshMinute;    // 새로고침 주기 - 분
        this.refreshSecond = refreshSecond;     // 새로고침 주기 - 초


        this.refresh = (refreshMinute * 60) + refreshSecond; // 초 단위로 변환

        refreshProfile();
        refreshQuote();
    }

    public void refreshQuote() {
//        // 네트워크 검사
//        if (!NetworkManager.isInternetAvailable()) {
//            System.out.println("⚠ 모니터링 중단 - 인터넷 연결 실패\n");
//            return;
//        }

        StockService stockService = new StockService();
        var quote = stockService.getLiveStockQuote(this.ticker);
        CompanyService companyService = new CompanyService();
        var profileOpt = companyService.getCompanyInfo(this.ticker);

        // 시세 정보 조회
        if (quote != null) {
            currentPrice = quote.getCurrentPrice();     // 현재가
            openPrice = quote.getOpenPrice();           // 시가
            highPrice = quote.getHighPrice();           // 당일 최고가
            lowPrice = quote.getLowPrice();             // 당일 최저가
            previousClosePrice = quote.getPreviousClosePrice();     // 전일 종가
            api_refreshTime = LocalDateTime.now(); // 최근 갱신 시간

            profileOpt.ifPresent(profile -> {
                if (profile.getName() != null) name = profile.getName();
            });

            notifyListeners();
        }
    }
    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
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

    // 회사 프로필 조회
    public void refreshProfile() {
//        // 네트워크 검사
//        if (!NetworkManager.isInternetAvailable()) {
//            System.out.println("⚠ 모니터링 중단 - 인터넷 연결 실패\n");
//            return;
//        }

        CompanyService companyService = new CompanyService();

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
                // 기존 로고가 없거나 URL이 달라졌을 때만 새로 로드
                if (logoUrl == null || !logo.equals(logoUrl.getImage().getUrl())) {
                    try {
                        Image image = new Image(logo, true); // background 로드
                        ImageView newView = new ImageView(image);
                        newView.setFitHeight(50);
                        newView.setPreserveRatio(true);
                        logoUrl = newView;
                    } catch (Exception e) {
                        System.out.println("⚠️ 로고 이미지 로딩 실패: " + logo);
                        logoUrl = null;
                    }
                }
            } else {
                // 새 URL이 없으면 로고 없앰
                logoUrl = null;
            }
        });
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


    public int getToggleOption() { return toggleOption; }
    public void setToggleOption(int toggleOption) { this.toggleOption = toggleOption; }

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