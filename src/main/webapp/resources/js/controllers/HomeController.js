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


    $http.post('/api/home/favPair', $rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                $scope.favPair = data;
                console.log("favPair ",$scope.favPair)

            }}).error(function (error) {
        console.log("something went wrong in /api/home/favPair!!");
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
            }}).error(function (error) {
        console.log("something went wrong in /api/home/gameLoss!!");
    });






});

