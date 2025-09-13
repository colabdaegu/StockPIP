package network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class NetworkManager {
    // 네트워크 연결 진단
    public static boolean isInternetAvailable() {
        // 1차 검사 : Ping으로 빠르게 확인
        try {
            boolean pingSuccess = InetAddress.getByName("8.8.8.8").isReachable(1000);
            if (pingSuccess) {
                return true; // Ping 성공 → 인터넷 연결 확인
            }
        } catch (IOException e) {
            // Ping 도중 오류 → HTTP로 2차 확인 진행
        }

        // 2차 검사 : HTTP 요청으로 다시 확인
        try {
            URL url = new URL("https://www.google.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            int responseCode = connection.getResponseCode();

            // 응답 코드가 200~399면 성공으로 간주
            return (responseCode >= 200 && responseCode <= 399);
        } catch (IOException e) {
            // HTTP 요청 실패 → 인터넷 연결 안 됨
            return false;
        }
    }
}
