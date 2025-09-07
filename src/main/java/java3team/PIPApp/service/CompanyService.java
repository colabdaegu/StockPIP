package service;

import api.model.FinnhubApiClient;
import api.model.CompanyProfile;

import java.util.Optional;

public class CompanyService {

    private final FinnhubApiClient apiClient;

    public CompanyService() {
        this.apiClient = new FinnhubApiClient();
    }

    public Optional<CompanyProfile> getCompanyInfo(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            return Optional.empty();
        }
        return apiClient.fetchCompanyProfile(symbol.trim().toUpperCase());
    }


    // 회사명으로 회사 정보 가져오기
    public Optional<CompanyProfile> getCompanyInfoByName(String companyName) {
        if (companyName == null || companyName.isBlank()) {
            return Optional.empty();
        }

        // 1. 회사명으로 검색해서 티커(symbol) 얻기
        return apiClient.searchSymbol(companyName.trim())
                // 2. 얻은 티커로 기존 메서드 호출
                .flatMap(this::getCompanyInfo);
    }
}