/**
 * Created by feiyu.pan on 2017/4/24.
 * Updated by feiyu.pan on 2017/5/31
 */
(function () {
  'use strict';
  
  angular.module("myApp").controller("cartQueryShipmentCtl",function ($scope,outboundService,$stateParams,cartQueryShipmentService) {
    $scope.searchOption='';//获取查询的小车名称
      $scope.cartName = $stateParams.cartName;
      console.log("$scope.cartName-->"+$scope.cartName);
      var flag = false;
      if($scope.cartName!==""){
          flag = true;
      }
    var columns=[
      {field:"sortCode",headerTemplate:"<span translate='Sort Code'></span>"},
      {field:"shipmentId",headerTemplate:"<span translate='shipment ID'></span>",template:"<a ui-sref='main.shipment_detail({shipment:dataItem.shipmentId})'>#:shipmentId#</a>"},
      {field:"moveDate",headerTemplate:"<span translate='移包裹时间'></span>",template:
          function (item) {
              if(item.moveDate===""||item.moveDate===undefined||item.moveDate===null){
                  return "";
              }else{
                  return kendo.format("{0:yyyy/MM/dd HH:mm:ss}",kendo.parseDate(item.moveDate))===null?"":kendo.format("{0:yyyy/MM/dd HH:mm:ss}",kendo.parseDate(item.moveDate));
              }
          }}];
    $scope.cartShipmentGridOptions=outboundService.reGrids("",columns,$(document.body).height()-158);
    //搜索
    $scope.search=function () {
      if($scope.searchOption!=="") {
        cartQueryShipmentService.searchCartQueryShipmentData($scope.searchOption, function (data) {
          var grid = $("#cartShipmentGrid").data("kendoGrid");
            grid.setOptions({selectable: true, allowCopy: true});
          grid.setDataSource(new kendo.data.DataSource({data:data}))
        })
      }
    };
      $scope.initPage=function () {
          cartQueryShipmentService.getCartQueryShipmentData($scope.cartName, function (data) {
              var grid = $("#cartShipmentGrid").data("kendoGrid");
              grid.setOptions({selectable: true, allowCopy: true});
              grid.setDataSource(new kendo.data.DataSource({data:data}));
              flag = false;
          });
      };
      if(flag){
          $scope.initPage();
      }
  })
})();