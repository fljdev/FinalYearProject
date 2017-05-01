angular.module('myApp.HomeController',[]).
    controller('HomeController',function($scope, $stateParams, $cookieStore,$http,$rootScope,$timeout){

    $scope.setUser = function(){
        $rootScope.currentUser = $cookieStore.get('userCookie');
        if($rootScope.currentUser){
            $http.post('/api/user/findById', JSON.stringify($rootScope.currentUser.id))
                .success(function (data, status) {
                    if(status = 200){
                        $cookieStore.put('userCookie', data);
                        $rootScope.currentUser = data;
                    }
                }).error(function (error) {
                console.log("something went wrong in findById -> HomeController!!");
            });
        }
    };
    $scope.setUser();

    $scope.personalViewVisible = true;
    $scope.personalView = function(){
        console.log("personal tab");
        $scope.personalViewVisible = true;
        $scope.soloHistoryViewVisible = false;
        $scope.gameHistoryViewVisible = false;
    };
    $scope.soloHistoryView = function(){
        console.log("solo tab");
        $scope.soloHistoryViewVisible = true;
        $scope.personalViewVisible = false;
        $scope.gameHistoryViewVisible = false;
    };
    $scope.gameHistoryView = function(){
        console.log("game tab");
        $scope.gameHistoryViewVisible = true;
        $scope.personalViewVisible = false;
        $scope.soloHistoryViewVisible = false;
    };




    $http.get('/api/home/averageBalance')
        .success(function (data, status) {
            if(status = 200){
                var allBankBalances = data;
                var amountOfAccounts = data.length;
                var total = 0;
                for(i=0;i<amountOfAccounts;i++){
                    total+=allBankBalances[i];
                }
                $scope.averageBalance = total/amountOfAccounts;
            }}).error(function (error) {
        console.log("something went wrong in /api/home/averageBalance!!");
    });

    $http.post('/api/home/numberOfSoloTrades', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.numberOfSoloTrades = data;
            }}).error(function (error) {
        console.log("something went wrong in /api/home/numberOfSoloTrades!!");
    });

    $http.post('/api/home/soloTradeProfit', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.soloTradeProfit = data;
                console.log("solo trade profit ",$scope.soloTradeProfit)
            }}).error(function (error) {
        console.log("something went wrong in /api/home/soloTradeProfit!!");
    });

    $http.post('/api/home/soloTradeLoss', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.soloTradeLoss = data;
                console.log("solo trade loss ",$scope.soloTradeLoss)


            }}).error(function (error) {
        console.log("something went wrong in /api/home/soloTradeLoss!!");
    });

    $http.post('/api/home/largestSoloTrade', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.largestSoloTrade = data;
                console.log("largestSoloTrade ",$scope.largestSoloTrade)

            }}).error(function (error) {
        console.log("something went wrong in /api/home/soloTradeLoss!!");
    });

    $http.get('/api/home/averageSoloTrade')
        .success(function (data, status) {
            if(status = 200){
                $scope.averageSoloTrade = data;
                console.log("averageSoloTrade ",$scope.averageSoloTrade)

            }}).error(function (error) {
        console.log("something went wrong in /api/home/averageSoloTrade!!");
    });

    $http.get('/api/home/largestSoloTradeByAllUsers')
        .success(function (data, status) {
            if(status = 200){
                $scope.largestSoloTradeByAllUsers = data;
                console.log("largestSoloTradeByAllUsers ",$scope.largestSoloTradeByAllUsers)
                $scope.chartSoloTrades();

            }}).error(function (error) {
        console.log("something went wrong in /api/home/averageSoloTrade!!");
    });


    $http.post('/api/home/favPair', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.favPair = data;
                console.log("favPair ",$scope.favPair)

            }}).error(function (error) {
        console.log("something went wrong in /api/home/favPair!!");
    });




    $http.post('/api/home/getAllChallengesUserInvolvedIn', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.getAllChallengesUserInvolvedIn = data;
            }}).error(function (error) {
        console.log("something went wrong in /api/home/getAllChallengesUserInvolvedIn!!");
    });

    $http.post('/api/home/numberOfGameTrades', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.numberOfGameTrades = data;
            }}).error(function (error) {
        console.log("something went wrong in /api/home/numberOfGameTrades!!");
    });

    $http.post('/api/home/gameProfit', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.gameProfit = data;
                console.log("game profit ",$scope.gameProfit);
            }}).error(function (error) {
        console.log("something went wrong in /api/home/gameProfit!!");
    });

    $http.post('/api/home/gameLoss', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.gameLoss = data;
                console.log("game loss ",$scope.gameLoss);
                $scope.chartGameTrades();
            }}).error(function (error) {
        console.log("something went wrong in /api/home/gameLoss!!");

    });





    $scope.chartSoloTrades = function(){

        $scope.soloTradeProfitLossLabels = ["Profit","Losses"];
        $scope.soloTradeProfitLossData = [$scope.soloTradeProfit,($scope.soloTradeLoss*-1)];

        $scope.positionSizeLabels =["Your Highest","Average","Highest on Record"];
        $scope.positionSizeData =[$scope.largestSoloTrade,$scope.averageSoloTrade,$scope.largestSoloTradeByAllUsers];

        console.log("average here is ",$scope.averageSoloTrade)

    };

    $http.post('/api/home/challengesWithdrawn', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.challengesWithdrawn = data;
                $scope.challengesData.push($scope.challengesWithdrawn);
                console.log("challengesWithdrawn ",$scope.challengesWithdrawn);

            }}).error(function (error) {
        console.log("something went wrong in /api/home/soloTradeLoss!!");
    });

    $http.post('/api/home/challengesDeclined', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.challengesDeclined = data;
                $scope.challengesData.push($scope.challengesDeclined);

                console.log("challengesDeclined ",$scope.challengesDeclined)

            }}).error(function (error) {
        console.log("something went wrong in /api/home/soloTradeLoss!!");
    });

    $http.post('/api/home/challengesAccepted', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.challengesAccepted = data;
                $scope.challengesData.push($scope.challengesAccepted);

                console.log("challengesAccepted ",$scope.challengesAccepted)

            }}).error(function (error) {
        console.log("something went wrong in /api/home/soloTradeLoss!!");
        $scope.chartChallengeHistory();
    });

    $scope.chartGameTrades = function(){
        $scope.gameTradeProfitLossLabels = ["Profit","Losses"];
        $scope.gameTradeProfitLossData = [$scope.gameProfit,($scope.gameLoss)];
    };

    $scope.chartChallengeHistory = function(){

        $scope.challengesLabels = ["accepted","declined","withdrawn"];
        $scope.challengesData= [];
    };
    $scope.chartChallengeHistory();










    $http.post('/api/stats/findWinsByUser',$rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.victoriesList = data;


                $http.post('/api/stats/findTotalWinnings',$rootScope.currentUser.id)
                    .success(function (data, status) {
                        if(status = 200){
                            $scope.totalWinnings = data;
                        }
                    }).error(function (error) {
                    console.log("something went wrong in /api/stats/findByUser!!");
                });


            }
        }).error(function (error) {
        console.log("something went wrong in /api/stats/findByUser!!");
    });

    $http.post('/api/stats/findLossesByUser',$rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.lossesList = data;

                $http.post('/api/stats/findTotalLosses',$rootScope.currentUser.id)
                    .success(function (data, status) {
                        if(status = 200){
                            $scope.totalLosses = data;
                        }
                    }).error(function (error) {
                    console.log("something went wrong in /api/stats/findByUser!!");
                });
            }
        }).error(function (error) {
        console.log("something went wrong in /api/stats/findByUser!!");
    });

    $timeout (function(){
        $scope.result = ["won","lost"];
        $scope.number = [$scope.victoriesList.length, $scope.lossesList.length];
    }, 1500);

    $scope.options = {


        responsive: true,
        scales: {
            yAxes: [{
                ticks: {
                    suggestedMax : 20,
                    beginAtZero:true
                }
            }]
        }
    };

    $scope.pieOptions = {
        responsive: true,
        legend: {
            display: true
        }
    };


});

