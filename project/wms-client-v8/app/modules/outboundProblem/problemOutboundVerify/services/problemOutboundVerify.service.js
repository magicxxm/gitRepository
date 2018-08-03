/**
 * Created by thoma.bian on 2017/5/10.
 */

(function() {
  "use strict";

  angular.module("myApp").service('outboundProblemVerifyService', function (commonService, $window, PROBLEM_OUTBOUND) {
    return {
      // 扫描工作站
      getOutboundProblemStation: function(name, success, error){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getOnboundProblemStation+ "?name="+ name,
          success: function (datas){
            success && success(datas.data);
          },
          error: function(datas){
            error && error(datas.data);
          }
        });
      },
        //解绑工作站
        exitOnboundProblemStation: function(name, success){
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.exitOnboundProblemStation+ "?name="+ name,
                success: function (datas){
                    success && success(datas.data);
                },
            });
        },
      getOutboundProblem:function(data, success){
        if(data.seek == undefined){
          data.seek = "";
        }
        if(data.userName == undefined){
          data.userName="";
        }
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getOutboundProblem+"?state="+data.state+"&userName="+data.userName+"&seek="+data.seek,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      updateOutboundProblemVerify:function(data,success){
        commonService.ajaxMushiny({
          method: "PUT",
          url: PROBLEM_OUTBOUND.updateOutboundProblemVerify,
          data: data,
          success: function (datas) {
            success && success(datas.content);
          }
        });
      },
      findOutboundProblem:function(id,success){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.findOutboundProblem+"/"+id,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      getGoodsInformation:function(id,success){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getGoodsInformation+"/"+id,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      outboundProblemRecord: function (id,jobType,success){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.outboundProblemRecord+"/"+id+"/"+jobType,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
        //rebin车记录
      getRebinCarRecords:function(problemStoragelocation,itemNo,jobType,success){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getRebinCarRecords+"?problemStoragelocation="+problemStoragelocation+ "&itemNo="+itemNo+"&jobType="+jobType ,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },

      getOutbountProblemState:function(data,success){
        commonService.ajaxMushiny({
          method: "POST",
          data:data,
          url: PROBLEM_OUTBOUND.getOutbountProblemState,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      problemProductsNumber: function (data,success){
        commonService.ajaxMushiny({
          method: "POST",
          url: PROBLEM_OUTBOUND.getProblemProductsNumber,
          data:data,
          success: function (datas) {
            success && success(datas.data);
          }
        })
      },
      // 找到多货位置
      findOverageGoods:function(data,success){
        commonService.ajaxMushiny({
         url: PROBLEM_OUTBOUND.findOverageGoods+"?amount="+data.amount+"&itemSku="+data.itemSku+"&storageLocation="+data.storageLocation+"&fromName="+data.fromName+"&jobType="+data.jobType,
         success: function (datas) {
           success && success(datas.data);
          }
        });
      },
      // 找到少货位置
      findLossGoods:function(data,success){
        commonService.ajaxMushiny({
         url: PROBLEM_OUTBOUND.findLossGoods+"?amount="+data.amount+"&itemSku="+data.itemSku+"&toName="+data.toName+"&fromName="+data.fromName+"&jobType="+data.jobType,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      getStowingOverage:function(data,success){
        commonService.ajaxMushiny({
         url: PROBLEM_OUTBOUND.getStowingOverage+"?amount="+data.amount+"&itemDataId="+data.itemDataId+"&storageLocation="+data.storageLocation+"&jobType="+data.jobType,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      getStowingLoss:function(data,success){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getStowingLoss+"?amount="+data.amount+"&itemDataId="+data.itemDataId+"&toName="+data.toName+"&fromName="+data.fromName+"&jobType="+data.jobType,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },

      moveGoods:function(data,success){
        commonService.ajaxMushiny({
          method: "POST",
          url: PROBLEM_OUTBOUND.getMoveGoods,
          data:data,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },

      overageGoods:function(data,cb) {
        commonService.ajaxMushiny({
          method: "POST",
          url: PROBLEM_OUTBOUND.getOverageGoods,
          data: data,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      lossGoods:function(data,cb) {
        commonService.ajaxMushiny({
          method: "POST",
          url: PROBLEM_OUTBOUND.getLossGoods,
          data: data,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getGoods:function(data,cb){
        commonService.ajaxMushiny({
          method: "GET",
          url: PROBLEM_OUTBOUND.getClientGoods+"?client="+data.client+"&itemNo="+data.itemNo,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
     getRule:function(name,cb){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getRule+"?rule="+name,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getAnalysis:function(ids,cb) {
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getAnalysis + "/" + ids,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },



       getDestinationId : function(name,cb){
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.getDestinationId+"?storageLocationName="+name,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      updateOutboundProblemList: function (ids,name, success) {
        commonService.ajaxMushiny({
          url: PROBLEM_OUTBOUND.updateOutboundProblemList +  "?ids="+ids+ "&name="+name,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
        //呼叫pod
        callPodInboundProblem: function (inboundProblemIds,sectionId,jobType, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.callPodInboundProblem + "?outboundProblemIds="+inboundProblemIds +"&sectionId="+sectionId+"&jobType="+jobType,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        //呼叫pod 接口
        callPodInterface: function (data, success) {
            commonService.generalNet({
                method: "POST",
                url: PROBLEM_OUTBOUND.callPodInterface,
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        //停止呼叫pod 恢复分配pod
        stopCallPod: function (type,workStationId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.stopCallPod + "?type="+type+"&workStationId="+workStationId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 释放pod
        reservePod:function (podName,sectionId,force,workStationId,logicStationId,success,error) {
            commonService.generalNet({
                url:PROBLEM_OUTBOUND.releasePod1+"?podName="+podName+"&sectionId="+sectionId+"&force="+force+"&workStationId="+workStationId+"&logicStationId="+logicStationId,
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
                url: PROBLEM_OUTBOUND.yesOrNoFinsh+ "?name="+ name,
                success: function (datas){
                    success && success(datas.data);
                },
            });
        },
        //返回时查状态
        workStationPodState: function (workStationId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.workStationPodState + "?workStationId="+workStationId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        refreshPod:function (sectionId,workStationId,success,error) {
            commonService.generalNet({
                url:PROBLEM_OUTBOUND.refreshNewPod+"?sectionId="+sectionId+"&workStationId="+workStationId,
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