package com.java;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchTrader {
    static String[] creds;

    public static void login() throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String json = "{"
            + "\"email\": \"demo+FTP@fundedtradingplus.com\","
            + "\"password\": \"FTPNewPW2023!F$\","
            + "\"brokerId\": \"1\""
            + "}";
        
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
            .url("https://mtr.gooeytrade.com/mtr-core-edge/login")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build();

        String mainToken = "";
        String systemUuid = "";
        String tradingApiToken = "";

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                String responseBody = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                
                mainToken = rootNode.get("token").asText();
                JsonNode tradingAccounts = rootNode.get("tradingAccounts");
                
                for (JsonNode account : tradingAccounts) {
                    if (account.get("tradingAccountId").asText().equals("10445")) {
                        systemUuid = account.get("offer").get("system").get("uuid").asText();
                        tradingApiToken = account.get("tradingApiToken").asText();
                        break;
                    }
                }
            }
        }

        Filehandler.writeToFile("tradingApiToken",tradingApiToken);
        Filehandler.writeToFile("Cookie","co-auth="+mainToken);
        Filehandler.writeToFile("systemUuid",systemUuid);
    }

    public static String getBalance() throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();
        String res="";
        boolean success=false;
        while (!success) {
        String url = "https://mtr.gooeytrade.com/mtr-api/" + Filehandler.readFromFile("systemUuid") + "/balance";
        
        Request request = new Request.Builder()
            .url(url)
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .addHeader("Accept", "application/json")
            .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    success=true;
                    res = response.body().string();
                }
                if(!success) login();
            }
        }
        return res;
    }
    
    public static List<String[]> OHLC() throws IOException {
        String interval="M1";
        OkHttpClient client = new OkHttpClient();
        List<String[]> candles = new ArrayList<>();
        boolean success = false;
        
        while (!success) {
            String url = "https://mtr.gooeytrade.com/mtr-api/" + 
            Filehandler.readFromFile("systemUuid") + 
            "/candles?symbol=EURUSD&interval=" + interval;
            
            Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode candlesArray = rootNode.get("candles");
                    
                    for (JsonNode candleNode : candlesArray) {
                        String[] ohlc = new String[5];
                        ohlc[0] = candleNode.get("time").toString();
                        ohlc[1] = candleNode.get("open").toString();
                        ohlc[2] = candleNode.get("high").toString();
                        ohlc[3] = candleNode.get("low").toString();
                        ohlc[4] = candleNode.get("close").toString();
                        candles.add(ohlc);
                    }
                    success = true;
                }
                if (!success) login();
            }
        }
        return candles;
    }
    
    public static String getSecondLastCandleTime() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String timestamp = "";
        boolean success = false;
        
        while (!success) {
            String url = "https://mtr.gooeytrade.com/mtr-api/" + 
            Filehandler.readFromFile("systemUuid") + 
            "/candles?symbol=EURUSD&interval=M1";
            
            Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode candlesArray = rootNode.get("candles");
                    
                    int size = candlesArray.size();
                    
                    if (size >= 2) {
                        JsonNode secondLastCandle = candlesArray.get(size - 2);
                        timestamp = secondLastCandle.get("time").asText();
                    }
                    success = true;
                }
                if (!success) login();
            }
        }
        return timestamp;
    }
    
    public static String getHL(String startTime, String endTime, String side) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String result = "NULL";
        boolean success = false;
        
        if(!startTime.equals("NULL")){
            long startTimestamp = Long.parseLong(startTime);
            long endTimestamp = Long.parseLong(endTime);
            
            while (!success) {
                String url = "https://mtr.gooeytrade.com/mtr-api/" + 
                Filehandler.readFromFile("systemUuid") + 
                "/candles?symbol=EURUSD&interval=M1";
                
                Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
                .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
                .build();
                
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(response.body().string());
                        JsonNode candlesArray = rootNode.get("candles");
                        
                        double extremeValue = side.equalsIgnoreCase("High") ? Double.MIN_VALUE : Double.MAX_VALUE;
                        
                        for (JsonNode candleNode : candlesArray) {
                            long candleTime = candleNode.get("time").asLong();
                            
                            if (candleTime >= startTimestamp && candleTime <= endTimestamp) {
                                if (side.equalsIgnoreCase("High")) {
                                    double high = candleNode.get("high").asDouble();
                                    extremeValue = Math.max(extremeValue, high);
                                } else if (side.equalsIgnoreCase("Low")) {
                                    double low = candleNode.get("low").asDouble();
                                    extremeValue = Math.min(extremeValue, low);
                                }
                            }
                        }
                        
                        if (extremeValue != Double.MIN_VALUE && extremeValue != Double.MAX_VALUE) {
                            result = String.format("%.5f", extremeValue);
                        }
                        success = true;
                    }
                    if (!success) login();
                }
            }
        }
        return result;
    }
    
    public static String getEntryPrice(String startTime, String endTime, String side) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String result = "0";
        boolean success = false;
        long startTimestamp = Long.parseLong(startTime);
        long endTimestamp = Long.parseLong(endTime);
        
        while (!success) {
            String url = "https://mtr.gooeytrade.com/mtr-api/" +
            Filehandler.readFromFile("systemUuid") +
            "/candles?symbol=EURUSD&interval=M1";
            
            Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode candlesArray = rootNode.get("candles");
                    
                    double entryPrice = side.equalsIgnoreCase("BUY") ? Double.MAX_VALUE : Double.MIN_VALUE;
                    boolean foundValidCandle = false;
                    
                    for (JsonNode candleNode : candlesArray) {
                        long candleTime = candleNode.get("time").asLong();
                        if (candleTime >= startTimestamp && candleTime <= endTimestamp) {
                            double openPrice = candleNode.get("open").asDouble();
                            double closePrice = candleNode.get("close").asDouble();
                            
                            if (side.equalsIgnoreCase("BUY")) {
                                double minPrice = Math.min(openPrice, closePrice);
                                entryPrice = Math.min(entryPrice, minPrice);
                            } else if (side.equalsIgnoreCase("SELL")) {
                                double maxPrice = Math.max(openPrice, closePrice);
                                entryPrice = Math.max(entryPrice, maxPrice);
                            }
                            foundValidCandle = true;
                        }
                    }
                    
                    if (foundValidCandle) {
                        result = String.format("%.5f", entryPrice);
                    }
                    success = true;
                }
                if (!success) {
                    login();
                }
            }
        }
        return result;
    }
    
    public static String getOpenPositions() throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();
        String res="";
        boolean success=false;
        while (!success) {
            String url = "https://mtr.gooeytrade.com/mtr-api/" + Filehandler.readFromFile("systemUuid") + "/open-positions";
            
            Request request = new Request.Builder()
            .url(url)
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .addHeader("Accept", "application/json")
            .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    success=true;
                    res = response.body().string();
                }
                if(!success) login();
            }
        }
        return res;
    }

    public static List<Double> getOpenPrices() throws IOException {
        OkHttpClient client = new OkHttpClient();
        List<Double> openPrices = new ArrayList<>();
        boolean success = false;
        
        while (!success) {
            String url = "https://mtr.gooeytrade.com/mtr-api/" + 
                        Filehandler.readFromFile("systemUuid") + "/open-positions";
                        
            Request request = new Request.Builder()
                .url(url)
                .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
                .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
                .addHeader("Accept", "application/json")
                .build();
                
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode positions = rootNode.get("positions");
                    
                    for (JsonNode position : positions) {
                        double openPrice = position.get("openPrice").asDouble();
                        openPrices.add(openPrice);
                    }
                    success = true;
                }
                if (!success) login();
            }
        }
        return openPrices;
    }

    public double cumulativeSL() throws IOException, InterruptedException {
        String jsonResponse = getOpenPositions();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        double totalSL = 0.0;

        JsonNode positions = rootNode.get("positions");
        if (positions != null && positions.isArray()) {
            for (JsonNode position : positions) {
                int ContractSize=0;
                String symbol = position.get("symbol").asText();
                String side = position.get("side").asText();
                double openPrice = position.get("openPrice").asDouble();
                double stopLoss = position.get("stopLoss").asDouble();
                double volume = position.get("volume").asDouble();
                if(symbol.equals("EURUSD") || symbol.equals("GBPUSD")) ContractSize=100000;
                if(symbol.equals("XAUUSD")) ContractSize=1000;
                if(symbol.equals("BTCUSD") || symbol.equals("ETHUSD")) ContractSize=1;
                // System.out.println();
                // System.out.println(openPrice+" "+stopLoss+" "+volume);
                // System.out.println();
                double sl = (side.equalsIgnoreCase("BUY")) ? (openPrice - stopLoss) : (stopLoss - openPrice);
                // System.out.println(sl);
                totalSL += sl*volume*ContractSize;
            }
        }
        return totalSL;
    }

    public static String trade(String symbol, float volume, float price, String orderSide, double SL) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String res="";
        boolean success=false;
        while (!success) {
        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://mtr.gooeytrade.com/mtr-api/" + Filehandler.readFromFile("systemUuid") + "/position/open";

        String json = String.format(
            // "{\"instrument\":\"%s\",\"volume\":%f,\"orderSide\":\"%s\",\"slPrice\":%f,\"isMobile\":false}",
            // symbol, volume, orderSide, SL
            "{\"instrument\":\"%s\",\"volume\":%f,\"price\":%f,\"orderSide\":\"%s\",\"slPrice\":%f,\"isMobile\":false}",
            symbol, volume, price, orderSide, SL
        );
        
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .addHeader("Content-Type", "application/json")
            .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    String orderId = rootNode.get("orderId").asText();
                    success=true;
                    res = orderId;
                }
                if(response.code() == 400) return "TRADE_FAILED";
                if(!success) login();
            }
        }
        return res;
    }

    

    public static String partialClosePosition(String positionId, float volume, String orderSide, String symbol) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String res="";
        boolean success=false;
        while (!success) {
        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://mtr.gooeytrade.com/mtr-api/" + Filehandler.readFromFile("systemUuid") + "/position/close-partially";

        volume=(float)Math.floor(volume*100)/100;
        String json = String.format(
            "{\"positionId\":\"%s\",\"volume\":%f,\"isMobile\":true,\"instrument\":\"%s\",\"orderSide\":\"%s\"}",
            positionId, volume, symbol, orderSide
        );

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .build();

            try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    res = response.body().string();
                    success=true;
                }
                if(!success) login();
            }
        }
        return res;
    }

    public static String closePosition(String positionId) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();
        String res="";
        boolean success=false;
        while (!success) {
        String url = "https://mtr.gooeytrade.com/mtr-api/" + Filehandler.readFromFile("systemUuid") + "/position/" + positionId;

        Request request = new Request.Builder()
            .url(url)
            .delete()
            .addHeader("Auth-trading-api", Filehandler.readFromFile("tradingApiToken"))
            .addHeader("Cookie", Filehandler.readFromFile("Cookie"))
            .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null && response.header("Content-Type").equals("application/json")) {
                    res = "Position closed successfully.";
                    success=true;
                }
                if(!success) login();
            }
        }
        return res;
    }
}