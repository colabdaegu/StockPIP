package ui;

public class SettingsFontSize {
    private static double pipFontSize = 28.0; // 기본값

    public static void setFontSize(double size) {
        pipFontSize = size;
    }

    public static double getFontSize() {
        return pipFontSize;
    }
}