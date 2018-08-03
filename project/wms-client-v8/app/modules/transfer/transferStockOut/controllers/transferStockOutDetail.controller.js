/**
 * 调拨出详情页面
 * Created by preston.zhang on 2017/8/18.
 */
(function () {
    "use strict";
    angular.module("myApp").controller("transferStockOutDetailCtl",function ($rootScope,$stateParams,$scope,transOutService) {
        var no = $stateParams.No;
        $scope.fromTransferStockOutNo = '';
        $scope.toTransferStockOutNo = '';
        $scope.acceptWareHouse = '';
        $scope.shippingDateTime = '';
        $scope.state = '';
        var columnsContainer = [
            {field:"transferContainerNo",width:80,headerTemplate:"<span translate='调拨箱号码'></span>"},
            {field:"outAmount",width:80,headerTemplate:"<span translate='发货数量'></span>"}
            ];
        var columnsContainerDetail = [
            {field:"transferContainerNo",width:80,headerTemplate:"<span translate='调拨箱号码'></span>"},
            {field:"uniqueNo",width:80,headerTemplate:"<span translate='唯一编号'></span>"},
            {field:"itemName",width:80,headerTemplate:"<span translate='商品名称'></span>"},
            {field:"amount",width:80,headerTemplate:"<span translate='数量'></span>"}
            ];
        $scope.transferStockOutContainerSomeGridOptions = {dataSource: "", columns: columnsContainer};
        $scope.transferStockOutDetailGridOptions = {dataSource: "", columns: columnsContainerDetail};
        if(no!==null&&no!==undefined&&no!==''){
            transOutService.transOutDetail(no,function (data) {
                $scope.fromTransferStockOutNo = data.fromTransferStockOutNo;
                $scope.toTransferStockOutNo = data.toTransferStockOutNo;
                $scope.acceptWareHouse = data.acceptWareHouse;
                $scope.shippingDateTime = data.shippingDateTime;
                $scope.state = data.state;
                var containerGrid = $("#transferStockOutContainerSomeGrid").data("kendoGrid");
                containerGrid.setDataSource(new kendo.data.DataSource({data:data.containerList}));

                var containerDetailGrid = $("#transferStockOutContainerDetailGrid").data("kendoGrid");
                containerDetailGrid.setDataSource(new kendo.data.DataSource({data:data.containerDetailList}));
            });
        }
    });
})();