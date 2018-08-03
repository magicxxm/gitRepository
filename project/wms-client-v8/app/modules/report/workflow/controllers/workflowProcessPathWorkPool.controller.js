/**
 * Created by frank.zhou on 2017/04/01.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("workflowProcessPathWorkPoolCtl", function ($scope, $timeout, $stateParams, $rootScope, $state, $sce, workflowService) {
    $scope.exsdType = "ALL";
    $scope.dimensionX = "None";
    $scope.dimensionY = "None";
    $scope.itemType = "ALL";
    var params = angular.fromJson($stateParams.params);
    if (params) {
      $scope.timeStart = params.timeStart;
      $scope.timeEnd = params.timeEnd;
      $scope.goodsType = params.goodsType;
    } else {
      $scope.timeStart = null;
      $scope.timeEnd = null;
      $scope.goodsType = null;
    }
    // 取匹配值
    function getMatchValue(times, value) {
      var dataMap = null;
      for (var i = 0; i < times.length; i++) {
        var time = times[i];
        if (time.exsdTime === value) {
          dataMap = time;
          break;
        }
      }
      return dataMap;
    }
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
        $scope.splitterHeight = $(document.body).height() - 110;
      })
    });
    // 组装内容串
    function getWorkPoolContent(datas) {
      var htmls = [];
      for (var i = 0; i < datas.length; i++) {
        var data = datas[i],
          columns = data.columns;
        if (!columns.length) continue;
        htmls.push("<div style='margin-bottom:15px;'>");
        htmls.push(" <div style='font-size:14px;font-weight:bold;margin-bottom:5px;'>" + data.name + "</div>");
        htmls.push(" <table border=1 cellspacing=0 cellpadding=0 style='width:" + (data.columns.length + 1) * 130 + "px;border-color:white;'>");
        // 列头
        htmls.push("  <tr style='height:30px;background-color:rgb(0, 176, 255);color:white;'>");
        htmls.push("   <td style='width:130px;text-align:center;'>cutline</td>");
        for (var j = 0; j < columns.length; j++) {
          var exsdTime = kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(columns[j]));
          var headers = exsdTime.split(" ");
          htmls.push(" <td style='width:130px;'>");
          htmls.push("  <div style='text-align:center;'>" + headers[0] + "</div>");
          htmls.push("  <div style='text-align:center;'>" + headers[1] + "</div>");
          htmls.push(" </td>");
        }
        htmls.push("  </tr>");
        // 数据行
        for (var j = 0, sources = data.sources; j < sources.length; j++) {
          var source = sources[j];
          debugger
          if (!source.workPpNameTime.length) continue;
          htmls.push("  <tr style='height:30px;'>");
          htmls.push("  <td style='text-align:center;'>" + source.ppName + "</td>");
          for (var k = 0; k < columns.length; k++) {
            var field = columns[k],
              fieldData = getMatchValue(source.workPpNameTime, field);
            if (fieldData === null) htmls.push("<td></td>");
            else {
              var exsdTime = kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", kendo.parseDate(field));
              htmls.push("<td style='text-align:center;'>");
              htmls.push("<a href='javascript:void(0)' type='" + data.name + "' pp='" + source.ppName + "' field='" + exsdTime + "'>" + fieldData.amount + "</a>");
              htmls.push("</td>");
            }
          }
          htmls.push("  </tr>");
        }
        htmls.push(" </table>");
        htmls.push("</div>");
      }
      return htmls.join(" ");
    }


    // 取processPathWorkPool
    function getProcessPathWorkPool() {
      workflowService.getProcessPathWorkPool($scope.timeStart, $scope.timeEnd, $scope.goodsType, function (data) {
        var datas = [];
        for (var k in $rootScope.workflowMap) {
          if (data[k] == null) continue;
          datas.push({
            name: $rootScope.workflowMap[k],
            columns: data[k].times,
            sources: data[k].ppNameData
          });
        }
        // 汇总列放最前
        var lastData = datas[datas.length - 1];
        datas.pop();
        datas.unshift(lastData);
        // 内容串
        $scope.workPoolContent = $sce.trustAsHtml(getWorkPoolContent(datas));
        // 事件
        setTimeout(function () {
          $("#workPoolContent a").each(function () {
            $(this).bind("click", function () {
              var type = $(this).attr("type"),
                pp = $(this).attr("pp"),
                field = $(this).attr("field");
              $state.go("main.workflowProcessPathWorkPoolDetail", {
                params: angular.toJson({
                  type: type,
                  pp: pp,
                  field: field
                })
              });
            });
          });
        }, 300);
      });
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
        $state.go('main.workflowWorkPoolProcessPath', {
          params: angular.toJson({
            timeStart: $scope.timeStart,
            timeEnd: $scope.timeEnd,
            goodsType: $scope.itemType
          })
        });

      //  $state.go("main.workflowProcessPathWorkPool");;
      else if (dimensionX === "ProcessPath" && dimensionY === "WorkPool") {

        getProcessPathWorkPool();
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
    // 初始化
    $scope.dimensionSourceYS = ["None", "WorkPool", "ProcessPath"];
    $scope.dimensionSourceXS = ["WorkPool", "ProcessPath"];
    $scope.dimensionSourceX = ["WorkPool", "ProcessPath"];
    $scope.dimensionSourceY = ["None", "WorkPool", "ProcessPath"];
    // 初始化
    getProcessPathWorkPool();
  }).controller("workflowProcessPathWorkPoolDetailCtl", function ($scope, $rootScope, $window, $stateParams, workflowService) {
    var workflowType = null,
      params = angular.fromJson($stateParams.params);
    for (var k in $rootScope.workflowMap) {
      if ($rootScope.workflowMap[k] === params.type) {
        workflowType = k;
        break;
      }
    }
      workflowService.getProcessPathWorkPoolDetail(workflowType, params.pp, params.field, function (datas) {
          var grid = $("#workflowProcessPathWorkPoolDetailGRID").data("kendoGrid");
          grid.setOptions({
              dataSource: datas
          });
      });
      $scope.getLegacy = function () {
          workflowService.getProcessPathWorkPoolDetail(workflowType, params.pp, params.field, function (datas) {
              var grid = $("#workflowProcessPathWorkPoolDetailGRID").data("kendoGrid");
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
    $scope.workflowProcessPathWorkPoolDetailGridOptions = {
      height: $(document.body).height() - 158,
      columns: columns,
      selectable: false,
      sortable: true,
      resizable: true
    };
  });
})();