/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("transferStockInCtl", function ($scope,$window,$rootScope,$state,$timeout,$translate,transferStockInService) {

    $scope.transferDisplayDataClick=false;
    $scope.transferInOrderId='';
    $scope.endDate='';
    $scope.warehouseId=$window.localStorage["warehouseId"];
    $scope.clientId=$window.localStorage["clientId"];
    $scope.startDate='';
    $scope.transferStockInDatas=[];
    $scope.transferConSumaryDatas=[];
    $scope.transferProConSumaryDatas=[];
    $scope.transferProConDetailsDatas=[];
    $scope.transferConDetailsDatas=[];
    $scope.selectTranferData='';

    $scope.transferStockInDataColumn= [
      { hidden: true, field: "id" },
      {
      field: "transferOutOrderNo",
      title: $translate.instant("transferStockIn_transferExternDn"),//外部调拨单号
      width: 100
    },
      {
        field: "transferInOrderNo",
        title: $translate.instant("transferStockIn_transferStocInOrderDn"),//调拨发货单号
        width: 100
      } ,
      {
        field: "transferInState",
        title: $translate.instant("transferStockIn_transferStocInState"),//状态
        width: 100
      }
      ,
      {
        field: "containerAmount",
        title: $translate.instant("transferStockIn_transferStocInContainerAmount"),//调拨箱数
        width: 100
      }
      ,
      {
        field: "trInOrderAmount",
        title: $translate.instant("transferStockIn_transferStocInGroodAmount"),//调拨数量
        width: 100
      }
      ,
      {
        field: "deliveryWarehouse",
        title: $translate.instant("transferStockIn_transferStocInDeliveWareHouse"),//发货库房
        width: 100
      } ,
      {
        field: "transferInMore",
        title: $translate.instant("transferStockIn_transferStocInMore"),//调拨多货
        width: 100
      } ,
      {
        field: "transferInLess",
        title: $translate.instant("transferStockIn_transferStocInLess"),//调拨少货
        width: 100
      } ,
      {
        field: "reveiceTime",
        title: $translate.instant("transferStockIn_transferStocInReceiveTime"),//接收时间
        width: 100
      } ,
      {
        field: "expectedTime",
        title: $translate.instant("transferStockIn_transferStocInPredictTime"),//预计到货时间
        width: 100
      } ,
      {
        field: "deliverGoodsTime",
        title: $translate.instant("transferStockIn_transferStocInDeloGoodTime"),//发货库房发货时间
        width: 100
      } ,
      {
        field: "closeTime",
        title: $translate.instant("transferStockIn_transferStocInCloseTime"),//关闭时间
        width: 100
      }
      ]

      $timeout(function () {
          $scope.initSearchParam();

          $("#startDatetimepicker").kendoDatePicker({
              format: "yyyy-MM-dd"

          });
          $("#endDatetimepicker").kendoDatePicker({
              format: "yyyy-MM-dd"
          });
          $scope.initGrid();
          $scope.gridClickInit()
          $scope.submitStatistics();


      });

      $scope.submitStatistics = function () {
          $scope.initSearchParam();

        transferStockInService.getTransferInOrders(function(data){
          $scope.transferStockInDatas=data;
          $('.transferDisplay').data("kendoGrid").setDataSource(new kendo.data.DataSource({
            data: $scope.transferStockInDatas
          }))

        }, $scope.searchParam);

      };
    $scope.specialSearch = function () {
      $scope.initSearchParam();

      transferStockInService.getConditionOrders(function(data){
        $scope.transferStockInDatas=data;


        $('.transferDisplay').data("kendoGrid").setDataSource(new kendo.data.DataSource({
          data: $scope.transferStockInDatas
        }))

      }, $scope.searchParam);

    };

    $scope.seachDetail=function(eve)
    {
      var s=$(eve.currentTarget);
      if(!s.hasClass('k-minus'))
      {
        $scope.initDetailSearchParam();
        if(!angular.isUndefined(s))
        {
          $scope.transferInOrderId=s.parent().next('td').text();
          $scope.initDetailSearchParam();
        }

        $scope.$apply(function()
        {
          transferStockInService.getTransferInContainerSurvey(function(data){

            $scope.transferConSumaryDatas=data;
            $('.transferContainerSumary').data("kendoGrid").setDataSource(new kendo.data.DataSource({
              data: $scope.transferConSumaryDatas
            }))
          }, $scope.DetailSearchParam);
          transferStockInService.getTransferInContainerDetail(function(data){

            $scope.transferConDetailsDatas=data;
            $('.transferContainerDetails').data("kendoGrid").setDataSource(new kendo.data.DataSource({
              data: $scope.transferConDetailsDatas
            }))
          }, $scope.DetailSearchParam);
          transferStockInService.getTransferInProConSurvey(function(data){

            $scope.transferProConSumaryDatas=data;
            $('.transferProbContainerSumary').data("kendoGrid").setDataSource(new kendo.data.DataSource({
              data: $scope.transferProConSumaryDatas
            }))
          }, $scope.DetailSearchParam);
          transferStockInService.getTransferInProConDetail(function(data){

            $scope.transferProConDetailsDatas=data;
            $('.transferProbContainerdetails').data("kendoGrid").setDataSource(new kendo.data.DataSource({
              data: $scope.transferProConDetailsDatas
            }))
          }, $scope.DetailSearchParam);


        });


      }




    }

      $scope.initGrid=function()
      {

        $(".transferDisplay").kendoGrid({
          allowCopy: true,
          columns: $scope.transferStockInDataColumn,
          dataSource:$scope.transferStockInDatas,
          height:500,
          selectable: "row",
          scrollable: true,
          sortable: true,
          pageable: true,
          detailTemplate: kendo.template($("#template").html()),
          detailInit: $scope.detailInit,
          dataBound: function(dataItem) {

            $scope.gridClickInit(dataItem);
            $('.k-plus').bind("click", $scope.seachDetail);

           // this.expandRow(this.tbody.find("tr.k-master-row").first());
          },

        });

      }



    $scope.activeTransfer=function()

    {
      var win=$("#mushinyWindow2").data("kendoWindow");
      transferStockInService.dialogMushiny2(win,{type:'activeTransfer',
      title:"请确认是否要激活调拨单",sureCall:function(){
          transferStockInService.activeTransferOrders(
            function(){
              win.close();
              $scope.submitStatistics();
            }
          ,{transferInOrderNo:$scope.selectTranferData.transferInOrderNo,
              warehouseId:$scope.warehouseId}
          )
      }});

    }
    $scope.closeTransfer=function()

    {
      var win=$("#mushinyWindow2").data("kendoWindow");
      transferStockInService.dialogMushiny2(win,{type:'closeTransfer',title:"请确认是否要关闭调拨单",sureCall:

        function(){
          transferStockInService.closeTransferOrders(
            function(){
              win.close();
              $scope.submitStatistics();
            }
            ,{transferInOrderNo:$scope.selectTranferData.transferInOrderNo,warehouseId:$scope.warehouseId}
          )
        }
      });
    }

    $scope.detailInit=function (e) {
      var detailRow = e.detailRow;

      detailRow.find(".tabstrip").kendoTabStrip({
        animation: {
          open: { effects: "expand:vertical" }
        }
      });

      detailRow.find(".transferContainerSumary").kendoGrid({
        dataSource: $scope.transferConSumaryDatas,
        height:500,
        scrollable: true,
        sortable: true,
        pageable: false,
        columns: [
          { field: "storageName", title:"容器号码", width: "70px" },
          { field: "systemAmount", title:"系统数量", width: "110px" },
          { field: "stowAmount", title:"收货上架数量" },
          { field: "differValue", title: "差值", width: "300px" },
          { field: "state", title: "状态", width: "300px" },
          { field: "receiptStation", title: "当前站台", width: "300px" },
          { field: "operatorName", title: "操作人员", width: "300px" }
        ]
      });

      detailRow.find(".transferProbContainerSumary").kendoGrid({
        dataSource: $scope.transferProConSumaryDatas,
        height:300,
        scrollable: true,
        sortable: true,
        pageable: false,
        columns: [
          { field: "storageName", title:"容器号码", width: "100px" },
          { field: "ShipCountry", title:"少货", width: "110px" },
          { field: "ShipAddress", title:"多货" ,width: "110px"},
          { field: "ShipName", title: "上报人员", width: "200px" }
        ]
      });


      detailRow.find(".transferProbContainerdetails").kendoGrid({
        dataSource: $scope.transferProConDetailsDatas,
        height:300,
        scrollable: true,
        sortable: true,
        pageable: false,
        columns: [
          { field: "storageName", title:"容器号码", width: "70px" },
          { field: "skuNo", title:"SKU ID", width: "110px" },
          { field: "goodsName", title:"商品名称", width: "300px" },
          { field: "amount", title: "数量", width: "100px" },
          { field: "problemType", title: "类型", width: "100px" },
          { field: "reporter", title: "上报人员", width: "100px" }
        ]
      });

      detailRow.find(".transferContainerDetails").kendoGrid({
        dataSource: $scope.transferConDetailsDatas,
        height:500,
        scrollable: true,
        sortable: true,
        pageable: false,
        columns: [
          { field: "storageName", title:"调拨箱号码", width: "200px" },
          { field: "itemNo", title:"唯一编号", width: "200px" },
          { field: "itemDataName", title:"商品名称",width:"300px" },
          { field: "systemAmount", title: "总数量", width: "100px" },
          { field: "stowAmount", title: "上架数量", width: "100px" },
          { field: "differValue", title: "差值", width: "100px" }
        ]
      });



    }



    $scope.gridClickInit=function(xx) {
      var grid = $(".transferDisplay").data("kendoGrid");

      $('.k-master-row').on('click', function (event) {

        $scope.$apply(function () {
       /*   $(grid.select()).on('click', function () {

            $scope.transferDisplayDataClick = true;
            $scope.selectTranferData = angular.isObject(grid.dataItem(grid.select()))?grid.dataItem(grid.select()):'';

          })*/

          $scope.transferDisplayDataClick = true;
          grid.select(event.currentTarget)
          $scope.selectTranferData = angular.isObject(grid.dataItem(grid.select()))?grid.dataItem(grid.select()):'';


        })
      })
    }



    /*$scope.gridClickInit=function() {
      var grid = $(".transferDisplay").data("kendoGrid");

      $('.k-master-row').on('click', function () {

        $scope.$apply(function () {
          $(grid.select()).on('click', function () {

              $scope.transferDisplayDataClick = true;
            $scope.selectTranferData = angular.isObject(grid.dataItem(grid.select()))?grid.dataItem(grid.select()):'';

          })

          $scope.transferDisplayDataClick = true;
          $scope.selectTranferData = angular.isObject(grid.dataItem(grid.select()))?grid.dataItem(grid.select()):'';


        })
      })
    }
*/

    $scope.hideButton=function(){

      $scope.transferDisplayDataClick=false;
    }

      $scope.initSearchParam=function()
      {
          $scope.searchParam={
              startDate:$scope.startDate,
              endDate:$scope.endDate,
              seachText:$scope.seachText,
              closedOrder:$scope.closedOrder,
              differOrder:$scope.differOrder,
              clientId:$scope.clientId,
              warehouseId:$scope.warehouseId}

      }
    $scope.initDetailSearchParam=function()
    {
      $scope.transferInOrderNo=angular.isObject($scope.selectTranferData)?$scope.selectTranferData.transferInOrderNo:'';
      $scope.DetailSearchParam={
        transferInOrderId:$scope.transferInOrderId,
        clientId:$scope.clientId,
        warehouseId:$scope.warehouseId}

    }



  });
})();