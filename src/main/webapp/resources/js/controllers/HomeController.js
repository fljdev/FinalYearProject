angular.module('myApp.HomeController',[]).
    controller('HomeController',function($scope, $stateParams, $cookieStore){

    $scope.currUser = $cookieStore.get('userCookie').username;
    $scope.currUser = $cookieStore.get('userCookie');
    if($scope.currUser){
        $http.post('/api/user/findById', JSON.stringify($scope.currUser.id))
            .success(function (data, status) {
                if(status = 200){
                    $cookieStore.put('userCookie', data);
                }
            }).error(function (error) {
            console.log("something went wrong in findById -> HomeController!!");
        });
    }


});

