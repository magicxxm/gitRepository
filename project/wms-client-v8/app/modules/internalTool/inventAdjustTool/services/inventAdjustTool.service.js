/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */


(function () {
    "use strict";

    angular.module("myApp").service('inventAdjustToolService', function (commonService, INTERNAL_TOOL_CONSTANT) {
        return {


            //  扫描原始容器
            orgContainer: function (sourceName, success, error) {

                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.adjustScanningSource + "?sourceName=" + sourceName,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            // type 1  扫描商品
            adjustScanningItemDataGlobal: function (sourceId, sku, success, error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.adjustScanningItemDataGlobal + "?sourceId=" + sourceId + "&sku=" + sku,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },

            //扫描商品
            scanningGoods: function (sourceId, sku, success, error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.adjustScanningItemData + "?sourceId=" + sourceId + "&sku=" + sku,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            //扫描责任人
            adjustCheckUser: function (username, success, error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.adjustCheckUser + "?username=" + username,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            //扫目的容器
            adjustScanningDestination: function (sourceId, itemDataId, destinationName, success, error) {

                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.adjustScanningDestination + "?sourceId=" + sourceId + "&itemDataId=" + itemDataId + "&destinationName=" + destinationName,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            //整箱移货
            moveAllGoods: function (source, success, error) {
                commonService.ajaxMushiny({
                    method: "POST",
                    data: source,
                    url: INTERNAL_TOOL_CONSTANT.adjustMoveAllGoods,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            //商品移动
            moveGoods: function (source, success, error) {
                commonService.ajaxMushiny({
                    method: "POST",
                    data: source,
                    url: INTERNAL_TOOL_CONSTANT.moveGoods,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },

            //修改属性提交
            adjustUpdateInventoryAttributes: function (source, success, error) {
                commonService.ajaxMushiny({
                    data: source,
                    method: "POST",
                    url: INTERNAL_TOOL_CONSTANT.adjustUpdateInventoryAttributes,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },

            //盘亏提交
            lossGoods: function (source, success, error) {
                commonService.ajaxMushiny({
                    data: source,
                    method: "POST",
                    url: INTERNAL_TOOL_CONSTANT.lossGoods,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            overageGoods: function (source, success, error) {
                commonService.ajaxMushiny({
                    data: source,
                    method: "POST",
                    url: INTERNAL_TOOL_CONSTANT.overageGoods,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            },
            getItemData: function (source, success, error) {
                commonService.ajaxMushiny({
                    url: INTERNAL_TOOL_CONSTANT.getItemData + "?storageLocationId=" + source,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error: function (datas) {
                        error && error(datas.data);
                    }
                });
            }
        };
    });
})();