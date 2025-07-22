package service;// import com.google.gson.Gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

// ⭐ ExclusionStrategy 관련 임포트 추가
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;


/**
 * 애플리케이션의 사용자 설정(API 키, 테마 등)을 관리하는 클래스입니다.
 * 설정을 파일에 저장하고 로드하는 기능을 포함합니다.
 * JSON 파일을 사용하여 설정을 저장하는 것을 가정합니다.
 */
public class PreferencesManager {

    private static final String SETTINGS_FILE_NAME = "settings.json";
    private final Gson gson;

    public PreferencesManager() {
        // ⭐ ExclusionStrategy를 사용하여 특정 필드만 직렬화/역직렬화에 포함시킵니다.
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                // 직렬화/역직렬화에 포함시킬 필드의 이름만 여기서 'true'가 아닌 'false'를 반환합니다.
                // 즉, 여기에 명시되지 않은 필드들은 'true'를 반환하여 제외됩니다.
                String fieldName = f.getName();
                return !(fieldName.equals("ticker") ||
                        fieldName.equals("targetPrice") ||
                        fieldName.equals("stopPrice") ||
                        fieldName.equals("refreshMinute") ||
                        fieldName.equals("refreshSecond"));
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                // 특정 클래스 전체를 건너뛰고 싶을 때 사용하지만, 여기서는 모든 클래스를 포함합니다.
                return false;
            }
        };

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setExclusionStrategies(strategy) // ⭐ 정의한 전략을 GsonBuilder에 적용합니다.
                .create();
    }

    /**
     * 애플리케이션 설정을 JSON 파일에 저장합니다.
     * @param settings 저장할 설정 객체
     */
    public void saveSettings() {
        // StockList에 있는 모든 Stocks 객체 import
        List<Stocks> stocksToSave = StockList.getStockArray();

        try (FileWriter writer = new FileWriter(SETTINGS_FILE_NAME)) {
            // List<Stocks> 객체를 JSON 배열로 변환하여 파일저장.
            gson.toJson(stocksToSave, writer);
            //System.out.println("주식 종목 설정이 성공적으로 저장되었습니다: " + SETTINGS_FILE_NAME);
        } catch (IOException e) {
            //System.err.println("주식 종목 설정 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * JSON 파일에서 주식 종목 설정을 로드하여 StockList에 적용합니다.
     * 기존 StockList의 내용은 로드된 설정으로 덮어쓰여집니다.
     */
    public void loadSettings() {
        File settingsFile = new File(SETTINGS_FILE_NAME);
        if (!settingsFile.exists()) {
            System.out.println("설정 파일이 존재하지 않습니다. StockList는 빈 상태로 시작합니다.");
            // 파일이 없을 때 초기화.
            StockList.stockArray.clear(); // 기존에 추가된 것이 있을 수도 있으니 비우기.
            return;
        }

        try (FileReader reader = new FileReader(SETTINGS_FILE_NAME)) {
            // List<Stocks> 타입을 Gson이 파싱할 수 있도록 TypeToken을 사용.
            Type stockListType = new TypeToken<ArrayList<Stocks>>() {}.getType();
            List<Stocks> loadedStocks = gson.fromJson(reader, stockListType);

            // 기존 StockList의 내용을 로드된 내용으로 덮어쓰기.
            StockList.stockArray.clear(); // 기존 목록 비우기
            if (loadedStocks != null) {
                StockList.stockArray.addAll(loadedStocks); // 로드된 목록 추가
            }

            System.out.println("주식 종목 설정이 성공적으로 로드되어 StockList에 적용되었습니다: " + SETTINGS_FILE_NAME);
            System.out.println("로드된 주식 종목 수: " + StockList.stockArray.size());

        } catch (IOException e) {
            System.err.println("설정 로드 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            // 오류 발생 시 StockList는  빈 상태.
        } catch (JsonSyntaxException e) {
            System.err.println("설정 파일의 JSON 형식이 올바르지 않습니다: " + e.getMessage());
            e.printStackTrace();
            // JSON 파싱 오류 발생 시  빈 상태.
        }
    }
}