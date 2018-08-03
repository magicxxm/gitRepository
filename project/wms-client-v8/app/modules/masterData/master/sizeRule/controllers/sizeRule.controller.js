/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("sizeRuleCtl", function ($scope, $window) {
    $window.localStorage["currentItem"] = "sizeRule";
    var columns = [{field: "name", width: 300, headerTemplate: "<span>名称</span>"},
      {field: "mode", headerTemplate: "<span>方式</span>"}];
    $scope.sizeRuleGridOptions = {
      height: $(document.body).height() - 150,
      columns: columns,
      dataSource: [{
        name: "最长边", mode: "小于,小于等于,等于,大于,大于等于"
      }
        ,
        {
          name: "体积", mode: "小于,小于等于,等于,大于,大于等于"
        }
        ,
        {
          name: "重量", mode: "小于,小于等于,等于,大于,大于等于"
        }]
    }
    ;
  });
})();