package api.model;

import com.google.gson.annotations.SerializedName;

public class StockQuote {

    @SerializedName("c")
    private double currentPrice;

    @SerializedName("h")
    private double highPrice;

    @SerializedName("l")
    private double lowPrice;

    @SerializedName("o")
    private double openPrice;

    @SerializedName("pc")
    private double previousClosePrice;

    // 기본 생성자 (Gson 파싱용)
    public StockQuote() {
    }

    // 전체 필드를 사용하는 생성자
    public StockQuote(double currentPrice, double highPrice, double lowPrice, double openPrice, double previousClosePrice) {
        this.currentPrice = currentPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.previousClosePrice = previousClosePrice;
    }

    // Getter & Setter
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
        return "📊 StockQuote {" +
                "현재가 = " + currentPrice +
                ", 시가 = " + openPrice +
                ", 고가 = " + highPrice +
                ", 저가 = " + lowPrice +
                ", 전일 종가 = " + previousClosePrice +
                '}';
    }
}