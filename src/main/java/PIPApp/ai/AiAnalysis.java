package ai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.NetworkManager;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AiAnalysis {
    private static String AI_API_KEY;
    private static String AI_MODEL_NAME;

    static {
        try {
            File file = new File("apikey.json");

            // 패키징용 - API_KEY 가져오기
            if (!file.exists()) {
                try {
                    String jarDir = new File(AiAnalysis.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI())
                            .getParent();

                    File altFile = new File(jarDir, "apikey.json");
                    if (altFile.exists()) {
                        file = altFile;
                    } else {
                        System.err.println("⚠ Mini-Stock.jar과 같은 경로에 apikey.json이 위치해야 합니다.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 프로젝트용 - API_KEY 가져오기
            if (!file.exists()) {
                System.err.println("⚠ apikey 파일을 찾을 수 없습니다.");
                AI_API_KEY = "";
                AI_MODEL_NAME = "";
            } else {
                try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    AI_API_KEY = json.get("AI_API_KEY").getAsString();
                    AI_MODEL_NAME = json.get("AI_MODEL_NAME").getAsString();
                    System.out.println("✅ apikey 로드 완료");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AI_API_KEY = "";
            AI_MODEL_NAME = "";
        }
    }

    /**
     * 지정 티커 분석
     * @param ticker 분석할 종목 티커
     * @return Gemini AI가 분석한 결과 문자열
     */
    public static String start(String ticker) {
        // 인터넷 연결 체크
        if (!NetworkManager.isInternetAvailable()) {
            System.out.println("⚠ 인터넷 연결 실패\n");
            return "AiAnalysis Error";
        } else { System.out.println("=== AI 분석: " + ticker + " ==="); }

        File jsonFile = new File("data/analysis_" + ticker + ".json");
        if (!jsonFile.exists()) {
            System.err.println("⚠ 분석 파일이 없습니다: " + jsonFile.getPath());
            return "[" + ticker + "]\n⚠ 분석 데이터가 없습니다.";
        }

        try (Reader reader = new FileReader(jsonFile, StandardCharsets.UTF_8)) {
            JsonObject candleData = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray closePrices = candleData.getAsJsonArray("closePrices");
            int n = closePrices.size();

            // 최근 30일 간의 데이터
            StringBuilder priceDataStr = new StringBuilder();
            for (int i = 0; i < n; i++) {
                priceDataStr.append(closePrices.get(i).getAsDouble());
                if (i < n - 1) priceDataStr.append(", ");
            }

            // AI 프롬프트 작성
            String aiPrompt = "티커: " + ticker + "\n" +
                    "해당 종목의 전망치 예측 분석을 다음과 같이 요청\n" +
                    "최근 30일 주가 데이터(가중치 75%): " + priceDataStr.toString() + "\n" +
                    "최근 뉴스/제품/이슈/시장 동향 조사(가중치 25%)\n" +
                    "요약) 최근 30일 주가 데이터의 분석 결과에 75% + AI의 자체 조사(최근 뉴스, 제품, 이슈, 시장 동향)에 25% = 100%의 비중을 두어 종합 판단.\n\n" +
                    "반드시 세 가지 항목만 출력:\n" +
                    "① 현재값과 예측값 수치 기반 예측 (상승 예상/정체 예상/하락 예상 중 하나, 예: 상승 예상)\n" +
                    "② 예측값 수치 (예: $210 - $215)\n" +
                    "③ 회사 전망 (최근 뉴스/제품/이슈/시장 동향 조사 했을 때 내용을 한두 줄로 아주 간단히 요약, 문장은 공손체 종결어미 “-요.”, 예: 아이폰 15 출시 기대감과 서비스 부문 성장으로 긍정적인 모멘텀이 이어질 가능성이 높아요.)\n" +
                    "다른 내용은 절대로 출력 금지.\n" +
                    "출력 형식:\n" +
                    "① ...\n" +
                    "② ...\n" +
                    "③ 단기 전망: ...";

            String aiResponse = askGemini(aiPrompt);
            System.out.println("💬 " + aiResponse);

            // 응답 파싱
            String priceTrend = "", priceRange = "", companyOutlook = "";
            String[] lines = aiResponse.split("\n");
            for (String line : lines) {
                line = line.trim();

                if (line.startsWith("①")) {
                    priceTrend = line.substring(line.indexOf(":") + 1).trim();
                    priceTrend = priceTrend.replace("①", "").replace("②", "").replace("③", "").trim();

                } else if (line.startsWith("②")) {
                    priceRange = line.substring(line.indexOf(":") + 1).trim();
                    priceRange = priceRange.replace("①", "").replace("②", "").replace("③", "").trim();

                } else if (line.startsWith("③")) {
                    companyOutlook = line.substring(line.indexOf(":") + 1).trim();
                    companyOutlook = companyOutlook.replace("①", "").replace("②", "").replace("③", "").trim();
                }
            }

            // ① 현재값과 예측값 수치 기반 예측 - API 유효성 검사
            String trendDisplay;
            if (priceTrend.contains("예상")) trendDisplay = "**" + priceTrend + "**";
            else trendDisplay = "\n\n\nGemini API 에러";

            // ② 예측값 수치 - 이모지 처리
            String rangeDisplay;
            if (priceTrend.contains("상승")) rangeDisplay = "📈 " + priceRange + " 📈";
            else if (priceTrend.contains("하락")) rangeDisplay = "📉 " + priceRange + " 📉";
            else rangeDisplay = priceRange; // 정체 예상 또는 기타

            // ③ 회사 전망
            String companyDisplay;
            if (companyOutlook.contains(".")) companyDisplay = "\uD83D\uDD0E 단기 전망: " + companyOutlook + " \uD83D\uDD0E";
            else companyDisplay = "";

            // 화면 출력용 문자열
            String result = "[" + ticker + "]\n" +
                    trendDisplay + "\n" +
                    rangeDisplay + "\n\n" +
                    companyDisplay;

            System.out.println(result + "\n");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "[" + ticker + "]\n⚠ 분석 중 오류 발생: " + e.getMessage();
        }
    }

    // Gemini AI 호출
    public static String askGemini(String question) {
        try {
            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + AI_MODEL_NAME + ":generateContent?key=" + AI_API_KEY;

            // JSON-safe escape 처리
            String safeQuestion = question
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");

            String jsonInput = "{\n" +
                    "  \"contents\": [\n" +
                    "    {\n" +
                    "      \"parts\": [\n" +
                    "        {\"text\": \"" + safeQuestion + "\"}\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            java.net.URL url = new java.net.URL(apiUrl);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) return "⚠ 오류 발생: 응답 코드 " + responseCode;

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray candidates = jsonObject.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject content = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                if (parts != null && parts.size() > 0) {
                    return parts.get(0).getAsJsonObject().get("text").getAsString();
                }
            }

            return "⚠ AI 응답을 파싱할 수 없습니다.";

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠ 오류: " + e.getMessage();
        }
    }
}