/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("processPathTypeCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

        $window.localStorage["currentItem"] = "processPathType";
        $rootScope.pickFlowSource = ["PICK_TO_PACK", "PICK_TO_TOTE", "PICK_SINGLE", "PICK_MULTIPLE"];
        $rootScope.pickWaySource = ["One Person Per Batch", "Multiple Person Per Batch"];
        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.processPathTypeRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {field: "pickFlow", headerTemplate: "<span>拣货流程</span>"},
            {field: "pickWay", headerTemplate: "<span>拣货方式</span>"},
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.processPathTypeGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("processPathType")
        });

    }).controller("processPathTypeCreateCtl", function ($scope, $state, masterService) {
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("processPathType", {
                    "name": $scope.name,
                    "pickFlow": $scope.pickFlow,
                    "pickWay": $scope.pickWay,
                    "description": $scope.description
                }, function () {
                    $state.go("main.process_path_type");
                });
            }
        };
    }).controller("processPathTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("processPathType", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("processPathType", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "pickFlow": $scope.pickFlow,
                    "pickWay": $scope.pickWay,
                    "description": $scope.description
                }, function () {
                    $state.go("main.process_path_type");
                });
            }
        };
    }).controller("processPathTypeReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("processPathType", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
    });
})();