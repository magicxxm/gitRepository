/**
 * Created by frank.zhou on 2017/04/24.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("podCtl", function ($scope, $rootScope, $window, commonService, podService, masterService) {

        $window.localStorage["currentItem"] = "pod";
        $rootScope.toWardSource = ["0", "90", "180", "270"];
        $rootScope.stateSource = ["Available", "Reserved"];
        $rootScope.podTypeSource = masterService.getDataSource({key: "getPodType", text: "name", value: "id"});
        $rootScope.sectionSource = masterService.getDataSource({key: "getSection", text: "name", value: "id"});

        $rootScope.changeClient = function (clientId) {
            $rootScope.clientId = clientId;
            podService.getArea(clientId, function (areas) {
                var areaComboBox = $("#area").data("kendoComboBox");
                areaComboBox.value("");
                areaComboBox.setDataSource(new kendo.data.DataSource({data: areas}));
            });
        };
        $rootScope.changeSection = function (sectionId) {
            $rootScope.sectionId = sectionId;
            if ($rootScope.clientId !== null) {
                podService.getZone($rootScope.clientId, sectionId, function (zones) {
                    var zoneComboBox = $("#zone").data("kendoComboBox");
                    zoneComboBox.value("");
                    zoneComboBox.setDataSource(new kendo.data.DataSource({data: zones}));
                });
            }
        };

        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.podRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                field: "podType.name",
                template: "<a ui-sref='main.pod_type'>#: podType.name # </a>",
                headerTemplate: "<span translate='POD_TYPE'></span>"
            },
            {
                field: "zone", headerTemplate: "<span translate='ZONE'></span>", template: function (dataItem) {
                return dataItem.zone ? dataItem.zone.name : "";
            }
            },
            {
                field: "section", headerTemplate: "<span translate='SECTION'></span>", template: function (item) {
                return item.section ? item.section.name : "";
            }
            },
            {field: "placeMark", headerTemplate: "<span translate='PLACE_MARK'></span>"},
            {field: "xPos", headerTemplate: "<span>xPos</span>"},
            {field: "yPos", headerTemplate: "<span>xPos</span>"},
            {field: "podIndex", headerTemplate: "<span translate='POD_INDEX'></span>"},

            {field: "toWard", headerTemplate: "<span translate='TO_WARD'></span>"},
            {field: "xPosTar", headerTemplate: "<span translate='X_POS_TAR'></span>"},
            {field: "yPosTar", headerTemplate: "<span translate='Y_POS_TAR'></span>"},

            {field: "addrCodeIdTar", headerTemplate: "<span translate='ADDR_CODE_ID_TAR'></span>"},

            {field: "sellingDegree", headerTemplate: "<span translate='ITEM_SELLING_DEGREE'></span>"},
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.podGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("pod")
        });

    }).controller("podCreateCtl", function ($scope, $state, masterService) {
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("pod", {
                    "zoneId": $scope.zone ? $scope.zone.id : null,
                    "fromPod": $scope.podFrom,
                    "toPod": $scope.podTo,
                    "podTypeId": $scope.podType ? $scope.podType.id : null,
                    "areaId": $scope.area ? $scope.area.id : null,
                    "description": $scope.description,
                    "clientId": $scope.client ? $scope.client.id : null,

                    // "toWard": $scope.toWard,
                    // "xPos": $scope.xPos,
                    // "yPos": $scope.yPos,
                    // "addrCodeIdTar": $scope.addrCodeIdTar,
                    "state": $scope.state,
                    "sectionId": $scope.section ? $scope.section.id : ""
                }, function () {
                    $state.go("main.pod");
                });
            }
        }
    }).controller("podUpdateCtl", function ($scope, $rootScope, $stateParams, $state, podService, masterService, commonService) {
        $rootScope.selectPlaceMark = function () {
            commonService.dialogMushiny($scope.window, {
                title: "<span>选择pod地标点</span>",
                width: 650,
                height: 400,
                url: "modules/masterData/master/pod/templates/selectInWindowPod.html",
                open: function () {
                    podService.getPlaceMark($stateParams.id, function (placeMark) {
                        setTimeout(function () {
                            var grid = $("#queryPodPlaceMarkGrid").data("kendoGrid");
                            grid.setDataSource(new kendo.data.DataSource({data: placeMark}));
                        }, 500);
                    });
                    $rootScope.queryPodPlaceMarkGridOptions = commonService.gridMushiny1({
                        columns: [
                            {field: "placeMark", headerTemplate: "<span translate='PLACE_MARK'></span>"},
                            {field: "xPos", headerTemplate: "<span>xPos</span>"},
                            {field: "yPos", headerTemplate: "<span>yPos</span>"}
                        ],
                        height: 400 - 120
                    });
                    // 确认选择
                    $rootScope.doSelectInWindow = function (win) {
                        var grid = $("#queryPodPlaceMarkGrid").data("kendoGrid"), rows = grid.select();
                        if (!rows.length) return;
                        $rootScope.placeMarkData = grid.dataItem(rows[0]);
                        $scope.xPos = $rootScope.placeMarkData.xPos;
                        $scope.yPos = $rootScope.placeMarkData.yPos;
                        $scope.placeMark = $rootScope.placeMarkData.placeMark;
                        win.close();
                    };
                }
            });
        };
        masterService.read("pod", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            $scope.client = {id: data.clientId};
            $("#toWard").val(data.toWard.toString());

        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("pod", {
                    "id": $scope.id,
                    "podTypeId": $scope.podType ? $scope.podType.id : null,
                    "description": $scope.description,
                    "sectionId": $scope.section ? $scope.section.id : "",
                    "placeMark": $scope.placeMark,
                    "toWard": parseInt($scope.toWard),
                    "xPos": $scope.xPos,
                    "yPos": $scope.yPos,
                    "addrCodeIdTar": $scope.addrCodeIdTar
                }, function () {
                    $state.go("main.pod");
                });
            }
        };
    }).controller("podReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("pod", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
    });
})();