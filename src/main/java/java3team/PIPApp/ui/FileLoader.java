package ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileLoader {
    public static List<String> loadLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.err.println("파일 로딩 실패: " + path);
            e.printStackTrace();
            return List.of();  // 빈 리스트 반환
        }
    }
}