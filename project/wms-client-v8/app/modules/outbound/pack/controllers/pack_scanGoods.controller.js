/**
 * Created by feiyu.pan on 2017/4/17.
 * Updated by feiyu.pan on 2017/6/13.
 */
(function () {
  "use strict";

  angular.module('myApp').controller("packScanGoodsCtl", function ($scope, $window, $state, $rootScope,$interval,FLOOR_COLOR,
                                                                   webSocketService,BACKEND_CONFIG,OUTBOUND_CONSTANT,packService, outboundService, $translate) {
    $scope.packPage = "workstation"; //页面选择
    $scope.scanNumberShow = false; //扫描数量进度是否显示
    $scope.scanNumber = 0; //扫描进度
    $scope.packWeightEnable = true; //是否称重
    $scope.reBinCellColor = "#f2f2f2"; //reBin格颜色
    $scope.packProblemGoodsAmount = ""; //问题商品数量
    $scope.stationId = "";//工作站ID
    $scope.packProblem = "none";  //问题类型
    $scope.itemPicture = "";
    $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
    var lightStep = 0 ; //是否拍灯
    var socket;    //webSocket
    var weighStep = 0;
    setTimeout(function () {
      $("#workstationId").focus();
    }, 0);
    var date = new Date();
    //扫描工作站
    $scope.workstations = function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode == 13) {
        $scope.scanErrorMessage = "";
          $scope.stationName = $scope.workstation;
        packService.checkPackStation($scope.stationName, function (data) {
          if (data.packingStationTypeDTO.ifScan && data.packingStationTypeDTO.packingStationType == "Pick To Pack  Verify" ) {
            $scope.ifWeight = data.packingStationTypeDTO.ifWeight;
            $scope.packPage = "scanPickPackWall";
            $scope.stationId = data.packingStationTypeDTO.id;
            $scope.workStationId = data.workStationDTO.id;
              setTimeout(function () {
              $("#pickPackWallId").focus();
            }, 0);
            $rootScope.getIsCallPod($scope.workStationId);
            getLightResult();//拍灯获取结果
          } else {
            $scope.scanErrorMessage = "包装工具类型不符，请重新扫描工作站"
          }
        }, function (data) {
            $scope.scanErrorMessage = data.values[0];
        });
        $scope.workstation = ""
      }
    };
    //扫描pickPackWall
    $scope.scanPickPackWall = function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode == 13) {
        $scope.scanErrorMessage = "";
        $scope.pickPackWallName = $scope.pickPackWall;
        packService.checkPickPackWall($scope.pickPackWallName, $scope.workStationId,function (data) {
          $scope.packWeightEnable = true;
          $scope.pickPackWallData = data;
          $scope.packPage = "main";
          $scope.packType = "包装扫描商品站台-" + $scope.stationName;
          $rootScope.packStep = "checkPickPackCell";
          $rootScope.iptFocus();
          $scope.intervalQueryDigital();
          $scope.intervalQuery();
        }, function (data) {
                $scope.scanErrorMessage = data.values[0];
        });
        $scope.pickPackWall = "";
      }
    };

      //包装的灯亮的时候，推送过来灯的信息
      $scope.intervalQueryDigital = function () {
          $scope.getGoodsByCell = $interval(function () {
              if(lightStep == 2){
                  lightStep = 0;
                  console.log(date.toLocaleString()+"->亮灯的信息：",$scope.packLabelId);
                  $interval.cancel($scope.getGoodsByCell);
                  getdigitalLight($scope.packLabelId);
              }
          }, 500)
      }

      function getdigitalLight(digital) {
          packService.getCellName(digital, function (data) {
              $scope.reBinCellName = data.name;
              $scope.packPage = "main";
              $rootScope.packStep = "checkPickPackCell";
              $rootScope.iptFocus();
              $scope.cellStep = "showCell";
              weighStep = 1;
              $scope.getGoods1($scope.reBinCellName);
          },function (data) {
              $rootScope.errorMessage = data.values[0];
          })
      }

    //轮询获取PickPackCellName
        $scope.intervalQuery = function () {
            $scope.getPickPackCellName = $interval(function () {
              if(lightStep == 1){
                  lightStep = 0;
                  console.log(date.toLocaleString()+"->拍灯的信息：",$scope.labelId);
                  $interval.cancel($scope.getPickPackCellName);
                  getPickPackCellName($scope.labelId);
              }
        }, 100)
     }

     function getPickPackCellName(labelId) {
         packService.getCellName(labelId, function (data) {
             $scope.reBinCellName = data.name;
             $rootScope.packStep = "";
             weighStep = 0;
             $scope.getGoods($scope.reBinCellName);
         },function (data) {
             $rootScope.errorMessage = data.values[0];
         })
     }

      //拍灯，根据pickpackcellname获取商品信息
      $scope.checkCell = function (e) {
          var keyCode=window.event ? e.keyCode : e.which;
          if(keyCode == 13) {
              $scope.reBinCellName = $scope.pickPackCellName;
              weighStep = 0;
              $scope.getGoods($scope.reBinCellName);
              $scope.cellStep = "showCell";
              $scope.pickPackCellName = "";
          }
      }


    //grid表头
    var columns = [
      {field: "number", width: "40px", headerTemplate: "<span translate='编号'></span>"},
      {field: "itemNo", width: "140px", headerTemplate: "<span translate='商品条码'></span>"},
      {field: "name", headerTemplate: "<span translate='GOODS_NAME'></span>", attributes: {style: "text-align:left"}},
      {field: "scanAmount", width: "60px", headerTemplate: "<span translate='扫描数'></span>"},
      {field: "amount", width: "60px", headerTemplate: "<span translate='总数量'></span>"},
      //{field: "picture",width:"80px",headerTemplate: "<span translate='PICTURE'></span>" },
      {
        field: "remarks", width: "80px", headerTemplate: "<span translate='备注'></span>", template: function (item) {
        //返回备注
        return $translate.instant(item.remarks).replace("{0}", $scope.packProblemGoodsAmount.toString());
      }
      }];
    $scope.goodDetailsGridOptions = outboundService.reGrids("", columns, $(document.body).height() - 280);


    //获取商品信息
    $scope.getGoods = function (pickPackCellId) {
      $rootScope.errorMessage = "";
      packService.getGoods(pickPackCellId, $scope.stationName, "YES", function (data) {
          packService.updateDigitalShipment(data.shipmentDTO.id, $scope.stationName, function () {
              $scope.cellStep = "";
              console.log(date.toLocaleString()+"->拍灯获取订单信息：",data);
                getItem(data);
          });
      }, function (data) {
          if(data.key == "EX_SHIPMENT_ISNOT_ASSIGN"){
              $scope.shipmentRelation = data.values[0];
              $rootScope.scanCellWindow("shipmentStationId", $scope.noStationWindows);
          }else{
              $rootScope.errorMessage = data.values[0];
              $rootScope.scanCellWindow("scanCellWindowId", $scope.reScanCellWindow);
          }
          //$scope.packProblem = "errorDialog";
      });
    };
      $scope.getGoods1 = function (pickPackCellId) {
          $rootScope.errorMessage = "";
          packService.getGoods(pickPackCellId, $scope.stationName, "YES", function (data) {
              console.log(date.toLocaleString()+"->亮灯获取订单信息：",data);
              getItem(data);
          }, function (data) {
              $rootScope.errorMessage = data.values[0];
              $rootScope.scanCellWindow("scanCellWindowId", $scope.reScanCellWindow);
          });
      };
    function getItem(data) {
        $scope.reScanCellWindow.close();
        $rootScope.getIsCallPod($scope.workStationId);
        var goodDetails = [];
        $rootScope.packStep = "";
        $scope.packProblem = "none";
        $scope.shipmentState = data.shipmentDTO.state;
        $scope.problemShipment = data.shipmentDTO.shipmentNo;
        $scope.shipmentId = data.shipmentDTO.id;
        $scope.shipmentAccomplish = data.shipmentDTO.accomplish;
        if ($scope.shipmentState != 800) {
            $scope.scanNumberShow = false;
            $scope.packBox = data.shipmentDTO.boxType ? data.shipmentDTO.boxType.name : "";
            $scope.boxNameLength = $scope.packBox.length;
            $scope.shipmentNo = data.shipmentDTO.shipmentNo;
            $scope.packExsdTime = kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(data.shipmentDTO.deliveryDate));
            $scope.rebinCell = $scope.reBinCellName.substring($scope.reBinCellName.length - 5, $scope.reBinCellName.length);
            $scope.reBinCellColor = FLOOR_COLOR[$scope.rebinCell.substring(2, 3).toUpperCase()];
            if ($scope.reBinCellColor == "#F5EB2D" || $scope.reBinCellColor == "#CDE7D7" || $scope.reBinCellColor == "#B7D5EF" || $scope.reBinCellColor == "#B98FBF") {
                $scope.cellColor = "black";
            } else {
                $scope.cellColor = "white";
            }
            $rootScope.packOrderState = "";
            var time = (new Date($scope.packExsdTime) - new Date) / 3600000;
            $rootScope.packExsdColorChange(time);
            var box = data.shipmentDTO.boxType ? data.shipmentDTO.boxType.typeGroup : "";
            for (var i = 0; i < data.stockUnitDTOList.length; i++) {
                var bubble = data.stockUnitDTOList[i].itemDataDTO.useBubbleFilm
                if (box == "BOX") {
                    if (data.stockUnitDTOList[i].itemDataDTO.useBubbleFilm) {
                        bubble = false;
                    }
                }
                goodDetails.push({
                    number: i + 1,
                    id: data.stockUnitDTOList[i].id,
                    serialNo: data.stockUnitDTOList[i].serialNo || "",
                    serialRecordType: data.stockUnitDTOList[i].itemDataDTO.serialRecordType,
                    itemDataId: data.stockUnitDTOList[i].itemDataDTO.id,
                    itemNo: data.stockUnitDTOList[i].itemDataDTO.itemNo,
                    skuNo: data.stockUnitDTOList[i].itemDataDTO.skuNo,
                    name: data.stockUnitDTOList[i].itemDataDTO.name,
                    amount: data.stockUnitDTOList[i].amount,
                    scanAmount: 0,
                    useBubbleFilm: bubble,
                    lotNo: data.stockUnitDTOList[i].lotDTO ? data.stockUnitDTOList[i].lotDTO.lotNo : "",
                    remarks: "待扫描"
                });
            }
            var grid = $("#packGoodDetailsGrid").data("kendoGrid");
            grid.setOptions({
                dataSource: goodDetails, change: function () {
                    //弹出问题处理列表
                    var grid = $("#packGoodDetailsGrid").data("kendoGrid");
                    var row = grid.select();
                    $scope.packSelectedData = grid.dataItem(row);
                    if (parseInt($scope.packSelectedData.scanAmount) != parseInt($scope.packSelectedData.amount) && $rootScope.packStep !== "checkProblemShipment" && $rootScope.packStep !== "checkProblemContainer" && $rootScope.packStep != "") {
                        $rootScope.packProblemWindow("#problemMenu", $scope.problemMenuWindow);
                        $scope.itemDataId = $scope.packSelectedData.itemDataId;
                        $scope.itemNo = $scope.packSelectedData.itemNo;
                        $scope.skuNo = $scope.packSelectedData.skuNo;
                        $scope.itemDataName = $scope.packSelectedData.name;
                        $scope.uid = $scope.packSelectedData.uid;
                        $scope.serialNo = $scope.packSelectedData.serialNo;
                        $scope.lotNo = $scope.packSelectedData.lotNo;
                        $scope.amount = $scope.packSelectedData.amount;
                        $scope.scanAmount = $scope.packSelectedData.scanAmount;
                        $scope.scanNumber = $scope.scanAmount + "/" + $scope.amount;
                        $scope.scanNumberShow = true;
                        $scope.packProblemGoodsAmount = "";
                    }
                }, dataBound: function () {
                    // //改变grid行颜色
                    setTimeout(function () {
                        var grid = $("#packGoodDetailsGrid").data("kendoGrid");
                        grid.tbody.find('tr').each(function () {
                            if ($(this).find('td:eq(3)').text() == $(this).find('td:eq(4)').text()) {
                                $(this).css("background", "#c5e0b4")
                            } else if (parseInt($(this).find('td:eq(3)').text()) > 0 && parseInt($(this).find('td:eq(3)').text()) < parseInt($(this).find('td:eq(4)').text()) && $rootScope.packStep != "checkProblemShipment") {
                                $(this).css("background", "#deebf7")
                            } else if ($rootScope.packStep == "checkProblemShipment") {
                                $("#packGoodDetailsGrid tr").css("background", "#f2f2f2");
                                $("#packGoodDetailsGrid tr[data-uid=" + $scope.uid + "]").css("background", "#FBE5D6");
                            }
                        });
                    }, 0);
                }
            });
            if(weighStep == 0){
                if ($scope.ifWeight) {
                    console.log(date.toLocaleString()+"->称重weighStep:",weighStep);
                    $rootScope.packStepName = "请点击称重按钮，对订单进行称重";
                    $scope.packWeightEnable = false;
                } else {
                    $rootScope.packStep = "checkGoods";
                    $rootScope.packStepName = "请检查并扫描商品";
                    $rootScope.iptFocus();
                }
            }
        } else {
            $rootScope.packStep = "checkProblemContainer";
            $rootScope.packStepName = "订单已被删除，请将订单商品放置到问题货筐并扫描问题货筐";
            //$scope.packProblem = "errorDialog";
            $scope.titleMessage = "订单已被删除";
            //$rootScope.errorMessage = "此订单已被删除，请将订单内全部商品放置在问题处理货筐，请扫描问题货筐";
            $rootScope.iptFocus();

            $rootScope.errorMsg = "此订单已被删除，请将订单内全部商品放置在问题处理货筐，请扫描问题货筐";
            $rootScope.packErrorStep = "";
            $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemContainerTxtId");
        }
    }
    //称重
    $scope.packGoodsWeight = function () {
      //packService.getWeight($scope.stationId,function (data) {
      $scope.weight = 11;
      packService.weigh($scope.shipmentNo, $scope.weight, function () {
           if($scope.shipmentAccomplish == 2){
               $scope.packProblem = "none";
               $rootScope.packStep = "checkProblemContainer";
               $rootScope.packOrderState = "markComplete";
               $scope.cellColor = "black";
               $rootScope.packStepName = "请将订单商品放置到问题货筐，并扫描问题货筐条码";
               $scope.reBinCellColor = "#f2f2f2";
               $rootScope.iptFocus();
               $scope.packWeightEnable = true;
           }else {
              $rootScope.packStep = "checkGoods";
              $rootScope.packOrderState = "packing";
              $rootScope.packStepName = "请检查并扫描商品";
              $scope.packWeightEnable = true;
              $rootScope.iptFocus();
           }
      });
      //});
    };

    //检查扫描商品
    $scope.checkGoods = function (e) {
      var goodsNumber = 0;
      var itemNumber = 0;
      var keycode = window.event ? e.keyCode : e.which;
      var grid = $("#packGoodDetailsGrid").data("kendoGrid");
      var data = grid.dataSource.data();
      if (keycode == 13) {
        $scope.errorMsgWindow.close();
        $scope.packProblem = "none";
        $scope.itemNo = $scope.goods;
        for (var i = 0; i < data.length; i++) {
          if (data[i].itemNo == $scope.itemNo) {
            $scope.amount = data[i].amount;
            $scope.scanAmount = data[i].scanAmount;
            $scope.itemDataName = data[i].name;
            $scope.itemDataId = data[i].itemDataId;
            $scope.shipmentPositionId = data[i].id;
            $scope.serialRecordType = data[i].serialRecordType;
            $scope.serialNo = data[i].serialNo;
            $scope.useBubbleFilm = data[i].useBubbleFilm;
            $scope.lotNo = data[i].lotNo;
            if ($scope.amount < $scope.scanAmount) {
              //多货
              $rootScope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
              $scope.moreItemDataName = $scope.itemDataName;
              $scope.moreScanAmount = $scope.scanAmount;
              $scope.moreAmount = $scope.amount;
              $scope.moreData = {
                description: "",
                problemType: "MORE",
                amount: 1,
                jobType: 'Pack',
                reportBy: $window.localStorage["username"],
                reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                problemStoragelocation: $scope.reBinCellName,
                container: "",
                lotNo: $scope.lotNo,
                serialNo: $scope.serialNo,
                skuNo: $scope.skuNo,
                itemNo: $scope.itemNo,
                itemDataId: $scope.itemDataId,
                shipmentId: $scope.shipmentId,
                state: ""
              }

            }
          } else {
            goodsNumber++;
          }
        }
        //判断商品编号是否存在
        if (goodsNumber == data.length) {//表示输入的itemNo与要包装列表的商品都不相同
          packService.checkScanItem($scope.itemNo,$scope.reBinCellName, $scope.shipmentId,function (data1) {
              $scope.packProblem = "none";
              for (var i = 0; i < data.length; i++) {
                  if (data[i].itemNo == data1.itemDataDTO.itemNo) { //因扫描的可能是skuNo，当两者相等时，表示扫描的商品是要包装的商品
                      itemNumber++;
                  }
              }
              if(itemNumber == 0){ //扫描的商品不是订单中的商品，报多货
                  $rootScope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
                  $scope.moreItemDataName = data1.itemDataDTO.name;
                  $scope.moreScanAmount = 0;
                  $scope.moreAmount = 0;
                  var lot = "";
                  if(data1.LotDTO != null){
                    lot = data1.LotDTO.lotNo;
                  }
                  $scope.moreData = {
                      description: "",
                      problemType: "MORE",
                      amount: 1,
                      jobType: 'Pack',
                      reportBy: $window.localStorage["username"],
                      reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                      problemStoragelocation: $scope.reBinCellName,
                      container: "",
                      lotNo: lot,
                      serialNo: data1.serialNo,
                      skuNo: data1.itemDataDTO.skuNo,
                      itemNo: data1.itemDataDTO.itemNo,
                      itemDataId: data1.itemDataDTO.id,
                      shipmentId: $scope.shipmentId,
                      state: ""
                  }
                  $rootScope.packStep = "checkGoods";
                  $rootScope.packStepName = "请检查并扫描商品";
              }else{
                  $scope.skuData = {
                      stationName: $scope.stationName,
                      itemDataId: data1.itemDataDTO.id,
                      shipmentNo: $scope.shipmentNo,
                      cellName:$scope.reBinCellName,
                      type: "PACK"
                  };
                  checkSuccess(data1.itemDataDTO.itemNo);
              }

          }, function (data) {
            $rootScope.packStepName = "请重新扫描商品";
            //$scope.packProblem = "errorDialog";
            $scope.titleMessage = "请扫描其他有效条码";
            //$rootScope.errorMessage = data.values[0];
              $rootScope.errorMsg = data.values[0];
              $rootScope.packErrorStep = "checkGoods";
              $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkGoodsTxtId");
          })
        } else {
          $scope.skuData = {
            stationName: $scope.stationName,
            itemDataId: $scope.itemDataId,
            shipmentNo: $scope.shipmentNo,
            cellName:$scope.reBinCellName,
            type: "PACK"
          };
          checkSuccess($scope.itemNo);
          /*if ($scope.serialRecordType == "ALWAYS_RECORD") {
            $rootScope.packStep = "checkSerialNumber";
            $rootScope.packStepName = "请扫描商品序列号";
            $scope.packProblem = "serialNumber";
            $scope.serialNumberError = false;
            $rootScope.packOrderState = 'exception';
            $rootScope.iptFocus()
          } else {
            checkSuccess($scope.itemNo);
          }*/
        }
        $scope.goods = "";
      }
    };
    //扫描序列号
    /*$scope.checkSerialNumber = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        if ($scope.serialNumber == $scope.serialNo) {
          $scope.skuData["serialNo"] = $scope.serialNo;
          checkSuccess($scope.itemNo);
        } else {
          $scope.serialNumberError = true;
        }
        $scope.serialNumber = "";
      }
    };*/
      //检查完成
      function checkSuccess(itemNo) {
          //保存一条商品记录
          packService.packing($scope.skuData, function () {
              var totalCount = 0, numCount = 0;
              var grid = $("#packGoodDetailsGrid").data("kendoGrid");
              var data = grid.dataSource.data();
              $scope.packProblem = "none";
              $rootScope.packStep = "checkGoods";
              $rootScope.packStepName = "请检查并扫描商品";
              $rootScope.packOrderState = "scanning";
              var scanSum = 0;//判断有没有执行,确保每扫描一次，只对一件商品扫描
              for (var j = 0; j < data.length; j++) {
                  if (data[j].itemNo ==  itemNo) {
                      for(var i = 0; i< data.length; i++){
                          if(data[j].itemNo == data[i].itemNo){//同一个订单拆分为不同的position，所以有几个相同的商品条例
                              if(data[j].remarks == "扫描完成"){
                                  break;
                              }else{
                                  if(scanSum == 0){
                                      var scanAmount = data[j].scanAmount + 1;
                                      $scope.scanNumber = scanAmount + "/" + data[j].amount;
                                      $scope.scanNumberShow = true;
                                      if($scope.itemNo == "129714641165855" || $scope.itemNo == "6901939251607"){
                                          $scope.itemPicture = "kele";
                                      }else if($scope.itemNo == "517036749820530" || $scope.itemNo == "6902827110037"){
                                          $scope.itemPicture = "meinianda";
                                      }else if($scope.itemNo == "711209561729769" || $scope.itemNo == "6901939251201"){
                                          $scope.itemPicture = "xuebi";
                                      }else if($scope.itemNo == "235270135112427" || $scope.itemNo == "6921168511280"){
                                          $scope.itemPicture = "shanquan";
                                      }
                                      grid.dataSource.at(j).set("scanAmount", scanAmount);
                                      grid.dataSource.at(j).set("remarks", "正在扫描");
                                      if (data[j].scanAmount == data[j].amount) {
                                          grid.dataSource.at(j).set("remarks", "扫描完成");
                                      }
                                      scanSum++;
                                      break;
                                  }
                              }
                          }
                      }
                  }
                  totalCount += data[j].amount;
                  numCount += data[j].scanAmount;
              }
              //总数和扫描数相同
              if (numCount == totalCount) {
                  $rootScope.packStep = "checkShipment";
                  $rootScope.packStepName = "请扫描订单号码";
                  $rootScope.packOrderState = "scanShipment"
              }
              $rootScope.iptFocus()
          },function (data) {
              if(data.key == "EX_SHIPMENT_ITEM_IS_PACKED"){//有多件商品，扫描一件商品的总数大于customershipment的amount时，报多货
                  $scope.packProblem = "none";
                  var data1 = data.values[0];
                  var lotNo = "";
                  if(data1.LotDTO != null){
                      lotNo = data1.LotDTO.lotNo;
                  }
                  $rootScope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
                  $scope.moreItemDataName = data1.itemDataDTO.name;
                  $scope.moreScanAmount = data1.amount;
                  $scope.moreAmount = data1.amount;
                  $scope.moreData = {
                      description: "",
                      problemType: "MORE",
                      amount: 1,
                      jobType: 'Pack',
                      reportBy: $window.localStorage["username"],
                      reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                      problemStoragelocation: $scope.reBinCellName,
                      container: "",
                      lotNo: lotNo,
                      serialNo: data1.serialNo,
                      skuNo: data1.itemDataDTO.skuNo,
                      itemNo: data1.itemDataDTO.itemNo,
                      itemDataId: data1.itemDataDTO.id,
                      shipmentId: $scope.shipmentId,
                      state: ""
                  }
              }else{
                  //$scope.packProblem = "errorDialog";
                  //$scope.errorMessage = data.values[0];
                  $rootScope.errorMsg = data.values[0];
                  $rootScope.packErrorStep = "checkGoods";
                  $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkGoodsTxtId");
              }
          });
      }

      //扫描订单
    $scope.checkShipment = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.errorMsgWindow.close();
          $scope.shipmentValue = $scope.shipment;
        if ($scope.shipmentNo == $scope.shipment) {//扫描的shipment正确
          $scope.packProblem = "none";
          $rootScope.packStepName = "请扫描箱型号码";
          $rootScope.packStep = "checkBox";
          $rootScope.packOrderState = "scanBox";
          $rootScope.iptFocus()
        } else {
          packService.scanShimpment($scope.shipmentValue,$scope.reBinCellName, function (data){//扫描的shipment不正确，判断是否是商品
              $scope.packProblem = "none";
              $scope.moreItemDataName = data.itemDataDTO.name;
              $scope.moreScanAmount = data.amount;
              $scope.moreAmount = data.amount;
             /* var grid = $("#packGoodDetailsGrid").data("kendoGrid");//获取表格中的商品的信息
              var data1 = grid.dataSource.data();
              var scanNum = 0;
              var num = 0;
              for (var j = 0; j < data1.length; j++) {
                if(data1[j].itemNo == data.itemDataDTO.itemNo){//不是订单中的商品，scanNum和num为0，若是订单中的商品，获取订单中的数量
                   scanNum = data1[j].scanAmount;
                   num = data1[j].amount;
                }
              }*/

              var lotNo = "";
              if(data.LotDTO != null){
                  lotNo = data1.LotDTO.lotNo;
              }
              $rootScope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
              $scope.moreData = {
                  description: "",
                  problemType: "MORE",
                  amount: 1,
                  jobType: 'Pack',
                  reportBy: $window.localStorage["username"],
                  reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                  problemStoragelocation: $scope.reBinCellName,
                  container: "",
                  lotNo: lotNo,
                  serialNo: data.serialNo,
                  skuNo: data.itemDataDTO.skuNo,
                  itemNo: data.itemDataDTO.itemNo,
                  itemDataId: data.itemDataDTO.id,
                  shipmentId: $scope.shipmentId,
                  state: ""
              }
              $rootScope.packStep = "checkShipment";
              $rootScope.packStepName = "请重新扫描订单号码";
              $rootScope.packOrderState = "scanShipment"
          },function (data) {
              if(data.key == "EX_SHIPMENT_ISNOT_CURRENT_SHIPMENT"){
                  $rootScope.packStepName = "请重新扫描订单号码";
                  //$scope.packProblem = "errorDialog";
                  //$rootScope.errorMessage = data.values[0] + "并不是此订单对应的订单号码，请重新扫描"
                  $rootScope.errorMsg = $scope.shipmentValue + "并不是此订单对应的订单号码，请重新扫描"
                  $rootScope.packErrorStep = "checkShipment";
                  $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkShipmentTxtId");
              }
              if(data.key == "EX_SCANNING_IS_MANY"){
                  $rootScope.packStepName = "请重新扫描订单号码";
                  //$scope.packProblem = "errorDialog";
                  //$rootScope.errorMessage = data.values[0] + "是一个多条码，请重新扫描"

                  $rootScope.errorMsg = data.values[0] + "是一个多条码，请重新扫描";
                  $rootScope.packErrorStep = "checkShipment";
                  $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkShipmentTxtId");
              }
          });
        }
        $scope.shipment = "";
      }
    };
    //扫描箱号
    $scope.checkBox = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.errorMsgWindow.close();
        $scope.boxName = $scope.box;
        if($scope.boxName == null || $scope.boxName == undefined || $scope.boxName == ""){
            $scope.errorMsgWindow.close();
            $rootScope.packProblemWindow("#packBoxWindow", $scope.packBoxWindow);
        }else {
            if ($scope.box.toLowerCase() == $scope.packBox.toLowerCase()) {
              $scope.packFinish();
            } else {
                $scope.errorMsgWindow.close();
              $rootScope.packProblemWindow("#packBoxWindow", $scope.packBoxWindow);
            }
        }
        $scope.box = "";
      }
    };
    //结束包装
    $scope.packFinish = function () {
      $scope.packBoxWindow.close();
      $scope.errorMsgWindow.close();
      $scope.packProblem = "none";
      packService.packFinish($scope.shipmentNo, $scope.boxName,$scope.reBinCellName,"YES", function () {
        $rootScope.packStep = "";
        $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
        $rootScope.packOrderState = "orderSuccess";
        $scope.onARebinCell = $scope.rebinCell;//上一Rebin单元格
        $scope.onTheOrder = $scope.shipmentNo;//上一订单
        $scope.onTheCartonNo = $scope.boxName;//上一箱号
          $scope.packPage = "main";
          $rootScope.packStep = "checkPickPackCell";
          $rootScope.iptFocus();
          $scope.intervalQueryDigital();
          $scope.intervalQuery();
      }, function (data) {
        $rootScope.packStepName = "请重新扫描箱号";
        //$rootScope.packProblem = "errorDialog";
        //$rootScope.errorMessage = data.values[0];
          $rootScope.errorMsg = data.values[0];
          $rootScope.packErrorStep = "checkBox";
          $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkBoxTxtId");
      });
    };
    //确认商品无法扫描
    $scope.goodsCanNotScanSure = function () {
      $scope.packProblem = 'goodsCanNotScan';
      $rootScope.packStepName = "请确认商品是否无法扫描";
      $rootScope.packOrderState = 'problemProcessing';
      $scope.problemMenuWindow.close();
    };
    //确认多货
    $scope.moreGoodsSure = function () {
        packService.getMoreItem($scope.reBinCellName, function () {//把cell格子的entityLock设置为1
            packService.submitQuestion($scope.moreData, function () {
                $scope.packManyGoodsWindow.close();
            });
        });
    };
    //问题商品处理
    $scope.goodsProblemMenu = function (windowId, windowName, problemType) {
      $scope.packProblemGoodsAmount = "";
      $scope.problemShipment = "";

      //问题商品数量为1时直接提报
      if ($scope.packSelectedData.amount === 1 || ($scope.packSelectedData.amount - $scope.packSelectedData.scanAmount) === 1) {
        //提报问题商品
        $scope.packProblemGoodsAmount = 1;
        $rootScope.packProblemProcessing("#packGoodDetailsGrid", $scope.itemNo, problemType);
      } else {
        //问题商品数量输入页面
        $rootScope.packProblemWindow(windowId, windowName);
      }
    };
    //数字点击事件
    $scope.bind = function (x) {
      if (x < $scope.amount + 1) {
        if ($scope.amount < 10 && x != 0) {
          $scope.packProblemGoodsAmount = x;
        } else {
          $scope.packProblemGoodsAmount = $scope.packProblemGoodsAmount + x;
          if ($scope.packProblemGoodsAmount.substring(0, 1) == 0) {
            $scope.packProblemGoodsAmount = "";
          }
        }
      }
    };
    //删除数字
    $scope.backspace = function () {
      if ($scope.packProblemGoodsAmount.length > 1) {
        $scope.packProblemGoodsAmount = $scope.packProblemGoodsAmount.substring(0, $scope.packProblemGoodsAmount.length - 1)
      } else {
        $scope.packProblemGoodsAmount = "";
      }
    };
    //确定提报问题商品
    $scope.problemProcessingSure = function (problemType) {
        $scope.problemShipment = "";
        if(problemType == "GOODS_NOT_CAN_SCAN"){//无法扫描的数量等于总数减去已扫描的数量
            var row = $("#packGoodDetailsGrid").data("kendoGrid").select();
            $scope.packProblemGoodsAmount=(parseInt(row.find('td:eq(4)').text()) - parseInt(row.find('td:eq(3)').text()));
        }
          $("#reBinCellColorId").attr("color","black");
        $rootScope.packProblemProcessing("#packGoodDetailsGrid", $scope.itemNo, problemType);
    };
    //检查问题订单
    $scope.checkProblemShipment = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.errorMsgWindow.close();
        if ($scope.problemShipment == $scope.shipmentNo) {
          $scope.packProblem = "none";
          $rootScope.packStep = "checkProblemContainer";
          $rootScope.packOrderState = "markComplete";
          $scope.cellColor = "black";
          $rootScope.packStepName = "请将订单商品放置到问题货筐，并扫描问题货筐条码";
          $scope.reBinCellColor = "#f2f2f2";
          $rootScope.iptFocus()
        } else {
          $rootScope.packStepName = "请重新扫描订单号码";
          //$scope.packProblem = "errorDialog";
          //$rootScope.errorMessage = $scope.problemShipment + "并不是此订单对应的订单号码，请重新扫描"
          $rootScope.errorMsg = $scope.problemShipment + "并不是此订单对应的订单号码，请重新扫描"
          $rootScope.packErrorStep = "checkProblemShipment";
          $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemShipmentTxtId");
        }
        $scope.problemShipment = "";
      }
    };
    //提报问题
    $scope.checkProblemContainer = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.errorMsgWindow.close();
        var container = $scope.problemContainer;
        $scope.packProblem = "none";
        var stateType = "YES";
        if ($scope.shipmentState != 800) {
            if($scope.shipmentAccomplish == 2){
                packService.checkProblemShipment($scope.shipmentNo,function (data) {
                    $rootScope.itemType = "GOODS_LOSS";
                    $scope.itemDataId = data.itemData.id;
                    $scope.packProblemGoodsAmount = data.amount;
                    packService.checkProblemContainer($scope.shipmentNo, container, $rootScope.itemType, $scope.reBinCellName, stateType,$scope.itemDataId, parseInt($scope.packProblemGoodsAmount), function () {
                        $scope.problemData = {
                            description: "",
                            problemType: "LOSE",
                            amount: parseInt(data.amount),
                            jobType: 'Pack',
                            reportBy: $window.localStorage["username"],
                            reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                            problemStoragelocation: $scope.reBinCellName,
                            container: container,
                            lotNo: "",
                            serialNo: "",
                            skuNo: data.itemData.skuNo,
                            itemNo: data.itemData.itemNo,
                            itemDataId: data.itemData.id,
                            shipmentId: data.shipmentId,
                            state: ""
                        };
                        packService.submitQuestion($scope.problemData, function () {
                            $scope.packPage = "main";
                            $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
                            $rootScope.packStep = "checkPickPackCell";
                            $rootScope.iptFocus();
                            $scope.intervalQueryDigital();
                            $scope.intervalQuery();
                        });
                    },function (data) {
                        $rootScope.packStepName = "请更换问题货筐";
                        $rootScope.errorMsg = data.values[0];
                        $rootScope.packErrorStep = "checkProblemContainer";
                        $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemContainerTxtId");
                    })
                });
            }else{
                packService.checkProblemContainer($scope.shipmentNo, container, $rootScope.itemType, $scope.reBinCellName, stateType,$scope.itemDataId, parseInt($scope.packProblemGoodsAmount), function () {
                        $scope.problemData = {
                            description: "",
                            problemType: $rootScope.problemType,
                            amount: parseInt($scope.packProblemGoodsAmount),
                            jobType: 'Pack',
                            reportBy: $window.localStorage["username"],
                            reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                            problemStoragelocation: $scope.reBinCellName,
                            container: container,
                            lotNo: $scope.lotNo,
                            serialNo: $scope.serialNo,
                            skuNo: $scope.skuNo,
                            itemNo: $scope.itemNo,
                            itemDataId: $scope.itemDataId,
                            shipmentId: $scope.shipmentId,
                            state: ""
                        };
                    packService.submitQuestion($scope.problemData, function () {
                        $scope.packPage = "main";
                        $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
                        $rootScope.packStep = "checkPickPackCell";
                        $rootScope.iptFocus();
                        $scope.intervalQueryDigital();
                        $scope.intervalQuery();
                    });
                }, function (data) {
                    $rootScope.packStepName = "请更换问题货筐";
                    $rootScope.errorMsg = data.values[0];
                    $rootScope.packErrorStep = "checkProblemContainer";
                    $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemContainerTxtId");
                });
            }
        } else {
          packService.checkDeleteContainer($scope.problemShipment, $scope.problemContainer, $scope.reBinCellName, stateType, function () {
            $scope.problemData = {
              description: "",
              problemType: "DELETE_ORDER_CUSTOMER",
              amount: parseInt($scope.packProblemGoodsAmount),
              jobType: 'Pack',
              reportBy: $window.localStorage["username"],
              reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
              problemStoragelocation: $scope.reBinCellName,
              container: container,
              lotNo: "",
              serialNo: "",
              skuNo: "",
              itemNo: "",
              itemDataId: "",
              shipmentId: $scope.shipmentId,
              state: ""
            };
            packService.submitQuestion($scope.problemData, function () {
                $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
                $scope.packPage = "main";
                $rootScope.packStep = "checkPickPackCell";
                $rootScope.iptFocus();
                $scope.intervalQueryDigital();
                $scope.intervalQuery();
            });
          }, function (data) {
            $rootScope.packStepName = "请更换问题货筐";
              $rootScope.errorMsg = data.values[0];
              $rootScope.packErrorStep = "checkProblemContainer";
              $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemContainerTxtId");
          })
        }
        $scope.problemContainer = "";
      }
    };
      $scope.closeCellWindow = function () {
          $scope.reScanCellWindow.close();
          $rootScope.iptFocus();
      }
      $scope.closeErrorMsgWindow = function () {
          $scope.errorMsgWindow.close();
          $rootScope.iptFocus();
      }
      //websocket 获取按灯后的结果
      /*function getLightResult(){
          //var url = OUTBOUND_CONSTANT.webSocket+$scope.workStationId;
          var url = BACKEND_CONFIG.websocket+"websocket/ws/"+$scope.workStationId;
          console.log("url:",url);
          console.log("stationId",$scope.workStationId)
          socket = new WebSocket(url);
          //打开事件
          socket.onopen = function() { console.log("Socket 已打开"); };
          //获得消息事件
          socket.onmessage = function(msg) {
              var data = JSON.parse(msg.data);
              if(data.cmd == "1"){
                  lightStep = 1;
                  $scope.labelId = data.labelId;
                  console.log("拍的灯id："+data.labelId);
              }
              if(data.cmd == "2"){
                  lightStep = 2;
                  $scope.packLabelId = data.labelId;
                  console.log("亮灯id："+data.labelId);
              }
          };
          //关闭事件
          socket.onclose = function() {
              console.log("Socket已关闭");
             /!* if(socket.readyState != 1){
                  socket = new WebSocket(url);
                  if(socket.readyState != 1){
                      $rootScope.scanCellWindow("hardwareId",$scope.hardwareWindows);
                  }
              }*!/
          };
          //发生了错误事件
          socket.onerror = function() {
              console.log("发生了错误");
              socket = new WebSocket(url);
          }
      }*/
      function getLightResult() {
          var option={
              "user":$scope.workStationId,
              "url":"websocket/ws/"+$scope.workStationId,
              "onmessageCall":onmessageCall
          }
         // if($.isEmptyObject(socket)){
              socket=webSocketService.initSocket(option);
         // }
      }
      function onmessageCall(msg){
          //接收到消息后做的业务处理代码
          if(msg != "Success") {
              console.log(date.toLocaleString()+"->拍灯返回信息：", msg);
              var data = JSON.parse(msg);
              if (data.cmd == "1") {
                  lightStep = 1;
                  $scope.labelId = data.labelId;
                  console.log(date.toLocaleString()+"->推送拍灯id：" + data.labelId);
              }
              if (data.cmd == "2") {
                  lightStep = 2;
                  $scope.packLabelId = data.labelId;
                  console.log(date.toLocaleString()+"->推送亮灯id：" + data.labelId);
              }
          }
      };

      //关闭websocket
      function closeWebsocket(){
          if(!($.isEmptyObject(socket))){
              console.log(date.toLocaleString()+"->扫描包装客户端主动关闭websocket连接");
              socket.close(3666,"包装客户端主动关闭websocket连接");
          }
      };
      $scope.stopPack=function () {
          packService.stopPack($scope.stationName,function () {
              closeWebsocket();
              $state.go('main.pack')
          });
      }

    //信息查询
    $scope.informationInquiry = function () {
      packService.informationInquiry(function (data) {
        $scope.userName = $window.localStorage["username"];
        $scope.operationTime = data.operationTime;
        $scope.totalOperating = data.totalOperating;
        $scope.operationalEfficiency = data.operationalEfficiency;
        $scope.target = data.target;
        $scope.conclude = data.conclude;
        $scope.onAPod = data.onAPod;
        $scope.onAPallet = data.onAPallet;
        $rootScope.packProblemWindow('#informationInquiryId', $scope.informationInquiryWindow);
        $scope.problemProcessingWindow.close()
      })
    }

    $scope.getDigitalLabel = function () {
        $scope.problemProcessingWindow.close();
        $scope.digitallightWindows.close();
        packService.getDigitalLabel($scope.pickPackWallName,$scope.stationName,function (data) {
            getdigitalLight(data.digitalLabel);
        },function (data) {
            $scope.digitalLight = "digital";
            $rootScope.scanCellWindow("digitallightId",$scope.digitallightWindows);
        });
    }
  });
})();