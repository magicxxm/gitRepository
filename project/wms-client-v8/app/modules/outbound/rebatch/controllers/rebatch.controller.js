/**
 * Created by bian on 2016/11/29.
 */
(function () {
    'use strict';
    angular.module('myApp').controller("rebatchCtl", function ($scope, $rootScope, $state, commonService, outboundService, reBatchService) {
        var step = 1;
        var iptStatus = 0;
        var dataSource = [];
        $scope.name = localStorage["name"];
        $scope.rebatchChildPage = 1;
        $scope.rebatchPage = 1;
        $scope._container = 1;
        $scope.soltContent = false;
        $scope.slot = true;
        $scope.errorSlot = false;
        $scope.tittle = "请扫描容器号码";
        $scope.slotId = "";
        $scope.id = "";
        $scope.stationId = "";
        $scope.batchOperation = function () {
            $scope.rebatchPage = 2;
            iptStatus = 1;
            setTimeout(function () {
                $("#workstationId").focus();
            }, 0); // 首获焦
        };
        //锁定输入框
        $scope.selectStation = function () {
            if (iptStatus == 1) {
                $("#workstationId").focus().select();
            } else if (iptStatus == 2) {
                $("#mainInput").focus().select();
            }
        };

        //扫描工作站
        $scope.workStation = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.messageOperate = "none";
                reBatchService.scanReBatchStations(this.workstation, function (datas) {
                    $scope.reBachStationName = datas.rebatchStationName;
                    $scope.stationId = datas.id;
                    $scope.rebatchPage = 3;
                    setTimeout(function () {
                        $("#mainInput").focus();
                    }, 0);
                    iptStatus = 2;
                    reBatchService.getReBatchInfo(function (datas) {
                        initGrid(datas)
                    });
                }, function (datas) {
                    $scope.workstation = "";
                    $("#workstationId").focus().select();
                    $scope.messageOperate = "show";
                    $scope.message = datas.values[0];
                })
            }
        };


        $scope.inputImformation = function (e) {
            $scope.exsdFlag = 0;
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                //扫描小车
                $scope.container = $scope.imformation;
                if (step == 1) {
                    reBatchService.scanReBatchContainer($scope.container, $scope.reBachStationName, function (data) {
                        $scope.exsdFontColor = "white";
                        step = 2;
                        $scope.tittle = "请扫描Slot号码";
                        $scope.slotNumber = data.rebatchSlotName;
                        $scope.slotColor = data.labelColor;
                        $scope.id = data.id;
                        $scope.mainContent = 2;
                        $scope.Exsd = data.deliveryTimes[0] ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(data.deliveryTimes[0])) : "";
                        $scope.time = (new Date(kendo.parseDate(data.deliveryTimes[0])) - new Date()) / 3600000;
                        if ($scope.time > 12) {
                            $scope.exsdColor = "#0066CD";
                        } else if (($scope.time < 12 && $scope.time > 6) || $scope.time == 12) {
                            $scope.exsdFontColor = "black";
                            $scope.exsdColor = "#66CEFF";
                        } else if (($scope.time < 6 && $scope.time > 3) || $scope.time == 6) {
                            $scope.exsdFontColor = "black";
                            $scope.exsdColor = "#FFFF01";
                        } else if (($scope.time < 3 && $scope.time > 1) || $scope.time == 3) {
                            $scope.exsdColor = "#FF9901";
                        } else if (($scope.time < 1 && $scope.time > 0) || $scope.time == 1) {
                            $scope.exsdColor = "#FF7C81";
                        } else if ($scope.time < 0 || $scope.time == 0) {
                            $scope.exsdColor = "#FF0000";
                        }
                        //  $scope.slotColor = "#3E70CA";
                        $scope.soltContent = true;
                    }, function () {
                        $scope.mainContent = 1;
                    });
                    //扫描Solt
                } else if (step == 2) {
                    if ($scope.container.toUpperCase() == $scope.slotNumber.toUpperCase()) {
                        var data = {
                            id: $scope.id,
                            rebatchSlotName: $scope.slotNumber,
                            rebatchStationId: $scope.stationId
                        };
                        reBatchService.scanReBatchSlot($scope.id, data, function (data) {
                            if (data) {
                                //获取所以slot的信息
                                reBatchService.getReBatchInfo(function (datas) {
                                    for (var k = 0; k < datas.length; k++) {
                                        if ($scope.slotNumber == datas[k].rebatchSlotName) {
                                            //判断总数和扫描数是否相等
                                            if (datas[k].numPosition == datas[k].numPositionScanned && datas[k].numPosition != 0) {
                                                $scope.mainContent = 3;
                                                step = 1;
                                                $scope.tittle = "请处理已满Slot，并扫描容器条码";
                                            } else {
                                                step = 1;
                                                $scope.tittle = "请扫描容器号码";
                                                $scope.errorSlot = false;
                                                $scope.soltContent = false;
                                            }
                                        }
                                    }
                                    initGrid(datas)
                                });
                            }
                        }, function () {
                            $scope.soltContent = true;
                            $scope.errorSlot = true;
                        });
                    } else {
                        $scope.soltContent = true;
                        $scope.errorSlot = true;
                    }
                }
                $scope.imformation = "";
            }
        };

        //初始化列
        var columns = [
            {field: "id", headerTemplate: "<sapn translate='ID'></sapn>"},
            {field: "rebatchSlotName", width: 150, headerTemplate: "<span translate='位置'></span>"},
            {field: "pickingOrderNumber", width: 150, headerTemplate: "<span translate='批次'></span>"},
            {
                width: 100, headerTemplate: "<span translate='扫描数/总数'></span>", template: function (item) {
                var num = item.num;
                var htmlStr = "<div>";
                if (num.numPosition != 0) {
                    htmlStr += num.numPositionScanned + "/" + num.numPosition;
                }
                htmlStr += "</div>";
                return htmlStr;
            }
            },
            {title: "caseNumber", headerTemplate: "<span translate='箱号'></span>"},
            {
                width: 103, headerTemplate: "<span translate='备注'></span>", template: function (item) {
                var reBatchSlotName = item.rebatchSlotName;
                var requestId = item.id;
                var num = item.num;
                var htmlStr = "<div>";
                //状态判定
                if (num.numPosition != 0) {
                    if (num.numPositionScanned == 0) {
                        htmlStr += "待扫描"
                    } else if (num.numPositionScanned > 0 && num.numPositionScanned < num.numPosition) {
                        htmlStr += "正在扫描"
                    } else if (num.numPositionScanned == num.numPosition) {
                        htmlStr += "<kendo-button class='k-primary' ng-click='okClick(\"" + reBatchSlotName + "\",\"" + requestId + "\")' style='background-color: #00b050;margin: 0;padding: 0;border: 0'>换车完成</kendo-button>"
                    }
                    setTimeout(function () {
                        var grid = $("#rebatchGRID").data("kendoGrid");
                        grid.tbody.find('tr').each(function () {
                            if ($(this).find('td:last-child').text() == "换车完成") {
                                $(this).css("background", "#c5e0b4")
                            } else if ($(this).find('td:last-child').text() == "正在扫描") {
                                $(this).css("background", "#deebf7")
                            }
                        })
                    }, 0)
                } else {
                    htmlStr += "待分配任务"
                }
                htmlStr += "</div>";
                return htmlStr
            }
            }];

        //初始化grid
        function initGrid(datas) {
            dataSource = [];
            var maxBoxNum = 1;
            //拼接数据
            for (var i = 0; i < datas.length; i++) {
                datas[i].num = {"numPosition": datas[i].numPosition, "numPositionScanned": datas[i].numPositionScanned};
                (maxBoxNum < datas[i].numPosition) ? maxBoxNum = datas[i].numPosition : maxBoxNum;
                for (var j = 0; j < datas[i].positions.length; j++) {
                    datas[i]["T" + (j + 1)] = datas[i].positions[j]
                }
                dataSource.push(datas[i])
            }
            var col = [];
            //创建列
            for (var k = 1; k <= maxBoxNum; k++) {
                col.push({field: "T" + k});
            }
            columns[4].columns = col;
            //绑定数据
            var grid = $("#rebatchGRID").data("kendoGrid");
            grid.setOptions({dataSource: dataSource, columns: columns});
            grid.hideColumn(0);
        }

        //退出工作站
        $scope.exitClick = function () {
            // $state.go("main.mainMenu")
            reBatchService.exitClick(this.workstation, function () {
                $scope.rebatchPage = 1;
            })
        };

        //生成grid
        var height = $(document.body).height() / 2 - 20;
        $scope.rebatchGridOptions = outboundService.reGrids(dataSource, columns, height);

        //结束reatch，換車完成
        $scope.okClick = function (reBatchSlotName, requestId) {
            reBatchService.doReBatch(requestId, $scope.reBachStationName, function () {
                reBatchService.getReBatchInfo(function (datas) {
                    initGrid(datas)
                });
            })
        };
    });
})();