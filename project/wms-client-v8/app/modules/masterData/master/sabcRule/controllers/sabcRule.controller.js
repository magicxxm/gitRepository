/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("sabcRuleCtl", function ($scope, $window, $rootScope, $state, commonService, masterService) {

        $window.localStorage["currentItem"] = "sabcRule";

        var columns = [
            {field: "skuTypeName", headerTemplate: "<span>名称</span>"},
            {
                title: "SKU % Range",
                attributes: {style: "text-align:center;"},
                columns: [{
                    field: "fromNo",
                    headerTemplate: "<span>from</span>"
                }, {
                    field: "toNo",
                    headerTemplate: "<span>to</span>"
                }]
            },
            // {field: "fromNo", headerTemplate: "<span>from</span>"},
            {field: "salesPro", headerTemplate: "<span>销量占比</span>"},
            {field: "maxDoc", headerTemplate: "<span>收货DOC </span>"},
            {field: "replenDoc", headerTemplate: "<span>补货DOC</span>"},
            {field: "safelyDoc", headerTemplate: "<span>安全DOC</span>"}
        ];
        $scope.sabcRuleGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("sabcRule")
        });

    }).controller("sabcRuleCreateCtl", function ($scope, $state, masterService) {

        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("sabcRule", {
                        "skuTypeName": $scope.skuTypeName,
                        "fromNo": $scope.fromNo,
                        "toNo": $scope.toNo,
                        "maxDoc": $scope.maxDoc,
                        "replenDoc": $scope.replenDoc,
                        "safelyDoc": $scope.safelyDoc,
                    },
                    function () {
                        $state.go("main.sabc_rule");
                    }
                )
                ;
            }
        };
    }).controller("sabcRuleUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("sabcRule", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = data[k];
            }
            $scope.client = {id: data.clientId};
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("sabcRule", {
                    "id": $scope.id,
                    "skuTypeName": $scope.skuTypeName,
                    "fromNo": $scope.fromNo,
                    "toNo": $scope.toNo,
                    "maxDoc": $scope.maxDoc,
                    "replenDoc": $scope.replenDoc,
                    "safelyDoc": $scope.safelyDoc,
                }, function () {
                    $state.go("main.sabc_rule");
                });
            }
        };
    }).controller("sabcRuleReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("sabcRule", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = data[k];
            }
        });
    });
})();