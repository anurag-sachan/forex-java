package com.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PendingTrades {
    public static List<String[]> readFromPreTrades() {
        String filePath="/Users/anurag/Desktop/forex/src/main/java/com/java/data/preTrades.csv";
        List<String[]> trades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                if (data.length == 3) {
                    trades.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trades;
    }

    public static boolean positionAlreadyExists(List<Double[]> openPrices, float LTP) {
        for (Double[] val : openPrices) {
            if (Math.min(val[0], val[1]) <= LTP && LTP <= Math.max(val[0], val[1])) {
                return true;
            }
        }
        return false;
    }

    public static void updatePreTradeCSV(List<String[]> updatedTrades) {
        String filePath="/Users/anurag/Desktop/forex/src/main/java/com/java/data/preTrades.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] trade : updatedTrades) {
                bw.write(String.join(" ", trade));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
