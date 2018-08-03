/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  "use strict";

  angular.module("myApp").service('transferStockInService', function (commonService, TRANSFERIN_CONSTANTS) {
    return {
      dialogMushiny2:function(window, options){
      window.setOptions({
        width: options.width || 450,
        height: options.height || 200,
        title: options.title || "",
        scrollable: false,
        content: {
          url: options.url || "modules/transfer/transferStockIn/base/templates/"+ options.type+ "Window.html"
        },
        open: function(){
          options.open && options.open(this);
        }
      });
      window.sureCall=options.sureCall;


      window.refresh();
     window.center();
      window.open();
    },
        getTransferInOrders: function (success,param) {

            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.getTransferInOrders,
                params:param,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        addTransferInOrders: function (success,data) {

            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.addTransferOrders,
                params:data,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        getConditionOrders: function (cb,param) {
            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.getConditionOrders ,
                params:param,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
      activeTransferOrders : function (success,param) {

            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.activeTransferOrders,
                params:param,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
      deleteTransferOrders : function (success,param) {

        commonService.ajaxMushiny({
          url: TRANSFERIN_CONSTANTS.deleteTransferOrders,
          params:param,
          success: function (datas) {
            success && success(datas.data);
          }
        });

      },
      closeTransferOrders : function (success,params) {

            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.closeTransferOrders,
                params:params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
      addTransferOrders : function (success,params) {

            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.addTransferOrders,
                params:params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
      getTransferInContainerSurvey : function (success,params) {

            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.getTransferInContainerSurvey,
                params:params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });

        },
      getTransferInContainerDetail: function (cb,params) {

            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.getTransferInContainerDetail,
                params:params,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
      getTransferInProConSurvey: function (cb,params) {
            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.getTransferInProConSurvey ,
                params:params,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },

      getTransferInProConDetail: function (cb,params) {
      commonService.ajaxMushiny({
        url: TRANSFERIN_CONSTANTS.getTransferInProConDetail ,
        params:params,
        success: function (datas) {
          cb && cb(datas.data);
        }
      });
    },
      getTransferInDiffDetail: function (cb,params) {
        commonService.ajaxMushiny({
          url: TRANSFERIN_CONSTANTS.getTransferInDiffDetail ,
          params:params,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
        // 取客户
        getClientByWarehouse: function(warehouseId, cb){
            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.getClientInfoByWarehouse+"?warehouseId="+ warehouseId,
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        },getWareHouseAndClient:function(cb,param)
        {
            commonService.ajaxMushiny({
                url: TRANSFERIN_CONSTANTS.getWareHouseAndClient,
                params:param || {},
                success: function(datas){
                    cb && cb(datas.data);
                }
            });
        }
    };
  });


})();
