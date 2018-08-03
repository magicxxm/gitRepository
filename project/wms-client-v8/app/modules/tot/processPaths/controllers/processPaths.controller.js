/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("processPathsCtl", function ($scope, $rootScope, $window, commonService, totService) {

    $window.localStorage["currentItem"] = "processPaths";

    $rootScope.collateTypeSource = ["AUTO", "MANUAL"];
    $rootScope.regenerateShortedPicksSource = ["Hot Pick", "Back To Process"];
    $rootScope.collateDocumentsSource = ["Batch Cover", "Face Label", "Shipment Detail"];
    $rootScope.allowedContainerTypesSource = ["Tote", "Cart", "Pallet"];
    $rootScope.processPathTypeSource = totService.getDataSource({key: "getProcessPathType", text: "name", value: "id"});
    $rootScope.pickStationTypeSource = totService.getDataSource({key: "getPickStationType", text: "name", value: "id"});
    $rootScope.rebinStationTypeSource = totService.getDataSource({key: "getRebinStationType", text: "name", value: "id"});
    $rootScope.rebinWallTypeSource = totService.getDataSource({key: "getRebinWallType", text: "name", value: "id"});
    $rootScope.packingStationTypeSource = totService.getDataSource({key: "getPackingStationType", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.processPathRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "processPathType.name",template: "<a ui-sref='main.process_path_type'>#: processPathType.name # </a>", headerTemplate: "<span translate='PROCESS_PATH_TYPE'></span>"},
      //{field: "sortCode", headerTemplate: "<span translate='SORT_CODE'></span>"},
      //{field: "regenerateShortedPicks", headerTemplate: "<span translate='REGENERATE_SHORTED_PICKS'></span>"},
      //{field: "minShipmentsPerBatch", headerTemplate: "<span translate='MIN_SHIPMENTS_PER_BATCH'></span>"},
      //{field: "maxShipmentsPerBatch", headerTemplate: "<span translate='MAX_SHIPMENTS_PER_BATCH'></span>"},
      //{field: "minItemsPerBatch", headerTemplate: "<span translate='MIN_ITEMS_PER_BATCH'></span>"},
      //{field: "maxItemsPerBatch", headerTemplate: "<span translate='MAX_ITEMS_PER_BATCH'></span>"},
      {field: "collateType", headerTemplate: "<span translate='COLLATE_TYPE'></span>"},
      //{field: "collateDocuments", headerTemplate: "<span translate='COLLATE_DOCUMENTS'></span>"},
      {field: "toteBufferLimited", headerTemplate: "<span translate='TOTE_BUFFER_LIMITED'></span>"},
      //{field: "rejectReasons", headerTemplate: "<span translate='REJECT_REASONS'></span>"},
      //{field: "allowedPickTypes", headerTemplate: "<span translate='ALLOWED_PICK_TYPES'></span>"},
      //{field: "allowedContainerTypes", headerTemplate: "<span translate='ALLOWED_CONTAINER_TYPES'></span>"},
      {field: "pickDestination.name",template: "<a ui-sref='main.pick_station_type'>#: pickDestination.name # </a>", headerTemplate: "<span translate='PICK_DESTINATION'></span>"},
      {field: "rebinDestination.name",template: "<a ui-sref='main.rebin_station_type'>#: rebinDestination.name # </a>", headerTemplate: "<span translate='REBIN_DESTINATION'></span>"},
      {field: "rebinWallType.name",template: "<a ui-sref='main.rebin_wall_type'>#: rebinWallType.name # </a>", headerTemplate: "<span translate='REBIN_WALL_TYPE'></span>"},
      {field: "packDestination.name",template: "<a ui-sref='main.packing_station_type'>#: packDestination.name # </a>", headerTemplate: "<span translate='PACKING_STATION_TYPE'></span>"},
      //{field: "laborUnitClass", headerTemplate: "<span translate='LABOR_UNIT_CLASS'></span>"},
      //{field: "weightLimit", headerTemplate: "<span translate='WEIGHT_LIMIT'></span>"},
      //{field: "scanBinFirst", headerTemplate: "<span translate='SCAN_BIN_FIRST'></span>"},
      //{field: "colorBasedBinPicking", headerTemplate: "<span translate='COLOR_BASED_BIN_PICKING'></span>"},
      //{field: "colorBasedItemPicking", headerTemplate: "<span translate='COLOR_BASED_ITEM_PICKING'></span>"},
      //{field: "oneItemPerTote", headerTemplate: "<span translate='ONE_ITEM_PER_TOTE'></span>"},
      //{field: "requireNewToteOnPickAreaChange", headerTemplate: "<span translate='REQUIRE_NEW_TOTE_ON_PICK_AREA_CHANGE'></span>"},
      //{field: "idleTimeoutInSeconds", headerTemplate: "<span translate='IDLE_TIMEOUT_IN_SECONDS'></span>"},
      //{field: "toteRescan", headerTemplate: "<span translate='TOTE_RESCAN'></span>"},
      //{field: "pickToContainerImage", headerTemplate: "<span translate='PICK_TO_CONTAINER_IMAGE'></span>"},
      //{field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.processPathsGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("processPaths")});

  }).controller("processPathsCreateCtl", function ($scope, $state, totService){
    $scope.scanBinFirst = "false"; $scope.colorBasedBinPicking = "false"; $scope.colorBasedItemPicking = "false";
    $scope.oneItemPerTote = "false"; $scope.requireNewToteOnPickAreaChange = "false";

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.create("processPaths", {
          "name": $scope.name,
          "processPathTypeId": $scope.processPathType? $scope.processPathType.id: null,
          "sortCode": $scope.sortCode,
          "regenerateShortedPicks": $scope.regenerateShortedPicks,
          "minShipmentsPerBatch": $scope.minShipmentsPerBatch,
          "maxShipmentsPerBatch": $scope.maxShipmentsPerBatch,
          "minItemsPerBatch": $scope.minItemsPerBatch,
          "maxItemsPerBatch": $scope.maxItemsPerBatch,
          "collateType": $scope.collateType,
          "collateDocuments": $scope.collateDocuments.join(","),
          "toteBufferLimited": $scope.toteBufferLimited,
          "rejectReasons": $scope.rejectReasons,
          "allowedPickTypes": $scope.allowedPickTypes,
          "allowedContainerTypes": $scope.allowedContainerTypes,
          "pickDestinationId": $scope.pickDestination? $scope.pickDestination.id: null,
          "rebinDestinationId": $scope.rebinDestination? $scope.rebinDestination.id: null,
          "rebinWallTypeId": $scope.rebinWallType? $scope.rebinWallType.id: null,
          "packDestinationId": $scope.packDestination? $scope.packDestination.id: null,
          "laborUnitClass": $scope.laborUnitClass,
          "weightLimit": $scope.weightLimit,
          "scanBinFirst": $scope.scanBinFirst,
          "colorBasedBinPicking": $scope.colorBasedBinPicking,
          "colorBasedItemPicking": $scope.colorBasedItemPicking,
          "oneItemPerTote": $scope.oneItemPerTote,
          "requireNewToteOnPickAreaChange": $scope.requireNewToteOnPickAreaChange,
          "idleTimeoutInSecond": $scope.idleTimeoutInSecond,
          "toteRescan": $scope.toteRescan,
          "pickToContainerImage": $scope.pickToContainerImage,
          "description": $scope.description
        }, function () {
          $state.go("main.process_paths");
        });
      }
    };
  }).controller("processPathsUpdateCtl", function ($scope, $state, $stateParams, totService){
    totService.read("processPaths", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = (k === "collateDocuments"? data[k].split(","): data[k]);
      }
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("processPaths", {
          "id": $scope.id,
          "name": $scope.name,
          "processPathTypeId": $scope.processPathType? $scope.processPathType.id: null,
          "sortCode": $scope.sortCode,
          "regenerateShortedPicks": $scope.regenerateShortedPicks,
          "minShipmentsPerBatch": $scope.minShipmentsPerBatch,
          "maxShipmentsPerBatch": $scope.maxShipmentsPerBatch,
          "minItemsPerBatch": $scope.minItemsPerBatch,
          "maxItemsPerBatch": $scope.maxItemsPerBatch,
          "collateType": $scope.collateType,
          "collateDocuments": $scope.collateDocuments.join(","),
          "toteBufferLimited": $scope.toteBufferLimited,
          "rejectReasons": $scope.rejectReasons,
          "allowedPickTypes": $scope.allowedPickTypes,
          "allowedContainerTypes": $scope.allowedContainerTypes,
          "pickDestinationId": $scope.pickDestination? $scope.pickDestination.id: null,
          "rebinDestinationId": $scope.rebinDestination? $scope.rebinDestination.id: null,
          "rebinWallTypeId": $scope.rebinWallType? $scope.rebinWallType.id: null,
          "packDestinationId": $scope.packDestination? $scope.packDestination.id: null,
          "laborUnitClass": $scope.laborUnitClass,
          "weightLimit": $scope.weightLimit,
          "scanBinFirst": $scope.scanBinFirst,
          "colorBasedBinPicking": $scope.colorBasedBinPicking,
          "colorBasedItemPicking": $scope.colorBasedItemPicking,
          "oneItemPerTote": $scope.oneItemPerTote,
          "requireNewToteOnPickAreaChange": $scope.requireNewToteOnPickAreaChange,
          "idleTimeoutInSecond": $scope.idleTimeoutInSecond,
          "toteRescan": $scope.toteRescan,
          "pickToContainerImage": $scope.pickToContainerImage,
          "description": $scope.description
        }, function () {
          $state.go("main.process_paths");
        });
      }
    };
  }).controller("processPathsReadCtl", function ($scope, $stateParams, totService){
    totService.read("processPaths", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });
  });
})();