angular.module('myApp',[
    'ngRoute',
    'ngSanitize',
    'ngAnimate',
    'ngAria',
    'ngMaterial',
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
    'myApp.ChallengedController',
    'myApp.FightController',
    'myApp.FightStartController',
    'myApp.FightEndController',
    'myApp.LogRegController',
    'myApp.ChallengeController',
    'myApp.OpenChallengesController',
    'myApp.TradeController'
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
        })
        .state('fight',{
            url: '/fight/:param',
            templateUrl: "resources/js/views/Fight.html",
            controller: "FightController"
        })
        .state('fightStart',{
            url: '/fightStart/:paramm',
            templateUrl: "resources/js/views/FightStart.html",
            controller: "FightStartController"
        })
        .state('fightEnd',{
            url: '/fightEnd',
            templateUrl: "resources/js/views/FightEnd.html",
            controller: "FightEndController"
        })
        .state('logreg',{
            url: '/logreg',
            templateUrl: "resources/js/views/LogReg.html",
            controller: "LogRegController"
        })
        .state('challenge',{
            url: '/challenge/:param',
            templateUrl: "resources/js/views/Challenge.html",
            controller: "ChallengeController"
        })
        .state('openChallenges',{
            url: '/openChallenges',
            templateUrl: "resources/js/views/OpenChallenges.html",
            controller: "OpenChallengesController"
        })
        .state('trade',{
            url: '/trade',
            templateUrl: "resources/js/views/Trade.html",
            controller: "TradeController"
        });
});

