package com.java;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    // make db login vals 0
    public static void main(String[] args) throws Exception { 
        // MatchTrader.trade("EURUSD", 0.01f, 0, "SELL", 1.04932);
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
        String[] pairs={"NZDCHF","GBPNZD","USDMXN","NZDCAD","EURUSD","GBPUSD","USDJPY","USDCHF","USDCAD","USDHKD",
        "OANDA:US30USD","TVC:SILVER","SP:SPX","OANDA:AU200AUD"};
        String mailEntry="";

        for(String pair : pairs){
            // float ema5m= Algo.getEMA(pair, "|5");
            // float ema15m= Algo.getEMA(pair, "|15");
            // float ema4h= Algo.getEMA(pair, "|240");
            float cci1m= Algo.getCCI(pair, "|1");
            // float cci5m= Algo.getCCI(pair, "|5");
            float cci15m= Algo.getCCI(pair, "|15");
            float cci4h= Algo.getCCI(pair, "|240");
    
                // List<String[]> csvData = Filehandler.readFromOpenPositions();
                // if(((cci4h>100 || cci15m>100) && cci1m<=100) || ((cci4h<-100 || cci15m<-100) && cci1m>=-100)){
                //     List<String> exitList=new ArrayList<>();
                //     for (String[] row : csvData) {
                //         String id = row[0];
                //         String condition = row[1];
                //         String side = row[2];
                //         if (side.equals("BUY") && (cci4h>100 && condition.equals("CCI4H") || cci15m>100 && condition.equals("CCI15M")) && cci1m<=100) exitList.add(id);
                //         if (side.equals("SELL") && (cci4h<-100 && condition.equals("CCI4H") || cci15m<-100 && condition.equals("CCI15M")) && cci1m>=-100) exitList.add(id);
                //     }
                //     for(String id: exitList) MatchTrader.closePosition(id);
                // }
    
                // HashMap<String, Float> profitablePositions = Algo.getProfitablePositions();
                // for(Map.Entry<String, Float> map: profitablePositions.entrySet()){
                //     for (String[] row : csvData) {
                //         String orderSide="";
                //         if(row[0].equals(map.getKey())) orderSide = row[2];
                //         if(map.getValue()>=0.01 && !orderSide.isEmpty()){
                //             String[] partialClosedPositions = Filehandler.readFromFile("PartialClose").split(" ");
                //             for(String position: partialClosedPositions){
                //                 if(!position.equals(map.getKey())){
                //                     MatchTrader.partialClosePosition(map.getKey(),map.getValue(),orderSide,"EURUSD");
                //                     Filehandler.writeToFile("PartialClose", Filehandler.readFromFile("PartialClose").concat(" ").concat(position));
                //                 }
                //             }
                //         }
                //     }
                // }
    
            float LTP= Algo.LTP(pair);
            mailEntry=Algo.alerts(pair, LTP, mailEntry);
            
            System.out.println();
            
            System.out.printf("\n%s: %s\n15M: %s | 1M: %s",pair,LTP,cci15m,cci1m);
            // System.out.printf("\n%s: %s\n15M: %s | 5M: %s | 1M: %s\n4H: %s | 15M: %s | 5M: %s ",pair,LTP,cci15m,cci5m,cci1m,ema4h,ema15m,ema5m);
            
            Date time=new Date();
            String timeStr=time.toString();
            timeStr=timeStr.split(" ")[1]+timeStr.split(" ")[2]+" "+timeStr.split(" ")[3];
            
            // String cciPair=pair.contains(":")? pair.split(":")[1]+"1M" : pair+"1M";
            // if(cci1m>=100 && !Filehandler.readFromFile(cciPair).endsWith("2")) Filehandler.writeToFile(cciPair, Filehandler.readFromFile(cciPair).concat("2").substring(1, 4));
            // else if(cci1m<=-100 && !Filehandler.readFromFile(cciPair).endsWith("1")) Filehandler.writeToFile(cciPair, Filehandler.readFromFile(cciPair).concat("1").substring(1, 4));
            // else if(cci1m>-100 && cci1m<100 && !Filehandler.readFromFile(cciPair).endsWith("0")) Filehandler.writeToFile(cciPair, Filehandler.readFromFile(cciPair).concat("0").substring(1, 4));
            
            // int trend = Filehandler.readFromFile(cciPair).equals("102") || Filehandler.readFromFile(cciPair).equals("202") || Filehandler.readFromFile(cciPair).equals("010")? 1:Filehandler.readFromFile(cciPair).equals(cciPair) || Filehandler.readFromFile(cciPair).equals("101") || Filehandler.readFromFile(cciPair).equals("020")?-1:0;

            String condition;
            // #----------
            if((cci15m>=100 && cci1m<-100 && cci4h>-100) || (cci15m<=-100 && cci1m>100 && cci4h<100)){
                String str = Algo.alert(pair,LTP);
                if(!str.equals("")) set.add(str);
                if(cci15m>=100 && cci1m<-100 && cci4h>-100) set.add(pair+ ": 🟢");
                if(cci15m<=-100 && cci1m>100 && cci4h<100) set.add(pair+ ": 🔴");
            }
            // if((cci15m>=100 && trend==1) || (cci15m<=-100 && trend==-1)){
            //     String str = Algo.alert(pair,LTP);
            //     if(!str.equals("")) set.add(str);
            //     if(cci15m>=100 && trend==1) set.add(pair+ ": 🟢");
            //     if(cci15m<=-100 && trend==-1) set.add(pair+ ": 🔴");
            // }

            // if((cci15m>=100 && cci1m<-100) || (cci15m<=-100 && cci1m>100)){
            //     String str = Algo.alert(pair,LTP);
            //     if(!str.equals("")) set.add(str);
            // }

        //     if((LTP>ema4h && LTP<=ema5m && cci15m<=-100 && cci1m<=-100) || (LTP<ema4h && LTP>=ema5m && cci15m>=100 && cci1m>=100)){
        //         List<String[]> trades=PendingTrades.readFromPreTrades();
        //         if(!trades.isEmpty()){
        //             List<String[]> updatedTrades = new ArrayList<>();
        //             for(String[] trade: trades){
        //                 if(trade[0].equalsIgnoreCase(pair)){
        //                     Float sl=Float.parseFloat(trade[2]);
        //                     if((LTP<=ema5m && cci15m<=-100 && cci1m<=-100) && LTP<=Float.parseFloat(trade[1]) && LTP>sl && !PendingTrades.positionAlreadyExists(MatchTrader.getOpenPrices(pair), LTP)){
        //                         set.add(pair+"💚 BUY: ⚠️ ALERT");
        //                         float pipAtRisk = (LTP-sl)*10000;
        //                         float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
        //                         String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
        //                         Filehandler.writeToOpenPositions(positionId,"","BUY",volume,0,true);
        //                     }
        //                     else if((LTP>=ema5m && cci15m>=100 && cci1m>=100) && LTP>=Float.parseFloat(trade[1]) && LTP<sl && !PendingTrades.positionAlreadyExists(MatchTrader.getOpenPrices(pair), LTP)){
        //                         set.add(pair+"🔥 SELL: ⚠️ ALERT");
        //                         float pipAtRisk = (sl-LTP)*10000;
        //                         float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
        //                         String positionId=MatchTrader.trade(pair, volume, LTP, "SELL", sl);
        //                         Filehandler.writeToOpenPositions(positionId,"","SELL",volume,0,true);
        //                     }
        //                 }
        //                 else updatedTrades.add(trade);
        //             }
        //             PendingTrades.updatePreTradeCSV(updatedTrades);
        //         }
        //     }
        //     // #----------

        //     if(LTP>ema4h && LTP<=ema5m && LTP>=ema15m && cci15m<=-100 && cci1m<=-100){
        //         condition="💚 BUY";
        //         System.out.println("\n"+pair+" "+condition);
        //         set.add(pair+" "+condition);
        //         Filehandler.writeToFronttestCSV(pair, 0.0f, 0.0f, 0.0f, cci15m, LTP, timeStr, condition, true);
                
        //     Float sl=0.0f;
        //     System.out.print("ENTER SL: ");
        //     Scanner sc= new Scanner(System.in);
        //     long startTime = System.currentTimeMillis();
        //     while ((System.currentTimeMillis() - startTime) < 10000 && System.in.available() == 0) Thread.sleep(1000);
        //     if(System.in.available() > 0) sl=sc.nextFloat();

        //     if(sl!=0.0f && sl<LTP && !PendingTrades.positionAlreadyExists(MatchTrader.getOpenPrices(pair), LTP)){
        //         float pipAtRisk = (LTP-sl)*10000;
        //         float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
        //         String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
        //         Filehandler.writeToOpenPositions(positionId,"","BUY",volume,0,true);
        //     }
        // }

        // else if(LTP<ema4h && LTP>=ema5m && LTP<=ema15m && cci15m>=100 && cci1m>=100){
        //     condition="🔥 SELL";
        //     System.out.println("\n"+pair+" "+condition);
        //     set.add(pair+" "+condition);
        //     Filehandler.writeToFronttestCSV(pair, 0.0f, 0.0f, 0.0f, cci15m, LTP, timeStr, condition, true);

        //     Float sl=0.0f;
        //     System.out.print("ENTER SL: ");
        //     Scanner sc= new Scanner(System.in);
        //     long startTime = System.currentTimeMillis();
        //     while ((System.currentTimeMillis() - startTime) < 10000 && System.in.available() == 0) Thread.sleep(1000);
        //     if(System.in.available() > 0) sl=sc.nextFloat();

        //     if(sl!=0.0f && sl>LTP && !PendingTrades.positionAlreadyExists(MatchTrader.getOpenPrices(pair), LTP)){
        //         float pipAtRisk = (sl-LTP)*10000;
        //         float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
        //         String positionId=MatchTrader.trade(pair, volume, LTP, "SELL", sl);
        //         Filehandler.writeToOpenPositions(positionId,"","SELL",volume,0,true);
        //     }
        // }

        //     // #----------

            // int trend1=0, trend5=0, trend15=0, trend4h=0, trend1d, trend1w;

            // if(pair.startsWith("E")){
                // String timeNow=MatchTrader.getSecondLastCandleTime();
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

                // String condition;

                // List<Double> openPrices = MatchTrader.getOpenPrices();
                // String[] highs=Filehandler.readFromFile("H").split(" ");
                // String[] lows=Filehandler.readFromFile("L").split(" ");

                // float pivotHigh=Float.parseFloat(highs[highs.length-1]);
                // float pivotLow=Float.parseFloat(lows[lows.length-1]);
                // float pivotMid=(pivotHigh+pivotLow)/2;

                // System.out.println();
                // System.out.println();
                // System.out.println("PIVOT HIGH : "+pivotHigh);
                // System.out.println("PIVOT MID : "+pivotMid);
                // System.out.println("PIVOT LOW : "+pivotLow);

                // if(LTP<=ema5m && LTP>ema15m && cci15m<=-100 && cci1m<=-100){
                //     condition="💚 BUY";
                //     System.out.println("\n"+pair+" "+condition);
                //     set.add(pair+" "+condition);
                //     Filehandler.writeToFronttestCSV(pair, 0.0f, 0.0f, 0.0f, cci15m, LTP, timeStr, condition, true);
                // }

                // if(LTP>=ema5m && LTP<ema15m && cci15m>=100 && cci1m>=100){
                //     condition="🔥 SELL";
                //     System.out.println("\n"+pair+" "+condition);
                //     set.add(pair+" "+condition);
                //     Filehandler.writeToFronttestCSV(pair, 0.0f, 0.0f, 0.0f, cci15m, LTP, timeStr, condition, true);
                // }

                // if(trend4h==1 && (cci1m<=-100 || (trend1==1 && cci1m<100)) && ((trend15==1 && cci15m<=100) || (cci4h<=100 && trend15==-1 && cci15m>=-100))){
                    // condition="💚 BUY";
                    // System.out.println("\n"+pair+" "+condition);
                    // set.add(pair+" "+condition);
                    // Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                //     Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("LST-F"), timeNow, "BUY"));
                //     if(LTP>pivotLow && entryPrice<=pivotMid){
                //         float sl=pivotLow;
                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP<=entryPrice){
                //                 float pipAtRisk = (LTP-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (entryPrice-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
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
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //             else{
                //                 float pipAtRisk = (entryPrice-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //         }
                //     }
                // }

                // if(trend4h==-1 && (cci1m>=100 || (trend1==-1 && cci1m>-100)) && ((trend15==-1 && cci15m>=-100) || (cci4h>=-100 && trend15==1 && cci15m<=100))){
                    // condition="🔥 SELL";
                    // System.out.println("\n"+pair+" "+condition);
                    // set.add(pair+" "+condition);
                    // Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                //     Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("LST-F"), timeNow, "BUY"));
                //     if(LTP<pivotHigh && entryPrice>=pivotMid){
                //         float sl=pivotHigh;
                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP>=entryPrice){
                //                 float pipAtRisk = (sl-LTP)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (sl-entryPrice)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
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
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (sl-entryPrice)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);

                //             }
                //         }
                //     }
                // }
            
                // if(trend4h==1 && cci15m>-100 && cci15m<0 && trend1==1 && cci1m<100){
                //     condition="💚 BUY";
                //     System.out.println("\n"+pair+" "+condition);
                //     set.add(pair+" "+condition);
                //     Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                //     Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("LST-F"), timeNow, "BUY"));
                //     System.out.println();
                //     System.out.println("ENTRY PRICE "+entryPrice);
                    
                //     if(entryPrice>pivotLow && entryPrice<=pivotMid){
                //         float sl=pivotLow;
                //         System.out.println("LTP > PIVOT LOW, SL: "+sl);

                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP<entryPrice){
                //                 float pipAtRisk = (LTP-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 if(!positionId.equals("TRADE_FAILED")) Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (entryPrice-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "BUY", sl);
                //                 if(!positionId.equals("TRADE_FAILED")) Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //         }
                //     };
                //     if(entryPrice<pivotLow){
                //         float sl=Algo.findSL(LTP, highs, lows, "BUY");
                //         System.out.println("LTP < PIVOT LOW, SL: "+sl);

                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP<entryPrice){
                //                 float pipAtRisk = (LTP-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //             else{
                //                 float pipAtRisk = (entryPrice-sl)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","BUY",volume,0,true);
                //             }
                //         }
                //     }
                // }

                // if(trend4h==-1 && cci15m<100 && cci15m>0 && trend1==-1 && cci1m>-100){
                //     condition="🔥 SELL";
                //     System.out.println("\n"+pair+" "+condition);
                //     set.add(pair+" "+condition);
                //     Filehandler.writeToFronttestCSV(pair, cci1w, cci1d, cci4h, cci15m, LTP, timeStr, condition, true);
                    
                //     Float entryPrice= Float.parseFloat(MatchTrader.getEntryPrice(Filehandler.readFromFile("HST-F"), timeNow, "SELL"));
                //     System.out.println();
                //     System.out.println("ENTRY PRICE "+entryPrice);

                //     if(entryPrice<pivotHigh && entryPrice>=pivotMid){
                //         float sl=pivotHigh;
                //         System.out.println("LTP < PIVOT HIGH, SL: "+sl);

                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP>=entryPrice){
                //                 float pipAtRisk = (sl-LTP)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 LTP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (sl-entryPrice)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                //             }
                //         }
                //     };
                //     if(entryPrice>pivotHigh){
                //         float sl=Algo.findSL(LTP, highs, lows, "SELL");
                //         System.out.println("LTP > PIVOT HIGH, SL: "+sl);

                //         if(!Algo.positionAlreadyExists(openPrices, sl)){
                //             if(LTP<entryPrice){
                //                 float pipAtRisk = (sl-LTP)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 LTP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, LTP, "BUY", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h<-50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                //             }else{
                //                 float pipAtRisk = (sl-entryPrice)*10000;
                //                 float volume = Algo.calculatePositionSize(pair, LTP, 20, pipAtRisk);
                //                 System.out.println("🚨 EP: "+LTP+", "+entryPrice+", "+volume+", "+pipAtRisk);
                //                 String positionId=MatchTrader.trade(pair, volume, entryPrice, "SELL", sl);
                //                 Filehandler.writeToOpenPositions(positionId,(trend4h==trend15 && cci4h>50)?"CCI4H":"CCI15M","SELL",volume,0,true);
                //             }
                //         }
                //     }
                // }
            // }
        }
        
        System.out.println();
        // System.out.print("\n🚀 TARGETs Achieved: ");
        Iterator setVals = set.iterator(); 
        String targetVals="";
        while (setVals.hasNext()) { 
            targetVals=targetVals.concat(setVals.next()+" ");
        }

        if(!targetVals.isEmpty() && !targetVals.trim().contains(Filehandler.readFromFile("mailMsg").trim())){
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