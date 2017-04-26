angular.module('myApp.StatsController',[]).
controller('StatsController',function($scope, $stateParams, $cookieStore, $http,$rootScope,$timeout){



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
    $scope.statsMessage = $rootScope.currentUser.firstName + " in the stat page";






    $http.post('/api/stats/findByUser',$rootScope.currentUser.id)
        .success(function (data, status) {
            if(status = 200){
                console.log("got back results ",data)
                $scope.resultList = data;
            }
        }).error(function (error) {
        console.log("something went wrong in /api/stats/findByUser!!");
    });

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




});