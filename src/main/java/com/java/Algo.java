package com.java;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Algo {

    static HttpClient client = HttpClient.newHttpClient();

    public static float LTP(String str) throws IOException, InterruptedException {
        String baseURL = "https://scanner.tradingview.com/symbol";
        String symbol = str;
        String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
        String fields = "close";
        String encodedFields = URLEncoder.encode(fields, StandardCharsets.UTF_8);
        String url = baseURL + "?symbol=OANDA:" + encodedSymbol + "&fields=" + encodedFields+ "&no_404=true";

        float val=0;
        boolean success=false;
        while (!success) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            if(jsonNode.size()!=0){
                JsonNode dataNode = jsonNode.get(fields);
                val = Float.parseFloat(dataNode.toString());
                success=true;
            }
            if(!success) Thread.sleep(1000);
        }
        return val;
    }
    
    public static float getCCI(String str, String time) throws IOException, InterruptedException {
        String baseURL = "https://scanner.tradingview.com/symbol";
        String symbol = str;
        String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
        String fields = "CCI20"+time;
        String encodedFields = URLEncoder.encode(fields, StandardCharsets.UTF_8);
        String url = baseURL + "?symbol=OANDA:" + encodedSymbol + "&fields=" + encodedFields+ "&no_404=true";

        float val=0;
        boolean success=false;
        while (!success) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            if(jsonNode.size()!=0){
                JsonNode dataNode = jsonNode.get(fields);
                val = Float.parseFloat(dataNode.toString());
                success=true;
            }
            if(!success) Thread.sleep(1000);
        }
        return val;
    }
}
