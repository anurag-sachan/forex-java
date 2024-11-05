package com.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        while (true) {
            Date date=new Date();
            System.out.printf("-------------- %s --------------\n",date);
            try{
                iceberg();
            }catch(Exception e){
                System.err.println(e);
                System.out.println("Error in the main thread. Retrying!");
                Thread.sleep(2000);
            }
        }
    }

    public static void iceberg() throws IOException, InterruptedException{
        //for new accounts, change acc number in URL
        prop.openPosition();
        target.newOP();
        target.setTargets();
        target.cleanup();

        strategy();
        // Thread.sleep(3000);
        Thread.sleep(300000);
    }
    
    static HashSet<String> set=new HashSet<>();
    public static void strategy() throws IOException, InterruptedException{
        String[] pairs={"XAUUSD","EURUSD","GBPUSD","USDJPY"};
        String mailEntry="";
        for(String pair : pairs){
            float cci15m= Algo.getCCI(pair, "|15");
            float cci4h= Algo.getCCI(pair, "|240");
            float cci1d= Algo.getCCI(pair, "");
            float cci1w= Algo.getCCI(pair, "|1W");
            
            float LTP= Algo.LTP(pair);
            mailEntry=Algo.alerts(pair, LTP, mailEntry);
            
            System.out.println();
            System.out.printf("\n"+pair+": "+LTP+"\n"+cci1w+" "+cci1d+" "+cci4h+" "+cci15m+"\n");
            
            HashMap<String, List<String>> map= target.getTargetVals();
            List<String> targets= new ArrayList<>();
            if(map.containsKey(pair)){ 
                targets= map.get(pair);
                int sizeTargets= targets.size();

                //cci-targets
                // String symbol="";
                // if(pair.startsWith("X")) symbol="XU";
                // if(pair.startsWith("E")) symbol="EU";
                // if(pair.startsWith("G")) symbol="GU";
                // if(pair.startsWith("U")) symbol="UJ";
                // symbol="CCI4H"+symbol;

                if(targets.get(0).equals("buy")){
                    // if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile(symbol))-100)/2){
                    // // if(cci15m>100 || cci4h>100 || cci1d>100 || cci1w>100){
                    if(cci4h>100 || cci1d>100){
                        set.add(pair);
                        System.out.println("🚀 CCI TARGET. TRAIL SL ⚠️");
                    }
                    for(int i=1; i<sizeTargets; i++){
                        if(LTP>Float.parseFloat(targets.get(i))){
                            set.add(pair);
                            System.out.println("🚀 PRICE TARGET. TRAIL SL ⚠️");
                        }
                    }
                }
                else if(targets.get(0).equals("sell")){
                    // if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile(symbol))+100)/2){
                    // // if(cci15m<-100 || cci4h<-100 || cci1d<-100 || cci1w<-100){
                    if(cci4h<-100 || cci1d<-100){
                        set.add(pair);
                        System.out.println("🚀 CCI TARGET. TRAIL SL ⚠️");
                    }
                    for(int i=1; i<sizeTargets; i++){
                        if(LTP<Float.parseFloat(targets.get(i))){
                            set.add(pair);
                            System.out.println("🚀 PRICE TARGET. TRAIL SL ⚠️");
                        }
                    }
                }
            }

            // if((cci4h>90 && cci4h<110) || (cci1d>90 && cci1d<110) || (cci1w>90 && cci1w<110)) System.out.println("ZONE🔥");

            // if(pair.equals("XAUUSD")){
            //     String[] vals= Filehandler.readFromFile("XU").split(" ");
            //     for(String val: vals){
            //         float num= Float.parseFloat(val);
            //         float max= (float)(num + num*0.10);
            //         float min= (float)(num - num*0.10);
            //         if(LTP>min && LTP<max) System.out.println("💎 HIGH VOLUME ZONE : "+num);
            //     }
            // }
            // if(pair.equals("EURUSD")){
            //     String[] vals= Filehandler.readFromFile("EU").split(" ");
            //     for(String val: vals){
            //         float num= Float.parseFloat(val);
            //         float max= (float)(num + num*0.10);
            //         float min= (float)(num - num*0.10);
            //         if(LTP>min && LTP<max) System.out.println("💎 HIGH VOLUME ZONE : "+num);
            //     }
            // }
            // if(pair.equals("GBPUSD")){
            //     String[] vals= Filehandler.readFromFile("GU").split(" ");
            //     for(String val: vals){
            //         float num= Float.parseFloat(val);
            //         float max= (float)(num + num*0.10);
            //         float min= (float)(num - num*0.10);
            //         if(LTP>min && LTP<max) System.out.println("💎 HIGH VOLUME ZONE : "+num);
            //     }
            // }
            // if(pair.equals("USDJPY")){
            //     String[] vals= Filehandler.readFromFile("UJ").split(" ");
            //     for(String val: vals){
            //         float num= Float.parseFloat(val);
            //         float max= (float)(num + num*0.10);
            //         float min= (float)(num - num*0.10);
            //         if(LTP>min && LTP<max) System.out.println("💎 HIGH VOLUME ZONE : "+num);
            //     }
            // }

            if(cci15m>=-100 && cci15m<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU15").split("")[2].equals("0")) Filehandler.writeToFile("XU15", Filehandler.readFromFile("XU15").concat("0").substring(1, 4));
                else if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU15").split("")[2].equals("0")) Filehandler.writeToFile("EU15", Filehandler.readFromFile("EU15").concat("0").substring(1, 4));
                else if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU15").split("")[2].equals("0")) Filehandler.writeToFile("GU15", Filehandler.readFromFile("GU15").concat("0").substring(1, 4));
                else if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ15").split("")[2].equals("0")) Filehandler.writeToFile("UJ15", Filehandler.readFromFile("UJ15").concat("0").substring(1, 4));
            }
            else if(cci15m<-100){
                if(pair.equals("XAUUSD")){
                    if(cci15m<Float.parseFloat(Filehandler.readFromFile("CCI15XU"))) Filehandler.writeToFile("CCI15XU", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("XU15").split("")[2].equals("1")) Filehandler.writeToFile("XU15", Filehandler.readFromFile("XU15").concat("1").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci15m<Float.parseFloat(Filehandler.readFromFile("CCI15EU"))) Filehandler.writeToFile("CCI15EU", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("EU15").split("")[2].equals("1")) Filehandler.writeToFile("EU15", Filehandler.readFromFile("EU15").concat("1").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci15m<Float.parseFloat(Filehandler.readFromFile("CCI15GU"))) Filehandler.writeToFile("CCI15GU", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("GU15").split("")[2].equals("1")) Filehandler.writeToFile("GU15", Filehandler.readFromFile("GU15").concat("1").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci15m<Float.parseFloat(Filehandler.readFromFile("CCI15UJ"))) Filehandler.writeToFile("CCI15UJ", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("UJ15").split("")[2].equals("1")) Filehandler.writeToFile("UJ15", Filehandler.readFromFile("UJ15").concat("1").substring(1, 4));
                }
            }
            else if(cci15m>100){
                if(pair.equals("XAUUSD")){
                    if(cci15m>Float.parseFloat(Filehandler.readFromFile("CCI15XU"))) Filehandler.writeToFile("CCI15XU", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("XU15").split("")[2].equals("2")) Filehandler.writeToFile("XU15", Filehandler.readFromFile("XU15").concat("2").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci15m>Float.parseFloat(Filehandler.readFromFile("CCI15GU"))) Filehandler.writeToFile("CCI15GU", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("EU15").split("")[2].equals("2")) Filehandler.writeToFile("EU15", Filehandler.readFromFile("EU15").concat("2").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci15m>Float.parseFloat(Filehandler.readFromFile("CCI15EU"))) Filehandler.writeToFile("CCI15EU", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("GU15").split("")[2].equals("2")) Filehandler.writeToFile("GU15", Filehandler.readFromFile("GU15").concat("2").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci15m>Float.parseFloat(Filehandler.readFromFile("CCI15UJ"))) Filehandler.writeToFile("CCI15UJ", String.valueOf(cci15m));
                    if(!Filehandler.readFromFile("UJ15").split("")[2].equals("2")) Filehandler.writeToFile("UJ15", Filehandler.readFromFile("UJ15").concat("2").substring(1, 4));
                }
            }

            if(cci4h>=-100 && cci4h<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU4H").split("")[2].equals("0")) Filehandler.writeToFile("XU4H", Filehandler.readFromFile("XU4H").concat("0").substring(1, 4));
                else if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU4H").split("")[2].equals("0")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("0").substring(1, 4));
                else if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU4H").split("")[2].equals("0")) Filehandler.writeToFile("GU4H", Filehandler.readFromFile("GU4H").concat("0").substring(1, 4));
                else if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ4H").split("")[2].equals("0")) Filehandler.writeToFile("UJ4H", Filehandler.readFromFile("UJ4H").concat("0").substring(1, 4));
            }
            else if(cci4h<-100){
                if(pair.equals("XAUUSD")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))) Filehandler.writeToFile("CCI4HXU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("XU4H").split("")[2].equals("1")) Filehandler.writeToFile("XU4H", Filehandler.readFromFile("XU4H").concat("1").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))) Filehandler.writeToFile("CCI4HEU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("EU4H").split("")[2].equals("1")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("1").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))) Filehandler.writeToFile("CCI4HGU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("GU4H").split("")[2].equals("1")) Filehandler.writeToFile("GU4H", Filehandler.readFromFile("GU4H").concat("1").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))) Filehandler.writeToFile("CCI4HUJ", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("UJ4H").split("")[2].equals("1")) Filehandler.writeToFile("UJ4H", Filehandler.readFromFile("UJ4H").concat("1").substring(1, 4));
                }
            }
            else if(cci4h>100){
                if(pair.equals("XAUUSD")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))) Filehandler.writeToFile("CCI4HXU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("XU4H").split("")[2].equals("2")) Filehandler.writeToFile("XU4H", Filehandler.readFromFile("XU4H").concat("2").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))) Filehandler.writeToFile("CCI4HEU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("EU4H").split("")[2].equals("2")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("2").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))) Filehandler.writeToFile("CCI4HGU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("GU4H").split("")[2].equals("2")) Filehandler.writeToFile("GU4H", Filehandler.readFromFile("GU4H").concat("2").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))) Filehandler.writeToFile("CCI4HUJ", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("UJ4H").split("")[2].equals("2")) Filehandler.writeToFile("UJ4H", Filehandler.readFromFile("UJ4H").concat("2").substring(1, 4));
                }
            }

            if(cci1d>=-100 && cci1d<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU1D").split("")[2].equals("0")) Filehandler.writeToFile("XU1D", Filehandler.readFromFile("XU1D").concat("0").substring(1, 4));
                else if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU1D").split("")[2].equals("0")) Filehandler.writeToFile("EU1D", Filehandler.readFromFile("EU1D").concat("0").substring(1, 4));
                else if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU1D").split("")[2].equals("0")) Filehandler.writeToFile("GU1D", Filehandler.readFromFile("GU1D").concat("0").substring(1, 4));
                else if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ1D").split("")[2].equals("0")) Filehandler.writeToFile("UJ1D", Filehandler.readFromFile("UJ1D").concat("0").substring(1, 4));
            }
            else if(cci1d<-100){
                if(pair.equals("XAUUSD")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DXU"))) Filehandler.writeToFile("CCI1DXU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("XU1D").split("")[2].equals("1")) Filehandler.writeToFile("XU1D", Filehandler.readFromFile("XU1D").concat("1").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DEU"))) Filehandler.writeToFile("CCI1DEU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("EU1D").split("")[2].equals("1")) Filehandler.writeToFile("EU1D", Filehandler.readFromFile("EU1D").concat("1").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DGU"))) Filehandler.writeToFile("CCI1DGU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("GU1D").split("")[2].equals("1")) Filehandler.writeToFile("GU1D", Filehandler.readFromFile("GU1D").concat("1").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DUJ"))) Filehandler.writeToFile("CCI1DUJ", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("UJ1D").split("")[2].equals("1")) Filehandler.writeToFile("UJ1D", Filehandler.readFromFile("UJ1D").concat("1").substring(1, 4));
                }
            }
            else if(cci1d>100){
                if(pair.equals("XAUUSD")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DXU"))) Filehandler.writeToFile("CCI1DXU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("XU1D").split("")[2].equals("2")) Filehandler.writeToFile("XU1D", Filehandler.readFromFile("XU1D").concat("2").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DEU"))) Filehandler.writeToFile("CCI1DEU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("EU1D").split("")[2].equals("2")) Filehandler.writeToFile("EU1D", Filehandler.readFromFile("EU1D").concat("2").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DGU"))) Filehandler.writeToFile("CCI1DGU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("GU1D").split("")[2].equals("2")) Filehandler.writeToFile("GU1D", Filehandler.readFromFile("GU1D").concat("2").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DUJ"))) Filehandler.writeToFile("CCI1DUJ", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("UJ1D").split("")[2].equals("2")) Filehandler.writeToFile("UJ1D", Filehandler.readFromFile("UJ1D").concat("2").substring(1, 4));
                }
            }
            if(cci1w>=-100 && cci1w<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU1W").split("")[2].equals("0")) Filehandler.writeToFile("XU1W", Filehandler.readFromFile("XU1W").concat("0").substring(1, 4));
                else if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU1W").split("")[2].equals("0")) Filehandler.writeToFile("EU1W", Filehandler.readFromFile("EU1W").concat("0").substring(1, 4));
                else if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU1W").split("")[2].equals("0")) Filehandler.writeToFile("GU1W", Filehandler.readFromFile("GU1W").concat("0").substring(1, 4));
                else if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ1W").split("")[2].equals("0")) Filehandler.writeToFile("UJ1W", Filehandler.readFromFile("UJ1W").concat("0").substring(1, 4));
            }
            else if(cci1w<-100){
                if(pair.equals("XAUUSD")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WXU"))) Filehandler.writeToFile("CCI1WXU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("XU1W").split("")[2].equals("1")) Filehandler.writeToFile("XU1W", Filehandler.readFromFile("XU1W").concat("1").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WEU"))) Filehandler.writeToFile("CCI1WEU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("EU1W").split("")[2].equals("1")) Filehandler.writeToFile("EU1W", Filehandler.readFromFile("EU1W").concat("1").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WGU"))) Filehandler.writeToFile("CCI1WGU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("GU1W").split("")[2].equals("1")) Filehandler.writeToFile("GU1W", Filehandler.readFromFile("GU1W").concat("1").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WUJ"))) Filehandler.writeToFile("CCI1WUJ", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("UJ1W").split("")[2].equals("1")) Filehandler.writeToFile("UJ1W", Filehandler.readFromFile("UJ1W").concat("1").substring(1, 4));
                }
            }
            else if(cci1w>100){
                if(pair.equals("XAUUSD")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WXU"))) Filehandler.writeToFile("CCI1WXU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("XU1W").split("")[2].equals("2")) Filehandler.writeToFile("XU1W", Filehandler.readFromFile("XU1W").concat("2").substring(1, 4));
                }
                else if(pair.equals("EURUSD")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WEU"))) Filehandler.writeToFile("CCI1WEU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("EU1W").split("")[2].equals("2")) Filehandler.writeToFile("EU1W", Filehandler.readFromFile("EU1W").concat("2").substring(1, 4));
                }
                else if(pair.equals("GBPUSD")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WGU"))) Filehandler.writeToFile("CCI1WGU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("GU1W").split("")[2].equals("2")) Filehandler.writeToFile("GU1W", Filehandler.readFromFile("GU1W").concat("2").substring(1, 4));
                }
                else if(pair.equals("USDJPY")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WUJ"))) Filehandler.writeToFile("CCI1WUJ", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("UJ1W").split("")[2].equals("2")) Filehandler.writeToFile("UJ1W", Filehandler.readFromFile("UJ1W").concat("2").substring(1, 4));
                }
            }

            int trend15, trend4h, trend1d, trend1w;
            if(pair.startsWith("X")){
                trend15 = Filehandler.readFromFile("XU15").equals("102") || Filehandler.readFromFile("XU15").equals("202") || Filehandler.readFromFile("XU15").equals("010")? 1:Filehandler.readFromFile("XU15").equals("201") || Filehandler.readFromFile("XU15").equals("101") || Filehandler.readFromFile("XU15").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("XU4H").equals("102") || Filehandler.readFromFile("XU4H").equals("202") || Filehandler.readFromFile("XU4H").equals("010")? 1:Filehandler.readFromFile("XU4H").equals("201") || Filehandler.readFromFile("XU4H").equals("101") || Filehandler.readFromFile("XU4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("XU1D").equals("102") || Filehandler.readFromFile("XU1D").equals("202") || Filehandler.readFromFile("XU1D").equals("010")? 1:Filehandler.readFromFile("XU1D").equals("201") || Filehandler.readFromFile("XU1D").equals("101") || Filehandler.readFromFile("XU1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("XU1W").equals("102") || Filehandler.readFromFile("XU1W").equals("202") || Filehandler.readFromFile("XU1W").equals("010")? 1:Filehandler.readFromFile("XU1W").equals("201") || Filehandler.readFromFile("XU1W").equals("101") || Filehandler.readFromFile("XU1W").equals("020")?-1:0;
                int temp1w=0, temp1d=0, temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" - ");
                if(cci1w>100 && cci1w<100+(Float.parseFloat(Filehandler.readFromFile("CCI1WXU"))-100)/2) temp1w=trend1w*-1;
                else if(cci1w<-100 && cci1w>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1WXU"))+100)/2) temp1w=trend1w*-1;
                if(trend1w != temp1w) System.out.print(temp1w==1?"🟢":temp1w==-1?"🔴":"-");

                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" - ");
                if(cci1d>100 && cci1d<100+(Float.parseFloat(Filehandler.readFromFile("CCI1DXU"))-100)/2) temp1d=trend1d*-1;
                else if(cci1d<-100 && cci1d>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1DXU"))+100)/2) temp1d=trend1d*-1;
                if(trend1d != temp1d) System.out.print(temp1d==1?"🟢":temp1d==-1?"🔴":"-");

                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" - ");
                if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))-100)/2) temp4h=trend4h*-1;
                else if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))+100)/2) temp4h=trend4h*-1;
                if(trend4h != temp4h) System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"-");

                System.out.print(trend15==1?" 15M: 🟢 ":trend15==-1?" 15M: 🔴 ":" - ");
                System.out.print(" | ");
                boolean zone=false;
                if(((trend1w==1 || temp1w==1) && cci1w<-80) && ((trend1d==-1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80)) { System.out.println("🔥 4H : BUY"); zone=true; }
                else if(((trend1w==-1 || temp1w==-1) && cci1w>80) && ((trend1d==1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80)) { System.out.println("🔥 4H : SELL"); zone=true; }
                else if(((trend1d==1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80) && (trend15==-1 && cci15m<-100)) { System.out.println("🔥 15M : BUY"); zone=true; }
                else if(((trend1d==-1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80) && (trend15==1 && cci15m>100)) { System.out.println("🔥 15M : SELL"); zone=true; }
                if(zone) mailEntry=mailEntry.concat(pair+" ");
                // if(trend1w == trend1d && trend1d==temp4h && temp4h==trend15){
                    // mailEntry=mailEntry.concat(pair+" ");
                    // System.out.println("ENTRY 🔥");
                // }
            }
            
            if(pair.startsWith("E")){
                trend15 = Filehandler.readFromFile("EU15").equals("102") || Filehandler.readFromFile("EU15").equals("202") || Filehandler.readFromFile("EU15").equals("010")? 1:Filehandler.readFromFile("EU15").equals("201") || Filehandler.readFromFile("EU15").equals("101") || Filehandler.readFromFile("EU15").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("EU4H").equals("102") || Filehandler.readFromFile("EU4H").equals("202") || Filehandler.readFromFile("EU4H").equals("010")? 1:Filehandler.readFromFile("EU4H").equals("201") || Filehandler.readFromFile("EU4H").equals("101") || Filehandler.readFromFile("EU4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("EU1D").equals("102") || Filehandler.readFromFile("EU1D").equals("202") || Filehandler.readFromFile("EU1D").equals("010")? 1:Filehandler.readFromFile("EU1D").equals("201") || Filehandler.readFromFile("EU1D").equals("101") || Filehandler.readFromFile("EU1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("EU1W").equals("102") || Filehandler.readFromFile("EU1W").equals("202") || Filehandler.readFromFile("EU1W").equals("010")? 1:Filehandler.readFromFile("EU1W").equals("201") || Filehandler.readFromFile("EU1W").equals("101") || Filehandler.readFromFile("EU1W").equals("020")?-1:0;
                int temp1w=0, temp1d=0, temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" - ");
                if(cci1w>100 && cci1w<100+(Float.parseFloat(Filehandler.readFromFile("CCI1WEU"))-100)/2) temp1w=trend1w*-1;
                else if(cci1w<-100 && cci1w>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1WEU"))+100)/2) temp1w=trend1w*-1;
                if(trend1w != temp1w) System.out.print(temp1w==1?"🟢":temp1w==-1?"🔴":"-");

                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" - ");
                if(cci1d>100 && cci1d<100+(Float.parseFloat(Filehandler.readFromFile("CCI1DEU"))-100)/2) temp1d=trend1d*-1;
                else if(cci1d<-100 && cci1d>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1DEU"))+100)/2) temp1d=trend1d*-1;
                if(trend1d != temp1d) System.out.print(temp1d==1?"🟢":temp1d==-1?"🔴":"-");

                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" - ");
                if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))-100)/2) temp4h=trend4h*-1;
                else if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))+100)/2) temp4h=trend4h*-1;
                if(trend4h != temp4h) System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"-");

                System.out.print(trend15==1?" 15M: 🟢 ":trend15==-1?" 15M: 🔴":" - ");
                System.out.print(" | ");
                boolean zone=false;
                if(((trend1w==1 || temp1w==1) && cci1w<-80) && ((trend1d==-1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80)) { System.out.println("🔥 4H : BUY"); zone=true; }
                else if(((trend1w==-1 || temp1w==-1) && cci1w>80) && ((trend1d==1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80)) { System.out.println("🔥 4H : SELL"); zone=true; }
                else if(((trend1d==1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80) && (trend15==-1 && cci15m<-100)) { System.out.println("🔥 15M : BUY"); zone=true; }
                else if(((trend1d==-1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80) && (trend15==1 && cci15m>100)) { System.out.println("🔥 15M : SELL"); zone=true; }
                if(zone) mailEntry=mailEntry.concat(pair+" ");
                // if(((trend1w==1 && trend1d==-1 && cci1d<-100 && temp4h==1) || (trend1w==-1 && trend1d==1 && cci1d<100 && temp4h==-1)) || (trend1d==1 && trend4h==-1 && cci4h<-100 && trend15==1) || (trend1d==-1 && trend4h==1 && cci4h<100 && trend15==-1)){
                // if(trend1w == trend1d && trend1d==temp4h && temp4h==trend15){
                    // mailEntry=mailEntry.concat(pair+" ");
                    // System.out.println("ENTRY 🔥");
                // }
            }

            if(pair.startsWith("G")){
                trend15 = Filehandler.readFromFile("GU15").equals("102") || Filehandler.readFromFile("GU15").equals("202") || Filehandler.readFromFile("GU15").equals("010")? 1:Filehandler.readFromFile("GU15").equals("201") || Filehandler.readFromFile("GU15").equals("101") || Filehandler.readFromFile("GU15").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("GU4H").equals("102") || Filehandler.readFromFile("GU4H").equals("202") || Filehandler.readFromFile("GU4H").equals("010")? 1:Filehandler.readFromFile("GU4H").equals("201") || Filehandler.readFromFile("GU4H").equals("101") || Filehandler.readFromFile("GU4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("GU1D").equals("102") || Filehandler.readFromFile("GU1D").equals("202") || Filehandler.readFromFile("GU1D").equals("010")? 1:Filehandler.readFromFile("GU1D").equals("201") || Filehandler.readFromFile("GU1D").equals("101") || Filehandler.readFromFile("GU1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("GU1W").equals("102") || Filehandler.readFromFile("GU1W").equals("202") || Filehandler.readFromFile("GU1W").equals("010")? 1:Filehandler.readFromFile("GU1W").equals("201") || Filehandler.readFromFile("GU1W").equals("101") || Filehandler.readFromFile("GU1W").equals("020")?-1:0;
                int temp1w=0, temp1d=0, temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" - ");
                if(cci1w>100 && cci1w<100+(Float.parseFloat(Filehandler.readFromFile("CCI1WGU"))-100)/2) temp1w=trend1w*-1;
                else if(cci1w<-100 && cci1w>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1WGU"))+100)/2) temp1w=trend1w*-1;
                if(trend1w != temp1w) System.out.print(temp1w==1?"🟢":temp1w==-1?"🔴":"-");

                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" - ");
                if(cci1d>100 && cci1d<100+(Float.parseFloat(Filehandler.readFromFile("CCI1DGU"))-100)/2) temp1d=trend1d*-1;
                else if(cci1d<-100 && cci1d>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1DGU"))+100)/2) temp1d=trend1d*-1;
                if(trend1d != temp1d) System.out.print(temp1d==1?"🟢":temp1d==-1?"🔴":"-");

                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" - ");
                if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))-100)/2) temp4h=trend4h*-1;
                else if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))+100)/2) temp4h=trend4h*-1;
                if(trend4h != temp4h) System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"-");

                System.out.print(trend15==1?" 15M: 🟢 ":trend15==-1?" 15M: 🔴 ":" - ");
                System.out.print(" | ");
                boolean zone=false;
                if(((trend1w==1 || temp1w==1) && cci1w<-80) && ((trend1d==-1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80)) { System.out.println("🔥 4H : BUY"); zone=true; }
                else if(((trend1w==-1 || temp1w==-1) && cci1w>80) && ((trend1d==1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80)) { System.out.println("🔥 4H : SELL"); zone=true; }
                else if(((trend1d==1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80) && (trend15==-1 && cci15m<-100)) { System.out.println("🔥 15M : BUY"); zone=true; }
                else if(((trend1d==-1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80) && (trend15==1 && cci15m>100)) { System.out.println("🔥 15M : SELL"); zone=true; }
                if(zone) mailEntry=mailEntry.concat(pair+" ");
                // if(((trend1w==1 && trend1d==-1 && cci1d<-100 && temp4h==1) || (trend1w==-1 && trend1d==1 && cci1d<100 && temp4h==-1)) || (trend1d==1 && trend4h==-1 && cci4h<-100 && trend15==1) || (trend1d==-1 && trend4h==1 && cci4h<100 && trend15==-1)){
                // if(trend1w == trend1d && trend1d==temp4h && temp4h==trend15){
                    // mailEntry=mailEntry.concat(pair+" ");
                //     System.out.println("ENTRY 🔥");
                // }
            }

            if(pair.startsWith("U")){
                trend15 = Filehandler.readFromFile("UJ15").equals("102") || Filehandler.readFromFile("UJ15").equals("202") || Filehandler.readFromFile("UJ15").equals("010")? 1:Filehandler.readFromFile("UJ15").equals("201") || Filehandler.readFromFile("UJ15").equals("101") || Filehandler.readFromFile("UJ15").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("UJ4H").equals("102") || Filehandler.readFromFile("UJ4H").equals("202") || Filehandler.readFromFile("UJ4H").equals("010")? 1:Filehandler.readFromFile("UJ4H").equals("201") || Filehandler.readFromFile("UJ4H").equals("101") || Filehandler.readFromFile("UJ4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("UJ1D").equals("102") || Filehandler.readFromFile("UJ1D").equals("202") || Filehandler.readFromFile("UJ1D").equals("010")? 1:Filehandler.readFromFile("UJ1D").equals("201") || Filehandler.readFromFile("UJ1D").equals("101") || Filehandler.readFromFile("UJ1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("UJ1W").equals("102") || Filehandler.readFromFile("UJ1W").equals("202") || Filehandler.readFromFile("UJ1W").equals("010")? 1:Filehandler.readFromFile("UJ1W").equals("201") || Filehandler.readFromFile("UJ1W").equals("101") || Filehandler.readFromFile("UJ1W").equals("020")?-1:0;
                int temp1w=0, temp1d=0, temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" - ");
                if(cci1w>100 && cci1w<100+(Float.parseFloat(Filehandler.readFromFile("CCI1WUJ"))-100)/2) temp1w=trend1w*-1;
                else if(cci1w<-100 && cci1w>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1WUJ"))+100)/2) temp1w=trend1w*-1;
                if(trend1w != temp1w) System.out.print(temp1w==1?"🟢":temp1w==-1?"🔴":"-");

                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" - ");
                if(cci1d>100 && cci1d<100+(Float.parseFloat(Filehandler.readFromFile("CCI1DUJ"))-100)/2) temp1d=trend1d*-1;
                else if(cci1d<-100 && cci1d>-100+(Float.parseFloat(Filehandler.readFromFile("CCI1DUJ"))+100)/2) temp1d=trend1d*-1;
                if(trend1d != temp1d) System.out.print(temp1d==1?"🟢":temp1d==-1?"🔴":"-");

                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" - ");
                if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))-100)/2) temp4h=trend4h*-1;
                else if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))+100)/2) temp4h=trend4h*-1;
                if(trend4h != temp4h) System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"-");

                System.out.print(trend15==1?" 15M: 🟢 ":trend15==-1?" 15M: 🔴 ":" - ");
                System.out.print(" | ");
                boolean zone=false;
                if(((trend1w==1 || temp1w==1) && cci1w<-80) && ((trend1d==-1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80)) { System.out.println("🔥 4H : BUY"); zone=true; }
                else if(((trend1w==-1 || temp1w==-1) && cci1w>80) && ((trend1d==1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80)) { System.out.println("🔥 4H : SELL"); zone=true; }
                else if(((trend1d==1 || temp1d==1) && cci1d<-80) && ((trend4h==-1 || temp4h==1) && cci4h<-80) && (trend15==-1 && cci15m<-100)) { System.out.println("🔥 15M : BUY"); zone=true; }
                else if(((trend1d==-1 || temp1d==-1) && cci1d>80) && ((trend4h==1 || temp4h==-1) && cci4h>80) && (trend15==1 && cci15m>100)) { System.out.println("🔥 15M : SELL"); zone=true; }
                if(zone) mailEntry=mailEntry.concat(pair+" ");
                // if(((trend1w==1 && trend1d==-1 && cci1d<-100 && temp4h==1) || (trend1w==-1 && trend1d==1 && cci1d<100 && temp4h==-1)) || (trend1d==1 && trend4h==-1 && cci4h<-100 && trend15==1) || (trend1d==-1 && trend4h==1 && cci4h<100 && trend15==-1)){
                // if(trend1w == trend1d && trend1d==temp4h && temp4h==trend15){
                    // mailEntry=mailEntry.concat(pair+" ");
                //     System.out.println("ENTRY 🔥");
                // }
            }
        }
        System.out.println();
        System.out.print("\n🚀 TARGETs Achieved: ");
        Iterator setVals = set.iterator(); 
        String targetVals="";
        while (setVals.hasNext()) { 
            targetVals=targetVals.concat(setVals.next()+" ");
        }
        if(!targetVals.isEmpty() && !targetVals.trim().equals(Filehandler.readFromFile("mailMsg"))){
            Filehandler.writeToFile("mailMsg", targetVals.trim());
            // Email.sendEmail(targetVals);
        }
        // System.out.println(targetVals+" ");
        // System.out.println(mailEntry);
        // System.out.println(Filehandler.readFromFile("mailEntry"));
        if(!mailEntry.isEmpty() && !mailEntry.trim().equals(Filehandler.readFromFile("mailEntry"))){
            Filehandler.writeToFile("mailEntry", mailEntry.trim());
            Email.sendEmail(mailEntry);
        }
        // System.out.println("New Entry: "+mailEntry);
        System.out.println();
        System.out.println(" ------------------------------------------ ");
    }
}