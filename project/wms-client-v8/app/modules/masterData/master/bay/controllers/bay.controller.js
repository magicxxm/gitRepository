/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("bayCtl", function ($scope, $window, $rootScope, $state, bayService, commonService, masterService) {
        $window.localStorage["currentItem"] = "bay";
        var columns = [
                {
                    field: "name",
                    template: "<a ui-sref='main.bayRead({id:dataItem.id})'>#: name # </a>",
                    headerTemplate: "<span translate='NAME'></span>"
                },
                {
                    field: "podType.name",
                    template: "<a ui-sref='main.pod_type'>#: podType.name # </a>",
                    headerTemplate: "<span translate='POD_TYPE'></span>"
                },
                {field: "aisle", headerTemplate: "<span translate='AISLE'></span>"},
                {field: "bayIndex", headerTemplate: "<span translate='BAY_INDEX'></span>"}
            ]
        ;
        $scope.bayGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("bay")
        });
        $rootScope.podTypeSource = masterService.getDataSource({key: "getPodType", text: "name", value: "id"});

        $rootScope.changeClient = function (clientId) {
            bayService.getZone(clientId, function (zones) {
                var zoneComboBox = $("#zone").data("kendoComboBox");
                zoneComboBox.value("");
                zoneComboBox.setDataSource(new kendo.data.DataSource({data: zones}));
            });
            //
            bayService.getArea(clientId, function (areas) {
                var areaComboBox = $("#area").data("kendoComboBox");
                areaComboBox.value("");
                areaComboBox.setDataSource(new kendo.data.DataSource({data: areas}));
            });
        };
    }).controller("bayCreateCtl", function ($scope, $state, masterService) {
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("bay", {
                    "zoneId": $scope.zone ? $scope.zone.id : null,
                    "fromAisle": $scope.aisleFrom,
                    "toAisle": $scope.aisleTo,
                    "fromBay": $scope.bayFrom,
                    "toBay": $scope.bayTo,
                    "index": $scope.bayIndex,
                    "podTypeId": $scope.podType ? $scope.podType.id : null,
                    "areaId": $scope.area ? $scope.area.id : null,
                    "clientId": $scope.client ? $scope.client.id : null
                }, function () {
                    $state.go("main.bay");
                });
            }
        }
    }).controller("bayUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("bay", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("bay", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "aisle": $scope.aisle,
                    "index": $scope.bayIndex,
                    "clientId": $scope.client.id,
                    "podTypeId": $scope.podType ? $scope.podType.id : null
                }, function () {
                    $state.go("main.bay");
                });
            }
        }
    }).controller("bayReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("bay", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = data[k];
            }
        });
    });
})();