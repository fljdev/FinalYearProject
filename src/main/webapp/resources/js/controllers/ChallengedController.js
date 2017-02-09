angular.module('myApp.ChallengedController',[]).
controller('ChallengedController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){

    $scope.challenged = {};

    $scope.direction = ["long","short"];
    $scope.stakes = ["100", "250","500","1000","2500","5000","10000"]
    $scope.leverages=["20","33","50","100","200","400"];

    $scope.currUser = $cookieStore.get('userCookie');
    $scope.askedUserID = $stateParams.param;
    $scope.askedUser;




    $scope.pairs;
    $scope.initPairs = function(){
        $http.get('http://localhost:8080/api/fight/pairs')
            .success(function (data, status) {
                if(status = 200){

                    //data will be equal to the arraylist returned by the UserRestController
                    console.log(data, "This is angalar pairs");
                    $scope.pairs = data;

                }
            }).error(function (error) {
            console.log("something went wrong in the pairs controller init function!!");
        });//end http.get
    }
    $scope.initPairs()



    $http.post('http://localhost:8080/api/user/findById', $scope.askedUserID)
        .success(function (data, status) {
            if(status = 200){
                $scope.askedUser = data;
            }
        }).error(function (error) {
        alert("something went wrong!!");
    });



    $scope.p1 = $scope.currUser.symbols;

    $scope.fight = function() {

        var opponentID = $scope.askedUserID;

        alert(opponentID);



        // $state.go('fightStart',{paramm:opponentID});


        var cI = $scope.currUser.id +"";
        var oI = $scope.askedUser.id +"";
        console.log(cI);

        $scope.gameParamsObj.cId = cI;
        $scope.gameParamsObj.oId = oI;


        $scope.gameParamsObj.cPair=$scope.currUserPair.symbols;
        $scope.gameParamsObj.oPair=$scope.askedUserPair.symbols;

        $scope.gameParamsObj.cBalance = $scope.currUser.account.balance+"";
        $scope.gameParamsObj.oBalance = $scope.askedUser.account.balance+"";

        $http.post('http://localhost:8080/api/fight/getFightStartObject', JSON.stringify($scope.gameParamsObj))
            .success(function (data, status) {
                if(status = 200){

                    // $scope.register = data;
                    console.log("getFightStartObject method worked");


                    // $state.go('home');
                    // $cookieStore.put('userCookie',$scope.register);


                }
            }).error(function (error) {
            alert("something went wrong!!");
        });


        // $http.post('http://localhost:8080/api/fight/getFightGameObject', JSON.stringify($scope.gameParamsObj))
        //     .success(function (data, status) {
        //         if(status = 200){
        //
        //             // $scope.register = data;
        //             console.log("worked");
        //
        //
        //             // $state.go('home');
        //             // $cookieStore.put('userCookie',$scope.register);
        //
        //
        //         }
        //     }).error(function (error) {
        //     alert("something went wrong!!");
        // });


        //     $scope.currentUserFinalPair = $scope.currUserPair ;
        //     $scope.askedUserFinalPair = $scope.askedUserPair ;
        //
        //
        //     /**
        //      * Get the players stakes in numeric format for calculations
        //      */
        //     $scope.currUserStakeFloat = parseFloat($scope.currUserStake)
        //     $scope.askedUserStakeFloat = parseFloat($scope.askedUserStake)
        //
        //     /**
        //      * Going long, they pay the ask, so get the ask for both pairs
        //      */
        //     $scope.currUserPairFloatAsk = parseFloat($scope.currUserPair.ask)
        //     $scope.askedUserPairFloatAsk = parseFloat($scope.askedUserPair.ask)
        //
        //     /**
        //      * get the leverage each player want to fight with
        //      */
        //     $scope.currUserLev = $scope.currUserLeverage
        //     $scope.askedUserLev = $scope.askedUserLeverage
        //
        //     /**
        //      * get the direction (long/short) for each player
        //      */
        //     $scope.currUserDir = $scope.currUserDirection
        //     $scope.askedUserDir = $scope.askedUserDirection
        //
        //
        //     $scope.currUserPosSize = $scope.currUserStakeFloat * $scope.currUserPairFloatAsk * $scope.currUserLev;
        //     // var x = $scope.currUserPosSize
        //     // var x4 = x.toFixed(4);
        //     // $scope.currUserPosSize = x4;
        //
        //     $scope.askedUserPosSize = $scope.askedUserStakeFloat * $scope.askedUserPairFloatAsk * $scope.askedUserLev;
        //     // var y = $scope.askedUserPosSize
        //     // var y4 = y.toFixed(4);
        //     // $scope.askedUserPosSize = y4;
        //
        //     $scope.currUser.account.balance = $scope.currUser.account.balance - $scope.currUserStakeFloat
        //     $scope.askedUser.account.balance = $scope.askedUser.account.balance - $scope.askedUserStakeFloat
        //
        //
        //     $scope.updatePriceChanges();
        // }
        //
        // $scope.updatePriceChanges = function(){
        //
        //     $scope.counter = 10;
        //
        //     $scope.onTimeout = function(){
        //         $scope.counter--;
        //         mytimeout = $timeout($scope.onTimeout,1000);
        //
        //         $scope.initPairs();
        //
        //         function findLatestQuote(currentCurrency) {
        //             return currentCurrency.symbols === $scope.currentUserFinalPair.symbols;
        //         }//end findLatestPosition
        //
        //
        //         $scope.currUserLatestPositon =  $scope.pairs.find(findLatestQuote);
        //         $scope.askedUserLatestPositon =  $scope.pairs.find(findLatestQuote);
        //
        //         $scope.currUserPosSize = $scope.currUserStakeFloat * parseFloat($scope.currUserLatestPositon.ask) * $scope.currUserLev;
        //         var x = $scope.currUserPosSize
        //         var x4 = x.toFixed(4);
        //         $scope.currUserPosSize = x4;
        //         $scope.askedUserPosSize = $scope.askedUserStakeFloat * parseFloat($scope.askedUserLatestPositon.ask) * $scope.askedUserLev;
        //         var y = $scope.askedUserPosSize
        //         var y4 = y.toFixed(4);
        //         $scope.askedUserPosSize = y4;
        //
        //
        //         $scope.currUserMargin = $scope.currUserStakeFloat;
        //         $scope.askedUserMargin = $scope.askedUserStakeFloat;
        //         // $scope.marginPercent=11;
        //
        //         if($scope.currUserLev==400){
        //             $scope.currMarginPercent = .25;
        //         }else if($scope.currUserLev==200){
        //             $scope.currMarginPercent = .50;
        //         }else if($scope.currUserLev==100){
        //             $scope.currMarginPercent = 1.0;
        //         }else if($scope.currUserLev==50){
        //             $scope.currMarginPercent = 2.0;
        //         }else if($scope.currUserLev==33){
        //             $scope.currMarginPercent = 3.0;
        //         }else if($scope.currUserLev==20){
        //             $scope.currMarginPercent = 5.0;
        //         }
        //
        //         if($scope.askedUserLev==400){
        //             $scope.askedMarginPercent = .25;
        //         }else if($scope.askedUserLev==200){
        //             $scope.askedMarginPercent = .50;
        //         }else if($scope.askedUserLev==100){
        //             $scope.askedMarginPercent = 1.0;
        //         }else if($scope.askedUserLev==50){
        //             $scope.askedMarginPercent = 2.0;
        //         }else if($scope.askedUserLev==33){
        //             $scope.askedMarginPercent = 3.0;
        //         }else if($scope.askedUserLev==20){
        //             $scope.askedMarginPercent = 5.0;
        //         }
        //
        //         if($scope.counter==0){
        //
        //             $timeout.cancel(mytimeout);
        //         }//end if
        //     }//end onTimeout
        //     var mytimeout = $timeout($scope.onTimeout,1000);
        //
        // }//end updatePriceChanges
    }

});//end controller
