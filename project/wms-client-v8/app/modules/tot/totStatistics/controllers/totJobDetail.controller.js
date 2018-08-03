/**
 *
 */
(function () {
    'use strict';

    angular.module('myApp').controller("totJobDetailCtl", function ($scope,$stateParams, $rootScope,$timeout, $window, $state,
                                                                    totStatisticsService,totStatisticsDetailService,commonService,totService) {

        $scope.searchParam={employeeCode:totStatisticsDetailService.employeeCode,
            startDate:totStatisticsDetailService.startDate,
            endDate:totStatisticsDetailService.endDate,
            dayDate:totStatisticsDetailService.dayDate,
            clientId:totStatisticsDetailService.clientId,
            warehouseId:totStatisticsDetailService.warehouseId}
        var columnT = [
            {
                field: "tool",
                headerTemplate: "<span translate='TOOL'></span>"
            },
            {
                field: "jobAction",
                headerTemplate: "<span translate='JOB_ACTION'></span>"
            },
            {
                field: "unitType",
                headerTemplate: "<span translate='UNIT_TYPE'></span>"
            },
            {
                field: "size",
                headerTemplate: "<span translate='SIZE'></span>"
            },
            {
                field: "quantity",
                headerTemplate: "<span translate='QUANTITY'></span>"
            },
            {
                field: "entityLock",
                headerTemplate: "<span translate='STATUS_CODE'></span>"
            }
        ];
        var columnD = [
            {
                field: "recordTime",
                headerTemplate: "<span translate='TIME'></span>"
            },
            {
                field: "jobAction",
                width:"160px",
                headerTemplate: "<span translate='JOB_ACTION'></span>"
            },
            {
                field: "skuNo",
                headerTemplate: "<span translate='SKU_NO'></span>"
            },
            {
                field: "newBarcode",
                width:"150px",
                headerTemplate: "<span translate='ITEM_NO'></span>"
            },
            {
                field: "unitType",
                width:"80px",
                headerTemplate: "<span translate='UNIT_TYPE'></span>"
            },
            {
                field: "size",
                width:"60px",
                headerTemplate: "<span translate='SIZE'></span>"
            },
            {
                field: "quantity",
                width:"60px",
                headerTemplate: "<span translate='QUANTITY'></span>"
            },
            {
                field: "fromStoragelocation",
                headerTemplate: "<span translate='ORIGINAL_CONTAINER'></span>"
            },
            {
                field: "toStoragelocation",
                width:"150px",
                headerTemplate: "<span translate='PURPOSE_CONTAINER'></span>"
            },
            {
                field: "clientId",
                width:"90px",
                headerTemplate: "<span translate='CLIENT'></span>"
            },
            {
                field: "tool",
                width:"80px",
                headerTemplate: "<span translate='TOOL'></span>"
            },
            {
                field: "shipmentNo",
                width:"150px",
                headerTemplate: "<span translate='SHIPMENTNO'></span>"
            },
            {
                field: "entityLock",
                width:"70px",
                headerTemplate: "<span translate='STATUS_CODE'></span>"
            }
        ];
        totStatisticsService.getJobTotal(function (data) {
            $scope.totStatisticsJobTotalGridOptions = {
                dataSource: data,
                columns: columnT
                // editable: false,
                // selectable: "row",
                // sortable: true,
                // scrollable: true,
                // pageable: false
            };
        },$scope.searchParam);

        //表格数据初始化
        // totStatisticsService.getJobDetail(function (data) {
        //     $scope.totStatisticsJobDetailGridOptions = {
        //         dataSource: data,
        //         columns: columnD
        //         // editable: false,
        //         // selectable: "row",
        //         // sortable: true,
        //         // scrollable: false,
        //         // pageable: false
        //     };
        // },$scope.searchParam);

        $scope.totStatisticsJobDetailGridOptions = commonService.gridMushiny({
            columns: columnD,
            dataSource: totService.getGridDataSourceByJobDetail($scope.searchParam)});
    });
})();