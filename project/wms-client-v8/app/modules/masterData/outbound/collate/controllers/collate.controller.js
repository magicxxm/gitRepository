/**
 * Created by frank.zhou on 2017/03/07.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("collateCtl", function ($scope, $rootScope, $window, commonService, outboundService, collateService,masterService) {

    $window.localStorage["currentItem"] = "collate";

    // ==============================================================================================================
    // 锁定区
    function getLockedTemplate(dataItem){
      var htmls = [];
      htmls.push("<table id='collate_locked_"+ dataItem.processPathId+ "' border=0 cellspacing=0 cellpadding=0>");
      htmls.push(" <tr style='background-color:"+ (dataItem.processPathLock===0? "#00b0ff": "#ffcc00")+ ";color:white;'>");
      htmls.push("  <td style='width:160px;'><input type='checkbox' ng-checked='"+ (dataItem.processPathLock===0)+ "' style='margin:0px;padding:0px;' />Active</td>");
      htmls.push("  <td style='width:120px;'>Ready to Pick</td>");
      htmls.push("  <td style='width:100px;'>PP Type</td>");
      htmls.push("  <td style='width:100px;'>Liver Picker</td>");
      htmls.push(" </tr>");
      htmls.push(" <tr>");
      htmls.push("  <td style='border-bottom:0px;'><a id='collate_locked_pp_"+ dataItem.processPathId+ "' href='javascript:void(0)'>"+ dataItem.processPathName+ "</a></td>");
      htmls.push("  <td style='border-bottom:0px;'>"+ dataItem.shipments+ "/"+ dataItem.items+ "</td>");
      htmls.push("  <td style='border-bottom:0px;'>"+ dataItem.processPathType+ "</td>");
      htmls.push("  <td style='border-bottom:0px;'><a ui-sref='main.live_picker'>"+ dataItem.pickers+ "</a></td>");
      htmls.push(" </tr>");
      htmls.push("</table>");
      return htmls.join("");
    }

    // 发货点显示内容
    function getPointText(points, current){
      for(var i = 0, pointText = ""; i < points.length; i++){
        var point = points[i], times = point.deliveryTime.split("T")[1].split(":");
        var value = parseInt(times[0])+ parseInt(times[1])/60;
        value === 0 && (value = 24);
        if(value <= current && value > current-1){
          pointText = "<a href='javascript:void(0)' class='collateExsd' deliveryTime='"+ point.deliveryTime+ "'>";
          pointText += times[0]+ ":"+ times[1]+ " "+ point.shipments+ "/"+ point.items;
          pointText += "</a>";
          break;
        }
      }
      return pointText;
    }

    // 内容区
    function getTemplate(dataItem){
      var htmls = [], currentTime = parseInt(dataItem.now.split(":")[0]);
      htmls.push("<table id='collate_locked_content_"+ dataItem.processPathId+ "' border=0 cellspacing=0 cellpadding=0 style='text-align:center;'>");
      // 发货点(仅首行显示)
      htmls.push("<tr style='background-color:"+ (dataItem.processPathLock===0? "#00b0ff": "#ffcc00")+ ";color:white;'>");
      for(var i = currentTime; i < 24; i++)
        htmls.push("<td colspan='2' style='border-right-width:1px;'>"+ ($scope.setExsd? "": getPointText(dataItem.deliveryPoints, i+1))+ "</td>");
      for(var i = 0; i < currentTime; i++)
        htmls.push("<td colspan='2' style='border-right-width:1px;'>"+ ($scope.setExsd? "": getPointText(dataItem.deliveryPoints, i+1))+ "</td>");
      htmls.push("</tr>");
      $scope.setExsd = true;
      // collateProfile
      htmls.push("<tr>");
      var padTime = dataItem.padTime/60; // pad time
      padTime && htmls.push(" <td colspan='"+ padTime*2+ "' style='background:gray;border-right-width:1px;'>Pad Time</td>"); // pad time
      for(var i = 0, count=0, profiles = dataItem.collateProfiles; i < profiles.length; i++){
        var profile = profiles[i], type = profile.profileType;
        var next = profile.toDays*24+ profile.toHours+ profile.toMinutes/60; // 结束时间
        var prev = profile.fromDays*24+ profile.fromHours+ profile.fromMinutes/60; // 开始时间
        var color = ["red", "yellow", "green"][["NEAR", "INTERMEDIATE", "FAR"].indexOf(type)]; // 颜色
        var style= "background:"+ color+ ";border-right-width:1px;white-space:nowrap;cursor:pointer;";
        var text = type + "("+ prev+ "-"+ next+ ")  "+ profile.shipments+ "/"+ profile.items; // 显示内容
        var colspan = (type!="FAR"? (next-prev)*2: Math.min(48-padTime*2-count, (next-prev)*2)); // 合并列数
        htmls.push(" <td id='"+ profile.profileId+ "' class='collateProfile' colspan='"+ colspan+ "' style='"+ style+ "'>"+ text+ "</td>");
        count += colspan; // 合并数累加
      }
      48- padTime*2- count > 0 && htmls.push("<td class='collateProfile' colspan='"+ (48- padTime*2- count)+ "' style='cursor:pointer;'></td>"); // 默认
      htmls.push("</tr>");
      htmls.push("</table>");
      return htmls.join("");
    }

    // 查看processPath明细
    function readProcessPathDetail(processPathId){
      commonService.dialogMushiny($scope.collateProcessPathWin, {
        title: "collate processPath detail",
        width: 550,
        height: 380,
        url: "modules/masterData/outbound/collate/templates/collate_processPath.html",
        open: function(win){
            masterService.read("processPath", processPathId, function(data){
            for(var k in data) $scope[k] = data[k];
          });
          // 确认
          $scope.validate = function(event, targetPickRate, processPad, toteLimit, batchLimit){
            event.preventDefault();
            collateService.updateProcessPathCollate({
              processPathId: processPathId,
              pickRate: targetPickRate,
              processPad: processPad,
              toteLimit: toteLimit,
              batchLimit: batchLimit
            }, function(){
              win.close();
            });
          };
        }
      });
    }

    // 新增/编辑collateProfile
    function operateCollateProfile(options){
      commonService.dialogMushiny($scope.collateProfileWin, {
        title: options.type + " collate profile",
        width: 550,
        height: 400,
        url: "modules/masterData/outbound/collate/templates/collate_profile_"+ options.type+ ".html",
        open: function (win) {
          // 初始化collateProfile
          $scope.profileTypeSource = ["NEAR", "INTERMEDIATE", "FAR"];
          //$scope.templateSource =  outboundService.getDataSource({key: "getCollateTemplate", text: "name", value: "id"});
          collateService.getCollateTemplate(function(templates){
            for(var i = 0, datas = []; i < templates.length; i++){
              var template = templates[i];
              var items = template.minItems*100+ "% to "+ template.maxItems*100+ "%";
              var shipments = template.minShipments*100+ "% to "+ template.maxShipments*100+ "%";
              datas.push({id: template.id, name: template.name+ "//shipments:"+ shipments+ "//items:"+ items});
            }
            $scope.templateSource = datas;
          });
          //
          if(!options.collateProfileId){
            $scope.processPath = {id: options.processPath.processPathId, name: options.processPath.processPathName};
            $scope.index = ""; $scope.template = "";
            $scope.fromDays = ""; $scope.fromHours = ""; $scope.fromMinutes = "";
            $scope.toDays = ""; $scope.toHours = ""; $scope.toMinutes = "";
          }else
            outboundService.read("collateProfile", options.collateProfileId, function(data){
              for(var k in data) $scope[k] = data[k];
            });
          // 保存
          $scope.saveCollateProfile = function(profileType, index, fromDays, fromHours, fromMinutes, toDays, toHours, toMinutes, template){
            outboundService[options.type]("collateProfile", {
              "id": $scope.id || null,
              "processPathId": $scope.processPath.id,
              "profileType": profileType,
              "index": index,
              "fromDays": fromDays,
              "fromHours": fromHours,
              "fromMinutes": fromMinutes,
              "toDays": toDays,
              "toHours": toHours,
              "toMinutes": toMinutes,
              "templateId": template? template.id: "",
              "warehouseId": $window.localStorage["warehouseId"]
            }, function() {
              win.close();
              refresh(); // 页面刷新
            });
          };
        }
      });
    }

    // 查看collateProfile
    function readCollateProfile(collateProfileId, processPath){
      commonService.dialogMushiny($scope.collateProfileReadWin, {
        title: "read collate profile",
        width: 550,
        height: 400,
        url: "modules/masterData/outbound/collate/templates/collate_profile_read.html",
        open: function(win){
          outboundService.read("collateProfile", collateProfileId, function(data){
            $scope.processPathName = data.processPath.name;
            $scope.profileType = data.profileType;
            var next = data.toDays*24+ data.toHours+ data.toMinutes/60; // 结束时间
            var prev = data.fromDays*24+ data.fromHours+ data.fromMinutes/60; // 开始时间
            $scope.collateWindow = prev+ " hours to "+ next+ " hours";
            var template = data.template;
            $scope.template = template;
            $scope.itemPerBatch = template.minItems*100+ "% to "+ template.maxItems*100+ "%";
            $scope.shipmentPerBatch = template.minShipments*100+ "% to "+ template.maxShipments*100+ "%";
          });
          // 编辑
          $scope.updateCollateProfile = function(){
            operateCollateProfile({type: "update", processPath: processPath, collateProfileId: collateProfileId});
            win.close();
          };
          // 删除
          $scope.deleteCollateProfile = function(){
            outboundService.deleteOne("collateProfile", collateProfileId, function(){
              win.close();
              refresh();
            });
          };
        }
      });
    }

    // 查看发货点信息
    function readExsdInfo(deliveryTime){
      commonService.dialogMushiny($scope.collateExsdWin, {
        title: "read exsd",
        width: 520,
        height: 310,
        url: "modules/masterData/outbound/collate/templates/collate_exsd.html",
        open: function () {
          var columns = [
            {field: "deliveryTime", width: 100, headerTemplate: "<span translate='EXSD_TIME'></span>", template: function(dataItem){
              return dataItem.deliveryTime?  kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.deliveryTime)): "";
            }},
            {field: "processPathName", width: 120, headerTemplate: "<span translate='PROCESS_PATH'></span>"},
            {field: "shipments", width: 60, headerTemplate: "<span translate='SHIPMENTS'></span>"},
            {field: "items", width: 60, headerTemplate: "<span translate='ITEMS'></span>"}
          ];
          $scope.collateExsdGridOptions = {height: 240, columns: columns, selectable: false, sortable: true, resizable: true};
          collateService.getCollateExsdInfo(deliveryTime, function(datas){
            var grid = $("#collateExsdGRID").data("kendoGrid");
            grid.setOptions({dataSource: datas});
          });
        }
      });
    }

    // 定义事件
    function defineEvent(dataItem){
      // locked-status
      var tr = $("#collate_locked_"+ dataItem.processPathId).find("tr:eq(0)");
      tr.find("td:eq(0)").find("input").bind("click", function(){
        var checked = this.checked;
        // 更新processPath状态
        collateService.updateProcessPathStatus({
          processPathId: dataItem.processPathId,
          lock: checked? 0: 1
        }, function(){
          var bgColor = checked? "#00b0ff": "#ffcc00";
          tr.css("background-color", bgColor);
          $("#collate_locked_content_"+ dataItem.processPathId).find("tr:eq(0)").css("background-color", bgColor);
        });
      });
      // locked-pp
      $("#collate_locked_pp_"+ dataItem.processPathId).bind("click", function(){ readProcessPathDetail(dataItem.processPathId);});
      // content-profile
      $(".collateProfile", $("#collate_locked_content_"+ dataItem.processPathId)).each(function(){
        $(this).bind("click", function(){
          var profileId = $(this).attr("id"); // 如有值表示查看，无值表示新增
          if(profileId) readCollateProfile(profileId, dataItem); // 查看
          else operateCollateProfile({type: "create", processPath: dataItem});
        });
      });
    }

    // 刷新
    function refresh(){
      $scope.setExsd = false;
      collateService.getCollate(function(datas){
        if(datas == "") return;
        var grid = $("#collateProcessPathGRID").data("kendoGrid"), currentTime = parseInt(datas[0].now.split(":")[0]);
        // columns
        var columns = [{locked: true, width: 480, template: getLockedTemplate}];
        for(var i = currentTime; i < 24; i++){
          var col = {width: 180, headerTemplate: i+ ""};
          columns.push(i===currentTime? angular.extend({template: getTemplate}, col): angular.extend({}, col));
        }
        for(var i = 0; i < currentTime; i++) columns.push({width: 180, headerTemplate: i+ ""});
        // 刷新
        grid.setOptions({columns: columns, dataSource: datas});
        // 合并
        grid.tbody.find("tr[data-uid]").each(function(){ $(this).find("td:eq(0)").attr("colspan", 24);});
        // 定义事件
        datas.forEach(function(data){ defineEvent(data);});
        $(".collateExsd").each(function(){ $(this).bind("click", function(){ readExsdInfo($(this).attr("deliveryTime"));});});
      });
    }

    // ==============================================================================================================
    // 初始化
    $scope.collateProcessPathGridOptions = {height: $(document.body).height() - 148, selectable: false, sortable: true, resizable: true}; // grid
    refresh(); // 刷新表
  });
})();