angular.module('myApp.RankController',[]).
controller('RankController',function($scope, $stateParams, $cookieStore, $http,$rootScope){



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
                    }
                }).error(function (error) {
                console.log("something went wrong in findById -> AllUsersController!!");
            });
        }
    };
    $scope.setUser();






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
                    $scope.ranks = data;
                }
            }).error(function (error) {
            console.log("something went wrong getting saveRanks!!");
        });


    };
    $scope.init();



});