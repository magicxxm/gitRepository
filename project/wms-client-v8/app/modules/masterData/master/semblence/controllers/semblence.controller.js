/**
 * create by preston.zhang 2017.6.5
 */
(function () {
  'use strict';

  angular.module('myApp').controller("semblenceCtl", function ($scope, $rootScope, $window, $state,semblenceService,commonService) {
      $window.localStorage["currentItem"] = "semblence";
      var columns = [
          {field: "semblenceId",template: "<a ui-sref='main.semblenceRead({id:dataItem.id})'>#: semblence # </a>", headerTemplate: "<span translate='SEMBLENCE'></span>"},
          {field: "clientName", headerTemplate: "<span translate='CLIENT'></span>"}
      ];

      semblenceService.getSemblences(function (data) {
          $scope.semblenceGridOptions = commonService.gridMushiny({columns: columns, dataSource: data});
          // $("#semblenceGrid").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: data}));
      });
  }).controller("semblenceCreateCtl", function ($scope, $state,semblenceService){
      semblenceService.getClient(function (data) {
          console.log("clients--->",data);
          data.push({"id":"ALL", "name":"ALL"});
          $scope.clientDataSource = data;
      });
      // console.log("clients-->",clientDataSource);
      // clientDataSource.push({"id":"ALL", "name":"ALL"});
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
          semblenceService.createSemblence({
          "clientId": $scope.clientId.id,
          "semblence": $scope.semblence,
        }, function () {
          $state.go("main.semblence");
         });
      }
    };
  }).controller("semblenceReadCtl", function ($scope, $state, $stateParams,semblenceService,masterService){
      console.log("$stateParams.id---->"+$stateParams.id);
    masterService.read("semblence", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  }).controller("semblenceUpdateCtl", function ($scope, $state, $stateParams,semblenceService,masterService){
      var semblenceId = null;
      masterService.read("semblence", $stateParams.id, function(data){
          semblenceId = data.semblenceId;
          console.log("data",data);
          console.log("data.id",data.semblenceId);
          $scope.clientId = data.clientId;
          $scope.semblence = data.semblence;
      });
      // 修改
      $scope.validate = function (event) {
          event.preventDefault();
          if ($scope.validator.validate()) {
              semblenceService.updateSemblence({
                  "clientId": $scope.clientId.id,
                  "semblence": $scope.semblence,
                  "semblenceId":semblenceId
              }, function () {
                  $state.go("main.semblence");
              });
          }
      };
  });
})();