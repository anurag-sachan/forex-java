package com.java;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties props = new Properties();
        String pathConfigFile="/Users/anurag/Desktop/forex/src/main/java/com/java/config.properties";
        try (InputStream input = new FileInputStream(pathConfigFile)) {
            props.load(input);
        }
        
        String propCookie = props.getProperty("op-cookie");
        prop.openPosition(propCookie);

        target.newOP();
        target.setTargets();
        target.cleanup();

        while (true) {
            strategy();
            Thread.sleep(300000);
        }
    }

    public static void strategy() throws IOException, InterruptedException{
        String[] pairs={"XAUUSD","EURUSD","GBPUSD","USDJPY"};
        for(String pair : pairs){
            float cci30m= Algo.getCCI(pair, "|30");
            float cci4h= Algo.getCCI(pair, "|240");
            float cci1d= Algo.getCCI(pair, "");
            float cci1w= Algo.getCCI(pair, "|1W");

            float LTP= Algo.LTP(pair);

            System.out.println();
            System.out.printf("\n"+pair+": "+LTP+"\n"+cci1w+" "+cci1d+" "+cci4h+" "+cci30m+"\n");
            if((cci4h>90 && cci4h<110) || (cci1d>90 && cci1d<110) || (cci1w>90 && cci1w<110)) System.out.println("🔥ZONE🔥");
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

            if(cci30m>=-100 && cci30m<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU30").split("")[2].equals("0")) Filehandler.writeToFile("XU30", Filehandler.readFromFile("XU30").concat("0").substring(1, 4));
                if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU30").split("")[2].equals("0")) Filehandler.writeToFile("EU30", Filehandler.readFromFile("EU30").concat("0").substring(1, 4));
                if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU30").split("")[2].equals("0")) Filehandler.writeToFile("GU30", Filehandler.readFromFile("GU30").concat("0").substring(1, 4));
                if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ30").split("")[2].equals("0")) Filehandler.writeToFile("UJ30", Filehandler.readFromFile("UJ30").concat("0").substring(1, 4));
            }
            if(cci30m<-100){
                if(pair.equals("XAUUSD")){
                    if(cci30m<Float.parseFloat(Filehandler.readFromFile("CCI30XU"))) Filehandler.writeToFile("CCI30XU", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("XU30").split("")[2].equals("1")) Filehandler.writeToFile("XU30", Filehandler.readFromFile("XU30").concat("1").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci30m<Float.parseFloat(Filehandler.readFromFile("CCI30EU"))) Filehandler.writeToFile("CCI30EU", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("EU30").split("")[2].equals("1")) Filehandler.writeToFile("EU30", Filehandler.readFromFile("EU30").concat("1").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci30m<Float.parseFloat(Filehandler.readFromFile("CCI30GU"))) Filehandler.writeToFile("CCI30GU", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("GU30").split("")[2].equals("1")) Filehandler.writeToFile("GU30", Filehandler.readFromFile("GU30").concat("1").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci30m<Float.parseFloat(Filehandler.readFromFile("CCI30UJ"))) Filehandler.writeToFile("CCI30UJ", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("UJ30").split("")[2].equals("1")) Filehandler.writeToFile("UJ30", Filehandler.readFromFile("UJ30").concat("1").substring(1, 4));
                }
            }
            if(cci30m>100){
                if(pair.equals("XAUUSD")){
                    if(cci30m>Float.parseFloat(Filehandler.readFromFile("CCI30XU"))) Filehandler.writeToFile("CCI30XU", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("XU30").split("")[2].equals("2")) Filehandler.writeToFile("XU30", Filehandler.readFromFile("XU30").concat("2").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci30m>Float.parseFloat(Filehandler.readFromFile("CCI30GU"))) Filehandler.writeToFile("CCI30GU", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("EU30").split("")[2].equals("2")) Filehandler.writeToFile("EU30", Filehandler.readFromFile("EU30").concat("2").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci30m>Float.parseFloat(Filehandler.readFromFile("CCI30EU"))) Filehandler.writeToFile("CCI30EU", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("GU30").split("")[2].equals("2")) Filehandler.writeToFile("GU30", Filehandler.readFromFile("GU30").concat("2").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci30m>Float.parseFloat(Filehandler.readFromFile("CCI30UJ"))) Filehandler.writeToFile("CCI30UJ", String.valueOf(cci30m));
                    if(!Filehandler.readFromFile("UJ30").split("")[2].equals("2")) Filehandler.writeToFile("UJ30", Filehandler.readFromFile("UJ30").concat("2").substring(1, 4));
                }
            }

            if(cci4h>=-100 && cci4h<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU4H").split("")[2].equals("0")) Filehandler.writeToFile("XU4H", Filehandler.readFromFile("XU4H").concat("0").substring(1, 4));
                if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU4H").split("")[2].equals("0")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("0").substring(1, 4));
                if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU4H").split("")[2].equals("0")) Filehandler.writeToFile("GU4H", Filehandler.readFromFile("GU4H").concat("0").substring(1, 4));
                if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ4H").split("")[2].equals("0")) Filehandler.writeToFile("UJ4H", Filehandler.readFromFile("UJ4H").concat("0").substring(1, 4));
            }
            if(cci4h<-100){
                if(pair.equals("XAUUSD")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))) Filehandler.writeToFile("CCI4HXU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("XU4H").split("")[2].equals("1")) Filehandler.writeToFile("XU4H", Filehandler.readFromFile("XU4H").concat("1").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))) Filehandler.writeToFile("CCI4HEU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("EU4H").split("")[2].equals("1")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("1").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))) Filehandler.writeToFile("CCI4HGU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("GU4H").split("")[2].equals("1")) Filehandler.writeToFile("GU4H", Filehandler.readFromFile("GU4H").concat("1").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))) Filehandler.writeToFile("CCI4HUJ", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("UJ4H").split("")[2].equals("1")) Filehandler.writeToFile("UJ4H", Filehandler.readFromFile("UJ4H").concat("1").substring(1, 4));
                }
            }
            if(cci4h>100){
                if(pair.equals("XAUUSD")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))) Filehandler.writeToFile("CCI4HXU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("XU4H").split("")[2].equals("2")) Filehandler.writeToFile("XU4H", Filehandler.readFromFile("XU4H").concat("2").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))) Filehandler.writeToFile("CCI4HEU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("EU4H").split("")[2].equals("2")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("2").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))) Filehandler.writeToFile("CCI4HGU", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("GU4H").split("")[2].equals("2")) Filehandler.writeToFile("GU4H", Filehandler.readFromFile("GU4H").concat("2").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))) Filehandler.writeToFile("CCI4HUJ", String.valueOf(cci4h));
                    if(!Filehandler.readFromFile("UJ4H").split("")[2].equals("2")) Filehandler.writeToFile("UJ4H", Filehandler.readFromFile("UJ4H").concat("2").substring(1, 4));
                }
            }

            if(cci1d>=-100 && cci1d<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU1D").split("")[2].equals("0")) Filehandler.writeToFile("XU1D", Filehandler.readFromFile("XU1D").concat("0").substring(1, 4));
                if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU1D").split("")[2].equals("0")) Filehandler.writeToFile("EU1D", Filehandler.readFromFile("EU1D").concat("0").substring(1, 4));
                if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU1D").split("")[2].equals("0")) Filehandler.writeToFile("GU1D", Filehandler.readFromFile("GU1D").concat("0").substring(1, 4));
                if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ1D").split("")[2].equals("0")) Filehandler.writeToFile("UJ1D", Filehandler.readFromFile("UJ1D").concat("0").substring(1, 4));
            }
            if(cci1d<-100){
                if(pair.equals("XAUUSD")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DXU"))) Filehandler.writeToFile("CCI1DXU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("XU1D").split("")[2].equals("1")) Filehandler.writeToFile("XU1D", Filehandler.readFromFile("XU1D").concat("1").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DEU"))) Filehandler.writeToFile("CCI1DEU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("EU1D").split("")[2].equals("1")) Filehandler.writeToFile("EU1D", Filehandler.readFromFile("EU1D").concat("1").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DGU"))) Filehandler.writeToFile("CCI1DGU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("GU1D").split("")[2].equals("1")) Filehandler.writeToFile("GU1D", Filehandler.readFromFile("GU1D").concat("1").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci1d<Float.parseFloat(Filehandler.readFromFile("CCI1DUJ"))) Filehandler.writeToFile("CCI1DUJ", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("UJ1D").split("")[2].equals("1")) Filehandler.writeToFile("UJ1D", Filehandler.readFromFile("UJ1D").concat("1").substring(1, 4));
                }
            }
            if(cci1d>100){
                if(pair.equals("XAUUSD")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DXU"))) Filehandler.writeToFile("CCI1DXU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("XU1D").split("")[2].equals("2")) Filehandler.writeToFile("XU1D", Filehandler.readFromFile("XU1D").concat("2").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DEU"))) Filehandler.writeToFile("CCI1DEU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("EU1D").split("")[2].equals("2")) Filehandler.writeToFile("EU1D", Filehandler.readFromFile("EU1D").concat("2").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DGU"))) Filehandler.writeToFile("CCI1DGU", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("GU1D").split("")[2].equals("2")) Filehandler.writeToFile("GU1D", Filehandler.readFromFile("GU1D").concat("2").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci1d>Float.parseFloat(Filehandler.readFromFile("CCI1DUJ"))) Filehandler.writeToFile("CCI1DUJ", String.valueOf(cci1d));
                    if(!Filehandler.readFromFile("UJ1D").split("")[2].equals("2")) Filehandler.writeToFile("UJ1D", Filehandler.readFromFile("UJ1D").concat("2").substring(1, 4));
                }
            }
            if(cci1w>=-100 && cci1w<=100){
                if(pair.equals("XAUUSD") && !Filehandler.readFromFile("XU1W").split("")[2].equals("0")) Filehandler.writeToFile("XU1W", Filehandler.readFromFile("XU1W").concat("0").substring(1, 4));
                if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU1W").split("")[2].equals("0")) Filehandler.writeToFile("EU1W", Filehandler.readFromFile("EU1W").concat("0").substring(1, 4));
                if(pair.equals("GBPUSD") && !Filehandler.readFromFile("GU1W").split("")[2].equals("0")) Filehandler.writeToFile("GU1W", Filehandler.readFromFile("GU1W").concat("0").substring(1, 4));
                if(pair.equals("USDJPY") && !Filehandler.readFromFile("UJ1W").split("")[2].equals("0")) Filehandler.writeToFile("UJ1W", Filehandler.readFromFile("UJ1W").concat("0").substring(1, 4));
            }
            if(cci1w<-100){
                if(pair.equals("XAUUSD")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WXU"))) Filehandler.writeToFile("CCI1WXU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("XU1W").split("")[2].equals("1")) Filehandler.writeToFile("XU1W", Filehandler.readFromFile("XU1W").concat("1").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WEU"))) Filehandler.writeToFile("CCI1WEU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("EU1W").split("")[2].equals("1")) Filehandler.writeToFile("EU1W", Filehandler.readFromFile("EU1W").concat("1").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WGU"))) Filehandler.writeToFile("CCI1WGU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("GU1W").split("")[2].equals("1")) Filehandler.writeToFile("GU1W", Filehandler.readFromFile("GU1W").concat("1").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci1w<Float.parseFloat(Filehandler.readFromFile("CCI1WUJ"))) Filehandler.writeToFile("CCI1WUJ", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("UJ1W").split("")[2].equals("1")) Filehandler.writeToFile("UJ1W", Filehandler.readFromFile("UJ1W").concat("1").substring(1, 4));
                }
            }
            if(cci1w>100){
                if(pair.equals("XAUUSD")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WXU"))) Filehandler.writeToFile("CCI1WXU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("XU1W").split("")[2].equals("2")) Filehandler.writeToFile("XU1W", Filehandler.readFromFile("XU1W").concat("2").substring(1, 4));
                }
                if(pair.equals("EURUSD")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WEU"))) Filehandler.writeToFile("CCI1WEU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("EU1W").split("")[2].equals("2")) Filehandler.writeToFile("EU1W", Filehandler.readFromFile("EU1W").concat("2").substring(1, 4));
                }
                if(pair.equals("GBPUSD")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WGU"))) Filehandler.writeToFile("CCI1WGU", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("GU1W").split("")[2].equals("2")) Filehandler.writeToFile("GU1W", Filehandler.readFromFile("GU1W").concat("2").substring(1, 4));
                }
                if(pair.equals("USDJPY")){
                    if(cci1w>Float.parseFloat(Filehandler.readFromFile("CCI1WUJ"))) Filehandler.writeToFile("CCI1WUJ", String.valueOf(cci1w));
                    if(!Filehandler.readFromFile("UJ1W").split("")[2].equals("2")) Filehandler.writeToFile("UJ1W", Filehandler.readFromFile("UJ1W").concat("2").substring(1, 4));
                }
            }

            int trend30, trend4h, trend1d, trend1w;
            if(pair.startsWith("X")){
                trend30 = Filehandler.readFromFile("XU30").equals("102") || Filehandler.readFromFile("XU30").equals("202") || Filehandler.readFromFile("XU30").equals("010")? 1:Filehandler.readFromFile("XU30").equals("201") || Filehandler.readFromFile("XU30").equals("101") || Filehandler.readFromFile("XU30").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("XU4H").equals("102") || Filehandler.readFromFile("XU4H").equals("202") || Filehandler.readFromFile("XU4H").equals("010")? 1:Filehandler.readFromFile("XU4H").equals("201") || Filehandler.readFromFile("XU4H").equals("101") || Filehandler.readFromFile("XU4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("XU1D").equals("102") || Filehandler.readFromFile("XU1D").equals("202") || Filehandler.readFromFile("XU1D").equals("010")? 1:Filehandler.readFromFile("XU1D").equals("201") || Filehandler.readFromFile("XU1D").equals("101") || Filehandler.readFromFile("XU1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("XU1W").equals("102") || Filehandler.readFromFile("XU1W").equals("202") || Filehandler.readFromFile("XU1W").equals("010")? 1:Filehandler.readFromFile("XU1W").equals("201") || Filehandler.readFromFile("XU1W").equals("101") || Filehandler.readFromFile("XU1W").equals("020")?-1:0;
                int temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" Neutral ");
                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" Neutral ");
                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" Neutral ");
                if(trend30 == trend1d){
                    if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))-100)/2) temp4h=trend4h*-1;
                    if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HXU"))+100)/2) temp4h=trend4h*-1;
                    System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"Neutral");
                }
                System.out.print(trend30==1?" 30M: 🟢 ":trend30==-1?" 30M: 🔴 ":" Neutral ");
                System.out.print(" | ");
                if(trend30 == trend1d){
                    System.out.println(trend1d == temp4h? "ENTRY 🔥": trend1d == -1*temp4h? "EXIT ⚠️" : "");
                }
            }
            
            if(pair.startsWith("E")){
                trend30 = Filehandler.readFromFile("EU30").equals("102") || Filehandler.readFromFile("EU30").equals("202") || Filehandler.readFromFile("EU30").equals("010")? 1:Filehandler.readFromFile("EU30").equals("201") || Filehandler.readFromFile("EU30").equals("101") || Filehandler.readFromFile("EU30").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("EU4H").equals("102") || Filehandler.readFromFile("EU4H").equals("202") || Filehandler.readFromFile("EU4H").equals("010")? 1:Filehandler.readFromFile("EU4H").equals("201") || Filehandler.readFromFile("EU4H").equals("101") || Filehandler.readFromFile("EU4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("EU1D").equals("102") || Filehandler.readFromFile("EU1D").equals("202") || Filehandler.readFromFile("EU1D").equals("010")? 1:Filehandler.readFromFile("EU1D").equals("201") || Filehandler.readFromFile("EU1D").equals("101") || Filehandler.readFromFile("EU1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("EU1W").equals("102") || Filehandler.readFromFile("EU1W").equals("202") || Filehandler.readFromFile("EU1W").equals("010")? 1:Filehandler.readFromFile("EU1W").equals("201") || Filehandler.readFromFile("EU1W").equals("101") || Filehandler.readFromFile("EU1W").equals("020")?-1:0;
                int temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" Neutral ");
                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" Neutral ");
                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" Neutral ");
                if(trend30 == trend1d){
                    if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))-100)/2) temp4h=trend4h*-1;
                    if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))+100)/2) temp4h=trend4h*-1;
                    System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"Neutral");
                }
                System.out.print(trend30==1?" 30M: 🟢 ":trend30==-1?" 30M: 🔴":" Neutral ");
                System.out.print(" | ");
                if(trend30 == trend1d){
                    System.out.print(trend1d == temp4h? "ENTRY 🔥": trend1d == -1*temp4h? "EXIT ⚠️" : "");
                }
            }

            if(pair.startsWith("G")){
                trend30 = Filehandler.readFromFile("GU30").equals("102") || Filehandler.readFromFile("GU30").equals("202") || Filehandler.readFromFile("GU30").equals("010")? 1:Filehandler.readFromFile("GU30").equals("201") || Filehandler.readFromFile("GU30").equals("101") || Filehandler.readFromFile("GU30").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("GU4H").equals("102") || Filehandler.readFromFile("GU4H").equals("202") || Filehandler.readFromFile("GU4H").equals("010")? 1:Filehandler.readFromFile("GU4H").equals("201") || Filehandler.readFromFile("GU4H").equals("101") || Filehandler.readFromFile("GU4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("GU1D").equals("102") || Filehandler.readFromFile("GU1D").equals("202") || Filehandler.readFromFile("GU1D").equals("010")? 1:Filehandler.readFromFile("GU1D").equals("201") || Filehandler.readFromFile("GU1D").equals("101") || Filehandler.readFromFile("GU1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("GU1W").equals("102") || Filehandler.readFromFile("GU1W").equals("202") || Filehandler.readFromFile("GU1W").equals("010")? 1:Filehandler.readFromFile("GU1W").equals("201") || Filehandler.readFromFile("GU1W").equals("101") || Filehandler.readFromFile("GU1W").equals("020")?-1:0;
                int temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" Neutral ");
                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" Neutral ");
                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" Neutral ");
                if(trend30 == trend1d){
                    if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))-100)/2) temp4h=trend4h*-1;
                    if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HGU"))+100)/2) temp4h=trend4h*-1;
                    System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"Neutral");
                }
                System.out.print(trend30==1?" 30M: 🟢 ":trend30==-1?" 30M: 🔴 ":" Neutral ");
                System.out.print(" | ");
                if(trend30 == trend1d){
                    System.out.print(trend1d == temp4h? "ENTRY 🔥": trend1d == -1*temp4h? "EXIT ⚠️" : "");
                }
            }

            if(pair.startsWith("U")){
                trend30 = Filehandler.readFromFile("UJ30").equals("102") || Filehandler.readFromFile("UJ30").equals("202") || Filehandler.readFromFile("UJ30").equals("010")? 1:Filehandler.readFromFile("UJ30").equals("201") || Filehandler.readFromFile("UJ30").equals("101") || Filehandler.readFromFile("UJ30").equals("020")?-1:0;
                trend4h = Filehandler.readFromFile("UJ4H").equals("102") || Filehandler.readFromFile("UJ4H").equals("202") || Filehandler.readFromFile("UJ4H").equals("010")? 1:Filehandler.readFromFile("UJ4H").equals("201") || Filehandler.readFromFile("UJ4H").equals("101") || Filehandler.readFromFile("UJ4H").equals("020")?-1:0;
                trend1d = Filehandler.readFromFile("UJ1D").equals("102") || Filehandler.readFromFile("UJ1D").equals("202") || Filehandler.readFromFile("UJ1D").equals("010")? 1:Filehandler.readFromFile("UJ1D").equals("201") || Filehandler.readFromFile("UJ1D").equals("101") || Filehandler.readFromFile("UJ1D").equals("020")?-1:0;
                trend1w = Filehandler.readFromFile("UJ1W").equals("102") || Filehandler.readFromFile("UJ1W").equals("202") || Filehandler.readFromFile("UJ1W").equals("010")? 1:Filehandler.readFromFile("UJ1W").equals("201") || Filehandler.readFromFile("UJ1W").equals("101") || Filehandler.readFromFile("UJ1W").equals("020")?-1:0;
                int temp4h=0;
                System.out.print(trend1w==1?" W: 🟢 ":trend1w==-1?" W: 🔴 ":" Neutral ");
                System.out.print(trend1d==1?" D: 🟢 ":trend1d==-1?" D: 🔴 ":" Neutral ");
                System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" Neutral ");
                if(trend30 == trend1d){
                    if(cci4h>100 && cci4h<100+(Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))-100)/2) temp4h=trend4h*-1;
                    if(cci4h<-100 && cci4h>-100+(Float.parseFloat(Filehandler.readFromFile("CCI4HUJ"))+100)/2) temp4h=trend4h*-1;
                    System.out.print(temp4h==1?"🟢":temp4h==-1?"🔴":"Neutral");
                }
                System.out.print(trend30==1?" 30M: 🟢 ":trend30==-1?" 30M: 🔴 ":" Neutral ");
                System.out.print(" | ");
                if(trend30 == trend1d){
                    System.out.print(trend1d == temp4h? "ENTRY 🔥": trend1d == -1*temp4h? "EXIT ⚠️" : "");
                }
            }
        }
        System.out.println();
        System.out.println(" ------------------------------------------ ");
    }
}