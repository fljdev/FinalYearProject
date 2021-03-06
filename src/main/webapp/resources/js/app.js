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
    'myApp.WelcomeController',
    'myApp.OnlineController',
    'myApp.AllUsersController',
    'myApp.navheader',
    'myApp.PairsController',
    'myApp.LogRegController',
    'myApp.OpenChallengesController',
    'myApp.TradeController',
    'myApp.RankController',
    'myApp.StatsController',
    'myApp.WhatIsForexController',
    'myApp.HowToUseThisSiteController'

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
        .state('logreg',{
            url: '/logreg',
            templateUrl: "resources/js/views/LogReg.html",
            controller: "LogRegController"
        })
        .state('openChallenges',{
            url: '/openChallenges',
            templateUrl: "resources/js/views/OpenChallenges.html",
            controller: "OpenChallengesController"
        })
        .state('trade',{
            url: '/trade/{challengeID}',
            templateUrl: "resources/js/views/Trade.html",
            controller: "TradeController"
        })
        .state('rank',{
            url: '/rank',
            templateUrl: "resources/js/views/Rank.html",
            controller: "RankController"
        })
        .state('stats',{
            url: '/stats',
            templateUrl: "resources/js/views/Stats.html",
            controller: "StatsController"
        })
        .state('whatIsForex',{
            url: '/whatIsForex',
            templateUrl: "resources/js/views/WhatIsForex.html",
            controller: "WhatIsForexController"
        })
        .state('howToUseThisSite',{
            url: '/howToUseThisSite',
            templateUrl: "resources/js/views/HowToUseThisSite.html",
            controller: "HowToUseThisSiteController"
        })
      ;
});

