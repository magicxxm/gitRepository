
/**
 * Created by frank.zhou on 2016/12/28.
 */
(function () {
  'use strict';
    angular.module('myApp').controller("receivingSingleCtl", function($window,$scope, $rootScope, $state, $stateParams,
                                                                      webSocketService,INBOUND_CONSTANT,receiving_commonService,receivingService,mySocket){
        var headerStyle = {"class": "table-header-cell",style:"font-size:16px;line-height:2;color:white;height:35px;overflow:hidden"};
        var baseStyle = {style: "font-size:16px;height:25px;text-align:center;overflow:hidden"};
        var columns= [
            {field: "desinationName", headerTemplate: "<span translate='POSITION_NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
            {field: "receiveStorageName", headerTemplate: "<span translate='PICKCAR_NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
            {field: "amount", headerTemplate: "<span translate='SKU_COUNT'></span>", attributes: baseStyle,headerAttributes:headerStyle}
        ];

      $scope.scanstatus = '0'; 
      $scope.fullfinish = '0';
      $scope.sideshow = '1';
      $scope.currentReceive = $stateParams.id;
      $scope.user = $window.localStorage["name"];
      $scope.operateTime = '0.9小时';
      $scope.operateTotalCount = '250';
      $scope.operatePercentage = '260/小时';
      $scope.goal = '500';
      $scope.achieved = '83%';
      $scope.prePod = 'VirtualPod';
      $scope.preLocation = 'VirtualLocation';
      $scope.preDN = 'VirtualDN';
      $scope.isSureGoodsMore = false;
      $scope.isNotIntro = false;
      var isOld = true;
      var destinationId = null;
      var positionIndex = null;
      var innormalBin = false;
      var receiveType = INBOUND_CONSTANT.GENUINE;
      var finishType = INBOUND_CONSTANT.ALL;
      var item = null;
      if($rootScope.scan_product_content_DAMAGED===undefined)
          $rootScope.scan_product_content_DAMAGED=false;
      if($rootScope.scan_product_content_MEASURED===undefined)
          $rootScope.scan_product_content_MEASURED=false;
      if($rootScope.scan_product_content_TO_INVESTIGATE===undefined)
          $rootScope.scan_product_content_TO_INVESTIGATE=false;
      var scan_product_content_GENUINE= false;
      var scan_product_content_DAMAGED= $rootScope.scan_product_content_DAMAGED;
      var scan_product_content_MEASURED= $rootScope.scan_product_content_MEASURED;
      var scan_product_content_TO_INVESTIGATE= $rootScope.scan_product_content_TO_INVESTIGATE;
      $("#receiving-singlemode").css({"backgroundColor":"#6E6E6E"});
      $("#receiving-allmode").css({"backgroundColor":"#3f51b5"});
      var scan_pod = false;
      var scan_DN = false;
      var scan_product_info= false;
      var scan_bin= false;
      var isSingle= false;
      var isAll= true;
      //是否测量
      var isMeasured= false;
      //是否待调查
      var isInvest = false;
      var isDamaged = false;
      var isLot = false;
      var isSn = false;
      var normalFull = false;
      var adviceNo = '';
      var storageid = '';
      var amount = '';
      //应收数量
      var dnAmount = null;
      var podName = null;
      //实收数量
      var receiveAmount = null;
      //用户可收最大数量
      var maxAmount = null;
      //正常可收数量
      var canReceiveAmount = null;
      var itemid = "";
      var sn = "";
      var useAfter = "";
      var podid = '';
      var upid='';
      var avaTimeType='';
      var TimeType='';
      var currentId='';

      /*sd add start*/
        var podSocket;//webSocket
        /*sd add end*/

        var websocketClient = null;
      $scope.Pod = 'Pod';
      $scope.podShow='1';
      var storages = [];
      var introStorages = [];
      $scope.mySocket = mySocket;
      $scope.webSocketBuilder = mySocket.webSocketBuilder;
      //跳转收货页面
      $scope.toReceiving = function(){
          $state.go($scope.receivingCurrent==='single'? "main.receivingSingle": "main.receivingPallet");
      };

      /*未用*/
      $scope.reloadPod = function () {
            if(podid!==''&&podid!==null&&podid!==undefined){
                receivingService.releasePod($rootScope.sectionId,$rootScope.workStationId,podid,false,$rootScope.stationId,function (data) {
                    podid = '';
                    $("#receiving_pod_layout").html("");
                    $scope.Pod = 'Pod';
                    if(data.pod===''||data.pod===null||data.pod===undefined){
                        $scope.podShow='1';
                    }else{
                        getPodInfo(data.pod);
                    }
                    focusOnReceiveInputer();
                });
            }else{
                receivingService.refreshPod($rootScope.sectionId,$rootScope.workStationId,function (data) {
                    if(data.pod===''||data.pod===null||data.pod===undefined){
                        $scope.podShow='1';
                    }else{
                        getPodInfo(data.pod);
                    }
                    focusOnReceiveInputer();
                });
            }
        };
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
                        getPodInfo(data.pod);
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
          getPodResult();
      }else{
          console.log("货位类型未选");
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
                            getPodInfo(data.pod);
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
      //刷新pod信息
      $scope.refreshPod = function () {
          $("#receiving_pod_layout").html("");
          $scope.Pod = 'Pod';
          receivingService.refreshPod($rootScope.sectionId,$rootScope.workStationId,function (poddata) {
              if(poddata==null||poddata===undefined||poddata===''||poddata.pod===''||poddata.pod===null||poddata.pod===undefined){
                  $scope.podShow='1';
                  focusOnReceiveInputer();
                  return;
              }
              getPodInfo(poddata.pod);
              focusOnReceiveInputer();
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
      //收货数量弹出框
      $scope.finish_keyboard = function (isKeyDonw,e) {
          if(isKeyDonw==true){
              if(!receiving_commonService.autoAddEvent(e)) return;
          }
          if($("#keyboard_inputer").val()===undefined||$("#keyboard_inputer").val()<1){
                $scope.keyboardStatus='0';
                return;
          }else{
                $scope.keyboardStatus='1';
          }
          amount = $("#keyboard_inputer").val();
          console.log("maxAmount--->"+maxAmount);
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
            }
          else if(!$scope.isSureGoodsMore&&parseInt(amount)>maxAmount){
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
          }
          else{
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
                    toolName:INBOUND_CONSTANT.EACHRECEIVETOSTOW
                },function () {
                    scan_product_info = false;
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                    if(receiveType===INBOUND_CONSTANT.DAMAGED){
                        $("#scanbadcib").css({"backgroundColor":"#008B00"});
                        $("#receiving_status_span").css({"backgroundColor":"#FF0000"});
                        $("#scanbadcib").html(amount);
                        isDamaged = false;
                    }
                    if(receiveType===INBOUND_CONSTANT.MEASURED){
                        $("#scanmeasurecib").css({"backgroundColor":"#008B00"});
                        $("#scanmeasurecib").html('1');
                        $("#receiving_status_span").css({"backgroundColor":"#FFC125"});
                        isMeasured = false;
                    }
                    if(receiveType===INBOUND_CONSTANT.TO_INVESTIGATE){
                        $("#scanwaitcib").css({"backgroundColor":"#008B00"});
                        $("#scanwaitcib").html('1');
                        $("#receiving_status_span").css({"backgroundColor":"#00BFFF"});
                        isInvest = false;
                    }
                    if(receiveType===INBOUND_CONSTANT.GENUINE){
                        upid = receiving_commonService.findStorageLocation(storageid,storages);
                        console.log("upid-->"+upid);
                        $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                        $("#"+upid).css({"backgroundColor":"#008B00"});
                        $("#"+upid).children("span").text(amount);
                    }
                    $("#receiving_status_span").html("已成功收货上架"+amount+"件商品至</br>"+storageid);
                    reSetAllVar();
                    receiving_commonService.CloseWindowByBtn("window_img_ok_cancel_amount_sku");
                    var window = $("#keyboard_window").data("kendoWindow");
                    window.close();
                    focusOnReceiveInputer();
                },
                function (data) {
                    if (data.key === '-1' || data.key === '-2') {
                        showStorageFull(data.values/*.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error","")*/);
                    } else {
                        if(isDamaged||isMeasured||isInvest){
                            showGeneralWindow(data.message.replace("Unknown Error",""), data.message.replace("Unknown Error",""));
                        }else{
                            showGeneralWindow(data.message.replace("Unknown Error",""), data.message.replace("Unknown Error",""));
                        }
                    }
                });
            }
      };
      //序列号无法扫描
      $scope.cantScanSN = function () {
          showGeneralWindow("功能待定","功能待定");
          return;
          if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE){
              showGeneralWindow("请先绑定货筐","请先绑定残品/测量/待调查货筐");
              return;
          }
          $scope.product_info_con = '1';
          $scope.scanwaitcib = '0';
          if(!scan_DN){
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANDN);
              $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANDN);
              $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
          }else{
              $("#receiving_dn_span").css({"backgroundColor":"#EEEEE0"});
              $("#product_info_span").css({"backgroundColor":"#FFDEAD"});
              $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
              $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
          }
          if(isMeasured){
              isMeasured = false;
          }
          scan_product_info = false;
          isInvest = true;
          scan_pod = true;
          receiveType = INBOUND_CONSTANT.TO_INVESTIGATE;
          receiving_commonService.CloseWindowByBtn("promenu_pop_window");
          focusOnReceiveInputer();
      };

      function focusOnReceiveInputer() {
          setTimeout(function () {
              $("#receiving-inputer").focus();
          },500);
      }
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
          var CurrentDate = receiving_commonService.getDate();//获取当前时间
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
          receivingService.checkAvaTime(itemid,useAfter,podid,function (data) {
              var window = $("#avatime_pop_window").data("kendoWindow");
              window.close();
              isLot = false;
              if(isMeasured){
                  $('#receiving_tip').html(INBOUND_CONSTANT.SCANMEASURE);
                  $scope.scanmeasurecib = '0';
                  setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                  checkContainerIsScan();
              }else{
                  if(data.cls===undefined||data.cls===null||data.cls===''||data.cls.length===0){
                      receiving_commonService.receiving_tip_dialog("releasePodWindow",{
                          title:"无推荐货位",
                          width:600,
                          height:500,
                          open:function () {
                              $scope.sureDnTip = INBOUND_CONSTANT.NOINTROSTORAGES;
                              $scope.sureDn = '';
                              setTimeout(function () {
                                  $("#sureDn").focus();
                              },500);
                          },
                          close:function () {
                              focusOnReceiveInputer();
                          }
                      });
                  }else{
                      introStorages = data.cls;
                      checkContainerIsScan();
                      if(!isDamaged){
                          receiving_commonService.colorIntroStorage(introStorages,storages);
                      }
                      setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                  }
              }
          },function (data) {
              useAfter = null;
              $("#avatime_tip").html(data.key);
              $scope.avatime_normal = '1';
              // showGeneralWindow("有效期检查失败",data.key);
          });
      };
      //释放pod
      $scope.releasePod = function() {
          receiving_commonService.CloseWindowByBtn("releasePodWindow");
          if(podid===''||podid===undefined||podid===null){
              podid='P0000000C';
          }
          receivingService.releasePod($rootScope.sectionId,$rootScope.workStationId,podid,false,$rootScope.stationId,function (data) {
              podid = '';
              scan_pod = false;
              $scope.Pod = 'Pod';
              $("#receiving_pod_layout").html("");
              //data.podName
              if(data===null||data===''||data===undefined||data.pod===''||data.pod===undefined||data.pod===null){
                  $scope.podShow ='1';
                  return ;
              }
              getPodInfo(data.pod);
              if (isDamaged) {
                  if ($scope.product_info_con === '1') {
                      $scope.product_info_con = 'hidden';
                      $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                      $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                  }
                  $scope.scanbadcib = '1';
              } else {
                  if (isMeasured) {
                      if ($scope.product_info_con === '1') {
                          $scope.product_info_con = 'hidden';
                          $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                          $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                      }
                      $scope.scanmeasurecib = '1';
                  } else {
                      if (isInvest) {
                          if ($scope.product_info_con === '1') {
                              $scope.product_info_con = 'hidden';
                              $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                              $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                          }
                          $scope.scanwaitcib = '1';
                      } else {
                          if (!scan_DN) {
                              $("#receiving_dn_span").html('');
                              $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                          } else {
                              $("#receiving_dn_span").html(adviceNo);
                              if (!scan_product_info) {
                                  $scope.product_info_con = 'hidden';
                                  $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                  $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                  $("#receiving_status_span").html("");
                                  $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                  $scope.scanbadcib = '1';
                                  $scope.scanmeasurecib = '1';
                                  $scope.scanwaitcib = '1';
                              }
                          }
                      }
                  }
              }
          });
      };

      $scope.receivingscan = function (e) {
          if(!receiving_commonService.autoAddEvent(e)) return;
          var inputvalue = $("#receiving-inputer").val().trim()||$scope.tipvalue;
          console.log("inputvalue-->"+inputvalue);
          $("#receiving-inputer").val("");
          receiving_commonService.CloseWindowByBtn("newtipwindowwithinputer");
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
                                      $("#win_content").html("当前货筐:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
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
                          scan_product_content_DAMAGED = true;
                          checkContainerIsScan();
                      }
                  },function (data) {
                      var options = {
                          title:INBOUND_CONSTANT.RESCANDAMAGED,
                          width:600,
                          height:400,
                          open:function () {
                              $scope.tipvalue = '';
                              $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                              setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                          }
                      },function (data) {
                          var options = {
                              title:INBOUND_CONSTANT.RESCANMEASURED,
                              width:600,
                              height:400,
                              open:function () {
                                  $scope.tipvalue = '';
                                  $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                                          setTimeout(function () {
                                              $("#receiving-inputer").focus();
                                          },500);
                                      }
                                  };
                                  receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                              }else{
                                  isOld = true;
                                  scan_product_content_TO_INVESTIGATE = true;
                                  checkContainerIsScan();
                                  if(podid===null||podid===undefined||podid===''){
                                      $scope.receivingscan();
                                  }
                                  setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                              }
                          },function (data) {
                              var options = {
                                  title:INBOUND_CONSTANT.RESCANINVEST,
                                  width:600,
                                  height:400,
                                  open:function () {
                                      $scope.tipvalue = '';
                                      $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                             // showGeneralWindow("提示","Pod尚未就绪,请稍后操作／刷新Pod获取最新Pod信息");
                              if(inputvalue!==''||inputvalue!==null||inputvalue==undefined){
                                  getPodInfo(inputvalue);
                              }
                          } else {
                              if (isMeasured || isDamaged || isInvest) {
                                  checkContainerIsScan();
                              }
                              receivingService.scanIsDN(inputvalue, function (data) {
                                  $scope.scanDn = data;
                                  if ($scope.scanDn) {
                                      scan_DN = false;
                                      scan_product_info = false;
                                      scan_bin = false;
                                      //是否测量
                                      isMeasured = false;
                                      isInvest = false;
                                      isDamaged = false;
                                      isSingle = false;
                                      receiveType = INBOUND_CONSTANT.GENUINE;
                                      isLot = false;
                                      isSn = false;
                                      adviceNo = "";
                                      storageid = "";
                                      amount = "";
                                      itemid = "";
                                      sn = "";
                                      useAfter = "";
                                      $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                                      $("#" + upid).children("span").text("");
                                      upid = '';
                                      item = null;
                                      $scope.scanDn = false;
                                      $scope.scanmeasurecib = '1';
                                      $("#receiving_status_span").html("");
                                      $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                                      console.log("ScanningDN...");
                                      receivingService.scanDN(inputvalue, function (data) {
                                          scan_DN = true;
                                          adviceNo = data.cls.request.adviceNo;
                                          $("#receiving_dn_span").html(adviceNo);
                                          $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                          $scope.product_info_con = '1';
                                          $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                          $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                          $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                          checkContainerIsScan();
                                      }, function (data) {
                                          $("#receiving_tip").html(INBOUND_CONSTANT.RESCANDN);
                                          var options = {
                                              width: 600,
                                              height: 400,
                                              title: INBOUND_CONSTANT.RESCANDN,
                                              open: function () {
                                                  $scope.tipvalue = '';
                                                  $("#newtipwindow_span").html("DN号码:" + inputvalue + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
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
                                          checkContainerIsScan();
                                      });
                                  }else{
                                      if(!scan_DN){
                                          receivingService.scanDN(inputvalue, function (data) {
                                              scan_DN = true;
                                              adviceNo = data.cls.request.adviceNo;
                                              $("#receiving_dn_span").html(adviceNo);
                                              $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                              $scope.product_info_con = '1';
                                              $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                              $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                              $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                              checkContainerIsScan();
                                          }, function (data) {
                                              $("#receiving_tip").html(INBOUND_CONSTANT.RESCANDN);
                                              var options = {
                                                  width: 600,
                                                  height: 400,
                                                  title: INBOUND_CONSTANT.RESCANDN,
                                                    open: function () {
                                                      $scope.tipvalue = '';
                                                      $("#newtipwindow_span").html("DN号码:" + inputvalue + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
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
                                              checkContainerIsScan();
                                          });
                                      }else if (!scan_product_info) {
                                          $("#receiving_status_span").html("");
                                          $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                          $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                                          $("#" + upid).children("span").text("");
                                          itemid = inputvalue;
                                          receiving_commonService.backPodStorageColor(storages);
                                          receivingService.scanItem(adviceNo, itemid, $rootScope.stationName,podid, function (data) {
                                              console.log("scanItem---->", data);
                                              if (isMeasured) {
                                                  $scope.scanwaitcib = '1';
                                                  $scope.scanbadcib = '1';
                                                  $scope.scanmeasurecib = '1';
                                              }
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
                                              item = data.cls.itemData;
                                              avaTimeType = data.cls.itemData.lotType;
                                              TimeType = data.cls.itemData.itemDataGlobal.lotUnit;
                                              dnAmount = data.cls.dnAmount;
                                              receiveAmount = data.cls.receiveAmount;
                                              maxAmount = data.cls.maxReceiveAmount;
                                              canReceiveAmount = data.cls.canReceiveAmount;
                                              console.log("dnAmount-->"+dnAmount);
                                              console.log("receiveAmount-->"+receiveAmount);
                                              console.log("maxAmount-->"+maxAmount);
                                              console.log("canReceiveAmount-->"+canReceiveAmount);
                                              if (data.status === '1' && !isDamaged) {//测量商品
                                                  console.log("测量商品");
                                                  isMeasured = true;
                                                  isSingle = true;
                                                  $("#scanmeasurecib").html(INBOUND_CONSTANT.SCANMEASURE);
                                                  finishType = INBOUND_CONSTANT.SINGLE;
                                                  receiveType = INBOUND_CONSTANT.MEASURED;
                                                  cleanStatus();
                                                  checkContainerIsScan();
                                                  checkType(data.cls.itemData);
                                              }else{
                                                  //非有效期商品并且无推荐货位
                                                  if(!item.lotMandatory&&(data.cls.introStorages===undefined||data.cls.introStorages===null||data.cls.introStorages===''||data.cls.introStorages.length==0)){
                                                      showNoIntroStorage();
                                                  }else{
                                                      cleanStatus();
                                                      checkContainerIsScan();
                                                      if(!item.lotMandatory&& !isDamaged){
                                                          introStorages = data.cls.introStorages;
                                                          receiving_commonService.colorIntroStorage(introStorages,storages);
                                                      }
                                                      checkType(data.cls.itemData);
                                                  }
                                              }
                                          }, function (data) {
                                              cleanStatus();
                                              checkContainerIsScan();
                                              $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                              var options = {
                                                  title: INBOUND_CONSTANT.RESCANITEM,
                                                  width: 600,
                                                  height: 400,
                                                  open: function () {
                                                      $scope.tipvalue = '';
                                                      $("#newtipwindow_span").html("商品条码:" + inputvalue + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
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
                                          if (!isInvest && isSn && (sn == null || sn === '' || sn === undefined)) {
                                              popSnWindow();
                                              return;
                                          }
                                          if (isLot && (useAfter === null || useAfter === '' || useAfter === undefined)) {
                                              popLotWindow();
                                              return;
                                          }
                                          storageid = inputvalue;
                                          if (isMeasured || isInvest || isDamaged) {
                                              console.log("测量检查货筐");
                                              receivingService.checkNotGenuisContainer(storageid, $rootScope.stationName, receiveType, useAfter, itemid, function (data) {
                                                  console.log("测量开始收货");
                                                  finishGoods();
                                              }, function (data) {//扫描非正品货筐错误
                                                  if (data.key === '-1' || data.key === '-2') {
                                                      showStorageFull(data.values[0]);
                                                  } else {
                                                      var options = {
                                                          title: receiveType === INBOUND_CONSTANT.DAMAGED ? INBOUND_CONSTANT.RESCANDAMAGED : receiveType === INBOUND_CONSTANT.MEASURED ? INBOUND_CONSTANT.RESCANMEASURED : INBOUND_CONSTANT.RESCANINVEST,
                                                          width: 600,
                                                          height: 400,
                                                          open: function () {
                                                              $scope.tipvalue = '';
                                                              $("#newtipwindow_span").html("条码:" + inputvalue + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
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
                                                  }
                                              });
                                          } else {
                                              if ($scope.isNotIntro) {
                                                  showNoIntroStorage();
                                                  return;
                                              }
                                              receivingService.checkBin(storageid, itemid, useAfter, podid, $rootScope.stationName, function (data) {
                                                  scan_bin = true;
                                                  receiving_commonService.backPodStorageColor(storages);
                                                  finishGoods();
                                              }, function (data) {
                                                  storageid = "";
                                                  if (data.key === '-5') {
                                                      var options = {
                                                          width: 600,
                                                          height: 400,
                                                          title: INBOUND_CONSTANT.SCANLOCATIONORDN,
                                                          open: function () {
                                                              $scope.tipvalue = '';
                                                              $("#newtipwindow_span").html("<h3>" + data.message.key + "</h3><br>" + data.message.message);
                                                              setTimeout(function () {
                                                                  $("#newtipwindow_inputer").focus();
                                                              }, 500);
                                                          },
                                                          close: function () {
                                                              $("#receiving-inputer").focus();
                                                          }
                                                      };
                                                      receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                                  } else {
                                                      if (data.key === '-2') {
                                                          var options = {
                                                              width: 600,
                                                              height: 400,
                                                              title: INBOUND_CONSTANT.RESCANLOCATION,
                                                              open: function () {
                                                                  $scope.tipvalue = '';
                                                                  $("#newtipwindow_span").html(data.values[0]);
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
                                                      } else {
                                                          var options = {
                                                              width: 600,
                                                              height: 400,
                                                              title: INBOUND_CONSTANT.RESCANLOCATION,
                                                              open: function () {
                                                                  $scope.tipvalue = '';
                                                                  if (data.key.indexOf("%") != -1) {
                                                                      $("#newtipwindow_span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 43%;'>" + data.values[0] + "</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>" + data.key.substr(data.key.indexOf("&") + 1, data.key.length - 1) + "</p>");
                                                                  } else {
                                                                      $("#newtipwindow_span").html(data.values[0] || "" + "</br>" + data.key);
                                                                  }
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
                                                      }
                                                      receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
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
                                  isInvest = false;
                                  isDamaged = false;
                                  isSingle = false;
                                  receiveType = INBOUND_CONSTANT.GENUINE;
                                  isLot = false;
                                  isSn = false;
                                  adviceNo = "";
                                  storageid = "";
                                  amount = "";
                                  itemid = "";
                                  sn = "";
                                  useAfter = "";
                                  $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                                  $("#" + upid).children("span").text("");
                                  upid = '';
                                  item = null;
                                  $scope.scanmeasurecib = '1';
                                  $("#receiving_status_span").html("");
                                  $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                              }
                              if (!scan_DN) {
                                  console.log("ScanningDN...");
                                  receivingService.scanDN(inputvalue, function (data) {
                                      scan_DN = true;
                                      adviceNo = data.cls.request.adviceNo;
                                      $("#receiving_dn_span").html(adviceNo);
                                      $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                      $scope.product_info_con = '1';
                                      $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                      $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                      $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                      checkContainerIsScan();
                                  }, function (data) {
                                      $("#receiving_tip").html(INBOUND_CONSTANT.RESCANDN);
                                      var options = {
                                          width: 600,
                                          height: 400,
                                          title: INBOUND_CONSTANT.RESCANDN,
                                          open: function () {
                                              $scope.tipvalue = '';
                                              $("#newtipwindow_span").html("DN号码:" + inputvalue + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
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
                                      checkContainerIsScan();
                                  });
                              } else {
                                  if (!scan_product_info) {
                                      $("#receiving_status_span").html("");
                                      $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                      $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                                      $("#" + upid).children("span").text("");
                                      itemid = inputvalue;
                                      receiving_commonService.backPodStorageColor(storages);
                                      receivingService.scanItem(adviceNo, itemid, $rootScope.stationName,podid, function (data) {
                                          console.log("scanItem---->", data);
                                          if (isMeasured) {
                                              $scope.scanwaitcib = '1';
                                              $scope.scanbadcib = '1';
                                              $scope.scanmeasurecib = '1';
                                          }
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
                                          item = data.cls.itemData;
                                          avaTimeType = data.cls.itemData.lotType;
                                          TimeType = data.cls.itemData.itemDataGlobal.lotUnit;
                                          dnAmount = data.cls.dnAmount;
                                          receiveAmount = data.cls.receiveAmount;
                                          maxAmount = data.cls.maxReceiveAmount;
                                          canReceiveAmount = data.cls.canReceiveAmount;
                                          console.log("dnAmount-->"+dnAmount);
                                          console.log("receiveAmount-->"+receiveAmount);
                                          console.log("maxAmount-->"+maxAmount);
                                          console.log("canReceiveAmount-->"+canReceiveAmount);
                                          if (data.status === '1' && !isDamaged) {//测量商品
                                              console.log("测量商品");
                                              isMeasured = true;
                                              isSingle = true;
                                              $("#scanmeasurecib").html(INBOUND_CONSTANT.SCANMEASURE);
                                              finishType = INBOUND_CONSTANT.SINGLE;
                                              receiveType = INBOUND_CONSTANT.MEASURED;
                                              cleanStatus();
                                              checkContainerIsScan();
                                              checkType(data.cls.itemData);
                                          }else{
                                              //非有效期商品并且无推荐货位
                                              if(!item.lotMandatory&&(data.cls.introStorages===undefined||data.cls.introStorages===null||data.cls.introStorages===''||data.cls.introStorages.length==0)){
                                                  showNoIntroStorage();
                                              }else{
                                                  cleanStatus();
                                                  checkContainerIsScan();
                                                  if(!item.lotMandatory&& !isDamaged){
                                                      introStorages = data.cls.introStorages;
                                                      receiving_commonService.colorIntroStorage(introStorages,storages);
                                                  }
                                                  checkType(data.cls.itemData);
                                              }
                                          }
                                      }, function (data) {
                                          cleanStatus();
                                          checkContainerIsScan();
                                          $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                          var options = {
                                              title: INBOUND_CONSTANT.RESCANITEM,
                                              width: 600,
                                              height: 400,
                                              open: function () {
                                                  $scope.tipvalue = '';
                                                  $("#newtipwindow_span").html("商品条码:" + inputvalue + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
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
                                      if (!isInvest && isSn && (sn == null || sn === '' || sn === undefined)) {
                                          popSnWindow();
                                          return;
                                      }
                                      if (isLot && (useAfter === null || useAfter === '' || useAfter === undefined)) {
                                          popLotWindow();
                                          return;
                                      }
                                      storageid = inputvalue;
                                      if (isMeasured || isInvest || isDamaged) {
                                          console.log("测量检查货筐");
                                          receivingService.checkNotGenuisContainer(storageid, $rootScope.stationName, receiveType, useAfter, itemid, function (data) {
                                              console.log("测量开始收货");
                                              finishGoods();
                                          }, function (data) {//扫描非正品货筐错误
                                              if (data.key === '-1' || data.key === '-2') {
                                                  showStorageFull(data.values[0]);
                                              } else {
                                                  var options = {
                                                      title: receiveType === INBOUND_CONSTANT.DAMAGED ? INBOUND_CONSTANT.RESCANDAMAGED : receiveType === INBOUND_CONSTANT.MEASURED ? INBOUND_CONSTANT.RESCANMEASURED : INBOUND_CONSTANT.RESCANINVEST,
                                                      width: 600,
                                                      height: 400,
                                                      open: function () {
                                                          $scope.tipvalue = '';
                                                          $("#newtipwindow_span").html("条码:" + inputvalue + data.message.replace("[", "").replace("]", "").replace("Unknown Error", ""));
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
                                              }
                                          });
                                      } else {
                                          if($scope.isNotIntro){
                                              showNoIntroStorage();
                                              return;
                                          }
                                          receivingService.checkBin(storageid, itemid, useAfter, podid, $rootScope.stationName, function (data) {
                                              scan_bin = true;
                                              receiving_commonService.backPodStorageColor(storages);
                                              finishGoods();
                                          }, function (data) {
                                              storageid = "";
                                              if (data.key === '-5') {
                                                  var options = {
                                                      width: 600,
                                                      height: 400,
                                                      title: INBOUND_CONSTANT.SCANLOCATIONORDN,
                                                      open: function () {
                                                          $scope.tipvalue = '';
                                                          $("#newtipwindow_span").html("<h3>" + data.message.key + "</h3><br>" + data.message.message);
                                                          setTimeout(function () {
                                                              $("#newtipwindow_inputer").focus();
                                                          }, 500);
                                                      },
                                                      close: function () {
                                                          $("#receiving-inputer").focus();
                                                      }
                                                  };
                                                  receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                              } else {
                                                  if (data.key === '-2') {
                                                      var options = {
                                                          width: 600,
                                                          height: 400,
                                                          title: INBOUND_CONSTANT.RESCANLOCATION,
                                                          open: function () {
                                                              $scope.tipvalue = '';
                                                              $("#newtipwindow_span").html(data.values[0]);
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
                                                  } else {
                                                      var options = {
                                                          width: 600,
                                                          height: 400,
                                                          title: INBOUND_CONSTANT.RESCANLOCATION,
                                                          open: function () {
                                                              $scope.tipvalue = '';
                                                              if (data.key.indexOf("%") != -1) {
                                                                  $("#newtipwindow_span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 43%;'>" + data.values[0] + "</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>" + data.key.substr(data.key.indexOf("&") + 1, data.key.length - 1) + "</p>");
                                                              } else {
                                                                  $("#newtipwindow_span").html(data.values[0] || "" + "</br>" + data.key);
                                                              }
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
                                                  }
                                                  receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
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
      //显示无推荐货位弹窗
      function showNoIntroStorage() {
          receiving_commonService.receiving_tip_dialog("releasePodWindow",{
              title:"无推荐货位",
              width:600,
              height:500,
              open:function () {
                  $scope.sureDnTip = INBOUND_CONSTANT.NOINTROSTORAGES;
                  $scope.sureDn = '';
                  setTimeout(function () {
                     $("#sureDn").focus();
                  });
              },
              close:function () {
                  focusOnReceiveInputer();
              }
          });
      }

      function getPodInfo(podInfo) {
          receiving_commonService.CloseWindowByBtn("reloadPod_Window");
          receivingService.getPodInfo(podInfo, INBOUND_CONSTANT.BIN, function (datas) {
              if (Number(datas.status) < 0 || datas.cls.totalRow === 0) {//pod信息不合法
                  $scope.podShow ='1';
              }
              else {
                  console.log("pod--->",datas);
                  $scope.podShow ='0';
                  scan_pod = true;
                  podid = podInfo;
                  $scope.Pod = podInfo;
                  podName = datas.cls.pod.name;
                  checkContainerIsScan();
                  storages = datas.cls.storageLocations;

                  $("#receiving_pod_layout").html("");

                 // $("#receiving-releasepod").css({"backgroundColor": "#3f51b5","borderColor":"#3f51b5"});
                  //$("#receiving-releasepod").focus(function(){$(this).css({"backgroundColor": "#3f51b5","borderColor":"#3f51b5"})};
                  //层级货位类型
                  var levelTypes = [];
                  var totalHeight = 0;
                  for(var key = datas.cls.columnMap.length-1;key>=0;key--){
                      var count = storages.length-1;
                      if(key<datas.cls.columnMap.length-1){
                          for(var l=key+1;l<datas.cls.columnMap.length;l++){
                              count -=datas.cls.columnMap[l];
                          }
                      }
                      totalHeight+=storages[count].storageLocationType.height;
                  }
                  for(var key = datas.cls.columnMap.length-1;key>=0;key--){
                      var count = storages.length-1;
                      if(key<datas.cls.columnMap.length-1){
                          for(var l=key+1;l<datas.cls.columnMap.length;l++){
                              count -=datas.cls.columnMap[l];
                          }
                      }
                      levelTypes.push(parseFloat(storages[count].storageLocationType.height/totalHeight));
                  }
                  receiving_commonService.fillGrid(document.getElementById("receiving_pod_layout"), datas.cls.totalRow, "receiving_pod_layout", "receiving_pod_layout_item", datas.cls.columnMap,levelTypes);
                  setTimeout(function () {
                      //重新查找推荐pod
                      console.log("重新查找推荐pod");
                      if(itemid!==undefined&&itemid!==''&&itemid!==null){
                          if(item.lotMandatory!==null&&item.lotMandatory!==undefined&&item.lotMandatory){
                              if(!isLot){
                                  receivingService.checkAvaTime(itemid,useAfter,podid,function (data) {
                                      if(data.cls===undefined||data.cls===null||data.cls===''||data.cls.length===0){
                                          $scope.isNotIntro = true;
                                          showNoIntroStorage();
                                      }else{
                                          $scope.isNotIntro = false;
                                          introStorages = data.cls;
                                          receiving_commonService.colorIntroStorage(introStorages,storages);
                                          focusOnReceiveInputer();
                                      }
                                  });
                              }
                          }else{
                              receivingService.scanItem(adviceNo,itemid,$rootScope.stationName,podid,function (data) {
                                    if(data.cls.introStorages===undefined||data.cls.introStorages===null||data.cls.introStorages===''||data.cls.introStorages.length===0){
                                        $scope.isNotIntro = true;
                                        showNoIntroStorage();
                                    }else{
                                        $scope.isNotIntro = false;
                                        introStorages = data.cls.introStorages;
                                        receiving_commonService.colorIntroStorage(introStorages,storages);
                                        focusOnReceiveInputer();
                                    }
                              });
                          }
                      }
                      $("#receiving-inputer").focus();
                  }, 100);
              }
          },function () {
              $scope.podShow ='1';
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
                  receivingService.getPodInPath($rootScope.workStationId,$rootScope.sectionId,function (data) {
                      if(data.status==='0') {
                          $scope.podTotal = data.cls.length;
                      }
                      else {
                          $scope.podTotal = 0;
                      }
                  });
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
      };
      //停止呼叫pod
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
      //显示货筐已满窗口
      function showStorageFull(msg) {
            var options = {
                title: INBOUND_CONSTANT.EXCHANGESTORAGE,
                open: function () {
                    $("#ok_tipwindow_span").html(msg+","+INBOUND_CONSTANT.CLICKSTORAGEFULL);
                }
            };
            receiving_commonService.receiving_tip_dialog("ok_tipwindow", options);
      }
      //显示更换货筐窗口
      $scope.showChangeStoage = function (data) {
          console.log("single.changeState-->"+data);
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
                        console.log("enter normalFull close function");
                        normalFull = false;
                        isOld = true;
                        focusOnReceiveInputer();
                        return;
                    }
                    if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE){
                        console.log("enter innormalFull close function");
                        isOld = true;
                        checkContainerIsScan();
                    }
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("scanstoragewindow", options);
        };
      //站台货筐信息
      function gridStorageInfo() {
          $scope.singleReceivedGridOptions = {height: 200,columns: columns,scrollable:false};
          receivingService.findgridStorageInfo($rootScope.stationName,$rootScope.currentReceive,function (data) {
              console.log("data-->",data);
              $rootScope.receiveProcessDTOList = data.cls.receiveProcessDTOList;
                  $scope.scanstatus='0';
                  $("#singleReceivedGRID").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: data.cls.receiveProcessDTOList}));
          },function (data) {
              $scope.scanstatus = '1';
              $("#warnStation").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
          });
      }
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
      }
      //切换收货模式
      $scope.switchMode = function (modeData) {
          isAll = modeData;
          if(modeData){//不是单件
              finishType = INBOUND_CONSTANT.ALL;
            $("#receiving-singlemode").css({"backgroundColor":"#6E6E6E"});
            $("#receiving-allmode").css({"backgroundColor":"#3f51b5"});
          }else{//是单件
              finishType = INBOUND_CONSTANT.SINGLE;
            $("#receiving-allmode").css({"backgroundColor":"#6E6E6E"});
            $("#receiving-singlemode").css({"backgroundColor":"#3f51b5"});
          }
          focusOnReceiveInputer();
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
          } /*else {*/
              if (isLot) {
                  popLotWindow();
              }
          /*}*/
      }
      //单件收货
      function SingleFinishGoods() {
            if(maxAmount>0){
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
                    toolName:INBOUND_CONSTANT.EACHRECEIVETOSTOW
                },function () {
                    if(isMeasured||isDamaged||isInvest){
                        console.log("特殊商品");
                        console.log("isMeasured-->"+isMeasured);
                        if(isMeasured){
                            $("#scanmeasurecib").html("1");
                            $("#scanmeasurecib").css({"backgroundColor":"#008B00"});
                            isMeasured = false;
                            $("#receiving_status_span").css({"backgroundColor":"#FFC125"});
                        }
                        if(isInvest){
                            $("#scanwaitcib").html("1");
                            $("#scanwaitcib").css({"backgroundColor":"#008B00"});
                            isInvest = false;
                            $("#receiving_status_span").css({"backgroundColor":"#00BFFF"});
                        }
                        if(isDamaged){
                            $("#scanbadcib").html("1");
                            $("#scanbadcib").css({"backgroundColor":"#008B00"});
                            isDamaged = false;
                            $("#receiving_status_span").css({"backgroundColor":"#FF0000"});
                        }
                        // $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                    }else{
                        upid = receiving_commonService.findStorageLocation(storageid,storages);
                        $("#"+upid).css({"backgroundColor":"#008B00"});

                        $("#"+upid).children("span").text(1);
                        $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                    }
                    reSetAllVar();
                    $("#receiving_status_span").html("已成功收货上架1件商品至</br>"+storageid);
                    receiving_commonService.closePopWindow("keyboard_window");
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                    focusOnReceiveInputer();
                },function (data) {
                    if(isDamaged||isInvest||isMeasured){
                        if (data.key === '-1' || data.key === '-2') {
                            showStorageFull(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        } else {
                            showGeneralWindow("扫描货筐错误", data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                        }
                    }else{
                        showGeneralWindow(INBOUND_CONSTANT.RESCANLOCATION, data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                    }
                });
            }else{
                showGeneralWindow("请确认是否要进行多货收货","商品数量超出当前用户可收商品数量最大值,请重新扫描商品进行收货");
                scan_product_info = false;
                $scope.scanmeasurecib = '1';
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
            }
        }
      $scope.scanDnSureItem = function (e) {
          if(!receiving_commonService.autoAddEvent(e)) return;
          receivingService.checkDNProblem(itemid,$scope.sureDn,function () {
              receiving_commonService.CloseWindowByBtn('releasePodWindow');
              itemid = '';
              introStorages = [];
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
      //重置相关状态位
      function reSetAllVar() {
          isSingle = false;
          isSn = false;
          isLot = false;
          isDamaged = false;
          isMeasured = false;
          isInvest = false;
          receiveType = INBOUND_CONSTANT.GENUINE;
          if(isAll){
              finishType = INBOUND_CONSTANT.ALL;
          }else{
              finishType = INBOUND_CONSTANT.SINGLE;
          }
          scan_product_info = false;
          avaTimeType = null;
          TimeType = null;
          dnAmount = null;
          receiveAmount = null;
          maxAmount = null;
          item = null;
          itemid=null;
          sn = null;
          $scope.sn = '';
          $scope.isSureGoodsMore = false;
        }
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
                  if(avaTimeType===INBOUND_CONSTANT.MANUFACTURE){/*按有效期计算*/
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
                  setTimeout(function () {
                      $("#receiving-inputer").focus();
                  },500);
              }
          };
          receiving_commonService.receiving_tip_dialog("avatime_pop_window",options);
      }
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
      //有效期异常确定
      $scope.avaTimeInNormalOk = function () {
          $scope.avatime_normal = '0';
          receiving_commonService.CloseWindowByBtn("avatime_pop_window");
          scan_product_info = false;
          checkContainerIsScan();
      };
      //有效期异常取消
      $scope.avaTimeInNormalCancle = function () {
          $scope.avatime_normal = '0';
          $("#avatime_pop_window_madeyear").val('');
          $("#avatime_pop_window_mademonth").val('');
          $("#avatime_pop_window_madeday").val('');
          $("#avatime_pop_window_avatime").val('');
          setTimeout(function () {
              $('#avatime_pop_window_madeyear').focus();
          },50);
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

                  $("#inputwindow_span").html(item.name);
                  setTimeout(function () {
                      $("#window-receiving-inputer").focus();
                  },500);
              },
              close:function () {
                  checkContainerIsScan();
              }
          };
          receiving_commonService.receiving_tip_dialog("scanwindow", options);
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
          $("#scanwaitcib").html('');
          $("#scanwaitcib").css({"backgroundColor":"#00BFFF"});
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
      function finishGoods() {
            if(isSingle){
                //检查用户收货数量是否符合
                console.log("单件开始收货");
                console.log("maxAmount--->"+maxAmount);
                SingleFinishGoods();
            }else{
                if(isAll||isDamaged){
                    receiving_commonService.receiving_tip_dialog_normal("keyboard_window",{
                        width:600,
                        title:"请输入收货数量",
                        open:function () {
                            $("#keyboard_inputer").val("");
                            receiving_commonService.keyboard_fillGrid($("#keyboard_keys"),2,5,"keyboard","keyboard_layout_item");
                            setTimeout(function () {
                                $("#keyboard_inputer").focus();
                            },500);
                        },
                        close:function () {
                            $("#keyboard_inputer").value = "";
                            setTimeout(function(){ $("#receiving-inputer").focus();}, 500);
                        }
                    });
                }else{
                    console.log("单件开始收货");
                    SingleFinishGoods();
                }
            }
        }
      //确认使用当前货筐
      $scope.win_receivingok = function (type) {
          receivingService.bindStorageLocation(storageid,type,$rootScope.stationName,destinationId,positionIndex,function (data) {
              destinationId = null;
              positionIndex = null;
              isOld = true;
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
                  finishGoods();
              }
              checkContainerIsScan();
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
      //显示问题菜单弹窗
      $scope.showProMenuWindow = function () {
          var options = {
              title:INBOUND_CONSTANT.SELECTPMENU,
              width:800,
              height:600,
              close:function () {
                  setTimeout(function(){ $("#receiving-inputer").focus();}, 500);
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("promenu_pop_window",options);
      };
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
                     height:350,
                     open:function () {
                         $scope.exitflag = '0';
                         $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_EACH;
                     },
                     close:function () {
                         $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_EACH;
                         focusOnReceiveInputer();
                     }
                 };
                 receiving_commonService.receiving_tip_dialog_normal("window_general_ok_cancel",options);
             }
          });
      };
      //改变退出工作站提示
      $scope.exitStationBefore = function () {
          $scope.exitflag = '1';
          $scope.exitStationContent = INBOUND_CONSTANT.SUREEXITANDFULL;
      };
      //结束收货确定(结满所有筐)
      $scope.exitStation = function(){
        receivingService.exitReceiveStation($rootScope.stationName,"YES",function(){
                //关闭webSocket连接
            /*sd update start*/
                //$scope.webSocketBuilder.close(true);
                //podSocket.close();
                closeWebsocket();
            /*sd update end*/
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
      };
      //退出工作站不满筐
      $scope.exitNotFull = function(){
            receivingService.exitReceiveStation($rootScope.stationName,"NO",function(){
                    //关闭webSocket连接
                /*sd update start*/
                  //  $scope.webSocketBuilder.close(true);
                    //podSocket.close();
                    closeWebsocket();
                /*sd update end*/
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
        };
      //结束收获取消
      $scope.closeGeneralWindow = function(){
        var window = $("#window_general_ok_cancel").data("kendoWindow");
        window.close();
      };
      //有效期输入框焦点函数
      $scope.avatimemethod = function (currentid) {
          currentId = currentid;
          receiving_commonService.getavatimeid(currentid);

      };
      //点击报告暗灯
      var reportTypeId = '';
      var side = '';
      $scope.clickReportLight = function () {
          if(podid===null||podid===undefined||podid===''){
              showGeneralWindow("请先扫描Pod","请先扫描Pod");
              return;
          }
          receiving_commonService.CloseWindowByBtn("promenu_pop_window");
          var options = {
              title:"选择暗灯菜单",
              width:1000,
              height:650,
              open:function () {
                  $scope.badStorageState = '1';
                  $("#badStorageState").css({"display":"none"});
                  $scope.scanbadstorageworn = '';
                  //获取暗灯菜单
                  receivingService.getReportLight(function (data) {
                      // var typearraylong = getObjCount(typearrays);
                      for(var l=0;l<data.length;l++){
                          if(data[l]!=undefined&&(data[l].name==="扫描枪存在问题"||
                              data[l].name==="商品需要录入有效期")){
                              receiving_commonService.removeByValue(data,data[l]);
                          }
                          if(data[l].name==="商品丢失"){
                              receiving_commonService.removeByValue(data,data[l]);
                          }
                      }
                      receiving_commonService.grid_ReportMenu(data,2,function (typeid) {
                          //获取点击div的id
                          var typeName = $("#"+typeid).children("span").html();
                          reportTypeId = typeid;
                          if(typeName===INBOUND_CONSTANT.storagecantscan){
                              receiving_commonService.CloseWindowByBtn("report_light_pop_window");
                              $scope.scansideinputer = '';
                                showScanStorageSide();
                          }else{
                              // showBadStorage();
                              $scope.badStorageState = '0';
                              $("#badStorageState").css({"display":"block"});
                              setTimeout(function () {
                                  $("#scan-badstorage-inputer").focus();
                              },500);
                          }
                      });
                  });
              },
              close:function () {
                 // $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_EACH;
                  $("#scan-badstorage-inputer").val("");
                  focusOnReceiveInputer();
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("report_light_pop_window",options);
      };
      //显示货位无法扫描窗口
      function showScanStorageSide() {
            var options = {
                title: INBOUND_CONSTANT.SELECTSCANLOCATION,
                width: 800,
                height: 600,
                open: function () {
                    $("#scan_side_window_grid").html("");
                    $scope.scansideinputer = '';
                    $("#scan_side_inputer_div").css({"display": "none"});
                    receiving_commonService.grid_SideStorage([
                        {
                            "id": "up",
                            "name": INBOUND_CONSTANT.scanup
                        },
                        {
                            "id": "down",
                            "name": INBOUND_CONSTANT.scandown
                        },
                        {
                            "id": "left",
                            "name": INBOUND_CONSTANT.scanleft
                        },
                        {
                            "id": "right",
                            "name": INBOUND_CONSTANT.scanright
                        }], 2, function (sideid) {
                        $("#scan_side_inputer_div").css({"display": "block"});
                        $("#scan_side_window_wnd_title").html(INBOUND_CONSTANT.SCANSTORAGE);
                        side = sideid;
                        setTimeout(function () {
                            $("#scan-side-inputer").focus();
                        },500);
                    });
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("scan_side_window", options);
      }
      //扫描周边货位方法
      $scope.sideStorageScan = function (e) {
          if(!receiving_commonService.autoAddEvent(e)){
              return;
          }
          var flag = receiving_pod_layout;
          if($scope.scansideinputer===''||$scope.scansideinputer===undefined){
              $scope.scansideworn = INBOUND_CONSTANT.storagecantscan;
              return;
          }
          var storage = receiving_commonService.findSideStorageLocation($scope.scansideinputer,storages,side);
          console.log("storage--->"+storage);
          if(storage===null||
              storage===undefined){
              $scope.scansideworn = "货位号码:"+$scope.scansideinputer+"无效";
              return;
          }
          $scope.scansideworn = '';
          receivingService.createReportLight({
              "storageLocationId":storage,
              "problemName":$("#"+reportTypeId).children("span").html().replace($("#"+reportTypeId).children("span").html().substring(0,1),"").replace(".",""),
              "anDonMasterTypeId":reportTypeId,
              "state":"undisposed",
              "reportBy":$window.localStorage["username"],
              "clientId":$window.localStorage["clientId"],
              "warehouseId":$window.localStorage["warehouseId"]
          },function () {
              receiving_commonService.CloseWindowByBtn("report_light_pop_window");
              receiving_commonService.CloseWindowByBtn('scan_side_window');
              setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
          });
      };
      //显示问题货位方法
      function showBadStorage() {
          var options = {
              title:"请扫描货位条码",
              width:800,
              height:600,
              close:function () {
                  $scope.scanbadstorageinputer = '';
              }
          };
          receiving_commonService.receiving_tip_dialog_normal("scan_badstorage_window",options);
      }
      //扫描问题货位方法
      $scope.scanBadStorage = function (e) {
          if(!receiving_commonService.autoAddEvent(e)){
              return;
          }
          //提交暗灯
          if($scope.scanbadstorageinputer===''||$scope.scanbadstorageinputer===undefined){
                $scope.scanbadstorageworn = INBOUND_CONSTANT.storagecantscan;
                return;
          }
          receivingService.getstoragelocationId($scope.scanbadstorageinputer,function (data) {
              receivingService.createReportLight({
                  "storageLocationId":data.id,
                  "problemName":$("#"+reportTypeId).children("span").html().replace($("#"+reportTypeId).children("span").html().substring(0,1),"").replace(".",""),
                  "anDonMasterTypeId":reportTypeId,
                  "state":"undisposed",
                  "reportBy":$window.localStorage["username"],
                  "clientId":$window.localStorage["clientId"],
                  "warehouseId":$window.localStorage["warehouseId"]
              },function (data) {
                  // receiving_commonService.CloseWindowByBtn('scan_badstorage_window');
                  receiving_commonService.CloseWindowByBtn("report_light_pop_window");
              },function (data) {
                  $scope.scanbadstorageworn = $scope.scanbadstorageinputer+":"+data.key||data.message.replace("[","").replace("]","").replace("Unknown Error","");
                  $scope.scanbadstorageinputer = '';
                  setTimeout(function () {
                      $("#scan-badstorage-inputer").focus();
                  },500);
              });
          },function (data) {
              $scope.scanbadstorageworn = $scope.scanbadstorageinputer+":"+data.key||data.message.replace("[","").replace("]","").replace("Unknown Error","");
              $scope.scanbadstorageinputer = '';
              setTimeout(function () {
                  $("#scan-badstorage-inputer").focus();
              },500);
          });
      };
      $scope.delete_avavalue = function () {
          if(currentId===undefined||currentId===null){
              return;
          }
          $("#"+currentId).val("");
      };
      $scope.close = function(id){
            receivingService.getNewestReceiveAmount(adviceNo,itemid,function (data) {
                console.log("最新可收最大数量-->"+data);
                maxAmount = parseInt(data);
                receiving_commonService.CloseWindowByBtn(id);
            },function (data) {
                showGeneralWindow("提示","获取最新数量失败");
            });
        };
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
      $scope.startPodInPage = function () {
          receiving_commonService.getLocationTypesInPage(function (data) {
              receivingService.bindStorageLocationTypes({
                  "locationTypeDTOS":data,
                  "stationid":$rootScope.stationId
              },function (callBackData) {
                  receiving_commonService.CloseWindowByBtn("showBinType_window");
                  setTimeout(function(){ $("#receive-inputer").focus();}, 200);
              },function (data) {
                  showGeneralWindow("绑定货位类型失败","绑定货位类型失败");
              });
          });
      };
      //初始化
      $scope.receivingCurrent = $stateParams.id; // 当前收货模式

      setTimeout(function(){ $("#receiving_station").focus();}, 200); // 首获焦
    function getPositionNo(positions, id){
      var positionNo = null;
      for(var i = 0; i < positions.length; i++){
        var position = positions[i];
        if(position.receivingDestination.id === id){
          positionNo = position.positionNo;
          break;
        }
      }
      return positionNo;
    }
    // 初始化变量
    function init(){
      $scope.receivingDestination = null; // 目的地初始状态
      $scope.measuredStatus = "init"; // 测量初始状态
      $scope.goodsStatus = "init"; // 商品初始状态
      $scope.number = ""; // 收货数量
      $scope.containerName = ""; // 绑定车
      $scope.useNotAfter = ""; // 初始有效期
      $scope.year = ""; $scope.month = ""; $scope.day = ""; $scope.months = "";
      $scope.expiredYear = ""; $scope.expiredMonth = ""; $scope.expiredDay = "";
      $scope.measureContainer = ""; // 测量车
      $scope.goodsNumber = ""; $scope.damageContainer = ""; // 残品车
      $scope.oldContainer = ""; $scope.newContainer = ""; // 货筐已满
      $scope.serialNo = ""; // 初始序列号
    }
    //商品残损
    $scope.goodsDamage = function(){
        if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE){
            showGeneralWindow("请先绑定货筐","请先绑定残品/测量/待调查货筐");
            return;
        }
        $scope.product_info_con = '1';
        $scope.scanbadcib = '0';
        $("#scanbadcib").html("");
        $("#scanbadcib").css({"backgroundColor":"#FF0000"});
        $("#receiving_status_span").html("");
        $("#receiving_status_span").css({"backgroundColor":"#eeeee0"});

        $("#" + upid).css({"backgroundColor": "#8c8c8c"});
        $("#" + upid).children("span").text("");

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
            $("#receiving_tip").html(INBOUND_CONSTANT.SCANDAMAGEITEMS);
            isDamaged = true;
            scan_product_info = false;
            scan_pod = true;
            receiveType = INBOUND_CONSTANT.DAMAGED;
        }
        receiving_commonService.CloseWindowByBtn("promenu_pop_window");
        checkContainerIsScan();
        focusOnReceiveInputer();
    };
    // 扫描旧货筐
    $scope.scanOldContainer = function(e){
        if(!receiving_commonService.autoAddEvent(e)){
            return;
        }
        var storageName = $("#window-storage-inputer").val();
        $("#window-storage-inputer").val("");
        if(storageName===''||storageName===undefined){
            $("#inputstoragewindow_span").html("货筐条码无效");
            return;
        }
        if(isOld){
            receivingService.fullStorage(storageName,$rootScope.stationName,function (data) {
                console.log("data-->"+JSON.stringify(data));
                $("#inputstoragewindow_span").html("已成功满筐"+data.cls.storageType+",货筐条码:"+data.cls.storageName+",商品总数"+data.cls.stockAmount+"\n请扫描新的货筐");
                gridStorageInfo();
                destinationId = data.cls.destinationId;
                positionIndex = data.cls.positionIndex;
                $scope.scancontainerType = data.cls.storageType;
                console.log("$scope.scancontainerType.toLowerCase()-->"+$scope.scancontainerType.toLowerCase());
                if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                    console.log("满残品");
                    $scope.scanbadcib = '0';
                    scan_product_content_DAMAGED = false;
                }
                if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                    console.log("满测量");
                    $scope.scanmeasurecib = '0';
                    scan_product_content_MEASURED = false;
                }
                if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                    console.log("满待调查");
                    $scope.scanwaitcib = '0';
                    scan_product_content_TO_INVESTIGATE = false;
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
                    // $scope.scancontainerType = INBOUND_CONSTANT.TO_INVESTIGATE;
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
                            $("#window-storage-inputer").focus();
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                }else{
                    isOld = true;
                    storageid = storageName;
                    console.log("$scope.scancontainerType.toLowerCase()-->"+$scope.scancontainerType.toLowerCase());
                    console.log("INBOUND_CONSTANT.DAMAGED.toLowerCase()-->"+INBOUND_CONSTANT.DAMAGED.toLowerCase());
                    console.log("INBOUND_CONSTANT.MEASURED.toLowerCase()-->"+INBOUND_CONSTANT.MEASURED.toLowerCase());
                    console.log("INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()-->"+INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase());
                    console.log("INBOUND_CONSTANT.MEASURED.toLowerCase()"+INBOUND_CONSTANT.MEASURED.toLowerCase());
                    if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                        scan_product_content_DAMAGED = true;
                    }
                    if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()||$scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
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
                            $scope.scanwaitcib = '0';
                        }
                        finishGoods();
                        /*receiving_commonService.CloseWindowByBtn('scanstoragewindow');
                        return;*/
                    }
                    console.log("scan_product_content_DAMAGED-->"+scan_product_content_DAMAGED);
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
    //清楚所有上次收货状态信息
    function cleanStatus() {
        if(!isDamaged&&!isInvest&&!isMeasured){
            console.log("清除状态");
            $scope.scanbadcib='1';
            $scope.scanmeasurecib='1';
            $scope.scanwaitcib='1';
            $("#receiving_status_span").html("");
            $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
            $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATIONORDN);
        }
        $("#" + upid).css({"backgroundColor": "#8c8c8c"});
        $("#" + upid).children("span").text("");
    }
    function checkContainerIsScan() {
        if(!scan_product_content_DAMAGED){
            if(!isDamaged){//非残品
                $("#receiving_dn_span").html("");
                $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                $scope.product_info_con = 'hidden';
                $("#product_info_span").html("");
                $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                $("#receiving_status_span").html("");
                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                $("#" + upid).children("span").text("");

                //$("#scanbadcib").html(INBOUND_CONSTANT.SCANDAMAGED);
            }else{
                //$("#scanbadcib").html('1');
            }

            $scope.scanbadcib='0';
            $scope.scanmeasurecib='1';
            $scope.scanwaitcib='1';
            $("#scanbadcib").html(INBOUND_CONSTANT.SCANDAMAGED);
            $("#scanbadcib").css({"backgroundColor": "#FF0000"});
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

                    $("#scanmeasurecib").html(INBOUND_CONSTANT.SCANMEASURE);
                }else{
                    $("#scanmeasurecib").html('');
                }

                $scope.scanmeasurecib='0';
                $scope.scanbadcib='1';
                $scope.scanwaitcib='1';
                // $("#scanmeasurecib").html(INBOUND_CONSTANT.SCANMEASURE);
                $("#scanmeasurecib").css({"backgroundColor": "#FFF000"});
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

                        $("#scanwaitcib").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                    }else{
                        $("#scanwaitcib").html('');
                    }

                    $scope.scanwaitcib='0';
                    $scope.scanbadcib='1';
                    $scope.scanmeasurecib='1';
                    // $("#scanwaitcib").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                    $("#scanwaitcib").css({"backgroundColor": "#00b0ff"});
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                }else{
                    $scope.scanwaitcib='1';
                    $scope.scanbadcib='1';
                    $scope.scanmeasurecib='1';
                    if(!scan_pod){
                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
                    }else {
                        if (!scan_DN) {
                            $scope.product_info_con = '1';
                            $("#product_info_span").css({"backgroundColor": "#EEEEE0"});
                            $("#product_info_span").html('');

                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANDN);
                            $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANDN);
                            $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});

                        }else {
                            $("#receiving_dn_span").html(adviceNo);
                            if (!scan_product_info) {
                                if (podid == undefined || podid == null) {
                                    showGeneralWindow("请先扫描Pod", "请先扫描Pod条码");
                                    scan_pod = false;
                                    checkContainerIsScan();
                                    return;
                                }
                                if (isDamaged) {
                                    $scope.scanbadcib = '0';
                                    $scope.scanmeasurecib = '1';
                                    $scope.scanwaitcib = '1';
                                    $("#scanbadcib").css({"backgroundColor": "#FF0000"});
                                    $("#scanbadcib").html('');
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANDAMAGEITEMS);
                                } else if (isInvest) {
                                    $scope.scanbadcib = '1';
                                    $scope.scanmeasurecib = '1';
                                    $scope.scanwaitcib = '0';
                                    $("#scanwaitcib").html('');
                                    $("#scanwaitcib").css({"backgroundColor": "#00b0ff"});
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANINVESTITEMS);
                                } else if (isMeasured) {
                                    $scope.scanbadcib = '1';
                                    $scope.scanmeasurecib = '0';
                                    $scope.scanwaitcib = '1';
                                    $("#scanmeasurecib").html('');
                                    $("#scanmeasurecib").css({"backgroundColor": "#FFF000"});
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANMEASUREDITEMS);
                                } else {
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                }
                                $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                $("#receiving_status_span").html("");
                                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                $scope.product_info_con = '1';
                                $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                            } else {
                                if (isDamaged) {
                                    $scope.scanbadcib = '0';
                                    $scope.scanmeasurecib = '1';
                                    $scope.scanwaitcib = '1';
                                    $("#scanbadcib").css({"backgroundColor": "#FF0000"});
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANSKUTODAMAGED);
                                } else if (isInvest) {
                                    $scope.scanbadcib = '1';
                                    $scope.scanmeasurecib = '1';
                                    $scope.scanwaitcib = '0';
                                    $("#scanwaitcib").css({"backgroundColor": "#00b0ff"});
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                                } else if (isMeasured) {
                                    $scope.scanbadcib = '1';
                                    $scope.scanmeasurecib = '0';
                                    $scope.scanwaitcib = '1';
                                    $("#scanmeasurecib").css({"backgroundColor": "#FFC125"});
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANMEASURE);
                                } else {
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATIONORDN);
                                }
                            }
                        }
                    }
                }
            }
        }
        setTimeout(function(){ $("#receiving-inputer").focus();}, 500);
    }
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
    // 初始化
    $scope.contentWidth = ($rootScope.mainWidth+222-1200)/2;
    $("#receiving_receive").focus();


    //获取货位类型数据
    if($rootScope.locationTypeSize===0){
        $scope.fullfinish = '0';//让Bin选择界面显示
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
