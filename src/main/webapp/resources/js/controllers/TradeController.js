angular.module('myApp.TradeController',['chart.js']).
controller('TradeController',function($scope,$http,$state,$cookieStore,$interval,$mdSidenav,$stateParams,$rootScope,$timeout){
    /**
     * User user object from the Database, instead of the browser cookie (No Problems)
     */
    $scope.setUser = function(){
        $rootScope.currentUser = $cookieStore.get('userCookie');
        if($rootScope.currentUser){
            $http.post('/api/user/findById', JSON.stringify($rootScope.currentUser.id))
                .success(function (data, status) {
                    if(status = 200){
                        $cookieStore.put('userCookie', data);
                        $rootScope.currentUser = data;
                        $scope.watchForChanges($rootScope.currentUser);
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
                    $rootScope.currentUser=data;
                    $cookieStore.put('userCookie', $rootScope.currentUser);
                    $scope.updateUserSummaryTable();
                }
            }).error(function (error) {
            console.log("something went wrong in  watchForChanges()!!");
        });
    };

    $scope.watchForChangesInterval = function () {
        var promise = $interval(function () {
            if($state.current.name=="trade") {
                $scope.watchForChanges($rootScope.currentUser);
            }else {
                $interval.cancel(promise);
            }
        }, 5000);

    };

    $scope.watchForChangesInterval();

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

    $scope.showOpenGameTrades = function(){
        $scope.getPairs();
        $http.post('/api/gameTrade/getOpenGameTrades',$rootScope.currentUser.id)
            .success(function (data, status) {
                if(status = 200){
                    $scope.openGameTrades = data;
                    if($scope.openGameTrades.length>0){
                        $scope.activeTrades = true;
                    }else{
                        $scope.activeTrades = false;
                    }
                }
            }).error(function (error) {
            console.log("something went wrong in getOpenTrades call!!");
        });
    };

    $scope.showOpenTrades = function(){
        $scope.getPairs();
        $http.post('/api/trade/getOpenTrades',$rootScope.currentUser.id)
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
    $interval( function(){ $scope.showOpenTrades(); }, 5000);

    $scope.checkForOpenGameTrades= function (pair) {
        function findPair(currentPair) {
            return currentPair.currencyPairOpen.symbols === pair.symbols;
        }
        var trade = $scope.openGameTrades.find(findPair);

        if(trade){
            return trade;
        }
        return false;
    };

    $scope.showOpenGameTrades();
    $interval( function(){ $scope.showOpenGameTrades(); }, 5000);


    /**
     * Increment/Decrement function (No Problems with these)
     */
    var max = 10000000;
    var min = 50000;
    $scope.positionUnits = 2000000;
    $scope.leverage = 200;
    $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
    $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);


    $scope.increment = function() {

        if ($scope.positionUnits >= max) { return; }
        $scope.positionUnits +=50000;

        $scope.preTradeMarginRequiredTradedCurrency = $scope.positionUnits/$scope.leverage;
        $scope.preTradeMarginRequiredTradedCurrencyView = $scope.preTradeMarginRequiredTradedCurrency.toFixed(2);
        /**
         * I will now convert the margin from above to USD (account currency)
         */
        $scope.convertRequiredMarginToUSD($scope.preTradePairChosen.symbols);

    };
    $scope.decrement = function() {

        if ($scope.positionUnits <= min) { return; }
        $scope.positionUnits -=50000;

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
    $scope.challID=0;
    if($stateParams.challengeID){
        $scope.challID = $stateParams.challengeID;
    }

    $scope.currentChallenge={};
    $http.post('/api/challenge/findChallengeById',$scope.challID)
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


    $scope.checkIfGame =false;
    $scope.updateUserSummaryTable = function(){

        if($scope.challID>0){
            $scope.updateGameSummary();
            // $scope.available = $scope.theStake;
            // $scope.checkIfGame = true;
        }else{
            // $scope.available = $rootScope.currentUser.account.balance + $rootScope.currentUser.currentProfit;
            // $scope.checkIfGame=false;
            // $scope.PLV = $rootScope.currentUser.currentProfit;
            // $scope.equity = $scope.available + $scope.PLV + $rootScope.currentUser.totalMargin;
            $scope.updateEachTrade();
        }
    };


    $scope.updateGameSummary = function(){

        $scope.checkIfGame = true;

        var param = {};
        param.userId = $rootScope.currentUser.id+"";
        param.challId=$scope.challID+"";


        $http.post('/api/gameTrade/findUserChallengeGameAccount',JSON.stringify(param))
            .success(function (data, status) {
                if(status = 200){


                    console.log("game acount ",data);
                    $scope.gameAccount = data;
                    $scope.available = $scope.gameAccount.balance + $rootScope.currentUser.gameProfit;
                    $scope.mMargin=$rootScope.currentUser.gameMargin;
                    $scope.PLV = $rootScope.currentUser.gameProfit;
                    $scope.equity = $scope.available + $scope.PLV + $rootScope.currentUser.gameMargin;
                }
            }).error(function (error) {
            console.log("something went wrong in findUserChallengeGameAccount");
        });

        $scope.updateEachGameTrade();

    };

    $scope.updateEachGameTrade = function(){
        $http.post('/api/gameTrade/updateEachGameTrade',JSON.stringify($rootScope.currentUser))
            .success(function (data, status) {
                if(status = 200){
                    $http.post('/api/gameTrade/getTotalProfitAndLoss',JSON.stringify($rootScope.currentUser))
                        .success(function (data, status) {
                            if(status = 200){
                                $rootScope.currentUser = data;
                                $cookieStore.put('userCookie', $rootScope.currentUser);
                                $scope.available = $scope.gameAccount.balance + $rootScope.currentUser.gameProfit;
                                $scope.PLV = $rootScope.currentUser.gameProfit;
                                $scope.mMargin = $rootScope.currentUser.gameMargin;
                                $scope.equity = $scope.available + $scope.PLV + $rootScope.currentUser.gameMargin;

                            }
                        }).error(function (error) {
                        console.log("something went wrong in getTotalProfitAndLoss");
                    });
                    $scope.showOpenGameTrades();
                }
            }).error(function (error) {
            console.log("something went wrong in updateEachTrade");
        });
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

    $scope.gameTrade = function(){
      swal("in a game controller , boo yaa","ok","success");
        $scope.closeToggleLeft();
        var tradeObject = {};
        tradeObject.playerID = $rootScope.currentUser.id+"";
        tradeObject.pairSymbols=$scope.preTradePairChosen.symbols;
        tradeObject.margin = $scope.preTradeMarginRequiredUSD;
        tradeObject.action = $scope.preTradeAction;
        tradeObject.positionUnits = $scope.positionUnits;
        tradeObject.challengeID=$scope.challID;

        $http.post('/api/gameTrade/saveGameTrade',JSON.stringify(tradeObject))
            .success(function (data, status) {
                if(status = 200){
                    swal(data.action  +" " + data.currencyPairOpen.symbols + " "+ data.positionUnits+ " units", " position opened!", "success");
                    $scope.tradeObject = data;
                    $rootScope.currentUser=$scope.tradeObject.user;
                    $cookieStore.put('userCookie', $rootScope.currentUser);

                    $scope.xID =data.id;
                }
            }).error(function (error) {
            console.log("something went wrong in saveGameTrade");
        });
    };


    $scope.trade = function(){


        $scope.closeToggleLeft();

        var tradeObject = {};
        tradeObject.playerID = $rootScope.currentUser.id+"";
        tradeObject.pairSymbols=$scope.preTradePairChosen.symbols;
        tradeObject.margin = $scope.preTradeMarginRequiredUSD;
        tradeObject.action = $scope.preTradeAction;
        tradeObject.positionUnits = $scope.positionUnits;

        $http.post('/api/trade/saveTrade',JSON.stringify(tradeObject))
            .success(function (data, status) {
                if(status = 200){
                    swal(data.action  +" " + data.currencyPairOpen.symbols + " "+ data.positionUnits+ " units", " position opened!", "success");
                    $scope.tradeObject = data;
                    $rootScope.currentUser=$scope.tradeObject.user;
                    $cookieStore.put('userCookie', $rootScope.currentUser);

                    $scope.xID =data.id;

                    // $scope.tradeChart($scope.xID);

                }
            }).error(function (error) {
            console.log("something went wrong in saveTrade");
        });
    };





    $scope.updateEachTrade = function(){
        $http.post('/api/trade/updateEachTrade',JSON.stringify($rootScope.currentUser))
            .success(function (data, status) {
                if(status = 200){
                    $http.post('/api/trade/getTotalProfitAndLoss',JSON.stringify($rootScope.currentUser))
                        .success(function (data, status) {
                            if(status = 200){
                                $rootScope.currentUser = data;
                                $cookieStore.put('userCookie', $rootScope.currentUser);
                                $scope.PLV = $rootScope.currentUser.currentProfit;
                                $scope.mMargin = $rootScope.currentUser.totalMargin;
                                $scope.available = $rootScope.currentUser.account.balance;
                                $scope.equity = $scope.available + $scope.PLV + $rootScope.currentUser.totalMargin;

                            }
                        }).error(function (error) {
                        console.log("something went wrong in getTotalProfitAndLoss");
                    });
                    $scope.showOpenTrades();
                }
            }).error(function (error) {
            console.log("something went wrong in updateEachTrade");
        });
    };




    $scope.closeGameTrade = function(x){
        var closeParams = {};
        closeParams.tradeId = x.id+"";
        closeParams.challengeId = x.challenge.id+"";
        closeParams.userId = x.user.id+"";

        $http.post('/api/gameTrade/closeGameTrade',JSON.stringify(closeParams))
            .success(function (data, status) {
                if(status = 200){
                    swal(data.action+ " "+ data.currencyPairOpen.symbols+ " position", "successfully closed", "error");
                    // $rootScope.currentUser = data.user;
                    // $cookieStore.put('userCookie', $rootScope.currentUser);
                    // $scope.PLV = $rootScope.currentUser.currentProfit;
                    // $scope.mMargin = $rootScope$scope.data.currentUser.totalMargin;
                }
            }).error(function (error) {
            console.log("something went wrong in closeTrade!!");
        });
    };



    $scope.closeLiveTrade = function(x){
        var closeParams = {};
        closeParams.id = $rootScope.currentUser.id+"";
        closeParams.sym = x.symbols;
        $http.post('/api/trade/closeLiveTrade',JSON.stringify(closeParams))
                .success(function (data, status) {
                    if(status = 200){
                        swal(data.action+ " "+ data.currencyPairOpen.symbols+ " position", "successfully closed", "error");
                        $rootScope.currentUser = data.user;
                        $cookieStore.put('userCookie', $rootScope.currentUser);
                        $scope.PLV = $rootScope.currentUser.currentProfit;
                        $scope.mMargin = $rootScope.currentUser.totalMargin;
                    }
                }).error(function (error) {
                console.log("something went wrong in closeTrade!!");
            });
    };



    $scope.labels2 = ["January", "February", "March", "April", "May", "June", "July"];
    $scope.series2 = ['Series A'];
    $scope.data2 = [[65, 59, 80, 81, 56, 55, 40]];
    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };
    $scope.datasetOverride = [{ yAxisID: 'y-axis-2' }];
    $scope.options = {
        scales: {
            yAxes: [

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







})

    .filter('toMinSec', function(){
        return function(input){
            var minutes = parseInt(input/60, 10);
            var seconds = input%60;

            return minutes+' mins'+(seconds ? ' and '+seconds+' seconds' : '');
        }
    });




