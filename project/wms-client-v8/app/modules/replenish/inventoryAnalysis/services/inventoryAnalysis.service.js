(function () {
  "use strict";

  angular.module("myApp").service('inventoryAnalysisService', function (commonService, REPLENISH_CONSTANT) {
    return {
        startCron: function (success,searchParams) {
            var getString = '?';
            var intervalStr = searchParams.interval ? searchParams.interval : 1;
            getString = getString + "interval=" + intervalStr;
            getString = getString + "&startDate=" + searchParams.startDate;
            getString = getString + "&startTime=" + searchParams.startTime;
            getString = getString + "&endDate=" + searchParams.endDate;
            getString = getString + "&endTime=" + searchParams.endTime;
            commonService.ajaxMushiny({
              url: REPLENISH_CONSTANT.startCron+getString,
              success: function (datas) {
                success && success(datas.data);
              }
            });
         },
        updateTimeDetail : function (success,params) {
            commonService.ajaxMushiny({
                url: REPLENISH_CONSTANT.updateTime,
                params:params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        updateStopTime : function (success,id) {
            commonService.ajaxMushiny({
                url: REPLENISH_CONSTANT.updateStopTime+"?id="+id,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        getTimeConfig:function(cb,param)
        {
            commonService.ajaxMushiny({
                url: REPLENISH_CONSTANT.getTimeConfig,
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        },
        stopCron: function (success) {
            commonService.ajaxMushiny({
                url: REPLENISH_CONSTANT.stopCron,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        getRecordsForPprDetail: function (success,searchParams) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getRecordsForPprDetail, params:searchParams,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        queryCtimedetail: function (userName,cb) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.queryCtimedetail + "?warehouseId=DEFAULT&clientId=SYSTEM&startDate="+"2017-06-26 00:00:00"+"&endDate="+"2017-06-26 23:59:59"+"&userName="+userName,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        searchCtimedetail : function (success,warehouseId,clientId,dayDate,startDate,endDate,userName) {
          //  alert("serve::::::"+warehouseId+","+clientId+","+dayDate+","+startDate+","+endDate+","+userName);
            var getString = '?';
            if(warehouseId)getString = getString + "warehouseId=" + warehouseId;
            if(clientId)getString = getString + "&clientId=" + clientId;
            if(dayDate)getString = getString + "&dayDate=" + dayDate;
            if(startDate)getString = getString + "&startDate=" + startDate;
            if(endDate)getString = getString + "&endDate=" + endDate;
            if(userName)getString = getString + "&userName=" + userName;
            if(typeof(warehouseId) =="undefined" && typeof(clientId) =="undefined" && typeof(dayDate) =="undefined"
                && typeof(startDate) =="undefined" && typeof(endDate) =="undefined" && typeof(userName) =="undefined" )
            {
                getString ='';
            }
          //   alert("endgetString=====:"+getString);
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.queryCtimedetail+getString,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
        getJobTotal: function (cb,employeeCode, warehouseId,clientId,startTime,endTime) {
            //alert("serve:"+warehouseId+","+clientId+","+startDate+","+endDate+","+userName)
            var getString = "/"+ employeeCode+"/"+warehouseId+"/"+clientId+"/"+startTime+"/"+endTime;
            //   alert("endgetString=====:"+getString);
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getJobTotal+getString,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        getJobDetail: function (cb,employeeCode, warehouseId,clientId,startTime,endTime) {
            //alert("serve:"+warehouseId+","+clientId+","+startDate+","+endDate+","+userName)
            var getString = "/"+ employeeCode+"/"+warehouseId+"/"+clientId+"/"+startTime+"/"+endTime;
            //   alert("endgetString=====:"+getString);
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getJobDetail + getString,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        getUserInfo: function (employeeCode,success,error) {
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getUserInfo+"?employeeCode="+employeeCode,
                success:function (datas) {
                    success && success(datas.data);
                },error:function (datas) {
                    alert("error");
                }
            });
        },
        // 取客户
        getClientByWarehouse: function(warehouseId, cb){
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getClientInfoByWarehouse+"?warehouseId="+ warehouseId,
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        },
        getWeekOfMouth: function(successFun, dateTime){
            commonService.ajaxMushiny({
                url: TOT_CONSTANT.getWeekOfMonth+"?dateTime="+ dateTime,
                success: function(datas){
                    successFun && successFun(datas.data);
                }
            });
        }
    };
  });
})();
