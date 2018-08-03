/**
 * Created by feiyu.pan on 2017/4/17.
 * Updated by feiyu.pan on 2017/6/13.
 */
(function () {
  "use strict";

  angular.module('myApp').controller("packNoScanGoodsCtl", function ($scope, $window, $state, $rootScope, $interval, packService,
                                                                     webSocketService,BACKEND_CONFIG,FLOOR_COLOR,OUTBOUND_CONSTANT,outboundService, $translate) {
    //$scope.mainHeight = $("#main").height();
    $scope.packPage = "workstation"; //页面选择
    $scope.scanNumberShow = false; //扫描数量进度是否显示
    $scope.packWeightEnable = true; //是否称重
    $scope.reBinCellColor = "#f2f2f2"; //reBin格颜色
    $scope.packProblemGoodsAmount = ""; //问题商品数量
    $scope.packSuccess=false; //是否包装完成
    $scope.stationId="";//stationId
    $scope.packProblem= "none";  //问题类型
    $rootScope.packStepName="请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
    var lightStep = 0 ; //是否拍灯
    var socket;//webSocket
    var weightStep = 0;
    var date = new Date();
    setTimeout(function () {
      $("#workstationId").focus();
    },0);
    //扫描工作站
    $scope.workstations=function (e) {
      var keyCode=window.event ? e.keyCode : e.which;
      if(keyCode == 13){
        $rootScope.stationName=$scope.workstation;
        packService.checkPackStation($scope.stationName,function (data) {
          $scope.workStationId = data.workStationDTO.id;
          $scope.scanErrorMessage="";
          if((!data.packingStationTypeDTO.ifScan) && (data.packingStationTypeDTO.packingStationType == "Pick To Pack Pack")){
            $scope.ifWeight=data.packingStationTypeDTO.ifWeight;
            $scope.packPage = "scanPickPackWall";
            $scope.stationId=data.packingStationTypeDTO.id;
            setTimeout(function () {
              $("#pickPackWallId").focus();
            },0);
              $rootScope.getIsCallPod($scope.workStationId);
              getLightResult();//打开webSocket
          }else{
            $scope.scanErrorMessage = "包装工具类型不符，请重新扫描工作站"
          }
        },function (data) {
            $scope.scanErrorMessage = data.values[0];
        });
        $scope.workstation=""
      }
    };
    //扫描pickPackWall
    $scope.scanPickPackWall=function (e) {
      var keyCode=window.event ? e.keyCode : e.which;
      if(keyCode == 13){
        $scope.pickPackWallName=$scope.pickPackWall;
        packService.checkPickPackWall($scope.pickPackWallName,$scope.workStationId,function (data) {
          $scope.scanErrorMessage="";
            $scope.pickPackWallData=data;
            $scope.packType="包装不扫描商品-"+$scope.stationName;
            $scope.packPage = "main";
            $rootScope.packStep = "checkPickPackCell";
            $scope.packWeightEnable = true;
            $rootScope.iptFocus();
            $scope.intervalQueryDigital();
            $scope.intervalQuery();
        },function (data) {
            $scope.scanErrorMessage = data.values[0];
        });
        $scope.pickPackWall="";
      }
    };
    /*  //轮询获取可包装的订单
      $scope.intervalPackShipment = function () {
          $scope.getPickedShipment = $interval(function () {
              packService.getDigitalShipment($scope.pickPackWallName,$scope.stationName,function (data) {
                  if(data != ""){
                      $interval.cancel($scope.getPickedShipment);
                      sendMessage(data.digegitalLabel2);
                      $scope.intervalQuery();
                  }
              });
          },1000);
      }*/

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
              weightStep = 1;
              $scope.cellStep = "showCell";
              $scope.getGoods1($scope.reBinCellName);
              $scope.packPage = "main";
              $rootScope.packStep = "checkPickPackCell";
              $rootScope.iptFocus();
          },function (data) {
              $rootScope.errorMessage = data.values[0];
          })
      }

      //拍灯轮询获取PickPackCellName
      $scope.intervalQuery = function () {
          $scope.getPickPackCellName = $interval(function () {
              if(lightStep == 1){
                  lightStep = 0;
                  console.log(date.toLocaleString()+"->拍灯的信息：",$scope.labelId);
                  $interval.cancel($scope.getPickPackCellName);
                  packService.getCellName($scope.labelId, function (data) {
                      weightStep = 0;
                      $rootScope.packStep = "";
                      $scope.reBinCellName = data.name;
                      $scope.getGoods($scope.reBinCellName);
                  },function (data) {
                      $rootScope.errorMessage = data.values[0];
                  })
              }
          }, 500)
      }

   //拍灯，根据pickpackcellname获取商品信息
      $scope.checkCell = function (e) {
          var keyCode=window.event ? e.keyCode : e.which;
          if(keyCode == 13) {
              weightStep = 0;
              $interval.cancel($scope.getPickPackCellName);
              $scope.reBinCellName = $scope.pickPackCellName;
              $scope.getGoods($scope.reBinCellName);
              $scope.pickPackCellName = "";
          }
      }
    //grid表头
    var columns = [
      {field: "number", width: "40px", headerTemplate: "<span translate='编号'></span>"},
      {field: "itemNo", width: "140px", headerTemplate: "<span translate='商品条码'></span>"},
      {field: "name", headerTemplate: "<span translate='GOODS_NAME'></span>", attributes: {style: "text-align:left"}},
      {field: "problemAmount", width: "60px", headerTemplate: "<span translate='问题数'></span>"},
      {field: "amount", width: "60px", headerTemplate: "<span translate='总数量'></span>"},
      //{field: "picture",width:"80px",headerTemplate: "<span translate='PICTURE'></span>" },
      {field: "remarks", width: "80px", headerTemplate: "<span translate='备注'></span>", template: function (item) {
        //返回备注
        return $translate.instant(item.remarks).replace("{0}", $scope.packProblemGoodsAmount.toString());
      }}];
    $scope.goodDetailsGridOptions = outboundService.reGrids("", columns, $(document.body).height() - 280);

    //获取商品信息
    $scope.getGoods=function (pickPackCellId) {
        packService.getGoods(pickPackCellId,$scope.stationName,"NO",function (data) {
            packService.updateDigitalShipment(data.shipmentDTO.id,$scope.stationName,function () {
                console.log(date.toLocaleString()+"->拍灯获取订单信息：",data);
                $scope.cellStep = "";
                getItem(data);
       });
      },function (data) {
         // $scope.packProblem = "errorDialog";
         /* $rootScope.errorMessage = data.values[0];
          $rootScope.scanCellWindow("scanCellWindowId",$scope.reScanCellWindow);*/
            if(data.key == "EX_SHIPMENT_ISNOT_ASSIGN"){
                $scope.shipmentRelation = data.values[0];
                $rootScope.scanCellWindow("shipmentStationId", $scope.noStationWindows);
            }else{
                $rootScope.errorMessage = data.values[0];
                $rootScope.scanCellWindow("scanCellWindowId", $scope.reScanCellWindow);
            }
      });
    };

      $scope.getGoods1=function (pickPackCellId) {
          packService.getGoods(pickPackCellId,$scope.stationName,"NO",function (data) {
              console.log(date.toLocaleString()+"->亮灯获取订单信息：",data);
              getItem(data);
          },function (data) {
              // $scope.packProblem = "errorDialog";
              $rootScope.errorMessage = data.values[0];
              $rootScope.scanCellWindow("scanCellWindowId",$scope.reScanCellWindow);
          });
      };
    function getItem(data) {
        $scope.reScanCellWindow.close();
        $rootScope.getIsCallPod($scope.workStationId);
        $scope.packSuccess = false;
        $scope.packProblem= "none";
        $rootScope.iptFocus();
        var goodDetails = [];
        $rootScope.packStep="";
        $scope.shipmentState=data.shipmentDTO.state;
        $scope.problemShipment=data.shipmentDTO.shipmentNo;
        $scope.shipmentId = data.shipmentDTO.id;
        $scope.shipmentAccomplish = data.shipmentDTO.accomplish;
        if( $scope.shipmentState!=800) {
            $scope.scanNumberShow = false;
            $scope.packBox = data.shipmentDTO.boxType ? data.shipmentDTO.boxType.name:"";
            $scope.boxNameLength = $scope.packBox.length;
            $scope.shipmentNo = data.shipmentDTO.shipmentNo;
            $scope.packExsdTime = kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(data.shipmentDTO.deliveryDate));
            $scope.rebinCell = $scope.reBinCellName.substring($scope.reBinCellName.length-5,$scope.reBinCellName.length);
            $scope.reBinCellColor = FLOOR_COLOR[$scope.rebinCell.substring(2, 3).toUpperCase()];
            if($scope.reBinCellColor == "#F5EB2D" || $scope.reBinCellColor == "#CDE7D7" || $scope.reBinCellColor == "#B7D5EF" || $scope.reBinCellColor == "#B98FBF"){
                $scope.cellColor = "black";
            }else{
                $scope.cellColor = "white";
            }
            $rootScope.packOrderState = "packing";
            var time = (new Date($scope.packExsdTime) - new Date) / 3600000;
            $rootScope.packExsdColorChange(time);
            var box = data.shipmentDTO.boxType ? data.shipmentDTO.boxType.typeGroup : "";
            for (var i = 0; i < data.stockUnitDTOList.length; i++) {
                var bubble = data.stockUnitDTOList[i].itemDataDTO.useBubbleFilm
                if(box == "BOX") {
                    $scope.useBubbleFilm = false;
                }else {
                    if(bubble){
                        $scope.useBubbleFilm = true;
                    }else {
                        $scope.useBubbleFilm = false;
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
                    problemAmount: 0,
                    // useBubbleFilm: bubble,
                    lotNo: data.stockUnitDTOList[i].lotDTO ? data.stockUnitDTOList[i].lotDTO.lotNo : "" ,
                    remarks: ""
                });
            }
            var grid = $("#packGoodDetailsGrid").data("kendoGrid");
            grid.setOptions({
                dataSource: goodDetails, change: function () {
                    //弹出问题处理列表
                    var grid = $("#packGoodDetailsGrid").data("kendoGrid");
                    var row = grid.select();
                    $scope.packSelectedData = grid.dataItem(row);
                    if ($scope.packStep == "checkShipment") {
                        $rootScope.packProblemWindow("#problemMenu", $scope.problemMenuWindow);
                        $scope.itemDataId = $scope.packSelectedData.itemDataId;
                        $scope.itemNo = $scope.packSelectedData.itemNo;
                        $scope.skuNo = $scope.packSelectedData.skuNo;
                        $scope.itemDataName = $scope.packSelectedData.name;
                        $scope.serialNo = $scope.packSelectedData.serialNo;
                        $scope.uid = $scope.packSelectedData.uid;
                        $scope.lotNo = $scope.packSelectedData.lotNo;
                        $scope.amount = $scope.packSelectedData.amount;
                        $scope.problemAmount = $scope.amount;
                        $scope.scanNumberShow = true;
                        $scope.packProblemGoodsAmount = "";
                    }
                }, dataBound: function () {
                    // //改变grid行颜色
                    setTimeout(function () {
                        var grid = $("#packGoodDetailsGrid").data("kendoGrid");
                        grid.tbody.find('tr').each(function () {
                            if ($scope.packSuccess) {
                                $(this).css("background", "#c5e0b4")
                            } else if ($rootScope.packStep == "checkProblemShipment") {
                                $("#pac kGoodDetailsGrid tr").css("background", "#f2f2f2");
                                $("#packGoodDetailsGrid tr[data-uid=" + $scope.uid + "]").css("background", "#FBE5D6");
                            }
                        });
                    }, 0);
                }
            });
            if(weightStep == 0){
                if ($scope.ifWeight) {
                    console.log(date.toLocaleString()+"->称重weighStep:",weightStep);
                    $rootScope.packStepName = "请点击称重按钮，对订单进行称重";
                    $scope.packWeightEnable = false;
                } else {
                    $rootScope.packStep = "checkGoods";
                    $rootScope.packStepName = "请检查并扫描商品";
                    $rootScope.iptFocus();
                }
            }
        }else{
            $rootScope.packStep = "checkProblemContainer";
            $rootScope.packStepName = "订单已被删除，请将订单商品放置到问题货筐并扫描问题货筐";
            $rootScope.errorMsg = "此订单已被删除，请将订单内全部商品放置在问题处理货筐，请扫描问题货筐";
            $rootScope.packErrorStep = "";
            $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow);
            $rootScope.iptFocus();
        }
    }
    //称重
    $scope.packGoodsWeight = function () {
      $scope.stationId = "HARDWARE_WEIGHT";
      //packService.getWeight($scope.stationId,function (data) {
        $scope.weight=11;
        packService.weigh($scope.shipmentNo,$scope.weight,function () {
            if($scope.shipmentAccomplish == 2){
                $scope.packProblem = "none";
                $rootScope.packStep = "checkProblemContainer";
                $scope.cellColor = "black";
                $rootScope.packOrderState = "markComplete";
                $rootScope.packStepName="请将订单商品放置到问题货筐，并扫描问题货筐条码";
                $scope.reBinCellColor = "#f2f2f2";
                $scope.iptFocus();
                $scope.packWeightEnable = true;
            }else {
                $rootScope.packStep = "checkShipment";
                $rootScope.packStepName = "请将下列商品装入指定的包装箱中，并扫描订单号码";
                $scope.packWeightEnable = true;
                $rootScope.iptFocus();
            }
        });
     // });
    };
    //扫描订单
    $scope.checkShipment = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.errorMsgWindow.close();
        if ($scope.shipmentNo == $scope.shipment) {
          $scope.packProblem= "none";
          $scope.packSuccess=true;
          $rootScope.packStepName = "请扫描箱型号码";
          $rootScope.packStep = "checkBox";
          $rootScope.packOrderState = "scanBox";
          $scope.changeRemarks("订单<br/>扫描完成");
          $rootScope.iptFocus()
        } else {
          $rootScope.packStepName = "请重新扫描订单号码";
          /*$scope.packProblem = "errorDialog";*/
          $rootScope.errorMsg = $scope.shipment + "并不是此订单对应的订单号码，请重新扫描"
          $rootScope.packErrorStep = "checkShipment";
          $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkShipmentTxtId");
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
        /*if ($scope.box.toLowerCase() == $scope.packBox.toLowerCase()) {
          $scope.packFinish();
        } else {
            $scope.errorMsgWindow.close();
          $rootScope.packProblemWindow("#packBoxWindow", $scope.packBoxWindow);
        }*/
        $scope.box = "";
      }
    };
    //箱型不匹配时的确认
    $scope.packBoxSure = function () {
        $scope.packBoxWindow.close();
        $scope.packFinish();
    }
    //结束包装
    $scope.packFinish = function () {
        $scope.packBoxWindow.close();
        $scope.errorMsgWindow.close();
      packService.packFinish($scope.shipmentNo,$scope.boxName.toUpperCase(),$scope.reBinCellName,"NO", function () {
        $rootScope.packStep = "";
        $scope.packProblem = "none";
        $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
        $scope.packPage = "main";
        $rootScope.packStep = "checkPickPackCell";
        $rootScope.iptFocus();
        $rootScope.packOrderState = "orderSuccess";
        $scope.onARebinCell=$scope.rebinCell;//上一Rebin单元格
        $scope.onTheOrder=$scope.shipment;//上一订单
        $scope.onTheCartonNo=$scope.boxName;//上一箱号
        $scope.changeRemarks("包装完成");
        $scope.intervalQueryDigital();
        $scope.intervalQuery();
        //$scope.intervalPackShipment();
      },function (data) {
        $rootScope.packStepName = "请重新扫描箱号";
        //$scope.packProblem = "errorDialog";
        $rootScope.errorMsg = data.values[0];
        $rootScope.packErrorStep = "checkBox";
        $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"checkBoxTxtId");
      });
    };
    //改变备注
    $scope.changeRemarks=function (remark) {
      var grid = $("#packGoodDetailsGrid").data("kendoGrid");
      var data = grid.dataSource.data();
      for(var i=0;i<data.length;i++){
        grid.dataSource.at(i).set("remarks", remark);
      }
    };
    //问题商品处理
    $scope.goodsProblemMenu = function (windowId, windowName, problemType) {
      $scope.packProblemGoodsAmount="";
      $scope.problemShipment = "";
      //问题商品数量为1时直接提报
      if ($scope.packSelectedData.amount === 1 || ($scope.packSelectedData.amount - $scope.packSelectedData.scanAmount) === 1) {
        //提报问题商品
        $scope.packProblemGoodsAmount = 1;
        $rootScope.packProblemProcessing("#packGoodDetailsGrid", $scope.itemNo, problemType);
      } else {
        //问题商品数量输入页面
        $rootScope.packProblemWindow(windowId, windowName)
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
      $rootScope.packProblemProcessing("#packGoodDetailsGrid", $scope.itemNo, problemType);
      //$("#reBinCellColorId").attr("color","black");
      $scope.problemAmount = $scope.packProblemGoodsAmount
    };
    //检查问题订单
    $scope.checkProblemShipment = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.errorMsgWindow.close();
        if ($scope.problemShipment == $scope.shipmentNo) {
          $scope.packProblem = "none";
          $rootScope.packStep = "checkProblemContainer";
          $scope.cellColor = "black";
          $rootScope.packOrderState = "markComplete";
          $rootScope.packStepName="请将订单商品放置到问题货筐，并扫描问题货筐条码";
          $scope.reBinCellColor = "#f2f2f2";
          $scope.iptFocus()
        }else {
          $rootScope.packStepName = "请重新扫描订单号码";
          //$scope.packProblem = "errorDialog";
          $rootScope.errorMsg = $scope.problemShipment + "并不是此订单对应的订单号码，请重新扫描"
          $rootScope.packErrorStep = "checkProblemShipment";
          $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemShipmentTxtId");
        }
        $scope.problemShipment = "";
      }
    };
    //提报问题
    $scope.checkProblemContainer=function (e) {
      var keycode=window.event ? e.keyCode : e.which;
      if(keycode == 13) {
          $scope.errorMsgWindow.close();
        var container = $scope.problemContainer;
        var stateType = "NO";
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
                packService.checkProblemContainer($scope.shipmentNo, $scope.problemContainer,$rootScope.itemType, $scope.reBinCellName,stateType,$scope.itemDataId,parseInt($scope.packProblemGoodsAmount), function () {
                    $scope.packProblem = "none";
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
                        state:""
                    };
                    packService.submitQuestion($scope.problemData,function () {
                        $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
                        $rootScope.packStep = "checkPickPackCell";
                        $rootScope.iptFocus();
                        $scope.intervalQueryDigital();
                        $scope.intervalQuery();
                        //$scope.intervalPackShipment();
                    });
                }, function (data) {
                    $rootScope.packStepName = "请更换问题货筐";
                    // $scope.packProblem = "errorDialog";
                    $rootScope.errorMsg = data.values[0];
                    $rootScope.packErrorStep = "checkProblemContainer";
                    $rootScope.errorWindow("errorWindowId",$scope.errorMsgWindow,"problemContainerTxtId");
                });
            }
        }else {
          packService.checkDeleteContainer($scope.problemShipment,$scope.problemContainer, $scope.reBinCellName,stateType,function () {
            $scope.packProblem = "none";
            $scope.problemData = {
              description: "",
              problemType:"DELETE_ORDER_CUSTOMER",
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
              state:""
            };
            packService.submitQuestion($scope.problemData,function () {
                $rootScope.packStepName = "请按动Pick Pack车上方暗灯或扫描Shipment ID，并取出商品放在电子称上";
                $rootScope.packStep = "checkPickPackCell";
                $rootScope.iptFocus();
                $scope.intervalQueryDigital();
                $scope.intervalQuery();
                //$scope.intervalPackShipment();
            });
          },function (data) {
            $rootScope.packStepName = "请更换问题货筐";
            //$scope.packProblem = "errorDialog";
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
    /*  function getLightResult(){
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
              }
              if(data.cmd == "2"){
                  lightStep = 2;
                  $scope.packLabelId = data.labelId;
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
          //if($.isEmptyObject(socket)){
              socket=webSocketService.initSocket(option);
        //  }
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
          packService.stopPack($rootScope.stationName,function () {
              closeWebsocket();
              $state.go('main.pack')
          });
      }
      //信息查询
    $scope.informationInquiry=function () {
      packService.informationInquiry(function (data){
        $scope.userName = $window.localStorage["username"];
        $scope.operationTime=data.operationTime;
        $scope.totalOperating=data.totalOperating;
        $scope.operationalEfficiency=data.operationalEfficiency;
        $scope.target=data.target;
        $scope.conclude=data.conclude;
        $scope.onAPod=data.onAPod;
        $scope.onAPallet=data.onAPallet;
        $rootScope.packProblemWindow('#informationInquiryId',$scope.informationInquiryWindow);
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