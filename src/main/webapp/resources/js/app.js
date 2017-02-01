angular.module('myApp',[
    'ngRoute',
    'ngSanitize',
    'ngCookies',
    'chart.js',
    'ui.router',
    'myApp.HomeController',
    'myApp.LoginController',
    'myApp.RegisterController',
    'myApp.WelcomeController',
    'myApp.OnlineController',
    'myApp.AllUsersController',
    'myApp.navheader',
    'myApp.PairsController',
    'myApp.ChallengedController'
]).
    config(function ($stateProvider,$urlRouterProvider) {

    $urlRouterProvider.otherwise("/welcome");

    $stateProvider
        .state('welcome',{
            url:"/welcome",
            templateUrl: "resources/js/views/Welcome.html",
            controller:"WelcomeController"
        })
        .state('home',{
            url:"/home",
            templateUrl: "resources/js/views/Home.html",
            controller:"HomeController"
        })
        .state('login',{
            url:"/login",
            templateUrl: "resources/js/views/Login.html",
            controller:"LoginController"
        })
        .state('register', {
            url: "/register",
            templateUrl: "resources/js/views/Register.html",
            controller: "RegisterController"
        })
        .state('allUsers', {
            url: "/allUsers",
            templateUrl: "resources/js/views/AllUsers.html",
            controller: "AllUsersController"
        })
        .state('onlineUsers',{
            url: "/onlineUsers",
            templateUrl: "resources/js/views/Online.html",
            controller: "OnlineController"
        })
        .state('pairs',{
            url: "/pairs",
            templateUrl: "resources/js/views/Pairs.html",
            controller: "PairsController"
        })
        .state('challenged',{
            url: '/challenged/:param',
            templateUrl: "resources/js/views/Challenged.html",
            controller: "ChallengedController"
        });;
});

