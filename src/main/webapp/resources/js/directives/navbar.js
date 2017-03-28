angular.module('myApp.navheader',[]).
directive('navheader', function($cookieStore, $state, $http){
    return{
        restrict:'E',
        scope : {},
        controller: function ($scope, $rootScope) {

            $rootScope.loggedIn = false;


            if($cookieStore.get('userCookie')){
                $rootScope.loggedIn = true;
                $scope.currentUser = $cookieStore.get('userCookie');
            }

            $scope.logOutUser = function () {
                var obj = $cookieStore.get('userCookie');
                $cookieStore.remove('userCookie');

                $rootScope.loggedIn = false;


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