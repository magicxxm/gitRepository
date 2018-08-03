/**
 * Created by frank.zhou on 2017/04/20.
 */
(function(){
  "use strict";

  angular.module("myApp")
    .provider("myProvider", function($translateProvider, BACKEND_CONFIG){
      this.$get = function(){
        return {
          setLocale: function(lang){
            $.ajax({
              method: "GET",
              async: false,
              url: BACKEND_CONFIG.system+ "system/resources?locale="+ lang,
              success: function(resourceMap){
                $translateProvider.translations(lang, resourceMap);
              }
            });
          }
        };
      };
    })
    .controller("mainCtl", function($scope, $state, $window, $translate, mainService, myProvider){
      // ===============================================================================================================
      // 切换语言
      $scope.changeLanguage = function(langKey){
        langKey == null && (langKey = "CN"); // 默认语言
        myProvider.setLocale(langKey);
        $translate.use(langKey);
      };

      // 退回
      $scope.goBack = function(){
        $state.go("main.mainMenu");
      };

      // 登出
      $scope.loginOut = function(){
        $window.localStorage["accessToken"] = "";
        $state.go("login");
      };

      // 子菜单
      $scope.forwardSubMenu = function(items){
        $scope.subMenus = items;
        $state.go("main.subMenu");
      };
      
      // ***************************************************************************************************************
      $scope.changeLanguage(); // 设置语言

      $scope.splitterHorizontalOptions = {
        panes: [
          {collapsible: true, resizable: false, size: "250px"}
        ],
        orientation: "horizontal"
      };
      $scope.menuOrientation = {orientation: "horizontal"};
      // menu
      $scope.menuOptions = {orientation: "vertical"};
      mainService.getMainMenu(function(datas){
        $scope.menus = datas;
        $state.go("main.mainMenu");
      });
    }).controller("subMenuCtl", function($state){
      // 函数
      function transformItem(str){
        for(var i = 0, outs = [], preIdx = 0; i < str.length; i++){
          var c = str.charAt(i);
          if(i == str.length - 1){
            var last = str.substring(preIdx);
            outs.push(last.substring(0, 1).toLowerCase()+ last.substring(1));
          }
          if(c < "A" || c > "Z") continue;
          var mid = str.substring(preIdx, i);
          outs.push(outs.length? mid.substring(0, 1).toLowerCase()+ mid.substring(1): mid);
          outs.push("_");
          preIdx = i;
        }
        return outs.join("");
      }

      // 初始化
      var splitter = $("#mainSplitter").data("kendoSplitter");
      var name = $state.current.name.replace("Read", "").replace("Create", "").replace("Update", "");
      var inbound = ["receiving", "receivingContainer", "stow", "inbound_problem_disposal", "inbound_problem_manage","problemInbound",'receivingSingle','receivingPallet','receivingTote','problemInboundManageDetail'];
      var outbound = ["pickToPack", "pickToTote","pickToSinglePack", "rebatch", "pick", "pack", "packScanGoods", "packNoScanGoods" , "packScanShipment" , "re_bin", "rebinMain", "rebin_pack"];
      var outboundProblem = ["outbound_problem_disposal", "outbound_problem_verify", "outbound_problem_manage"];
      var shipment = ["delivery_system","deliverySystemPrint", "delivery_shipments_detail", "shipment_detail", "query_cart", "cart_query_shipment"];
      var tool = ["input_validity_query", "measure_query","stockunit_check","lot_manager","item_query"];
      var report = ["fud", "workflow", "pick_query", "capacity","capacity_side","capacity_bin"];
      var tot = ["tot_attendance", "tot_jobrecord"];
      var items = inbound.concat(outbound, outboundProblem, shipment, tool, report,tot), targetName = name.replace("main.", "");
      if(items.indexOf(targetName) >= 0 || targetName.indexOf("icqa") >= 0 || targetName.indexOf("_tool") >= 0 || targetName.indexOf("problemOutbound") >= 0)
        splitter && splitter.collapse(".k-pane:first");
      else{
        splitter && splitter.expand(".k-pane:first");
        setTimeout(function(){
          var target = $("li[ui-sref='"+ transformItem(name)+ "']");
          target.css({"background-color": "#00b0ff", "color": "white"});
        }, 300);
      }
    }).controller("mainContainerCtl", function($rootScope){
      // layout-listView
      $rootScope.mainWidth = $("#mainContainer").width();
      $rootScope.mainHeight = $("#mainContainer").height()-35-10-10-26;
      $rootScope.listHeight = $rootScope.mainHeight-30-10-2;
      $rootScope.listBtnTop = ($rootScope.mainHeight-176)/2;
    });
})();