/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("transferStockInAddCtl", function ($scope,$compile,$window,$rootScope,$state,$timeout,transferStockInService,commonService,TRANSFERIN_CONSTANTS) {
    $scope.id='';
    $scope.transferInState='receive';
    $scope.transferInOrderNo='2121212212';
    $scope.reveiceTime='2017-09-15 12:12:12';
    $scope.warehouseId=$window.localStorage["warehouseId"];
    $scope.clientId=$window.localStorage["clientId"];
    $scope.expectedTime='2017-09-15 12:12:12';
    $scope.deliverGoodsTime='2017-09-15 12:12:12';
    $scope.deliveryWarehouse='FD2121212';


    $scope.idWatch = function(newValue,oldValue,scope){

      $scope.transferParam.id=newValue;
      return $scope.transferParam
    };
    $scope.$watch('id',$scope.idWatch,false)


   // $scope.searchData=[{name:"sfsf",trInOrderAmount:"adada",skuNo:"daada"}];
    $scope.transferStockInDatas=[];
      $timeout(function () {
          $scope.initGrid();
      });


    $scope.deleteTransferRequest=function() {



      transferStockInService.deleteTransferOrders(function(data){

        var win = $("#mushinyWindow").data("kendoWindow");
        commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
          setTimeout(function(){
            $("#warnContent").html('删除成功')
          }, 200);
        }});

      },$scope.transferParam = {
        transferInOrderId: $scope.transferInOrderNo
      });
    }



    $scope.saveTransferRequest=function()
    {

      $scope.transferParam={id:$scope.id,
        transferInState:$scope.transferInState,
        transferInOrderNo:$scope.transferInOrderNo,
        reveiceTime:$scope.reveiceTime,
        warehouseId:$scope.warehouseId,
        clientId:$scope.clientId,
        expectedTime:$scope.expectedTime,
        deliverGoodsTime:$scope.deliverGoodsTime,
        deliveryWarehouse:$scope.deliveryWarehouse,

      }

      transferStockInService.addTransferInOrders(function(data){

        var win = $("#mushinyWindow").data("kendoWindow");
        commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
          setTimeout(function(){
            $("#warnContent").html('保存成功')
          }, 200);
        }});
        $scope.id=data;
      },{transfer: kendo.stringify($scope.transferParam)});


    }

      $scope.initGrid=function()
      {
/*

        var crudServiceBaseUrl = "//demos.telerik.com/kendo-ui/service",
          dataSource = new kendo.data.DataSource({
            transport: {
              read:  {
                url: crudServiceBaseUrl + "/Products",
                dataType: "jsonp"
              },
              update: {
                url: crudServiceBaseUrl + "/Products/Update",
                dataType: "jsonp"
              },
              destroy: {
                url: crudServiceBaseUrl + "/Products/Destroy",
                dataType: "jsonp"
              },
              create: {
                url: crudServiceBaseUrl + "/Products/Create",
                dataType: "jsonp"
              },
              parameterMap: function(options, operation) {
                if (operation !== "read" && options.models) {
                  return {models: kendo.stringify(options.models)};
                }
              }
            },
            batch: true,
            pageSize: 20

          });

        $("#grid").kendoGrid({
          dataSource: dataSource,
          pageable: true,
          height: 550,
          toolbar: ["create"],
          columns: [
            "ProductName",
            { field: "UnitPrice", title: "Unit Price", format: "{0:c}", width: "120px" },
            { field: "UnitsInStock", title:"Units In Stock", width: "120px" },
            { field: "Discontinued", width: "120px" },
            { command: ["edit", "destroy"], title: " ", width: "250px" }],
          editable: "inline"
        });
*/



        $scope.dataSource = new kendo.data.DataSource({
              transport: {

                create: {
                  url: "http://192.168.1.152:11089/"+TRANSFERIN_CONSTANTS.addTransferOrderPosition,
                  dataType: "json",
                  type:"GET",
                  beforeSend: function(XMLHttpRequest){
                    XMLHttpRequest.setRequestHeader("Warehouse", $window.localStorage["warehouseId"]);
                    XMLHttpRequest.setRequestHeader("Authorization", "Bearer "+ $window.localStorage["accessToken"]);
                  }
                },
                parameterMap: function(options, operation) {

                  if (operation !== "read" && options.models) {

                    options.models.push($scope.transferParam)
                    return {transfer: kendo.stringify(options.models)};
                  }
                }
              },
              batch: true,
            schema: {
              model: {
                id: "name",
                fields: {
                  name: { validation: { required: true } },
                  skuNo: { validation: { required: true } },
                  trInOrderAmount: { type: "number", validation: { required: true, min: 1} }

                }
              }
            }
          }
            );

          $("#grid").kendoGrid({
              dataSource: $scope.dataSource,
              height: 550,
            autoBind:false,
              toolbar:  [
/*
                {template :


                  kendo.template($compile($("#template").html())($scope).html())
              },*/
              {
                name : "create",
                text : "请添加容器详情"
              }
             ]
            ,
              columns: [
                  { field: "name", title: "容器号码", width: "120px" },
                  { field: "trInOrderAmount", title:"数量", width: "120px" },
                  { field: "skuNo", title:"SKU",width: "120px" },
                 { command: ["edit"], title: " ", width: "250px" }
                  ],
              editable: "inline"
          });



      }




  });
})();