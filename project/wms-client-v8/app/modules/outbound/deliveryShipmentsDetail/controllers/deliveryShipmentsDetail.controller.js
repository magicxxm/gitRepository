/**
 * Created by feiyu.pan on 2017/4/25.
 * Updated by feiyu.pan on 2017/5/31
 */
(function () {
  "use strict";

  angular.module('myApp').controller("deliveryShipmentsDetailCtl",function ($scope,$stateParams,outboundService,deliveryShipmentsDetailService) {
    $scope.checked=false;
    $scope.jump = false;
    if($stateParams.params!=""){
        var params=angular.fromJson($stateParams.params);
        $scope.searchOption=params.sortCode;//搜索条件
        $scope.sortCode = params.sortCode;
        $scope.checked = params.checked;
        $scope.state = params.state;
        $scope.exSD = params.exSD.replace(".0","");
        $scope.sendDate = params.sendDate.replace(".0","");
        $scope.jump = params.jump;
        $scope.shipmentState = params.shipmentState;
        console.log("jump--->"+$scope.jump);
        $scope.endTime=kendo.format("{0:yyyy-MM-dd}",new Date(params.endTime));//结束时间
        $scope.startTime=kendo.format("{0:yyyy-MM-dd}",new Date(params.startTime));//开始时间
      }else{
        $scope.startTime=kendo.format("{0:yyyy-MM-dd}",new Date());
        $scope.endTime=kendo.format("{0:yyyy-MM-dd}",new Date(new Date().setDate(new Date().getDate()+1)));
        $scope.searchOption="";
    }
    //获取所有exSD
      deliveryShipmentsDetailService.getExsds(function (data) {
          data.unshift("ALL");
          $scope.exSDOptions={
              dataSource:data
          };
      });
    var columns=[
      {field:"date",width:80,headerTemplate:"<span translate='日期'></span>"},
      {field:"deliverTimeExSd",width:80,headerTemplate:"<span translate='ExSD'></span>"},
      {field:"shipmentId",width:100,headerTemplate:"<span translate='Shipment ID'></span>",template:"<a ui-sref='main.shipment_detail({shipment:dataItem.shipmentId})'>#: shipmentId #</a>"},
      {field:"ciperNo",width:80,headerTemplate:"<span translate='笼车号码'></span>",template:"<a ui-sref='main.cart_query_shipment({cartName:dataItem.ciperNo})'>#: ciperNo#</a>"
      },
      {field:"moveTime",width:120,headerTemplate:"<span translate='移包裹时间'></span>",template:function (item) {
          if(item.moveTime===""||item.moveTime===undefined||item.moveTime===null){
              return "";
          }else{
              return kendo.format("{0:yyyy/MM/dd HH:mm:ss}",kendo.parseDate(item.moveTime))===null?"":kendo.format("{0:yyyy/MM/dd HH:mm:ss}",kendo.parseDate(item.moveTime));
          }
      }},
      {field:"operator",width:80,headerTemplate:"<span translate='移包裹人员'></span>"},
      {field:"sortCode",width:110,headerTemplate:"<span translate='Sort Code区域'></span>"},
      {field:"state",width:80,headerTemplate:"<span translate='状态'></span>"}];
    $scope.deliverShipmentsDetailGridOptions=outboundService.reGrids("",columns,$(document.body).height()-203);
    //上面部分搜索
    $scope.determineSearchCriteria=function () {
      $scope.airTime=$scope.startTime;
      $scope.finishTime=$scope.endTime;
      $scope.searchOption="";
      var dropDownList=$("#exSD").data("kendoDropDownList");
      $scope.exSD=dropDownList.value();
      $scope.search();
    };
    //按照搜索框搜索
    $scope.search=function(){
      deliveryShipmentsDetailService.searchDeliveryShipmentsDetailData($scope.startTime,$scope.endTime,$scope.exSD, $scope.checked,$scope.searchOption,$scope.sortCode,function (data) {
        var grid=$("#deliverShipmentsDetailGrid").data("kendoGrid");
        var resource = [];
        for (var l=0;l<data.length;l++){
            if(data[l].moveTime===null||data[l].moveTime===undefined){
                data[l].moveTime='';
            }
            resource.push(data[l]);
        }
        grid.setOptions({selectable: true, allowCopy: true});
        grid.setDataSource(new kendo.data.DataSource({data:resource}));
      })
    };
      $scope.getInitData=function(){
          deliveryShipmentsDetailService.getDeliveryShipmentsDetailData($scope.sortCode,
                                                                        $scope.startTime,
                                                                        $scope.endTime,
                                                                        $scope.exSD,
                                                                        $scope.sendDate,
                                                                        $scope.state, 
                                                                        $scope.checked,
                                                                        $scope.shipmentState,
              function (data) {
                  var grid=$("#deliverShipmentsDetailGrid").data("kendoGrid");
                  var resource = [];
                  for (var l=0;l<data.length;l++){
                      if(data[l].moveTime===null||data[l].moveTime===undefined){
                          data[l].moveTime='';
                      }
                      resource.push(data[l]);
                  }
                  grid.setOptions({selectable: true, allowCopy: true});
                  grid.setDataSource(new kendo.data.DataSource({data:resource}));
                  $scope.startTime=kendo.format("{0:yyyy-MM-dd}",new Date());
                  $scope.endTime=kendo.format("{0:yyyy-MM-dd}",new Date(new Date().setDate(new Date().getDate()+1)));
                  $scope.searchOption='';
                  $scope.sortCode='';
          })
      };
      if($scope.jump){
           debugger;
          $scope.getInitData();
      }else{
          $scope.search();
      }
  })
})();
