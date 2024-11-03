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

import java.util.Scanner;

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

    public static String alerts(String pair, float LTP, String mailEntry) throws IOException, InterruptedException {
        String symbol="";
        if(pair.startsWith("X")) symbol="XU";
        if(pair.startsWith("E")) symbol="EU";
        if(pair.startsWith("G")) symbol="GU";
        if(pair.startsWith("U")) symbol="UJ";

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
            String[] vals= Filehandler.readFromFile(symbol).split(" ");
            for(String val: vals){
                if((val.charAt(0)=='G' && LTP>Float.parseFloat(val.substring(1))) || (val.charAt(0)=='L' && LTP<Float.parseFloat(val.substring(1)))){
                    System.out.println();
                    System.out.printf("\n⚠️ ALERT TRIGGERED : %s @ %f",pair,Float.parseFloat(val.substring(1)));
                    // mailEntry=mailEntry+pair+"@"+val.substring(1)+"\n";
                    mailEntry=mailEntry+pair+"@"+val.substring(1)+" ";
                }
            }
        // }
        return mailEntry;
    }
}