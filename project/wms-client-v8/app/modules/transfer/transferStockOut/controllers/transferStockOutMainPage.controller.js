/**
 * 调拨出主页面
 * Created by preston.zhang on 2017/8/18.
 */
(function () {
    "use strict";
    angular.module("myApp").controller("transferStockOutMainPageCtl",function ($rootScope,$scope,$state,transOutService) {
        //搜索条件
        $scope.searchOption = '';
        //默认开始时间
        $scope.startTime=kendo.format("{0:yyyy-MM-dd}",new Date());//默认开始时间
        //默认结束时间
        $scope.endTime=kendo.format("{0:yyyy-MM-dd}",new Date(new Date().setDate(new Date().getDate()+1)));//默认结束时间
        //colunms
        var columns=[
            {field:"fromTransferOutNo",width:80,headerTemplate:"<span translate='外部调拨单号'></span>",template:function (item) {
                var fromTransferOutNo = item.fromTransferOutNo;
                var html="<a ng-click='fromTypeJumpToDetail(\""+fromTransferOutNo+"\")'>"+fromTransferOutNo+"</a>";
                return html;
            }},
            {field:"toTransferOutNo",width:100,headerTemplate:"<span translate='调拨发货单号'></span>",template:function (item) {
                var toTransferOutNo = item.toTransferOutNo;
                var html="<a ng-click='toTypeJumpToDetail(\""+toTransferOutNo+"\")'>"+toTransferOutNo+"</a>";
                return html;
            }},
            {field:"state",width:100,headerTemplate:"<span translate='状态'></span>"},
            {field:"transferContainerNum",width:110,headerTemplate:"<span translate='调拨箱数'></span>"},
            {field:"transferNum",width:110,headerTemplate:"<span translate='调拨数量'></span>"},
            {field:"acceptWareHouse",width:110,headerTemplate:"<span translate='接收库房'></span>"},
            {field:"predictShippingDateTime",width:110,headerTemplate:"<span translate='预计发货时间'></span>"},
            {field:"ShippingDateTime",width:80,headerTemplate:"<span translate='发货时间'></span>"},
            {field:"predictArriveDateTime",width:120,headerTemplate:"<span translate='预计到货时间'></span>"}];
        $scope.transferStockOutGridOptions = {dataSource: "", columns: columns};
        //根据条件搜索调拨出页面数据
        $scope.search = function (e) {
            // $state.go("main.transferStockOutDetail");
            transOutService.transOutMainPage($scope.startTime,$scope.endTime,$scope.searchOption,function (data) {
                var grid = $("#transferStockOutGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data:data}));
            });
        };
        $scope.search();
        //外部调拨单号跳转
        $scope.fromTypeJumpToDetail = function (fromTransferOutNo) {
            $state.go("main.transferStockOutDetail",{params:angular.fromJson({fromTransferOutNo:fromTransferOutNo})});
        };
        //调拨发货单号跳转
        $scope.toTypeJumpToDetail = function (toTransferOutNo) {
            $state.go("main.transferStockOutDetail",{params:angular.fromJson({toTransferOutNo:toTransferOutNo})});
        };
    });
})();