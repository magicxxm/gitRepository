/**
 * Created by frank.zhou on 2017/04/21.
 * Updated by frank.zhou on 2017/04/24.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("zoneCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

        $window.localStorage["currentItem"] = "zone";
        $rootScope.sectionSource = masterService.getDataSource({key: "getSection", text: "name", value: "id"});
        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.zoneRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {field: "section.name", headerTemplate: "<span>物理区</span>"},
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.zoneGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("zone")
        });
    }).controller("zoneCreateCtl", function ($scope, $state, masterService) {
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("zone", {
                    "name": $scope.name,
                    "description": $scope.description,
                    "clientId": $scope.client ? $scope.client.id : null,
                    "sectionId": $scope.section ? $scope.section.id : null
                }, function () {
                    $state.go("main.zone");
                });
            }
        };
    }).controller("zoneUpdateCtl", function ($scope, $stateParams, $state, masterService) {
        masterService.read("zone", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            $scope.client = {id: data.clientId};
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("zone", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "description": $scope.description,
                    "clientId": $scope.client ? $scope.client.id : null,
                    "sectionId": $scope.section ? $scope.section.id : null
                }, function () {
                    $state.go("main.zone");
                });
            }
        };
    }).controller("zoneReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("zone", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
    });
})();