angular.module('myApp.RankController',[]).
controller('RankController',function($scope, $stateParams, $cookieStore, $http){



    $scope.currUser = $cookieStore.get('userCookie').username;

    // if($scope.currUser){
    //     $http.post('/api/user/findById', JSON.stringify($scope.currUser.id))
    //         .success(function (data, status) {
    //             if(status = 200){
    //                 $cookieStore.put('userCookie', data);
    //             }
    //         }).error(function (error) {
    //         console.log("something went wrong in findById -> RankController!!");
    //     });
    // }



    $scope.init = function(){
        // $http.get('/api/rank/allRanks')
        //     .success(function (data, status) {
        //         if(status = 200){
        //             $scope.allRanks = data;
        //         }
        //     }).error(function (error) {
        //     console.log("something went wrong getting allRanks!!");
        // });

        $http.get('/api/rank/saveRank')
            .success(function (data, status) {
                if(status = 200){
                    // $scope.allRanks = data;\
                    console.log(",saveRank returned : ",data);
                    $scope.ranks = data;
                }
            }).error(function (error) {
            console.log("something went wrong getting saveRanks!!");
        });


    }
    $scope.init();



});