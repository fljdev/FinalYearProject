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
import java.text.DecimalFormat;
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
    public static ArrayList<CurrencyPairSet> currencyPairSets = new ArrayList<CurrencyPairSet>();


//    private Player playerA;
//    private Player playerB;
    private static int playerAPairIndex;
    private static double amount;


    // HTTP GET request
    private String getAuthCode() throws Exception {

        String username;
        String password;

        String url = "http://webrates.truefx.com/rates/connect.html?u="+USERNAME+"&p="+PASSWORD+"&q=eurates&c=EUR/USD,USD/JPY,"+
        "GBP/USD,EUR/GBP,USD/CHF,EUR/JPY,EUR/CHF,USD/CAD,AUD/USD,GBP/JPY,AUD/CAD,AUD/CHF,AUD/JPY,AUD/NZD,CAD/CHF,"+
        "CAD/JPY,CHF/JPY,EUR/AUD,EUR/CAD,EUR/NOK,EUR/NZD,GBP/CAD,GBP/CHF,NZD/JPY,NZD/USD,USD/NOK,USD/SEK&f=xml\n";
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
    }//end getAuthCode

    public void getCurrencyPairRates()throws Exception{

        Document doc;
        String authCode = getAuthCode();

        doc = Jsoup.connect("http://webrates.truefx.com/rates/connect.html?f=html&id="+authCode).get();
        Elements rows = doc.select("tr");

        for (Element aRow : rows) {

//            System.out.println("a row looks like "+aRow);
            String[] splitArray = null;
            splitArray = aRow.text().split("\\s+");
            parseResponse(splitArray);

        }//end for
    }//end getCurrencyPairs()


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
    }//end parseResponse()

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
                spread = ((askNumeric - bidNumeric)*10000);


            }

            aPair.setSpreadPips(Double.parseDouble(df.format(spread)));


//            if()

//            System.out.println("Pairs are "+aPair.getSymbols()+" added to the currencyPair []");
//            System.out.println(aPair.toString());

            currencyPairs.add(aPair);



        }

    }

    public static void main(String[] args)throws Exception {

        ForexDriver tester = new ForexDriver();
        tester.makeCurrencyPairs(tester.rawResponseList);
//        tester.createBackupFiles();
    }


    public void makeCurrencyPairSets(CurrencyPair cp){
        CurrencyPairSet cps = new CurrencyPairSet();

        CurrencyPair aPair = cp;
        makeCurrencyPairSets(aPair);

        if(aPair.getSymbols().equals("EUR/USD")){
            cps.setEURUSD(aPair);
        }
        if(aPair.getSymbols().equals("USD/JPY")){
            cps.setUSDJPY(aPair);
        }
        if(aPair.getSymbols().equals("GBP/USD")){
            cps.setGBPUSD(aPair);
        }
        if(aPair.getSymbols().equals("EUR/GBP")){
            cps.setEURGBP(aPair);
        }
        if(aPair.getSymbols().equals("USD/CHF")){
            cps.setUSDCHF(aPair);
        }
        if(aPair.getSymbols().equals("EUR/JPY")){
            cps.setEURJPY(aPair);
        }
        if(aPair.getSymbols().equals("EUR/CHF")){
            cps.setEURCHF(aPair);
        }
        if(aPair.getSymbols().equals("USD/CAD")){
            cps.setUSDCAD(aPair);
        }
        if(aPair.getSymbols().equals("AUD/USD")){
            cps.setAUDUSD(aPair);
        }
        if(aPair.getSymbols().equals("GBP/JPY")){
            cps.setGBPJPY(aPair);
        }
        if(aPair.getSymbols().equals("AUD/CAD")){
            cps.setAUDCAD(aPair);
        }
        if(aPair.getSymbols().equals("AUD/CHF")){
            cps.setAUDCHF(aPair);
        }
        if(aPair.getSymbols().equals("AUD/JPY")){
            cps.setAUDJPY(aPair);
        }
        if(aPair.getSymbols().equals("AUD/NZD")){
            cps.setAUDNZD(aPair);
        }
        if(aPair.getSymbols().equals("CAD/CHF")){
            cps.setCADCHF(aPair);
        }
        if(aPair.getSymbols().equals("CAD/JPY")){
            cps.setCADJPY(aPair);
        }
        if(aPair.getSymbols().equals("CHF/JPY")){
            cps.setCHFJPY(aPair);
        }
        if(aPair.getSymbols().equals("EUR/AUD")){
            cps.setEURAUD(aPair);
        }
        if(aPair.getSymbols().equals("EUR/CAD")){
            cps.setEURCAD(aPair);
        }
        if(aPair.getSymbols().equals("EUR/NOK")){
            cps.setEURNOK(aPair);
        }
        if(aPair.getSymbols().equals("EUR/NZD")){
            cps.setEURNZD(aPair);
        }
        if(aPair.getSymbols().equals("GBP/CAD")){
            cps.setGBPCAD(aPair);
        }
        if(aPair.getSymbols().equals("GBP/CHF")){
            cps.setGBPCHF(aPair);
        }
        if(aPair.getSymbols().equals("NZD/JPY")){
            cps.setNZDJPY(aPair);
        }
        if(aPair.getSymbols().equals("NZD/USD")){
            cps.setNZDUSD(aPair);
        }
        if(aPair.getSymbols().equals("USD/NOK")){
            cps.setUSDNOK(aPair);
        }
        if(aPair.getSymbols().equals("USD/SEK")){
            cps.setUSDSEK(aPair);
        }

        System.out.println(cps.toString());
        currencyPairSets.add(cps);
        System.out.println("CurrencyPairSet object arraylist size : "+currencyPairSets.size());
    }

