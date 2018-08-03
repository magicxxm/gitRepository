/**
 * Created by frank.zhou on 2017/04/01.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("workflowWorkPoolProcessPathCtl", function ($scope, $timeout, $stateParams, $rootScope, $state, $sce, workflowService) {
    $scope.exsdType = "ALL";
    $scope.itemType = "ALL";
    var params = angular.fromJson($stateParams.params);
    $scope.dimensionX = "None";
    $scope.dimensionY = "None";
    if (params) {
      $scope.timeStart = params.timeStart;
      $scope.timeEnd = params.timeEnd;
      $scope.goodsType = params.goodsType;
    } else {
      $scope.timeStart = null;
      $scope.timeEnd = null;
      $scope.goodsType = null;
    }
    // 组装内容串
    function getProcessPathContent(datas) {
      var htmls = [];
      for (var i = 0; i < datas.length; i++) {
        var data = datas[i],
          workflows = data.workflows; // 表格数据集
        if (!workflows.length) continue;
        htmls.push("<div style='margin-bottom:15px;'>");
        htmls.push(" <div style='font-size:14px;font-weight:bold;margin-bottom:5px;'>" + data.name + "</div>");
        // 组装列
        for (var j = 0, columns = []; j < workflows.length; j++) columns.push(workflows[j].exsdTime);
        htmls.push(" <table border=1 cellspacing=0 cellpadding=0 style='width:" + (160 + columns.length * 130) + "px;border-color:white;'>");
        // 列头
        htmls.push("  <tr style='height:30px;background-color:rgb(0, 176, 255);color:white;'>");
        htmls.push("   <td style='width:160px;text-align:center;'>cutline</td>");
        for (var j = 0; j < columns.length; j++) {
          var exsdTime = kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(columns[j])),
            headers = exsdTime.split(" ");
          htmls.push(" <td style='width:130px;'>");
          htmls.push("  <div style='text-align:center;'>" + headers[0] + "</div>");
          htmls.push("  <div style='text-align:center;'>" + headers[1] + "</div>");
          htmls.push(" </td>");
        }
        htmls.push("  </tr>");
        // 数据行
        var categories = ["Replenishment", "Picking", "Work In Process", "Problem Solve In Process", "Ship", "汇总"];
        for (var k in $rootScope.workflowMap) {
          var workflowType = $rootScope.workflowMap[k];
          htmls.push("<tr style='height:30px;'>");
          var isCategory = (categories.indexOf(workflowType) >= 0); // 是否分类
          var colspan = (isCategory ? " colspan='" + (1) + "' " : ''); // 暂无意义
          htmls.push("<td" + colspan + " style='" + (isCategory ? "text-align:left;font-weight:bold;" : "text-align:center;") + ";'>" + workflowType + "</td>");
          for (var j = 0; j < workflows.length; j++) {
            var rowData = workflows[j];
            if (rowData.cutline[k] != null) {
              var exsdTime = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", kendo.parseDate(rowData.exsdTime));
              htmls.push("<td style='text-align:center;'>");
              htmls.push("<a href='javascript:void(0)' type='" + workflowType + "' pp='" + data.name + "' field='" + exsdTime + "'>" + rowData.cutline[k] + "</a>");
              htmls.push("</td>");
            } else
              htmls.push("<td></td>");
          }
          htmls.push("</tr>");
        }
        htmls.push(" </table>");
        htmls.push("</div>");
      }
      return htmls.join(" ");
    }

    //exsd选择time选项两个时间控件初始化
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
      if ($scope.exsdType == "ALL") {} else if ($scope.exsdType == "Today") {

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
        var dateTimePickerStart = $("#datetimepickerStart").data("kendoDateTimePicker");
        var dateTimePickerEnd = $("#datetimepickerEnd").data("kendoDateTimePicker");
        $scope.timeStart = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", dateTimePickerStart.value());
        $scope.timeEnd = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", dateTimePickerEnd.value());
      } else {
        $scope.timeStart = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", dateStart);
        $scope.timeEnd = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", dateEnd);
      }

      var dimensionX = $scope.dimensionX;
      var dimensionY = $scope.dimensionY;
      if (dimensionX === "WorkPool" && dimensionY === "ProcessPath")
        getWorkPoolProcessPath();



      //  $state.go("main.workflowProcessPathWorkPool");;
      else if (dimensionX === "ProcessPath" && dimensionY === "WorkPool") {
        $state.go('main.workflowProcessPathWorkPool', {
          params: angular.toJson({
            timeStart: $scope.timeStart,
            timeEnd: $scope.timeEnd,
            goodsType: $scope.itemType
          })
        });
        //   console.log({timeStart: $scope.timeStart,timeEnd: $scope.timeEnd,goodsType:$scope.itemType})
      }
      //  $state.go("main.workflowWorkPoolProcessPath");
        else if (dimensionY === "None" && (dimensionX === "WorkPool" || dimensionX === "None")) {
        $state.go('main.workflow', {
          params: angular.toJson({
            timeStart: $scope.timeStart,
            timeEnd: $scope.timeEnd,
            goodsType: $scope.itemType
          })
        });
      }
    };
    //左侧菜单
    $timeout(function () {
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
        $scope.splitterHeight = $(document.body).height() - 146;
      })
    });
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
    };

    // 取workPoolProcessPath
    function getWorkPoolProcessPath() {
      workflowService.getWorkPoolProcessPath($scope.timeStart, $scope.timeEnd, $scope.goodsType, function (datas) {
        for (var i = 0, mainDatas = []; i < datas.length; i++) {
          var data = datas[i];
          mainDatas.push({
            name: data.ppName,
            workflows: data.workflows
          });
        }
        // 内容串
        $scope.processPathContent = $sce.trustAsHtml(getProcessPathContent(mainDatas));
        // 事件
        setTimeout(function () {
          $("#processPathContent a").each(function () {
            $(this).bind("click", function () {
              var type = $(this).attr("type"),
                pp = $(this).attr("pp"),
                field = $(this).attr("field");
              $state.go("main.workflowWorkPoolProcessPathDetail", {
                params: angular.toJson({
                  type: type,
                  pp: pp,
                  field: field
                })
              });
            });
          });
        }, 500);
      });
    }
    // 初始化
    $scope.dimensionSourceYS = ["None", "WorkPool", "ProcessPath"];
    $scope.dimensionSourceXS = ["WorkPool", "ProcessPath"];
    $scope.dimensionSourceX = ["WorkPool", "ProcessPath"];
    $scope.dimensionSourceY = ["None", "ProcessPath"];
    // 初始化
    getWorkPoolProcessPath();
  }).controller("workflowWorkPoolProcessPathDetailCtl", function ($scope, $rootScope, $window, $stateParams, workflowService) {
    var workflowType = null,
      params = angular.fromJson($stateParams.params);
    for (var k in $rootScope.workflowMap) {
      if ($rootScope.workflowMap[k] === params.type) {
        workflowType = k;
        break;
      }
    }
      workflowService.getWorkPoolProcessPathDetail(params.pp, workflowType, params.field, function (datas) {
          var grid = $("#workflowWorkPoolProcessPathDetailGRID").data("kendoGrid");
          grid.setOptions({
              dataSource: datas
          });
      });
      $scope.getLegacy = function () {
          workflowService.getWorkPoolProcessPathDetail(params.pp, workflowType, params.field, function (datas) {
              var grid = $("#workflowWorkPoolProcessPathDetailGRID").data("kendoGrid");
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
          return item.planDepartTime ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.planDepartTime)) : "";
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
    $scope.workflowWorkPoolProcessPathDetailGridOptions = {
      height: $(document.body).height() - 158,
      columns: columns,
      selectable: false,
      sortable: true,
      resizable: true
    };
  });
})();