
(function () {
  'use strict';

  angular.module('myApp').controller("unpickmenuCtl", function ($scope,$window,$state,outboundService,commonService,masterService,unpickmenuService) {
      setTimeout(function(){ $("#stationName").focus();}, 0);
      $scope.warehouseId=$window.localStorage["warehouseId"];
      $scope.clientId=$window.localStorage["clientId"];
      $scope.stationname='';
      $scope.serachBnt = true;
      var flag = true;
      $scope.inboundIntefaceDatas={};
      var columns= [
          { field:"customershipmentNo", title:"订单号" },
          { field:"stationName", title: "工作站名称" },
          { field:"skuName", title:"商品名称" },
          { field:"unpickCount",width:"150px", title:"未拣数量" },
          { field:"podName", title: "POD名称" },
          { command: [
              {text:"呼叫pod",click:callPod,width: "150px"}
          ]}
      ]
      $scope.inboundIntefaceDataGridOptions = outboundService.reGrids("",columns,$(document.body).height()-158);

      //点击查询按钮
      $scope.specialSearch = function(){
          $scope.getdata();
      };
      //回车事件
      $scope.searchUnPickMenu = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              $scope.getdata();
          }
      };
      //呼叫pod
      function callPod(e){

          e.preventDefault();
          var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

          $scope.pname = dataItem.podName.substring(0,8);
          $scope.pface = dataItem.podName.substring(8,9);
          $scope.stationname = dataItem.stationName;

          unpickmenuService.isCallPod($scope.pname,$scope.pface,$scope.stationname,function (data) {
              var win = $("#mushinyWindow").data("kendoWindow");
              commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                  setTimeout(function(){
                      $("#warnContent").html(data.message)
                  }, 200);
              }});
          });
      }
      //查询数据
      $scope.getdata = function () {
          unpickmenuService.searchUnpickMenuData($scope.stationname, function (data) {
              var grid = $("#inboundIntefaceContainer").data("kendoGrid");
              grid.setDataSource(new kendo.data.DataSource({data:data}));
          });
      }
      //初始化方法
      function initPage() {
          $scope.getdata();
      };

      initPage();


  });
})();