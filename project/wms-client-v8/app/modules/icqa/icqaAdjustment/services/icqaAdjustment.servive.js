/**
 * Created by thoma.bian on 2017/5/10.
 */

(function () {
    "use strict";

    angular.module("myApp").service('icqaAdjustmentService', function (commonService, ICQA_CONSTANT) {
        return {
            getItemAdjust: function (option, cb) {
                if (option.startDate == undefined || option.startDate == "") {
                    option.startDate = "";
                } else {
                    option.startDate = option.startDate + "T00:00:00";
                }
                if (option.endDate == undefined || option.endDate == "") {
                    option.endDate = "";
                } else {
                    option.endDate = option.endDate + "T23:59:59";
                }
                if (option.remark == undefined) {
                    option.remark = "";
                }
                if (option.seek == undefined) {
                    option.seek = "";
                }
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.getItemAdjust + "?startDate=" + option.startDate + "&endDate=" + option.endDate + "&remark=" + option.remark + "&seek=" + option.seek,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            updateAdjustment: function (data, cb, error) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.updateAdjustment + "?id=" + data.id + "&thoseResponsible=" + data.thoseResponsible + "&adjustReason=" + data.adjustReason + "&reasonName=" + data.reasonName + "&problemDestination=" + data.problemDestination,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }, error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            autoAddEvent: function (event) {
                var keyCode = window.event ? event.keyCode : event.which;
                return keyCode == 13 ? true : false;
            },
            // 扫描工作站
            getIcqaAdjustmentStation: function(name, success, error){
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.getAndonStation+ "?name="+ name,
                    success: function (datas){
                        success && success(datas.data);
                    },
                    error: function(datas){
                        error && error(datas.data);
                    }
                });
            },
            //解绑工作站
            exitIcqaAdjustmentStation: function(name, success){
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.exitAndonStation+ "?name="+ name,
                    success: function (datas){
                        success && success(datas.data);
                    },
                });
            },
            //对应调整过滤pod
            callPodIcqaAdjustment: function(adjustmentIds,sectionId, success){
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.callPodIcqaAdjustment+ "?adjustmentIds="+ adjustmentIds+"&sectionId="+sectionId,
                    success: function (datas){
                        success && success(datas.data);
                    },
                });
            },
            //呼叫pod 接口
            callPodInterface: function (data, success) {
                commonService.generalNet({
                    method: "POST",
                    url: ICQA_CONSTANT.callPodInterface,
                    data: data,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            },
            //停止呼叫pod 恢复分配pod
            stopCallPod: function (type,workStationId, success) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.stopCallPod + "?type="+type+"&workStationId="+workStationId,
                    success: function (datas) {
                        success && success(datas.data);
                    }
                });
            },
            // 释放Pod
            reservePod:function (podName,sectionId,force,workStationId,logicStationId,success,error) {
                commonService.generalNet({
                    url:ICQA_CONSTANT.releasePod1+"?podName="+podName+"&sectionId="+sectionId+"&force="+force+"&workStationId="+workStationId+"&logicStationId="+logicStationId,
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                });
            },

            // 是否finash
            yesOrNoFinsh: function(name, success){
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.yesOrNoFinsh+"?name="+name,
                    success: function (datas){
                        success && success(datas.data);
                    },
                });
            },
            // 刷新pod
            refreshPod:function (sectionId,workStationId,success,error) {
                commonService.generalNet({
                    url:ICQA_CONSTANT.refreshNewPod+"?sectionId="+sectionId+"&workStationId="+workStationId,
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                });
            },






        }
    })
})();