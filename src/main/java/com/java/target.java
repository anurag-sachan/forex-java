package com.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class target {
    public static void setTargets() throws IOException{
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/trades.csv";
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;
        List<String> list=new ArrayList<>();
        while((line=br.readLine())!=null){
            String[] data= line.split(",");
            if(data.length==6){
                System.out.println(line);
                Scanner sc= new Scanner(System.in);
                System.out.print("enter target price: ");
                String str=sc.nextLine();
                line=line.concat(",tp:").concat(str);

                System.out.print("enter target cci[4H]: ");
                str=sc.nextLine();
                line=line.concat(",tc4h:").concat(str);

                System.out.print("enter target cci[1D]: ");
                str=sc.nextLine();
                line=line.concat(",tc1d:").concat(str);

                System.out.print("enter target cci[1W]: ");
                str=sc.nextLine();
                line=line.concat(",tc1w:").concat(str);

                list.add(line+"\n");
            }
        }
        Filehandler.writeToTradeCSV(list, true);
    }

    public static void newOP() throws IOException{
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/trades.csv";
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;
        HashSet<String> set=new HashSet<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            set.add(id);
        }

        file="/Users/anurag/Desktop/forex/src/main/java/com/java/op.csv";
        br=new BufferedReader(new FileReader(file));
        line="";
        List<String> list=new ArrayList<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            if(!set.contains(id)) list.add(line+"\n");
        }
        Filehandler.writeToTradeCSV(list, true);
    }

    public static void cleanup() throws IOException{
        String file="/Users/anurag/Desktop/forex/src/main/java/com/java/trades.csv";
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

        file="/Users/anurag/Desktop/forex/src/main/java/com/java/op.csv";
        br=new BufferedReader(new FileReader(file));
        line="";
        HashSet<String> set=new HashSet<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            set.add(id);
        }

        file="/Users/anurag/Desktop/forex/src/main/java/com/java/trades.csv";
        br=new BufferedReader(new FileReader(file));
        line="";
        List<String> newList=new ArrayList<>();
        while((line=br.readLine())!=null){
            String id= line.split(",")[0];
            if(set.contains(id)) newList.add(line+"\n");
        }
        Filehandler.writeToTradeCSV(newList, false);
    }
}
