angular.module('myApp.TradeController',['chart.js']).
controller('TradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav,$stateParams){
    /**
     * User user object from the Database, instead of the browser cookie (No Problems)
     */
    $scope.setUser = function(){
        $scope.thisUser = $cookieStore.get('userCookie');
        if($scope.thisUser){
            $http.post('/api/user/findById', JSON.stringify($scope.thisUser.id))
                .success(function (data, status) {
                    if(status = 200){
                        $cookieStore.put('userCookie', data);
                        $scope.thisUser = data;
                        $scope.watchForChanges($scope.thisUser);
                    }
                }).error(function (error) {
                console.log("something went wrong in findById -> TradeController!!");
            });
        }
    };
    $scope.setUser();

    var count=0;

    $scope.watchForChanges = function(thisUser){
        count = count+1;
        console.log("Wach for changes method entered ",count," times");

        $http.post('/api/trade/watchForChanges',JSON.stringify(thisUser))
            .success(function (data, status) {
                if(status = 200){
                    $scope.thisUser=data;
                    $cookieStore.put('userCookie', $scope.thisUser);
                    $scope.updateUserSummaryTable();
                }
            }).error(function (error) {
            console.log("something went wrong in  watchForChanges()!!");
        });
    };
    $interval( function(){ $scope.watchForChanges($scope.thisUser); }, 3500);

    $scope.getPairs = function(){
        $http.get('/api/trade/pairs')
            .success(function (data, status) {
                if(status = 200){
                    $scope.pairs = data;
                    $scope.setGraph1(data);
                }
            }).error(function (error) {
            console.log("something went wrong in the pairs controller init function!!");
        });
    };

    $scope.showOpenTrades = function(){
        $scope.getPairs();
        $http.post('/api/trade/getOpenTrades',$scope.thisUser.id)
            .success(function (data, status) {
                if(status = 200){
                    $scope.openTrades = data;
                    if($scope.openTrades.length>0){
                        $scope.activeTrades = true;
                    }else{
                        $scope.activeTrades = false;
                    }
                }
            }).error(function (error) {
            console.log("something went wrong in getOpenTrades call!!");
        });
    };

    $scope.checkForOpenTrades= function (pair) {
        function findPair(currentPair) {
            return currentPair.currencyPairOpen.symbols === pair.symbols;
        }
        var trade = $scope.openTrades.find(findPair);

        if(trade){
            return trade;
        }
        return false;
    };

    $scope.showOpenTrades();
    $interval( function(){ $scope.showOpenTrades(); }, 3500);



    /**
     * Increment/Decrement function (No Problems with these)
     */
    var max = 5000000;
    var min = 2500;
    $scope.positionUnits = 2500;
    $scope.leverage = 100;
    $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
    $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);


    $scope.increment = function() {

        if ($scope.positionUnits >= max) { return; }
        $scope.positionUnits +=2500;

        $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
        $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);
        /**
         * I will now convert the margin from above to USD (account currency)
         */
        $scope.convertRequiredMarginToUSD($scope.preTradePairChosen.symbols);

    };
    $scope.decrement = function() {

        if ($scope.positionUnits <= min) { return; }
        $scope.positionUnits -=2500;

        /**
         * Based on users chosen positionUnits size, I will calc the amount of margin required
         * in the chosen currency base pair
         */
        $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
        $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);

        /**
         * I will now convert the margin from above to USD (account currency)
         */
        $scope.convertRequiredMarginToUSD($scope.preTradePairChosen.symbols);
    };

    $scope.convertRequiredMarginToUSD = function(sym){
        if(sym.match("EUR/")){
            var symParam = "EUR/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("GBP/")){
            var symParam = "GBP/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("AUD/")){
            var symParam = "AUD/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("NZD/")){
            var symParam = "NZD/USD";
            $scope.getThisConversionPair(symParam);
        }else if(sym.match("USD/")){
            console.log("got USD/");
            $scope.preTradeMarginRequiredUSD = $scope.preTradeMarginRequiredTradedCurrency;
            $scope.preTradeMarginRequiredUSDView = $scope.preTradeMarginRequiredUSD.toFixed(2);
        }
    };

    $scope.getThisConversionPair = function(conversionPair){
        $http.post('/api/trade/getThisPair',conversionPair)
            .success(function (data, status) {
                if(status = 200){
                    $scope.preTradeMarginConversionPairAsk = data.ask;
                    $scope.preTradeMarginRequiredUSD = $scope.preTradeMarginRequiredTradedCurrency *  $scope.preTradeMarginConversionPairAsk;
                    $scope.preTradeMarginRequiredUSDView = $scope.preTradeMarginRequiredUSD.toFixed(2);
                }
            }).error(function (error) {
            console.log("something went wrong in $scope.getThisConversionPair!!");
        });
    };



    /**
     * Check if the game is a solo trade or part of a challenge
     * if part of a challenge, change the balance to the stake
     */
    var theVar = $stateParams.challengeID;
    $scope.currentChallenge={};
    $http.post('/api/challenge/findChallengeById',theVar)
        .success(function (data, status) {
            if(status = 200){
                $scope.currentChallenge = data;
                console.log("current challenge is : ",$scope.currentChallenge);
                $scope.theStake = $scope.currentChallenge.stake;
            }
        }).error(function (error) {
        console.log("something went wrong in  findChallengeByID!!");
    });
    /**
     * INITIAL Top table values ""**BEFORE TRADE**"" (No Problems with these)
     */
    $scope.check =false;
    $scope.updateUserSummaryTable = function(){
        console.log("inti the updateUserSummary methot");

        if(theVar>0){
            $scope.available = $scope.theStake;
            $scope.check = true;
        }else{
            $scope.available = $scope.thisUser.account.balance;
            $scope.check=false;
            $scope.PLV = $scope.thisUser.currentProfit;
            $scope.equity = $scope.available + $scope.PLV + $scope.thisUser.totalMargin;
        }
        // $scope.availableView = $scope.available.toFixed(2);
        // $scope.equity = $scope.available;
        // $scope.equityView = ($scope.equity.toFixed(2));
        $scope.updateEachTrade();

    };


    $scope.setGraph1 = function(data){

        $scope.labels=[];
        $scope.data=[];
        for( i = 0; i < data.length; i++){
            $scope.labels.push(data[i].symbols);
            $scope.data.push(data[i].spreadPips);
        }
    };



    /**
     * All the toggle section is concerned with it the button pressed (buy/sell) and the pair chosen (100% working)
     */
    $scope.toggleLeft = buildToggler('left');
    $scope.toggleRight = buildToggler('right');
    function buildToggler(componentId) {
        return function(x,y) {

            $scope.preTradeAction = y;
            $scope.preTradePairChosen = x;
            $scope.preTradeBasePairChosenSymbols = x.symbols.charAt(0)+x.symbols.charAt(1)+x.symbols.charAt(2);

            $scope.convertRequiredMarginToUSD($scope.preTradePairChosen.symbols);

            $mdSidenav(componentId).toggle();
        };
    }
    $scope.closeToggleLeft = buildTogglerClose('left');
    function buildTogglerClose(componentId) {
        return function() {
            $mdSidenav(componentId).toggle();
        };
    }




    /**
     * TRADE AREA
     * ******************************************************************************************************************************************************************************************************
     * ******************************************************************************************************************************************************************************************************
     * ******************************************************************************************************************************************************************************************************
     * ******************************************************************************************************************************************************************************************************
     */



    $scope.trade = function(){

        $scope.closeToggleLeft();

        var tradeObject = {};
        tradeObject.playerID = $scope.thisUser.id+"";
        tradeObject.pairSymbols=$scope.preTradePairChosen.symbols;
        tradeObject.margin = $scope.preTradeMarginRequiredUSD;
        tradeObject.action = $scope.preTradeAction;
        tradeObject.positionUnits = $scope.positionUnits;

        $http.post('/api/trade/saveTrade',JSON.stringify(tradeObject))
            .success(function (data, status) {
                if(status = 200){
                    swal(data.action  +" " + data.currencyPairOpen.symbols + " "+ data.positionUnits+ " units", " position opened!", "success");
                    $scope.tradeObject = data;
                    $scope.thisUser=$scope.tradeObject.user;
                    $cookieStore.put('userCookie', $scope.thisUser);

                }
            }).error(function (error) {
            console.log("something went wrong in saveTrade");
        });
    };




    $scope.updateEachTrade = function(){
        $http.post('/api/trade/updateEachTrade',JSON.stringify($scope.thisUser))
            .success(function (data, status) {
                if(status = 200){
                    $http.post('/api/trade/getTotalProfitAndLoss',JSON.stringify($scope.thisUser))
                        .success(function (data, status) {
                            if(status = 200){
                                $scope.thisUser = data;
                                $cookieStore.put('userCookie', $scope.thisUser);
                                $scope.PLV = $scope.thisUser.currentProfit;
                                $scope.mMargin = $scope.thisUser.totalMargin;
                            }
                        }).error(function (error) {
                        console.log("something went wrong in updateEachTrade");
                    });
                    $scope.showOpenTrades();
                }
            }).error(function (error) {
            console.log("something went wrong in updateEachTrade");
        });
    };








    $scope.closeLiveTrade = function(x){
        var closeParams = {};
        closeParams.id = $scope.thisUser.id+"";
        closeParams.sym = x.symbols;
        $http.post('/api/trade/closeLiveTrade',JSON.stringify(closeParams))
                .success(function (data, status) {
                    if(status = 200){
                        swal(data.action+ " "+ data.currencyPairOpen.symbols+ " position", "successfully closed", "error");
                        $scope.thisUser = data.user;
                        $cookieStore.put('userCookie', $scope.thisUser);
                        $scope.PLV = $scope.thisUser.currentProfit;
                        $scope.mMargin = $scope.thisUser.totalMargin;
                    }
                }).error(function (error) {
                console.log("something went wrong in closeTrade!!");
            });
    };



    $scope.labels2 = ["January", "February", "March", "April", "May", "June", "July"];
    $scope.series2 = ['Series A', 'Series B'];
    $scope.data2 = [
        [65, 59, 80, 81, 56, 55, 40],
        [28, 48, 40, 19, 86, 27, 90]
    ];
    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };
    $scope.datasetOverride = [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }];
    $scope.options = {
        scales: {
            yAxes: [
                {
                    id: 'y-axis-1',
                    type: 'linear',
                    display: true,
                    position: 'left'
                },
                {
                    id: 'y-axis-2',
                    type: 'linear',
                    display: true,
                    position: 'right'
                }
            ]
        },
        elements: {
            line: {
                tension: 0
            }
        }
    };



});



