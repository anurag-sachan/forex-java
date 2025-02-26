package com.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Filehandler {
    public static String readFromFile(String str) throws IOException {
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/database.txt";
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;
        while ((line=br.readLine())!=null){
            String key=line.split(":")[0];
            String val;
            if(line.split(":").length>1) {
                if (key.equals("contract") || key.equals("buyTime") || key.equals("targetIndex")) val = line.split(":")[1].concat(":").concat(line.split(":")[2]);
                else val=line.split(":")[1];
            }
            else val="0";
            if(key.equals(str)) return val;
        }
        br.close();
        return "FAILED TO UPDATE <database.txt>.";
    }

    public static void writeToFile(String str, String newValue) throws IOException {
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/database.txt";
        File tempFile = new File(file + ".temp");

        try (BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String key = parts[0].trim();
                    String value="";
                    if(parts.length==2) value = parts[1].trim();
                    else value = parts[1].trim()+":"+parts[2].trim();

                    if (key.equals(str)) {
                        bw.write(key + ":" + newValue);
                    } else {
                        bw.write(key + ":" + value);
                    }

                    bw.newLine();
                } else {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }

        if (tempFile.renameTo(new File(file))) {
            ;
        } else {
            System.out.println("FAILED TO UPDATE <database.txt>.");
        }
    }

    public static void writeToFronttestCSV(String pair, float cci1W, float cci1D, float cci4H, float cci15M, float LTP, String time, String condition, boolean append) throws IOException {
        String csvFile = "/Users/anurag/Desktop/forex/src/main/java/com/java/data/Fronttest.csv";
        try (FileWriter csvWriter = new FileWriter(csvFile, append)) {
            csvWriter.append(String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%s,%s\n", pair, cci1W, cci1D, cci4H, cci15M, LTP, time, condition));
        }
    }

    public static void writeToOpenPositions(String pair, String exitCondition, String orderSide, float volume, float slPips, boolean append) throws IOException {
        String csvFile = "/Users/anurag/Desktop/forex/src/main/java/com/java/data/OpenPositions.csv";
        try (FileWriter csvWriter = new FileWriter(csvFile, append)) {
            csvWriter.append(String.format("%s,%s,%s,%.2f\n", pair, exitCondition, orderSide, volume));
        }
    }

    public static List<String[]> readFromOpenPositions() throws IOException {
        List<String[]> data = new ArrayList<>();
        String csvFile = "/Users/anurag/Desktop/forex/src/main/java/com/java/data/OpenPositions.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        }
        return data;
    }

    public static List<String[]> readFromTempTrades() throws FileNotFoundException, IOException {
        List<String[]> data = new ArrayList<>();
        String csvFile = "/Users/anurag/Desktop/forex/src/main/java/com/java/data/TempTrades.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        }
        return data;
    }

    public static void writeToTempTrades(List<String[]> remTrades) throws IOException {
        String csvFile = "/Users/anurag/Desktop/forex/src/main/java/com/java/data/TempTrades.csv";
        try (FileWriter csvWriter = new FileWriter(csvFile, false)) {
            if (remTrades != null && !remTrades.isEmpty()) {
                for (String[] trade : remTrades) {
                    String OpenPrice = trade[0];
                    String SL = trade[1];
                    csvWriter.append(String.format("%s,%s\n", OpenPrice, SL));
                }
            }
        }
    }
}
