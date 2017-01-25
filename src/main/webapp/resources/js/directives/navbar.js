angular.module('myApp.navheader',[]).
directive('navheader', function($cookieStore){
    return{
        restrict:'E',
        scope : {

        },
        controller: function ($scope) {

            $scope.logOut = function () {
                console.log("Pressed");
                $cookieStore.remove('cookie');

            };

            $scope.home="HOME!!!";
        },
        templateUrl:'resources/js/views/NavHeader.html',
        replace:true
    }
});