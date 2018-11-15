/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp")

    .controller('mainCtl', function ($window, $scope,  $state,$timeout,$websocket, $translate, $location){

        $scope.menus=[{module:"moduleDisplay",text:"显示所有模块",url:"mainDisplay.moduleDisplay"},
            {module:"uploadFile",text:"上传模块",url:"mainDisplay.uploadFile"},
            {module:"downFile",text:"下载安装包",url:"mainDisplay.downLoad"}
         ];
        $scope.menuOptions = {orientation: "vertical"};
        $scope.trans=function (menu) {
            $state.go(menu.url);
        };
        $timeout(function () {
            $state.go("mainDisplay.mainMenu")
            $("#vertical").kendoSplitter({
                orientation: "horizontal",
                panes: [

                    { collapsible: true,size:"10%" ,max:"100%"}


                ]
            });


        },2000)

    })

})();