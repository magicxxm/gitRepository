
/**
 * Created by frank.zhou on 2016/12/28.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receivingPalletCtl", function($scope, $rootScope,$window, $state,INBOUND_CONSTANT,$stateParams,webSocketService,receiving_commonService,receivingService,mySocket){
      // $scope.scanhead = '0';
      var receiveType = INBOUND_CONSTANT.GENUINE;
      var finishType = INBOUND_CONSTANT.PALLET;
      var scan_product_content_GENUINE= false;
      $("#receiving-singlemode").css({"backgroundColor":"#6E6E6E"});
      $("#receiving-allmode").css({"backgroundColor":"#3f51b5"});
      if($rootScope.scan_product_content_DAMAGED===undefined)
          $rootScope.scan_product_content_DAMAGED=false;
      if($rootScope.scan_product_content_MEASURED===undefined)
          $rootScope.scan_product_content_MEASURED=false;
      if($rootScope.scan_product_content_TO_INVESTIGATE===undefined)
          $rootScope.scan_product_content_TO_INVESTIGATE=false;
      var scan_product_content_DAMAGED= $rootScope.scan_product_content_DAMAGED;
      var scan_product_content_MEASURED= $rootScope.scan_product_content_MEASURED;
      var scan_product_content_TO_INVESTIGATE= $rootScope.scan_product_content_TO_INVESTIGATE;
      var headerStyle = {"class": "table-header-cell",style:"font-size:16px;line-height:2;color:white;height:35px;overflow:hidden"};
      var baseStyle = {style: "font-size:16px;height:25px;text-align:center;overflow:hidden"};
      var columns= [
          {field: "desinationName", headerTemplate: "<span translate='POSITION_NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
          {field: "receiveStorageName", headerTemplate: "<span translate='PICKCAR_NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
          {field: "amount", headerTemplate: "<span translate='SKU_COUNT'></span>", attributes: baseStyle,headerAttributes:headerStyle}
      ];
      $scope.user = $window.localStorage["name"];
      $scope.operateTime = '0.9小时';
      $scope.operateTotalCount = '250';
      $scope.operatePercentage = '260/小时';
      $scope.goal = '500';
      $scope.achieved = '83%';
      $scope.prePod = 'VirtualPod';
      $scope.preLocation = 'VirtualLocation';
      $scope.preDN = 'VirtualDN';
      // var scan_pod = false;
      var scan_DN = false;
      var normalFull = false;
      var isAutoRelease = true;
      var reportTypeId = null;
      var destinationId = null;
      var positionIndex = null;
      var scan_product_info= false;
      var scan_bin= false;
      var isSingle= false;
      var isMeasured = false;
      //是否待调查
      var isInvest = false;
      var isDamaged = false;
      var podid = "";
      var adviceNo = "";
      var storageid = "";
      var amount = "";
      var itemid = "";
      var sn = "";
      var useAfter = null;
      var thisid;
      var totalamount = "";
      var isSn = false;
      var isLot = false;
      var storages = new Array();
      var upid = '';
      var item = null;
      var avaTimeType = '';
      var TimeType = '';
      var currentId = '';
      var dnAmount = null;
      var receiveAmount = null;
      var canReceiveAmount = null;
      var maxAmount = null;
      var scan_pod = false;

      /*sd add start*/
      var podSocket;//webSocket
       /*sd add end*/
      $scope.podInfo = 'Pod';
      $scope.podShow='1';
      $scope.mySocket = mySocket;
      $scope.webSocketBuilder = mySocket.webSocketBuilder;
    //初次进入开始叫Pod
    //   $scope.receiveCallPod = function (callback) {
    //       receivingService.receiveCallPod($rootScope.stationId,callback);
    //   };


      var websocketClient = null;
      //连接websocket
      function getPodResult() {
          var option = {
              "user": $scope.workStationId,
              "url": "websocket/getPod/" + $scope.workStationId,
              "onmessageCall": onmessageCall
          }
          if ($.isEmptyObject(websocketClient)) {
              websocketClient = webSocketService.initSocket(option)
          }
      }
      //接收到消息后做的业务处理代码
      function onmessageCall(msg){
          console.log("推送pod:",msg);
          var data = JSON.parse(msg);
          if(data.pod != "success"){
              if (data.workstation == $rootScope.workStationId) {
                  console.log("推送pod的信息：",data);
                  console.log("onMessage.pod-->"+data.pod);
                  if(podid===''||podid===null||podid===undefined){
                      $scope.getPodInfo(data.pod);
                  }
              }
          }
      }
      //关闭websocket
      function closeWebsocket(){
          if(!($.isEmptyObject(websocketClient))){
              console.log("客户端主动关闭websocket连接");
              websocketClient.close(3666,"客户端主动关闭websocket连接");
          }
      };
      if($rootScope.locationTypeSize>0){
         // console.log("podSocket 已打开");
          getPodResult();
      }else{
          console.log("货位类型为空");
      }
        /*sd add start*/
      /*function getPodResult() {
          var url = INBOUND_CONSTANT.readWebSocketPod+$rootScope.workStationId;
          console.log("url:",url);
          podSocket = new WebSocket(url);
          //打开事件
          podSocket.onopen = function () {
              console.log("podSocket 已打开");
          };
          //获得消息事件
          podSocket.onmessage = function (msg) {
              console.log("podSocket 正在推送消息。。。");
              var data = JSON.parse(msg.data);
              if(data.pod != "success"){
                  if (data.workstation == $rootScope.workStationId) {
                      console.log("推送pod的信息：",data);
                      console.log("onMessage.pod-->"+data.pod);
                      if(podid===''||podid===null||podid===undefined){
                          $scope.getPodInfo(data.pod);
                      }
                  }
              }
          };
          //关闭事件
          podSocket.onclose = function () {
              console.log("podSocket 关闭");
          };
          //发生了错误事件
          podSocket.onerror = function () {
              console.log("podSocket 发生了错误");
              podSocket = new WebSocket(url);
          }
      }*/
    /*sd add end*/

      $scope.receivingscan = function (e) {
          if(!receiving_commonService.autoAddEvent(e)) return;
          var inputvalue = $("#receiving-inputer").val().trim()||$scope.tipvalue;
          $scope.tipvalue = '';
          receiving_commonService.CloseWindowByBtn("newtipwindowwithinputer");
          console.log("inputvalue-->"+inputvalue);
          $("#receiving-inputer").val("");
          if(!scan_product_content_DAMAGED){
              receivingService.scanStorageLocation(inputvalue,INBOUND_CONSTANT.DAMAGED,$rootScope.stationName,$rootScope.demagedDestinationId,$rootScope.demagedPositionIndex,function (data) {
                  if(data.status==='2'){//有商品,提示用户
                      $scope.scancontainerType = INBOUND_CONSTANT.DAMAGED;
                      storageid = inputvalue;
                      destinationId = $rootScope.demagedDestinationId;
                      positionIndex = $rootScope.demagedPositionIndex;
                      var options = {
                          width:600,
                          height:500,
                          title:INBOUND_CONSTANT.SUREUSELOCATION,
                          open:function () {
                              $scope.wimgstatus='hidden';
                              $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                          },
                          close:function () {
                              $("#receiving-inputer").focus();
                          }
                      };
                      receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                  }else{
                      isOld = true;
                      scan_product_content_DAMAGED = true;
                      checkContainerIsScan();
                  }
              },function (data) {
                  var options = {
                      title:INBOUND_CONSTANT.RESCANDAMAGED,
                      width:600,
                      height:400,
                      open:function () {
                          $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                          setTimeout(function () {
                              $("#newtipwindow_inputer").focus();
                          },500);
                      },
                      close:function () {
                          setTimeout(function () {
                              $("#receiving-inputer").focus();
                          },500);
                      }
                  };
                  receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
              });
          }else{
              if(!scan_product_content_MEASURED){
                  receivingService.scanStorageLocation(inputvalue,INBOUND_CONSTANT.MEASURED,$rootScope.stationName,$rootScope.measuredDestinationId,$rootScope.measuredPositionIndex,function (data) {
                      if(data.status==='2'){//有商品,提示用户
                          $scope.scancontainerType = INBOUND_CONSTANT.MEASURED;
                          storageid = inputvalue;
                          destinationId = $rootScope.measuredDestinationId;
                          positionIndex = $rootScope.measuredPositionIndex;
                          var options = {
                              width:600,
                              height:500,
                              title:INBOUND_CONSTANT.SUREUSELOCATION,
                              open:function () {
                                  $scope.wimgstatus='hidden';
                                  $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                              },
                              close:function () {
                                  setTimeout(function () {
                                      $("#receiving-inputer").focus();
                                  },500);
                              }
                          };
                          receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                      }else{
                          isOld = true;
                          scan_product_content_MEASURED = true;
                          checkContainerIsScan();
                      }
                  },function (data) {
                      var options = {
                          title:INBOUND_CONSTANT.RESCANMEASURED,
                          width:600,
                          height:400,
                          open:function () {
                              $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                              setTimeout(function () {
                                  $("#newtipwindow_inputer").focus();
                              },500);
                          },
                          close:function () {
                              setTimeout(function () {
                                  $("#receiving-inputer").focus();
                              },500);
                          }
                      };
                      receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                  });
              }else{
                  if(!scan_product_content_TO_INVESTIGATE){
                      receivingService.scanStorageLocation(inputvalue,INBOUND_CONSTANT.TO_INVESTIGATE,$rootScope.stationName,$rootScope.investDestinationId,$rootScope.investPositionIndex,function (data) {
                          if(data.status==='2'){//有商品,提示用户
                              storageid = inputvalue;
                              $scope.scancontainerType = INBOUND_CONSTANT.TO_INVESTIGATE;
                              destinationId = $rootScope.investDestinationId;
                              positionIndex = $rootScope.investPositionIndex;
                              var options = {
                                  width:600,
                                  height:500,
                                  title:INBOUND_CONSTANT.SUREUSELOCATION,
                                  open:function () {
                                      $scope.wimgstatus='hidden';
                                      $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                                  },
                                  close:function () {
                                      $("#receiving-inputer").focus();
                                  }
                              };
                              receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                          }else{
                              isOld = true;
                              scan_product_content_TO_INVESTIGATE = true;
                              checkContainerIsScan();
                          }
                      },function (data) {
                          var options = {
                              title:INBOUND_CONSTANT.RESCANINVEST,
                              width:600,
                              height:400,
                              open:function () {
                                  $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                  setTimeout(function () {
                                      $("#newtipwindow_inputer").focus();
                                  },500);
                              },
                              close:function () {
                                  setTimeout(function () {
                                      $("#receiving-inputer").focus();
                                  },500);
                              }
                          };
                          receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                      });
                  }else {
                      if (!scan_pod) {
                          showGeneralWindow("提示","Pod尚未就绪,请稍后操作／刷新Pod获取最新Pod信息");
                      } else {
                          if (isMeasured || isDamaged || isInvest) {
                              checkContainerIsScan();
                          }
                          receivingService.scanIsDN(inputvalue, function (data) {
                              //是车牌true,不是false
                              $scope.scanDn = data;
                              if ($scope.scanDn) {
                                  scan_DN = false;
                                  scan_product_info = false;
                                  scan_bin = false;
                                  //是否测量
                                  isMeasured = false;
                                  isDamaged = false;
                                  isInvest = false;
                                  isSingle = false;
                                  isLot = false;
                                  isSn = false;
                                  adviceNo = "";
                                  storageid = "";
                                  receiveType = INBOUND_CONSTANT.GENUINE;
                                  amount = "";
                                  itemid = "";
                                  sn = "";
                                  item = null;
                                  useAfter = "";
                                  upid = '';
                                  $scope.scanDn = false;
                                  $scope.scanmeasurecib = '1';
                                  $("#pallet_storage").css({"backgroundColor": "#00BFFF"});
                                  $("#receiving_status_span").html("");
                                  $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                                  console.log("ScanningDN...");
                                  receivingService.scanDN(inputvalue, function (data) {
                                      scan_DN = true;
                                      adviceNo = data.cls.request.adviceNo;
                                      $("#receiving_dn_span").html(adviceNo);
                                      $scope.product_info_con = '1';
                                      $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                      $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                      $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                  }, function (data) {
                                      $("#receiving_tip").html(INBOUND_CONSTANT.RESCANDN);
                                      var options = {
                                          title: INBOUND_CONSTANT.RESCANDN,
                                          width: 600,
                                          height: 400,
                                          open: function () {
                                              $("#newtipwindow_span").html(data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
                                              setTimeout(function () {
                                                  $("#newtipwindow_inputer").focus();
                                              }, 500);
                                          },
                                          close: function () {
                                              setTimeout(function () {
                                                  $("#receiving-inputer").focus();
                                              }, 500);
                                          }
                                      };
                                      receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                  });
                                  checkContainerIsScan();
                              }else{
                                  if (!scan_product_info) {
                                      receivingService.scanItem(adviceNo, inputvalue, $rootScope.stationName,podid, function (data) {
                                          if (isMeasured) {
                                              $scope.scanwaitcib = '1';
                                              $scope.scanbadcib = '1';
                                              $scope.scanmeasurecib = '1';
                                          }
                                          itemid = inputvalue;
                                          $("#pallet_storage").css({"backgroundColor": "#00BFFF"});
                                          $("#receiving_status_span").html("");
                                          scan_product_info = true;
                                          $scope.product_info_con = '0';
                                          /*SD add start*/
                                          if(data.cls.itemData.itemNo=="235270135112427"){
                                              $("#skuimg").attr("src","../../../../image/nongfushanquan2.png");
                                          }
                                          if(data.cls.itemData.itemNo=="711209561729769"){
                                              $("#skuimg").attr("src","../../../../image/xuebi2.png");
                                          }
                                          if(data.cls.itemData.itemNo=="129714641165855"){
                                              $("#skuimg").attr("src","../../../../image/kele2.png");
                                          }
                                          if(data.cls.itemData.itemNo=="517036749820530"){
                                              $("#skuimg").attr("src","../../../../image/meinianda2.png");
                                          }
                                          /*SD add end*/

                                          $("#product_info_title").html("SKU:" + data.cls.itemData.skuNo);
                                          $("#product_info_text").html("商品名称:" + data.cls.itemData.name);
                                          avaTimeType = data.cls.itemData.lotType;
                                          TimeType = data.cls.itemData.itemDataGlobal.lotUnit;
                                          dnAmount = data.cls.dnAmount;
                                          item = data.cls.itemData;
                                          canReceiveAmount = data.cls.canReceiveAmount;
                                          receiveAmount = data.cls.receiveAmount;
                                          maxAmount = data.cls.maxReceiveAmount;
                                          if (data.status === '1') {//测量商品
                                              console.log("测量商品");
                                              isMeasured = true;
                                              isSingle = true;
                                              $("#palletscanmeasurecib").html(INBOUND_CONSTANT.DAMAGED);
                                              finishType = INBOUND_CONSTANT.SINGLE;
                                              receiveType = INBOUND_CONSTANT.MEASURED;
                                              // $('#receiving_tip').html(INBOUND_CONSTANT.SCANMEASURE);
                                              // $scope.scanmeasurecib = '0';
                                              cleanStatus();
                                              checkContainerIsScan();
                                              checkType(data.cls.itemData);
                                          } else {
                                              //非有效期商品并且无推荐货位
                                              if (!item.lotMandatory && (data.cls.introStorages === undefined || data.cls.introStorages === null || data.cls.introStorages === '' || data.cls.introStorages.length === 0)) {
                                                  receiving_commonService.receiving_tip_dialog("releasePodWindow", {
                                                      title: "无推荐货位",
                                                      width: 600,
                                                      height: 500,
                                                      open: function () {
                                                          $scope.sureDnTip = INBOUND_CONSTANT.NOINTROSTORAGES;
                                                      },
                                                      close: function () {
                                                          focusOnReceiveInputer();
                                                      }
                                                  });
                                              } else {
                                                  cleanStatus();
                                                  checkContainerIsScan();
                                                  checkType(data.cls.itemData);
                                              }
                                          }
                                      }, function (data) {
                                          $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                          var options = {
                                              title: INBOUND_CONSTANT.RESCANITEM,
                                              width: 600,
                                              height: 400,
                                              open: function () {
                                                  $("#newtipwindow_span").html(data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
                                                  setTimeout(function () {
                                                      $("#newtipwindow_inputer").focus();
                                                  }, 500);
                                              },
                                              close: function () {
                                                  setTimeout(function () {
                                                      $("#receiving-inputer").focus();
                                                  }, 500);
                                              }
                                          };
                                          receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                      });
                                  } else {
                                      //检查上架货位
                                      storageid = inputvalue;
                                      if (!isInvest && isSn && (sn == null || sn === '' || sn === undefined)) {
                                          popSnWindow();
                                          return;
                                      }
                                      if (isLot && (useAfter === null || useAfter === '' || useAfter === undefined)) {
                                          popLotWindow();
                                          return;
                                      }
                                      if (isMeasured || isInvest || isDamaged) {
                                          console.log("测量检查货筐");
                                          receivingService.checkNotGenuisContainer(storageid, $rootScope.stationName, receiveType, useAfter, itemid, function (data) {
                                              finishPallet();
                                          }, function (data) {//扫描非正品货筐错误
                                              if (data.key === '-1' || data.key === '-2') {
                                                  showStorageFull(data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
                                              } else {
                                                  var options = {
                                                      width: 600,
                                                      height: 400,
                                                      title: INBOUND_CONSTANT.RESCANCONTAINER,
                                                      open: function () {
                                                          $("#newtipwindow_span").html("扫描货筐错误" + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""))
                                                          setTimeout(function () {
                                                              $("#newtipwindow_inputer").focus();
                                                          }, 500);
                                                      },
                                                      close: function () {
                                                          setTimeout(function () {
                                                              $("#receiving-inputer").focus();
                                                          }, 500);
                                                      }
                                                  };
                                                  receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);

                                                  // showGeneralWindow("扫描货筐错误", data.message);
                                              }
                                          });
                                      } else {
                                          receivingService.checkBin(storageid, itemid, useAfter, podid, $rootScope.stationName, function (data) {
                                              scan_bin = true;
                                              finishPallet();
                                          }, function (data) {
                                              storageid = "";
                                              if (data.key === '-4') {
                                                  var options = {
                                                      width: 600,
                                                      height: 400,
                                                      title: INBOUND_CONSTANT.SCANLOCATIONORDN,
                                                      open: function () {
                                                          $("#newtipwindow_span").html("<h3>" + data.values[0] + "</h3>")
                                                          setTimeout(function () {
                                                              $("#newtipwindow_inputer").focus();
                                                          }, 500);
                                                      },
                                                      close: function () {
                                                          setTimeout(function () {
                                                              $("#receiving-inputer").focus();
                                                          }, 500);
                                                      }
                                                  };
                                                  receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                              } else {
                                                  var options = {
                                                      title: INBOUND_CONSTANT.SCANDNORPOD,
                                                      width: 600,
                                                      height: 400,
                                                      open: function () {
                                                          $scope.tipvalue = '';
                                                          if (data.key.indexOf("%") != -1) {
                                                              $("#check-bin-inputwindow-span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 42%;'>" + data.values[0] + "</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>" + data.key.substr(data.key.indexOf("&") + 1, data.key.length - 1) + "</p>");
                                                          } else {
                                                              $("#check-bin-inputwindow-span").html(data.values[0] || "" + "</br>" + data.key);
                                                          }
                                                          $("#check-bin-receiving-inputer").val('');
                                                          setTimeout(function () {
                                                              $("#check-bin-receiving-inputer").focus();
                                                          }, 500);
                                                      },
                                                      close: function () {
                                                          $("#receiving-inputer").focus();
                                                      }
                                                  };
                                                  receiving_commonService.receiving_tip_dialog("scanwindowwithpodbtn", options);
                                              }
                                          });
                                      }
                                  }
                              }
                          })




                          /*if (receiving_commonService.isDN(inputvalue)) {
                              scan_DN = false;
                              scan_product_info = false;
                              scan_bin = false;
                              //是否测量
                              isMeasured = false;
                              isDamaged = false;
                              isInvest = false;
                              isSingle = false;
                              isLot = false;
                              isSn = false;
                              adviceNo = "";
                              storageid = "";
                              receiveType = INBOUND_CONSTANT.GENUINE;
                              amount = "";
                              itemid = "";
                              sn = "";
                              item = null;
                              useAfter = "";
                              upid = '';
                              $scope.scanmeasurecib = '1';
                              $("#pallet_storage").css({"backgroundColor": "#00BFFF"});
                              $("#receiving_status_span").html("");
                              $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                          }
                          if (!scan_DN) {
                              console.log("ScanningDN...");
                              receivingService.scanDN(inputvalue, function (data) {
                                  scan_DN = true;
                                  adviceNo = data.cls.request.adviceNo;
                                  $("#receiving_dn_span").html(adviceNo);
                                  $scope.product_info_con = '1';
                                  $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                  $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                  $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                  $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                              }, function (data) {
                                  $("#receiving_tip").html(INBOUND_CONSTANT.RESCANDN);
                                  var options = {
                                      title: INBOUND_CONSTANT.RESCANDN,
                                      width: 600,
                                      height: 400,
                                      open: function () {
                                          $("#newtipwindow_span").html(data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
                                          setTimeout(function () {
                                              $("#newtipwindow_inputer").focus();
                                          }, 500);
                                      },
                                      close: function () {
                                          setTimeout(function () {
                                              $("#receiving-inputer").focus();
                                          }, 500);
                                      }
                                  };
                                  receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                              });
                              checkContainerIsScan();
                          }
                          else {
                              if (!scan_product_info) {
                                  receivingService.scanItem(adviceNo, inputvalue, $rootScope.stationName,podid, function (data) {
                                      if (isMeasured) {
                                          $scope.scanwaitcib = '1';
                                          $scope.scanbadcib = '1';
                                          $scope.scanmeasurecib = '1';
                                      }
                                      itemid = inputvalue;
                                      $("#pallet_storage").css({"backgroundColor": "#00BFFF"});
                                      $("#receiving_status_span").html("");
                                      scan_product_info = true;
                                      $scope.product_info_con = '0';
                                      /!*SD add start*!/
                                      if(data.cls.itemData.itemNo=="235270135112427"){
                                          $("#skuimg").attr("src","../../../../image/nongfushanquan2.png");
                                      }
                                      if(data.cls.itemData.itemNo=="711209561729769"){
                                          $("#skuimg").attr("src","../../../../image/xuebi2.png");
                                      }
                                      if(data.cls.itemData.itemNo=="129714641165855"){
                                          $("#skuimg").attr("src","../../../../image/kele2.png");
                                      }
                                      if(data.cls.itemData.itemNo=="517036749820530"){
                                          $("#skuimg").attr("src","../../../../image/meinianda2.png");
                                      }
                                      /!*SD add end*!/

                                      $("#product_info_title").html("SKU:" + data.cls.itemData.skuNo);
                                      $("#product_info_text").html("商品名称:" + data.cls.itemData.name);
                                      avaTimeType = data.cls.itemData.lotType;
                                      TimeType = data.cls.itemData.itemDataGlobal.lotUnit;
                                      dnAmount = data.cls.dnAmount;
                                      item = data.cls.itemData;
                                      canReceiveAmount = data.cls.canReceiveAmount;
                                      receiveAmount = data.cls.receiveAmount;
                                      maxAmount = data.cls.maxReceiveAmount;
                                      if (data.status === '1') {//测量商品
                                          console.log("测量商品");
                                          isMeasured = true;
                                          isSingle = true;
                                          $("#palletscanmeasurecib").html(INBOUND_CONSTANT.DAMAGED);
                                          finishType = INBOUND_CONSTANT.SINGLE;
                                          receiveType = INBOUND_CONSTANT.MEASURED;
                                          // $('#receiving_tip').html(INBOUND_CONSTANT.SCANMEASURE);
                                          // $scope.scanmeasurecib = '0';
                                          cleanStatus();
                                          checkContainerIsScan();
                                          checkType(data.cls.itemData);
                                      } else {
                                          //非有效期商品并且无推荐货位
                                          if (!item.lotMandatory && (data.cls.introStorages === undefined || data.cls.introStorages === null || data.cls.introStorages === '' || data.cls.introStorages.length === 0)) {
                                              receiving_commonService.receiving_tip_dialog("releasePodWindow", {
                                                  title: "无推荐货位",
                                                  width: 600,
                                                  height: 500,
                                                  open: function () {
                                                      $scope.sureDnTip = INBOUND_CONSTANT.NOINTROSTORAGES;
                                                  },
                                                  close: function () {
                                                      focusOnReceiveInputer();
                                                  }
                                              });
                                          } else {
                                              cleanStatus();
                                              checkContainerIsScan();
                                              checkType(data.cls.itemData);
                                          }
                                      }
                                  }, function (data) {
                                      $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                      var options = {
                                          title: INBOUND_CONSTANT.RESCANITEM,
                                          width: 600,
                                          height: 400,
                                          open: function () {
                                              $("#newtipwindow_span").html(data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
                                              setTimeout(function () {
                                                  $("#newtipwindow_inputer").focus();
                                              }, 500);
                                          },
                                          close: function () {
                                              setTimeout(function () {
                                                  $("#receiving-inputer").focus();
                                              }, 500);
                                          }
                                      };
                                      receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                  });
                              } else {
                                  //检查上架货位
                                  storageid = inputvalue;
                                  if (!isInvest && isSn && (sn == null || sn === '' || sn === undefined)) {
                                      popSnWindow();
                                      return;
                                  }
                                  if (isLot && (useAfter === null || useAfter === '' || useAfter === undefined)) {
                                      popLotWindow();
                                      return;
                                  }
                                  if (isMeasured || isInvest || isDamaged) {
                                      console.log("测量检查货筐");
                                      receivingService.checkNotGenuisContainer(storageid, $rootScope.stationName, receiveType, useAfter, itemid, function (data) {
                                          finishPallet();
                                      }, function (data) {//扫描非正品货筐错误
                                          if (data.key === '-1' || data.key === '-2') {
                                              showStorageFull(data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
                                          } else {
                                              var options = {
                                                  width: 600,
                                                  height: 400,
                                                  title: INBOUND_CONSTANT.RESCANCONTAINER,
                                                  open: function () {
                                                      $("#newtipwindow_span").html("扫描货筐错误" + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""))
                                                      setTimeout(function () {
                                                          $("#newtipwindow_inputer").focus();
                                                      }, 500);
                                                  },
                                                  close: function () {
                                                      setTimeout(function () {
                                                          $("#receiving-inputer").focus();
                                                      }, 500);
                                                  }
                                              };
                                              receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);

                                              // showGeneralWindow("扫描货筐错误", data.message);
                                          }
                                      });
                                  } else {
                                      receivingService.checkBin(storageid, itemid, useAfter, podid, $rootScope.stationName, function (data) {
                                          scan_bin = true;
                                          finishPallet();
                                      }, function (data) {
                                          storageid = "";
                                          if (data.key === '-4') {
                                              var options = {
                                                  width: 600,
                                                  height: 400,
                                                  title: INBOUND_CONSTANT.SCANLOCATIONORDN,
                                                  open: function () {
                                                      $("#newtipwindow_span").html("<h3>" + data.values[0] + "</h3>")
                                                      setTimeout(function () {
                                                          $("#newtipwindow_inputer").focus();
                                                      }, 500);
                                                  },
                                                  close: function () {
                                                      setTimeout(function () {
                                                          $("#receiving-inputer").focus();
                                                      }, 500);
                                                  }
                                              };
                                              receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                          } else {
                                              var options = {
                                                  title: INBOUND_CONSTANT.SCANDNORPOD,
                                                  width: 600,
                                                  height: 400,
                                                  open: function () {
                                                      $scope.tipvalue = '';
                                                      if (data.key.indexOf("%") != -1) {
                                                          $("#check-bin-inputwindow-span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 42%;'>" + data.values[0] + "</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>" + data.key.substr(data.key.indexOf("&") + 1, data.key.length - 1) + "</p>");
                                                      } else {
                                                          $("#check-bin-inputwindow-span").html(data.values[0] || "" + "</br>" + data.key);
                                                      }
                                                      $("#check-bin-receiving-inputer").val('');
                                                      setTimeout(function () {
                                                          $("#check-bin-receiving-inputer").focus();
                                                      }, 500);
                                                  },
                                                  close: function () {
                                                      $("#receiving-inputer").focus();
                                                  }
                                              };
                                              receiving_commonService.receiving_tip_dialog("scanwindowwithpodbtn", options);
                                          }
                                      });
                                  }
                              }
                          }*/
                      }
                  }
              }
          }
      };
      //显示货筐已满窗口
      function showStorageFull(msg) {
          var options = {
              title: INBOUND_CONSTANT.EXCHANGESTORAGE,
              open: function () {
                  $("#ok_tipwindow_span").html(INBOUND_CONSTANT.CLICKSTORAGEFULL);
              }
          };
          receiving_commonService.receiving_tip_dialog("ok_tipwindow", options);
      }
      //刷新pod信息
      $scope.refreshPod = function () {
          receivingService.refreshPod($rootScope.sectionId,$rootScope.workStationId,function (data) {
              if(data.pod===''||data.pod===null||data.pod===undefined){
                  storages = null;
                  $scope.stockAmount = 0;
                  $scope.podlayout = 'hidden';
                  scan_pod = false;
                  $scope.podShow = '1';
                  $("#pallet_storage").html("");
                  $("#pallet_storage").css({"backgroundColor":"#b6b6b6"});
                  $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
              }else{
                  $scope.getPodInfo(data.pod);
              }
          });
      };

      //重新加载pod信息
      $scope.reloadPod = function () {
          if (podid === null || podid === undefined || podid === '') {
                $scope.refreshPod();
          } else {
                $scope.releasePodNormal();
          }
          focusOnReceiveInputer();
      };
      //获取pod信息
      $scope.getPodInfo = function (pod) {
          receivingService.getPodInfo(pod, INBOUND_CONSTANT.PALLET, function (data) {
              if (Number(data.status) < 0 || data.cls.totalRow === 0) {//pod信息不合法
                  var options = {
                      title:INBOUND_CONSTANT.NOTGETPOD,
                      width:600,
                      height:400,
                      open:function () {
                          $("#newtipwindow_span").html(INBOUND_CONSTANT.NOTGETPOD);
                          setTimeout(function () {
                              $("#newtipwindow_inputer").focus();
                          },500);
                      },
                      close:function () {
                          setTimeout(function () {
                              $("#receiving-inputer").focus();
                          },500);
                      }
                  };
                  receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
              } else {
                  podid = pod;
                  $scope.podInfo = pod;
                  $scope.podlayout = '0';
                  $scope.podShow = '0';
                  scan_pod = true;
                  console.log("stockAmount--->"+data.cls.stockAmount);
                  $scope.stockAmount = data.cls.stockAmount;
                  $("#pallet_storage").html("当前货位数量总计:"+$scope.stockAmount);
                  $("#pallet_storage").css({"backgroundColor":"#00BFFF"});
                  checkContainerIsScan();
                  storages = data.cls.storageLocations;
                  setTimeout(function () {
                      if(itemid!==undefined&&itemid!==''&&itemid!==null){
                          //重新查找推荐pod
                          console.log("重新查找推荐pod");
                          if(item.lotMandatory!==null&&item.lotMandatory!==undefined&&item.lotMandatory){
                              if(!isLot){
                                  receivingService.checkAvaTime(itemid,useAfter,podid,function (data) {
                                      if(data.cls===undefined||data.cls===null||data.cls===''||data.cls.length===0){
                                          var options = {
                                              title: INBOUND_CONSTANT.SCANDNORPOD,
                                              width:600,
                                              height:400,
                                              open: function () {
                                                  $scope.tipvalue = '';
                                                  if(data.key.indexOf("%")!=-1){
                                                      $("#check-bin-inputwindow-span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 42%;'>"+data.values[0]+"</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>"+data.key.substr(data.key.indexOf("&")+1,data.key.length-1)+"</p>");
                                                  }else{
                                                      $("#check-bin-inputwindow-span").html(data.values[0]||""+"</br>"+data.key);
                                                  }
                                                  $("#check-bin-receiving-inputer").val('');
                                                  setTimeout(function () {
                                                      $("#check-bin-receiving-inputer").focus();
                                                  },500);
                                              },
                                              close: function () {
                                                  focusOnReceiveInputer();
                                              }
                                          };
                                          receiving_commonService.receiving_tip_dialog("scanwindowwithpodbtn", options);
                                      }else{
                                          focusOnReceiveInputer();
                                      }
                                  });
                              }
                          }else{
                              receivingService.scanItem(adviceNo,itemid,$rootScope.stationName,podid,function (data) {
                                  if(data.cls.introStorages===undefined||data.cls.introStorages===null||data.cls.introStorages===''||data.cls.introStorages.length===0){
                                      var options = {
                                          title: INBOUND_CONSTANT.SCANDNORPOD,
                                          width:600,
                                          height:400,
                                          open: function () {
                                              $scope.tipvalue = '';
                                              if(data.key.indexOf("%")!=-1){
                                                  $("#check-bin-inputwindow-span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 42%;'>"+data.values[0]+"</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>"+data.key.substr(data.key.indexOf("&")+1,data.key.length-1)+"</p>");
                                              }else{
                                                  $("#check-bin-inputwindow-span").html(data.values[0]||""+"</br>"+data.key);
                                              }
                                              $("#check-bin-receiving-inputer").val('');
                                              setTimeout(function () {
                                                  $("#check-bin-receiving-inputer").focus();
                                              },500);
                                          },
                                          close: function () {
                                              focusOnReceiveInputer();
                                          }
                                      };
                                      receiving_commonService.receiving_tip_dialog("scanwindowwithpodbtn", options);
                                  }else{
                                      focusOnReceiveInputer();
                                  }
                              });
                          }
                      }
                      $("#receiving-inputer").focus();
                  }, 500);
              }
          },function (data) {
              var options = {
                  title:INBOUND_CONSTANT.NOTGETPOD,
                  width:600,
                  height:400,
                  open:function () {
                      $("#newtipwindow_span").html(INBOUND_CONSTANT.NOTGETPOD);
                      setTimeout(function () {
                          $("#newtipwindow_inputer").focus();
                      },500);
                  },
                  close:function () {
                      setTimeout(function () {
                          $("#receiving-inputer").focus();
                      },500);
                  }
              };
              receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
          });  
      };
      
      //显示更换货筐窗口
      $scope.showChangeStoage = function (data) {
          console.log("showChangeState-->"+data);
          normalFull = data;
          receiving_commonService.CloseWindowByBtn("promenu_pop_window");
          receiving_commonService.CloseWindowByBtn("ok_tipwindow");
          var options = {
              title: INBOUND_CONSTANT.SCANFULLSTORAGE,
              width:800,
              height:500,
              open: function () {
                  gridStorageInfo();
                  $("#inputstoragewindow_span").html("请扫描已满货筐条码");
                  $("#window-storage-inputer").val("");
                  setTimeout(function () {
                      $("#window-storage-inputer").focus();
                  },500);
              },
              close:function () {
                  if(scan_product_content_MEASURED&&scan_product_content_TO_INVESTIGATE&&scan_product_content_DAMAGED&&normalFull){
                      normalFull = false;
                      isOld = true;
                      return;
                  }
                  if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE){
                      checkContainerIsScan();
                      isOld = true;
                  }
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("scanstoragewindow", options);
      };
      //商品残损
      $scope.goodsDamage = function(){
          if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE){
              showGeneralWindow("请先绑定货筐","请先绑定残品/测量/待调查货筐");
              checkContainerIsScan();
              return;
          }
          if(podid==undefined||podid==null||podid===''){
              showGeneralWindow("请先扫描Pod","请先扫描Pod条码");
              checkContainerIsScan();
              return;
          }
          $scope.product_info_con = '1';
          $scope.scanbadcib = '0';

          // $scope.podlayout = '1';

          //$("#palletscanbadcib").html("1");
          $("#palletscanbadcib").css({"backgroundColor":"#FF0000"});
          $("#receiving_status_span").html("");
          $("#receiving_status_span").css({"backgroundColor":"#eeeee0"});

          $("#" + storageid).css({"backgroundColor": "#8c8c8c"});
          $("#" + storageid).children("span").text("");

          $("#product_info_title").html("");
          $("#product_info_text").html("");

          if(!scan_DN){
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANDN);
              $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANDN);
              $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
          }else{
              receiveType = INBOUND_CONSTANT.DAMAGED;
              $("#receiving_dn_span").css({"backgroundColor":"#EEEEE0"});
              $("#product_info_span").css({"backgroundColor":"#FFDEAD"});
              $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
          }
          isDamaged = true;
          scan_product_info = false;
          receiveType = INBOUND_CONSTANT.DAMAGED;
          receiving_commonService.CloseWindowByBtn("promenu_pop_window");
          $("#receiving-inputer").focus();

      };
      //确认使用当前货筐
      $scope.win_receivingok = function (type) {
          receivingService.bindStorageLocation(storageid,type,$rootScope.stationName,destinationId,positionIndex,function (data) {
              destinationId = null;
              positionIndex = null;
              console.log("当前货筐type--->"+type);
              if(type.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                  scan_product_content_DAMAGED = true;
              }
              if(type.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                  scan_product_content_MEASURED = true;
              }
              if(type.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                  scan_product_content_TO_INVESTIGATE = true;
              }
              if(isMeasured||isDamaged||isInvest){
                  finishPallet();
              }
              checkContainerIsScan();
              //scan_DAMAGED = true;
              normalFull = false;
              isOld = true;
              $scope.scancontainerType = null;
              receiving_commonService.CloseWindowByBtn('scanstoragewindow');
              receiving_commonService.CloseWindowByBtn("window_img_ok_cancel");
          });
      };
      //不使用当前货筐
      $scope.win_receivingcancel = function (type) {
          if(type.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
              scan_product_content_DAMAGED = false;
              $('#receiving_tip').html(INBOUND_CONSTANT.SCANDAMAGED);
          }
          if(type.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
              scan_product_content_MEASURED = false;
              $('#receiving_tip').html(INBOUND_CONSTANT.SCANMEASURE);
          }
          if(type.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
              scan_product_content_TO_INVESTIGATE = false;
              $('#receiving_tip').html(INBOUND_CONSTANT.SCANINVESTAGETE);
          }
          receiving_commonService.CloseWindowByBtn("window_img_ok_cancel");
      };
      $scope.autoClose = function (e) {
          if(!receiving_commonService.autoAddEvent(e)) return;
          var window = $("#tipwindow").data("kendoWindow");
          window.close();
      };
      //显示有效期弹窗
      $scope.showAvaTimeWindow = function () {
          var options = {
              title:"请输入商品有效期",
              width:800,
              height:600,
              open:function () {
                  $("#avatime_pop_window_madeyear").val("");
                  $("#avatime_pop_window_mademonth").val("");
                  $("#avatime_pop_window_madeday").val("");
                  $("#avatime_pop_window_avatime").val("");
                  receiving_commonService.avatime_keyboard_fillGrid($("#avatime_pop_window_keyboard"),2,5,"avatime_pop","keyboard_layout_item",0,"32");
              },
              close:function () {
                  focusOnReceiveInputer();
              }
          };
          receiving_commonService.receiving_tip_dialog("avatime_pop_window",options);
      };
      //有效期输入框会车焦点转移到
      $scope.avaTimeNextFocus = function (e,id) {
          if(!receiving_commonService.autoAddEvent(e)) return;
          switch(id){
              case 'avatime_pop_window_madeyear':{
                  setTimeout(function () {
                      $("#avatime_pop_window_mademonth").focus();
                  },50);
              }break;
              case 'avatime_pop_window_mademonth':{
                  setTimeout(function () {
                      $("#avatime_pop_window_madeday").focus();
                  },50);
              }break;
              case 'avatime_pop_window_madeday':{
                  if($scope.avatimevalue==='1'){
                      $scope.finish_avatime_keyboard();
                  }else{
                      setTimeout(function () {
                          $("#avatime_pop_window_avatime").focus();
                      },50);
                  }
              }break;
              case 'avatime_pop_window_avatime':{
                  $scope.finish_avatime_keyboard();
              }break;
          }
      };
      //显示问题菜单弹窗
      $scope.showProMenuWindow = function () {
          var options = {
              title:INBOUND_CONSTANT.SELECTPMENU,
              width:800,
              height:600,
              close:function () {
                  focusOnReceiveInputer();
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("promenu_pop_window",options);
      };
      //显示数量输入窗口
      function showSelectNumsWindow () {
          if(isDamaged){
              receiving_commonService.receiving_tip_dialog_normal("damage_keyboard_window",{
                  width:600,
                  title:"请输入残品数量",
                  open:function () {
                      $("#keyboard_inputer").val("");
                      $("#keyboard_inputer").focus();
                      receiving_commonService.keyboard_fillGrid($("#damage_keyboard_keys"),2,5,"keyboard","keyboard_layout_item");
                      //$("#keyboard_inputer").focus();
                      setTimeout(function () {
                          $("#keyboard_inputer").focus();
                      },500);
                  },
                  close:function () {
                      $("#keyboard_inputer").value = "";
                      $("#receiving_receive").focus();
                  }
              });
          }else{
              var options = {
                  width:800,
                  height:400,
                  title:"请输入收货数量",
                  open:function () {
                      $("#everpacknums").val('');
                      $("#totallevel").val('');
                      $("#everlevelpacks").val('');
                      $("#uncompletepacks").val('');
                      $("#totalnums").val('');
                      receiving_commonService.keyboard_fillGrid($("#keyboard_keys"),2,5,"keyboard","keyboard_layout_item");
                      setTimeout(function () {
                          $("#everpacknums").focus();
                      },500);
                  },
                  close:function () {
                      $("#keyboard_inputer").value = "";
                      focusOnReceiveInputer();
                  }
              };
              receiving_commonService.receiving_tip_dialog_normal("keyboard_window",options);
          }
      }
      $scope.numsinputmethod = function (id) {
          currentId = id;
          receiving_commonService.getavatimeid(currentId);
      };
      //输入窗口确认按钮事件
      $scope.inputnumfinish = function () {
                  if($("#everpacknums").val()===undefined||$("#everpacknums").val()===null||$("#everpacknums").val()===''){
                      $scope.keyboardStatus ='0';
                      $scope.inputer = '每箱数量';
                      $("#everpacknums").focus();
                      return;
                  }
                  $scope.keyboardStatus ='1';
                  if($("#totallevel").val()===undefined||$("#totallevel").val()===null||$("#totallevel").val()===''){
                      $scope.keyboardStatus ='0';
                      $scope.inputer = '完整层数';
                      $("#totallevel").focus();
                      return;
                  }
                  $scope.keyboardStatus ='1';
                  if($("#everlevelpacks").val()===undefined||$("#everlevelpacks").val()===null||$("#everlevelpacks").val()===''){
                      $scope.keyboardStatus ='0';
                      $scope.inputer = '每层箱数';
                      $("#everlevelpacks").focus();
                      return;
                  }
                  $scope.keyboardStatus ='1';
                  if($("#uncompletepacks").val()===undefined||$("#uncompletepacks").val()===null||$("#uncompletepacks").val()===''){
                      $scope.keyboardStatus ='0';
                      $scope.inputer = '不足一层箱数';
                      $("#uncompletepacks").focus();
                      return;
                  }
                  $scope.keyboardStatus ='1';
                  amount = ($("#everpacknums").val()*$("#everlevelpacks").val()*$("#totallevel").val())+($("#everpacknums").val()*$("#uncompletepacks").val());
                  $scope.totalnums = amount;
                  console.log("商品上架总数--->"+amount);
                  finishAll(amount);
      };
      $scope.finish_keyboard = function (isKeyDown,e) {
          if(isKeyDown){
              if(!receiving_commonService.autoAddEvent(e)) return;
          }
          if ($("#keyboard_inputer").val() === undefined || $("#keyboard_inputer").val() < 1) {
              $scope.keyboardStatus = '0';
              return;
          } else {
              $scope.keyboardStatus = '1';
          }
          amount = $("#keyboard_inputer").val();
          finishAll(amount);
      };
      function finishAll(amount) {
          if(!$scope.isSureGoodsMore&&parseInt(amount)>canReceiveAmount&&parseInt(amount)<=maxAmount){
              var options = {
                  width:600,
                  height:500,
                  title:INBOUND_CONSTANT.SUREGOODSMORE,
                  open:function () {
                      $scope.moregoods = '0';
                      $("#amountsku_sku").html($("#product_info_title").html());
                      $("#amountsku_skuName").html($("#product_info_text").html());
                      $("#amountsku_dnAmount").html(dnAmount);
                      $("#amountsku_receiveAmount").html(receiveAmount);
                      $("#amountsku_inputAmount").html(amount);
                      if((parseInt(amount)-maxAmount)>=0){
                          $("#amountsku_beyondMaxAmount").html((parseInt(amount)-maxAmount));
                      }else{
                          $("#amountsku_beyondMaxAmount").html("0");
                      }
                      $("#amountsku_beyondAmount").html(receiveAmount-dnAmount+(parseInt(amount)));
                      $("#amountsku_content").html("点击确定确认收货,点击取消请将商品放回原包装箱,重新扫描DN号码/商品条码收货");
                  }
              };
              receiving_commonService.receiving_tip_dialog("window_img_ok_cancel_amount_sku",options);
          }else if(!$scope.isSureGoodsMore&&parseInt(amount)>maxAmount){
              var options = {
                  width:600,
                  height:500,
                  title:INBOUND_CONSTANT.SUREGOODSMORE,
                  open:function () {
                      $scope.moregoods = '1';
                      $("#amountsku_sku").html($("#product_info_title").html());
                      $("#amountsku_skuName").html($("#product_info_text").html());
                      $("#amountsku_dnAmount").html(dnAmount);
                      $("#amountsku_receiveAmount").html(receiveAmount);
                      $("#amountsku_inputAmount").html(amount);
                      if((parseInt(amount)-maxAmount)>=0){
                          $("#amountsku_beyondMaxAmount").html((parseInt(amount)-maxAmount));
                      }else{
                          $("#amountsku_beyondMaxAmount").html("0");
                      }
                      $("#amountsku_beyondAmount").html(receiveAmount-dnAmount+(parseInt(amount)));
                      $("#amountsku_content").html("点击确认重新输入数量,点击取消请将商品放回原包装箱,重新扫描DN号码/商品条码收货");
                  }
              };
              receiving_commonService.receiving_tip_dialog("window_img_ok_cancel_amount_sku",options);
          }else{
              receivingService.finishReceive({
                      receiveStation:$rootScope.stationId,
                      receiveStationId:$rootScope.stationName,
                      adviceId:adviceNo,
                      sn:sn,
                      useNotAfter:useAfter,
                      itemId:itemid,
                      storageLocationId:storageid,
                      amount:amount,
                      receiveType:receiveType,
                      finishType:finishType,
                      toolName:INBOUND_CONSTANT.PALLETRECEIVETOSTOW
                  },function () {
                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                      if(receiveType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                          $("#palletscanbadcib").css({"backgroundColor":"#008B00"});
                          $("#receiving_status_span").css({"backgroundColor":"#FF0000"});
                          $("#palletscanbadcib").html(amount);
                          isDamaged = false;
                      }
                      if(receiveType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                          $("#palletscanmeasurecib").css({"backgroundColor":"#008B00"});
                          $("#palletscanmeasurecib").html('1');
                          $("#receiving_status_span").css({"backgroundColor":"#FFC125"});
                          isMeasured = false;
                      }
                      if(receiveType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                          $("#palletscanwaitcib").css({"backgroundColor":"#008B00"});
                          $("#palletscanwaitcib").html('1');
                          $("#receiving_status_span").css({"backgroundColor":"#00BFFF"});
                          isInvest = false;
                      }
                      if(receiveType.toLowerCase()===INBOUND_CONSTANT.GENUINE.toLowerCase()){
                          upid = receiving_commonService.findStorageLocation(storageid,storages);
                          console.log("upid-->"+upid);
                          $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                          $scope.stockAmount = parseInt((amount+$scope.stockAmount));
                          if(isAutoRelease){
                              console.log("自动释放pod");
                              storages = null;
                              $scope.stockAmount = 0;
                              $scope.podlayout = 'hidden';
                              $scope.podShow = '1';
                              scan_pod = false;
                              $("#pallet_storage").html("");
                              $("#pallet_storage").css({"backgroundColor":"#b6b6b6"});
                              $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
                              $scope.releasePod();
                              // $scope.receiveCallPod();
                          }else{
                              $scope.podlayout = '0';
                              $("#pallet_storage").css({"backgroundColor":"#008B00"});
                              $("#pallet_storage").html("<h1>"+amount+"</h1></br>当前货位数量总计:"+$scope.stockAmount);
                          }
                      }
                      $("#receiving_status_span").html("已成功收货上架"+amount+"件商品至</br>"+storageid);
                      receiving_commonService.CloseWindowByBtn("damage_keyboard_window");
                      receiving_commonService.CloseWindowByBtn("window_img_ok_cancel_amount_sku");
                      reSetAllVar();
                      var window = $("#keyboard_window").data("kendoWindow");
                      window.close();
                      focusOnReceiveInputer();
                  },
                  function (data) {
                      scan_product_info = false;
                      checkContainerIsScan();
                      if (data.key === '-1' || data.key === '-2') {
                          showStorageFull(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                      } else {
                          if(isDamaged||isMeasured||isInvest){
                              showGeneralWindow(data.message.replace("[","").replace("]","").replace("Unknown Error",""), data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                          }else{
                              showGeneralWindow(data.message.replace("[","").replace("]","").replace("Unknown Error",""), data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                          }
                      }
                  });
          }
      }

      function focusOnReceiveInputer() {
          setTimeout(function () {
              $("#receiving-inputer").focus();
          },500);
      }

      //输入窗口删除事件
      $scope.deletecurrentinput = function () {
          switch($scope.focusmodel){
              case 0:{
                  receiving_commonService.deleteinput("everpacknums");
              }break;
              case 1:{
                  receiving_commonService.deleteinput("totallevel");
              }break;
              case 2:{
                  receiving_commonService.deleteinput("everlevelpacks");
              }break;
              case 3:{
                  receiving_commonService.deleteinput("uncompletepacks");
              }break;
          }
      }
      //切换收货模式
      $scope.switchMode = function (modeData) {
          isAutoRelease = modeData;
          if(modeData){//自动释放
              $("#receiving-singlemode").css({"backgroundColor":"#6E6E6E"});
              $("#receiving-allmode").css({"backgroundColor":"#3f51b5"});
          }else{//不是单件
              $("#receiving-allmode").css({"backgroundColor":"#6E6E6E"});
              $("#receiving-singlemode").css({"backgroundColor":"#3f51b5"});
          }
          focusOnReceiveInputer();
      };
      //结束收货弹窗
      /*$scope.finishReceiveWindow = function () {
          receivingService.getPodInPath($rootScope.workStationId,$rootScope.sectionId,function (data) {
              if(data.status==='0'){
                  var podInfo = '';
                  for(var i=0;i<data.cls.length;i++){
                      podInfo+=data.cls[i]+',';
                  }
                  showGeneralWindow("提示","当前工作站尚有在途Pod,请等待全部到站并释放完毕再进行退出.</br>"+podInfo);
              }else{
                  var options = {
                      title:INBOUND_CONSTANT.FINISHMENU,
                      width:800,
                      height:600,
                      open:function () {
                          $("#general_content").html(INBOUND_CONSTANT.FINISHRECEIVECONTENT_PALLET);
                      },
                      close:function () {
                          $("#general_content").html("");
                          focusOnReceiveInputer();
                      }
                  };
                  receiving_commonService.receiving_tip_dialog("window_general_ok_cancel",options);
              }
          });
      };*/
      //结束收货弹窗
      $scope.finishReceiveWindow = function () {
          receivingService.getPodInPath($rootScope.workStationId,$rootScope.sectionId,function (data) {
              if(data.status==='0'){
                  var podInfo = '';
                  for(var i=0;i<data.cls.length;i++){
                      podInfo+=data.cls[i]+',';
                  }
                  showGeneralWindow("提示","当前工作站尚有在途Pod,请等待全部到站并释放完毕再进行退出.</br>"+podInfo);
              }else{
                  var options = {
                      title:INBOUND_CONSTANT.FINISHMENU,
                      width:600,
                      height:400,
                      open:function () {
                          $scope.exitflag = '0';
                          $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_PALLET;
                      },
                      close:function () {
                          $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_PALLET;
                          focusOnReceiveInputer();
                      }
                  };
                  receiving_commonService.receiving_tip_dialog_normal("window_general_ok_cancel",options);
              }
          });
      };
      //结束收获取消
      $scope.closeGeneralWindow = function(){
          var window = $("#window_general_ok_cancel").data("kendoWindow");
          window.close();
      };
      //有效期异常确定
      $scope.avaTimeInNormalOk = function () {
          $scope.avatime_normal = '0';
          receiving_commonService.CloseWindowByBtn("avatime_pop_window");
          scan_product_info = false;
          checkContainerIsScan();
      };
      //有效期异常确定
      $scope.avaTimeInNormalCancle = function () {
          $scope.avatime_normal = '0';
      };
      //站台货筐信息
      function gridStorageInfo() {
          $scope.palletReceivedGridOptions = {height: 200,columns: columns,scrollable:false};
          $("#storagewindowtip").html(INBOUND_CONSTANT.CURRENTSTATIONLOADING);
          receivingService.findgridStorageInfo($rootScope.stationName,$rootScope.currentReceive,function (data) {
              console.log("data-->",data);
              $rootScope.receiveProcessDTOList = data.cls.receiveProcessDTOList;
              $scope.scanstatus='0';
              $("#storagewindowtip").html(INBOUND_CONSTANT.CURRENTSTATION);
              $("#palletReceivedGRID").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: data.cls.receiveProcessDTOList}));
          },function (data) {
              $scope.scanstatus = '1';
              $("#warnStation").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
          });
      };
      //画pod布局
      $scope.startPod = function () {
          receiving_commonService.getLocationTypes(function (bindata,areadata) {
              receivingService.bindStorageLocationTypes({
                  "locationTypeDTOS":bindata,
                  "areaDTOS":areadata,
                  "stationid":$rootScope.stationId
              },function (data) {
                  $scope.fullfinish = '1';
                  $scope.podstatus = '0';
                  $rootScope.locationTypeSize = data.cls;
                  if($rootScope.locationTypeSize>0){
                      /*sd update start*/
                      //$scope.webSocketBuilder.build(INBOUND_CONSTANT.readWebSocketPod+$rootScope.workStationId);
                      getPodResult();
                      /*sd update end*/
                  }
                  checkContainerIsScan();
                  setTimeout(function(){ $("#receive-inputer").focus();}, 200);
              },function (data) {
                  if(data.key==='该工作站已绑定货位类型'){
                      $scope.fullfinish = '1';
                      $scope.podstatus = '0';
                      //聚焦pod输入框
                      checkContainerIsScan();
                      setTimeout(function(){ $("#receive-inputer").focus();}, 200);
                  }
              });
          });
      };
      //检查商品是否序列号/是否有效期/是否测量
      function checkType(itemData) {
          console.log("检查商品type");
          if(itemData.serialRecordType===INBOUND_CONSTANT.alwaysrecord){//是否序列号商品
              isSn = true;
              isSingle = true;
          }
          if(itemData.lotMandatory){//是否有效期商品
              isLot = true;
          }
          if (isSn) {
              popSnWindow();
          } else {
              if (isLot) {
                  popLotWindow();
              }
          }
      };
      //检查货位异常释放pod
      $scope.releasePod = function () {
          receiving_commonService.CloseWindowByBtn("scanwindowwithpodbtn");
          if(podid===''||podid===undefined||podid===null){
              podid='P0000000C';
          }
          receivingService.releasePod($rootScope.sectionId,$rootScope.workStationId,podid,false,$rootScope.stationId,function (data) {
              storages = null;
              $scope.stockAmount = 0;
              $scope.podlayout = 'hidden';
              scan_pod = false;
              $("#pallet_storage").html("");
              $("#pallet_storage").css({"backgroundColor":"#b6b6b6"});
              checkContainerIsScan();
              podid = '';
              $scope.podInfo = 'Pod';
              if(data===null||data===''||data===undefined||data.pod===''||data.pod===undefined||data.pod===null){
                  return ;
              }
              $scope.getPodInfo(data.pod);
          });
      };
      //正常点击释放pod
      $scope.releasePodNormal = function(){
          receivingService.releasePod($rootScope.sectionId,$rootScope.workStationId,podid,false,$rootScope.stationId,function (data) {
              storages = null;
              $scope.stockAmount = 0;
              $scope.podlayout = 'hidden';
              $("#pallet_storage").html("");
              $("#pallet_storage").css({"backgroundColor":"#b6b6b6"});
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
              checkContainerIsScan();
              podid = '';
              $scope.podInfo = 'Pod';
              if(data===null||data===''||data===undefined||data.pod===''||data.pod===undefined||data.pod===null){
                  return ;
              }
              $scope.getPodInfo(data.pod);
          });
          $("#receiving-inputer").focus();
      };


      //改变退出工作站提示
      $scope.exitStationBefore = function () {
          $scope.exitflag = '1';
          $scope.exitStationContent = INBOUND_CONSTANT.SUREEXITANDFULL;
      }
      //结束收货确定(结满所有筐)
      $scope.exitStation = function(){
          receivingService.exitReceiveStation($rootScope.stationName,"YES",function(){
                  $scope.maxAmount=null;
                  $scope.stationId = null; // 工作站id
                  $scope.stationName = null; // 工作站name
                  $rootScope.stationId = null;
                  $rootScope.stationName = null;
                  $rootScope.locationTypeSize=null;
                  $rootScope.maxAmount=null;
                  $rootScope.processSize = null;
                  $rootScope.areaSize = null;
                  $rootScope.receiveProcessDTOList = null;
                  $rootScope.currentReceive = null;
                  receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                  $state.go("main.receiving");
              },
              function(data){
                  alert("结束收货失败,请重试");
              });
      }
      //退出工作站不满筐
      $scope.exitNotFull = function(){
          receivingService.exitReceiveStation($rootScope.stationName,"NO",function(){
                  $scope.maxAmount=null;
                  $scope.stationId = null; // 工作站id
                  $scope.stationName = null; // 工作站name
                  $rootScope.stationId = null;
                  $rootScope.stationName = null;
                  $rootScope.locationTypeSize=null;
                  $rootScope.maxAmount=null;
                  $rootScope.processSize = null;
                  $rootScope.areaSize = null;
                  $rootScope.receiveProcessDTOList = null;
                  $rootScope.currentReceive = null;
                  receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                  $state.go("main.receiving");
              },
              function(data){
                  alert("结束收货失败,请重试");
              });
      }
      //显示货位类型
      $scope.showBinTypeWindow = function () {
          var options = {
              title: INBOUND_CONSTANT.storagelocationtype,
              width:850,
              height:550,
              open: function () {
                  if($rootScope.receivecallingpodflag){
                      $("#receiving-stopassignpod").css({"backgroundColor":"#FF0000"});
                      $scope.assignpodinfo = INBOUND_CONSTANT.stopassignpod;
                  }else{
                      $("#receiving-stopassignpod").css({"backgroundColor":"#B3EE3A"});
                      $scope.assignpodinfo = INBOUND_CONSTANT.reassignpod;
                  }
                  receivingService.getSelectedStorageType($rootScope.stationName,function (data) {
                      console.log("bintype--->",data);
                      receiving_commonService.grid_BayTypeInPage(data.cls.allBinType,data.cls.selectedBinType,2);
                  });
              },
              close:function () {
                  focusOnReceiveInputer();
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("showBinType_window", options);
      }

      $scope.stopAssignPod = function () {
          if($scope.assignpodinfo===INBOUND_CONSTANT.stopassignpod){
              receivingService.receivestoporcallpod($rootScope.stationName,false,function (data) {
                  $("#receiving-stopassignpod").css({"backgroundColor":"#B3EE3A"});
                  $scope.assignpodinfo = INBOUND_CONSTANT.reassignpod;
                  $rootScope.receivecallingpodflag = false;
              });
          }else{
              receivingService.receivestoporcallpod($rootScope.stationName,true,function (data) {
                  $("#receiving-stopassignpod").css({"backgroundColor":"#FF0000"});
                  $scope.assignpodinfo = INBOUND_CONSTANT.stopassignpod;
                  $rootScope.receivecallingpodflag = true;
              });
          }
      };
      $scope.scanDnSureItem = function (e) {
          if(!receiving_commonService.autoAddEvent(e)) return;
          receivingService.checkDNProblem(itemid,$scope.sureDn,function () {
              receiving_commonService.CloseWindowByBtn('releasePodWindow');
              itemid = '';
              scan_product_info = false;
              $scope.product_info_con = '1';
              $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
              $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
              checkContainerIsScan();
          },function () {
              $scope.sureDnTip = 'DN扫描错误,请重新扫描DN';
          });
      };
      //放回商品扫描DN确认商品是当前DN商品
      $scope.sureDN = function (e) {
          if(!receiving_commonService.autoAddEvent(e)||$scope.adviceNo===''||$scope.adviceNo===undefined){
              console.log("adviceNo--->"+$scope.adviceNo);
              return;
          }
          receivingService.checkDNProblem(itemid, $scope.adviceNo, function (data) {
              scan_product_info = false;
              checkContainerIsScan();
              receiving_commonService.CloseWindowByBtn("scanwindowwithpodbtn");
          }, function (data) {
              showGeneralWindow(data.message.replace("[","").replace("]","").replace("Unknown Error",""),data.message.replace("[","").replace("]","").replace("Unknown Error",""));
          });
      };
      $scope.close = function(id){
          receivingService.getNewestReceiveAmount(adviceNo,itemid,function (data) {
              maxAmount = parseInt(data);
              receiving_commonService.CloseWindowByBtn(id);
          },function (data) {
              showGeneralWindow("提示","获取最新数量失败");
          });
      };
      //多货取消
      $scope.amountCancle = function(id){
          scan_product_info = false;
          $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
          $scope.product_info_con = '1';
          $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
          $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
          receiving_commonService.CloseWindowByBtn(id);
          receiving_commonService.CloseWindowByBtn("keyboard_window");
      };
      //现实通用提示窗口
      function showGeneralWindow(title,msg) {
          var options = {
              title:title,
              open:function () {
                  $("#tipwindow_span").html(msg);
              },
              close:function () {
                  focusOnReceiveInputer();
              }
          };
          receiving_commonService.receiving_tip_dialog("tipwindow",options);
      };
      //弹出有效期窗口
      function popLotWindow() {
          var options = {
              title:INBOUND_CONSTANT.INPUTAVATIME,
              width:800,
              height:600,
              open:function () {
                  $scope.avatime_normal = '0';
                  $("#avatime_tip").html('商品为有效期商品,请输入商品有效期');
                  /*SD add start   avatimeskuimg*/
                  if(item.itemNo=="235270135112427"){
                      $("#avatimeskuimg").attr("src","../../../../image/nongfushanquan2.png");
                  }
                  if(item.itemNo=="711209561729769"){
                      $("#avatimeskuimg").attr("src","../../../../image/xuebi2.png");
                  }
                  if(item.itemNo=="129714641165855"){
                      $("#avatimeskuimg").attr("src","../../../../image/kele2.png");
                  }
                  if(item.itemNo=="517036749820530"){
                      $("#avatimeskuimg").attr("src","../../../../image/meinianda2.png");
                  }
                  /*SD add end*/
                  $("#avatime_sku_name").html(item.name);
                  $("#avatime_pop_window_madeyear").val("");
                  $("#avatime_pop_window_mademonth").val("");
                  $("#avatime_pop_window_madeday").val("");
                  $("#avatime_pop_window_avatime").val("");
                  if(avaTimeType===INBOUND_CONSTANT.MANUFACTURE){
                      $("#avatime_year_span").html(INBOUND_CONSTANT.MANU_YEAR);
                      $("#avatime_mon_span").html(INBOUND_CONSTANT.MANU_MON);
                      $("#avatime_day_span").html(INBOUND_CONSTANT.MANU_DAY);
                      $scope.avatimevalue = '0';
                      $scope.TimeType = receiving_commonService.switchUnitLot(TimeType);
                  }else{
                      $("#avatime_year_span").html(INBOUND_CONSTANT.NOTMANU_YEAR);
                      $("#avatime_mon_span").html(INBOUND_CONSTANT.NOTMANU_MON);
                      $("#avatime_day_span").html(INBOUND_CONSTANT.NOTMANU_DAY);
                      $scope.avatimevalue = '1';
                  }
                  receiving_commonService.avatime_keyboard_fillGrid($("#avatime_pop_window_keyboard"),2,5,"avatime_pop","keyboard_layout_item");
                  setTimeout(function () {
                      $("#avatime_pop_window_madeyear").focus();
                  },500);
              },
              close:function () {
                  checkContainerIsScan();
                  focusOnReceiveInputer();
              }
          };
          receiving_commonService.receiving_tip_dialog("avatime_pop_window",options);
      };
      //弹出序列号扫描窗口
      function popSnWindow() {
          if(isInvest){
              return;
          }
          var options = {
              width:800,
              height:450,
              title: INBOUND_CONSTANT.SCANSERIALNO,
              open: function () {
                  $scope.cantSN='0';
                  $scope.snTip = '';
                  $scope.sn = '';
                  $("#window-receiving-inputer").val("");
                  $("#inputwindow_span").html(item.name);
                  /*SD add start   avatimeskuimg*/
                  if(item.itemNo=="235270135112427"){
                      $("#SerialNoskuimg").attr("src","../../../../image/nongfushanquan2.png");
                  }
                  if(item.itemNo=="711209561729769"){
                      $("#SerialNoskuimg").attr("src","../../../../image/xuebi2.png");
                  }
                  if(item.itemNo=="129714641165855"){
                      $("#SerialNoskuimg").attr("src","../../../../image/kele2.png");
                  }
                  if(item.itemNo=="517036749820530"){
                      $("#SerialNoskuimg").attr("src","../../../../image/meinianda2.png");
                  }
                  /*SD add end*/
                  setTimeout(function () {
                      $("#window-receiving-inputer").focus();
                  },500);
              },
              close:function () {
                  checkContainerIsScan();
              }
          };
          receiving_commonService.receiving_tip_dialog("scanwindow", options);
      };

      $scope.checkReceiveKeBoard = function (e) {
          if(!receiving_commonService.autoAddEvent(e)){
              return;
          }
          if($("#everpacknums").val()===undefined||$("#everpacknums").val()===null||$("#everpacknums").val()===''){
              $("#everpacknums").focus();
              return;
          }
          $("#totallevel").focus();
          if($("#totallevel").val()===undefined||$("#totallevel").val()===null||$("#totallevel").val()===''){
              $("#totallevel").focus();
              return;
          }
          $("#everlevelpacks").focus();
          if($("#everlevelpacks").val()===undefined||$("#everlevelpacks").val()===null||$("#everlevelpacks").val()===''){
              $("#everlevelpacks").focus();
              return;
          }
          $("#uncompletepacks").focus();
          if($("#uncompletepacks").val()===undefined||$("#uncompletepacks").val()===null||$("#uncompletepacks").val()===''){
              $("#uncompletepacks").focus();
              return;
          }
         // $("#everpacknums").focus();
          amount = ($("#everpacknums").val()*$("#everlevelpacks").val()*$("#totallevel").val())+($("#everpacknums").val()*$("#uncompletepacks").val());
          console.log("add end..."+amount);
          $("#totalnums").val(amount);
          $scope.totalnums = amount;
          console.log("赋值结束");
      };

      function finishPallet() {
          if(maxAmount>0){
              if(isSingle){
                  receivingService.finishReceive({
                      receiveStation:$rootScope.stationId,
                      receiveStationId:$rootScope.stationName,
                      adviceId:adviceNo,
                      sn:sn,
                      useNotAfter:useAfter,
                      itemId:itemid,
                      storageLocationId:storageid,
                      amount:1,
                      receiveType:receiveType,
                      finishType:finishType,
                      toolName:INBOUND_CONSTANT.PALLETRECEIVETOSTOW
                  },function () {
                      if(isMeasured||isDamaged||isInvest){
                          console.log("特殊商品");
                          console.log("isMeasured-->"+isMeasured);
                          if(isMeasured){
                              $("#palletscanmeasurecib").html("1");
                              $("#palletscanmeasurecib").css({"backgroundColor":"#008B00"});
                              isMeasured = false;
                              $("#receiving_status_span").css({"backgroundColor":"#FFC125"});
                          }
                          if(isInvest){
                              $("#palletscanwaitcib").html("1");
                              $("#palletscanwaitcib").css({"backgroundColor":"#008B00"});
                              isInvest = false;
                              $("#receiving_status_span").css({"backgroundColor":"#00BFFF"});
                          }
                          if(isDamaged){
                              $("#palletscanbadcib").html("1");
                              $("#palletscanbadcib").css({"backgroundColor":"#008B00"});
                              isDamaged = false;
                              $("#receiving_status_span").css({"backgroundColor":"#FF0000"});
                          }
                          $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                          // $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                      }else{
                          $scope.stockAmount = parseInt($scope.stockAmount+1);
                          if(isAutoRelease){
                              $scope.stockAmount = 0;
                              $scope.podlayout = 'hidden';
                              $("#pallet_storage").html("");
                              $("#pallet_storage").css({"backgroundColor":"#b6b6b6"});
                              $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
                          }else{
                              $scope.podlayout = '0';
                              $("#pallet_storage").css({"backgroundColor":"#008B00"});
                              $("#pallet_storage").html("<h1>1</h1></br>当前货位数量总计:"+$scope.stockAmount);
                              $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                          }
                          $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                      }
                      $("#receiving_status_span").html("已成功收货上架1件商品至</br>"+storageid);
                      reSetAllVar();
                      receiving_commonService.closePopWindow("keyboard_window");
                      focusOnReceiveInputer();
                  },function (data) {
                      scan_product_info = false;
                      checkContainerIsScan();
                      if (data.key === '-1' || data.key === '-2') {
                          showStorageFull(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                      } else {
                          showGeneralWindow("扫描货筐错误", data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                      }
                  });
              }else{
                  showSelectNumsWindow();
              }
          }else{
              showGeneralWindow("请确认是否要进行多货收货","商品数量超出当前用户可收商品数量最大值,请重新扫描商品进行收货");
              scan_product_info = false;
              checkContainerIsScan();
          }
      }
      //重置相关状态位
      function reSetAllVar() {
          isSingle = false;
          isSn = false;
          isLot = false;
          isDamaged = false;
          isMeasured = false;
          isInvest = false;
          receiveType = INBOUND_CONSTANT.GENUINE;
          finishType = INBOUND_CONSTANT.PALLET;
          scan_product_info = false;
          avaTimeType = null;
          TimeType = null;
          dnAmount = null;
          receiveAmount = null;
          maxAmount = null;
          item = null;
          itemid = null;
          $scope.podlayout = '0';
          sn = null;
          $scope.sn = '';
          itemid = '';
          useAfter = '';
      }
      //序列号弹出框扫描
      $scope.windowScan = function (e) {
          if(!receiving_commonService.autoAddEvent(e)) return;
          sn = $scope.sn;
          receivingService.checkSN(itemid,sn,function () {
              var window = $("#scanwindow").data("kendoWindow");
              window.close();
              isSn = false;
              if(isInvest){
                  isInvest = false;
                  receiveType = INBOUND_CONSTANT.GENUINE;
              }
              if(isLot){
                  popLotWindow();
              }else{
                  $("#receiving-inputer").focus();
              }
              checkContainerIsScan();
          },function (data) {
              console.log("data.key-->"+data.key);
              $scope.sn = '';
              if(data.key===-1){
                  showGeneralWindow(data.values[1],data.values[0]);
              }else{
                  $scope.cantSN='1';
                  $scope.snTip = sn+INBOUND_CONSTANT.NOTSNRESCAN;
              }
          });
      };
      //序列号确定
      $scope.win_serok = function () {
          receiving_commonService.CloseWindowByBtn("scanwindow");
          $scope.scanwaitcib='0';
          isInvest = true;
          $("#palletscanwaitcib").html('1');
          $("#palletscanwaitcib").css({"backgroundColor":"#00BFFF"});
          $('#receiving_tip').html(INBOUND_CONSTANT.SCANINVESTAGETE);
          finishType = INBOUND_CONSTANT.SINGLE;
          receiveType = INBOUND_CONSTANT.TO_INVESTIGATE;
          isSingle = true;
          if(isDamaged){
              $scope.scanbadcib='1';
              isDamaged = false;
          }
          if(isMeasured){
              isMeasured = false;
              $scope.scanmeasurecib='1';
          }
          checkContainerIsScan();
      };
      //序列后窗口取消
      $scope.win_sercancle = function () {
          $scope.cantSN='0';
          $scope.snTip = '';
          setTimeout(function () {
              $("#window-receiving-inputer").focus();
          },500);
      };
     //有效期输入框焦点函数
      $scope.avatimemethod = function (currentid) {
          currentId = currentid;
          receiving_commonService.getavatimeid(currentid);
      }
      //显示货位无法扫描窗口
      var side = null;
      $scope.clickReportLight = function () {
          if(podid===undefined||podid===null||podid===''){
              showGeneralWindow("请先扫描Pod","请先扫描Pod");
              return;
          }
          receiving_commonService.CloseWindowByBtn("promenu_pop_window");
          var options = {
              title:"选择暗灯菜单",
              width:1000,
              height:650,
              open:function () {
                  //获取暗灯菜单
                  $("#badStorageState").css({"display":"none"});
                  receivingService.getReportLight(function (data) {
                      receiving_commonService.grid_ReportMenu(data,2,function (typeid) {
                          //获取点击div的id
                          var typeName = $("#"+typeid).children("span").html();
                          reportTypeId = typeid;
                          if(typeName.indexOf("货位存在残品")!=-1){
                              $("#badStorageState").css({"display":"block"});
                              setTimeout(function () {
                                  $("#scan-badstorage-inputer").focus();
                              },500);
                          }else{
                              $("#badStorageState").css({"display":"none"});
                              receivingService.createReportLight({
                                  "storageLocationId":storages[0].id,
                                  "problemName":$("#"+typeid).children("span").html().replace($("#"+typeid).children("span").html().substring(0,1),"").replace(".",""),
                                  "anDonMasterTypeId":typeid,
                                  "state":"undisposed",
                                  "reportBy":$window.localStorage["username"],
                                  "clientId":$window.localStorage["clientId"],
                                  "warehouseId":$window.localStorage["warehouseId"]
                              },function () {
                                  receiving_commonService.CloseWindowByBtn('report_light_pop_window');
                                  setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                              });
                          }
                      });
                  });
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("report_light_pop_window",options);
      };

      $scope.scanBadStorage = function (e) {
          if(!receiving_commonService.autoAddEvent(e)){
              return;
          }
          //提交暗灯
          if($scope.scanbadstorageinputer===''||$scope.scanbadstorageinputer===undefined){
              $scope.scanbadstorageworn = INBOUND_CONSTANT.storagecantscan;
              $scope.scanbadstorageinputer = '';
              setTimeout(function () {
                  $("#scan-badstorage-inputer").focus();
              },500);
              return;
          }
          var objlength = receiving_commonService.getObjCount(storages);
          var checkflag = false;
          for (var i=0;i<objlength;i++){
              if(storages[i].name===$scope.scanbadstorageinputer){
                  checkflag = true;
              }
          }
          if(!checkflag){
              $scope.scanbadstorageworn = INBOUND_CONSTANT.storagecantscan;
              $scope.scanbadstorageinputer = '';
              setTimeout(function () {
                  $("#scan-badstorage-inputer").focus();
              },500);
              return;
          }
          receivingService.createReportLight({
              "storageLocationId":storages[0].id,
              "problemName":$("#"+reportTypeId).children("span").html().replace($("#"+reportTypeId).children("span").html().substring(0,1),"").replace(".",""),
              "anDonMasterTypeId":reportTypeId,
              "state":"undisposed",
              "reportBy":$window.localStorage["username"],
              "clientId":$window.localStorage["clientId"],
              "warehouseId":$window.localStorage["warehouseId"]
          },function () {
              receiving_commonService.CloseWindowByBtn('report_light_pop_window');
              setTimeout(function(){ $("#receiving-inputer").focus();}, 500);
          });
      };

      //有效期弹出框确定
      $scope.finish_avatime_keyboard = function () {
          if($("#avatime_pop_window_madeyear").val()===''||
              $("#avatime_pop_window_madeyear").val()===undefined||
              $("#avatime_pop_window_mademonth").val()===''||
              $("#avatime_pop_window_mademonth").val()===undefined||
              $("#avatime_pop_window_madeday").val()===''||
              $("#avatime_pop_window_madeday").val()===undefined){
              return;
          }else if($("#avatime_pop_window_madeyear").val()>9999||
              $("#avatime_pop_window_mademonth").val()>12||
              $("#avatime_pop_window_madeday").val()>31){
              return;
          }
          var date = $("#avatime_pop_window_madeyear").val()+"-"+
              $("#avatime_pop_window_mademonth").val()+"-"+
              $("#avatime_pop_window_madeday").val();
          var CurrentDate = receiving_commonService.getDate();
          console.log("currentDate-->"+CurrentDate);
          var cDate = new Date(CurrentDate.replace("-",",")).getTime() ;
          var inputDate = new Date(date.replace("-",",")).getTime();
          console.log("输入时间-->"+inputDate);
          console.log("当前时间"+cDate);
          var arriveTime = null;
          if(avaTimeType===INBOUND_CONSTANT.MANUFACTURE){//按照生产日期
              if(inputDate>=cDate){
                  var options = {
                      title:INBOUND_CONSTANT.CANTBEMANUTTIME,
                      open:function () {
                          $("#tipwindow_span").html(INBOUND_CONSTANT.CANTBEMANUTTIME);
                      }
                  };
                  receiving_commonService.receiving_tip_dialog("tipwindow",options);
                  return;
              }
              arriveTime = $("#avatime_pop_window_avatime").val();
              useAfter = receiving_commonService.getDateFormat(receiving_commonService.getDays(date,arriveTime,TimeType));
              if(useAfter<cDate){
                  useAfter = null;
                  var options = {
                      title:INBOUND_CONSTANT.SURETIME,
                      open:function () {
                          $("#tipwindow_span").html(INBOUND_CONSTANT.SURETIME);
                      }
                  };
                  receiving_commonService.receiving_tip_dialog("tipwindow",options);
                  return;
              }
          }else{
              if(inputDate<cDate){
                  var options = {
                      title:INBOUND_CONSTANT.CANTBECURRENTTIME,
                      open:function () {
                          $("#tipwindow_span").html(INBOUND_CONSTANT.CANTBECURRENTTIME);
                      }
                  };
                  receiving_commonService.receiving_tip_dialog("tipwindow",options);
                  return;
              }
              useAfter = receiving_commonService.getDateFormat(receiving_commonService.getDays(date,0,'DAY'));
          }
          console.log("avaTimeType-->"+avaTimeType);
          console.log("有效期-->"+useAfter);
          console.log("时间类型-->"+TimeType);
          receivingService.checkAvaTime(itemid,useAfter,podid,function (data) {
              var window = $("#avatime_pop_window").data("kendoWindow");
              window.close();
              if(isMeasured){
                  $('#receiving_tip').html(INBOUND_CONSTANT.SCANMEASURE);
                  $scope.scanmeasurecib = '0';
                  setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                  checkContainerIsScan();
              }else{
                  if(data.cls===undefined||data.cls===null||data.cls===''||data.cls.length===0){
                      receiving_commonService.receiving_tip_dialog("releasePodWindow",{
                          width:600,
                          height:500,
                          open:function () {
                              $scope.sureDnTip = INBOUND_CONSTANT.NOINTROSTORAGES;
                              $scope.sureDn = '';
                              setTimeout(function () {
                                  $("#sureDn").focus();
                              },100);
                          },
                          close:function () {
                              focusOnReceiveInputer();
                          }
                      });
                  }else{
                      setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                      checkContainerIsScan();
                  }
              }
          },function (data) {
              useAfter = null;
              $("#avatime_tip").html(data.key);
              $scope.avatime_normal = '1';
          });
      }
      $scope.delete_avavalue = function () {
          if(currentId===undefined||currentId===null){
              return;
          }
          $("#"+currentId).val("");
      }
      $scope.startPodInPage = function () {
          receiving_commonService.getLocationTypesInPage(function (data) {
              receivingService.bindStorageLocationTypes({
                  "locationTypeDTOS":data,
                  "stationid":$rootScope.stationId
              },function () {
                  receiving_commonService.CloseWindowByBtn("showBinType_window");
                  setTimeout(function(){ $("#receive-inputer").focus();}, 200);
              },function (data) {
                  showGeneralWindow("绑定货位类型失败","绑定货位类型失败");
              });
          });
      }
      // 扫描旧货筐
      var isOld = true;
      $scope.scanOldContainer = function(e){
          if(!receiving_commonService.autoAddEvent(e)){
              return;
          }
          var storageName = $("#window-storage-inputer").val();
          if(storageName===''||storageName===undefined){
              $("#inputstoragewindow_span").html("货筐条码无效");
              return;
          }
          if(isOld){
              receivingService.fullStorage(storageName,$rootScope.stationName,function (data) {
                  console.log("data-->"+JSON.stringify(data));
                  $("#inputstoragewindow_span").html("已成功满筐"+data.cls.storageType+",货筐条码:"+data.cls.storageName+",商品总数"+data.cls.stockAmount+"\n请扫描新的货筐");
                 // $("#palletReceivedGRID").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: $rootScope.receiveProcessDTOList}));
                  gridStorageInfo();
                  destinationId = data.cls.destinationId;
                  positionIndex = data.cls.positionIndex;
                  $scope.scancontainerType = data.cls.storageType;
                  if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                      console.log("满残品");
                      $scope.scanbadcib = '0';
                      scan_product_content_DAMAGED = false;
                  }
                  if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                      console.log("满测量");
                      scan_product_content_MEASURED = false;
                      $scope.scanmeasurecib = '0';
                  }
                  if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                      console.log("满待调查");
                      scan_product_content_TO_INVESTIGATE = false;
                      $scope.scanwaitciban = '0';
                  }

                  $("#window-storage-inputer").val("");
                  isOld = false;
                  $("#window-storage-inputer").focus();
              },function (data) {
                  $("#inputstoragewindow_span").html("满筐失败,请重新扫描");
              });
          }else{
              receivingService.scanStorageLocation(storageName,$scope.scancontainerType,$rootScope.stationName,destinationId,positionIndex,function (data) {
                  console.log("扫描新货筐数据返回---》",data);
                  if(data.status==='2'){//有商品,提示用户
                      $scope.scancontainerType = data.cls.storagelocationType;
                      storageid = storageName;
                      var options = {
                          width:600,
                          height:500,
                          title:INBOUND_CONSTANT.SUREUSELOCATION,
                          open:function () {
                              $scope.wimgstatus='hidden';
                              $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                          },
                          close:function () {
                              $("#receiving-inputer").focus();
                          }
                      };
                      receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                  }else{
                      isOld = true;
                      storageid = storageName;
                      if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                          scan_product_content_DAMAGED = true;
                          console.log("成功绑定残品筐"+scan_product_content_DAMAGED);
                      }
                      if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                          scan_product_content_MEASURED = true;
                      }
                      if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                          scan_product_content_TO_INVESTIGATE = true;
                      }

                      if(isDamaged||isMeasured||isInvest){
                          if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                              $scope.scanbadcib = '0';
                          }
                          if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                              $scope.scanmeasurecib = '0';
                          }
                          if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                              $scope.scanwaitciban = '0';
                          }
                          finishPallet();
                         // return;
                      }
                      destinationId = null;
                      positionIndex = null;
                      $scope.scancontainerType = null;
                      receiving_commonService.CloseWindowByBtn('scanstoragewindow');
                  }
              },function (data) {
                  var options = {
                      title:INBOUND_CONSTANT.RESCANLOCATION,
                      open:function () {
                          $("#tipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                      },
                      close:function () {
                          $("#receiving-inputer").focus();
                      }
                  };
                  receiving_commonService.receiving_tip_dialog("tipwindow",options);
              });
          }
      };

      $scope.showChartWindow = function () {
          $("#chart").kendoChart({
              title: {
                  text: "当前用户工作情况记录图表"
              },
              legend: {
                  position: "bottom"
              },
              chartArea: {
                  background: ""
              },
              seriesDefaults: {
                  type: "line",
                  style: "smooth"
              },
              series: [{
                  name: "目标情况",
                  data: [100, 100, 100, 100, 100, 100, 100, 100, 100, 100,100,100,100]
              },{
                  name: "实际情况",
                  data: [80, 82, 82, 83, 85, 90, 91, 90, 92, 91,94,93,96]
              }],
              valueAxis: {
                  labels: {
                      format: "{0}%"
                  },
                  line: {
                      visible: false
                  },
                  axisCrossingValue: -10
              },
              categoryAxis: {
                  categories: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13],
                  majorGridLines: {
                      visible: false
                  },
                  labels: {
                      rotation: "auto"
                  }
              },
              tooltip: {
                  visible: true,
                  format: "{0}%",
                  template: "#= series.name #: #= value #"
              }
          });
          // $(document).ready(createChart);
          // $(document).bind("kendo:skinChange", createChart);
          var options = {
              title:"信息查询",
              width:1000,
              height:550,
              open:function () {
                  receiving_commonService.CloseWindowByBtn("promenu_pop_window");
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("chart-window",options);
      };

      //清楚所有上次收货状态信息
      function cleanStatus() {
          if(!isDamaged&&!isInvest&&!isMeasured){
              $scope.scanbadcib='1';
              $scope.scanmeasurecib='1';
              $scope.scanwaitcib='1';
              $("#receiving_status_span").html("");
              $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATIONORDN);
          }
      }
      function checkContainerIsScan() {
          if(!scan_product_content_DAMAGED){
              if(!isDamaged){
                  $("#receiving_dn_span").html("");
                  $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                  $scope.product_info_con = 'hidden';
                  $("#product_info_span").html("");
                  $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                  $("#receiving_status_span").html("");
                  $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                  $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                  $("#" + upid).children("span").text("");

                  $("#palletscanbadcib").html(INBOUND_CONSTANT.RESCANDAMAGED);
              }else{
                  $("#palletscanbadcib").html('1');
              }

              $scope.scanbadcib='0';
              $scope.scanmeasurecib='1';
              $scope.scanwaitcib='1';
              $("#palletscanbadcib").css({"backgroundColor": "#FF0000"});
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANDAMAGED);
          }else{
              if(!scan_product_content_MEASURED){
                  if(!isMeasured){
                      $("#receiving_dn_span").html("");
                      $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                      $scope.product_info_con = 'hidden';
                      $("#product_info_span").html("");
                      $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                      $("#receiving_status_span").html("");
                      $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                      $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                      $("#" + upid).children("span").text("");

                      $("#palletscanmeasurecib").html(INBOUND_CONSTANT.SCANMEASURE);
                  }else{
                      $("#palletscanmeasurecib").html('1');
                  }

                  $scope.scanmeasurecib='0';
                  $scope.scanbadcib='1';
                  $scope.scanwaitcib='1';
                  // $("#scanmeasurecib").html(INBOUND_CONSTANT.SCANMEASURE);
                  $("#palletscanmeasurecib").css({"backgroundColor": "#FFF000"});
                  $("#receiving_tip").html(INBOUND_CONSTANT.SCANMEASURE);
              }else{
                  if(!scan_product_content_TO_INVESTIGATE){
                      if(!isInvest){
                          $("#receiving_dn_span").html("");
                          $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                          $scope.product_info_con = 'hidden';
                          $("#product_info_span").html("");
                          $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                          $("#receiving_status_span").html("");
                          $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                          $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                          $("#" + upid).children("span").text("");

                          $("#palletscanwaitcib").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                      }else{
                          $("#palletscanwaitcib").html('1');
                      }

                      $scope.scanwaitcib='0';
                      $scope.scanbadcib='1';
                      $scope.scanmeasurecib='1';
                      // $("#scanwaitcib").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                      $("#palletscanwaitcib").css({"backgroundColor": "#00b0ff"});
                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                  }else{
                      $scope.scanwaitcib='1';
                      $scope.scanbadcib='1';
                      $scope.scanmeasurecib='1';
                      $scope.podlayout = '0';
                      if(!scan_pod){
                          $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
                      }else {
                          if (!scan_DN) {
                              if (isDamaged) {
                                  $scope.scanbadcib = '0';
                                  $scope.scanmeasurecib = '1';
                                  $scope.scanwaitcib = '1';
                                  $scope.podlayout = '1';
                                  $("#palletscanbadcib").css({"backgroundColor": "#FF0000"});
                                  $("#palletscanbadcib").html("1");
                              } else if (isMeasured) {
                                  $scope.scanbadcib = '1';
                                  $scope.scanmeasurecib = '0';
                                  $scope.scanwaitcib = '1';
                                  $("#palletscanmeasurecib").css({"backgroundColor": "#FFF000"});
                                  $("#palletscanmeasurecib").html("1");
                              } else if (isInvest) {
                                  $scope.scanbadcib = '1';
                                  $scope.scanmeasurecib = '1';
                                  $scope.scanwaitcib = '0';
                                  $("#palletscanwaitcib").css({"backgroundColor": "#00b0ff"});
                                  $("#palletscanwaitcib").html("1");
                              }
                              $scope.product_info_con = '1';
                              $("#product_info_span").css({"backgroundColor": "#EEEEE0"});
                              $("#product_info_span").html('');

                              $("#receiving_tip").html(INBOUND_CONSTANT.SCANDN);
                              $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANDN);
                              $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});

                          } else {
                              if (!scan_product_info) {
                                  if (isDamaged) {
                                      $scope.scanbadcib = '0';
                                      $scope.scanmeasurecib = '1';
                                      $scope.scanwaitcib = '1';
                                      $scope.podlayout = '1';
                                      $("#palletscanbadcib").css({"backgroundColor": "#FF0000"});
                                      $("#palletscanbadcib").html("1");
                                  } else if (isMeasured) {
                                      $scope.scanbadcib = '1';
                                      $scope.scanmeasurecib = '0';
                                      $scope.scanwaitcib = '1';
                                      $("#palletscanmeasurecib").css({"backgroundColor": "#FFF000"});
                                      $("#palletscanmeasurecib").html("1");
                                  } else if (isInvest) {
                                      $scope.scanbadcib = '1';
                                      $scope.scanmeasurecib = '1';
                                      $scope.scanwaitcib = '0';
                                      $("#palletscanwaitcib").css({"backgroundColor": "#00b0ff"});
                                      $("#palletscanwaitcib").html("1");
                                  }
                                  $scope.product_info_con = '1';
                                  $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                  $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                  $("#receiving_status_span").html("");
                                  $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                  $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                  $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                              } else {
                                  if (isDamaged) {
                                      $scope.scanbadcib = '0';
                                      $scope.scanmeasurecib = '1';
                                      $scope.scanwaitcib = '1';
                                      $scope.podlayout = '1';
                                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANSKUTODAMAGED);
                                  } else if (isMeasured) {
                                      $scope.scanbadcib = '1';
                                      $scope.scanmeasurecib = '0';
                                      $scope.scanwaitcib = '1';
                                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANMEASURE);
                                  } else if (isInvest) {
                                      $scope.scanbadcib = '1';
                                      $scope.scanmeasurecib = '1';
                                      $scope.scanwaitcib = '0';
                                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                                  } else {
                                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATIONORDN);
                                  }
                              }
                          }
                      }
                  }
              }
          }
          setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
      }
      // 初始化
      $scope.receivingCurrent = $stateParams.id; // 当前收货模式
    $scope.inputer = '';
    $scope.receivingGridOptions = {selectable: "row", height: 260, sortable: true, columns: columns};
    $scope.fullfinish = '1';
    $scope.podstatus = '0';
    $("#receiving-inputer").focus(); // 首获焦
      if($rootScope.locationTypeSize===0){
          $scope.fullfinish = '0';
          $scope.podstatus = '1';
          receivingService.getStorageLocationTypes(function (data) {
              //填充数据
              receiving_commonService.grid_BayType(data.cls.storageLocationDTOList,data.cls.binTypeColumn.column,data.cls.binTypeColumn.row);
          });
      }else{
          $scope.fullfinish = '1';
          $scope.podstatus = '0';
          checkContainerIsScan();
      }
  });
})();