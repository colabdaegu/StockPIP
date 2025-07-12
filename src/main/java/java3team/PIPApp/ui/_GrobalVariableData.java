package ui;

public class _GrobalVariableData {
    String name;
    double targetPrice;
    double stopPrice;
    int refresh;
    int refreshMinute;
    int refreshSecond;

    // 초기화 메서드
    public void resetData() {
        name = "";
        targetPrice = 0.0;
        stopPrice = 0.0;
        refresh = 0;
        refreshMinute = 0;
        refreshSecond = 0;
    }
}
