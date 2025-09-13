package fileLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class FileLoader {
    public static List<String> loadLines(String resourcePath) {
        try (InputStream in = FileLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("리소스 파일을 찾을 수 없습니다: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                return reader.lines().collect(Collectors.toList());
            }
        } catch (IOException e) {
            System.err.println("파일 로딩 실패: " + resourcePath);
            e.printStackTrace();
            return List.of();  // 빈 리스트 반환
        }
    }
}