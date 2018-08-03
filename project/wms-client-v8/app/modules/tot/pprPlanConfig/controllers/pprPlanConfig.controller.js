(function () {
  'use strict';
  angular.module('myApp').controller("pprPlanConfigCtl", function ($scope,$timeout,$state, $window, commonService,
                                                                   pprPlanConfigService,totService) {

      $window.localStorage["currentItem"] = "pprPlanConfig";
      var pprPlanConfigDataColumn= [
          {
              field: "mainProcesses",
              headerTemplate: "<span translate='Main Processes'></span>"
          },
          {
              field: "coreProcesses",
              headerTemplate: "<span translate='Core Processes'></span>"
          },
          {
              field: "categoryName",
              title: "项目"
          },
          {
              field: "lineItems",
              headerTemplate: "<span translate='Line Items'></span>"
          },
          {
              field: "planRate",
              title: "Plan Rate"
          }]

      $scope.pprDataBound= function () {
          $.each([1,2,3], function (index, value) {

              var item = $('#pprPlanConfigGrid>.k-grid-content>table')
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

      pprPlanConfigService.getPprPlanConfigData(function (data) {

          // $("#pprPlanConfigGrid").kendoGrid
          $scope.pprPlanConfigGridOptions = ({
              dataSource: data,
              selectable: "row",
              height: $(document.body).height() - 191,
              sortable: true,
              scrollable: true,
              pageable: {
                  numeric: true,
              },
              columns: pprPlanConfigDataColumn,
              dataBound:$scope.pprDataBound
          });
      })

}).controller("pprPlanConfigUpdateCtl", function ($scope, $state, $stateParams, totService){

      totService.read("pprplanconfig", $stateParams.id, function(data){
          for(var k in data) $scope[k] = data[k];
      });
      $scope.validate = function (event) {
          event.preventDefault();
          if ($scope.validator.validate()) {
              totService.update("pprplanconfig", {
                  "id": $scope.id,
                  "mainProcesses": $scope.mainProcesses,
                  "coreProcesses": $scope.coreProcesses,
                  "categoryName": $scope.categoryName,
                  "lineItems": $scope.lineItems,
                  "planRate": $scope.planRate
              }, function () {
                  $state.go("main.pprplanconfig");
              });
          }
      };
  })
})
  ();