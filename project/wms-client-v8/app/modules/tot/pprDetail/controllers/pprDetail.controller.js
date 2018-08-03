(function () {
  'use strict';
  angular.module('myApp').controller("pprDetailCtl", function ($scope,$timeout, $window, commonService, pprStatisticsService,totService,
                                                               pprStatisticsDetailService,totStatisticsService,$rootScope) {
      //选择时间事件
      // $scope.detail_warehouseId = pprStatisticsDetailService.warehouseId;
      // $scope.detail_clientId =pprStatisticsDetailService.clientId;
      $scope.detail_category=pprStatisticsDetailService.categoryName;
      $scope.timeType=pprStatisticsDetailService.timeType
      if($scope.timeType==1)
      {
          $scope.dayTime=pprStatisticsDetailService.dayDate;
          $scope.dateType=0;

      }else if($scope.timeType==2){

          $scope.weekTempDate=pprStatisticsDetailService.dayDate;
          $scope.weekSrcDate=pprStatisticsDetailService.weekSrcDate;
          $scope.dateType=2;
      }else if($scope.timeType==3)
      {
          $scope.monthDate=pprStatisticsDetailService.dayDate;
          $scope.dateType=1;
      }else {
          $scope.startDateTime=pprStatisticsDetailService.startDate;
          $scope.endDateTime=pprStatisticsDetailService.endDate;
          $scope.dateType=3;
      };
      // 无记录or有记录(直接工作or间接工作)
      $scope.jobTypeFlag = "";
      $scope.pprDetailTotalSource= [];
      $scope.pprIndirectDetailTotalSource= [];
      $scope.pprDetailDataSource= [];
      $scope.pprIndirectDetailDataSource= [];

      $scope.pprDetailTotalColumn= [{
          title: "工作--汇总",
          columns: [{
          field: "jobCode",
          title: "工作条码",
          width: 70
          },
          {
              field: "jobName",
              title: "工作名",
              width: 70
          },
          {
              field: "size",
              title: "尺寸",
              width: 70
          },
          {
              field: "hours",
              title: "时间(小时)",
              width: 70
          },
          {
              title: "商品",
              columns: [{
                  field: "times",
                  title: "扫描商品次数",
                  width: 100
              },{
                  field: "unitHourTimes",
                  title: "扫描商品效率",
                  width: 100
              },{
                  field: "unitAmount",
                  title: "一次扫描操作商品数量",
                  width: 130
              },{
                  field: "amount",
                  title: "商品数量",
                  width: 100
              },{
                  field: "unitHourAmount",
                  title: "操作商品效率",
                  width: 100
              }]
          }]
      }]
      $scope.pprIndirectDetailTotalColumn= [{
          title: "工作--汇总",
          columns: [{
              field: "jobCode",
              title: "工作条码",
              width: 70
          },
              {
                  field: "jobName",
                  title: "工作名",
                  width: 70
              },
              {
                  field: "size",
                  title: "尺寸",
                  width: 70
              },
              {
                  field: "hours",
                  title: "时间(小时)",
                  width: 70
              },
              {
                  title: "商品",
                  columns: [{
                      field: "amount",
                      title: "商品数量",
                      width: 100
                  },{
                      field: "unitHourAmount",
                      title: "操作商品效率",
                      width: 100
                  }]
              }]
      }]

      $scope.pprDataBound= function () {
          $.each([1,2], function (index, value) {

              var item = $('#pprDetailTotalGrid>.k-grid-content>table');
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

      $scope.pprIndirectDataBound= function () {
          $.each([1,2], function (index, value) {

              var item = $('#pprindirectdetailtotalgrid>.k-grid-content>table');
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
          $("#selectCategory").data("kendoComboBox").value(pprStatisticsDetailService.categoryName);
          $scope.setDateType();
          totStatisticsService.getWarehouse(function (data1) {
              $scope.warehouseId = data1[0];
              totStatisticsService.getClientByCurrentWarehouse(function (data2) {
                  $scope.clientId = data2[0];
                  $scope.submitStatistics();
              })
          });
      });

      $scope.setDateType=function()
      {
          if($scope.timeType==1) {
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

      //sumbit按钮事件
      $scope.submitStatistics = function () {
          $scope.searchTimeInit();
          //格式时间参数
          $scope.formateDate();
          $scope.detail_warehouseId = currentWarehouse.value();
          $scope.detail_clientId =  currentClient.value();
          var params={
              category:$scope.detail_category,
              warehouseId:$scope.detail_warehouseId,
              clientId:$scope.detail_clientId,
              dayDate:$scope.dayDate,
              startDate:$scope.startDate,
              endDate:$scope.endDate,
              userName:"mengmengsun",
              dateType:$scope.dateType
          }
          console.info(params);
          //初始化查询时间标志位
          pprStatisticsService.getRecordsForPprDetail(function (data) {
              console.info(data);
              if(data!=""){
                  if("直接工作"==data[0][0].jobType) {
                      $scope.jobTypeFlag = "direct";
                      $scope.pprDetailTotalSource=data[0];
                      if($.isEmptyObject($scope.pprDetailTotalObject))
                      {
                          $scope.pprDetailTotalObject=$("#pprDetailTotalGrid").kendoGrid({
                              scrollable: true,
                              // height:600,
                              columns:$scope.pprDetailTotalColumn,
                              dataSource:$scope.pprDetailTotalSource,
                              dataBound:$scope.pprDataBound
                          });
                      }
                      else{
                          $scope.pprDetailTotalObject.data('kendoGrid').setDataSource(new kendo.data.DataSource({data:$scope.pprDetailTotalSource}));
                      }
                      data.splice(0,1);
                      $scope.pprDetailDataSource= data;
                  }
                  else {
                      $scope.jobTypeFlag = "inDirect";
                      $scope.pprIndirectDetailDataSource=data[0];
                      if($.isEmptyObject($scope.pprIndirectDetailTotalObject))
                      {
                          $scope.pprIndirectDetailTotalObject=$("#pprIndirectDetailTotalGrid").kendoGrid({
                              scrollable: true,
                              // height:600,
                              columns:$scope.pprIndirectDetailTotalColumn,
                              dataSource:$scope.pprIndirectDetailDataSource,
                              dataBound:$scope.pprIndirectDataBound
                          });
                      }
                      else{
                          $scope.pprIndirectDetailTotalObject.data('kendoGrid').setDataSource(new kendo.data.DataSource({data:$scope.pprIndirectDetailDataSource}));
                      }
                      data.splice(0,1);
                      $scope.pprIndirectDetailDataSource= data;
                  }
              }
              else {
                  $scope.jobTypeFlag = "nothing";
              }
          },params);
      };

      $scope.setState=function(eve){
          var tat=eve.target;
          if(tat.id=='dayState')
          {
              $scope.timeType=1;
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

      //项目
      $scope.selectCategoryOptions = getComboxOption({
          key: "getAllJobcategory",
          change: function () {
              if(null==$scope.selectCategory) $scope.detail_category = null;
              else $scope.detail_category = $scope.selectCategory.name;
          }
      });
      //初始仓库
      function getComboxOption(options){
          return {
              dataSource: options.key? totService.getDataSource({key: options.key}): [],
              dataTextField: "name",
              dataValueField: "id",
              filter: "contains",
              change: function(){
                  options.change && options.change();
              }
          };
      }

      if(pprStatisticsDetailService.warehouseId=='') {
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
      }
      else {
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
              // index: 0
          });
          $("#selectClientInfos").data("kendoComboBox").value(pprStatisticsDetailService.clientId);
      }
      var currentWarehouse =$("#selectWarehouse").data("kendoComboBox")
      var currentClient = $("#selectClientInfos").data("kendoComboBox")
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

      $scope.$on('$destroy', function(){
          // do cleanup
          pprStatisticsDetailService.timeType=1;
          pprStatisticsDetailService.dayDate=pprStatisticsDetailService.format('yyyy-MM-dd',new Date());
          pprStatisticsDetailService.startDate='';
          pprStatisticsDetailService.endDate='';
          pprStatisticsDetailService.employeeName='';
          pprStatisticsDetailService.employeeCode='';
          pprStatisticsDetailService.warehouseId='';
          pprStatisticsDetailService.clientId='';
          pprStatisticsDetailService.categoryName='Each Receive To Stow';
          pprStatisticsDetailService.weekSrcDate='';
      });
})
})
  ();