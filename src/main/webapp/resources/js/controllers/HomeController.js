angular.module('myApp.HomeController',[]).
    controller('HomeController',function($scope, $stateParams, $cookieStore,$http,$rootScope,$timeout){

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









});

