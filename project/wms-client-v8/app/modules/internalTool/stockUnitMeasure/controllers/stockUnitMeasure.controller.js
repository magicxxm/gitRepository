/**
 * Created by 123 on 2017/11/8.
 */

(function () {
    'use strict';

    angular.module('myApp').controller("stockUnitMeasureCtl", function ($scope, $rootScope, $window, $state, commonService,stockUnitMeasureService,internalToolService) {

        /**********************************首页表格****************************************************/
        var stockUnitColumns = [
            {
                field: "itemNo",
                width: 60,
                template: "<a ui-sref='main.item_query({id:dataItem.itemNo,name:dataItem.client})'>#: itemNo # </a>",
                headerTemplate: "<span translate='ITEM_NO'></span>"
            },
            {
                field: "skuNo",
                width: 50,
                headerTemplate: "<span translate='SKU_NO'></span>"
            },
            {field: "name", width: 200, headerTemplate: "<span>商品名称</span>"},
            {field: "client", width: 40, headerTemplate: "<span translate='CLIENT'></span>"},
            {field: "amountUse", width: 45, headerTemplate: "<span>可用数量</span>"},
            {field: "amountReserve", width: 45, headerTemplate: "<span>锁定数量</span>"},
            {field: "amountDamage", width: 45, headerTemplate: "<span>残损数量</span>"},
            {field: "amountPending", width: 45, headerTemplate: "<span>待调查数量</span>"},
           // {field: "amountTotal", width: 45, headerTemplate: "<span>总数量</span></span>"}
            {field: "amountTotal", width: 45, headerTemplate: "<span>总数量</span><span>({{stockAmount}})</span>",template:function(item){
                $scope.stockAmount=item.stockAmount;
                return item.amountTotal;
            }}
        ];

        //初始化页面
        function initPage() {
           /* stockUnitMeasureService.getAllStockUnit(function (data) {
                $scope.stockGridOptions = commonService.gridMushiny({
                    columns: stockUnitColumns,
                    dataSource: data,
                    height:$(document.body).height() - 168
                });
            },function (data) {
                $scope.stockGridOptions = commonService.gridMushiny({
                    columns: stockUnitColumns,
                    dataSource: [],
                    height:$(document.body).height() - 168
                });
            })*/
            var url="internal-tool/stockUnit-measures";
            $scope.stockGridOptions = commonService.gridMushiny({
                columns: stockUnitColumns,
                dataSource: internalToolService.getGridDataSourceSpecial(url),
                height:$(document.body).height() - 180
            });
        }

        $scope.export = function () {
            $state.go("main.stockunit_export");
        };

        initPage();

        //回车搜索
        $scope.stockUnitSearch = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.search();
            }
        };

        //查询
        $scope.search = function () {
            if($scope.param==undefined || $scope.param==null || $scope.param=="")
                initPage();
            else
                stockUnitMeasureService.getByParamSearch($scope.param,function (data) {
                    $scope.stockGridOptions = commonService.gridMushiny({
                        columns: stockUnitColumns,
                        dataSource: data,
                        height:$(document.body).height() - 180
                    });
                    setTimeout(function () {
                        var grid = $("#stockGrid").data("kendoGrid");
                        grid.setDataSource(new kendo.data.DataSource({data: data}));
                    }, 100);
                    $scope.param=""; var searchAmount=0;
                    for(var i=0;i<data.length;i++){
                        searchAmount=searchAmount+data[i].amountTotal;
                    }
                    $scope.stockAmount=searchAmount;
                    setTimeout(function () {$("#paramId").focus();},600);
                })
        };

    }).controller("stockUnitMeasureDetailCtl",function ($stateParams,$scope, $rootScope, $window, $state,
                                                        internalToolService,commonService,stockUnitMeasureService) {

        var inventoryColumns = [
            {field: "storageLocationName", headerTemplate: "<span translate='容器'></span>"},
            {field: "amount", headerTemplate: "<span translate='COUNT'></span><span>({{inventoryTotalNum}})</span>"},
            {field: "inventoryState", headerTemplate: "<span translate='STATE'></span>"},
            {field: "shipmentNo", headerTemplate: "<span translate='SHIPMENT_NO'></span>"},
            {field: "clientName", headerTemplate: "<span translate='CLIENT'></span>"},
            {field: "useNotAfter", headerTemplate: "<span translate='到期日期'></span>"}];

        var itemNo = $stateParams.id;
        var client = $stateParams.name;
        function init() {
            //获取商品信息
            stockUnitMeasureService.getItemdata(itemNo,client,function (data) {
                $scope.itemNo = data.itemNo;
                $scope.skuNo = data.skuNo;
                $scope.itemName = data.name;
             },function (data) {

             });

            //获取商品的库存信息
            stockUnitMeasureService.getItemRecords(itemNo,function (data) {
                //统计总数
                $scope.inventoryTotalNum = 0;
                for (var i = 0; i < data.length; i++) {
                    $scope.inventoryTotalNum += data[i].amount;
                }
                $scope.stockItemGridOptions = commonService.gridMushiny({
                    columns: inventoryColumns,
                    dataSource: data,
                    height:$(document.body).height() - 213
                });
            },function (data) {
                $scope.stockItemGridOptions = commonService.gridMushiny({
                    columns: inventoryColumns,
                    dataSource: [],
                    height:$(document.body).height() - 213
                });
            });
        };

        init();

    }).controller("stockUnitMeasureExportCtl", function ($scope, $rootScope, $state, Excel, masterService, commonService, stockUnitMeasureService) {
        var exportColumns = [
            {field: "itemNo", width: 60, headerTemplate: "<span translate='ITEM_NO'></span>"},
            {field: "name", width: 200, headerTemplate: "<span>商品名称</span>"},
            {field: "amountTotal", width: 50, headerTemplate: "<span>总数量</span>"},
            {field: "warehouse", width: 50, headerTemplate: "<span>仓库</span>"},
            {field: "client", width: 50, headerTemplate: "<span translate='CLIENT'></span>"}
        ];
        stockUnitMeasureService.getStockUnits(function (datas) {
            $scope.stockUnitMeasureExportGridOptions = commonService.gridMushiny1({
                height: $(document.body).height() - 135,
                columns: exportColumns,
                dataSource: datas
            });
        });
        var columnsName = ["唯一编号","商品名称","总数量","仓库","客户"];
        //列宽
        var cellWidths = [200,450,100,150,150];
        $scope.OKExport = function () {
            Excel.exportToExcel("#stockUnitMeasureExportGrid", "库存", cellWidths, columnsName);
        };
    });
})();
