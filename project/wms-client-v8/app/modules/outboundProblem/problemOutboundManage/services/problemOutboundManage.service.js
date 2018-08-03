/**
 * Created by thoma.bian on 2017/5/10.
 */

(function () {
  "use strict";

  angular.module("myApp").service('problemOutboundManageService', function (commonService, $window, PROBLEM_OUTBOUND) {

    return {
        getOutboundProblem:function(data, success) {
            if(data.seek == undefined){
                data.seek = "";
            }
            if(data.userName == undefined){
                data.userName="";
            }
            if(data.startDate == undefined || data.startDate==""){
                data.startDate = "";
            }else{
                data.startDate = data.startDate + "T00:00:00";
            }
            if(data.endDate == undefined || data.endDate==""){
                data.endDate = ""
            }else{
                data.endDate = data.endDate + "T00:00:00";
            }
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getOutboundProblem+"?state="+data.state+"&userName="+data.userName+"&seek="+data.seek+ "&startDate=" + data.startDate + "&endDate=" + data.endDate,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        getOutboundProblemManage:function(data, success) {
        if(data.seek == undefined){
          data.seek = "";
        }
        if(data.userName == undefined){
          data.userName="";
        }
        if(data.startDate == undefined || data.startDate==""){
          data.startDate = "";
        }else{
          data.startDate = data.startDate + "T00:00:00";
        }
        if(data.endDate == undefined || data.endDate==""){
           data.endDate = ""
         }else{
          data.endDate = data.endDate + "T00:00:00";
        }
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getOutboundProblemSolve+"?state="+data.state+"&userName="+data.userName+"&seek="+data.seek+ "&startDate=" + data.startDate + "&endDate=" + data.endDate,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },

     updateOutboundProblemList: function (ids,name, success) {
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.updateOutboundProblemList + "?ids="+ids+"&name="+name,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      getGoodsInformation: function (data, cb) {
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getGoodsInformation + "/" + data.anDonInboundId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },

      getAndonOutboundLocations: function (data, cb) {
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getAndonOutboundLocations + "?search=anDonInbound.id==" + data.anDonInboundId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        })
      },

      findOutboundProblem:function(id,success){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.findOutboundProblem+"/"+id,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }

    }
  });
})();

