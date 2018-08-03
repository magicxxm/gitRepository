/**
 * Created by frank.zhou on 2017/05/02.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("workstationCtl", function ($scope, $rootScope, $window, commonService, masterService, workstationService) {
        // ===================================================workstation====================================================
        $window.localStorage["currentItem"] = "workstation";
        $rootScope.toWardSource = ["0", "90", "180", "270"];
        $rootScope.pickOrPackSource = ["PICK", "PACK"];
        $rootScope.labelController1;//选择的标签控制器
        $rootScope.flag = false;//是否过滤
        $rootScope.filterData = [];//过滤后的数组
        $rootScope.workstationTypeSource = masterService.getDataSource({
            key: "getWorkstationType",
            text: "name",
            value: "id"
        });
        $rootScope.sectionSource = masterService.getDataSource({key: "getSection", text: "name", value: "id"});
        $rootScope.pickPackWallSource = masterService.getDataSource({
            key: "getPickPackWall",
            text: "name",
            value: "id"
        });
        $rootScope.labelControllerSource = masterService.getDataSource({
            key: "getLabelController",
            text: "name",
            value: "id"
        });

        $rootScope.changeSection = function (sectionId) {
            workstationService.getNodeBySectionId(sectionId, function (datas) {
                var comboBox = $("#placeMark").data("kendoComboBox");
                comboBox.value("");
                comboBox.setDataSource(new kendo.data.DataSource({data: datas}));
            });
        };
        //强制退出
        $rootScope.exit = function () {
            debugger;
            var grid = $("#workstationGrid").data("kendoGrid");
            var rows = grid.select();
            if (rows.length) {
                commonService.dialogMushiny($scope.window, {
                    url:"modules/common/templates/exitWindow.html",
                    open: function (win) {
                        $rootScope.exitSure = function () {
                            var rowData = grid.dataItem(rows[0]);
                            workstationService.exitWorkStation(rowData.id, function () {
                                win.close();
                                grid.dataSource.read(); // 刷新表格
                            });
                        };
                    }
                });
            }
        };
        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.workstationRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                field: "workstationType.name",
                template: "<a ui-sref='main.workstation_type'>#: workstationType.name # </a>",
                headerTemplate: "<span translate='WORKSTATION_TYPE'></span>"
            },
            {
                field: "pickPackWall",
                headerTemplate: "<span translate='PICK_PACK_WALL'></span>",
                template: function (item) {
                    return item.pickPackWall ? item.pickPackWall.name : "";
                }
            },
            {field: "pickOrPack", headerTemplate: "<span>拣货还是包装</span>"},
            {field: "workingFaceOrientation", headerTemplate: "<span translate='WORKING_FACE_ORIENTATION'></span>"},
            {field: "placeMark", headerTemplate: "<span translate='PLACE_MARK'></span>"},
            {field: "stopPoint", headerTemplate: "<span translate='STOP_POINT'></span>"},
            {field: "scanPoint", headerTemplate: "<span translate='SCAN_POINT'></span>"},
            {field: "bufferPoint", headerTemplate: "<span translate='BUFFER_POINT'></span>"},
            {
                field: "section", headerTemplate: "<span translate='SECTION'></span>", template: function (item) {
                return item.section ? item.section.name : "";
            }
            },
            {field: "callPod", headerTemplate: "<span translate='IS_CALL_POD'></span>"},
            {field: "stationName", headerTemplate: "<span>工作站</span>"},
            {
                field: "useName", headerTemplate: "<span>操作人</span>"
            }
        ];
        $scope.workstationGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("workstation")
        });

        // =================================================workstationPosition===============================================
        // workstationPosition-column
        var workstationPositionColumns = [
            {field: "positionNo", headerTemplate: "<span translate='POSITION_NO'></span>"},
            {
                field: "positionIndex",
                editor: masterService.numberEditor,
                headerTemplate: "<span translate='POSITION_INDEX'></span>"
            },
            {
                field: "digitalLabel", editor: function (container, options) {
                //重新定义下拉框
                $('<input id="digitalLabelId" name="' + options.field + '" />')
                    .appendTo(container)
                    .kendoComboBox({
                        dataTextField: "name",
                        dataValueField: "id"
                    });
                // 标签控制器获取id
                for (var i = 0, ids = []; i < $rootScope.labelController1.length; i++) ids.push($rootScope.labelController1[i].id);
                // 取所有电子标签
                workstationService.getDigitalLabelByLabel(ids, function (data) {
                    $rootScope.flag1 = true;//点击了新增不需要重新获取数据
                    var grid = $("#digitalLabelId").data("kendoComboBox");
                    //绑定改变事件
                    grid.bind("change", function (e) {
                        $rootScope.flag = true;//是否过滤
                        if (!$rootScope.flag1) {
                            workstationService.getDigitalLabelByLabel(ids, function (data1) {
                                $scope.digitalLabelChange(data1); //电子标签改变后过滤数据
                            });
                        } else {
                            $scope.digitalLabelChange(data); //电子标签改变后过滤数据
                        }
                    });
                    if (!$rootScope.flag) {
                        grid.setDataSource(new kendo.data.DataSource({data: data}));
                    } else {
                        grid.setDataSource(new kendo.data.DataSource({data: $rootScope.filterData}));
                    }
                });
            }, headerTemplate: "<span translate='DIGITAL_LABEL'></span>", template: function (item) {
                return item.digitalLabel ? item.digitalLabel.name : "";
            }
            }
        ];
        $rootScope.workstationPositionGridOptions = masterService.editGrid({
            height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 11 - 10 - 20 - 20 - 40),
            columns: workstationPositionColumns
        });

    }).controller("workstationCreateCtl", function ($scope, $rootScope, $state, masterService) {
        // 选择标签控制器
        $scope.changeLabelController = function (labelController) {
            if (!labelController.length) {
                $scope.digitalLabelSource = [];
                return;
            }
            $rootScope.labelController1 = labelController;
        };
        //选择标签后的过滤
        $rootScope.digitalLabelChange = function (data) {
            $rootScope.flag1 = false;//为了判断下次有没有点击新增
            var newData = $("#workstationPositionGrid").data("kendoGrid").dataSource.data();
            for (var i = 0; i < newData.length; i++) {
                var name = newData[i].digitalLabel.name;
                for (var j = 0; j < data.length; j++) {
                    var name1 = data[j].name;
                    if (data[j].name === name) {
                        data.splice(j, 1);
                    }
                }
            }
            $rootScope.filterData = data;
        };

        $scope.fixedScanner = "true";
        $scope.isCallPod = "false";
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var workstationPositionGrid = $("#workstationPositionGrid").data("kendoGrid"),
                    datas = workstationPositionGrid.dataSource.data();
                for (var i = 0, details = []; i < datas.length; i++) {
                    var data = datas[i];
                    details.push({
                        "positionNo": data.positionNo,
                        "positionIndex": data.positionIndex,
                        "digitalLabelId": data.digitalLabel ? data.digitalLabel.id : ""
                    });
                }
                masterService.create("workstation", {
                    "name": $scope.name,
                    "typeId": $scope.workstationType ? $scope.workstationType.id : "",
                    "pickPackWallId": $scope.pickPackWall ? $scope.pickPackWall.id : "",
                    "pickOrPack": $scope.pickOrPack,
                    "fixedScanner": $scope.fixedScanner,
                    "workingFaceOrientation": parseInt($scope.workingFaceOrientation),
                    "placeMark": $scope.placeMark.placeMark,
                    "stopPoint": $scope.stopPoint,
                    "scanPoint": $scope.scanPoint,
                    "bufferPoint": $scope.bufferPoint,
                    "sectionId": $scope.section ? $scope.section.id : null,
                    "callPod": $scope.isCallPod,
                    "positions": details
                }, function () {
                    $state.go("main.workstation");
                });
            }
        };
    }).controller("workstationUpdateCtl", function ($scope, $rootScope, $state, $stateParams, masterService) {
            // 选择标签控制器
            $scope.changeLabelController = function (labelController) {
                //清除position数据
                // var grid = $("#workstationPositionGrid").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: null}));
                if (!labelController.length) {
                    $scope.digitalLabelSource = [];
                    return;
                }
                $rootScope.labelController1 = labelController;
            };

            //选择标签后的过滤
            $rootScope.digitalLabelChange = function (data) {
                $rootScope.flag1 = false;//为了判断下次有没有点击新增
                var newData = $("#workstationPositionGrid").data("kendoGrid").dataSource.data();
                for (var i = 0; i < newData.length; i++) {
                    if (newData[i].digitalLabel == null) {
                    } else {
                        var name = newData[i].digitalLabel.name;
                        for (var j = 0; j < data.length; j++) {
                            var name1 = data[j].name;
                            if (data[j].name === name) {
                                data.splice(j, 1);
                            }
                        }
                    }
                }
                $rootScope.filterData = data;
            }
            ;
            masterService.read("workstation", $stateParams.id, function (data) {
                for (var k in data) {
                    if (data[k] === true) data[k] = "true";
                    else if (data[k] === false) data[k] = "false";
                    $scope[k] = data[k];
                }
                var grid = $("#workstationPositionGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
            });

            $scope.validate = function (event) {
                event.preventDefault();
                if ($scope.validator.validate()) {
                    var workstationPositionGrid = $("#workstationPositionGrid").data("kendoGrid"),
                        datas = workstationPositionGrid.dataSource.data();
                    for (var i = 0, details = []; i < datas.length; i++) {
                        var data = datas[i];
                        details.push({
                            "id": data.id || null,
                            "positionNo": data.positionNo,
                            "positionIndex": data.positionIndex,
                            "digitalLabelId": data.digitalLabel ? data.digitalLabel.id : ""
                        });
                    }
                    masterService.update("workstation", {
                        "id": $scope.id,
                        "name": $scope.name,
                        "typeId": $scope.workstationType ? $scope.workstationType.id : "",
                        "workingFaceOrientation": parseInt($scope.workingFaceOrientation),
                        "pickPackWallId": $scope.pickPackWall ? $scope.pickPackWall.id : "",
                        "pickOrPack": $scope.pickOrPack,
                        "placeMark": $scope.placeMark.placeMark ? $scope.placeMark.placeMark : $scope.placeMark,
                        "stopPoint": $scope.stopPoint,
                        "scanPoint": $scope.scanPoint,
                        "bufferPoint": $scope.bufferPoint,
                        "sectionId": $scope.section ? $scope.section.id : null,
                        "callPod": $scope.callPod,
                        "positions": details
                    }, function () {
                        $state.go("main.workstation");
                    });
                }
            };
        }
    ).controller("workstationReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("workstation", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = data[k];
            }
            var grid = $("#workstationPositionGrid").data("kendoGrid");
            grid.setOptions({"editable": false});
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });
    });
})();