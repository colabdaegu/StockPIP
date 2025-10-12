package api.model;

import java.util.List;

public class StockCandleData {
    private List<Double> closePrices;
    private List<Double> highPrices;
    private List<Double> lowPrices;
    private List<Double> openPrices;
    private List<Long> timestamps;

    // setter
    public void setClosePrices(List<Double> closePrices) { this.closePrices = closePrices; }
    public void setHighPrices(List<Double> highPrices) { this.highPrices = highPrices; }
    public void setLowPrices(List<Double> lowPrices) { this.lowPrices = lowPrices; }
    public void setOpenPrices(List<Double> openPrices) { this.openPrices = openPrices; }
    public void setTimestamps(List<Long> timestamps) { this.timestamps = timestamps; }

    // getter
    public List<Double> getClosePrices() { return closePrices; }
    public List<Double> getHighPrices() { return highPrices; }
    public List<Double> getLowPrices() { return lowPrices; }
    public List<Double> getOpenPrices() { return openPrices; }
    public List<Long> getTimestamps() { return timestamps; }

    public boolean isValid() {
        return closePrices != null && !closePrices.isEmpty();
    }
}