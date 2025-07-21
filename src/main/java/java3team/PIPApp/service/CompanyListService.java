package service;

import config.AppConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CompanyListService {

    public List<String> fetchTickerSymbols(int limit) {
        List<String> tickers = new ArrayList<>();
        String url = AppConstants.API_BASE_URL + "/stock/symbol?exchange=US&token=" + AppConstants.API_KEY;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = AppConstants.HTTP_CLIENT.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            int count = 0;
            for (int i = 0; i < jsonArray.size() && count < limit; i++) {
                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                String symbol = obj.get("symbol").getAsString();
                tickers.add(symbol);
                count++;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return tickers;
    }
}