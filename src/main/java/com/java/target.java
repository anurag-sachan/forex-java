package com.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class target {
    public static void newOP() throws IOException{
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/trades.csv";
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;
        HashSet<String> set=new HashSet<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            set.add(id);
        }

        file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/op.csv";
        br=new BufferedReader(new FileReader(file));
        line="";
        List<String> list=new ArrayList<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            if(!set.contains(id)){
                list.add(line+"\n");
            }
        }
        Filehandler.writeToTradeCSV(list, true);
    }

    public static void setTargets() throws IOException, InterruptedException{
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/trades.csv";
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;
        List<String> list=new ArrayList<>();
        while((line=br.readLine())!=null){
            String[] data= line.split(",");
            if(data.length==6){
                System.out.println(line);
                System.out.print("enter target price: ");
                Scanner sc= new Scanner(System.in);
                
                long startTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - startTime) < 7000 && System.in.available() == 0) {
                    Thread.sleep(1000);
                }
                if (System.in.available() > 0){
                    String str=sc.nextLine();
                    line=line.concat(","+str);
                    list.add(line+"\n");
                }
                else return;

                // String str=sc.nextLine();
                // line=line.concat(","+str);
                // list.add(line+"\n");
            }
        }
        Filehandler.writeToTradeCSV(list, true);
    }

    public static void cleanup() throws IOException{
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/trades.csv";
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;
        List<String> list=new ArrayList<>();
        while((line=br.readLine())!=null){
            String[] data= line.split(",");
            if(data.length!=6){
                list.add(line+"\n");
            }
        }
        Filehandler.writeToTradeCSV(list, false);

        file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/op.csv";
        br=new BufferedReader(new FileReader(file));
        line="";
        HashSet<String> set=new HashSet<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            set.add(id);
        }

        file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/trades.csv";
        br=new BufferedReader(new FileReader(file));
        line="";
        List<String> newList=new ArrayList<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            if(set.contains(id)) newList.add(line+"\n");
        }
        Filehandler.writeToTradeCSV(newList, false);
    }

    public static HashMap<String, List<String>> getTargetVals() throws IOException{
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/data/trades.csv";
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;
        HashMap<String, List<String>> map= new HashMap<>();
        while((line=br.readLine())!=null){
            String key= line.split(",")[1];
            List<String> list=new ArrayList<>();
            String orderType= line.split(",")[2];
            list.add(orderType);
            String[] arr= line.split(",")[6].split(" ");
            for(String vals: arr) list.add(vals);
            map.put(key, list);
        }
        return map;
    }
}