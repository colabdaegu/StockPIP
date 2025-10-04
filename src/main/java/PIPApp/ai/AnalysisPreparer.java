package ai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AnalysisPreparer {
    private static final String API_KEY = "AIzaSyB2RaIO7JD2yCgk8ZejdYL_FTlDBn1KpJo"; // API 키
    private static final String MODEL_NAME = "gemini-2.0-flash"; // 모델명

    public static void start() {
        System.out.println("AI 준비 중...");
    }

    // Gemini에게 질문 보내고 답변 받기
    public static String askGemini(String question) {
        try {
            // 1. API URL 만들기
            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL_NAME + ":generateContent?key=" + API_KEY;

            // 2. 보낼 JSON 데이터 (질문 내용 포함)
            String jsonInput = "{\n" +
                    "  \"contents\": [\n" +
                    "    {\n" +
                    "      \"parts\": [\n" +
                    "        {\"text\": \"" + question + "\"}\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            // 3. HTTP 연결 설정
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // POST 요청
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // 4. JSON 데이터 보내기
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 5. 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return "⚠ 오류 발생: 응답 코드 " + responseCode;
            }

            // 6. 응답 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // 7. JSON 파싱
            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray candidates = jsonObject.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject content = candidates.get(0).getAsJsonObject()
                        .getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                if (parts != null && parts.size() > 0) {
                    String text = parts.get(0).getAsJsonObject().get("text").getAsString();
                    return text;
                }
            }

            return "⚠ AI 응답을 파싱할 수 없습니다.";

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠ 오류: " + e.getMessage();
        }
    }
}