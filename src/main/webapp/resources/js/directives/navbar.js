angular.module('myApp.navheader',[]).
directive('navheader', function($cookieStore, $state, $http, $rootScope){
    return{
        restrict:'E',
        scope : {},
        controller: function ($scope) {

            $scope.loggedIn = false;


            if($cookieStore.get('userCookie')){
                $scope.loggedIn = true;
            }

            $scope.logOut = function () {
                var obj = $cookieStore.get('userCookie');
                $cookieStore.remove('userCookie');

                $http.post('/api/user/logout', JSON.stringify(obj))
                    .success(function (data, status) {

                        if(status = 200){
                            $state.go('welcome');
                        }
                    }).error(function (error) {
                    console.log("something went wrong!!");
                });
            };
        },
        templateUrl:'resources/js/views/NavHeader.html',
        replace:true
    }
});