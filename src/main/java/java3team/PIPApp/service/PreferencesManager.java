package service;// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;
// import com.yourteam.pipapp.model.AppSettings; // 설정 데이터를 담을 별도의 모델 클래스가 있다면 임포트

/**
 * 애플리케이션의 사용자 설정(API 키, 테마 등)을 관리하는 클래스입니다.
 * 설정을 파일에 저장하고 로드하는 기능을 포함합니다.
 * JSON 파일을 사용하여 설정을 저장하는 것을 가정합니다.
 */
public class PreferencesManager {

    private static final String SETTINGS_FILE_NAME = "settings.json";
    // private final Gson gson;

    public PreferencesManager() {
        // gson = new GsonBuilder().setPrettyPrinting().create(); // 가독성 좋은 JSON 출력을 위한 설정
    }

    /**
     * 애플리케이션 설정을 JSON 파일에 저장합니다.
     * @param settings 저장할 설정 객체
     */
    // public void saveSettings(AppSettings settings) {
    //     try (FileWriter writer = new FileWriter(SETTINGS_FILE_NAME)) {
    //         gson.toJson(settings, writer);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * JSON 파일에서 애플리케이션 설정을 로드합니다.
     * @return 로드된 설정 객체, 또는 파일이 없으면 기본 설정 객체
     */
    // public AppSettings loadSettings() {
    //     File settingsFile = new File(SETTINGS_FILE_NAME);
    //     if (!settingsFile.exists()) {
    //         return new AppSettings(); // 파일이 없으면 기본 설정 반환
    //     }
    //
    //     try (FileReader reader = new FileReader(SETTINGS_FILE_NAME)) {
    //         return gson.fromJson(reader, AppSettings.class);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return new AppSettings(); // 오류 발생 시 기본 설정 반환
    //     }
    // }
}