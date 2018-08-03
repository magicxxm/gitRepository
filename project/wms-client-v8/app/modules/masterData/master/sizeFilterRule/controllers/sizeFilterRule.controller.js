/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("sizeFilterRuleCtl", function ($scope, $rootScope, $window, $state, sizeFilterRuleService, commonService, masterService) {
    $window.localStorage["currentItem"] = "sizeFilterRule";
    $rootScope.sizeFilterRuleData;
    $rootScope.size1;
    var isTrueSide;
    var isTrueVolume;
    var isTrueWeight;
    $rootScope.nameSource = ["小", "中", "大", "超大"];
    $rootScope.ruleSource = ["最长边(mm)", "体积(mm³)", "重量(g)"];
    $rootScope.modeSource = ["小于", "小于等于", "等于", "大于", "大于等于"];
    var columns = [{field: "name", width: 300, headerTemplate: "<span>名称</span>"},
      {
        field: "rule",
        template: function (item) {
          return item.rule === "最长边" ? item.rule + "(mm)" : item.rule === "体积" ? item.rule + "(mm³)" : item.rule === "重量" ? item.rule + "(g)" : "";
        }, headerTemplate: "<span>规则名</span>"
      },
      {field: "mode", headerTemplate: "<span>方式</span>"},
      {field: "number", headerTemplate: "<span>级别</span>"},
      {field: "price", headerTemplate: "<span>值</span>"}];
    $scope.sizeFilterRuleGridOptions = commonService.gridMushiny({
      height: $(document.body).height() - 150,
      columns: columns,
      dataSource: masterService.getGridDataSource('sizeFilterRule')
    });
    /*************************************************************************************************************/
    sizeFilterRuleService.getSizeFilterRule(function (data) {
      $rootScope.sizeFilterRuleData = data;
    });
    //数据添加到数组
    function addToArray(nameArrays, nameArray, name, obj) {
      nameArrays.push(obj);
      if (nameArray.indexOf(name) === -1) {
        nameArray.push(name);
      }
    };
    //设置每个规则是否True
    function ruleIsTrue(ruleName) {
      if (ruleName === "最长边") {
        isTrueSide = false;
      } else if (ruleName === "体积") {
        isTrueVolume = false;
      } else {
        isTrueWeight = false;
      }
    }

    //设置是否
    function isTrue(ruleName, price1, nameArrayS) {
      if (nameArrayS.mode === "小于") {
        if (price1 >= nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else if (nameArrayS.mode === "小于等于") {
        if (price1 > nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else if (nameArrayS.mode === "大于") {
        if (price1 <= nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else if (nameArrayS.mode === "大于等于") {
        if (price1 < nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else {
        if (price1 != nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      }
    };
    //设置size
    function setSize(size1, nameArrayS, longestSide, volume, weight) {
      isTrueSide = true;
      isTrueVolume = true;
      isTrueWeight = true;
      for (var s = 0; s < nameArrayS.length; s++) {
        if (nameArrayS[s].rule === "最长边") {
          isTrue("最长边", longestSide, nameArrayS[s]);
        } else if (nameArrayS[s].rule === "体积") {
          isTrue("体积", volume, nameArrayS[s]);
        } else {
          isTrue("重量", weight, nameArrayS[s]);
        }
      }
      if (isTrueSide && isTrueVolume && isTrueWeight) {
        $rootScope.size1 = size1;
      }
    }

    $rootScope.calculatorSize = function (side1, side2, side3, weight) {
      var nameArray = [];//放name的数组，最多四条小，中，大，超大
      var nameArrayS = [];//小
      var nameArrayM = [];//中
      var nameArrayB = [];//大
      var nameArrayBB = [];//超大
      //如果最长边或者体积或者重量有任何一个为0，那么size为null
      if (side1 === 0 || side2 === 0 || side3 === 0 || weight === 0) {
        $rootScope.size1 = "小";
      } else {
        var longestSide = side1 > side2 ? (side1 > side3 ? side1 : side3) : ((side2 > side3 ? side2 : side3));
        var volume = side1 * side2 * side3;

        //有过滤规则
        if ($rootScope.sizeFilterRuleData != null && $rootScope.sizeFilterRuleData.length !== 0) {
          for (var i = 0; i < $rootScope.sizeFilterRuleData.length; i++) {
            var name = $rootScope.sizeFilterRuleData[i].name;
            if (name === "小") {
              addToArray(nameArrayS, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            } else if (name === "中") {
              addToArray(nameArrayM, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            } else if (name === "大") {
              addToArray(nameArrayB, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            } else {
              addToArray(nameArrayBB, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            }
          }
          for (var a = 0; a < nameArray.length; a++) {
            if (nameArray[a] === "小") {
              if ($rootScope.size1 == undefined) {
                setSize("小", nameArrayS, longestSide, volume, weight);
              }
            } else if (nameArray[a] === "中") {
              if ($rootScope.size1 == undefined) {
                setSize("中", nameArrayM, longestSide, volume, weight);
              }
            } else if (nameArray[a] === "大") {
              if ($rootScope.size1 == undefined) {
                setSize("大", nameArrayB, longestSide, volume, weight);
              }
            } else if (nameArray[a] === "超大") {
              if ($rootScope.size1 == undefined) {
                setSize("超大", nameArrayBB, longestSide, volume, weight);
              }
            }
          }
          //有过滤规则，没有过滤出来设置为超大
          if ($rootScope.size1 == undefined) {
            $rootScope.size1 = "超大";
          }
        } else {
          //没有过滤规则默认为超大
          $rootScope.size1 = "超大";
        }
      }
    };
    //更新所有sku，skuGlobal中的size
    $rootScope.updateSize = function () {
      sizeFilterRuleService.getItemDataGlobal(function (data) {
        console.log(data);
        var sizeArray = [];
        for (var i = 0; i < data.length; i++) {
          $rootScope.size1 = undefined;
          $rootScope.calculatorSize(data[i].depth, data[i].width, data[i].height, data[i].weight)
          sizeArray.push({
            "id": data[i].id,
            "itemNo": data[i].itemNo,
            "size": $rootScope.size1
          })
        }
        sizeFilterRuleService.upDateSize({
          "upDateSize": sizeArray
        }, function () {
          $state.go("main.item_data_global");
        });
      });
    };
  }).controller("sizeFilterRuleCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("sizeFilterRule", {
          "name": $scope.name,
          "rule": $scope.rule === "最长边(mm)" ? "最长边" : $scope.rule === "体积(mm³)" ? "体积" : $scope.rule === "重量(g)" ? "重量" : "",
          "mode": $scope.mode,
          "price": $scope.price
        }, function () {
          $state.go("main.size_filter_rule");
        });
      }
    };
  }).controller("sizeFilterRuleUpdateCtl", function ($scope, $stateParams, $state, masterService) {
    masterService.read("sizeFilterRule", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("sizeFilterRule", {
          "id": $scope.id,
          "name": $scope.name,
          "rule": $scope.rule === "最长边(mm)" ? "最长边" : $scope.rule === "体积(mm³)" ? "体积" : $scope.rule === "重量(g)" ? "重量" : "",
          "mode": $scope.mode,
          "price": $scope.price
        }, function () {
          $state.go("main.size_filter_rule");
        });
      }
    };
  });
})();