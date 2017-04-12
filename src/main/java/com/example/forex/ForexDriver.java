package com.example.forex; /**
 * Created by admin on 17/10/2016.
 */

import com.example.entities.CurrencyPair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForexDriver {

    public ForexDriver()throws Exception{
        getCurrencyPairRates();
    }

    static final String USERNAME = "c13731869";
    static final String PASSWORD = "root";
    public static ArrayList<CurrencyPair> currencyPairs = new ArrayList<CurrencyPair>();
    public static ArrayList<TrueFxResponse> rawResponseList = new ArrayList<TrueFxResponse>();


    private static int playerAPairIndex;
    private static double amount;

    private String getAuthCode() throws Exception {

        String username;
        String password;
//        String url = "http://webrates.truefx.com/rates/connect.html?u="+USERNAME+"&p="+PASSWORD+"&q=eurates&c=EUR/USD,USD/JPY,"+
//                "GBP/USD,EUR/GBP,USD/CHF,AUD/USD,AUD/NZD,EUR/AUD,EUR/NZD,NZD/USD,USD/NOK,USD/SEK,USD/CAD&f=xml\n";
        String url = "http://webrates.truefx.com/rates/connect.html?u="+USERNAME+"&p="+PASSWORD+"&q=eurates&c=EUR/USD,USD/JPY,"+
                "GBP/USD,USD/CHF,AUD/USD,NZD/USD,USD/NOK,USD/SEK,USD/CAD&f=xml\n";
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public void getCurrencyPairRates()throws Exception{

        Document doc;
        String authCode = getAuthCode();

        doc = Jsoup.connect("http://webrates.truefx.com/rates/connect.html?f=html&id="+authCode).get();
        Elements rows = doc.select("tr");

        for (Element aRow : rows) {
            String[] splitArray = null;
            splitArray = aRow.text().split("\\s+");
            parseResponse(splitArray);
        }
    }


    public void parseResponse(String[] splitArray){
        TrueFxResponse aResponse = new TrueFxResponse();

        for(int i = 0; i < splitArray.length;i++){
            switch (i){
                case 0: aResponse.setTd1(splitArray[0]);
                    break;
                case 1: aResponse.setTd2(splitArray[1]);
                    break;
                case 2: aResponse.setTd3(splitArray[2]);
                    break;
                case 3: aResponse.setTd4(splitArray[3]);
                    break;
                case 4: aResponse.setTd5(splitArray[4]);
                    break;
                case 5: aResponse.setTd6(splitArray[5]);
                    break;
                case 6: aResponse.setTd7(splitArray[6]);
                    break;
                case 7: aResponse.setTd8(splitArray[7]);
                    break;
                case 8: aResponse.setTd9(splitArray[8]);
                    break;
            }
        }
        rawResponseList.add(aResponse);
    }

    public void makeCurrencyPairs(ArrayList<TrueFxResponse> responseList){

        for(TrueFxResponse res : responseList){

            CurrencyPair aPair = new CurrencyPair();

            String bid = res.getTd3()+res.getTd4();
            double bidNumeric = Double.parseDouble(bid);
            String ask = res.getTd5()+res.getTd6();
            double askNumeric = Double.parseDouble(ask);
            long milliseconds = Long.parseLong(res.getTd2());
            double low = Double.parseDouble(res.getTd7());
            double high = Double.parseDouble(res.getTd8());
            double open = Double.parseDouble(res.getTd9());

            aPair.setSymbols(res.getTd1());
            aPair.setMilliseconds(milliseconds);
            aPair.setBid(bidNumeric);
            aPair.setAsk(askNumeric);
            aPair.setHigh(high);
            aPair.setLow(low);
            aPair.setOpen(open);

            double spread = 0;
            DecimalFormat df = new DecimalFormat("#0.0");

            if(res.getTd1().contains("JPY")){
                spread = ((askNumeric - bidNumeric)*100);
            }else{
                spread = ((askNumeric - bidNumeric)*3000);
            }

            aPair.setSpreadPips(Double.parseDouble(df.format(spread)));

            SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            aPair.setTimeStampString(sdf.format(timestamp).toString());

            currencyPairs.add(aPair);
        }
    }

    public static void main(String[] args)throws Exception {
        ForexDriver tester = new ForexDriver();
        tester.makeCurrencyPairs(tester.rawResponseList);
    }


    public boolean validatePairSelection(String pair){
        boolean found = false;
        for(int i = 0;i<currencyPairs.size();i++){
            if(pair.equalsIgnoreCase(currencyPairs.get(i).getSymbols())){
                found = true;
                playerAPairIndex=i;
                found = true;
            }
        }
        return found;
    }

    public static ArrayList<CurrencyPair> getCurrencyPairs() {
        return currencyPairs;
    }
}
