/**
 * Created by zhihan.dong on 2017/04/17.
 * updated by zhihan.dong on 2017/05/02.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("capacityCtl", function ($scope, $timeout, $rootScope, $window, $state, commonService, reportService, capacityService) {
        ///////////////////////////////////////////capacity
        $scope.capacityType = 'total';
        $window.localStorage["currentItem"] = "capacity";
        var columns = [{

            field: "podName",
            template: "<a ui-sref='main.capacity_side({id:dataItem.podName})'>#: podName # </a>",
            headerTemplate: "<span translate='POD'></span>"
        },

            {
                field: "podType",
                width: "120px",
                headerTemplate: "<span translate='POD_TYPE'></span>"
            },
            {
                field: "location",
                headerTemplate: "<span translate='LOCATION'></span>"
            },
            ///units
            {
                field: "itemTotalAmount",
                headerTemplate: "<span translate='UNITS'></span>"
            },
            {
                field: "skuNoUnit",
                headerTemplate: "<span translate='SKU_NO_QUANTITY'></span>"
            },
            {
                field: "itemNoUnit",
                headerTemplate: "<span translate='SKU_ID_QUANTITY'></span>"
            },
            {
                field: "itemTotalM3",
                headerTemplate: "<span translate='INVENTORY_VOLUME'></span>"
            },
            {
                field: "binTotalM3",
                headerTemplate: "<span translate='TOTAL_BIN_VOLUME'></span>"
            },
            {
                field: "binNotNullTotalM3",
                headerTemplate: "<span translate='USED_BIN_VOLUME'></span>"
            },
            {
                field: "binNullTotalM3",
                headerTemplate: "<span translate='EMPTY_BINS_VOLUME'></span>"
            },
            {
                field: "useUtilization",
                headerTemplate: "<span translate='USE_BIN_UTILIZATION'></span>"
            },
            {
                field: "totalUtilization",
                headerTemplate: "<span translate='TOTAL_BIN_UTILIZATION'></span>"
            },
            {
                field: "binTotal",
                headerTemplate: "<span translate='TOTAL_BINS'></span>"
            },
            {
                field: "binNotNullTotal ",
                headerTemplate: "<span translate='USED_BINS'></span>"
            },
            {
                field: "binNullTotal",
                headerTemplate: "<span translate='EMPTY_PICK_BINS'></span>"
            },
            {
                field: "bufferBinNullTotal",
                headerTemplate: "<span translate='EMPTY_BUFFER_BINS'></span>"
            },
            {
                field: "binUtilization",
                headerTemplate: "<span translate='PICK_BIN_OCCUPIED'></span>"
            },
            {
                field: "bufferBinUtilization",
                headerTemplate: "<span translate='BUFFER_BIN_OCCUPIED'></span>"
            },
        ];

        //$scope.capacity_ExSDSource = ["2017-03-18 01:00:00", "2017-03-18 02:00:00", "2017-3-18 3:00", "2017-3-18 4:00", "2017-3-18 5:00", "2017-3-18 6:00", "2017-3-18 7:00", "2017-3-18 8:00", "2017-3-18 9:00", "2017-3-18 10:00", "2017-3-18 11:00", "2017-3-18 12:00", "2017-3-18 13:00", "2017-3-18 14:00", "2017-3-18 15:00", "2017-3-18 16:00", "2017-3-18 17:00", "2017-3-18 18:00", "2017-3-18 19:00", "2017-3-18 20:00", "2017-3-18 21:00", "2017-3-18 22:00", "2017-3-18 23:00"]
        $scope.capacityGridOptions = reportService.editGrid({
            columns: columns,
            editable: false,
            height: $(document.body).height() - $("#capacitybar").height() - 175,
        });
        $scope.queryCapcityPods = function () {
            $timeout(function () {
                capacityService.queryCapcityPods(function (data) {
                    var capacityGridId = $("#capacityGRID").data("kendoGrid");
                    capacityGridId.setOptions({
                        dataSource: data,
                        height: $(document.body).height() - $("#capacitybar").height() - 175,
                    });
                });
            })
        }


        ///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //total

        $scope.capacityTotalGridOptions = reportService.editGrid({

            editable: false,
            height: $(document.body).height() - $("#capacitybar").height() - 170,
        });

        $scope.totalQuery = function () {
            $timeout(function () {
                capacityService.queryCapacityTotal(function (data) {
                    var capacityGridId = $("#capacityTotalGRID").data("kendoGrid");
                    capacityGridId.setOptions({
                        dataSource: initDataSource(data),
                        editable: false,
                        columns: totalColumns(data),
                    });
                });
            }, 0);
        }


        //初始化数据源
        function initDataSource(data) {
            var dataSource = [];
            for (var i = 0; i < data.length; i++) {
                var source = {};
                source.zoneName = data[i].zoneName;
                source.totalUtilization = data[i].totalUtilization;
                var arr = data[i].binTypes;
                arr.map(function (index) {
                    source['_' + index.binType.replace(/\*/g,"_").replace(/\-/g,"_")] = index.binTypeUtilization;
                });
                dataSource.push(source);
            }
            return dataSource;
        }

        //初始化字段
        function totalColumns(datas) {
            debugger;
            var columns = [];
            columns.push({
                field: "zoneName",
                headerTemplate: "<span translate='AREA'></span>"
            });
            var utilization = [];
            datas.map(function (data) {
                for (var i = 0; i < data.binTypes.length; i++) {
                    var flag = true;
                    utilization.map(function (item) {

                        if (item.field == '_' + data.binTypes[i].binType.replace(/\*/g,"_").replace(/\-/g,"_")) {
                            flag = false;
                        }
                    })
                    if (flag) {
                        utilization.push({
                            field: '_' + data.binTypes[i].binType.replace(/\*/g,"_").replace(/\-/g,"_"),
                            headerTemplate: "<span >" + data.binTypes[i].binType + "</span>"
                        });
                    }
                }
            })

            columns.push({
                title: "utilization",
                columns: utilization
            });
            columns.push({
                field: 'totalUtilization',
                headerTemplate: "<span translate='TOTAL'></span>"
            });
            return columns;
        }
        $scope.totalQuery();
    });
})();