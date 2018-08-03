/**
 * Created by frank.zhou on 2017/05/03.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("packingStationCtl", function ($scope, $rootScope, $window, commonService, masterService) {

        $window.localStorage["currentItem"] = "packingStation";
        $rootScope.packingStationTypeSource = masterService.getDataSource({
            key: "getPackingStationType",
            text: "name",
            value: "id"
        });
        $rootScope.workstationSource = masterService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.packingStationRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                field: "packingStationType.name",
                template: "<a ui-sref='main.packing_station_type'>#: packingStationType.name # </a>",
                headerTemplate: "<span translate='PACKING_STATION_TYPE'></span>"
            },
            {
                headerTemplate: "<span translate='STATE'></span>", template: function (item) {
                return item.user ? "BUSY" : "IDLE";
            }
            },
            {
                field: "user", headerTemplate: "<span translate='OPERATOR'></span>", template: function (item) {
                return item.user ? item.user.username : "";
            }
            },
            {field: "workstation.name", headerTemplate: "<span translate='WORKSTATION'></span>"},
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.packingStationGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("packingStation")
        });

    }).controller("packingStationCreateCtl", function ($scope, $state, masterService) {
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("packingStation", {
                    "name": $scope.name,
                    "typeId": $scope.packingStationType ? $scope.packingStationType.id : null,
                    "workstationId": $scope.workstation ? $scope.workstation.id : null,
                    "description": $scope.description
                }, function () {
                    $state.go("main.packing_station");
                });
            }
        };
    }).controller("packingStationUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("packingStation", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("packingStation", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "typeId": $scope.packingStationType ? $scope.packingStationType.id : null,
                    "workstationId": $scope.workstation ? $scope.workstation.id : null,
                    "description": $scope.description
                }, function () {
                    $state.go("main.packing_station");
                });
            }
        };
    }).controller("packingStationReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("packingStation", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
    });
})();