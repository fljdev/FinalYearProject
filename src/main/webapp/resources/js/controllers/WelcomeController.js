angular.module('myApp.WelcomeController',[]).
controller('WelcomeController',function($scope,$cookieStore,$http){
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

    $scope.WelcomeText = "This is the Welcome Page!!";

});
