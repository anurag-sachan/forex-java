package com.java;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class prop {
    static HttpClient client = HttpClient.newHttpClient();
    public static List<List<String>> openPosition(String cookie) throws IOException, InterruptedException {
        String url = "https://backend.fundingpips.com/api/trading_accounts/100005904/open_positions?page=1";
        List<List<String>> list = new ArrayList<>();
        boolean success=false;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Cookie", cookie)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        if(jsonNode.size()==0) Filehandler.writeToOPCSV(new ArrayList<>(), false);
        if(jsonNode.size()!=0){
            String[] query = {"identifier","symbol","action","entry_price","stop_loss_price","opened_at"};
            for(JsonNode node: jsonNode){
                List<String> tempList= new ArrayList<>();
                for(String q: query) tempList.add(node.get(q).asText());
                list.add(tempList);
                System.out.println();
            }
            System.out.println(list);
            Filehandler.writeToOPCSV(list, false);
        }
        return list;
    }
}
