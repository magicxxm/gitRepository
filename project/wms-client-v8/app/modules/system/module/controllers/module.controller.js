/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("moduleCtl", function ($scope, $rootScope, $window, commonService, systemService) {

    $window.localStorage["currentItem"] = "module";

    var columns = [
      {field: "name",  template: "<a ui-sref='main.moduleRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"},
      {field: "moduleType", headerTemplate: "<span translate='MODULE_TYPE'></span>"},
      //{field: "rfActive", headerTemplate: "<span translate='RF_ACTIVE'></span>"},
      //{field: "dkActive", headerTemplate: "<span translate='DK_ACTIVE'></span>"},
      //{field: "formPath", headerTemplate: "<span translate='FORM_PATH'></span>"},
      //{field: "reportFilename", headerTemplate: "<span translate='REPORT_FILENAME'></span>"},
      //{field: "reportType", headerTemplate: "<span translate='REPORT_TYPE'></span>"},
      //{field: "printPreview", headerTemplate: "<span translate='PRINT_PREVIEW'></span>"},
      //{field: "printDialog", headerTemplate: "<span translate='PRINT_DIALOG'></span>"},
      //{field: "printCopies", headerTemplate: "<span translate='PRINT_COPIES'></span>"},
      //{field: "title", headerTemplate: "<span translate='TITLE'></span>"},
      {field: "resourceKey", headerTemplate: "<span translate='RESOURCE_KEY'></span>"}
    ];
    $scope.moduleGridOptions = commonService.gridMushiny({columns: columns, dataSource: systemService.getGridDataSource('module')});

    $rootScope.moduleTypeSource = systemService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "MODULE_TYPE"}
    });
  }).controller("moduleCreateCtl", function ($scope, $state, systemService){
    $scope.rfActive = "false"; $scope.dkActive = "true";
    $scope.printPreview = "true"; $scope.printDialog = "false";
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.create("module", {
           "name": $scope.name,
           "description": $scope.description,
           "moduleType": $scope.moduleType.selectionValue,
           "rfActive": $scope.rfActive,
           "dkActive": $scope.dkActive,
           "formPath": $scope.formPath,
           "reportFilename": $scope.reportFilename,
           "reportType": $scope.reportType,
           "printPreview": $scope.printPreview,
           "printDialog": $scope.printDialog,
           "printCopies": $scope.printCopies,
           "title": $scope.title,
           "resourceKey": $scope.resourceKey
         }, function(){
          $state.go("main.module");
        });
      }
    }
  }).controller("moduleUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("module", $stateParams.id, function (data) {
      for (var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = (k=="moduleType"? systemService.toMap(data[k]): data[k]);
      }
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.update("module", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "moduleType": $scope.moduleType.selectionValue,
          "rfActive": $scope.rfActive,
          "dkActive": $scope.dkActive,
          "formPath": $scope.formPath,
          "reportFilename": $scope.reportFilename,
          "reportType": $scope.reportType,
          "printPreview": $scope.printPreview,
          "printDialog": $scope.printDialog,
          "printCopies": $scope.printCopies,
          "title": $scope.title,
          "resourceKey": $scope.resourceKey
        }, function(){
          $state.go("main.module");
        });
      }
    }
  }).controller("moduleReadCtl", function ($scope, $stateParams, systemService) {
    systemService.read("module", $stateParams.id, function (data) {
      for (var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = (k=="moduleType"? systemService.toMap(data[k]): data[k]);
      }
    });
  });
})();
