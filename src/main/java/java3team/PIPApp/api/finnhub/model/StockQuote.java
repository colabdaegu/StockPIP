package api.finnhub.model;


// JSON 라이브러리(Gson) 어노테이션 사용 시 필요:
// import com.google.gson.annotations.SerializedName;

/**
 * Finnhub API의 주식 시세(Quote) 응답을 나타내는 데이터 모델 클래스입니다.
 * 필드 이름은 Finnhub API 문서에 따라 JSON 키와 매칭되어야 합니다.
 * (Gson 라이브러리를 사용할 경우 @SerializedName 어노테이션으로 매핑 가능)
 */
public class StockQuote {

    // @SerializedName("c") // 현재 가격
    private double currentPrice;
    // @SerializedName("h") // 당일 최고가
    private double highPrice;
    // @SerializedName("l") // 당일 최저가
    private double lowPrice;
    // @SerializedName("o") // 당일 시초가
    private double openPrice;
    // @SerializedName("pc") // 전일 종가 (Previous Close)
    private double previousClosePrice;

    // 기본 생성자 (JSON 파싱을 위해 필요)
    public StockQuote() {}

    // 모든 필드를 포함하는 생성자 (선택 사항)
    public StockQuote(double currentPrice, double highPrice, double lowPrice, double openPrice, double previousClosePrice) {
        this.currentPrice = currentPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.previousClosePrice = previousClosePrice;
    }

    // Getter 및 Setter 메소드
    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getPreviousClosePrice() {
        return previousClosePrice;
    }

    public void setPreviousClosePrice(double previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }

    @Override
    public String toString() {
        return "StockQuote{" +
                "currentPrice=" + currentPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", openPrice=" + openPrice +
                ", previousClosePrice=" + previousClosePrice +
                '}';
    }
}