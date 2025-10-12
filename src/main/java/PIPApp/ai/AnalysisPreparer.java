package ai;

import api.model.StockCandleData;
import api.service.StockCandleService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.NetworkManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class AnalysisPreparer {
    public static void start(String ticker) {
        // 인터넷 연결 체크
        if (!NetworkManager.isInternetAvailable()) {
            System.out.println("⚠ 인터넷 연결 실패\n");
            return;
        } else {
            System.out.println("🔍 [" + ticker + "] AI 분석 데이터 준비 중...");
        }

        StockCandleService candleService = new StockCandleService();
        Optional<StockCandleData> candleOpt = candleService.getRecentDailyCandles(ticker);

        if (candleOpt.isEmpty()) {
            System.err.println("⚠ [" + ticker + "] 최근 시세 데이터를 불러오지 못했습니다.");
            return;
        }

        StockCandleData data = candleOpt.get();

        // JSON으로 변환
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(data);

        // JSON 파일 저장
        File file = new File("data/analysis_" + ticker + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonOutput);
            System.out.println("✅ [" + ticker + "] 시세 데이터 저장 완료 → " + file.getPath());
        } catch (IOException e) {
            System.err.println("⚠ [" + ticker + "] JSON 파일 저장 중 오류 발생");
            e.printStackTrace();
        }
    }
}