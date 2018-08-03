/**
 * Created by frank.zhou on 2017/03/16.
 * updated by zhihan.dong on 2017/05/22.
 */
(function () {
    "use strict";
    angular
        .module("myApp")
        .controller("workflowCtl", function ($scope,
                                             $timeout,
                                             $rootScope,
                                             $stateParams,
                                             $state,
                                             $window,
                                             workflowService) {
            if ($stateParams.params) {
                var params = angular.fromJson($stateParams.params);
            }
            if (params) {
                $scope.timeStart = params.timeStart;
                $scope.timeEnd = params.timeEnd;
                $scope.goodsType = params.goodsType;
            } else {
                $scope.timeStart = null;
                $scope.timeEnd = null;
                $scope.goodsType = null;
            }
            $scope.dimensionX = "None";
            $scope.dimensionY = "None";

            $window.localStorage["currentItem"] = "workflow";
            $scope.itemType = "ALL";
            $scope.exsdType = "ALL";

            // 初始数据
            $rootScope.workflowMap = {
                // replenishment: "Replenishment",
                // needToReplenish: "Need To Replenish",
                // stockEnough: "Stock Enough",
                // replenishing: "Replenishing",
                // totalReplenishment: "Total Replenishment",
                picking: "Picking",
                pending: "Pending",
                // readyToPick: "Ready To Pick",
                pickingNotYetPicked: "Picking Not Yet Picked",
                totalPicking: "Total Picking",
                workInProcess: "Work In Process",
                pickingPicked: "Picking Picked",
                rebatched: "Rebatched",
                rebinBuffer: "Rebin Buffer",
                rebined: "Rebined",
                scanVerify: "Scan Verify",
                packed: "Packed",
                totalWorkInProcess: "Total Work In Process",
                problemInProcess: "Problem Solve In Process",
                problem: "Problem",
                ship: "Ship",
                sorted: "Sorted",
                loaded: "Loaded",
                manifested: "Manifested",
                totalShipping: "Total Shipping",
                total: "汇总"
            };

            $("#datetimepickerEnd").kendoDateTimePicker({
                format: "yyyy-MM-dd hh:mm:ss",
                animation: {
                    close: {
                        effects: "zoom:out",
                        duration: 300
                    }
                }
            });
            $("#datetimepickerStart").kendoDateTimePicker({
                format: "yyyy-MM-dd hh:mm:ss",
                animation: {
                    close: {
                        effects: "zoom:out",
                        duration: 300
                    }
                }
            });
            //确定维度和选择时间
            $scope.doLocation = function () {
                var dateStart = new Date();
                dateStart.setHours(0);
                dateStart.setMinutes(0);
                dateStart.setSeconds(0);
                dateStart.setMilliseconds(0);
                var dateEnd = new Date();
                dateEnd.setHours(24);
                dateEnd.setMinutes(0);
                dateEnd.setSeconds(0);
                dateEnd.setMilliseconds(0);
                if ($scope.exsdType == "ALL") {
                } else if ($scope.exsdType == "Today") {
                } else if ($scope.exsdType == "Next 3 Days") {
                    dateEnd.setDate(dateEnd.getDate() + 3);
                } else if ($scope.exsdType == "±1 Day") {
                    dateStart.setDate(dateStart.getDate() - 1);
                    dateEnd.setDate(dateEnd.getDate() + 1);
                } else if ($scope.exsdType == "±3 Day") {
                    dateStart.setDate(dateStart.getDate() - 3);
                    dateEnd.setDate(dateEnd.getDate() + 3);
                }
                if ($scope.exsdType == "ALL") {
                    $scope.timeStart = null;
                    $scope.timeEnd = null;
                } else if ($scope.exsdType == "Time") {
                    var dateTimePickerStart = $("#datetimepickerStart").data(
                        "kendoDateTimePicker"
                    );
                    var dateTimePickerEnd = $("#datetimepickerEnd").data(
                        "kendoDateTimePicker"
                    );
                    $scope.timeStart = kendo.format(
                        "{0:yyyy-MM-ddTHH:mm:ss}",
                        dateTimePickerStart.value()
                    );
                    $scope.timeEnd = kendo.format(
                        "{0:yyyy-MM-ddTHH:mm:ss}",
                        dateTimePickerEnd.value()
                    );
                } else {
                    $scope.timeStart = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", dateStart);
                    $scope.timeEnd = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", dateEnd);
                }


                var dimensionX = $scope.dimensionX;
                var dimensionY = $scope.dimensionY;
                if (dimensionX === "WorkPool" && dimensionY === "ProcessPath")
                    $state.go("main.workflowWorkPoolProcessPath", {
                        params: angular.toJson({
                            timeStart: $scope.timeStart,
                            timeEnd: $scope.timeEnd,
                            goodsType: $scope.itemType
                        })
                    });
                else if (dimensionX === "ProcessPath" && dimensionY === "WorkPool") {
                    //  $state.go("main.workflowProcessPathWorkPool");;
                    $state.go("main.workflowProcessPathWorkPool", {
                        params: angular.toJson({
                            timeStart: $scope.timeStart,
                            timeEnd: $scope.timeEnd,
                            goodsType: $scope.itemType
                        })
                    });

                    //   console.log({timeStart: $scope.timeStart,timeEnd: $scope.timeEnd,goodsType:$scope.itemType})
                } else if (dimensionY === "None" && (dimensionX === "WorkPool" || dimensionX === "None")) {
                    //  $state.go("main.workflowWorkPoolProcessPath");

                    refresh();
                }
            };

            // 取workflow
            function refresh() {
                workflowService.getWorkflow(
                    $scope.timeStart,
                    $scope.timeEnd,
                    $scope.goodsType,
                    function (datas) {

                        // columns-前2列
                        var columns = [{
                            locked: true,
                            width: "200px",
                            field: "cutline",
                            headerTemplate: "<div style='text-align:center;'>cutline</div>",
                            template: function (dataItem) {
                                var style = Object.keys(dataItem).length === 5 ||
                                dataItem.cutline === "汇总" ?
                                    "font-weight:bold;" :
                                    "text-align:center;white-space:nowrap;";
                                return (
                                    "<div style='" + style + "'>" + dataItem.cutline + "</div>"
                                );
                            }
                        },
                            {
                                locked: true,
                                width: "100px",
                                field: "total",
                                headerTemplate: "<div style='text-align:center;'>Total</div>",
                                template: function (dataItem) {
                                    return dataItem.total != null ?
                                        "<div style='text-align:center;white-space:nowrap;'><a href='javascript:void(0)' type='" +
                                        dataItem.cutline +
                                        "' field=''>" +
                                        dataItem.total +
                                        "</a></div>" :
                                        "";
                                }
                            }
                        ];
                        // columns-时间列
                        datas.forEach(function (data) {
                            var exsdTime = kendo.format(
                                "{0:yyyy-MM-dd HH:mm:ss}",
                                kendo.parseDate(data.exsdTime)
                            );
                            var headers = exsdTime.split(" ");
                            var headerTemplate =
                                "<div style='text-align:center;'>" + headers[0] + "</div>";
                            headerTemplate +=
                                "<div style='text-align:center;'>" + headers[1] + "</div>";
                            var field = "A" + exsdTime.replace(/[-\s:]/g, "");
                            columns.push({
                                width: "120px",
                                field: field,
                                headerTemplate: headerTemplate,
                                template: function (dataItem) {
                                    return dataItem[field] != null ?
                                        "<div style='text-align:center;'><a href='javascript:void(0)' type='" +
                                        dataItem.cutline +
                                        "' field='" +
                                        exsdTime +
                                        "'>" +
                                        dataItem[field] +
                                        "</a></div>" :
                                        "";
                                }
                            });
                        });
                        // columns-末列
                        columns.push({
                            headerTemplate: ""
                        });
                        // dataSource
                        var dataSource = [];
                        for (var k in $rootScope.workflowMap) {
                            var itemData = {
                                cutline: $rootScope.workflowMap[k]
                            };
                            for (var i = 0, total = -1; i < datas.length; i++) {
                                var data = datas[i],
                                    exsdTime = kendo.format(
                                        "{0:yyyy-MM-dd HH:mm:ss}",
                                        kendo.parseDate(data.exsdTime)
                                    );
                                var field = "A" + exsdTime.replace(/[-\s:]/g, "");
                                if (data.cutline[k] != null) {
                                    itemData[field] = data.cutline[k];
                                    total === -1 && (total = 0);
                                    total += data.cutline[k];
                                }
                            }
                            total != -1 && (itemData["total"] = total);
                            dataSource.push(itemData);
                        }
                        // 加载表
                        var grid = $("#workflowGrid").data("kendoGrid");
                        grid.setOptions({
                            columns: columns,
                            dataSource: dataSource
                        });
                        // 事件
                        $("#workflowGrid a").each(function () {
                            $(this).bind("click", function () {
                                $state.go("main.workflowDetail", {
                                    id: $(this).attr("field"),
                                    name: $(this).attr("type")
                                });
                            });
                        });
                    }
                );
            }

            //维度选择事件
            $scope.dimensionChange = function (type) {
                var dimensionChangeY = $(dimensionChangeYId).data("kendoDropDownList");
                var dimensionChangeX = $(dimensionChangeXId).data("kendoDropDownList");
                if (type == 1) {
                    //  x
                    /*      if (dimensionChangeX.value() == "None") {
                     dimensionChangeY.value("None");
                     $scope.dimensionSourceY = angular.copy($scope.dimensionSource);
                     $scope.dimensionSourceX = angular.copy($scope.dimensionSource);
                     return;
                     }*/
                    if (dimensionChangeX.value() != "WorkPool") {
                        $scope.dimensionSourceY = angular.copy($scope.dimensionSourceYS);
                        var indexNone = $scope.dimensionSourceY.indexOf("None");
                        var index = $scope.dimensionSourceY.indexOf(dimensionChangeX.value());
                        $scope.dimensionSourceY.splice(index, 1);
                        $scope.dimensionSourceY.splice(indexNone, 1);
                        return;
                    }
                    $scope.dimensionSourceY = angular.copy($scope.dimensionSourceYS);
                    var index = $scope.dimensionSourceY.indexOf($scope.dimensionX);
                    $scope.dimensionSourceY.splice(index, 1);
                }
                /*else if (type == 2) {
                 // y
                 if (dimensionChangeY.value() == "None") {
                 $scope.dimensionSourceX = angular.copy($scope.dimensionSourceXS);
                 $scope.dimensionSourceY = angular.copy($scope.dimensionSourceYS);
                 $timeout(function () {
                 dimensionChangeX.value("WorkPool");
                 });
                 return;
                 }

                 var index = $scope.dimensionSourceXS.indexOf($scope.dimensionY);
                 if (index > -1) {
                 $scope.dimensionSourceX = angular.copy($scope.dimensionSourceXS);
                 console.log($scope.dimensionSourceX)
                 $scope.dimensionSourceX.splice(index, 1);
                 }*/
                /*     if (
                 dimensionChangeX.value() == "None" ||
                 dimensionChangeX.value() == "? string:None ?"
                 ) {
                 dimensionChangeX.value("");
                 }*/
                // }
            };
            // 初始化
            $scope.dimensionSourceYS = ["None", "WorkPool", "ProcessPath"];
            $scope.dimensionSourceXS = ["WorkPool", "ProcessPath"];
            $scope.dimensionSourceX = ["WorkPool", "ProcessPath"];
            //$scope.dimensionSourceY = ["None", "WorkPool", "ProcessPath"];
            $scope.dimensionSourceY = ["None", "ProcessPath"];
            $scope.exsdSource = [{
                key: "all",
                name: "All"
            },
                {
                    key: "today",
                    name: "Today"
                },
                {
                    key: "next",
                    name: "Next 3 Days"
                },
                {
                    key: "bet",
                    name: "+/- 3 Days"
                }
            ];
            $scope.getLegacy = function () {
                refresh();
                $timeout();
            }
            // grid

            $timeout(function () {
                var workflow = $("#workflowGrid").data("kendoGrid");
                $scope.splitterOptions = {
                    panes: [{
                        collapsible: true,
                        resizable: false,
                        size: "300px"
                    }],
                    orientation: "horizontal"
                };
                $timeout(function () {
                    var splitter = $("#workflowSplitter").data("kendoSplitter");
                    splitter && splitter.collapse(".k-pane:first");
                })

                $scope.splitterHeight =   $(document.body).height() - 146;
                $scope.gridStyle = {
                    "border" : "none",
                    "height" : $scope.splitterHeight,
                }
            });
            $scope.workflowGridOptions = {
                height: $(document.body).height() - 146,
                selectable: false,
                sortable: false,
                resizable: true
            };
            refresh();
        })
        .controller("workflowDetailCtl", function ($scope,
                                                   $rootScope,
                                                   $window,
                                                   $stateParams,
                                                   workflowService) {
            var workflowType = null;
            for (var k in $rootScope.workflowMap) {
                if ($rootScope.workflowMap[k] === $stateParams.name) {
                    workflowType = k;
                    break;
                }
            }
            workflowService.getWorkflowDetail($stateParams.id, workflowType, function (datas) {
                var grid = $("#workflowDetailGrid").data("kendoGrid");
                grid.setOptions({
                    dataSource: datas
                });
            });
            $scope.getLegacy = function () {
                workflowService.getWorkflowDetail($stateParams.id, workflowType, function (datas) {
                    var grid = $("#workflowDetailGrid").data("kendoGrid");
                    grid.setOptions({
                        dataSource: datas
                    });
                });
            }

            //
            var columns = [{
                field: "shipmentID",
                width: 120,
                headerTemplate: "<span translate='SHIPMENT_ID'></span>"
            },
                {
                    field: "batchNo",
                    width: 120,
                    headerTemplate: "<span translate='BATCH_NO'></span>"
                },
                {
                    field: "priority",
                    width: 60,
                    headerTemplate: "<span translate='优先级'></span>",
                    template: function (item) {
                        var value = "";
                        if (item.priority == "2") {
                            value = "<span>加急</span>";
                        } else if(item.priority == "1") {
                            value = "<span>紧急</span>";
                        } else {
                            value = "<span>正常</span>";
                        }
                        return value;
                    }
                },
                // {
                //   field: "orderID",
                //   width: 120,
                //   headerTemplate: "<span translate='ORDER_ID'></span>"
                // },
                {
                    field: "boxType",
                    width: 70,
                    headerTemplate: "<span translate='BOX_TYPE'></span>"
                },
                {
                    field: "skuno",
                    width: 100,
                    headerTemplate: "<span translate='SKU_NO'></span>"
                },
                {
                    field: "skuid",
                    width: 100,
                    headerTemplate: "<span translate='SKU_ID'></span>"
                },
                {
                    field: "quality",
                    width: 60,
                    headerTemplate: "<span translate='QUALITY'></span>"
                },
                {
                    field: "planDepartTime",
                    width: 120,
                    headerTemplate: "<span translate='PLAN_DEPART_TIME'></span>",
                    template: function (item) {
                        return item.planDepartTime ?
                            kendo.format(
                                "{0:yyyy-MM-dd HH:mm:ss}",
                                kendo.parseDate(item.planDepartTime)
                            ) :
                            "";
                    }
                },
                {
                    field: "stockPosition1",
                    width: 120,
                    headerTemplate: "<span translate='STOCK_POSITION_1'></span>"
                },
                {
                    field: "stockPosition2",
                    width: 120,
                    headerTemplate: "<span translate='STOCK_POSITION_2'></span>"
                },
                {
                    field: "ppName",
                    width: 120,
                    headerTemplate: "<span translate='PROCESS_PATH'></span>"
                },
                {
                    field: "workFlowStatus",
                    width: 150,
                    headerTemplate: "<span translate='WORKFLOW_STATUS'></span>"
                }
            ];
            $scope.workflowDetailGridOptions = {
                height: $(document.body).height() - 108,
                columns: columns,
                selectable: false,
                sortable: true,
                resizable: true
            };
        });
})();