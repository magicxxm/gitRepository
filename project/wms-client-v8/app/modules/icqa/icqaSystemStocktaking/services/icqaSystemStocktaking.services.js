/**
 * Created by juping.li on 2017/8/30.
 */
(function () {
    "use strict";
    angular.module("myApp").service('icqaSystemStocktakingService', function (commonService, ICQA_CONSTANT) {
        return {
            // 取区域
            selectRoundOfInventory: function (times, value, createdDateOne, createdDateTwo, cb) {
                if (value == undefined) {
                    value = "";
                }
                if (createdDateOne) {
                    createdDateOne = createdDateOne + "T00:00:00Z";
                } else {
                    createdDateOne = "";
                }
                if (createdDateTwo) {
                    createdDateTwo = createdDateTwo + "T00:00:00Z";
                } else {
                    createdDateTwo = "";
                }
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.getSelectRoundOfInventory + "?times=" + times + "&search=" + value + "&date1=" + createdDateOne + "&date2=" + createdDateTwo,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },

            //进入盘点详情页面
            selectRoundOfInventoryId: function (times, id,createdDateOne,createdDateTwo, cb) {

                if (createdDateOne) {
                    createdDateOne = createdDateOne + "T00:00:00Z";
                } else {
                    createdDateOne = "";
                }
                if (createdDateTwo) {
                    createdDateTwo = createdDateTwo + "T00:00:00Z";
                } else {
                    createdDateTwo = "";
                }
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.enterSelectRoundOfInventoryId + "?times=" + times + "&stocktakingId=" + id+"&date1=" + createdDateOne + "&date2=" + createdDateTwo,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            //得到盘点下拉框人员
            getStocktaking0rderUser: function (clientId, cb) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.getStocktaking0rderUser + "?clientId=" + clientId,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            //得到盘点详情
            select0rdersByStocktakingIds: function (stocktakingId, times, cb) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.selectStocktakingIds + "?stocktakingId=" + stocktakingId + "&times=" + times,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            //保存盘点人员
            saveStocktakingUser: function (data, cb) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.saveStocktakingUser,
                    method: "POST",
                    data: data,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            //判断二轮三轮是否指定过人了
            checkInventory: function (stocktakingId, times,system, cb) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.checkInventory + "?stocktakingId=" + stocktakingId + "&times=" + times + "&rule=" +system,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            //指定用户时判断前一轮是否结束(未用)
            checkOneInventory: function (stocktakingId, times, cb) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.checkOneInventory + "?stocktakingId=" + stocktakingId + "&times=" + times,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            //删除用户
            deleteUsers: function (stocktakingId, times,system, cb) {
                commonService.ajaxMushiny({
                    method: "DELETE",
                    url: ICQA_CONSTANT.deleteUsers + "?stocktakingId=" + stocktakingId + "&times=" + times + "&rule=" + system,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                })
            },
            // 创建盘点任务
            createInventoryTask: function (stocktakingIds, cb) {
                commonService.ajaxMushiny({
                    method: "POST",
                    url: ICQA_CONSTANT.inventoryTask + "?stocktakingIds="+stocktakingIds,
                        success: function (datas) {
                        cb && cb(datas.data);
                    }
                })
            },
            //得到一轮，二轮，三轮剩余数量
            selectInventoryCount: function (stocktakingId,createdDateOne,createdDateTwo,cb) {
                if (createdDateOne) {
                    createdDateOne = createdDateOne + "T00:00:00Z";
                } else {
                    createdDateOne = "";
                }
                if (createdDateTwo) {
                    createdDateTwo = createdDateTwo + "T00:00:00Z";
                } else {
                    createdDateTwo = "";
                }
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.selectInventoryCounts + "?id=" + stocktakingId+"&date1=" + createdDateOne + "&date2=" + createdDateTwo,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // getCloseStocktaking: function (ids,cb) {
            //     commonService.ajaxMushiny({
            //         url: ICQA_CONSTANT.getCloseSkuStocktaking + "?ids=" + ids ,
            //         success: function (datas) {
            //             cb && cb(datas.data);
            //         }
            //     });
            //
            // }
            getCloseStocktaking: function (data,cb) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.getCloseSkuStocktaking ,
                    method: "POST",
                    data: data,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });

            }

        };
    });
})();
