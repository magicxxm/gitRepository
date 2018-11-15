/**
 * Created by frank.zhou on 2017/04/14.
 * Updated by frank.zhou on 2017/05/10.
 */
(function () {
  "use strict";

  angular.module('myApp').config(function ($stateProvider, $urlRouterProvider) {
    // ====================================================================================================================




    // ====================================================================================================================
    $urlRouterProvider.otherwise("/mainDisplay");
    $stateProvider

        .state("mainDisplay.uploadFile", {
            url: "/uploadFile",
            views: {
                "menu": {
                    templateUrl: "modules/main/templates/mainMenu.html"
                },

                "container": {
                    templateUrl: "modules/uploadFile/templates/uploadFile.html",
                    controller:"uploadFileCtl"

                }
            }
        })

        .state("mainDisplay.moduleDisplay", {
            url: "/moduleDisplay",
            views: {
                "menu": {
                    templateUrl: "modules/main/templates/mainMenu.html"
                },

                "container": {
                    templateUrl: "modules/main/templates/allModule.html",
                    controller:"moduleCtl"

                }
            }
        })
        .state("mainDisplay", {
            url: "/mainDisplay",
            templateUrl: "modules/main/templates/main.html",
            controller:"mainCtl"

        })

        .state("mainDisplay.mainMenu", {
            url: "/mainMenu",
            views: {
                "menu": {
                    templateUrl: "modules/main/templates/mainMenu.html"
                },

                "container": {
                    templateUrl: "modules/main/templates/mainContainer.html"


                }
            }


        })

        .state("mainDisplay.downLoad", {
            url: "/downLoad",
            views: {
                "menu": {
                    templateUrl: "modules/main/templates/mainMenu.html"
                },

                "container": {
                    templateUrl: "modules/downLoadFile/templates/downLoadFile.html",
                    controller:"downLoadFileCtl"

                }
            }


        })

        .state("mainDisplay.installModule", {
            url: "/installModule",
            params:{"modules":""},
            views: {
                "menu": {
                    templateUrl: "modules/main/templates/mainMenu.html"
                },

                "container": {
                    templateUrl: "modules/installModule/templates/installModule.html",
                    controller:"installModuleCtl"

                }
            }
        })


  });
})();