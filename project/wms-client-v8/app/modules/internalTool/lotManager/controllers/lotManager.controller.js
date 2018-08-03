/**
 * Created by 123 on 2017/11/8.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("lotManagerCtl", function ($scope, $rootScope, $window, $state, commonService,lotManagerService,internalToolService) {

        /**********************************首页表格****************************************************/
        var lotStockUnitColumns = [
            {
                field: "itemNo",
                width: 50,
                // template: "<a ui-sref='main.item_query({id:dataItem.itemNo,name:dataItem.client})'>#: itemNo # </a>",
                headerTemplate: "<span translate='ITEM_NO'></span>"
            },
            {
                field: "sku",
                width: 50,
                headerTemplate: "<span translate='SKU_NO'></span>",
            },
            {field: "itemDataName", width: 170, headerTemplate: "<span translate='商品名称'></span>"},
            {field: "storageLocationName",width: 35, headerTemplate: "<span translate='容器'></span>"},
            {field: "amount", width: 25,headerTemplate: "<span translate='COUNT'></span>"},
            {field: "inventoryState", width: 30,headerTemplate: "<span translate='STATE'></span>"},
            {field: "shipmentNo", width: 50,headerTemplate: "<span translate='SHIPMENT_NO'></span>"},
            {field: "clientName",width: 20, headerTemplate: "<span translate='CLIENT'></span>"},
            {field: "useNotAfter",width: 40, headerTemplate: "<span translate='到期日期'></span>"},
            {field: "days", width: 30,headerTemplate: "<span translate='剩余有效期'></span>"}
        ];
        setTimeout(function () {$("#paramId").focus();},600);

        //回车搜索
        $scope.lotSerach = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.search();
            }
        };

        //查询
        $scope.search = function () {
            setTimeout(function () {
                var grid = $("#lotManagerGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: null}));
            }, 100);
            lotManagerService.getByParam($scope.param,function (data) {
                $scope.lotManagerGridGridOptions = commonService.gridMushiny({
                    columns: lotStockUnitColumns,
                    dataSource: data,
                    height:$(document.body).height() - 213
                });
                setTimeout(function () {
                    var grid = $("#lotManagerGrid").data("kendoGrid");
                    grid.setDataSource(new kendo.data.DataSource({data: data}));
                }, 100);
            })
        };

        //初始化页面
        function initPage() {
           /* lotManagerService.getAllStockUnit(function (data) {
                $scope.lotManagerGridGridOptions = commonService.gridMushiny({
                    columns: lotStockUnitColumns,
                    dataSource: data,
                    height:$(document.body).height() - 213
                });
            },function (data) {
                $scope.lotManagerGridGridOptions = commonService.gridMushiny({
                    columns: lotStockUnitColumns,
                    dataSource: [],
                    height:$(document.body).height() - 213
                });
            })*/
            var url="internal-tool/findStock";
            $scope.lotManagerGridGridOptions = commonService.gridMushiny({
                columns: lotStockUnitColumns,
                dataSource: internalToolService.getGridDataSourceSpecial(url),
                height:$(document.body).height() - 160
            });
        }

        initPage();
    })
})
();