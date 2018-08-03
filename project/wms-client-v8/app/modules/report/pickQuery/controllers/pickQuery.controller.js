/**
 * Created by zhihan.dong on 2017/04/17.
 * updated by zhihan.dong on 2017/05/02.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("pickQueryCtl", function ($scope, $rootScope, $timeout, $window, $state, commonService, reportService, pickQueryService) {
    ///////////////////////////////////////////pickQuery

    $window.localStorage["currentItem"] = "pickQuery";
    $scope.pickType = 'area';
    $timeout(function () {
      var pickTypeExsdGRID = $("#pickTypeExsdGRID").data("kendoGrid");
      var pickTypeAreaGRID = $("#pickTypeAreaGRID").data("kendoGrid");
      //初始化grid  前三行合并单元格
      pickTypeAreaGRID.setOptions({
        dataBound: function () {
          for (var i = 0; i < 3; i++) {
            this.tbody.find("tr:eq(" + i + ")").each(function () {
              var td = $(this).find("td:eq(0)");
              // if (td.text() == "Total Picking Not Yet Picked" || td.text() == "Total Picked" || td.text() == "Total") {
              if (td.text() == "pending" || td.text() == "notPicked" || td.text() == "picked" || td.text() == "total") {
                td.attr("colspan", 3);
                var l = 0;
                while (l < 2) {
                  $(this).find("td:eq(1)").hide();
                    $(this).find("td:eq(2)").hide();
                  l++;
                }
              }
            });
          }
        }
      });
      pickTypeExsdGRID.setOptions({
        dataBound: function () {
          for (var i = 0; i < 3; i++) {
            this.tbody.find("tr:eq(" + i + ")").each(function () {
              var td = $(this).find("td:eq(0)");
              // if (td.text() == "Total Picking Not Yet Picked" || td.text() == "Total Picked" || td.text() == "Total") {
              if (td.text() == "pending" || td.text() == "notPicked" || td.text() == "picked" || td.text() == "total") {
                td.attr("colspan", 3);
                var l = 0;
                while (l < 2) {
                  $(this).find("td:eq(1)").hide();
                  $(this).find("td:eq(2)").hide();
                  l++;
                }
              }
            });
          }
        }
      });
    });

    //选择时间事件
    $("#datetimepicker").kendoDateTimePicker({
      format: "yyyy-MM-dd HH:mm:ss",
      animation: {
        close: {
          effects: "zoom:out",
          duration: 300
        }
      }
    });


    $scope.searchExsd = function () {
      var datetimepicker = $("#datetimepicker").data("kendoDateTimePicker");
      $scope.Exsd = kendo.format("{0:yyyy-MM-dd HH:mm:ss}", datetimepicker.value());
      pickQueryService.querypickTypeExsd(function (data) {
        var pickTypeExsdGRID = $("#pickTypeExsdGRID").data("kendoGrid");
        pickTypeExsdGRID.setOptions({
          dataSource: data,
          columns: exsdTotalColumns(data)
        });
      }, $scope.ppName, $scope.Exsd);
    };
    $scope.searchArea = function () {
      pickQueryService.querypickTypeArea(function (data) {
        var pickTypeAreaGRID = $("#pickTypeAreaGRID").data("kendoGrid");
        pickTypeAreaGRID.setOptions({
          dataSource: data,
          columns: areaTotalColumns(data)
        });
      }, $scope.ppName, $scope.pickArea);
    };
    ///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  根据zoneName 为字段
    //创建grid
    /*    pickQueryService.querypickTypeArea(function (data) {
          $scope.pickTypeAreaGridOptions = reportService.reGrids(data, areaTotalColumns(data));
          $scope.pickTypeAreaGridOptions.dataBound = function () {
            for (var i = 0; i < 3; i++) {
              this.tbody.find("tr:eq(" + i + ")").each(function () {
                var td = $(this).find("td:eq(0)");
                if (td.text() == "Total Picking Not Yet Picked" || td.text() == "Total Picked" || td.text() == "Total") {
                  td.attr("colspan", 3);
                  var l = 0;
                  while (l < 2) {
                    $(this).find("td:eq(1)").remove();
                    l++;
                  }
                }
              });
            }
          };
        });*/

    $scope.pickExsd= function () {
      pickQueryService.querypickTypeExsd(function (data) {
        var pickTypeExsdGRID = $("#pickTypeExsdGRID").data("kendoGrid");
        pickTypeExsdGRID.setOptions({
          dataSource: data,
          height: $(document.body).height() - 206,
          columns: exsdTotalColumns(data)
        });
      }, null, null);
    }

    $scope.pickarea = function () {
      pickQueryService.querypickTypeArea(function (data) {
        var pickTypeAreaGRID = $("#pickTypeAreaGRID").data("kendoGrid");
        pickTypeAreaGRID.setOptions({
          dataSource: data,
          height: $(document.body).height() - 206,
          columns: areaTotalColumns(data),
          dataBound: function () {
            for (var i = 0; i < 3; i++) {
              this.tbody.find("tr:eq(" + i + ")").each(function () {
                var td = $(this).find("td:eq(0)");
                //     if (td.text() == "Total Picking Not Yet Picked" || td.text() == "Total Picked" || td.text() == "Total") {
                if (td.text() == "pending" || td.text() == "notPicked" || td.text() == "picked" || td.text() == "total") {
                  td.attr("colspan", 3);
                  var l = 0;
                  while (l < 2) {
                    $(this).find("td:eq(1)").hide();
                    l++;
                  }
                }
              });
            }
          }
        });
        /*      $scope.pickTypeAreaGridOptions = reportService.reGrids(data, areaTotalColumns(data));
              $scope.pickTypeAreaGridOptions.dataBound = function () {
                for (var i = 0; i < 3; i++) {
                  this.tbody.find("tr:eq(" + i + ")").each(function () {
                    var td = $(this).find("td:eq(0)");
                    if (td.text() == "Total Picking Not Yet Picked" || td.text() == "Total Picked" || td.text() == "Total") {
                      td.attr("colspan", 3);
                      var l = 0;
                      while (l < 2) {
                        $(this).find("td:eq(1)").remove();
                        l++;
                      }
                    }
                  });
                }
              };*/
      }, null, null);
    }
    /*      $scope.pickTypeExsdGridOptions = reportService.reGrids(data, exsdTotalColumns(data));
          $scope.pickTypeExsdGridOptions.dataBound = function () {
            for (var i = 0; i < 3; i++) {
              this.tbody.find("tr:eq(" + i + ")").each(function () {
                var td = $(this).find("td:eq(0)");
                if (td.text() == "Total Picking Not Yet Picked" || td.text() == "Total Picked" || td.text() == "Total") {
                  td.attr("colspan", 3);
                  var l = 0;
                  while (l < 2) {
                    $(this).find("td:eq(1)").remove();
                    l++;
                  }
                }
              });
            }
          };*/



    //初始化字段
    function areaTotalColumns(data) {
      var columns = [];
      columns.push({
        field: "ppName",
        headerTemplate: "<span translate='ppName'></span>"
      });
      //
      columns.push({
        field: "deliveryDate",
        headerTemplate: "<span translate='ExSD'></span>"
      });
      //总units
      columns.push({
        headerTemplate: "<span translate='Units'></span>",
        template: function (item) {
          var picked = 0;
          var notPicked = 0;
          // var pending = 0;
          item.zonesSet.map(function (index) {
            picked += index.pickAmount.picked;
            notPicked += index.pickAmount.notPicked;
            // pending += index.pickAmount.pending;
            // notPicked = notPicked + pending;

          });
          var A = picked; //pick完未rebin
          var D = notPicked; //未pick
          var htmlStr = "<div style='margin:1px;width:200px;text-align:left'>";
          if ((A + D) <= 200) {
            A && (htmlStr += "<div style='width:" + Math.max(20, A / 2) + "px; background:#93d150' class='gridCellList'>" + A + "</div>");
            /*       B && (htmlStr += "<div style='width:" + 10 + "px; background:#ffc100' class='gridCellList'>" + B + "</div>")
                   C && (htmlStr += "<div style='width:" + 10 + "px; background:#ff0000' class='gridCellList'>" + C + "</div>")*/
            D && (htmlStr += "<div style='width:" + Math.max(20, D / 2) + "px; background:#bfbfbf' class='gridCellList'>" + D + "</div>");
            // E && (htmlStr += "<div style='width:" + Math.max(20, E / 2) + "px; background:#bdd7ef' class='gridCellList'>" + E + "</div>")
            //  F && (htmlStr += "<div style='width:" + Math.max(20, F / 2) + "px; background:#00b1f1' class='gridCellList'>" + F + "</div>")
          } else {
            var sum = A + D;
            A && (htmlStr += "<div style='width:" + A / sum * 100 + "%; background:#93d150' class='gridCellList'>" + A + "</div>");
            /*  B && (htmlStr += "<div style='width:" + 10 / sum * 100 + "%; background:#ffc100' class='gridCellList'>" + B + "</div>")
              C && (htmlStr += "<div style='width:" + 10 / sum * 100 + "%; background:#ff0000' class='gridCellList'>" + C + "</div>")*/
            D && (htmlStr += "<div style='width:" + D / sum * 100 + "%; background:#bfbfbf' class='gridCellList'>" + D + "</div>");
            //   E && (htmlStr += "<div style='width:" + E / sum * 100 + "%; background:#bdd7ef' class='gridCellList'>" + E + "</div>")
            //F && (htmlStr += "<div style='width:" + F / sum * 100 + "%; background:#00b1f1' class='gridCellList'>" + F + "</div>")
          }
          htmlStr += "</div>";
          return htmlStr;
        }
      });

      //合计
      columns.push({
        headerTemplate: "<span translate='total'></span>",
        template: function (item) {
          console.log(item);
          var picks = 0;
          //if (item.ppName == "Total Picking Not Yet Picked" || item.ppName == "Total Picked" || item.ppName == "Total") {
          if (item.ppName == "pending" || item.ppName == "notPicked" || item.ppName == "picked" || item.ppName == "total") {

            item.zonesSet.map(function (index) {
              picks += index.pickAmount.total;
              console.log(picks);
            });
          } else {
            item.zonesSet.map(function (index) {
              picks += index.pickAmount.picked;
              picks += index.pickAmount.notPicked;
              picks += index.pickAmount.pending;
            });
          }
          return "<span>" + picks + "</span>";
        }
      });
      //动态列定义
      data[0].zonesSet.map(function (zone) {
        columns.push({
          headerTemplate: "<span >" + zone.zoneName + "</span>",
          template: //function () {
            function (item) {
              var html = "<span></span>";
              var picked = 0;
              var pickAmout = 0;
              item.zonesSet.map(function (index) {
                if (index.zoneName == zone.zoneName && index.pickAmount.total != null) {
                  html = "<div>" + index.pickAmount.total + "</div>";
                } else if (index.zoneName == zone.zoneName) {
                  // 待测试
                  picked += index.pickAmount.picked;
                  pickAmout += index.pickAmount.notPicked + index.pickAmount.picked;
                  if (picked == pickAmout) {
                    html = "<div style='color:white;background:#92d050'>" + picked + "/" + pickAmout + "<div>";
                  } else if (picked < pickAmout && picked > 0) {
                    html = "<div style='color:black;background:#66ffff'>" + picked + "/" + pickAmout + "<div>";
                  } else if (picked == 0) {
                    html = "<div style='color:black;background:#d9d9d9'>" + picked + "/" + pickAmout + "<div>";
                  }
                }
              });
              return html;
            }
          //}()
        });
      });
      return columns;
    }
    //////exsd 初始化字段
    function exsdTotalColumns(data) {
      var columns = [];
      columns.push({
        field: "ppName",
        headerTemplate: "<span translate='ppName'></span>"
      });
      //
      columns.push({
        field: "zoneName",
        headerTemplate: "<span translate='zoneName'></span>"
      });
      //总units
      columns.push({

        headerTemplate: "<span translate='Units'></span>",
        template: function (item) {
          var picked = 0;
          var notPicked = 0;
          // var pending = 0;
          item.deliveryDates.map(function (index) {
            picked += index.pickAmount.picked;
            notPicked += index.pickAmount.notPicked;
            // pending += index.pending;
          });
          var A = picked; //pick完未rebin
          var D = notPicked; //未pick
          var htmlStr = "<div style='margin:1px;width:200px;text-align:left'>";
          if ((A + D) <= 200) {
            A && (htmlStr += "<div style='width:" + Math.max(20, A / 2) + "px; background:#93d150' class='gridCellList'>" + A + "</div>");
            /*       B && (htmlStr += "<div style='width:" + 10 + "px; background:#ffc100' class='gridCellList'>" + B + "</div>")
                   C && (htmlStr += "<div style='width:" + 10 + "px; background:#ff0000' class='gridCellList'>" + C + "</div>")*/
            D && (htmlStr += "<div style='width:" + Math.max(20, D / 2) + "px; background:#bfbfbf' class='gridCellList'>" + D + "</div>");
            // E && (htmlStr += "<div style='width:" + Math.max(20, E / 2) + "px; background:#bdd7ef' class='gridCellList'>" + E + "</div>")
            //  F && (htmlStr += "<div style='width:" + Math.max(20, F / 2) + "px; background:#00b1f1' class='gridCellList'>" + F + "</div>")
          } else {
            var sum = A + D;
            A && (htmlStr += "<div style='width:" + A / sum * 100 + "%; background:#93d150' class='gridCellList'>" + A + "</div>");
            /*  B && (htmlStr += "<div style='width:" + 10 / sum * 100 + "%; background:#ffc100' class='gridCellList'>" + B + "</div>")
              C && (htmlStr += "<div style='width:" + 10 / sum * 100 + "%; background:#ff0000' class='gridCellList'>" + C + "</div>")*/
            D && (htmlStr += "<div style='width:" + D / sum * 100 + "%; background:#bfbfbf' class='gridCellList'>" + D + "</div>");
            //   E && (htmlStr += "<div style='width:" + E / sum * 100 + "%; background:#bdd7ef' class='gridCellList'>" + E + "</div>")
            //F && (htmlStr += "<div style='width:" + F / sum * 100 + "%; background:#00b1f1' class='gridCellList'>" + F + "</div>")
          }
          htmlStr += "</div>";
          return htmlStr;
        }
      });
      columns.push({
        headerTemplate: "<span translate='total'></span>",
        template: function (item) {
          var picks = 0;
          // if (item.ppName == "Total Picking Not Yet Picked" || item.ppName == "Total Picked" || item.ppName == "Total") {
          if (item.ppName == "pending" || item.ppName == "notPicked" || item.ppName == "picked" || item.ppName == "total") {
            item.deliveryDates.map(function (index) {
              picks += index.pickAmount.total;
            });
          } else {
            item.deliveryDates.map(function (index) {
              picks += index.pickAmount.picked;
              picks += index.pickAmount.notPicked;
              picks += index.pickAmount.pending;
            });
          }
          return "<span>" + picks + "</span>";
        }
      });
      data[0].deliveryDates.map(function (zone) {
        columns.push({
          headerTemplate: "<span >" + kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(zone.deliveryDate)) + "</span>",
          template: //function () {
            function (item) {
              var html = "<span></span>";
              var picked = 0;
              var pickAmout = 0;
              item.deliveryDates.map(function (index) {
                if (index.deliveryDate == zone.deliveryDate && index.pickAmount.total != null) {
                  html = "<div>" + index.pickAmount.total + "</div>";
                } else if (index.deliveryDate == zone.deliveryDate) {
                  picked += index.pickAmount.picked;
                  pickAmout += index.pickAmount.notPicked + index.pickAmount.picked;
                  if (picked == pickAmout) {
                    html = "<div style='color:white;background:#92d050'>" + picked + "/" + pickAmout + "<div>";
                  } else if (picked < pickAmout && picked > 0) {
                    html = "<div style='color:black;background:#66ffff'>" + picked + "/" + pickAmout + "<div>";
                  } else if (picked == 0) {
                    html = "<div style='color:black;background:#d9d9d9'>" + picked + "/" + pickAmout + "<div>";
                  }
                }
              });
              return html;
            }
          //}()
        });
      });

      return columns;
    }
    $scope.pickExsd();
    $scope.pickarea();
  });
})();