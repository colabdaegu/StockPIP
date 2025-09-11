package pip;

import config.AppConstants;

public class PipSettingsFontSize {
    public static void setFontSize(double size) {
        AppConstants.pipFontSize = size;
    }

    public static double getFontSize() {
        return AppConstants.pipFontSize;
    }
}