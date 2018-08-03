/**
 * Created by frank.zhou on 2017/04/17.
 * Updated by feiyu.pan on 2017/5/19.
 */
(function () {
  "use strict";

  angular.module('myApp').controller("packCtl", function ($scope, $window, $state, $rootScope,packService,outboundService) {
    $scope.userName = $window.localStorage["name"];//用户名
    $rootScope.packExsdFontColor="white";//exsd字体颜色
    $rootScope.packExsdColor="#3b6dc7";//exsd颜色
    $rootScope.packStepName = "请按动Rebin车上方暗灯，并取出商品"; //包装当前步骤名称
    $rootScope.packStep=""; //包装步骤
    $rootScope.problemType="";//问题类型
    //pack扫描商品跳转
    $scope.packScanGoods = function () {
      $state.go("main.packScanGoods");
    };
    //pack不扫描商品跳转
    $scope.packNoScanGoods = function () {
      $state.go("main.packNoScanGoods");
    };
    //pack不扫描商品跳转
    $scope.packScanShipment = function () {
      $state.go("main.packScanShipment");
    };

    $rootScope.getIsCallPod = function(workStationId) {
        outboundService.getCallPod(workStationId,function (data) {
              if(data.isCallPod){
                  $rootScope.butPod = "停止分配";
                  $rootScope.butcolor = "red";
              }else {
                  $rootScope.butPod = "恢复分配";
                  $rootScope.butcolor = "green";
              }
          });
      }

      $rootScope.pausePack = function (workStationId) {
          if ($rootScope.butPod === "停止分配") {
              outboundService.callPod("stop", workStationId, function (data) {
                  $rootScope.butPod = "恢复分配";
                  $rootScope.butcolor = "green";
              })
          } else {
              outboundService.callPod("start", workStationId, function (data) {
                  $rootScope.butPod = "停止分配";
                  $rootScope.butcolor = "red";
              })
          }
      }

    //根据距离发货时间改变exsd的颜色
    $rootScope.packExsdColorChange=function (time) {
      if(time>12){
        $rootScope.packExsdColor = "#0066CD";
      }else if((time<12&&time>6)||time==12){
        $rootScope.packExsdFontColor="black";
        $rootScope.packExsdColor="#66CEFF";
      }else if((time<6&&time>3)||time==6){
        $rootScope.packExsdFontColor="black";
        $rootScope.packExsdColor="#FFFF01";
      }else if((time<3&&time>1)||time==3){
        $rootScope.packExsdColor="#FF9901";
      }else if((time<1&&time>0)||time==1){
        $rootScope.packExsdColor="#FF7C81";
      }else if(time<0||time==0){
        $rootScope.packExsdColor="#FF0000";
      }
    };
    //包装提报具体操作
    $rootScope.packProblemProcessing=function (gridName,name,problemType) {
        $rootScope.itemType = problemType;
        var grid = $(gridName).data("kendoGrid");
        var row = grid.select();
        $scope.packSelectedData = grid.dataItem(row);
        if(problemType!="MORE") {
          var a = $scope.packSelectedData.number;
          grid.dataSource.at(a-1).set("remarks", problemType);
          /*for (var i = 0; i < grid.dataSource.data().length; i++) {
            if (grid.dataSource.data()[i].itemNo == name) {

              grid.dataSource.at(i).set("remarks", problemType)
            }
          }*/
        }
        $rootScope.packStep = "checkProblemShipment";
        $rootScope.packOrderState="problemProcessing";
        if(problemType=="GOODS_LOSS"){
          $rootScope.problemType="LOSE";
          $rootScope.packStepName ="商品丢失标记完成，请扫描当前订单号码";
        }else if(problemType=="GOODS_DAMAGED"){
          $rootScope.problemType="DAMAGED";
          $rootScope.packStepName="商品残损标记完成，请扫描当前订单号码";
        }else if(problemType=="SERIAL_NUMBER_CAN_NOT_SCAN"){
          $rootScope.problemType="UNABLE_SCAN_SN";
          $rootScope.packStepName="已成功标记商品序列号无法扫描，请将商品放入问题货筐并扫描货筐号码";
        }else  if(problemType=="GOODS_NOT_CAN_SCAN"){
          $rootScope.problemType="UNABLE_SCAN_SKU";
          $rootScope.packStepName="商品无法扫描标记完成，请扫描订单号码"
        }else  if(problemType=="MORE"){
          $rootScope.problemType="MORE";
          $rootScope.packStepName="商品多货标记完成，请扫描订单号码"

        }
    };
    //弹窗
    $rootScope.packProblemWindow = function (windowId, windowName) {
      $(windowId).parent().addClass("packProblemWindow");
      windowName.setOptions({

        width:846,
        closable: true,
        close:function () {
          $rootScope.iptFocus();
        }
      });
      windowName.center();
      windowName.open();
    };
      //弹窗
      $rootScope.scanCellWindow = function (windowId, windowName) {
          $("#" + windowId).parent().addClass("myWindow");
          windowName.setOptions({
              width:700,
              height:180,
              closable: true,
              close:function () {
                  $rootScope.iptFocus();
              }
          });
          windowName.center();
          windowName.open();
      };
      //错误弹窗提示
      $rootScope.errorWindow = function (windowId, windowName,inputId) {
          $("#" + windowId).parent().addClass("myWindow");
          windowName.setOptions({
              width:700,
              height:300,
              closable: true,
              close:function () {
                  $rootScope.iptFocus();
              },
              activate:function () {
                  $("#"+inputId).focus();
              },
          });
          windowName.center();
          windowName.open();
      };
    //锁定输入框
    $rootScope.iptFocus=function () {
      if($rootScope.packStep == 'checkGoods'){
        setTimeout(function () {
          $("#checkGoodsTxt").focus();
        })
      }else if($rootScope.packStep == 'checkSerialNumber'){
        setTimeout(function () {
          $("#checkSerialNumberTxt").focus();
        },0)
      }else if($rootScope.packStep == 'checkShipment'){
        setTimeout(function () {
          $("#checkShipmentTxt").focus();
        },0)
      }else if($rootScope.packStep == 'checkBox'){
        setTimeout(function () {
          $("#checkBoxTxt").focus();
        },0)
      }else if($rootScope.packStep == 'checkProblemShipment'){
        setTimeout(function () {
          $("#problemShipmentTxt").focus();
        },0)
      }else if($rootScope.packStep == 'checkProblemContainer'){
        setTimeout(function () {
          $("#problemContainerTxt").focus();
        })
      }else if($rootScope.packStep == 'checkPickPackCell'){
         setTimeout(function () {
           $("#pickPackCellNameTxt").focus();
        },0)
      }else if($rootScope.packErrorStep == 'checkShipment'){
          setTimeout(function () {
              $("#checkShipmentTxtId").focus();
          },0)
      }else if($rootScope.packErrorStep == 'checkBox'){
          setTimeout(function () {
              $("#checkBoxTxtId").focus();
          },0)
      }else if($rootScope.packErrorStep == 'checkProblemShipment'){
          setTimeout(function () {
              $("#problemShipmentTxtId").focus();
          },0)
      }else if($rootScope.packErrorStep == 'checkProblemContainer'){
          setTimeout(function () {
              $("#problemContainerTxtId").focus();
          },0)
      }else if($rootScope.packErrorStep == 'checkGoods'){
          setTimeout(function () {
              $("#checkGoodsTxtId").focus();
          },0)
      }
    };
    //停止包装
    /*$rootScope.stopPack=function () {
      packService.stopPack($rootScope.stationName,function () {
        $state.go('main.pack')
      });
    }*/

  });
})();