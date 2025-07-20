package api.finnhub.model;

// import com.google.gson.annotations.SerializedName;

/**
 * Finnhub API의 회사 프로필(Company Profile) 응답을 나타내는 데이터 모델 클래스입니다.
 */
public class CompanyProfile {

    // @SerializedName("name")
    private String name;
    // @SerializedName("ticker")
    private String ticker;
    // @SerializedName("country")
    private String country;
    // @SerializedName("currency")
    private String currency;
    // @SerializedName("exchange")
    private String exchange;
    // @SerializedName("ipo")
    private String ipoDate; // IPO 날짜
    // @SerializedName("marketCapitalization")
    private double marketCapitalization; // 시가총액 (밀리언 단위)
    // @SerializedName("finnhubIndustry")
    private String industry;
    // @SerializedName("logo")
    private String logoUrl;

    // 기본 생성자
    public CompanyProfile() {}

    // Getter 및 Setter (생략, 필요에 따라 추가)
    //public String getName() {
    //    return name;
    //}

    public void setName(String name) {
        this.name = name;
    }
    // ... 다른 필드에 대한 Getter/Setter ...

    @Override
    public String toString() {
        return "CompanyProfile{" +
                "name='" + name + '\'' +
                ", ticker='" + ticker + '\'' +
                ", country='" + country + '\'' +
                // ...
                '}';
    }
}