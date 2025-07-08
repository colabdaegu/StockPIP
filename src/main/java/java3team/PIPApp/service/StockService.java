package service;

import api.FinnhubApiClient;
import api.finnhub.model.StockQuote;

/**
 * 주식 데이터와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * Finnhub API 클라이언트를 사용하여 데이터를 가져오고, 필요한 처리를 수행합니다.
 */
public class StockService {

    private final FinnhubApiClient finnhubApiClient;

    public StockService() {
        this.finnhubApiClient = new FinnhubApiClient();
    }

    /**
     * 특정 주식 종목의 실시간 시세를 가져옵니다.
     * @param symbol 주식 종목 코드
     * @return StockQuote 객체
     */
    public StockQuote getLiveStockQuote(String symbol) {
        // API 클라이언트를 통해 실제 데이터를 가져옵니다.
        // return finnhubApiClient.getStockQuote(symbol); // FinnhubApiClient에 해당 메소드가 구현되어 있다고 가정
        return null; // 임시 반환 값
    }

    /**
     * 특정 주식 종목의 회사 프로필을 가져옵니다.
     * @param symbol 주식 종목 코드
     * @return CompanyProfile 객체
     */
    // public CompanyProfile getCompanyProfile(String symbol) {
    //     return finnhubApiClient.getCompanyProfile(symbol);
    // }

    // 그 외 주식 데이터 관련 비즈니스 로직 (예: 데이터 캐싱, 변동률 계산 등)
}