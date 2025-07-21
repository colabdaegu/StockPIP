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
}