package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import config.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private static final String SETTINGS_FILE_NAME = "settings.json";
    private final Gson gson;

    public PreferencesManager() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /** 앱 설정 저장 */
    public void saveSettings() {
        JsonObject root = new JsonObject();

        // ✅ StockList 저장
        root.add("stockArray", gson.toJsonTree(StockList.getStockArray()));

        // ✅ LogBuilder 저장
        root.addProperty("logText", StockList.getLogText());

        // ✅ AppConstants 값 저장
        root.addProperty("notificationOption", AppConstants.notificationOption);
        root.addProperty("pipDecimalPoint", AppConstants.pipDecimalPoint);
        root.addProperty("pipOutlineOption", AppConstants.pipOutlineOption);
        root.addProperty("pipFontSize", AppConstants.pipFontSize);

        try (FileWriter writer = new FileWriter(SETTINGS_FILE_NAME)) {
            gson.toJson(root, writer);
            System.out.println("✅ 설정 저장 완료: " + SETTINGS_FILE_NAME);
        } catch (IOException e) {
            System.err.println("❌ 설정 저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** 앱 설정 로드 */
    public void loadSettings() {
        File settingsFile = new File(SETTINGS_FILE_NAME);
        if (!settingsFile.exists()) {
            System.out.println("⚠ 설정 파일 없음 → 기본값으로 시작");
            StockList.stockArray.clear();
            return;
        }

        try (FileReader reader = new FileReader(SETTINGS_FILE_NAME)) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            // ✅ StockList 로드
            if (root.has("stockArray")) {
                Type stockListType = new TypeToken<ArrayList<Stocks>>() {}.getType();
                List<Stocks> loadedStocks = gson.fromJson(root.get("stockArray"), stockListType);
                StockList.stockArray.clear();
                if (loadedStocks != null) {
                    StockList.stockArray.addAll(loadedStocks);
                }
            }

            // ✅ LogBuilder 로드
            if (root.has("logText")) {
                String savedLog = root.get("logText").getAsString();
                if (savedLog != null && !savedLog.isEmpty()) {
                    if (savedLog.endsWith("\n")) {
                        savedLog = savedLog.substring(0, savedLog.length() - 1);
                    }
                    StockList.appendLog(savedLog);
                }
            }

            // ✅ AppConstants 로드
            if (root.has("notificationOption"))
                AppConstants.notificationOption = root.get("notificationOption").getAsInt();

            if (root.has("pipDecimalPoint"))
                AppConstants.pipDecimalPoint = root.get("pipDecimalPoint").getAsInt();

            if (root.has("pipOutlineOption"))
                AppConstants.pipOutlineOption = root.get("pipOutlineOption").getAsBoolean();

            if (root.has("pipFontSize"))
                AppConstants.pipFontSize = root.get("pipFontSize").getAsDouble();

            System.out.println("✅ 설정 로드 완료 (" + StockList.stockArray.size() + "개 종목)");


            // 네트워크 검사
            if (!NetworkManager.isInternetAvailable()) {
                System.out.println("⚠ 모니터링 중단 - 인터넷 연결 실패\n");
                return;
            }

            // API 재호출
            List<Stocks> refreshedList = new ArrayList<>();
            for (Stocks stock : StockList.stockArray) {
                refreshedList.add(new Stocks(
                        stock.getTicker(),
                        stock.getToggleOption(),
                        stock.getTargetPrice(),
                        stock.getStopPrice(),
                        stock.getRefreshMinute(),
                        stock.getRefreshSecond()
                ));
            }
            StockList.stockArray = refreshedList;
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("❌ 설정 로드 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}