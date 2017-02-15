angular.module('myApp.FightStartController',[]).
controller('FightStartController', function($scope,$cookieStore,$http,$state,$stateParams,$timeout){

    $scope.thisFight = {};
    $scope.currUser = $cookieStore.get('userCookie');
    $scope.opponent = {};

    /**
     * this is the id of the FightStart object that will be retrieved from the DB
     */
    var fightStartID = $stateParams.paramm;

    $scope.init = function(){
        $http.post('http://localhost:8080/api/fight/findFightObjectById',fightStartID)
            .success(function (data, status) {
                if(status = 200){
                    $scope.thisFight = data;
                    console.log($scope.thisFight.opponentID);
                    $scope.play();
                }
            }).error(function (error) {
            alert("something went wrong!!");
        });//end http.get
    }//end function
    $scope.init();


    $scope.getPercentLeverage = function(challLev,oppLev){
                if(challLev==400){
                    $scope.currMarginPercent = .25;
                }else if(challLev==200){
                    $scope.currMarginPercent = .50;
                }else if(challLev==100){
                    $scope.currMarginPercent = 1.0;
                }else if(challLev==50){
                    $scope.currMarginPercent = 2.0;
                }else if(challLev==33){
                    $scope.currMarginPercent = 3.0;
                }else if(challLev==20){
                    $scope.currMarginPercent = 5.0;
                }

                if(oppLev==400){
                    $scope.askedMarginPercent = .25;
                }else if(oppLev==200){
                    $scope.askedMarginPercent = .50;
                }else if(oppLev==100){
                    $scope.askedMarginPercent = 1.0;
                }else if(oppLev==50){
                    $scope.askedMarginPercent = 2.0;
                }else if(oppLev==33){
                    $scope.askedMarginPercent = 3.0;
                }else if(oppLev==20){
                    $scope.askedMarginPercent = 5.0;
                }
    }

    $scope.play = function(){
        console.log($scope.thisFight);
        $scope.currUsername = $scope.currUser.username;

        $http.post('http://localhost:8080/api/user/findById',$scope.thisFight.opponentID)
            .success(function (data, status) {
                if(status = 200){
                    console.log(data.username);
                    $scope.oppUsername = data.username;
                }
            }).error(function (error) {
            alert("something went wrong in find by id!!");
        });//end http.get


        $scope.mMargin = $scope.thisFight.challengerStake/2;

        $scope.getPercentLeverage($scope.thisFight.challengerLeverage, $scope.thisFight.opponentLeverage);

        $scope.currAvailable = $scope.thisFight.challengerBalance - $scope.thisFight.challengerStake ;
        $scope.currPair = $scope.thisFight.pairs[0].symbols;
        $scope.currDir = $scope.thisFight.challengerDirection;
        $scope.currValue= "work out";

        $scope.oppAvailable = $scope.thisFight.opponentBalance - $scope.thisFight.challengerStake ;
        $scope.oppPair = $scope.thisFight.pairs[1].symbols;
        $scope.oppDir = $scope.thisFight.opponentDirection;
        $scope.oppValue= "work out";

    }//end play



    // $http.get('http://localhost:8080/api/fight/getAllFightStartObjects')
    //     .success(function (data, status) {
    //         if(status = 200){
    //             console.log(data, "This is angalar fights controller");
    //             $scope.allFights = data;
    //
    //         }
    //     }).error(function (error) {
    //     alert("something went wrong!!");
    //
    // });//end http.get


    // $scope.currUser = $cookieStore.get('userCookie');
    // $scope.opponentID = $stateParams.paramm;
    // alert($scope.gameParamsObj.direction);
    //
    //
    //
    //
    // $scope.play = function(){
    //     alert("got into the play function in fightstart controller");
    //
    //
    //     $scope.gameParamsObj.cId = $scope.currUser.id +"";
    //     $scope.gameParamsObj.oId = $scope.opponentID;
    //
    //
    //     $scope.gameParamsObj.cPair=$scope.currUserPair.symbols;
    //     $scope.gameParamsObj.oPair=$scope.askedUserPair.symbols;
    //
    //
    //     $http.post('http://localhost:8080/api/fight/getFightGameObject', JSON.stringify($scope.gameParamsObj))
    //         .success(function (data, status) {
    //             if(status = 200){
    //
    //                 // $scope.register = data;
    //                 console.log("worked");
    //
    //
    //                 // $state.go('home');
    //                 // $cookieStore.put('userCookie',$scope.register);
    //
    //
    //             }
    //         }).error(function (error) {
    //         alert("something went wrong!!");
    //     });
    // }
    // $scope.play();
    //
    //
    //
    //
    // //     $scope.currentUserFinalPair = $scope.currUserPair ;
    // //     $scope.askedUserFinalPair = $scope.askedUserPair ;
    // //
    // //
    // //     /**
    // //      * Get the players stakes in numeric format for calculations
    // //      */
    // //     $scope.currUserStakeFloat = parseFloat($scope.currUserStake)
    // //     $scope.askedUserStakeFloat = parseFloat($scope.askedUserStake)
    // //
    // //     /**
    // //      * Going long, they pay the ask, so get the ask for both pairs
    // //      */
    // //     $scope.currUserPairFloatAsk = parseFloat($scope.currUserPair.ask)
    // //     $scope.askedUserPairFloatAsk = parseFloat($scope.askedUserPair.ask)
    // //
    // //     /**
    // //      * get the leverage each player want to fight with
    // //      */
    // //     $scope.currUserLev = $scope.currUserLeverage
    // //     $scope.askedUserLev = $scope.askedUserLeverage
    // //
    // //     /**
    // //      * get the direction (long/short) for each player
    // //      */
    // //     $scope.currUserDir = $scope.currUserDirection
    // //     $scope.askedUserDir = $scope.askedUserDirection
    // //
    // //
    // //     $scope.currUserPosSize = $scope.currUserStakeFloat * $scope.currUserPairFloatAsk * $scope.currUserLev;
    // //     // var x = $scope.currUserPosSize
    // //     // var x4 = x.toFixed(4);
    // //     // $scope.currUserPosSize = x4;
    // //
    // //     $scope.askedUserPosSize = $scope.askedUserStakeFloat * $scope.askedUserPairFloatAsk * $scope.askedUserLev;
    // //     // var y = $scope.askedUserPosSize
    // //     // var y4 = y.toFixed(4);
    // //     // $scope.askedUserPosSize = y4;
    // //
    // //     $scope.currUser.account.balance = $scope.currUser.account.balance - $scope.currUserStakeFloat
    // //     $scope.askedUser.account.balance = $scope.askedUser.account.balance - $scope.askedUserStakeFloat
    // //
    // //
    // //     $scope.updatePriceChanges();
    // // }
    // //
    // // $scope.updatePriceChanges = function(){
    // //
    // //     $scope.counter = 10;
    // //
    // //     $scope.onTimeout = function(){
    // //         $scope.counter--;
    // //         mytimeout = $timeout($scope.onTimeout,1000);
    // //
    // //         $scope.initPairs();
    // //
    // //         function findLatestQuote(currentCurrency) {
    // //             return currentCurrency.symbols === $scope.currentUserFinalPair.symbols;
    // //         }//end findLatestPosition
    // //
    // //
    // //         $scope.currUserLatestPositon =  $scope.pairs.find(findLatestQuote);
    // //         $scope.askedUserLatestPositon =  $scope.pairs.find(findLatestQuote);
    // //
    // //         $scope.currUserPosSize = $scope.currUserStakeFloat * parseFloat($scope.currUserLatestPositon.ask) * $scope.currUserLev;
    // //         var x = $scope.currUserPosSize
    // //         var x4 = x.toFixed(4);
    // //         $scope.currUserPosSize = x4;
    // //         $scope.askedUserPosSize = $scope.askedUserStakeFloat * parseFloat($scope.askedUserLatestPositon.ask) * $scope.askedUserLev;
    // //         var y = $scope.askedUserPosSize
    // //         var y4 = y.toFixed(4);
    // //         $scope.askedUserPosSize = y4;
    // //
    // //
    // //         $scope.currUserMargin = $scope.currUserStakeFloat;
    // //         $scope.askedUserMargin = $scope.askedUserStakeFloat;
    // //         // $scope.marginPercent=11;
    // //
    // //         if($scope.currUserLev==400){
    // //             $scope.currMarginPercent = .25;
    // //         }else if($scope.currUserLev==200){
    // //             $scope.currMarginPercent = .50;
    // //         }else if($scope.currUserLev==100){
    // //             $scope.currMarginPercent = 1.0;
    // //         }else if($scope.currUserLev==50){
    // //             $scope.currMarginPercent = 2.0;
    // //         }else if($scope.currUserLev==33){
    // //             $scope.currMarginPercent = 3.0;
    // //         }else if($scope.currUserLev==20){
    // //             $scope.currMarginPercent = 5.0;
    // //         }
    // //
    // //         if($scope.askedUserLev==400){
    // //             $scope.askedMarginPercent = .25;
    // //         }else if($scope.askedUserLev==200){
    // //             $scope.askedMarginPercent = .50;
    // //         }else if($scope.askedUserLev==100){
    // //             $scope.askedMarginPercent = 1.0;
    // //         }else if($scope.askedUserLev==50){
    // //             $scope.askedMarginPercent = 2.0;
    // //         }else if($scope.askedUserLev==33){
    // //             $scope.askedMarginPercent = 3.0;
    // //         }else if($scope.askedUserLev==20){
    // //             $scope.askedMarginPercent = 5.0;
    // //         }
    // //
    // //         if($scope.counter==0){
    // //
    // //             $timeout.cancel(mytimeout);
    // //         }//end if
    // //     }//end onTimeout
    // //     var mytimeout = $timeout($scope.onTimeout,1000);
    // //
    // // }//end updatePriceChanges
    //


});//end controller
