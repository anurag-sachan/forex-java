package com.java;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Properties;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class prop {
    static HttpClient client = HttpClient.newHttpClient();
    
    public static void openPosition() throws IOException, InterruptedException {
    // public static List<List<String>> openPosition() throws IOException, InterruptedException {

        Properties props = new Properties();
        String pathConfigFile="/Users/anurag/Desktop/forex/src/main/java/com/java/config/config.properties";
        try (InputStream input = new FileInputStream(pathConfigFile)) {
            props.load(input);
        }

        String propCookie = props.getProperty("op-cookie");
        
        String url = "https://backend.fundingpips.com/api/trading_accounts/100005904/open_positions?page=1";
        List<List<String>> list = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Cookie", propCookie)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());        
        if(response.statusCode()==200){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
            String[] query = {"identifier","symbol","action","entry_price","stop_loss_price","opened_at"};
            for(JsonNode node: jsonNode){
                List<String> tempList= new ArrayList<>();
                for(String q: query) tempList.add(node.get(q).asText());
                list.add(tempList);
                System.out.println();
            }
            Filehandler.writeToOPCSV(list, false);
        }
        else if(response.statusCode()==401){
            String token="";
            long startTime = System.currentTimeMillis();
            System.out.print("Enter latest Open Position Cookies: ");
            Scanner sc=new Scanner(System.in);
            while ((System.currentTimeMillis() - startTime) < 5000 && System.in.available() == 0) {
                Thread.sleep(1000);
            }
            if (System.in.available() > 0){
                token = sc.nextLine();
                try (InputStream input = new FileInputStream(pathConfigFile)) {
                    props.load(input);
                }
                props.setProperty("op-cookie", token);
                try (OutputStream output = new FileOutputStream(pathConfigFile)) {
                    props.store(output, null);
                }
                openPosition();
            }
            else return;
        }
    }
}
