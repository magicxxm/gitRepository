/**
 * Created by feiyu.pan on 2017/5/4.
 * Updated by feiyu.pan on 2017/6/13.
 */
(function () {
  "use strict";

  angular.module('myApp').controller("rebinPackCtl", function ($scope, $window, $state, rebinPackService, outboundService, $translate,packService,FLOOR_COLOR) {
    $scope.packExsdFontColor = "white";//exsd字体颜色
    $scope.packExsdColor = "#3b6dc7";//exsd颜色
    $scope.packStepName = "请检查并扫描商品"; //包装当前步骤名称
    $scope.packStep = ""; //包装步骤
    $scope.packProblemGoodsAmount = ""; //问题商品数量
    $scope.packProblem = "none";  //问题类型
    $scope.packPage = "workstation"; //页面选择
    $scope.scanNumberShow = false; //扫描数量进度是否显示
    $scope.scanNumber = 0; //扫描进度
    $scope.packWeightEnable = true; //是否称重
    $scope.reBinCellColor = "#f2f2f2"; //reBin格颜色R
    $scope.stationId="";
    $scope.scanWallStep = "";
    setTimeout(function () {
      $("#workstationId").focus();
    },0);

    //扫描工作站
    $scope.workstations = function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode == 13) {
        $scope.scanErrorMessage = "";
        $scope.stationName = $scope.workstation;
        rebinPackService.checkPackStation($scope.stationName, function (data) {
          if(data.packingStationTypeDTO.ifScan && data.packingStationTypeDTO.packingStationType == "RebinPack"){
            $scope.ifWeight=data.packingStationTypeDTO.ifWeight;
            $scope.stationId=data.packingStationTypeDTO.id;
            $scope.packPage = "scanReBinWall";
            setTimeout(function () {
              $("#rebinCarId").focus();
            },0);
          }else{
            $scope.scanErrorMessage = "包装工具类型不符，请重新扫描工作站"
          }
        }, function (data) {
          $scope.scanErrorMessage = data.values[0]
        });
        $scope.workstation = ""
      }
    };
    //扫描reBinWall
    $scope.scanReBinWall = function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode == 13) {
        $scope.scanErrorMessage = "";
        $scope.rebinWallName = $scope.rebinWall;
        $scope.packType="Rebin-Pack包装站台-"+$scope.stationName;
        $scope.scanWallStep = "firstScan";
        $scope.getGoods($scope.stationName, $scope.rebinWallName);
        $scope.rebinWall = ""
      }
    };
    //grid表头
    var columns = [
      {field: "number", width: "40px", headerTemplate: "<span translate='编号'></span>"},
      {field: "itemNo", width: "140px", headerTemplate: "<span translate='商品条码'></span>"},
      {field: "name", headerTemplate: "<span translate='GOODS_NAME'></span>", attributes: {style: "text-align:left"}},
      {field: "scanAmount", width: "60px", headerTemplate: "<span translate='扫描数'></span>"},
      {field: "amount", width: "60px", headerTemplate: "<span translate='总数量'></span>"},
      //{field: "picture",width:"80px",headerTemplate: "<span translate='PICTURE'></span>" },
      {field: "remarks", width: "80px", headerTemplate: "<span translate='备注'></span>", template: function (item) {
        //返回备注
        return $translate.instant(item.remarks).replace("{0}", $scope.packProblemGoodsAmount.toString());
      }}];
    $scope.goodDetailsGridOptions = outboundService.reGrids("", columns, $(document.body).height() - 280);
    //获取商品信息
    $scope.getGoods = function (stationName, rebinWallName) {
        $scope.RebinPackEndWindow.close();
      rebinPackService.checkRebinWall(stationName, rebinWallName, function (data) {
        $scope.packPage = "main";
        var goodDetails = [];
        $scope.packStep="";
        $scope.packProblem= "none";
        $scope.shipmentState=data.shipmentDTO.state;
        $scope.problemShipment=data.shipmentDTO.shipmentNo;
        $scope.shipmentId = data.shipmentDTO.id;
        $scope.rebinCell = data.reBinCellName;
        $scope.reBinCellName = $scope.rebinCell;
        $scope.rebinStorageName = rebinWallName+data.reBinCellName;
        $scope.reBinCellColor = FLOOR_COLOR[$scope.reBinCellName.substring(0, 1)];
        if($scope.reBinCellColor == "#F5EB2D" || $scope.reBinCellColor == "#CDE7D7" || $scope.reBinCellColor == "#B7D5EF" || $scope.reBinCellColor == "#B98FBF"){
              $scope.cellColor = "black";
        }else{
            $scope.cellColor = "white";
        }
        if($scope.shipmentState!=800) {
          $scope.scanNumberShow = false;
          $scope.packBox = data.shipmentDTO.boxType ? data.shipmentDTO.boxType.name : "";
          $scope.boxNameLength = $scope.packBox.length;
          $scope.shipmentNo = data.shipmentDTO.shipmentNo;
          $scope.shipmentId = data.shipmentDTO.id;
          $scope.packExsdTime = kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(data.shipmentDTO.deliveryDate));
          $scope.packOrderState = "scanning";
          var time = (new Date($scope.packExsdTime) - new Date) / 3600000;
          $scope.packExsdColorChange(time);
          var box = data.shipmentDTO.boxType ? data.shipmentDTO.boxType.typeGroup :"";
          for (var i = 0; i < data.stockUnitDTOList.length; i++) {
              var bubble = data.stockUnitDTOList[i].itemDataDTO.useBubbleFilm
              if(box == "BOX") {
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
              remarks: "TO_SCAN"
            });
          }
          var grid = $("#packGoodDetailsGrid").data("kendoGrid");
          grid.setOptions({
            dataSource: goodDetails, change: function () {
              //弹出问题处理列表
              var grid = $("#packGoodDetailsGrid").data("kendoGrid");
              var row = grid.select();
              $scope.packSelectedData = grid.dataItem(row);
              if (parseInt($scope.packSelectedData.scanAmount) != parseInt($scope.packSelectedData.amount) && $scope.packStep !== "checkProblemShipment" && $scope.packStep !== "checkProblemContainer" && $scope.packStep != "") {
                $scope.packProblemWindow("#problemMenu", $scope.problemMenuWindow);
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
                  } else if (parseInt($(this).find('td:eq(3)').text()) > 0 && parseInt($(this).find('td:eq(3)').text()) < parseInt($(this).find('td:eq(4)').text())&& $scope.packStep != "checkProblemShipment") {
                    $(this).css("background", "#deebf7")
                  } else if ($scope.packStep == "checkProblemShipment") {
                    $("#packGoodDetailsGrid tr").css("background", "#f2f2f2");
                    $("#packGoodDetailsGrid tr[data-uid=" + $scope.uid + "]").css("background", "#FBE5D6");
                  }
                });
              }, 0);
            }
          });
          if ($scope.ifWeight) {
            $scope.packStepName = "请将商品取出放置在电子秤上，点击称重按钮，对订单进行称重";
            $scope.packWeightEnable = false;
          } else {
            $scope.packStep = "checkGoods";
            $scope.packStepName = "请检查并扫描商品";
            $scope.iptFocus();
          }
        }else {
            $scope.packStep = "checkProblemContainer";
            $scope.packStepName = "订单已被删除，请将订单商品放置到问题货筐并扫描问题货筐";
            $scope.packProblem = "errorDialog";
            $scope.errorMessage = "此订单已被删除，请将订单内全部商品放置在问题处理货筐，请扫描问题货筐";
            $scope.iptFocus();
        }
      }, function (data) {
          if($scope.scanWallStep == "firstScan"){
              $scope.scanWallStep == "";
              $scope.scanErrorMessage = data.values[0]
          }else{
              $scope.packErrorStep = "checkRebinWall";
              $scope.nextRebinWall = data.values[0];
              $scope.errorWindow("RebinPackEndWindowId",$scope.RebinPackEndWindow,"checkRebinWallTxt1");
          }
      });

    };
    //称重
    $scope.packGoodsWeight = function () {
     // rebinPackService.getWeight($scope.stationId,function (data) {
        //$scope.weight = data;
          $scope.weight = 11;
        rebinPackService.weigh($scope.shipmentNo,$scope.weight,  function () {
          $scope.packStep = "checkGoods";
          $scope.packStepName = "请检查并扫描商品";
          $scope.packWeightEnable = true;
          $scope.iptFocus()
        });
   //   });
    };
   /* //检查扫描商品
    $scope.checkGoods = function (e) {
      var goodsNumber = 0;
      var keycode = window.event ? e.keyCode : e.which;
      var grid = $("#packGoodDetailsGrid").data("kendoGrid");
      var data = grid.dataSource.data();
      if (keycode == 13) {
        $scope.itemNo = $scope.goods;
        for (var i = 0; i < data.length; i++) {
          if (data[i].itemNo == $scope.itemNo) {
            $scope.itemDataId =data[i].itemDataId;
            $scope.shipmentPositionId = data[i].id;
            $scope.serialRecordType = data[i].serialRecordType;
            $scope.serialNo = data[i].serialNo;
            $scope.itemDataName = data[i].name;
            $scope.useBubbleFilm = data[i].useBubbleFilm;
            $scope.lotNo=data[i].lotNo;
            $scope.amount = data[i].amount;
            $scope.scanAmount = data[i].scanAmount;
            if (data[i].scanAmount == data[i].amount) {
              //多货
              $scope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
              $scope.moreItemDataName = $scope.itemDataName;
              $scope.moreScanAmount = scanAmount;
              $scope.moreAmount = amount;
              $scope.moreData = {
                description: "",
                problemType: "MORE",
                amount: 1,
                jobType: 'Pack',
                reportBy: $window.localStorage["name"],
                reportDate: kendo.format("{0:yyyy-MM-hh HH:mm:ss}", new Date()),
                problemStoragelocation: $scope.rebinCell,
                container: "",
                lotNo: $scope.lotNo,
                serialNo: $scope.serialNo,
                skuNo: $scope.skuNo,
                itemNo: $scope.itemNo,
                itemDataId: $scope.itemDataId,
                shipmentId: $scope.shipmentId,
                state:""
              }
            }
          } else {
            goodsNumber++;
          }
        }
        //判断商品编号是否存在
        if (goodsNumber == data.length) {
          packService.checkItem($scope.itemNo, function (data) {
            $scope.packProblem = "none";
            $scope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
            $scope.moreItemDataName = data.itemNo;
            $scope.moreScanAmount = 1;
            $scope.moreAmount = 0;
            $scope.moreData = {
              description: "",
              problemType: "MORE",
              amount: 1,
              jobType: 'Pack',
              reportBy: $window.localStorage["name"],
              reportDate: kendo.format("{0:yyyy-MM-hh HH:mm:ss}", new Date()),
              problemStoragelocation: $scope.reBinCellName,
              container: "",
              lotNo: "",
              serialNo:data.serialNo || "",
              skuNo: data.skuNo,
              itemNo: data.itemNo,
              itemDataId: data.id,
              shipmentId: $scope.shipmentId,
              state: ""
            }
          }, function (data) {
            $scope.packStepName = "请重新扫描商品";
            $scope.packProblem = "errorDialog";
            $scope.errorMessage = data.message
          })
        } else {
          $scope.skuData = {
            stationName: $scope.stationName,
            itemDataId: $scope.itemDataId,
            shipmentNo: $scope.shipmentNo,
            type:"REBIN"
          };
          if ($scope.serialRecordType == "ALWAYS_RECORD") {
            $scope.packStep = "checkSerialNumber";
            $scope.packStepName = "请扫描商品序列号";
            $scope.packProblem = "serialNumber";
            $scope.serialNumberError = false;
            $scope.packOrderState = 'exception';
            $scope.iptFocus()
          } else {
            checkSuccess();
          }
        }
        $scope.goods = "";
      }
    };*/
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
                          $scope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
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
                              problemStoragelocation: $scope.rebinStorageName,
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
                  rebinPackService.checkScanItem($scope.itemNo,$scope.rebinStorageName,$scope.shipmentId, function (data1) {
                      $scope.packProblem = "none";
                      for (var i = 0; i < data.length; i++) {
                          if (data[i].itemNo == data1.itemDataDTO.itemNo) { //因扫描的可能是skuNo，当两者相等时，表示扫描的商品是要包装的商品
                              itemNumber++;
                          }
                      }
                      if(itemNumber == 0){ //扫描的商品不是订单中的商品，报多货
                          $scope.moreItemDataName = data1.itemDataDTO.name;
                          $scope.moreScanAmount = 0;
                          $scope.moreAmount = 0;
                          $scope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
                          $scope.moreData = {
                              description: "",
                              problemType: "MORE",
                              amount: 1,
                              jobType: 'Pack',
                              reportBy: $window.localStorage["username"],
                              reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                              problemStoragelocation: $scope.rebinStorageName,
                              container: "",
                              lotNo: data1.LotDTO != null?data1.LotDTO.lotNo:"",
                              serialNo: data1.serialNo,
                              skuNo: data1.itemDataDTO.skuNo,
                              itemNo: data1.itemDataDTO.itemNo,
                              itemDataId: data1.itemDataDTO.id,
                              shipmentId: $scope.shipmentId,
                              state: ""
                          }
                          $scope.packStep = "checkGoods";
                          $scope.packStepName = "请检查并扫描商品";
                      }else{
                          $scope.skuData = {
                              stationName: $scope.stationName,
                              itemDataId: data1.itemDataDTO.id,
                              shipmentNo: $scope.shipmentNo,
                              cellName:$scope.rebinStorageName,
                              type: "REBIN"
                          };
                          $scope.itemDataId = data1.itemDataDTO.id;
                          // checkSuccess(data1.itemDataDTO.itemNo);
                          if (data1.itemDataDTO.serialRecordType == "ALWAYS_RECORD") {
                              /*$scope.packStep = "checkSerialNumber";
                               $scope.packStepName = "请扫描商品序列号";
                               $scope.packProblem = "serialNumber";
                               $scope.serialNumberError = false;
                               $scope.packOrderState = 'exception';
                               $scope.iptFocus()
                               */
                              $scope.packStepName = "请扫描商品序列号";
                              $scope.serialItemName = data1.itemDataDTO.skuNo;
                              $scope.itemDataName = data1.itemDataDTO.name;
                              $scope.serialNumberError = false;
                              $scope.scaSerialNO = "scanSerialNo";
                              $scope.itemNo = data1.itemDataDTO.itemNo;
                              $scope.skuNo = data1.itemDataDTO.skuNo;
                              $scope.lotNo = data1.LotDTO != null?data1.LotDTO.lotNo:"";
                              $scope.serialNo = data1.serialNo;
                              $scope.itemDataId = data1.itemDataDTO.id;
                              $scope.errorWindow("serialNumberWindowId",$scope.serialNumberWindow,"checkSerialNoId");
                          } else {
                              checkSuccess(data1.itemDataDTO.itemNo);
                          }
                      }

                  }, function (data) {
                      $scope.packStepName = "请重新扫描商品";
                      //$scope.packProblem = "errorDialog";
                      $scope.titleMessage = "请扫描其他有效条码";
                      //$scope.errorMessage = data.values[0];
                      $scope.packErrorStep = "checkGoods";
                      $scope.errorMsg = data.values[0];
                      $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkGoodsTxtId");
                  })
              } else {
                  $scope.skuData = {
                      stationName: $scope.stationName,
                      itemDataId: $scope.itemDataId,
                      shipmentNo: $scope.shipmentNo,
                      cellName:$scope.rebinStorageName,
                      type: "REBIN"
                  };
                if ($scope.serialRecordType == "ALWAYS_RECORD") {
                    /*$scope.packStep = "checkSerialNumber";
                    $scope.packStepName = "请扫描商品序列号";
                    $scope.packProblem = "serialNumber";
                    $scope.serialNumberError = false;
                    $scope.packOrderState = 'exception';
                    $scope.iptFocus()
*/                  $scope.packStepName = "请扫描商品序列号";
                    $scope.serialNumberError = false;
                    $scope.scaSerialNO = "scanSerialNo";
                    $scope.errorWindow("serialNumberWindowId",$scope.serialNumberWindow,"checkSerialNoId");
                 } else {
                  checkSuccess($scope.itemNo);
                 }
              }
              $scope.goods = "";
          }
      };
    //扫描序列号
    $scope.checkSerialNumber = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.serialNum = $scope.serialNumber;
          $scope.serialNumberWindow.close();
          rebinPackService.scanSerialNo($scope.itemDataId,$scope.serialNum,$scope.rebinStorageName,function (data) {
              $scope.skuData["serialNo"] = $scope.serialNo;
              checkSuccess($scope.itemNo);
          },function (data) {
              $scope.packStepName = "请扫描商品序列号";
              $scope.SN = $scope.serialNum;
              $scope.serialNumberError = true;
              $scope.scaSerialNO = "scanSerialNoError";
              $scope.errorWindow("serialNumberWindowId",$scope.serialNumberWindow,"checkSerialNoError");

          });
        /*if ($scope.serialNumber == $scope.serialNo) {
          $scope.skuData["serialNo"] = $scope.serialNo;
          checkSuccess();
        } else {
          $scope.serialNumberError = true;
        }*/
        $scope.serialNumber = "";
      }
    };
   /* //检查完成
    function checkSuccess() {
      //保存一条商品记录
      rebinPackService.packing($scope.skuData, function () {
        var totalCount = 0, numCount = 0;
        var grid = $("#packGoodDetailsGrid").data("kendoGrid");
        var data = grid.dataSource.data();
        $scope.packProblem = "none";
        $scope.packStep = "checkGoods";
        $scope.packStepName = "请检查并扫描商品";
        $scope.packOrderState = "scanning";
        for (var j = 0; j < data.length; j++) {
          if (data[j].itemNo == $scope.itemNo) {
            var scanAmount = data[j].scanAmount + 1;
            $scope.scanNumber = scanAmount + "/" + data[j].amount;
            $scope.scanNumberShow = true;
            grid.dataSource.at(j).set("scanAmount", scanAmount);
            grid.dataSource.at(j).set("remarks", "正在扫描");
            if (data[j].scanAmount == data[j].amount) {
              grid.dataSource.at(j).set("remarks", "扫描完成");
            }
          }
          totalCount += data[j].amount;
          numCount += data[j].scanAmount;
        }
        //总数和扫描数相同
        if (numCount == totalCount) {
          $scope.packStep = "checkShipment";
          $scope.packStepName = "请扫描订单号码";
          $scope.packOrderState = "scanShipment"
        }
        $scope.iptFocus()
      });
    }*/
      //检查完成
      function checkSuccess(itemNo) {
          //保存一条商品记录
          $scope.errorMsgWindow.close();
          rebinPackService.packing($scope.skuData, function () {
              var totalCount = 0, numCount = 0;
              var grid = $("#packGoodDetailsGrid").data("kendoGrid");
              var data = grid.dataSource.data();
              $scope.packProblem = "none";
              $scope.packStep = "checkGoods";
              $scope.packStepName = "请检查并扫描商品";
              $scope.packOrderState = "scanning";
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
                  $scope.packStep = "checkShipment";
                  $scope.packStepName = "请扫描订单号码";
                  $scope.packOrderState = "scanShipment"
              }
              $scope.iptFocus()
          },function (data) {
              if(data.key == "EX_SHIPMENT_ITEM_IS_PACKED"){//有多件商品，扫描一件商品的总数大于customershipment的amount时，报多货
                  $scope.packProblem = "none";
                  var data1 = data.values[0];
                  $scope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
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
                      problemStoragelocation: $scope.rebinStorageName,
                      container: "",
                      lotNo: data1.LotDTO != null?data1.LotDTO.lotNo:"",
                      serialNo: data1.serialNo,
                      skuNo: data1.itemDataDTO.skuNo,
                      itemNo: data1.itemDataDTO.itemNo,
                      itemDataId: data1.itemDataDTO.id,
                      shipmentId: $scope.shipmentId,
                      state: ""
                  }
              }else{
                  // $scope.packProblem = "errorDialog";
                  // $scope.errorMessage = data.values[0];
                  $scope.errorMsg = data.values[0];
                  $scope.packErrorStep = "checkGoods";
                  $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkGoodsTxtId");
              }
          });
      }
    /*//扫描订单
    $scope.checkShipment = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        if ($scope.shipmentNo == $scope.shipment) {
          $scope.packProblem = "none";
          $scope.packStepName = "请扫描箱型号码";
          $scope.packStep = "checkBox";
          $scope.packOrderState = "scanBox";
          $scope.iptFocus()
        } else {
          $scope.packStepName = "请重新扫描订单号码";
          $scope.packProblem = "errorDialog";
          $scope.errorMessage = $scope.shipment + "并不是此订单对应的订单号码，请重新扫描"
        }
        $scope.shipment = "";
      }
    };*/
      //扫描订单
      $scope.checkShipment = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              $scope.shipmentValue = $scope.shipment;
              $scope.errorMsgWindow.close();
              if ($scope.shipmentNo == $scope.shipment) {//扫描的shipment正确
                  $scope.packProblem = "none";
                  $scope.packStepName = "请扫描箱型号码";
                  $scope.packStep = "checkBox";
                  $scope.packOrderState = "scanBox";
                  $scope.iptFocus()
              } else {
                  packService.scanShimpment($scope.shipmentValue,$scope.rebinStorageName, function (data){//扫描的shipment不正确，判断是否是商品
                      $scope.packProblem = "none";
                      $scope.moreItemDataName = data.itemDataDTO.name;
                      $scope.moreScanAmount = data.amount;
                      $scope.moreAmount = data.amount;
                      $scope.packProblemWindow("#packManyGoods", $scope.packManyGoodsWindow);
                      $scope.moreData = {
                          description: "",
                          problemType: "MORE",
                          amount: 1,
                          jobType: 'Pack',
                          reportBy: $window.localStorage["username"],
                          reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
                          problemStoragelocation: $scope.rebinStorageName,
                          container: "",
                          lotNo: data.LotDTO != null?data.LotDTO.lotNo:"",
                          serialNo: data.serialNo,
                          skuNo: data.itemDataDTO.skuNo,
                          itemNo: data.itemDataDTO.itemNo,
                          itemDataId: data.itemDataDTO.id,
                          shipmentId: $scope.shipmentId,
                          state: ""
                      }
                      $scope.packStep = "checkShipment";
                      $scope.packStepName = "请重新扫描订单号码";
                      $scope.packOrderState = "scanShipment"
                  },function (data) {
                      if(data.key == "EX_SHIPMENT_ISNOT_CURRENT_SHIPMENT"){
                          $scope.packStepName = "请重新扫描订单号码";
                         /* $scope.packProblem = "errorDialog";
                          $scope.errorMessage = data.values[0] + "并不是此订单对应的订单号码，请重新扫描"*/
                          $scope.errorMsg = $scope.shipmentValue + "并不是此订单对应的订单号码，请重新扫描"
                          $scope.packErrorStep = "checkShipment";
                          $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkShipmentTxtId");
                      }
                      if(data.key == "EX_SCANNING_IS_MANY"){
                          $scope.packStepName = "请重新扫描订单号码";
                          /*$scope.packProblem = "errorDialog";
                          $scope.errorMessage = data.values[0] + "是一个多条码，请重新扫描"*/
                          $scope.errorMsg = data.values[0] + "是一个多条码，请重新扫描";
                          $scope.packErrorStep = "checkShipment";
                          $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkShipmentTxtId");
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
        $scope.boxName = $scope.box;
          if ($scope.box.toUpperCase() == $scope.packBox.toUpperCase()) {
          $scope.packFinish();
        } else {
          $scope.errorMsgWindow.close();
          $scope.packProblemWindow("#packBoxWindow", $scope.packBoxWindow);
        }
        $scope.box = "";
      }
    };
    //结束包装
    $scope.packFinish = function () {
        $scope.packBoxWindow.close();
        $scope.errorMsgWindow.close();
      rebinPackService.packFinish($scope.shipmentNo, $scope.boxName, $scope.rebinStorageName,"REBIN",function () {
        $scope.packProblem = "none";
        //$scope.packStep = "";
        $scope.packStep = "checkRebinWall";
        $scope.iptFocus();
        $scope.packStepName = "Rebin车完成，请扫描下一辆Rebin车继续操作";
        $scope.packOrderState = "orderSuccess";
        $scope.onARebinCell=$scope.rebinCell;//上一Rebin单元格
        $scope.onTheOrder=$scope.shipment;//上一订单
        $scope.onTheCartonNo=$scope.boxName;//上一箱号
        $scope.getGoods($scope.stationName, $scope.rebinWallName)
      },function (data) {
        $scope.packStepName = "请重新扫描箱号";
       /* $scope.packProblem = "errorDialog";
        $scope.errorMessage = data.values[0]*/
          $scope.errorMsg = data.values[0];
          $scope.packErrorStep = "checkBox";
          $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkBoxTxtId");
      });
    };
    //确认商品无法扫描
    $scope.goodsCanNotScanSure=function () {
      $scope.packProblem='goodsCanNotScan';
      $scope.packStepName="请确认商品是否无法扫描";
      $scope.packOrderState='problemProcessing';
      $scope.problemMenuWindow.close();
    };
    //确认多货
    $scope.moreGoodsSure = function () {
        packService.getMoreItem($scope.rebinStorageName, function () {//把cell格子的entityLock设置为1
            rebinPackService.submitQuestion($scope.moreData, function () {
                $scope.packManyGoodsWindow.close();
            });
        });
    };
    //问题商品处理
    $scope.goodsProblemMenu = function (windowId, windowName, problemType) {
        $scope.packProblemGoodsAmount = "";
        $scope.problemShipment = "";
      //问题商品数量为1时直接提报
      if ($scope.packSelectedData.amount === 1|| ($scope.packSelectedData.amount - $scope.packSelectedData.scanAmount) === 1) {
        //提报问题商品
        $scope.packProblemGoodsAmount = 1;
        $scope.packProblemProcessing("#packGoodDetailsGrid", $scope.itemNo, problemType);
      } else {
        //问题商品数量输入页面
        $scope.packProblemWindow(windowId, windowName)
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
        $scope.packProblemProcessing("#packGoodDetailsGrid", $scope.itemNo, problemType);
    };
    //检查问题订单
    $scope.checkProblemShipment = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.errorMsgWindow.close();
        if ($scope.problemShipment == $scope.shipmentNo) {
          $scope.packProblem = "none";
          $scope.packStepName="请将订单商品放置到问题货筐，并扫描问题货筐条码";
          $scope.packStep = "checkProblemContainer";
          $scope.packOrderState = "markComplete";
          $scope.cellColor = "black";
          $scope.reBinCellColor = "#f2f2f2";
          $scope.iptFocus()
        }else{
          $scope.packStepName = "请重新扫描订单号码";
          /*$scope.packProblem = "errorDialog";
          $scope.errorMessage = $scope.problemShipment + "并不是此订单对应的订单号码，请重新扫描"*/
            $scope.errorMsg = $scope.problemShipment + "并不是此订单对应的订单号码，请重新扫描"
            $scope.packErrorStep = "checkProblemShipment";
            $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemShipmentTxtId");
        }
        $scope.problemShipment = "";
      }
    };
    //提报问题
    $scope.checkProblemContainer=function (e) {
      var keycode=window.event ? e.keyCode : e.which;
      if(keycode == 13) {
        var container = $scope.problemContainer;
        $scope.packProblem = "none";
          $scope.errorMsgWindow.close();
        if ($scope.shipmentState != 800) {
          rebinPackService.checkProblemContainer($scope.shipmentNo, container, $scope.itemType,$scope.rebinStorageName,"REBIN",$scope.itemDataId, parseInt($scope.packProblemGoodsAmount),function () {
           // if ($scope.problemType != "MORE") {
              $scope.problemData = {
                description: "",
                problemType: $scope.problemType,
                amount: parseInt($scope.packProblemGoodsAmount),
                jobType: 'Pack',
                reportBy: $window.localStorage["username"],
                reportDate: kendo.format("{0:yyyy-MM-hh HH:mm:ss}", new Date()),
                problemStoragelocation: $scope.rebinStorageName,
                container: container,
                lotNo: $scope.lotNo,
                serialNo: $scope.serialNo,
                skuNo: $scope.skuNo,
                itemNo: $scope.itemNo,
                itemDataId: $scope.itemDataId,
                shipmentId: $scope.shipmentId,
                state: ""
              };
            // } else {
            //   $scope.problemData = $scope.moreData;
            //   $scope.problemData["container"] = container
            // }
            rebinPackService.submitQuestion($scope.problemData, function () {
                $scope.packStepName = "Rebin车完成，请扫描下一辆Rebin车继续操作";
                $scope.packPage = "main";
                //$scope.packStep = "";
                $scope.packStep = "checkRebinWall";
                $scope.iptFocus();
                $scope.getGoods($scope.stationName, $scope.rebinWallName)
            });
          }, function (data) {
              $scope.packStepName = "请更换问题货筐";
              /*$scope.packProblem = "errorDialog";
              $scope.errorMessage = data.values[0]*/

              $scope.errorMsg = data.values[0];
              $scope.packErrorStep = "checkProblemContainer";
              $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemContainerTxtId");
          });
        }else {
          rebinPackService.checkDeleteContainer($scope.problemShipment,$scope.problemContainer,$scope.rebinStorageName,"REBIN",function () {
            $scope.problemData = {
              description: "",
              problemType:"DELETE_ORDER_CUSTOMER",
              amount: parseInt($scope.packProblemGoodsAmount),
              jobType: 'Pack',
              reportBy: $window.localStorage["username"],
              reportDate: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date()),
              problemStoragelocation: $scope.rebinStorageName,
              container: container,
              lotNo: "",
              serialNo: "",
              skuNo: "",
              itemNo: "",
              itemDataId: "",
              shipmentId: $scope.shipmentId,
              state:""
            };
            rebinPackService.submitQuestion($scope.problemData,function () {
                $scope.packStepName = "Rebin车完成，请扫描下一辆Rebin车继续操作";
                $scope.packPage = "main";
                $scope.getGoods($scope.stationName, $scope.rebinWallName)
                $scope.packStep = "checkRebinWall";
                $scope.iptFocus();
            });
          },function (data) {
              $scope.packStepName = "请更换问题货筐";
           /* $scope.packProblem = "errorDialog";
              $scope.errorMessage = data.values[0]*/
              $scope.errorMsg = data.values[0];
              $scope.packErrorStep = "checkProblemContainer";
              $scope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemContainerTxtId");
          })
        }
        $scope.problemContainer = "";
      }
    };
    //结束包装
    $scope.stopPack=function () {
      rebinPackService.stopPack($scope.stationName,function () {
        $scope.packPage='workstation';
        $scope.problemProcessingWindow.close()
      });
    };

    //信息查询
    $scope.informationInquiry = function () {
      packService.informationInquiry(function (data) {
        $scope.userName = data.userName;
        $scope.operationTime = data.operationTime;
        $scope.totalOperating = data.totalOperating;
        $scope.operationalEfficiency = data.operationalEfficiency;
        $scope.target = data.target;
        $scope.conclude = data.conclude;
        $scope.onAPod = data.onAPod;
        $scope.onAPallet = data.onAPallet;
        $scope.packProblemWindow('#informationInquiryId', $scope.informationInquiryWindow);
        $scope.problemProcessingWindow.close()
      })
    };

      //根据距离发货时间改变exsd的颜色
      $scope.packExsdColorChange = function (time) {
          if (time > 12) {
              $scope.packExsdColor = "#0066CD";
          } else if ((time < 12 && time > 6) || time == 12) {
              $scope.packExsdFontColor = "black";
              $scope.packExsdColor = "#66CEFF";
          } else if ((time < 6 && time > 3) || time == 6) {
              $scope.packExsdFontColor = "black";
              $scope.packExsdColor = "#FFFF01";
          } else if ((time < 3 && time > 1) || time == 3) {
              $scope.packExsdColor = "#FF9901";
          } else if ((time < 1 && time > 0) || time == 1) {
              $scope.packExsdColor = "#FF7C81";
          } else if (time < 0 || time == 0) {
              $scope.packExsdColor = "#FF0000";
          }
      };
      //锁定输入框
      $scope.iptFocus = function () {
          if ($scope.packStep == 'checkGoods') {
              setTimeout(function () {
                  $("#checkGoodsTxt").focus();
              })
          } else if ($scope.packStep == 'checkSerialNumber') {
              setTimeout(function () {
                  $("#checkSerialNumberTxt").focus();
              }, 0)
          } else if ($scope.packStep == 'checkShipment') {
              setTimeout(function () {
                  $("#checkShipmentTxt").focus();
              }, 0)
          } else if ($scope.packStep == 'checkBox') {
              setTimeout(function () {
                  $("#checkBoxTxt").focus();
              }, 0)
          } else if ($scope.packStep == 'checkProblemShipment') {
              setTimeout(function () {
                  $("#problemShipmentTxt").focus();
              }, 0)
          } else if ($scope.packStep == 'checkProblemContainer') {
              setTimeout(function () {
                  $("#problemContainerTxt").focus();
              })
          }else if ($scope.packStep == 'checkRebinWall') {
              setTimeout(function () {
                  $("#checkRebinWallTxt").focus();
              })
          }else if($scope.packErrorStep == 'checkShipment'){
              setTimeout(function () {
                  $("#checkShipmentTxtId").focus();
              },0)
          }else if($scope.packErrorStep == 'checkBox'){
              setTimeout(function () {
                  $("#checkBoxTxtId").focus();
              },0)
          }else if($scope.packErrorStep == 'checkProblemShipment'){
              setTimeout(function () {
                  $("#problemShipmentTxtId").focus();
              },0)
          }else if($scope.packErrorStep == 'checkProblemContainer'){
              setTimeout(function () {
                  $("#problemContainerTxtId").focus();
              },0)
          }else if($scope.packErrorStep == 'checkGoods'){
              setTimeout(function () {
                  $("#checkGoodsTxtId").focus();
              },0)
          }else if($scope.packErrorStep == 'checkRebinWall'){
              setTimeout(function () {
                  $("#checkRebinWallTxt1").focus();
              },0)
          }
      };

      $scope.closeErrorMsgWindow = function () {
          $scope.errorMsgWindow.close();
          $rootScope.iptFocus();
      }
      //弹窗
      $scope.packProblemWindow = function (windowId, windowName) {
          $(windowId).parent().addClass("packProblemWindow");
          windowName.setOptions({
              width: 846,
              closable: true,
              close: function () {
                  $scope.iptFocus();
              }
          });
          windowName.center();
          windowName.open();
      };
      //弹窗
      $scope.problemWindow = function (windowId, windowName) {
          $("#" + windowId).parent().addClass("myWindow");
          windowName.setOptions({
              width:700,
              height:180,
              closable: true,
              close:function () {
                  $scope.iptFocus();
              }
          });
          windowName.center();
          windowName.open();
      };
      //弹窗
      $scope.checkWallWindow = function (windowId, windowName) {
          $("#" + windowId).parent().addClass("myWindow");
          windowName.setOptions({
              width:700,
              height:180,
              closable: true,
              close:function () {
                  $scope.packStep = "checkRebinWallTxt";
                  $scope.iptFocus();
              }
          });
          windowName.center();
          windowName.open();
      };
      //错误弹窗提示
      $scope.errorWindow = function (windowId, windowName,inputId) {
          $("#" + windowId).parent().addClass("myWindow");
          windowName.setOptions({
              width:700,
              height:300,
              closable: true,
              close:function () {
                  $scope.iptFocus();
              },
              activate:function () {
                  $("#"+inputId).focus();
              },
          });
          windowName.center();
          windowName.open();
      };
      //包装提报具体操作
      $scope.packProblemProcessing = function (gridName, name, problemType) {
          var grid = $(gridName).data("kendoGrid");
          var row = grid.select();
          $scope.packSelectedData = grid.dataItem(row);
          $scope.itemType = problemType;
          if(problemType!="MORE") {
              if(problemType == "SERIAL_NUMBER_CAN_NOT_SCAN"){
                  $scope.serialNumberWindow.close();
                  for (var i = 0; i < grid.dataSource.data().length; i++) {
                          if (grid.dataSource.data()[i].itemNo == name || $scope.serialItemName == name) {
                              $scope.uid = grid.dataSource.data()[i].uid;
                              grid.dataSource.at(i).set("remarks", problemType);
                              break;
                          }
                   }
              }else {
                  var a = $scope.packSelectedData.number;
                  grid.dataSource.at(a - 1).set("remarks", problemType);
              }
          }
          $scope.packStep = "checkProblemShipment";
          $scope.packOrderState = "problemProcessing";
          if (problemType == "GOODS_LOSS") {
              $scope.problemType = "LOSE";
              $scope.packStepName = "商品丢失标记完成，请扫描当前订单号码";
          } else if (problemType == "GOODS_DAMAGED") {
              $scope.problemType = "DAMAGED";
              $scope.packStepName = "商品残损标记完成，请扫描当前订单号码";
          } else if (problemType == "SERIAL_NUMBER_CAN_NOT_SCAN") {
              $scope.problemShipment = "";
              $scope.problemType = "UNABLE_SCAN_SN";
              $scope.iptFocus();
              $scope.packStepName = "已成功标记商品序列号无法扫描，请扫描当前订单号码";
          } else if (problemType == "GOODS_NOT_CAN_SCAN") {
              $scope.problemType = "UNABLE_SCAN_SKU";
              $scope.packStepName = "商品无法扫描标记完成，请扫描订单号码"
          }
      };
  });
})();