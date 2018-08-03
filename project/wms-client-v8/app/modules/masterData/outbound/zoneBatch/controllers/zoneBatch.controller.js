/**
 * Created by frank.zhou on 2017/2/24.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("zoneBatchCtl", function ($scope, $rootScope, $window, commonService, zoneBatchService, outboundService) {
    $window.localStorage["currentItem"] = "zoneBatch";
    var columns = [];
    var zoneBatchData = "";
    var dataSource = [];
    var option = 0;//是否显示汇总
    var condition = "?processNames=&deliveryDates="; //查询条件
    var height = $(document.body).height() - 195;//grid高度
    //绑定PP
    function getComboxOption(options) {
      return {
        dataSource: outboundService.getDataSource({
          key: options.key,
          value: "id",
          text: "name"
        }),
        dataTextField: "name",
        dataValueField: "id",
        filter: "contains",
        change: function () {
          options.change && options.change();
        }
      };
    }

    $scope.processNamesOption = getComboxOption({
      key: "getSelectProcessPath",
      change: function () {
        initHeight();
        setTimeout(function () {
          //绑定数据和列名
          $("#zoneBatchGRID").data("kendoGrid").setOptions({height: height});
        }, 0)
      }
    });

    //绑定EsSD
    zoneBatchService.getExpectSendDelivery("SYSTEM", function (data) {
      for(var i=0;i<data.length;i++){
        data[i].time=kendo.format("{0:yyyy-MM-dd HH:mm:ss}",kendo.parseDate(data[i].time))
      }
      $("#expectSendDelivery").data("kendoMultiSelect").setDataSource(data)
    });
    $scope.deliveryDatesOption = {
      dataTextField: "time",
      dataValueField: "time",
      change: function () {
        initHeight();
      }
    };

    //初始化grid高度
    function initHeight() {
      height = $(document.body).height() - 197;
      var searchOptionsHeight = $("#searchOptions").height();
      if (searchOptionsHeight > 39) {
        height -= searchOptionsHeight - 37;
      }
      $("#zoneBatchGRID").data("kendoGrid").setOptions({height: height});
    }

    //初始化列名
    function initColumns() {
      columns = [{width: 150, field: "pickingOrderNo", headerTemplate: "<span>PickingOrderNo</span>"},
        {width: 100, field: "processPathName", headerTemplate: "<span>PP</span>"},
        {width: 100, field: "batchActiveTime", headerTemplate: "<span>BatchActiveTime</span>"},
        {width: 150, field: "exSDTime", headerTemplate: "<span>ExSD</span>", template:function (item) {
          return item.exSDTime ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.exSDTime)) : "";
        }},
        {width: 200, headerTemplate: "<span>TotalUnits</span>", template: function (item) {
          var total = item.totalUnits;
          var htmlStr = "<div style='height: 100%;text-align: center'>";
          if (typeof(total.finishedTotalUnits) != "undefined") {
            if (total.unfinishedTotalUnits + total.finishedTotalUnits <= 200) {
              if (total.finishedTotalUnits == 0) {
                htmlStr += "<div style='float: left;background-color: #d9d9d9;padding:5px 0 5px 0;color: black;width:"+total.unfinishedTotalUnits+"px;min-width: 10px'>" + total.unfinishedTotalUnits + "</div>";
              } else if (total.unfinishedTotalUnits == 0) {
                htmlStr += "<div style='float: left;background-color: #92D050;padding:5px 0 5px 0;color: black;width:"+total.finishedTotalUnits+"px;min-width: 10px'>" + total.finishedTotalUnits + "</div>";
              }else{
                htmlStr += "<div  style='float: left;background-color: #92D050;padding:5px 0 5px 0;color: white;width:" + total.finishedTotalUnits + "px;min-width: 10px;max-width: 380px'>" + total.finishedTotalUnits +
                  "</div><div style='float: left;background-color: #d9d9d9;padding:5px 0 5px 0;color: black;width:" + total.unfinishedTotalUnits + "px;max-width: 380px;min-width: 10px'>" + total.unfinishedTotalUnits + "</div>";
              }

            } else {
              if (total.finishedTotalUnits == 0) {
                htmlStr += "<div style='float: left;background-color: #d9d9d9;padding:5px 0 5px 0;color: black;width:100%;min-width: 10px'>" + total.unfinishedTotalUnits + "</div>";
              } else if (total.unfinishedTotalUnits == 0) {
                htmlStr += "<div style='float: left;background-color: #92D050;padding:5px 0 5px 0;color: black;width:100%;min-width: 10px'>" + total.finishedTotalUnits + "</div>";
              } else {
                htmlStr += "<div  style='float: left;background-color: #92D050;padding:5px 0 5px 0;color: white;width:" + total.finishedTotalUnits / (total.finishedTotalUnits + total.unfinishedTotalUnits) * 100 + "%;min-width: 10px;max-width: 380px'>" +
                  total.finishedTotalUnits + "</div>" +
                  "<div style='float: left;background-color: #d9d9d9;padding:5px 0 5px 0;color: black;width:" + total.unfinishedTotalUnits / (total.unfinishedTotalUnits + total.unfinishedTotalUnits) * 100 + "%;max-width: 380px;min-width: 10px;'>" +
                  total.unfinishedTotalUnits + "</div>";
              }
            }
          }
          htmlStr += "</div>";
          return htmlStr;
        }
        }];
    }

    //初始化grid
    function initGrid() {
      dataSource = [];
      initColumns();
      var operator = {
        "pickingOrderNo": "Current Operator",
        "pp": "",
        "batchActiceTime": "",
        "exSD": "",
        "totalUnits": ""
      };
      var totalVolume = {
        "pickingOrderNo": "TotalVolume",
        "pp": "",
        "batchActiceTime": "",
        "exSD": "",
        "totalUnits": ""
      };
      var pickAreaName = {};
      if(typeof(zoneBatchData.amountDetail)!="undefined") {
        for (var i = 0; i < zoneBatchData.amountDetail.length; i++) {
          zoneBatchData.amountDetail[i].totalUnits = {
            "finishedTotalUnits": zoneBatchData.amountDetail[i].finishedTotalUnits,
            "unfinishedTotalUnits": zoneBatchData.amountDetail[i].unfinishedTotalUnits
          };
          var position = zoneBatchData.amountDetail[i].positions;
          var amount = zoneBatchData.amount;
          //排序规则
          var compare = function (prop) {
            return function (obj1, obj2) {
              var val1 = obj1[prop];
              var val2 = obj2[prop];
              if (val1 < val2) {
                return -1;
              } else if (val1 > val2) {
                return 1;
              } else {
                return 0;
              }
            }
          };
          //排序
          position.sort(compare("pickAreaName"));
          amount.sort(compare("pickAreaName"));
          //将position和amount合并
          for (var j = 0; j < position.length; j++) {
            if (amount[j].pickAreaName == position[j].pickAreaName) {
              for (var l in amount[j]) {
                position[j][l] = amount[j][l]
              }
              pickAreaName[position[j].pickAreaName] = position[j];
            }
          }
          //拼接数据
          for (var m in pickAreaName) {
            zoneBatchData.amountDetail[i][m] = pickAreaName[m];
            if (i == 0) {
              operator[m] = pickAreaName[m];
              totalVolume[m] = pickAreaName[m];
              columns.push({
                headerTemplate: "<span>" + m + "</span>", template: function (m) {
                  return function (item) {
                    var number = item[m];
                    var pickingOrderNo = item.pickingOrderNo;
                    var htmlStr = "<div style='height: 100%;text-align: center; width: 100%'>";
                    var color = "";
                    var fontColor = "#000000";
                    if (number.pickedAmount == number.amount) {
                      color = '#92D050';
                    } else if (number.pickedAmount > 0 && number.pickedAmount < number.amount) {
                      color = '#66ffff'
                    } else {
                      color = '#d9d9d9'
                    }
                    if (pickingOrderNo == "Current Operator") {
                      htmlStr += "<div style='padding:5px 0 5px 0;color:" + fontColor + ";width: 100%'>" + number.currentOperatorAmount + "</div>"
                    } else if (pickingOrderNo == "TotalVolume") {
                      htmlStr += "<div style='padding:5px 0 5px 0;color:" + fontColor + ";width: 100%'>" + number.totalVolumeAmount + "</div>"
                    } else if (number.amount != 0) {
                      htmlStr += "<div style='float: left;background-color: " + color + ";padding:5px 0 5px 0;color:" + fontColor + ";width: 100%'>" + number.pickedAmount + "/" + number.amount + "</div>"
                    }
                    htmlStr += "</div>";
                    return htmlStr;
                  }

                }(m)
              });
            }
          }
          if (i == 0 && option == 0) {
            dataSource.push(operator);
            dataSource.push(totalVolume);
          }
          dataSource.push(zoneBatchData.amountDetail[i]);
        }
      }
      setTimeout(function () {
        //绑定数据和列名
        $("#zoneBatchGRID").data("kendoGrid").setOptions({columns: columns, dataSource: dataSource});
      }, 0)
    }

    //创建grid
    $scope.zoneBatchGridOptions = {
      dataSource: dataSource,
      height: height,
      //数据绑定成功后合并单元格
      dataBound: function () {
        for (var i = 0; i < 2; i++) {
          this.tbody.find("tr:eq(" + i + ")").each(function () {
            var td = $(this).find("td:eq(0)");
            if (td.text() == "Current Operator" || td.text() == "TotalVolume") {
              td.attr("colspan", 5);
              var l = 0;
              while (l < 4) {
                $(this).find("td:eq(1)").remove();
                l++
              }
            }
          });
        }
      }
    };

    //获得zoneBatch数据
    zoneBatchService.getZoneBatch(condition, function (data) {
      zoneBatchData = data;
      initGrid(option);
    });

    //搜索
    $scope.searchGrid = function () {
      var processNames = [];
      var deliveryDates = [];
      option = 0;
      if (typeof($scope.processNames) != "undefined") {
        processNames = $scope.processNames;
      }
      if (typeof($scope.deliveryDates) != "undefined") {
        deliveryDates = $scope.deliveryDates;
      }
      var processNameStr = "?processNames=";
      var deliveryDateStr = "&deliveryDates=";
      for (var i = 0; i < processNames.length; i++) {
        if (i == processNames.length - 1) {
          processNameStr += processNames[i].name;
          option = 1
        } else {
          processNameStr += processNames[i].name + "&processNames=";
        }
      }
      for (var j = 0; j < deliveryDates.length; j++) {
        if (j == deliveryDates.length - 1) {
          deliveryDateStr += deliveryDates[j].exsdTime.replace(/ /,"T")+"Z";
          option = 1
        } else {
          deliveryDateStr += deliveryDates[j].exsdTime.replace(/ /,"T") + "Z&deliveryDates=";
        }
      }
      condition = processNameStr + deliveryDateStr;
      zoneBatchService.getZoneBatch(condition, function (data) {
        zoneBatchData = data;
        initGrid(option);
      });
    };
  })
})();