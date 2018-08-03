/**
 * Created by feiyu.pan on 2017/4/24.
 * Updated by feiyu.pan on 2017/5/31
 * Updated by preston.zhang on 2017/08-05
 * Latest Update in 2017/08/10
 */
(function () {
  "use strict";

  angular.module('myApp').controller("deliverySystemCtl", function ($timeout,$scope, $window, $state, $rootScope,outboundService,deliverySystemService,receiving_commonService) {
    $scope.startTime=kendo.format("{0:yyyy-MM-dd}",new Date());//默认开始时间
    $scope.endTime=kendo.format("{0:yyyy-MM-dd}",new Date(new Date().setDate(new Date().getDate()+1)));//默认结束时间
    $scope.checked=true ;//是否只显示未发货的
    $scope.reloadable=true;//能否重新装载
    $scope.bindDoorAble=false;//能否绑定门
    $scope.isShowZeroShip = true;
    $scope.isDeliver = true;

    var columns=[
      {field:"deliveryPoint",width:80,headerTemplate:"<span translate='DATE'></span>"},
      {field:"exSD",width:80,headerTemplate:"<span translate='ExSD'></span>"},
      {field:"sortCode",width:80,headerTemplate:"<span translate='Sort Code'></span>",template:function (item) {
        var sortCode=item.sortCode;
        var sendDate = item.sendDate;
        var exSD = item.deliveryPoint+" "+item.exSD;
        if(typeof(sortCode)==="undefined"){
          sortCode="";
        }
        var html="<a ng-click='jump(\""+sortCode+"\",\""+item.state+"\",\""+$scope.checked+"\",\""+$scope.startTime+"\",\""+$scope.endTime+"\",\""+sendDate+"\",\""+exSD+"\")'>"+sortCode+"</a>";
        return html;
      }},
      {field:"dockDoor",width:80,headerTemplate:"<span translate='DOCK_DOOR'></span>"},
      {field:"outGoodsTotal",aggregate: "sum",width:100,headerTemplate:"<span translate='发货总单量'></span>",template:function (item) {
          var outGoodsTotal = item.outGoodsTotal;
          if(item.exSD==='Total'){
              var html="<a ng-click='jumpToOutDetails(\""+''+"\",\""+''+"\",\""+''+"\",\""+''+"\",\""+''+"\")'>"+outGoodsTotal+"</a>";
              return html;
          }
          var sortCode=item.sortCode;
          var deliverTime = item.deliveryPoint+" "+item.exSD;
          var sendDate = item.sendDate;
          var state = item.state;
          var shipmentState = '';
          var html="<a ng-click='jumpToOutDetails(\""+sortCode+"\",\""+state+"\",\""+deliverTime+"\",\""+sendDate+"\",\""+shipmentState+"\")'>"+outGoodsTotal+"</a>";
          return html;
      }},
      {field:"unpick",aggregate: "sum",width:110,headerTemplate:"<span translate='尚未分拣单量'></span>",template:function (item) {
          var shipmentState = '%3C%3D1100 AND nano.State%3C%3E1000 AND nano.State%3C%3E900 AND nano.State%3C%3E800 AND nano.State%3C%3E700 AND nano.State%3C%3E690 AND nano.State%3C%3E680 AND nano.State%3C%3E670 AND nano.State%3C%3E660 ';
          // shipmentState.replace(/\</g,"%3C").replace(/\=/g,"%3D").replace(/\>/g,"%3E");
          if(item.exSD==='Total'){
              var html="<a ng-click='jumpToOutDetails(\""+''+"\",\""+''+"\",\""+''+"\",\""+''+"\",\""+shipmentState+"\")'>"+item.unpick+"</a>";
              return html;
          }
          var sortCode=item.sortCode;
          var deliverTime = item.deliveryPoint+" "+item.exSD;
          var sendDate = item.sendDate;
          var state = item.state;
          var html="<a ng-click='jumpToOutDetails(\""+sortCode+"\",\""+state+"\",\""+deliverTime+"\",\""+sendDate+"\",\""+shipmentState+"\")'>"+item.unpick+"</a>";
          return html;
      }},
      {field:"pickedUnLoad",aggregate: "sum",width:100,headerTemplate:"<span translate='已分拣尚未装载单量'></span>",template:function (item) {
          var shipmentState = '%3E%3D660 AND nano.State%3C670 AND nano.State%3C%3E1100 ';
          // shipmentState.replace(/\</g,"%3C").replace(/\=/g,"%3D").replace(/\>/g,"%3E");
          if(item.exSD==='Total'){
              var html="<a ng-click='jumpToOutDetails(\""+''+"\",\""+''+"\",\""+''+"\",\""+''+"\",\""+shipmentState+"\")'>"+item.pickedUnLoad+"</a>";
              return html;
          }
          var sortCode=item.sortCode;
          var deliverTime = item.deliveryPoint+" "+item.exSD;
          var sendDate = item.sendDate;
          var state = item.state;
          var html="<a ng-click='jumpToOutDetails(\""+sortCode+"\",\""+state+"\",\""+deliverTime+"\",\""+sendDate+"\",\""+shipmentState+"\")'>"+item.pickedUnLoad+"</a>";
          return html;
      }},
      {field:"loaded",aggregate: "sum",width:110,headerTemplate:"<span translate='已装载单量'></span>",template:function (item) {
          var shipmentState = '%3E%3D670 AND nano.State%3C690 ';
          // shipmentState.replace(/\</g,"%3C").replace(/\=/g,"%3D").replace(/\>/g,"%3E");
          if(item.exSD==='Total'){
              var html="<a ng-click='jumpToOutDetails(\""+''+"\",\""+''+"\",\""+''+"\",\""+''+"\",\""+shipmentState+"\")'>"+item.loaded+"</a>";
              return html;
          }
          var sortCode=item.sortCode;
          var deliverTime = item.deliveryPoint+" "+item.exSD;
          var sendDate = item.sendDate;
          var state = item.state;
          var html="<a ng-click='jumpToOutDetails(\""+sortCode+"\",\""+state+"\",\""+deliverTime+"\",\""+sendDate+"\",\""+shipmentState+"\")'>"+item.loaded+"</a>";
          return html;
      }},
      {field:"usedContainersNum",aggregate: "sum",width:110,headerTemplate:"<span translate='使用笼车总数量'></span>",template:function (item) {
          var sortCode=item.sortCode;
          var usedContainersNum = item.usedContainersNum;
          var deliverTime = '';
          var state = item.state;
          var shippingDate = null;
          if(state==='已发货'){
              shippingDate = item.sendDate;
          }else{
              shippingDate = '';
          }
          if(item.exSD==='Total'){
              deliverTime = '';
          }else{
              deliverTime = item.deliveryPoint+" "+item.exSD
          }
          if(typeof(sortCode)==="undefined"){
              sortCode="";
          }
          var html="<a ng-click='checkCiperTotal(\""+sortCode+"\",\""+deliverTime+"\",\""+state+"\",\""+shippingDate+"\",\""+''+"\")'>"+usedContainersNum+"</a>";
          return html;
      }},
      {field:"unloadConinerNum",aggregate: "sum",width:110,headerTemplate:"<span translate='尚未装载笼车数量'></span>",template:function (item) {
          var sortCode=item.sortCode;
          var unloadConinerNum = item.unloadConinerNum;
          var deliverTime = '';
          var state = item.state;
          var shippingDate = null;
          if(state==='已发货'){
              shippingDate = item.sendDate;
          }else{
              shippingDate = '';
          }
          if(item.exSD==='Total'){
              deliverTime = '';
          }else{
              deliverTime = item.deliveryPoint+" "+item.exSD
          }
          if(typeof(sortCode)==="undefined"){
              sortCode="";
          }
          var html="<a ng-click='checkCiperTotal(\""+sortCode+"\",\""+deliverTime+"\",\""+state+"\",\""+shippingDate+"\",\""+'unload'+"\")'>"+unloadConinerNum+"</a>";
          return html;
      }},
      {field:"loadedContainerNum",aggregate: "sum",width:110,headerTemplate:"<span translate='已经装载笼车数量'></span>",template:function (item) {
          var sortCode=item.sortCode;
          var loadedContainerNum = item.loadedContainerNum;
          var deliverTime = '';
          var state = item.state;
          var shippingDate = null;
          if(state==='已发货'){
              shippingDate = item.sendDate;
          }else{
              shippingDate = '';
          }
          if(item.exSD==='Total'){
              deliverTime = '';
          }else{
              deliverTime = item.deliveryPoint+" "+item.exSD
          }
          if(typeof(sortCode)==="undefined"){
              sortCode="";
          }
          var html="<a ng-click='checkCiperTotal(\""+sortCode+"\",\""+deliverTime+"\",\""+state+"\",\""+shippingDate+"\",\""+'loaded'+"\")'>"+loadedContainerNum+"</a>";
          return html;
      }},
      {field:"state",width:80,headerTemplate:"<span translate='STATE'></span>",template:function (item) {
        //改变状态获取当前行信息
        var exSD=item.deliveryPoint+" "+item.exSD;
        var sortCode=item.sortCode;
        var state=item.state;

        $scope.outDockDoor = item.dockDoor;
        if(typeof(sortCode)==="undefined"){
          state="";
        }
        var html="<a ng-click='changeState(\""+sortCode+"\",\""+exSD+"\",\""+state+"\")'>"+state+"</a>";
        return html;
      }},
      {field:"sendDate",width:120,headerTemplate:"<span translate='发货时间'></span>"}];
      $scope.deliverGoodsGridOptions=outboundService.reGrids("",columns,$(document.body).height()-203);
      $scope.transOutGoodsGridOptions=outboundService.reGrids("",columns,$(document.body).height()-203);
    //TabSheet
    $scope.deliverTabOpation = {
          animation: {
              close: {
                  duration: 100,
                  effects: "fadeOut"
              },
              // fade-in new tab over 500 milliseconds
              open: {
                  duration: 100,
                  effects: "fadeIn"
              }
          },
          activate: function () {//设置调拨出库页面
              var grid = $("#transOutGoodsGrid").data("kendoGrid");
              grid.setOptions({
                  height: $(document.body).height() - 305
              });
          }
      };
    //按照上面部分条件搜索
    $scope.determineSearchCriteria=function () {
      $scope.airTime=$scope.startTime;
      $scope.finishTime=$scope.endTime;
      $scope.search();
      $scope.searchOption="";
    };

      $scope.getpage=function(){
          deliverySystemService.getPageData($scope.startTime,$scope.endTime,$scope.checked,$scope.isShowZeroShip,function (data) {
              if(data==null||data.goodsInnerClassList==null||data.goodsInnerClassList==undefined||data.goodsInnerClassList.length==0){
                  return;
              }
              console.log("發貨系統:"+data);
              var dataSource=[];
                  var totalData ={
                      "deliveryPoint":kendo.format("{0:yyyy/MM/dd}",kendo.parseDate(data.goodsInnerClassList[0].deliveryPoint)),
                      "exSD":"Total",
                      "outGoodsTotal":data.outGoodsTotal,
                      "unpick":data.unpickTotal,
                      "pickedUnLoad":data.unloadTotal,
                      "loaded":data.loadedTotal,
                      "usedContainersNum":data.usecontainerTotal,
                      "unloadConinerNum":data.unloadContainerTotal,
                      "loadedContainerNum":data.loadedContainerTotal
                  };
                  dataSource.push(totalData);
                  for(var k=0;k<data.goodsInnerClassList.length;k++){
                    var point = data.goodsInnerClassList[k].deliveryPoint;
                      data.goodsInnerClassList[k]["exSD"]=point.substr(11,18);
                      data.goodsInnerClassList[k].deliveryPoint=kendo.format("{0:yyyy/MM/dd}",data.goodsInnerClassList[k].deliveryPoint.substr(0,10));
                      dataSource.push(data.goodsInnerClassList[k]);
                  }
              var grid=$("#deliverGoodsGrid").data("kendoGrid");
              grid.setOptions({change:function () {
                  //选中行获得信息
                  var row=grid.select();
                  var item=grid.dataItem(row);
                  $scope.sortCode=item.sortCode;
                  $scope.deliveryTime=item.deliveryPoint+" "+item.exSD;
                  $scope.dockDoor=item.dockDoor;
                  $scope.date = item.deliveryPoint;
                  if(item.state=="等待装载"){
                      $timeout(function () {
                          $scope.bindDoorAble=false;
                          $scope.reloadable = true;
                      })
                  }else if(item.state=='等待打印发货单' || item.state=="等待发货") {
                      $timeout(function () {
                          $scope.bindDoorAble=true;
                          $scope.reloadable = false;
                      })
                  }else {
                      $timeout(function () {
                          $scope.reloadable = true;
                          $scope.bindDoorAble = true;
                      })
                  }
              },selectable: true, allowCopy: true
                  ,dataBound:function () {
                  //合并单元格
                  for (var i = 0; i < dataSource.length; i++) {
                      this.tbody.find("tr:eq(" + i + ")").each(function () {
                          var td = $(this).find("td:eq(1)");
                          if (td.text() == "Total") {
                              td.attr("colspan", 3);
                              var l = 0;
                              while (l < 2) {
                                  $(this).find("td:eq(2)").remove();
                                  l++;
                              }
                          }
                      });
                  }
              }
              });
              grid.setOptions({dataSource:dataSource});
          })
      };
      //获取网站默认数据
      $scope.getpage();
      //按照搜索框结合上面部分条件搜索
      $scope.search=function(e){
          console.log("e-->",e);
          if(e!==null&&e!==undefined){
              if(!receiving_commonService.autoAddEvent(e))
                  return;
              deliverySystemService.getDeliverySystemData($scope.searchOption,$scope.startTime,$scope.endTime,$scope.checked,$scope.isShowZeroShip,function (data) {
                  if(data==null||data.goodsInnerClassList==null||data.goodsInnerClassList==undefined||data.goodsInnerClassList.length==0){
                      receiving_commonService.receiving_tip_dialog("deliver_tipwindow",{
                          title:"未获取到列表信息",
                          open:function () {
                              $scope.deliver_tipwindow_span = '未搜索到相关信息,请重新选定搜索条件';
                          },
                          close:function () {
                              $scope.deliver_tipwindow_span = '';
                          }
                      });
                      return;
                  }
                  var dataSource=[];
                  for(var k=0;k<data.goodsInnerClassList.length;k++){
                      var point = data.goodsInnerClassList[k].deliveryPoint;
                      data.goodsInnerClassList[k]["exSD"]=point.substr(11,18);
                      data.goodsInnerClassList[k].deliveryPoint=kendo.format("{0:yyyy/MM/dd}",data.goodsInnerClassList[k].deliveryPoint.substr(0,10));
                      dataSource.push(data.goodsInnerClassList[k]);
                  }
                  var totalData ={
                      "deliveryPoint":kendo.format("{0:yyyy/MM/dd}",kendo.parseDate(data.goodsInnerClassList[0].deliveryPoint)),
                      "exSD":"Total",
                      "outGoodsTotal":data.outGoodsTotal,
                      "unpick":data.unpickTotal,
                      // "unloadTotal":data.pickedTotal,
                      "pickedUnLoad":data.unloadTotal,
                      "loaded":data.loadedTotal,
                      "usedContainersNum":data.usecontainerTotal,
                      "unloadConinerNum":data.unloadContainerTotal,
                      "loadedContainerNum":data.loadedContainerTotal
                  };
                  dataSource.unshift(totalData);
                  var grid=$("#deliverGoodsGrid").data("kendoGrid");
                  grid.setOptions({change:function () {
                      //选中行获得信息
                      var row=grid.select();
                      var item=grid.dataItem(row);
                      $scope.sortCode=item.sortCode;
                      $scope.deliveryTime=item.deliveryPoint+" "+item.exSD;
                      $scope.dockDoor=item.dockDoor;
                      $scope.date = item.deliveryPoint;
                      if(item.state=="等待装载"){
                          $timeout(function () {
                              $scope.bindDoorAble=false;
                              $scope.reloadable = true;
                          })
                      }else if(item.state=='等待打印发货单' || item.state=="等待发货") {
                          $timeout(function () {
                              $scope.bindDoorAble=true;
                              $scope.reloadable = false;
                          })
                      }else {
                          $timeout(function () {
                              $scope.reloadable = true;
                              $scope.bindDoorAble = true;
                          })
                      }
                  },selectable: true, allowCopy: true});
                  grid.setOptions({dataSource:dataSource});
              });
          }else{
              deliverySystemService.getDeliverySystemData($scope.searchOption,$scope.startTime,$scope.endTime,$scope.checked,$scope.isShowZeroShip,function (data) {
                  console.log("data.outGoodsTotal-fdfds-->"+data.outGoodsTotal);
                  if(data==null||data.goodsInnerClassList==null||data.goodsInnerClassList==undefined||data.goodsInnerClassList.length==0){
                      receiving_commonService.receiving_tip_dialog("deliver_tipwindow",{
                          title:"未获取到列表信息",
                          open:function () {
                              $scope.deliver_tipwindow_span = '未搜索到相关信息,请重新选定搜索条件';
                          },
                          close:function () {
                              $scope.deliver_tipwindow_span = '';
                          }
                      });
                      return;
                  }
                  var dataSource=[];
                  for(var k=0;k<data.goodsInnerClassList.length;k++){
                      var point = data.goodsInnerClassList[k].deliveryPoint;
                      data.goodsInnerClassList[k]["exSD"]=point.substr(11,18);
                      data.goodsInnerClassList[k].deliveryPoint=kendo.format("{0:yyyy/MM/dd}",data.goodsInnerClassList[k].deliveryPoint.substr(0,10));
                      dataSource.push(data.goodsInnerClassList[k]);
                  }
                  var totalData ={
                      "deliveryPoint":kendo.format("{0:yyyy/MM/dd}",kendo.parseDate(data.goodsInnerClassList[0].deliveryPoint)),
                      "exSD":"Total",
                      "outGoodsTotal":data.outGoodsTotal,
                      "unpick":data.unpickTotal,
                      // "unloadTotal":data.pickedTotal,
                      "pickedUnLoad":data.unloadTotal,
                      "loaded":data.loadedTotal,
                      "usedContainersNum":data.usecontainerTotal,
                      "unloadConinerNum":data.unloadContainerTotal,
                      "loadedContainerNum":data.loadedContainerTotal
                  };
                  dataSource.unshift(totalData);
                  var grid=$("#deliverGoodsGrid").data("kendoGrid");
                  grid.setOptions({change:function () {
                      //选中行获得信息
                      var row=grid.select();
                      var item=grid.dataItem(row);
                      $scope.sortCode=item.sortCode;
                      $scope.deliveryTime=item.deliveryPoint+" "+item.exSD;
                      $scope.dockDoor=item.dockDoor;
                      $scope.date = item.deliveryPoint;
                      if(item.state=="等待装载"){
                          $timeout(function () {
                              $scope.bindDoorAble=false;
                              $scope.reloadable = true;
                          })
                      }else if(item.state=='等待打印发货单' || item.state=="等待发货") {
                          $timeout(function () {
                              $scope.bindDoorAble=true;
                              $scope.reloadable = false;
                          })
                      }else {
                          $timeout(function () {
                              $scope.reloadable = true;
                              $scope.bindDoorAble = true;
                          })
                      }
                  },selectable: true, allowCopy: true});
                  grid.setOptions({dataSource:dataSource});
              });
          }
      };

      $scope.jumpToOutDetails = function (sortCode,state,exSD,sendDate,shipmentState) {
          $state.go('main.delivery_shipments_detail',{params: angular.toJson({sortCode: sortCode, startTime: $scope.startTime, endTime: $scope.endTime,checked:$scope.checked,state:state,exSD:exSD,sendDate:sendDate,shipmentState:shipmentState,jump:true})})
      }
    //跳转
    $scope.jump=function (sortCode,state,checked,startTime,endTime,sendDate,exSD) {
      $state.go('main.deliverySystemPrint',{params: angular.toJson({sortCode: sortCode, state: state,checked:checked,startTime:startTime,endTime:endTime,sendDate:sendDate,exSD:exSD})})
    };
    //查看当前sortcode下所有的笼车总数使用情况
    $scope.checkCiperTotal = function (sortCode,deliverTime,state,shippingDate,cartState) {
        $state.go('main.query_cart',{params: angular.toJson({sortCode: sortCode,deliverTime:deliverTime,state:state,shippingDate:shippingDate,cartState:cartState,startTime:$scope.startTime,endTime:$scope.endTime})});
    }
    //修改状态
    $scope.changeState=function (sortCode,exSD,state) {

      if(state==='等待装载'){
        deliverySystemService.changeState(sortCode,exSD, '100', function () {
          $scope.getpage();
        });
      }

      if(state==='正在装载') {
        deliverySystemService.changeState(sortCode,exSD, '200', function () {
          $scope.getpage();
        });
      }
      if(state==='等待发货'){
          receiving_commonService.receiving_tip_dialog_normal("sure_outed_tipwindow",{
              title:"确认发货?",
              width:600,
              height:350,
              open:function () {
                  $("#sure_out_window_span").html("请确认发货信息!");
                  $scope.outDeliverTime = exSD;
                  $scope.outSortCode = sortCode;
              }
          });
      }
    };
    //确认发货
    $scope.sureGoodsOut = function() {
        receiving_commonService.CloseWindowByBtn("sure_outed_tipwindow");
        deliverySystemService.changeState($scope.outSortCode, $scope.outDeliverTime, '400', function () {
            deliverySystemService.toinsertRequst($scope.outSortCode, $scope.outDeliverTime);
            $scope.getpage();
        },function (data) {
            receiving_commonService.receiving_tip_dialog("deliver_tipwindow",{
                title:"错误",
                open:function () {
                    $("#deliver_tipwindow_span").html(data.message.replace("Unkonw Error","").replace("[","").replace("]",""));
                },close:function () {
                    $scope.getpage();
                }
            });
        });
    };
    //绑定收获门
    $scope.confirmReloadBindDoor=function () {
      $scope.openWindow("#bindDoor",$scope.bindDoorWindow);
      deliverySystemService.getDockDoor(function (data) {
        $scope.dockDoorDataSource=data;
        var dropDownList=$("#dockDoor").data("kendoDropDownList");
        dropDownList.value("");
      });
    };
    //确认收获门
    $scope.bindDoor=function () {
      var dropDownList=$("#dockDoor").data("kendoDropDownList");
      $scope.dockDoor=dropDownList.value();
      deliverySystemService.bindDockDoor($scope.dockDoor,$scope.sortCode,$scope.deliveryTime,function () {
        $scope.checked=true ;//是否只显示未发货的
        $scope.isShowZeroShip = true;//是否只显示总单量不为零
        $scope.bindDoorWindow.close();

        deliverySystemService.getDeliverySystemData($scope.searchOption,$scope.startTime,$scope.endTime,$scope.checked,$scope.isShowZeroShip,function (data) {
          if(data==null||data.goodsInnerClassList==null||data.goodsInnerClassList==undefined||data.goodsInnerClassList.length==0){
            receiving_commonService.receiving_tip_dialog("deliver_tipwindow",{
              title:"未获取到列表信息",
              open:function () {
                $scope.deliver_tipwindow_span = '未搜索到相关信息,请重新选定搜索条件';
              },
              close:function () {
                $scope.deliver_tipwindow_span = '';
              }
            });
            return;
          }
          var dataSource=[];
          for(var k=0;k<data.goodsInnerClassList.length;k++){
            var point = data.goodsInnerClassList[k].deliveryPoint;
            data.goodsInnerClassList[k]["exSD"]=point.substr(11,18);
            data.goodsInnerClassList[k].deliveryPoint=kendo.format("{0:yyyy/MM/dd}",data.goodsInnerClassList[k].deliveryPoint.substr(0,10));
            dataSource.push(data.goodsInnerClassList[k]);
          }
          var totalData ={
            "deliveryPoint":kendo.format("{0:yyyy/MM/dd}",kendo.parseDate(data.goodsInnerClassList[0].deliveryPoint)),
            "exSD":"Total",
            "outGoodsTotal":data.outGoodsTotal,
            "unpick":data.unpickTotal,
            // "unloadTotal":data.pickedTotal,
            "pickedUnLoad":data.unloadTotal,
            "loaded":data.loadedTotal,
            "usedContainersNum":data.usecontainerTotal,
            "unloadConinerNum":data.unloadContainerTotal,
            "loadedContainerNum":data.loadedContainerTotal
          };
          dataSource.unshift(totalData);
          var grid=$("#deliverGoodsGrid").data("kendoGrid");
          grid.setOptions({change:function () {
            //选中行获得信息
            var row=grid.select();
            var item=grid.dataItem(row);
            $scope.sortCode=item.sortCode;
            $scope.deliveryTime=item.deliveryPoint+" "+item.exSD;
            $scope.dockDoor=item.dockDoor;
            $scope.date = item.deliveryPoint;
            if(item.state=="等待装载"){
              $timeout(function () {
                $scope.bindDoorAble=false;
                $scope.reloadable = true;
              })
            }else if(item.state=='等待打印发货单' || item.state=="等待发货") {
              $timeout(function () {
                $scope.bindDoorAble=true;
                $scope.reloadable = false;
              })
            }else {
              $timeout(function () {
                $scope.reloadable = true;
                $scope.bindDoorAble = true;
              })
            }
          },selectable: true, allowCopy: true});
          grid.setOptions({dataSource:dataSource});
        });







      })
    };




    //确认重新装载
    $scope.confirmReload=function () {
      $scope.openWindow("#reload",$scope.reloadWindow);
    };
    //重新装载
    $scope.reload=function () {
        $scope.reloadWindow.close();
      deliverySystemService.reload($scope.sortCode,function () {
        $scope.getpage();
      })
    };
    $scope.openWindow = function (windowId, windowName) {
      $(windowId).parent().addClass("deliverySystemWindow");
      windowName.setOptions({
        width:650,
        closable: true
      });
      windowName.center();
      windowName.open();
    };
    }).controller("deliverySystemPrintCtl",function ($stateParams,$scope,$state,deliverySystemService,receiving_commonService) {
        $scope.printable=true;//是否能够打印
        var params=angular.fromJson($stateParams.params);
        $scope.sortCode=params.sortCode;
        $scope.state=params.state;//状态
        $scope.orderNumber= [];
        $scope.checked = null;
        $scope.sendDate = params.sendDate;
        $scope.exSD = params.exSD;
        $scope.startTime = params.startTime;
        $scope.endTime = params.endTime;
        var shipState = '';
        if($scope.state==='已发货'){
            $scope.checked = false;
            shipState = '>=660 AND nano.State<=690 ';
        }else{
            $scope.checked = true;
            shipState = '>=670 AND nano.State<690 ';
        }
        if($scope.state == "等待打印发货单" || $scope.state == "等待发货"){
          $scope.printable=false;
        }
        getPrintInfo();
        //打印
        $scope.print=function () {
          deliverySystemService.print($scope.sortCode,function () {

            //getPrintInfo();
          },function (data) {
              var window_id = '';
              if(data.key==='EX_OBJECT_NOT_FOUND'){
                  window_id = "deliverTipwindowWithButton";
                  receiving_commonService.receiving_tip_dialog(window_id,{
                      title:"提示",
                      width:500,
                      height:400,
                      open:function () {
                          $("#deliverTipWindow_BtnSpan").html(kendo.format("{0:yyyy/MM/dd HH:mm:ss}",kendo.parseDate(data.values[0]))+data.values[1]+"点击确定重新打印发货单,并请将旧发货单销毁.");
                      }
                  });
              }else{
                  window_id = "deliverTipwindow";
                  receiving_commonService.receiving_tip_dialog(window_id,{
                      title:"错误",
                      width:500,
                      height:400,
                      open:function () {
                          $("#deliverTipWindow_Span").html(data);
                      }
                  });
              }
          });
        };
        //重新打印
        $scope.rePrint = function () {
            deliverySystemService.rePrint($scope.sortCode,$scope.outNo,function (data) {
                receiving_commonService.closePopWindow("deliverTipwindowWithButton");
                $scope.state = '等待发货';
              // getPrintInfo();
            });
        }
        //总单量跳转
        $scope.jump=function () {
          $state.go('main.delivery_shipments_detail',{params: angular.toJson({sortCode: $scope.sortCode, startTime: $scope.startTime, endTime: $scope.endTime,checked:$scope.checked,state:$scope.state,exSD:'',sendDate:$scope.sendDate,shipmentState:shipState,jump:true})})
        };
        //细节单量跳转
        $scope.toJump=function(exSD){
          $state.go('main.delivery_shipments_detail',{params: angular.toJson({sortCode: $scope.sortCode, startTime: $scope.startTime, endTime: $scope.endTime,checked:$scope.checked,state:$scope.state,exSD:exSD,sendDate:'',shipmentState:shipState,jump:true})})
        };

      /**
       * 获取打印单信息
       */
      function getPrintInfo(){
          var date = null;
          if($scope.state==='已发货'){
              date = $scope.sendDate;
          }else{
              date = $scope.exSD;
          }
            deliverySystemService.getSortCodePrintInfo($scope.sortCode,$scope.state,date,function (data) {
                if(data.exSDS!=null&&data.exSDS.length>0){
                    for (var key=0;key<data.exSDS.length;key++){
                        if($scope.orderNumber.length>0){
                            for (var l=0;l<$scope.orderNumber.length;l++){
                                if($scope.orderNumber[l].exSD===data.exSDS[key].deliverTime){
                                    $scope.orderNumber[l].totalShipments = data.exSDS[key].innerTotalNUm;
                                }else{
                                    $scope.orderNumber.push({
                                        exSD:data.exSDS[key].deliverTime,
                                        totalShipments:data.exSDS[key].innerTotalNUm
                                    });
                                }
                            }
                        }else{
                            $scope.orderNumber.push({
                                exSD:data.exSDS[key].deliverTime,
                                totalShipments:data.exSDS[key].innerTotalNUm
                            });
                        }
                    }
                }
                $scope.warehouse=data.outWarehouse;//仓库名
                $scope.outNo = data.outNo;
                $scope.sortCode = data.sortCode;
                $scope.nowDate=kendo.format("{0:yyyy/MM/dd HH:mm:ss}",new Date());
                $scope.totalNum = data.totalNum;
            },function (data) {
                var window_tip = '';
                if(data.key==='DataNotFound'){
                    window_tip = "当前发货时间点未进行绑定发货门操作请先进行绑定发货门操作\n\n如果该SortCode已经绑定Dock门请在确认发货门时直接选择空白项点击确定即可进行装载";
                }else{
                    window_tip = data.message.replace("Unkonw Error [","").replace("]","");
                }
                receiving_commonService.receiving_tip_dialog("deliverPrintTipwindow",{
                    title:"错误",
                    width:500,
                    height:400,
                    open:function () {
                        $("#deliverTipWindow_Span").html(window_tip);
                    },
                    close:function () {
                        history.back();
                    }
                });
            });
        }
  })
})();