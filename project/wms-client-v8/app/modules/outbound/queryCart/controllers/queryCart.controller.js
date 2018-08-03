/**
 * Created by feiyu.pan on 2017/4/24.
 * Updated by feiyu.pan on 2017/5/31
 */
(function () {
  'use strict';
  angular.module("myApp").controller("queryCartCtl",function ($scope,outboundService,$stateParams,queryCartService) {
    var flag = false;
    if($stateParams.params!==""){
        var params=angular.fromJson($stateParams.params);
        $scope.searchOption=params.sortCode;//获取查询信息
        $scope.sortCode = params.sortCode;
        $scope.deliverTime = params.deliverTime;
        $scope.state = params.state;
        $scope.shippingDate = params.shippingDate;
        $scope.cartState = params.cartState;
        $scope.startTime = params.startTime;
        $scope.endTime = params.endTime;
        flag = true;
    }
      console.log("queryshipid--->"+$stateParams.shipId);
    console.log("scope.sortCode-->"+$scope.sortCode+"/scope.deliverTime-->"+$scope.deliverTime+"/scope.state-->"+$scope.state);
    var columns=[
      {field:"sortCode",headerTemplate:"<span translate='Sort Code'></span>"},
      {field:"ciperNo",headerTemplate:"<span translate='笼车号码'></span>",template:"<a ui-sref='main.cart_query_shipment({cartName:dataItem.ciperNo})'>#:ciperNo#</a>"},
      {field:"ciperShipmentNum",headerTemplate:"<span translate='笼车内Shipment数量'></span>"},
      {field:"state",headerTemplate:"<span translate='STATE'></span>"}];
    $scope.cartGridOptions=outboundService.reGrids("",columns,$(document.body).height()-158);
    //搜索
    $scope.search=function () {
      if($scope.searchOption!=="") {
        queryCartService.searchQueryCartData($scope.searchOption, function (data) {
          var grid = $("#cartGrid").data("kendoGrid");
            grid.setOptions({selectable: true, allowCopy: true});
          grid.setDataSource(new kendo.data.DataSource({data:data}));
        });
      }
    };

      $scope.initPage=function () {
          queryCartService.getQueryCartData($scope.sortCode,$scope.deliverTime,$scope.state,$scope.shippingDate,$scope.cartState,$scope.startTime,$scope.endTime, function (data) {
              var grid = $("#cartGrid").data("kendoGrid");
              grid.setOptions({selectable: true, allowCopy: true});
              grid.setDataSource(new kendo.data.DataSource({data:data}));
              $scope.searchOption='';//获取查询信息
              $scope.sortCode = '';
              $scope.deliverTime = '';
              $scope.state = '';
              $scope.shippingDate = '';
              $scope.cartState = '';
              $scope.startTime = '';
              $scope.endTime = '';
              flag = false;
          });
      };
      if(flag){
          $scope.initPage();
      }
  })
})();