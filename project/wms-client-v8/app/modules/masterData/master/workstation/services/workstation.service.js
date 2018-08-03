/**
 * Created by frank.zhou on 2017/05/25.
 */
(function () {
    "use strict";

    angular.module("myApp").service('workstationService', function (commonService, MASTER_CONSTANT) {
        return {
            // 取电子标签
            getDigitalLabelByLabel: function (labelIds, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.getDigitalLabelByLabel.replace("#ids#", labelIds),
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // 取地图
            getNodeBySectionId: function (sectionId, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.getNodeBySectionId + "?sectionId=" + sectionId,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            exitWorkStation: function (stationId, sb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.exitWorkStation.replace("#id#", stationId),
                    success: function (datas) {
                        sb && sb(datas.data);
                    }
                });
            }
        };
    });
})();