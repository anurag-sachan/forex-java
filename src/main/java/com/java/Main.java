package com.java;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception { 
        while (true) {
            Date date=new Date();
            System.out.printf("-------------- %s --------------\n",date);
            try{
                iceberg();
            }catch(Exception e){
                System.err.println(e);
                System.out.println("Error running iceberg. Retrying!");
                Thread.sleep(2000);
            }
        }
    }

    public static void iceberg() throws Exception{
        strategy();
        Thread.sleep(180000);
    }
    
    public static void strategy() throws Exception{
        Algo.syncPositions();
        HashSet<String> set=new HashSet<>();
        String[] pairs={"EURUSD"};
        String mailEntry="";

        for(String pair : pairs){
            float ema5m= Algo.getEMA(pair, "|5");
            float ema15m= Algo.getEMA(pair, "|15");
            System.out.println(ema5m+"< , > "+ema15m);
            float cci1m= Algo.getCCI(pair, "|1");
            float cci5m= Algo.getCCI(pair, "|5");
            float cci15m= Algo.getCCI(pair, "|15");
            float cci4h= Algo.getCCI(pair, "|240");
            float cci1d= 0.0f;
            float cci1w= 0.0f;
            // float cci1d= Algo.getCCI(pair, "");
            // float cci1w= Algo.getCCI(pair, "|1W");

            List<String[]> csvData = Filehandler.readFromOpenPositions();
            if(((cci4h>100 || cci15m>100) && cci1m<=100) || ((cci4h<-100 || cci15m<-100) && cci1m>=-100)){
                List<String> exitList=new ArrayList<>();
                for (String[] row : csvData) {
                    String id = row[0];
                    String condition = row[1];
                    String side = row[2];
                    if (side.equals("BUY") && (cci4h>100 && condition.equals("CCI4H") || cci15m>100 && condition.equals("CCI15M")) && cci1m<=100) exitList.add(id);
                    if (side.equals("SELL") && (cci4h<-100 && condition.equals("CCI4H") || cci15m<-100 && condition.equals("CCI15M")) && cci1m>=-100) exitList.add(id);
                }
                for(String id: exitList) MatchTrader.closePosition(id);
            }

            HashMap<String, Float> profitablePositions = Algo.getProfitablePositions();
            for(Map.Entry<String, Float> map: profitablePositions.entrySet()){
                for (String[] row : csvData) {
                    String orderSide="";
                    if(row[0].equals(map.getKey())) orderSide = row[2];
                    if(map.getValue()>=0.01 && !orderSide.isEmpty()){
                        String[] partialClosedPositions = Filehandler.readFromFile("PartialClose").split(" ");
                        for(String position: partialClosedPositions){
                            if(!position.equals(map.getKey())){
                                MatchTrader.partialClosePosition(map.getKey(),map.getValue(),orderSide,"EURUSD");
                                Filehandler.writeToFile("PartialClose", Filehandler.readFromFile("PartialClose").concat(" ").concat(position));
                            }
                        }
                    }
                }
            }

            float LTP= Algo.LTP(pair);
            mailEntry=Algo.alerts(pair, LTP, mailEntry);
            
            System.out.println();

            System.out.printf("\n%s: %s\n4H: %s | 15M: %s | 5M: %s | 1M: %s\n",pair,LTP,cci4h,cci15m,cci5m,cci1m);

            // if(cci1m>=-100 && cci1m<=100){
            //     if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU1").split("")[2].equals("0")) Filehandler.writeToFile("EU1", Filehandler.readFromFile("EU1").concat("0").substring(1, 4));
            // }
            // else if(cci1m<-100){
            //     if(pair.equals("EURUSD")){
            //         if(cci1m<Float.parseFloat(Filehandler.readFromFile("CCI1EU"))) Filehandler.writeToFile("CCI1EU", String.valueOf(cci1m));
            //         if(!Filehandler.readFromFile("EU1").split("")[2].equals("1")) Filehandler.writeToFile("EU1", Filehandler.readFromFile("EU1").concat("1").substring(1, 4));
            //     }
            // }
            // else if(cci1m>100){
            //     if(pair.equals("EURUSD")){
            //         if(cci1m>Float.parseFloat(Filehandler.readFromFile("CCI1EU"))) Filehandler.writeToFile("CCI1EU", String.valueOf(cci1m));
            //         if(!Filehandler.readFromFile("EU1").split("")[2].equals("2")) Filehandler.writeToFile("EU1", Filehandler.readFromFile("EU1").concat("2").substring(1, 4));
            //     }
            // }

            // if(cci5m>=-100 && cci5m<=100){
            //     if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU5").split("")[2].equals("0")) Filehandler.writeToFile("EU5", Filehandler.readFromFile("EU5").concat("0").substring(1, 4));
            // }
            // else if(cci5m<-100){
            //     if(pair.equals("EURUSD")){
            //         if(cci5m<Float.parseFloat(Filehandler.readFromFile("CCI5EU"))) Filehandler.writeToFile("CCI5EU", String.valueOf(cci5m));
            //         if(!Filehandler.readFromFile("EU5").split("")[2].equals("1")) Filehandler.writeToFile("EU5", Filehandler.readFromFile("EU5").concat("1").substring(1, 4));
            //     }
            // }
            // else if(cci5m>100){
            //     if(pair.equals("EURUSD")){
            //         if(cci5m>Float.parseFloat(Filehandler.readFromFile("CCI5EU"))) Filehandler.writeToFile("CCI5EU", String.valueOf(cci5m));
            //         if(!Filehandler.readFromFile("EU5").split("")[2].equals("2")) Filehandler.writeToFile("EU5", Filehandler.readFromFile("EU5").concat("2").substring(1, 4));
            //     }
            // }

            // if(cci15m>=-100 && cci15m<=100){
            //     if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU15").split("")[2].equals("0")) Filehandler.writeToFile("EU15", Filehandler.readFromFile("EU15").concat("0").substring(1, 4));
            // }
            // else if(cci15m<-100){
            //     if(pair.equals("EURUSD")){
            //         if(cci15m<Float.parseFloat(Filehandler.readFromFile("CCI15EU"))) Filehandler.writeToFile("CCI15EU", String.valueOf(cci15m));
            //         if(!Filehandler.readFromFile("EU15").split("")[2].equals("1")) Filehandler.writeToFile("EU15", Filehandler.readFromFile("EU15").concat("1").substring(1, 4));
            //     }
            // }
            // else if(cci15m>100){
            //     if(pair.equals("EURUSD")){
            //         if(cci15m>Float.parseFloat(Filehandler.readFromFile("CCI15EU"))) Filehandler.writeToFile("CCI15EU", String.valueOf(cci15m));
            //         if(!Filehandler.readFromFile("EU15").split("")[2].equals("2")) Filehandler.writeToFile("EU15", Filehandler.readFromFile("EU15").concat("2").substring(1, 4));
            //     }
            // }

            // if(cci4h>=-100 && cci4h<=100){
            //     if(pair.equals("EURUSD") && !Filehandler.readFromFile("EU4H").split("")[2].equals("0")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("0").substring(1, 4));
            // }
            // else if(cci4h<-100){
            //     if(pair.equals("EURUSD")){
            //         if(cci4h<Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))) Filehandler.writeToFile("CCI4HEU", String.valueOf(cci4h));
            //         if(!Filehandler.readFromFile("EU4H").split("")[2].equals("1")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("1").substring(1, 4));
            //     }
            // }
            // else if(cci4h>100){
            //     if(pair.equals("EURUSD")){
            //         if(cci4h>Float.parseFloat(Filehandler.readFromFile("CCI4HEU"))) Filehandler.writeToFile("CCI4HEU", String.valueOf(cci4h));
            //         if(!Filehandler.readFromFile("EU4H").split("")[2].equals("2")) Filehandler.writeToFile("EU4H", Filehandler.readFromFile("EU4H").concat("2").substring(1, 4));
            //     }
            // }

            Date time=new Date();
            String timeStr=time.toString();
            timeStr=timeStr.split(" ")[1]+timeStr.split(" ")[2]+" "+timeStr.split(" ")[3];

            int trend1=0, trend5=0, trend15=0, trend4h=0, trend1d, trend1w;

            if(pair.startsWith("E")){
                // trend1 = Filehandler.readFromFile("EU1").equals("102") || Filehandler.readFromFile("EU1").equals("202") || Filehandler.readFromFile("EU1").equals("010")? 1:Filehandler.readFromFile("EU1").equals("201") || Filehandler.readFromFile("EU1").equals("101") || Filehandler.readFromFile("EU1").equals("020")?-1:0;
                // trend5 = Filehandler.readFromFile("EU5").equals("102") || Filehandler.readFromFile("EU5").equals("202") || Filehandler.readFromFile("EU5").equals("010")? 1:Filehandler.readFromFile("EU5").equals("201") || Filehandler.readFromFile("EU5").equals("101") || Filehandler.readFromFile("EU5").equals("020")?-1:0;
                // trend15 = Filehandler.readFromFile("EU15").equals("102") || Filehandler.readFromFile("EU15").equals("202") || Filehandler.readFromFile("EU15").equals("010")? 1:Filehandler.readFromFile("EU15").equals("201") || Filehandler.readFromFile("EU15").equals("101") || Filehandler.readFromFile("EU15").equals("020")?-1:0;
                // trend4h = Filehandler.readFromFile("EU4H").equals("102") || Filehandler.readFromFile("EU4H").equals("202") || Filehandler.readFromFile("EU4H").equals("010")? 1:Filehandler.readFromFile("EU4H").equals("201") || Filehandler.readFromFile("EU4H").equals("101") || Filehandler.readFromFile("EU4H").equals("020")?-1:0;
                // System.out.print(trend4h==1?" 4H: 🟢 ":trend4h==-1?" 4H: 🔴 ":" - ");
                // System.out.print(trend15==1?" 15M: 🟢 ":trend15==-1?" 15M: 🔴 ":" - ");
                // System.out.print(trend5==1?" 5M: 🟢 ":trend5==-1?" 5M: 🔴 ":" - ");
                
                String timeNow=MatchTrader.getSecondLastCandleTime();
                // if(trend1==-1 && cci1m<=-100){
                //     if(Filehandler.readFromFile("LST").equals("NULL") && Filehandler.readFromFile("LST-F").equals("NULL")) Filehandler.writeToFile("LST",timeNow);
                //     if(!Filehandler.readFromFile("LST").equals("NULL")){
                //         Filehandler.writeToFile("LST-F",Filehandler.readFromFile("LST"));
                //         Filehandler.writeToFile("LST","NULL");
                //     }
                //     String high=MatchTrader.getHL(Filehandler.readFromFile("HST-F"), timeNow, "high");
                //     String[] highs=Filehandler.readFromFile("H").split(" ");
                //     if(!high.equals("NULL") && !high.equals(highs[highs.length-1])) Filehandler.writeToFile("H", Filehandler.readFromFile("H").concat(" ").concat(high));
                // }
                // if(trend1==1 && cci1m>=100){
                //     if(Filehandler.readFromFile("HST").equals("NULL") && Filehandler.readFromFile("HST-F").equals("NULL")) Filehandler.writeToFile("HST",timeNow);
                //     if(!Filehandler.readFromFile("HST").equals("NULL")){
                //         Filehandler.writeToFile("HST-F",Filehandler.readFromFile("HST"));
                //         Filehandler.writeToFile("HST","NULL");
                //     }
                //     String low=MatchTrader.getHL(Filehandler.readFromFile("LST-F"), timeNow, "low");
                //     String[] lows=Filehandler.readFromFile("L").split(" ");
                //     if(!low.equals("NULL") && !low.equals(lows[lows.length-1])) Filehandler.writeToFile("L", Filehandler.readFromFile("L").concat(" ").concat(low));
                //     Filehandler.writeToFile("LST-F","NULL");
                // }

                String condition;

                List<Double> openPrices = MatchTrader.getOpenPrices();
                String[] highs=Filehandler.readFromFile("H").split(" ");
                String[] lows=Filehandler.readFromFile("L").split(" ");

                float pivotHigh=Float.parseFloat(highs[highs.length-1]);
                float pivotLow=Float.parseFloat(lows[lows.length-1]);
                float pivotMid=(pivotHigh+pivotLow)/2;

                System.out.println();
                System.out.println();
                System.out.println("PIVOT HIGH : "+pivotHigh);
                System.out.println("PIVOT MID : "+pivotMid);
                System.out.println("PIVOT LOW : "+pivotLow);

                // if(trend4h==1 && (cci1m<=-100 || (trend1==1 && cci1m<100)) && ((trend15==1 && cci15m<=100) || (cci4h<=100 && trend15==-1 && cci15m>=-100))){
                //     condition="💚 BUY";
                //     System.out.println("\n"+pair+" "+condition);
                //     set.add(pair+" "+condition);
                //     Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                //     Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("LST-F"), timeNow, "BUY"));
                //     if(LTP>pivotLow && entryPrice<=pivotMid){
                //         float sl=pivotLow;
                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP<=entryPrice){
                //                 float pipAtRisk = (LTP-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (entryPrice-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //         }
                //     };
                //     if(entryPrice<pivotLow){
                //         float sl=Algo.findSL(LTP, highs, lows, "BUY");
                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP<=entryPrice){
                //                 float pipAtRisk = (LTP-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //             else{
                //                 float pipAtRisk = (entryPrice-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //         }
                //     }
                // }

                // if(trend4h==-1 && (cci1m>=100 || (trend1==-1 && cci1m>-100)) && ((trend15==-1 && cci15m>=-100) || (cci4h>=-100 && trend15==1 && cci15m<=100))){
                //     condition="🔥 SELL";
                //     System.out.println("\n"+pair+" "+condition);
                //     set.add(pair+" "+condition);
                //     Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                //     Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("LST-F"), timeNow, "BUY"));
                //     if(LTP<pivotHigh && entryPrice>=pivotMid){
                //         float sl=pivotHigh;
                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP>=entryPrice){
                //                 float pipAtRisk = (sl-LTP)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (sl-entryPrice)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                //             }
                //         }
                //     };
                //     if(entryPrice<pivotLow){
                //         float sl=Algo.findSL(LTP, highs, lows, "SELL");
                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP<=entryPrice){
                //                 float pipAtRisk = (LTP-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (sl-entryPrice)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);

                //             }
                //         }
                //     }
                // }
            
                if(trend4h==1 && cci15m>-100 && cci15m<0 && trend1==1 && cci1m<100){
                    condition="💚 BUY";
                    System.out.println("\n"+pair+" "+condition);
                    set.add(pair+" "+condition);
                    Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                    Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("LST-F"), timeNow, "BUY"));
                    System.out.println();
                    System.out.println("ENTRY PRICE "+entryPrice);
                    
                    if(entryPrice>pivotLow && entryPrice<=pivotMid){
                        float sl=pivotLow;
                        System.out.println("LTP > PIVOT LOW, SL: "+sl);

                        if(!Algo.positionAlreadyExists(openPrices, sl)){
                            if(LTP<entryPrice){
                                float pipAtRisk = (LTP-sl)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                                if(!positionId.equals("TRADE_FAILED")) Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                            }else{
                                float pipAtRisk = (entryPrice-sl)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, entryPrice, "BUY", sl);
                                if(!positionId.equals("TRADE_FAILED")) Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                            }
                        }
                    };
                    if(entryPrice<pivotLow){
                        float sl=Algo.findSL(LTP, highs, lows, "BUY");
                        System.out.println("LTP < PIVOT LOW, SL: "+sl);

                        if(!Algo.positionAlreadyExists(openPrices, sl)){
                            if(LTP<entryPrice){
                                float pipAtRisk = (LTP-sl)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                                Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                            }
                            else{
                                float pipAtRisk = (entryPrice-sl)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, entryPrice, "BUY", sl);
                                Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                            }
                        }
                    }
                }

                if(trend4h==-1 && cci15m<100 && cci15m>0 && trend1==-1 && cci1m>-100){
                    condition="🔥 SELL";
                    System.out.println("\n"+pair+" "+condition);
                    set.add(pair+" "+condition);
                    Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                    Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("HST-F"), timeNow, "SELL"));
                    System.out.println();
                    System.out.println("ENTRY PRICE "+entryPrice);

                    if(entryPrice<pivotHigh && entryPrice>=pivotMid){
                        float sl=pivotHigh;
                        System.out.println("LTP < PIVOT HIGH, SL: "+sl);

                        if(!Algo.positionAlreadyExists(openPrices, sl)){
                            if(LTP>=entryPrice){
                                float pipAtRisk = (sl-LTP)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 LTP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, LTP, "SELL", sl);
                                Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                            }else{
                                float pipAtRisk = (sl-entryPrice)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, entryPrice, "SELL", sl);
                                Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                            }
                        }
                    };
                    if(entryPrice>pivotHigh){
                        float sl=Algo.findSL(LTP, highs, lows, "SELL");
                        System.out.println("LTP > PIVOT HIGH, SL: "+sl);

                        if(!Algo.positionAlreadyExists(openPrices, sl)){
                            if(LTP<entryPrice){
                                float pipAtRisk = (sl-LTP)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 LTP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                                Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                            }else{
                                float pipAtRisk = (sl-entryPrice)*10000;
                                float volume = Algo.calculatePositionSize(pair, 20, pipAtRisk);
                                System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                                String positionId=MatchTrader.trade(pair, volume, entryPrice, "SELL", sl);
                                Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println();
        // System.out.print("\n🚀 TARGETs Achieved: ");
        Iterator setVals = set.iterator(); 
        String targetVals="";
        while (setVals.hasNext()) { 
            targetVals=targetVals.concat(setVals.next()+" ");
        }

        if(!targetVals.isEmpty() && !targetVals.trim().equals(Filehandler.readFromFile("mailMsg").trim())){
            Filehandler.writeToFile("mailMsg", targetVals.trim());
            Date date=new Date();
            Filehandler.writeToFronttestCSV("pair", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, date.toString(), targetVals.trim(), true);
            System.out.println();
            Email.sendEmail(targetVals.trim());
        }

        System.out.println();
        System.out.println(" ------------------------------------------ ");
    }
}