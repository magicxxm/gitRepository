/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receiveCategoryRuleCtl", function ($scope, $rootScope, $window, commonService, masterService){

    $window.localStorage["currentItem"] = "receiveCategoryRule";
    // 列
    var columns = [
      {field: "name", width: 150, headerTemplate: "<span translate='NAME'></span>"},
      {field: "operator", width: 300, headerTemplate: "<span translate='OPERATOR'></span>", template: function(item){
        return item.operators? item.operators.join(","): "";
      }},
      {field: "decisionKey", width: 100, headerTemplate: "<span translate='DECISION_KEY'></span>"},
      {field: "comparisonType", width: 120, headerTemplate: "<span translate='COMPARISON_TYPE'></span>"},
      {field: "compKey", width: 100, headerTemplate: "<span translate='COMP_KEY'></span>"}
    ];
    $scope.receiveCategoryRuleGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("receiveCategoryRule")});

  }).controller("receiveCategoryRuleUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("receiveCategoryRule", $stateParams.id, function(data){
      for(var k in data){
        if(k === "operators")
          $scope["operator"] = data[k].join(",");
        else
          $scope[k] = data[k];
      }
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("receiveCategoryRule", {
          "id": $scope.id,
          "name": $scope.name,
          "operator": $scope.operator,
          "decisionKey": $scope.decisionKey,
          "comparisonType": $scope.comparisonType,
          "compKey": $scope.compKey
        }, function () {
          $state.go("main.receive_category_rule");
        });
      }
    };
  });
})();