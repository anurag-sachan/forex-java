package com.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void writeToOPCSV(List<List<String>> op, boolean append) throws IOException {
        String csvFile = "/Users/anurag/Desktop/forex/src/main/java/com/java/data/op.csv";
        try (FileWriter csvWriter = new FileWriter(csvFile, append)) {
            for (List<String> position : op) {
                csvWriter.append(String.format("%s,%s,%s,%s,%s,%s\n",
                position.get(0),position.get(1),position.get(2),position.get(3),position.get(4),position.get(5)));
            }
        }
    }

    public static void writeToTradeCSV(List<String> list, boolean append) throws IOException {
        String csvFile = "/Users/anurag/Desktop/forex/src/main/java/com/java/data/trades.csv";
        try (FileWriter csvWriter = new FileWriter(csvFile, append)) {
            for (String position : list) {
                csvWriter.append(position);
            }
        }
    }
}
