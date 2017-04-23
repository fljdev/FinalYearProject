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


    $scope.counter = 0;
    $scope.onTimeout = function(){
        $scope.counter++;
        mytimeout = $timeout($scope.onTimeout,1000);
    }
    var mytimeout = $timeout($scope.onTimeout,1000);

    $scope.stop = function(){
        $timeout.cancel(mytimeout);
    }


})
.filter('toMinSec', function(){
    return function(input){
        var minutes = parseInt(input/60, 10);
        var seconds = input%60;

        return minutes+' mins'+(seconds ? ' and '+seconds+' secs' : '');
    }
});

