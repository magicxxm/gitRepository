/**
 * Created by frank.zhou on 2017/01/17.
 * Updated by frank.zhou on 2017/06/09.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebinMainCtl", function ($scope, $rootScope, $window, $state, $timeout, FLOOR_COLOR,commonService, outboundService, rebinService) {

      $scope.rebinWallHeight = $("#rebinWallDivId").height();
      $scope.rebin2WallHeight = $("#rebinTwoWallDivId").height();
    // ================================================外部函数=====================================================
    // 扫描rebinWall
    $scope.scanReBinWall = function (e, type) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode != 13) return;
      var name = $scope[type === "one" ? "rebinWall1" : "rebinWall2"].toUpperCase();
      //////////判断非正常进入 rebinWall 判定
      if ($rootScope.pickingOrder.rebininfo || $rootScope.rebinStation.rebininfo) {
        if (type === "one") {
          if (name != $scope.rebinWall1) return;
        } else if (type === "two") {
          if (name != $scope.rebinWall2) return;
        }
      }
      rebinService.scanReBinWall(name, $scope.requestId, function (data) {
        if (type === "one") {
          $("#rebin_two").focus(); // 小车输入框获焦
          $scope.rebinWall1Id = data.id;
          $scope.rebinWallOne = data;
          $scope.rebinWallOneRows = [];
          $scope.rebinWallOneColumns = [];
          $scope.rebinWallOneName = name;
          //
          for (var i = 0; i < $scope.pickingOrder.numColumn; i++)
            $scope.rebinWallOneColumns.push({
              name: "",
              choice: false
            });
          for (var i = 0; i < $scope.pickingOrder.numRow; i++)
            $scope.rebinWallOneRows[i] = angular.copy({
              item: $scope.rebinWallOneColumns,
              color: "#c1c1c1"
            });
          //
          $scope.pickingOrder.numRebinWall < 2 && toMain();
        } else if (type === "two") {
          $scope.rebinWall2Id = data.id;
          $scope.rebinWallTwo = data;
          $scope.rebinWallTwoName = name;
          $scope.rebinWallTwoRows = [];
          $scope.rebinWallTwoColumns = [];
          //
          for (var i = 0; i < $scope.pickingOrder.numColumn; i++)
            $scope.rebinWallTwoColumns.push({
              name: "",
              choice: false
            });
          for (var i = 0; i < $scope.pickingOrder.numRow; i++)
            $scope.rebinWallTwoRows[i] = angular.copy({
              item: $scope.rebinWallTwoColumns,
              color: "#c1c1c1"
            });
          //
          toMain();
        }
      }, function (data) {
        $scope.bindRebinError = data.values[0];
      })
        $scope.rebinWall1 = "";
        $scope.rebinWall2 = "";
    };

    // 扫描拣货车
    $scope.scanPickingContainer = function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode != 13) return;
      var name = $scope[$scope.pickingProcess === "scanFirstContainer" ? "firstContainer" : "nextContainer"];
      rebinService.scanPickingContainer(name, $scope.rebinFromUnitLoadId,$scope.requestId, function (data) {
        //if ($scope.pickingProcess === "scanFirstContainer") {
        $scope.rebinErrorWindow.close();
          initRebinWall();//清空rebinwall信息
        $scope.batchTotal = data.amountTotalOfPickingOrder;
        $scope.batchComplete = data.amountRebinedOfPickingOrder + data.amountMissOfPickingOrder;
        $scope.rebinStart = true;
        $scope.rebinFirstContainer = data;
        $scope.pickingProcess = "scanGoods"; // 扫描商品
        $scope.rebinGoodsStatus = "start";
        $scope.containerName = data.containerName;
        $scope.containerComplete = data.amountRebined + data.amountMissOfUnitLoad;
        $scope.containerTotal = data.amountTotal;
        $scope.rebinFromUnitLoadId = data.id;
        $scope.lostAmount = data.amountMissOfUnitLoad;
        $scope.lostBatchAmount = data.amountMissOfPickingOrder;
          $scope.lessGoodsAmount = "none";
        if($scope.lostAmount != 0){
            $scope.lessGoodsAmount = "less";
        }
        if($scope.lostBatchAmount != 0){
            $scope.lessBatchGoodsAmount = "less";
        }
        if ($scope.containerTotal == $scope.containerComplete) {
          $scope.pickingProcess = "next";
        }
          if ($scope.rebinEnd === false && $scope.batchComplete == $scope.batchTotal && $scope.rebinStart === true) {
              setTimeout(function () {
                  $("#hotKey").focus();
              }, 100);
          }else {
              setTimeout(function () {
                  $("#rebin_goods").focus();
              }, 100);
          }
        //}
      }, function(data){
          $scope.rebinErrorWindow.close();
          $scope.rebinError = data.values[0];
          if (data.key == "EX_MORE_SKU") {
              skuMore(data.values[0],data.values[1]);
          } else if (data.key == "EX_MORE_UNIT") {
              $scope.goods = name;
              unitMore();
          } else if($scope.pickingProcess == "scanFirstContainer" && $scope.rebinEnd===false){//扫描第一辆拣货车
              $scope.errorMsgShow = "firstContainer";
              openWindow({
                  id: "rebinErrorWindowId",
                  className: "myWindow",
                  width: 600,
                  title:"请重新扫描",
                  activate:function () {
                      $("#scanOneContainer").focus();
                  },
                  height: 260
              });
          }else{//扫描第二辆拣货车
              $scope.errorMsgShow = "secondContainer";
              openWindow({
                  id: "rebinErrorWindowId",
                  className: "myWindow",
                  width: 600,
                  title:"请重新扫描",
                  activate:function () {
                      $("#scanneTwoContainer").focus();
                  },
                  height: 260
              });
          }
          $scope.goods = "";
      });
      $scope[$scope.pickingProcess === "scanFirstContainer" ? "firstContainer" : "nextContainer"] = "";
    };

    // 扫描商品
    $scope.scanGoods = function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode != 13) return;
      $scope.doScanGoods();
    };

    $scope.doScanGoods = function (rebinException) {
      var souce = {
        id: $scope.requestId,
        rebinFromUnitLoadId: $scope.rebinFromUnitLoadId,
        itemNumber: $scope.goods
      };
      if (rebinException) {
        souce.rebinException = rebinException;
      };
      $scope.rebinException = rebinException;
      rebinService.scanGoods(souce, $scope.scanGoodsSuccess, function (data) {
        $scope.rebinGoodsStatus = "error";
        if (data.key == "EX_MORE_SKU") {
          skuMore(data.values[0],data.values[1]);
        } else if (data.key == "EX_MORE_UNIT") {
          unitMore();
        } else {
          rebinError(data.values[0],"scanGoodsId");
        }
          $scope.goods = "";
      });
    }

    $scope.scanGoodsSuccess = function (data) {
      $scope.whetherMoreGoodsSKUWindow.close();
      $scope.rebinErrorWindow.close();
      $scope.whetherMoreGoodsWindow.close();
      var pictureBackgroundColor = "";
      if($scope.rebinException == 'DAMAGED' || $scope.rebinException == 'CANNOT_SCAN'){
          pictureBackgroundColor = "#EF9112";
           $scope.pictureColor = "true";
      }else {
          pictureBackgroundColor = "#f2f2f2";
          $scope.pictureColor = "false";
      }
      $scope.rebinExSD = data.deliveryTime ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(data.deliveryTime)) : "";
      $scope.timeHour = (new Date(kendo.parseDate(data.deliveryTime)) - new Date()) / 3600000;
      $scope.exsdColor = exsdColor($scope.timeHour);
      $scope.exsdFontColor = exsdFontColor($scope.exsdColor);
      $scope.rebinWallName = data.rebinWallName;
      $scope.rebinGoods = data;
      rebinWallHighlight(data);
      // goods detail
      var grid = $("#rebinGoodsGrid").data("kendoGrid");
      grid.setOptions({
        "editable": false
      });
      grid.setDataSource(new kendo.data.DataSource({
        data: [data]
      }));
      // check 历史记录轮播 exsd 背景颜色
      $scope.rebinHistory.push({
        pictureBackgroundColor: pictureBackgroundColor,
        color: $scope.rebinColor,
        type: data.rebinWallIndex, ////////////第一辆车还是第二辆车
        name: data.rebinToCellName, //rebinwall 名字
        deliveryTime: $scope.rebinExSD, //EXSD
        deliveryTimeColor: $scope.exsdColor, //Exsd背景
        deliveryTimeFont: $scope.exsdFontColor //Exsd 文字
      });
      $scope.rebinHistory.map(function (data) {
        var hour = (new Date(data.deliveryTime) - new Date()) / 3600000;
        data.deliveryTimeColor = exsdColor(hour);
        data.deliveryTimeFont = exsdFontColor(data.deliveryTimeColor);
        return data;
      });
      $scope.containerComplete = data.rebinFromUnitLoad.amountRebined + data.rebinFromUnitLoad.amountMissOfUnitLoad;
      $scope.batchComplete = data.rebinFromUnitLoad.amountRebinedOfPickingOrder + data.rebinFromUnitLoad.amountMissOfPickingOrder;
      if ($scope.containerComplete == $scope.containerTotal) {
        //if ($scope.batchComplete == $scope.batchTotal) {}
        $scope.pickingProcess = "next";
          if ($scope.rebinEnd === false && $scope.batchComplete == $scope.batchTotal && $scope.rebinStart === true) {
              setTimeout(function () {
                  $("#hotKey").focus();
              }, 100);
          }else {
              setTimeout(function () {
                  $("#rebin_next").focus();
              }, 100);
          }
      }
        if($scope.lostAmount != 0){
            $scope.lessGoodsAmount = "less";
        }
        if($scope.lostBatchAmount != 0){
            $scope.lessBatchGoodsAmount = "less";
        }
      $scope.rebinGoodsStatus = "success";
      $scope.goods = "";
    }
    // 查看所有车辆进度
    $scope.readContainerProcess = function () {
      //
      rebinService.rebinfromUnitloads({
        requestId: $scope.requestId
      }, function (data) {
       var containerRebined = [];
       data.map(function (item) {
           containerRebined.push({
               containerName: item.containerName,
               amountRebined: item.amountRebined + item.amountMissOfUnitLoad,
               amountTotal: item.amountTotal
           });
       });
      $scope.containerDetails = containerRebined;
      });
      //
      openWindow({
        id: "rebin_containerDetailWin",
        className: "windowTitle",
        width: 600,
        height: 280
      });
    };

    //焦点锁定
    $scope.rebinFocus = function () {
      if ($scope.showPage == 'main') {
        if ($scope.pickingProcess === 'scanFirstContainer' && $scope.rebinEnd === false) {
          $("#rebin_first").focus();
        } else if ($scope.pickingProcess === 'scanGoods' && $scope.containerComplete != $scope.containerTotal && $scope.rebinEnd === false && $scope.batchComplete != $scope.batchTotal) {
          $("#rebin_goods").focus();
        } else if ($scope.pickingProcess === 'next' && $scope.containerComplete == $scope.containerTotal && $scope.rebinEnd === false && $scope.batchComplete != $scope.batchTotal) {
          $("#rebin_next").focus();
        }else if ($scope.rebinEnd === false && $scope.batchComplete == $scope.batchTotal && $scope.rebinStart === true) {
          $("#hotKey").focus();
        }
      }
    };

    // 问题菜单事件
    $scope.problemGoodKeydown = function ($event) {
      if ($event.keyCode != 13) return;
      $scope.problemGoods();
    };

    $scope.problemGoods = function () {
      $scope.goods = "";
      var key = $scope.problemGood.toUpperCase();
      if (key == "D") doGoodsDamage();
      else if (key == "P") doGoodsNotScan();
      else if (key == "I") doMessageSearch();
      else if (key == "M") doGoodsLost();
      else if (key == "E") doRebinEnd();
      else if (key == "F") finishRebin()
      $scope.problemMenuWindow.close();
    };

    //数量多货
    $scope.notFontLocation = function () {
     rebinService.getMoreItem($scope.requestId, $scope.rebinFromUnitLoadId,function () {//把放商品的货框的entityLock设置为1
         rebinService.reportProblem({
             "problemType": "MORE",
             "jobType": "Rebin",
             "amount": 1,
             "reportBy": $window.localStorage["username"],
             "reportDate": kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
             "problemStoragelocation": $scope.unitMoreContainerName,
             "container": $scope.unitMoreContainerName,
             "itemNo": $scope.moreItemPosition.itemNo,
             "skuNo":  $scope.moreItemPosition.skuNo,
             "lotNo": "",
             "serialNo": "",
             "itemDataId": $scope.moreItemPosition.id,
             "shipmentId": "",
             "state": ''
         }, function () {
             $scope.locationContent = "location";
             $scope.locationBnt = true;
             $scope.errorLocationBnt = true;
             $("#whetherMoreGoodsId").data("kendoWindow").setOptions({
                 activate:$timeout(function(){ $("#rescan_nextGoods").focus();},100)
             });

         });
      });
    }
    // sku多货
    $scope.whetherMoreGoodsSKUSure = function () {
      /*  $scope.whetherMoreGoods = "more";
        $scope.skuMoreSure = true;
        $scope.skuMoreCancel = true;
        setTimeout(function () {
            $("#reScanGoodsId").focus();
        }, 0);
        itemMoreArray.push({
            SKU:$scope.skuNo,
            itemName:$scope.itemName,
            amount:1,
            container:$scope.skuMoreContainerName
        });*/

      rebinService.getMoreItem($scope.requestId, $scope.rebinFromUnitLoadId,function () {//把放商品的货框的entityLock设置为1
        rebinService.reportProblem({
            "problemType": "MORE",
            "jobType": "Rebin",
            "amount": 1,
            "reportBy": $window.localStorage["username"],
            "reportDate": kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
            "problemStoragelocation": $scope.skuMoreContainerName,
            "container": $scope.skuMoreContainerName,
            "itemNo": $scope.itemNo,
            "skuNo": $scope.skuNo,
            "lotNo": "",
            "serialNo": "",
            "itemDataId": $scope.itemDataId,
            "shipmentId": "",
            "state": ''
        }, function () {
            $scope.whetherMoreGoods = "more";
            $scope.skuMoreSure = true;
            $scope.skuMoreCancel = true;
            $("#whetherMoreGoodsSKUId").data("kendoWindow").setOptions({
                activate:$timeout(function(){ $("#reScanGoodsId").focus();},100)
            });
        });
      })
    };
    // 继续rebin下一批次
    $scope.rebinContinueClick = function () {
      $rootScope.rebinContinue = true;
      $state.go("main.re_bin");
    };

    // ================================================内部函数=====================================================
    // 打开窗口
    function openWindow(options) {
      options.className && $("#" + options.id).parent().addClass(options.className);
      var win = $("#" + options.id).data("kendoWindow");
      win.setOptions({
        width: options.width,
        height: options.height,
        actions: ["Close"],
        open: options.open,
        title: options.title,
        close: $scope.rebinFocus //焦点锁定
      });
      options.activate && win.bind("activate", options.activate);
      win.center().open();
    }
   // 打开商品丢失窗口
    function openLostWindow(options) {
          options.className && $("#" + options.id).parent().addClass(options.className);
          var win = $("#" + options.id).data("kendoWindow");
          win.setOptions({
              width: options.width,
              height: options.height,
              actions: ["Close"],
              open: options.open,
              title: options.title,
              close: function () {
                  if( $scope.lessGoodsCancel == true && $scope.lessGoodsSure == true){//点击未找到商品按钮
                      if($scope.batchTotal == $scope.batchComplete){//已经完成所有批次
                          $scope.rebinEnd = false;
                          $scope.rebinStart = true;
                          $timeout(function () {
                              $("#hotKey").focus();
                          }, 100);
                      }else if(($scope.containerComplete == $scope.containerTotal) && ($scope.batchTotal != $scope.batchComplete)){//完成货框的商品
                          $scope.pickingProcess = "next";
                          $scope.rebinEnd = false;
                          $timeout(function () {
                              $("#rebin_next").focus();
                          }, 100);
                      }
                  }
              }
          });
          options.activate && win.bind("activate", options.activate);
          win.center().open();
      }
    // 错误信息
    function rebinError(data,inputId) {

      $scope.whetherMoreGoodsSKUWindow.close();
      $scope.rebinErrorWindow.close();
      $scope.whetherMoreGoodsWindow.close();
      $scope.rebinError = data;
      $scope.errorMsgShow = "good";
      openWindow({
        id: "rebinErrorWindowId",
        className: "myWindow",
        width: 600,
        title:"请重新扫描",
        activate: function () {
              $("#"+inputId).focus();
          },
        height: 280
      });
    }

    // 监听
    /*function listenReBin() {
      $("#rebin_parent").bind("keydown", function (e) {
        if ($scope.showPage != "main" || [68, 69, 73, 77, 80].indexOf(e.keyCode) < 0) return;
        openWindow({
          id: "rebin_problemWin",
          className: "problemMenu",
          width: 500,
          height: 320,
          open: function () {
            $scope.problemGood = e.key;
          }
        });
      });
    }*/

    // 问题菜单
    function initProblemMenu() {
      $("#rebin_questionMenu").kendoMenu({
        select: function (e) {
          var key = $(e.item).attr("key");
          if (key == "GOODS_DAMAGE") doGoodsDamage();
          else if (key == "GOODS_NOT_SCAN") doGoodsNotScan();
          else if (key == "MESSAGE_SEARCH") doMessageSearch();
          else if (key == "GOODS_LOSE") doGoodsLost();
          else if (key == "RE_BIN_FINISH") doRebinEnd();
          else if (key == "RE_BIN_STOP") finishRebin();
          /*else {
            var problemFocus = function () {
              $("#problemGoodsId").focus();
            };
            openWindow({
              id: "rebin_problemWin",
              className: "problemMenu",
              width: 500,
              height: 380,
              activate: problemFocus
            });
          }*/
        }
      });
    }
    //数量多货弹窗
    function unitMore() {
      $scope.whetherMoreGoodsSKUWindow.close();
      $scope.rebinErrorWindow.close();
      $scope.whetherMoreGoodsWindow.close();
      $scope.locationContent = "";
      $scope.locationBnt = false;
      $scope.errorLocationBnt = false;
      openWindow({
            id: "whetherMoreGoodsId",
            className: "myWindow",
            width: 800,
            height: 450,
            activate: function () {
              $("#rescan_nextGoods").focus();
            },
            open:function () {
                var columns = [{
                    field: "itemNo",
                    headerTemplate: "<span translate='SKU_NO'></span>"
                },
                    {
                        field: "itemName",
                        headerTemplate: "<span translate='GOODS_NAME'></span>"
                    },
                    {
                        headerTemplate: "<span translate='POSITION'></span>",
                        template: function (data) {
                            return data.rebinWallIndex + "-" + data.rebinCellName;
                        }
                    },
                    {
                        field: "amount",
                        headerTemplate: "<span translate='AMOUNT'></span>"
                    }
                ];
                $scope.whetherMoreGoodsGridOptions = {
                    editable: false,
                    height: 200,
                    scrollable: true,
                    columns: columns,
                    dataBound: function () {
                        var tr = $("#whetherMoreGoodsGrid tr")
                        rowMerge(tr);
                        var $table = $("#whetherMoreGoodsGrid tbody");
                        $table.append("<tr></tr>");
                    }
                };
                rebinService.rebinMore({
                    id: $scope.requestId,
                    rebinFromUnitLoadId: $scope.rebinFromUnitLoadId,
                    itemNumber: $scope.goods
                }, function (datas) {
                    $scope.goods = "";
                    $scope.unitMoreContainerName = datas.containerName;
                    $scope.unitMoreAmountInContainer = datas.amountMoreInContainer;
                    $scope.moreItemPosition = datas.moreItemPositionDTO;
                    var grid = $("#whetherMoreGoodsGrid").data("kendoGrid");
                    grid.setDataSource(new kendo.data.DataSource({
                        data: datas.moreItems
                    }));
                });
            }
        });
    }
    // sku多货查询弹窗
    function skuMore(data1,data2) {
        $scope.whetherMoreGoods = "none";
        $scope.whetherMoreGoodsSKUWindow.close();
        $scope.rebinErrorWindow.close();
        $scope.whetherMoreGoodsWindow.close();
        $scope.skuMoreContainerName = $scope.containerName;
        $scope.skuMoreAmountInContainer = data2;
        $scope.skuNo = data1.skuNo;
        $scope.itemName = data1.name;
        $scope.itemNo = data1.itemNo;
        $scope.itemDataId = data1.id;
        $scope.skuMoreSure = false;
        $scope.skuMoreCancel = false;
        openWindow({
          id: "whetherMoreGoodsSKUId",
          className: "myWindow",
          width: 800,
          activate: function () {
             $("#reScanGoodsId").focus();
          },
          height: 340,
         });
      }
    // 商品残损
    function doGoodsDamage() {
       $scope.scandamageItem = "none";
      openWindow({
        id: "damagedGoodsId",
        className: "myWindow",
        width: 540,
        height: 240,
        open: function () {
          setTimeout(function () {
            $("#rebin_damageGoods").focus();
          }, 500);
          $scope.damageGoods = "";
        }
      });
    }

    // 扫描残损商品
    $scope.scanDamageGoods = function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode != 13) return;
      $scope.scandamageItem = "none";
      rebinService.scanGoods({
        id: $scope.requestId,
        rebinFromUnitLoadId: $scope.rebinFromUnitLoadId,
        itemNumber: $scope.damageGoods,
        rebinException: "DAMAGED"
      }, function (data) {
        rebinService.reportProblem({
          "problemType": "DAMAGED",
          "jobType": "Rebin",
          "amount": 1,
          "reportBy": $window.localStorage["username"],
          "reportDate": kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
          "problemStoragelocation": $scope.containerName,
          "container": data.rebinToContainer,
          "itemNo": data.itemNumber,
          "skuNo":  data.skuNo,
          "lotNo": data.lotNo,
          "serialNo": data.serialNo,
          "itemDataId": data.itemId,
          "shipmentId": data.shipmentId,
          "state": ''
        }, function () {
            $scope.rebinException = "DAMAGED";
            $scope.scanGoodsSuccess(data);
          $scope.damagedGoodsWindow.close();
        });
         $scope.damageGoods = "";
      },function (data) {
          if (data.key == "EX_MORE_SKU") {
              skuMore(data.values[0],data.values[1]);
              $scope.damagedGoodsWindow.close();
          } else if (data.key == "EX_MORE_UNIT") {
              $scope.goods = $scope.damageGoods;
              unitMore();
              $scope.damagedGoodsWindow.close();
          } else {
              $scope.scandamageItem = "damage";
              $scope.damageErrorMsg = data.values[0];
          }
          $scope.damageGoods = "";
      });
    };

    // 商品无法扫描
    function doGoodsNotScan() {
      openWindow({
        id: "notScanGoodsId",
        className: "myWindow",
        width: 800,
        height: 320,
        open: function () {
          var columns = [{
              field: "itemNumber",
              width: "50px",
              headerTemplate: "<span translate='编号'></span>"
            },
            {
               field: "itemNo",
               width: "100px",
               headerTemplate: "<span translate='商品条码'></span>"
            },
            {
              field: "itemName",
              width: "300px",
              headerTemplate: "<span translate='商品'></span>"
            },
            {
              field: "amountScan",
              width: "50px",
              headerTemplate: "<span translate='扫描数'></span>"
            },
            {
              field: "amountTotal",
              width: "50px",
              headerTemplate: "<span translate='总数'></span>"
            },
            {
              field: "position",
              width: "80px",
              headerTemplate: "<span translate='POSITION'></span>"
            }
          ];
          $scope.notScanGoodsGridOptions = {
            editable: false,
            height: 200,
            scrollable: true,
            selectable: "row",
            columns: columns,
            dataBound: function () {
              var tr = $("#notScanGoodsGrid tr")
             /* rowMerge(tr);*/
            },
            change: function () {
              var grid = $("#notScanGoodsGrid").data("kendoGrid");
              var row = grid.select();
              var item = grid.dataItem(row);
              $scope.itemNo = item.itemNo;
              $scope.amount = item.amountTotal - item.amountScan;
            }
          };
          // 查询批次内所有无法扫描商品
          rebinService.getUnscanGoods({
            rebinFromUnitLoadId: $scope.rebinFromUnitLoadId,
            id: $scope.requestId,
          }, function (data) {
            var grid = $("#notScanGoodsGrid").data("kendoGrid");
            var notScanGoodsDetail = [];
            for(var i = 0; i<data.length; i++){
                notScanGoodsDetail.push({
                    itemNumber: i + 1,
                    itemNo: data[i].itemNo,
                    itemName: data[i].itemName,
                    amountScan: data[i].amountScan,
                    amountTotal: data[i].amountTotal,
                    rebinWallIndex: data[i].rebinWallIndex,
                    rebinToCellName: data[i].rebinToCellName,
                    position: data[i].rebinWallIndex +"-"+data[i].rebinToCellName,
                });
            }
            grid.setDataSource(new kendo.data.DataSource({
              data: notScanGoodsDetail
            }));
          });
        }
      });
    }

    // 商品无法扫描-确认
    $scope.notScanGoodsSure = function () {
        var souce = {
            id: $scope.requestId,
            rebinFromUnitLoadId: $scope.rebinFromUnitLoadId,
            itemNumber: $scope.itemNo,
            rebinException: "CANNOT_SCAN"
        };
        rebinService.scanGoods(souce, function (data) {
            rebinService.reportProblem({
                "problemType": "UNABLE_SCAN_SKU",
                "jobType": "Rebin",
                "amount": $scope.amount,
                "reportBy": $window.localStorage["username"],
                "reportDate": kendo.format("{0:yyyy-MM-hh HH:mm:ss}", new Date()),
                "problemStoragelocation": $scope.containerName,
                "container": data.rebinToContainer,
                "itemNo": data.itemNumber,
                "skuNo":  data.skuNo,
                "lotNo": data.lotNo,
                "serialNo": data.serialNo,
                "itemDataId": data.itemId,
                "shipmentId": data.shipmentId,
                "state": ''
            }, function () {
                $scope.rebinException = "CANNOT_SCAN";
                $scope.scanGoodsSuccess(data);
                $scope.notScanGoodsWindow.close()
            })
        });
    };

    // 信息查询
    function doMessageSearch() {
      rebinService.rebinResult($scope.requestId, function (data) {

        //var cellOneData = doMsgCal(data.rebinWall1Cells);
        var grid = $("#selectInformationGridId1").data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({
          data: data.rebinWall1Cells
        }));
        //var cellTwoData = doMsgCal(data.rebinWall2Cells);
        var grid = $("#selectInformationGridId2").data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({
          data: data.rebinWall2Cells
        }));
        $scope.userName = localStorage.getItem("name");
        openWindow({
          id: "selectInformationId",
          className: "windowTitle",
          width: 800,
          height: 480
        });
      });

    }

    //对两个rebincell的残品  无法扫描的计算
    function doMsgCal(data) {
        var cell = data;
        var cellData = [];
        var damageCell = "",notScanCell = "",cancelOrderCell = "",loseCell = "";
        var damageAmount = 0,notScanAmount = 0,cancelOrderAmount = 0,loseAmount = 0;
        var a = 0 ,b = 0,c = 0,d = 0;
        for(var i =0; i<cell.length; i++){
            if(cell[i].state == "DAMAGE"){
                a = 1;
                damageCell = damageCell + cell[i].rebinCellName + "&";
                damageAmount = damageAmount + cell[i].amountRebined;
            }else if(cell[i].state == "CANNOT_SCAN"){
                b = 1;
                notScanCell = notScanCell + cell[i].rebinCellName + "&";
                notScanAmount = notScanAmount + cell[i].amountRebined;
            }else if(cell[i].state == "CANCEL_BY_CUSTOMER"){
                c = 1;
                cancelOrderCell = cancelOrderCell + cell[i].rebinCellName + "&";
                cancelOrderAmount = cancelOrderAmount + cell[i].amountRebined;
            }else if(cell[i].state == "LOSE"){
                d = 1;
                loseCell = loseCell + cell[i].rebinCellName + "&";
                loseAmount = loseAmount + cell[i].amountRebined;
            }
        }
        if(a == 1){
            cellData.push({
                rebinCellName: damageCell.substr(0,damageCell.length-1),
                amountRebined: damageAmount,
                state: "残损"
            });
        }
        if(b == 1){
            cellData.push({
                rebinCellName: notScanCell.substr(0,notScanCell.length-1),
                amountRebined: notScanAmount,
                state: "无法扫描"
            });
        }
        if(c == 1){
            cellData.push({
                rebinCellName: cancelOrderCell.substr(0,cancelOrderCell.length-1),
                amountRebined: cancelOrderAmount,
                state: "客户删单"
            });
        }
        if(d == 1){
            cellData.push({
                rebinCellName: loseCell.substr(0,loseCell.length-1),
                amountRebined: loseAmount,
                state: "少货"
            });
        }
        return cellData;
    }
    // 商品丢失
    function doGoodsLost() {
        $scope.lessGoodsCancel = false;
        $scope.lessGoodsSure = false;
        $scope.whetherLessGoods = "none";
      openLostWindow({
        id: "whetherLessGoodsId",
        className: "myWindow",
        width: 800,
        height: 400,
        open: function () {
          var columns = [{
              field: "itemNo",
              headerTemplate: "<span translate='SKU_NO'></span>"
            },
            {
              field: "itemName",
              headerTemplate: "<span translate='GOODS_NAME'></span>"
            },
            {
              field: "amount",
              headerTemplate: "<span translate='AMOUNT'></span>"
            },
            {
              headerTemplate: "<span translate='POSITION'></span>",
              template: function (data) {
                    return data.rebinWallIndex + "-" + data.rebinCellName;
              }
            }
          ];
          $scope.whetherLessGoodsGridOptions = {
            editable: false,
            height: 200,
            scrollable: true,
           // selectable: "row",
            columns: columns,
            dataBound: function () {
                var tr = $("#whetherLessGoodsGrid tr");
                rowMerge(tr);
                var $table = $("#whetherLessGoodsGrid tbody");
                $table.append("<tr></tr>");
            }
          };
          // 查询记录
          rebinService.rebinLess({
              rebinFromUnitLoadId: $scope.rebinFromUnitLoadId,
              id: $scope.requestId,
              itemNumber:""
          },function (datas) {
            var grid = $("#whetherLessGoodsGrid").data("kendoGrid");
            $scope.lessContainerName = datas.containerName;
            $scope.lessAmountInContainer = datas.amountMoreInContainer;

            var missItemTotal = [];
            var missItemArr = datas.missItemDTO;
            for(var i = 0; i<missItemArr.length; i++){
              var missItem = missItemArr[i];
              for(var j = 0; j<missItem.length; j++){
                  missItemTotal.push(missItem[j]);
              }
            }
            grid.setDataSource(new kendo.data.DataSource({
              data: missItemTotal
            }));
            $scope.unrebinItems = datas.missPositionDTOS;  //报问题处理
          });
        }
      });
    }

    //提交丢失
    $scope.whetherLessGoodsCancel = function () {
      var progress = 0;
      var lossItems = $scope.unrebinItems.length;
      $scope.lessGoodsCancel = true;
      $scope.lessGoodsSure = true;
      $scope.whetherLessGoods = "less";
      var lostAmount = 0;
      rebinService.confirmLoseItem($scope.requestId,$scope.rebinFromUnitLoadId,function () {
          $scope.unrebinItems.map(function (item) {
              lostAmount = lostAmount + item.amount;
              rebinService.reportProblem({
                  "problemType": "LOSE",
                  "jobType": "Rebin",
                  "amount": item.amount,
                  "reportBy": $window.localStorage["username"],
                  "reportDate": kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                  "problemStoragelocation": $scope.containerName,
                  "container": $scope.containerName,
                  "itemNo": item.itemNo,
                  "skuNo":  item.skuNo,
                  "lotNo": item.lotNo,
                  "serialNo": item.serialNo,
                  "itemDataId": item.itemId,
                  "shipmentId": item.shipmentId,
                  "state": ''
              }, function () {
                  progress = progress + 1;
                  if(progress >= lossItems){
                      $scope.lostAmount = lostAmount;
                      $scope.lostBatchAmount = $scope.lostBatchAmount + lostAmount;
                      $scope.batchComplete = $scope.batchComplete + lostAmount;
                      $scope.containerComplete = $scope.containerComplete + lostAmount;
                      $scope.lessGoodsAmount = "less";
                      $scope.lessBatchGoodsAmount = "less";
                  }
              });
          })
      });
    };

     //rebind完成
    function finishRebin() {
          $rootScope.finishRebin();
    }
    // 结束rebin
    function doRebinEnd() {
      rebinService.rebinEnd($scope.requestId, function () {
        $scope.rebinEnd = true;
        rebinService.rebinResult($scope.requestId, function (data) {
           // skuMoreAmountGrid();
          //var cellOneData = doMsgCal(data.rebinWall1Cells);
          var grid = $("#rebinWallOneProblemGrid").data("kendoGrid");
          grid.setDataSource(new kendo.data.DataSource({
            data: data.rebinWall1Cells
          }));
         // var cellTwoData = doMsgCal(data.rebinWall2Cells);
          var grid = $("#rebinWallTwoProblemGrid").data("kendoGrid");
          grid.setDataSource(new kendo.data.DataSource({
            data: data.rebinWall2Cells
          }));
        /*  var grid = $("#rebinMoreGoodsGrid").data("kendoGrid");
          grid.setDataSource(new kendo.data.DataSource({
            data: data.moreItemDTOS
          }));*/
         $scope.itemMore = data.moreItemDTOS;
          }, function(data){
            $scope.errorMsgShow = "";
            rebinError(data.values[0],"")
        });
      }, function(data){
          $scope.errorMsgShow = "";
          rebinError(data.values[0])
      });
      $scope.problemMenuWindow.close();
    }

    //初始化多货商品提示grid
    function skuMoreAmountGrid() {
          var columns = [
              {
                  field: "itemNo",
                  width: "30px",
                  headerTemplate: "<span translate='SKU'></span>"
              },
              {
                  field: "itemName",
                  width: "50px",
                  headerTemplate: "<span translate='名称'></span>"
              },
              {
                  field: "amount",
                  width: "20px",
                  headerTemplate: "<span translate='数量'></span>"
              },
              {
                  field: "container",
                  width: "30px",
                  headerTemplate: "<span translate='容器'></span>"
              }
          ];
          $scope.rebinMoreGoodsOptions = outboundService.editGrid({
              editable: false,
              height: 200,
              columns: columns
          });
      }
    function rowMerge(tr) {
      for (var j = 0; j < 2; j++) {
        var step = 1;
        for (var i = 1; i < tr.length - 1;) {
          if ($($(tr[i]).find('td')[j]).text() == $($(tr[i + step]).find('td')[j]).text()) {
            if ($($(tr[i]).find('td')[j]).attr('rowSpan')) {
              $($(tr[i]).find('td')[j]).attr('rowSpan', parseInt($($(tr[i]).find('td')[j]).attr('rowSpan')) + 1);
              $($(tr[i + step]).find('td')[j]).hide();
              step++;
            } else {
              $($(tr[i]).find('td')[j]).attr('rowSpan', 2);
              $($(tr[i + step]).find('td')[j]).hide();
              step++;
            }
          } else {
            step = 1;
            i = i + step;
          }
        }
      }
    }

    // 取批次详细信息
    function getPickingOrder() {
      //判断批次绑定rebinWall信息
      if ($rootScope.rebinStation.rebinInfo) {
        $scope.rebinWallMessage1 = $rootScope.rebinStation.rebinInfo.rebinWallNames[0];
        if ($rootScope.rebinStation.rebinInfo.numRebinWall > 1) {
          $scope.rebinWallMessage2 = $rootScope.rebinStation.rebinInfo.rebinWallNames[1];
        }
        $scope.rebinWallNames = $rootScope.rebinStation.rebinInfo.rebinWallNames;
        $scope.containerNumber = $rootScope.rebinStation.rebinInfo.containerNames.join();
        $scope.ExSD = $rootScope.rebinStation.rebinInfo.deliveryTimes.map(function (params) {
          return params ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(params)) : ""
        }).join();
        $scope.rebinWallCount = $rootScope.rebinStation.rebinInfo.numRebinWall;
        $scope.batchNumber = $rootScope.rebinStation.rebinInfo.pickingOrderNumber;
        $scope.requestId = $rootScope.rebinStation.rebinInfo.id; ///requestId
      } else if ($rootScope.pickingOrder) {
        if ($rootScope.pickingOrder.rebinWallNames != 0) {
          $scope.rebinWallMessage1 = $rootScope.pickingOrder.rebinWallNames[0];
          if ($rootScope.pickingOrder.numRebinWall > 1) {
            $scope.rebinWallMessage2 = $rootScope.pickingOrder.rebinWallNames[1];
          }
          $scope.rebinWallNames = $rootScope.pickingOrder.rebinWallNames;
        }
        $scope.batchNumber = $rootScope.pickingOrder.pickingOrderNumber;
        $scope.rebinWallCount = $rootScope.pickingOrder.numRebinWall;
        $scope.containerNumber = $rootScope.pickingOrder.containerNames.join();
        $scope.ExSD = $rootScope.pickingOrder.deliveryTimes.map(function (params) {
          return params ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(params)) : ""
        }).join();
        $scope.requestId = $rootScope.pickingOrder.id; ///requestId
      }
      setTimeout(function () {
        $("#rebin_one").focus();
      }, 0); // 小车输入框获焦
      $scope.bindRebinError = '';
      $scope.rebinHistory = [];
      // 轮播初始化
      setTimeout(function () {
        new Swiper('.swiper-container', {
          slidesPerView: 7,
          observer: true, //修改swiper自己或子元素时，自动初始化swiper
          observeParents: true, //修改swiper的父元素时，自动初始化swiper
          prevButton: '.swiper-button-prev',
          nextButton: '.swiper-button-next',
          spaceBetween: 20
        })
      });
    }

    // 绑定wall跳转主页
    function toMain() {
      rebinService.rebinWalls({
        id: $scope.requestId,
        rebinWall1Id: $scope.rebinWall1Id,
        rebinWall2Id: $scope.rebinWall2Id
      }, function (data) {
        $scope.bindRebinError = '';
        $scope.pickingProcess = "scanFirstContainer"; // 扫描第一辆拣货车
        setTimeout(function () {
          $("#rebin_first").focus();
        }, 100); // 拣货车输入框获焦
        $scope.showPage = "main"; // 跳转主页
      }, function (data) {
        $scope.bindRebinError = data.values[0]
      });
    }

    // rebinEndGrid
    function initRebinEndGrid() {
      var columns = [{
          field: "rebinCellName",
          headerTemplate: "<span translate='REBIN_CELL'></span>"
        },
        {
          field: "amountRebined",
          headerTemplate: "<span>总数</span>"
        }
       /* {
          field: "state",
          width:60,
          headerTemplate: "<span>原因</span>"
        }*/
      ];

      $scope.rebinWallOneProblemOptions = outboundService.editGrid({
        editable: false,
        height: 200,
        columns: columns
      });
      $scope.rebinWallTwoProblemOptions = outboundService.editGrid({
        editable: false,
        height: 200,
        columns: columns
      });
    }

    // 初始化商品列表
    function initReBinGoodsGrid() {
      var columns = [{
          field: "unitName",
          width: "60px",
          headerTemplate: "<span translate='TYPE'></span>"
        },
        {
          field: "itemName",
          headerTemplate: "<span translate='NAME'></span>"
        },
        {
          field: "itemNumber",
          width: "120px",
          headerTemplate: "<span translate='SKU'></span>"
        },
        {
          width: "60px",
          headerTemplate: "<span translate='商品状态'></span>",
          template: function (item) {
            return item.amountItemRebined + "/" + item.amountItem;
          }
        },
        {
          field: "rebinToCellName",
          width: "60px",
          headerTemplate: "<span translate='目的地'></span>"
        },
        {
          width: "80px",
          headerTemplate: "<span translate='目的地状态'></span>",
          template: function (item) {
            var state = item.amountShipmentRebined + "/" + item.amountShipment;
            setTimeout(function () {
              var grid = $("#rebinGoodsGrid").data("kendoGrid");
              grid.tbody.find('tr').each(function () {
                var state = $(this).find('td:nth-child(6)').text().split("/");
                $(this).css("background", state[0] == state[1] ? "#c5e0b4" : "#f2f2f2")
              })
            }, 0);
            return state;
          }
        },
        {
          field: "rebinWallName",
          width: "90px",
          headerTemplate: "<span translate='Rebin车号码'></span>"
        }
      ];
      $scope.rebinGoodsGridOptions = outboundService.editGrid({
        editable: false,
        height: 95,
        columns: columns
      });
    }

    // 选择rebin格高亮
    function rebinWallHighlight(data) {
      var changeChar = function (char) {
        return Math.abs(65 - char.substring(0, 1).charCodeAt() + $scope.pickingOrder.numRow) - 1
      };
      initRebinWall();
      if (data.rebinWallIndex == 1) {
        $scope.rebinType = "one";
        $scope.rebinWallOneFolatHierarchy = data.rebinToCellName.substring(0, 1); //ABC  层级
        $scope.rebinWallOneFolatNumber = data.rebinToCellName.substring(1, 3); //123 排列
        $scope.rebinWallOneFolatHeight = parseInt(changeChar(data.rebinToCellName));
        $scope.rebinWallOneFolatWidth = parseInt($scope.rebinWallOneFolatNumber) - 1;
        $scope.rebinColor = FLOOR_COLOR[$scope.rebinWallOneFolatHierarchy];
        $scope.rebinWallOneRows[$scope.rebinWallOneFolatHeight].color = FLOOR_COLOR[$scope.rebinWallOneFolatHierarchy]; //行颜色
        $scope.rebinWallOneRows[$scope.rebinWallOneFolatHeight].item[$scope.rebinWallOneFolatWidth].name = data.rebinToCellName;
        $scope.rebinWallOneRows[$scope.rebinWallOneFolatHeight].item[$scope.rebinWallOneFolatWidth].choice = true;
      } else if (data.rebinWallIndex == 2) {
        $scope.rebinType = "two";
        $scope.rebinWallTwoFolatHierarchy = data.rebinToCellName.substring(0, 1); //ABC  层级
        $scope.rebinWallTwoFolatNumber = data.rebinToCellName.substring(1, 3); //123 排列
        $scope.rebinWallTwoFolatHeight = changeChar(data.rebinToCellName);
        $scope.rebinWallTwoFolatWidth = parseInt($scope.rebinWallTwoFolatNumber) - 1;
        $scope.rebinColor = FLOOR_COLOR[$scope.rebinWallOneFolatHierarchy];
        $scope.rebinWallTwoRows[$scope.rebinWallTwoFolatHeight].color = FLOOR_COLOR[$scope.rebinWallTwoFolatHierarchy]; //行颜色
        $scope.rebinWallTwoRows[$scope.rebinWallTwoFolatHeight].item[$scope.rebinWallTwoFolatWidth].name = data.rebinToCellName;
        $scope.rebinWallTwoRows[$scope.rebinWallTwoFolatHeight].item[$scope.rebinWallTwoFolatWidth].choice = true;
      }
    }

    // 初始化Rebin格子
    function initRebinWall() {
      var init = function (name) {
        angular.forEach(name, function (data) {
          data.color = "#c1c1c1";
          angular.forEach(data.item, function (data) {
            data.name = "";
            data.choice = false;
          })
        });
      };
      init($scope.rebinWallOneRows);
      init($scope.rebinWallTwoRows);
    }

    // exsd 背景
    function exsdColor(timeHour) {
      var color = "";
      if ($scope.timeHour > 12) {
        color = "#0066CD";
      } else if (($scope.timeHour < 12 && $scope.timeHour > 6) || $scope.timeHour == 12) {
        color = "#66CEFF";
      } else if (($scope.timeHour < 6 && $scope.timeHour > 3) || $scope.timeHour == 6) {
        color = "#FFFF01";
      } else if (($scope.timeHour < 3 && $scope.timeHour > 1) || $scope.timeHour == 3) {
        color = "#FF9901";
      } else if (($scope.timeHour < 1 && $scope.timeHour > 0) || $scope.timeHour == 1) {
        color = "#FF7C81";
      } else if ($scope.timeHour < 0 || $scope.timeHour == 0) {
        color = "#FF0000";
      }
      return color;
    }

    // exsd文字
    function exsdFontColor(time) {
      return ["#66CEFF", "#FFFF01"].indexOf(time) >= 0 ? "#000000" : "#FFFFFF";
    }

    //rebin基础初始化
    function initRebin() {
      $scope.rebinWall1Id = '';
      $scope.rebinWall2Id = '';
      $scope.rebinStart = false;
      $scope.rebinStation = $rootScope.rebinStation;
      if (!$rootScope.rebinStation.rebinInfo) {
        $scope.pickingOrder = $rootScope.pickingOrder;
      } else {
        $scope.pickingOrder = $rootScope.rebinStation.rebinInfo;
      }
      $scope.showPage = "container"; // 小车页面
      $scope.rebinEnd = false; //rebin结束
    }

    // 初始加载
    function init() {
      initRebin(); //rebin基础初始化
      // listenReBin();
      initProblemMenu();
      getPickingOrder(); // 取批次信息
      initReBinGoodsGrid(); // 商品信息表
      initRebinEndGrid(); //rebin结束grid
     // toMain(); // 临时测试
    }

    // =============================================================================================================
    init();
  })
})();