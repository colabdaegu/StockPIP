package ui;

import config.AppConstants;

public class _PIP_SettingsFontSize {
    public static void setFontSize(double size) {
        AppConstants.pipFontSize = size;
    }

    public static double getFontSize() {
        return AppConstants.pipFontSize;
    }
}