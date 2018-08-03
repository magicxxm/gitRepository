/**
 * Created by frank.zhou on 2017/03/16.
 */
(function () {
  "use strict";

  angular.module("myApp").service('workflowService', function (commonService, REPORT_CONSTANT) {
    return {
      // 取workflow
      getWorkflow: function (startTime, endTime, goodsType, success) {
        var params = "?";
        startTime != null && (params += "startTime=" + startTime);
        endTime != null && (params += "&endTime=" + endTime);
        goodsType && (params += "&goodsType=" + goodsType);
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getWorkflow + params,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // 取workflow明细
      getWorkflowDetail: function (exsdTime, workflowType, success) {
        var params = "";
        workflowType != "" && (params += "?workflowType=" + workflowType);
        exsdTime != "" && (params += "&exsdTime=" + exsdTime);
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getWorkflowDetail + params,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // processPath-workPool
      getProcessPathWorkPool: function (startTime, endTime, goodsType, success) {
        var params = "?";
        startTime != null && (params += "startTime=" + startTime);
        endTime != null && (params += "&endTime=" + endTime);
        goodsType && (params += "&goodsType=" + goodsType);
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getProcessPathWorkPool + params,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // processPath-workPool-detail
      getProcessPathWorkPoolDetail: function (workflowType, ppName, exsdTime, success) {
        var params = "?workflowType=" + workflowType;
        ppName != "" && (params += "&ppName=" + ppName);
        exsdTime != "" && (params += "&exsdTime=" + exsdTime);
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getProcessPathWorkPoolDetail + params,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // workPool-processPath
      getWorkPoolProcessPath: function (startTime, endTime, goodsType, success) {
        var params = "?";
        startTime != null && (params += "startTime=" + startTime);
        endTime != null && (params += "&endTime=" + endTime);
        goodsType && (params += "&goodsType=" + goodsType);
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getWorkPoolProcessPath + params,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // workPool-processPath-detail
      getWorkPoolProcessPathDetail: function (ppName, workflowType, exsdTime, success) {
        var params = "?ppName=" + ppName;
        workflowType != "" && (params += "&workflowType=" + workflowType);
        exsdTime != "" && (params += "&exsdTime=" + exsdTime);
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getWorkPoolProcessPathDetail + params,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }
    };
  });
})();