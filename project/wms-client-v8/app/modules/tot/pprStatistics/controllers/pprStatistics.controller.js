(function () {
  'use strict';
  angular.module('myApp').controller("pprStatisticsCtl", function ($scope,$timeout,$state, $window, commonService, pprStatisticsService,totService,
                                                                   pprStatisticsDetailService,totStatisticsService) {
      //选择时间事件
      $scope.timeType=1;
      $scope.dayTime=pprStatisticsDetailService.format('yyyy-MM-dd',new Date());
      $scope.detail_warehouseId = null;
      $scope.detail_clientId =null;

      $scope.turnPage = function(dataItem) {
          pprStatisticsDetailService.warehouseId=$scope.detail_warehouseId;
          pprStatisticsDetailService.clientId=$scope.detail_clientId;
          pprStatisticsDetailService.categoryName=dataItem.categoryName;
          pprStatisticsDetailService.dayDate=$scope.dayDate;
          pprStatisticsDetailService.timeType=$scope.timeType;//日期类型
          pprStatisticsDetailService.startDate=$scope.startDate;
          pprStatisticsDetailService.endDate=$scope.endDate;
          pprStatisticsDetailService.weekSrcDate =$scope.weekSrcDate;//周显示字符串
          $state.go("main.pprdetail");
      }

      $scope.pprStatisticsDataSource= [];

      $scope.pprStatisticsDataColumn= [
          {
          field: "mainProcesses",
          title: "Main Processes",
          width: 70
      },
          {
          field: "coreProcesses",
          title: "Core Processes",
          width: 70
      },
          {
              field: "categoryName",
              title: "项目",
              width: 90
          },
          {
              field: "lineItems",
              title: "Line Items",
              template: "<a id='lineItems' style='cursor:pointer' ng-click='turnPage(dataItem)'>#: lineItems # </a>",
              width: 120
          },
          {
              title: "Actual",
              columns: [{
                  field: "unit",
                  title: "Unit",
                  width: 100
              },{
                  field: "amount",
                  title: "Amount",
                  width: 100
              },{
                  field: "hours",
                  title: "Hours",
                  width: 100

              },{
                  field: "rates",
                  title: "Rate",
                  width: 100
              }]
          },
          {
              title: "Plan",
              columns: [{
                  field: "planRate",
                  title: "Rate",
                  width: 100
              },{
                  field: "planHours",
                  title: "Hours",
                  width: 100
              },{
                  field: "increment",
                  title: "∆ to Plan",
                  width: 100

              },{
                  field: "quotient",
                  title: "% to Plan",
                  width: 100
              }]
          }]

      $scope.pprDataBound= function () {
          $.each([1,2,3], function (index, value) {

              var item = $('#pprStatisticsDataGrid>.k-grid-content>table')
              var dimension_col = value;
              var rowspan = 1;
              var first_instance = null;
              var sipTr = -1;
              $(item).find('tr').each(function (i, trItem) {
                  if (i > sipTr) {
                      var current_td = $(this).find('td:nth-child(' + dimension_col + ')');
                      if (first_instance == null) {
                          first_instance = current_td;
                          rowspan = 1;
                          sipTr++;

                      } else if (current_td.text() == first_instance.text()) {
                          rowspan++;
                          sipTr++;
                         // $(current_td).css("visibility","hidden");
                         current_td.hide();
                          $(trItem).nextAll().each(function () {
                              current_td = $(this).find('td:nth-child(' + dimension_col + ')');
                              if (current_td.text() == first_instance.text()) {
                                  current_td.hide();
                                  rowspan++;
                                  sipTr++;
                              }else {
                                  return false;
                              }
                          });

                          first_instance.attr('rowspan', rowspan);

                          rowspan = 1;

                      } else {
                          rowspan = 1;
                          sipTr++;
                          first_instance = current_td;
                      }
                  }

              });
              //    dimension_col++;
          });
      }

      $timeout(function(){
          $scope.weekTempDate="";
          $("#weekDatetimepicker").kendoDatePicker({
              format: "yyyy-MM-dd",
              close:function()
              {
                  pprStatisticsService.getWeekOfMouth(
                      function (data){

                          $scope.weekSrcDate=data.convertDate;
                          $scope.weekTempDate=data.srcDate;
                      },$scope.weekSrcDate
                  );
              }
          });
          $("#startDatetimepicker").kendoDateTimePicker({
              format: "yyyy-MM-dd HH:mm:ss",
          });
          $("#monthDateTimepicker").kendoDatePicker({

              start: "year",
              depth: "year",
              format: "yyyy-MM"

          });
          $("#dayDatetimepicker").kendoDatePicker({

              start: "year",
              depth: "month",
              format: "yyyy-MM-dd"

          });
          $("#endDatetimepicker").kendoDateTimePicker({
              format: "yyyy-MM-dd HH:mm:ss"

          });
          $scope.initGrid();
          totStatisticsService.getWarehouse(function (data1) {
              $scope.warehouseId = data1[0];
              totStatisticsService.getClientByCurrentWarehouse(function (data2) {
                  $scope.clientId = data2[0];
                  $scope.submitStatistics();
              })
          });

      });

      //sumbit按钮事件
      $scope.submitStatistics = function () {
          //初始化查询时间标志位
          $scope.searchTimeInit();
          //格式时间参数
          $scope.formateDate();
          $scope.detail_warehouseId=currentWarehouse.value();
          $scope.detail_clientId=currentClient.value();
          var params={
              warehouseId:$scope.detail_warehouseId,
              clientId:$scope.detail_clientId,
              dayDate:$scope.dayDate,
              startDate:$scope.startDate,
              endDate:$scope.endDate,
              userName:"mengmengsun",
              dateType:$scope.dateType
          }
          console.info(params);
          pprStatisticsService.getPprStatisticsData(function (data) {
              $scope.pprStatisticsDataSource=data;
              var pprStatisticsDataGridId = $("#pprStatisticsDataGrid").data("kendoGrid");
              pprStatisticsDataGridId.setOptions({
                      scrollable: true,
                      editable: false,
                      // height:600,
                      columns:$scope.pprStatisticsDataColumn,
                      dataSource:$scope.pprStatisticsDataSource,
                      dataBound:$scope.pprDataBound
              });
          },params);
      };

      $scope.initGrid=function()
      {
          $scope.pprStatisticsDataGridOptions = totService.reGrids([], $scope.pprStatisticsDataColumn,
              $(document.body).height() - 192,$scope.pprDataBound);
      }

      $scope.setState=function(eve){
          var tat=eve.target;
          if(tat.id=='dayState')
          {
              $scope.timeType=1;
              // $scope.weekDateState=false;
              // $scope.monthDateState=false;
              $scope.weekSrcDate="";
              $scope.weekTempDate="";
              $scope.monthDate="";
              $scope.startDateTime="";
              $scope.endDateTime="";
          }else if(tat.id=='weekDateState')
          {
              $scope.timeType=2;
              // $scope.weekDateState=true;
              // $scope.monthDateState=false;
              $scope.dayTime="";
              $scope.monthDate="";
              $scope.startDateTime="";
              $scope.endDateTime="";
          }
          else if(tat.id=='monthDateState')
          {
              $scope.timeType=3;
              // $scope.weekDateState=false;
              // $scope.monthDateState=true;
              $scope.dayTime="";
              $scope.weekSrcDate="";
              $scope.weekTempDate="";
              $scope.startDateTime="";
              $scope.endDateTime="";
          }else{

              $scope.timeType=4;
              // $scope.weekDateState=false;
              // $scope.monthDateState=false;
              $scope.dayTime="";
              $scope.weekSrcDate="";
              $scope.weekTempDate="";
              $scope.monthDate="";
          }

      }

      $("#selectWarehouse").kendoComboBox({
          dataSource: totService.getDataSource({key: "getWarehouse"}),
          dataTextField: "name",
          dataValueField: "id",
          filter: "contains",
          suggest: true,
          index: 0
      });

      $("#selectClientInfos").kendoComboBox({
          dataSource: totService.getDataSource({key: "getClientByCurrentWarehouse"}),
          dataTextField: "name",
          dataValueField: "id",
          filter: "contains",
          suggest: true,
          index: 0
      });
      var currentWarehouse =$("#selectWarehouse").data("kendoComboBox")
      var currentClient = $("#selectClientInfos").data("kendoComboBox")

      // //仓库
      // $scope.selectWarehouseOptions = getComboxOption({
      //     key: "getWarehouse",
      // });
      // //客户
      // $scope.selectClientInfoOptions = getComboxOption({
      //     key: "getClientByCurrentWarehouse",
      //     change: function(){
      //         if(null==$scope.selectClientInfos) $scope.detail_clientId = null;
      //         else $scope.detail_clientId = $scope.selectClientInfos.id;
      //     }
      // });
      // //初始仓库
      // function getComboxOption(options){
      //     return {
      //         dataSource: options.key? totService.getDataSource({key: options.key}): [],
      //         dataTextField: "name",
      //         dataValueField: "id",
      //         filter: "contains",
      //         index: 0,
      //         change: function(){
      //             options.change && options.change();
      //         }
      //     };
      // }

      $scope.formateDate=function()
      {
          $scope.dayDate=kendo.format("{0:yyyy-MM-dd}", $scope.dayDate);
          $scope.startDate=kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", $scope.startDate);
          $scope.endDate=kendo.format("{0:yyyy-MM-ddTHH:mm:ss}", $scope.endDate);

      }
      $scope.searchTimeInit = function()
      {
          if($scope.timeType==1)
          {
              $scope.dayDate=$scope.dayTime
              $scope.startDate="";
              $scope.endDate="";
              $scope.dateType=0;

          }else if($scope.timeType==2){

              $scope.dayDate=$scope.weekTempDate;
              $scope.startDate="";
              $scope.endDate="";
              $scope.dateType=2;
          }else if($scope.timeType==3)
          {
              $scope.dayDate=$scope.monthDate;
              $scope.startDate="";
              $scope.endDate="";
              $scope.dateType=1;
          }else {
              $scope.dayDate="";
              $scope.startDate=$scope.startDateTime;
              $scope.endDate=$scope.endDateTime;
              $scope.dateType=3;
          }

      };
})
})
  ();