/**
 * Created by thoma.bian on 2017/5/10.
 * Updated by frank.zhou on 2017/05/10.
 */
(function(){
  "use strict";

  // ==========================================================================================================
  function transformItem(str){
    for(var i = 0, outs = [], preIdx = 0; i < str.length; i++){
      var c = str.charAt(i);
      if(i == str.length - 1){
        var last = str.substring(preIdx);
        outs.push(last.substring(0, 1).toLowerCase()+ last.substring(1));
      }
      if(c < "A" || c > "Z") continue;
      var mid = str.substring(preIdx, i);
      outs.push(outs.length? mid.substring(0, 1).toLowerCase()+ mid.substring(1): mid);
      outs.push("-");
      preIdx = i;
    }
    return outs.join("");
  }

  // ==========================================================================================================
  var items = ["attendance", "jobcategory","job","jobrecord","jobrelation","jobthreshold","pprplanconfig"];
    var baseConstant = {};
    for(var i = 0; i < items.length; i++){
      var item = items[i], key = item.substring(0, 1).toUpperCase()+ item.substring(1);
      item = "tot/"+ item; // 转换item
      baseConstant["find"+ key] = item+ "s";
      baseConstant["create"+ key] = item+ "s/create";
      baseConstant["get"+ key] = item+ "s";
      baseConstant["read"+ key] = item+ "s/#id#";
      baseConstant["update"+ key] = item+ "s";
      baseConstant["delete"+ key] = item+ "s/#id#";
    }
      angular.module("myApp").constant("TOT_FILTER", {
         "attendance": [{"field": "name"}],
         "directJob": [{"field": "name"}],
         "directJobcategory": [{"field": "name"}],
         "job": [{"field": "name"}],
         "jobcategory": [{"field": "name"}],
         "jobrecord": [{"field": "name"}],
         "jobrelation": [{"field": "name"}],
         "jobthreshold": [{"field": "name"}]
       }).constant("TOT_CONSTANT", angular.extend(baseConstant, {
           //检查用户是否存在
          "validitySource":"/tot/report/checkUserInfo",
          //上下班打卡
          "checkEmployeeCode": "tot/attendance/createAttendance/check",
          //间接工作打卡
          "checkEmployeeCodeAndJobCode": "tot/jobrecord/scanJobrecord",
          "addIndirectJobRecord": "tot/jobrecord/addJobrecord",
          //间接项目
          "findJobcategory": "tot/jobcategorys?jobType="+"jobType==INDIRECT",
          "getJobcategoryNames":"tot/jobcategorys/getJobcategorys",
          //间接工作
          "findJob": "tot/jobs?jobType="+"jobType==INDIRECT",
          "getJobByName":"/tot/jobs/getJobByName?name=",
          //直接项目
          "getDJobNames":"tot/report/getDJobType",
          "getDJobcategoryNames":"tot/jobcategorys/getDJobcategorys",
          "findDirectJobcategory": "tot/jobcategorys?jobType="+"jobType==DIRECT",
          "createDirectJobcategory":"tot/jobcategorys/create",
          "getDirectJobcategory":"tot/jobcategorys",
          "readDirectJobcategory":"tot/jobcategorys/#id#",
          "updateDirectJobcategory":"tot/jobcategorys",
          "deleteDirectJobcategory":"tot/jobcategorys/#id#",
          //直接工作
          "findDirectJob": "tot/jobs?jobType="+"jobType==DIRECT",
          "createDirectJob":"/tot/jobs/create",
          "getDirectJob": "tot/jobs",
          "readDirectJob": "tot/jobs/#id#",
          "updateDirectJob": "tot/jobs",
          "deleteDirectJob": "tot/jobs/#id#",
          //tot搬迁
          "getTotStatisticsData": "/tot/report/statisticsData",
          "queryCtimedetail":"/tot/report/ctimedetailData",
          // "getUserInfo":"/tot/report/getUserInfo",
          "getJobTotal":"tot/jobrecord/getTotal",
          "getJobDetail":"tot/jobrecord/getDetail",
          "getWarehouse":"/tot/general/warehouses",
          // "getClientInfoByWarehouse":"/tot/general/clients/getClients",
          "getClientByCurrentWarehouse":"/tot/general/clients/getClientByCurrentWarehouse",
          // "getWareHouseAndClient":"/tot/report/getWareHouseAndClient",
          "getPprStatisticsData":"/tot/ppr/recordsForPpr",
          "getWeekOfMonth":"tot/ppr/getWeekOfMonth",
          "deleteClockTimedetail":"/tot/attendance/deleteColckTimes",
          "addClockTimedetail":"/tot/attendance/addColckTimes",
          "searchClockTimedetail":"/tot/attendance/getColckTimes",
          "getAllJobcategory":"/tot/jobcategorys/getAllJobcategorys",
          "getRecordsForPprDetail":"/tot/ppr/recordsForPprDetail",
          "getPprPlanConfigData":"tot/pprplanconfigs/getRelation",

          "saveImportJob": "tot/jobs/import/file",
          "saveImportJobcategory": "tot/jobcategorys/import/file",
          "saveImportPlanConfig": "tot/pprplanconfigs/import/file",
  }));
})();