public void createBackupFiles()throws Exception{
    for(int i = 0;i<10800;i++){

        //We can get current date using default constructor
        Date currentDate = new Date();
        //toString would print the full date time string
        String stamp = (currentDate.toString());

        ForexDriver forexDriver = new ForexDriver();
        forexDriver.makeCurrencyPairs(forexDriver.rawResponseList);

        PrintWriter wr = new PrintWriter("/Users/admin/Documents/College/4th_Year/FYP/CurrencyQuotes/quotes/"+stamp+".txt","UTF-8");

        Thread.sleep(1000);

        for(CurrencyPairSet set : forexDriver.currencyPairSets){
            ObjectMapper om = new ObjectMapper();
            String x = om.writeValueAsString(set);
            wr.println(x);
//            wr.println(set.toString());
        }
//        for(CurrencyPair cp : forexDriver.currencyPairs){
//
//            ObjectMapper om = new ObjectMapper();
//
//            System.out.println("this json give ");
//            String x = om.writeValueAsString(cp);
//            System.out.println(x);
//
////          wr.println(cp.toString());
////            System.out.println(cp.toString()+"\n");
//
//        }
        wr.close();
    }
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

//    public String playerAChoosesPair(){
//        Scanner scan = new Scanner(System.in);
//
//        System.out.println("Pick a pair");
//        String playerAPair = scan.nextLine();
//
//
//        do{
//            if(validatePairSelection(playerAPair)){
//                System.out.println("You chose "+playerAPair);
//
//                CurrencyPair playerACurrencyPair = new CurrencyPair();
//                playerACurrencyPair.setSymbols(playerAPair);
//                playerA.setPlayersPair(playerACurrencyPair);
//                return playerAPair;
//
//            }else{
//                System.out.println("You chose an invalid pair, retry");
//                playerAPair=scan.nextLine();
//            }
//        }while(!validatePairSelection(playerAPair));
//
//        return playerAPair;
//    }
//
//
//    public void setUpGame(){
//        Scanner scan = new Scanner(System.in);
//        playerA = new Player("Johnny",1000000);
//        playerB = new Player("Samantha",1000000);
//
//        // playerAChoosesPair();
//        String aPair = playerAChoosesPair();
//        for(CurrencyPair cp : currencyPairs){
//            if(aPair.equalsIgnoreCase(cp.getSymbols())){
//                playerA.setPlayersPair(cp);
//            }
//        }
//
//
//        System.out.println("Do you want to take a long or short position?");
//        String position = scan.nextLine();
//        System.out.println("Ok, how much do you want to play with?");
//        amount = scan.nextDouble();
//        scan.nextLine();
//        System.out.println("Ok , that's "+amount+ ","+position +" on "+playerA.getPlayersPair().getSymbols().toUpperCase());
//        /**
//         * Now player A has a pair,  i need to generate a random one for player b
//         */
//        int x = generateBPair(playerAPairIndex);
//        playerB.setPlayersPair(currencyPairs.get(x));
//
//        playGame(playerA,playerB);
//    }
//
//    public void playGame(Player a, Player b){
//
//        Game aGame = new Game();
//        aGame.setPlayerA(playerA);
//        aGame.setPlayerB(playerB);
//        aGame.setPlayerACurrency(playerA.getPlayersPair());
//        aGame.setPlayerBCurrency(playerB.getPlayersPair());
//        aGame.setStake(amount);
//
//        playerA.setBalance(playerA.getBalance()-amount);
//        playerB.setBalance(playerB.getBalance()-amount);
//
//    }
//
//    public int generateBPair(int playerAPairIndex){
//        int bPair;
//        do{
//            Random random = new Random();
//            int max = 10;
//            int min = 1;
//             bPair = random.nextInt(max - min + 1) + min;
//        }while(playerAPairIndex ==bPair );
//
//            return bPair;
//    }


    public static ArrayList<CurrencyPair> getCurrencyPairs() {
        return currencyPairs;
    }


}
