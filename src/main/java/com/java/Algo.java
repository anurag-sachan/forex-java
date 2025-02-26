package com.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Algo {

    static HttpClient client = HttpClient.newHttpClient();

    public static float LTP(String str) throws IOException, InterruptedException {
        String baseURL = "https://scanner.tradingview.com/symbol";
        String symbol = str;
        String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
        String fields = "close";
        String encodedFields = URLEncoder.encode(fields, StandardCharsets.UTF_8);
        String url = baseURL + "?symbol=FX:" + encodedSymbol + "&fields=" + encodedFields+ "&no_404=true";

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
        String url = baseURL + "?symbol=FX:" + encodedSymbol + "&fields=" + encodedFields+ "&no_404=true";
        
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

    public static float getEMA(String str, String time) throws IOException, InterruptedException {
        String baseURL = "https://scanner.tradingview.com/symbol";
        String symbol = str;
        String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
        String fields = "EMA200"+time;
        String encodedFields = URLEncoder.encode(fields, StandardCharsets.UTF_8);
        String url = baseURL + "?symbol=FX:" + encodedSymbol + "&fields=" + encodedFields+ "&no_404=true";
        
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

    public static String alerts(String pair, float LTP, String mailEntry) throws IOException, InterruptedException {
        String symbol="";
        if(pair.startsWith("X")) symbol="XU";
        else if(pair.startsWith("E")) symbol="EU";
        else if(pair.startsWith("G")) symbol="GU";
        else if(pair.startsWith("U")) symbol="UJ";
        // if(Filehandler.readFromFile(symbol).equals("0")){
        //     System.out.printf("\n\nEnter alert price for %s :",pair);
        //     Scanner sc= new Scanner(System.in);
        //     long startTime = System.currentTimeMillis();
        //     while ((System.currentTimeMillis() - startTime) < 10000 && System.in.available() == 0) {
        //         Thread.sleep(1000);
        //     }
        //     if (System.in.available() > 0){
        //         String str=sc.nextLine();
        //         String[] vals=str.split(" ");
        //         for(String val: vals){
        //             if(Float.parseFloat(val)>LTP) Filehandler.writeToFile(symbol, "G"+val);
        //             else Filehandler.writeToFile(symbol, "L"+val);
        //         }
        //     }
        //     else return;
        // } else{
            if(!Filehandler.readFromFile(symbol).equals("0")){
                String[] vals= Filehandler.readFromFile(symbol).split(" ");
                for(String val: vals){
                    if((val.charAt(0)=='G' && LTP>Float.parseFloat(val.substring(1))) || (val.charAt(0)=='L' && LTP<Float.parseFloat(val.substring(1)))){
                        System.out.println();
                        System.out.printf("\n⚠️ ALERT TRIGGERED : %s @ %f",pair,Float.parseFloat(val.substring(1)));
                        // mailEntry=mailEntry+pair+"@"+val.substring(1)+"\n";
                        mailEntry=mailEntry+pair+"@"+val.substring(1)+" ";
                    }
                }
            }
        // }
        return mailEntry;
    }

    // public static Float calculatePositionSize(double moneyToRisk, double pipAtRisk, double lotSize, double onePipSize, double rate) {
    public static Float calculatePositionSize(String symbol, float moneyToRisk, float pipAtRisk) {

        float lotSize = 0;
        float onePipSize = 0;
        float rate = 0;

        if(symbol.equals("EURUSD")){
            lotSize = 100000.0f;
            onePipSize = 0.0001f;
            rate = 1.0f;
        }

        float valuePerPip = (lotSize * onePipSize) / rate;
        float positionSize = moneyToRisk / (pipAtRisk * valuePerPip);
        
        float calculatedLotSize = positionSize * lotSize;
        float unitsPerLot = calculatedLotSize / lotSize;

        return (float)Math.floor(unitsPerLot * 100)/100;
    }
    
    private static float calculateTradeSL(double openPrice, double stopLoss) {
        return Math.abs((float)((openPrice - stopLoss)));
    }
    
    public static void syncPositions() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(MatchTrader.getOpenPositions());
        JsonNode positions = root.get("positions");
        Map<String, String[]> existingPositions = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/anurag/Desktop/forex/src/main/java/com/java/data/OpenPositions.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                existingPositions.put(parts[0], parts);
            }
        } catch (FileNotFoundException e) {
            new File("/Users/anurag/Desktop/forex/src/main/java/com/java/data/OpenPositions.csv").createNewFile();
        }
    
        Set<String> apiPositionIds = new HashSet<>();
        List<String> newPositions = new ArrayList<>();
        List<String> updatedPositions = new ArrayList<>();
    
        for (JsonNode position : positions) {
            String id = position.get("id").asText();
            String symbol = position.get("symbol").asText();
            String volume = position.get("volume").asText();
            String side = position.get("side").asText();
            double openPrice = position.get("openPrice").asDouble();
            float stopLoss = Float.parseFloat(position.get("stopLoss").asText());
            float slPips = calculateTradeSL(openPrice, stopLoss);
            // apiPositionIds.add(id);
            if(symbol.equals("EURUSD")){
                apiPositionIds.add(id);
    
                if (existingPositions.containsKey(id)) {
                    String[] existingPosition = existingPositions.get(id);
                    String existingVolume = existingPosition[3];
                    float existingSlPips = stopLoss / (Float.parseFloat(existingPosition[3]) * 100000);
                    
                    if (!volume.equals(existingVolume) || Math.abs(slPips - existingSlPips) > 0.0001) {
                        updatedPositions.add(String.format("%s,%s,%s,%s,%.2f",
                            id, existingPosition[1], existingPosition[2], volume, 
                            slPips * Float.parseFloat(volume) * 100000));
                    } else {
                        updatedPositions.add(String.join(",", existingPosition));
                    }
                } else {
                    System.out.println("New position found: " + id);
                    System.out.printf("Exit-strategy( CCI15M / CCI4H ) > %s, %s, %s :\n", id, symbol, side);
                    
                    Scanner sc = new Scanner(System.in);
                    long startTime = System.currentTimeMillis();
                    String strategy = "";
                    
                    while ((System.currentTimeMillis() - startTime) < 10000 && System.in.available() == 0) {
                        Thread.sleep(1000);
                    }
                    
                    if (System.in.available() > 0) {
                        strategy = sc.nextLine().trim();
                    } else {
                        strategy = "CCI15M";
                    }
                    
                    newPositions.add(String.format("%s,%s,%s,%s,%.2f",
                        id, strategy, side, volume, slPips * Float.parseFloat(volume) * 100000));
                }
            }
        }
    
        existingPositions.keySet().removeIf(id -> !apiPositionIds.contains(id));
    
        try (PrintWriter writer = new PrintWriter(new FileWriter("/Users/anurag/Desktop/forex/src/main/java/com/java/data/OpenPositions.csv"))) {
            for (String position : updatedPositions) {
                writer.println(position);
            }
            for (String position : newPositions) {
                writer.println(position);
            }
        }
    }

    public static HashMap<String,Float> getProfitablePositions() throws JsonMappingException, JsonProcessingException, IOException, InterruptedException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(MatchTrader.getOpenPositions());
        JsonNode positions = root.get("positions");
        
        HashMap<String, Float> tradeMap=new HashMap<>();
        HashMap<String, Float> volumeMap=new HashMap<>();
        for (JsonNode position : positions) {
            String id = position.get("id").asText();
            Float volume = Float.parseFloat(position.get("volume").asText());
            Float profit = Float.parseFloat(position.get("profit").asText());
            tradeMap.put(id, profit);
            volumeMap.put(id, volume);
        }

        List<String[]> csvData = Filehandler.readFromOpenPositions();
        HashMap<String,Float> returnMap=new HashMap<>();
        for (String[] row : csvData) {
            String id = row[0];
            float csvSL = Float.parseFloat(row[row.length - 1]);
            if (tradeMap.containsKey(id)) {
                int factor=0;
                float volume=0.0f;
                if(tradeMap.get(id)>=2*csvSL){
                    // factor=(int)(tradeMap.get(id)/csvSL);
                    // volume=volumeMap.get(id)/factor;
                    volume=volumeMap.get(id)/2;
                    returnMap.put(id, volume);
                }
            }
        }
        return returnMap;
    }

    public static Float findSL(Float entryPrice, String[] highs, String[] lows, String side){
        float sl=0.0f;
        if(side.equalsIgnoreCase("BUY")){
            for(int i=lows.length-1; i>1; i--){
                int j=i-1;
                Float low=Float.parseFloat(lows[i]);
                Float prevLow=Float.parseFloat(lows[j]);
                if(entryPrice>prevLow && entryPrice<=low){
                    sl=prevLow;
                    break;
                }

            }
        }else if(side.equalsIgnoreCase("SELL")){
            for(int i=highs.length-1; i>1; i--){
                int j=i-1;
                Float high=Float.parseFloat(highs[i]);
                Float prevHigh=Float.parseFloat(highs[j]);
                if(entryPrice<prevHigh && entryPrice>=high){
                    sl=prevHigh;
                    break;
                }
            }
        }
        return sl;
    }

    public static boolean positionAlreadyExists(List<Double> openPrices, float sl){
        for(Double openPrice: openPrices){
            if(openPrice>=sl) return true;
        }
        return false;
    }
}