/**
 * Created by frank.zhou on 2017/05/25.
 */
(function () {
    "use strict";

    angular.module("myApp").service('callPodService', function (commonService, ICQA_CONSTANT) {
        return {
            callPod: function (podName, face, stationName, sb) {
                commonService.ajaxMushiny({
                    url: ICQA_CONSTANT.callPod + "?podName=" + podName + "&face=" + face + "&stationName=" + stationName + "&type=ICQAPod",
                    success: function (datas) {
                        sb && sb(datas.data);
                    }
                });
            },
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
        };
    });
})();