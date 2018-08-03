/**
 * Created by frank.zhou on 2016/12/04.
 * Updated by feiyu,pan on 2017/5/19
 */
(function () {
    "use strict";

    // ===================================================================================================================
    function transformItem(str) {
        for (var i = 0, outs = [], preIdx = 0; i < str.length; i++) {
            var c = str.charAt(i);
            if (i == str.length - 1) {
                var last = str.substring(preIdx);
                outs.push(last.substring(0, 1).toLowerCase() + last.substring(1));
            }
            if (c < "A" || c > "Z") continue;
            var mid = str.substring(preIdx, i);
            outs.push(outs.length ? mid.substring(0, 1).toLowerCase() + mid.substring(1) : mid);
            outs.push("-");
            preIdx = i;
        }
        return outs.join("");
    }
    // =================================================================================================================
    var itemOne = ["boxType", "customerOrder", "customerShipment", "packingStation", "packingStationType", "pickingArea"];
    var itemTwo = ["collateProfile", "collateTemplate", "processPath", "processPathType", "reBatchSlot", "reBatchStation", "reBatchStationType"];
    var itemThree = ["reBinCellType", "reBinStation", "reBinStationType", "reBinWall", "reBinWallType"];
    var items = itemOne.concat(itemTwo, itemThree);
    var baseConstant = {};
    for (var i = 0; i < items.length; i++) {
        var item = items[i],
            key = item.substring(0, 1).toUpperCase() + item.substring(1);
        item = "outbound/" + transformItem(item);
        baseConstant["find" + key] = item + "s";
        baseConstant["create" + key] = item + "s";
        baseConstant["get" + key] = item + "s";
        baseConstant["read" + key] = item + "s/#id#";
        baseConstant["update" + key] = item + "s";
        baseConstant["delete" + key] = item + "s/#id#";
    }

    angular.module("myApp").constant("OUTBOUND_FILTER", {
        "customerOrder": [{"field": "customerName"}, {"field": "entityLock", operator: "eq"}],
        "customerShipment": [{"field": "customerOrder.orderNo"}, {"field": "entityLock", operator: "eq"}],
    }).constant("OUTBOUND_CONSTANT", angular.extend(baseConstant, {
        "getWarehouse": "system/warehouses",
        "getClient": "system/clients",
        "getSelectionBySelectionKey": "system/selections",
        "getSortCode": "outbound/sortCodes",
        "getDeliveryTimeBySortCode": "outbound/sortCodes/getDeliveryTimeBySortCode",
        "getAllDeliveryTime": "outbound/sortCodes/getAllDeliveryTime",
        "getSelectProcessPath":"masterdata/outbound/process-paths",
        "getShipmetPriority":"outbound/shipment-priority/getShipmetPriority",
        "savePriority":"outbound/shipment-priority/saveShipmentPriority",
        "createPriority":"outbound/shipment-priority/addPriority",
        "updatePriority":"outbound/shipment-priority/updatePriority",
        "deleteCustomerShipmentTime":"outbound/shipment-priority/#id#",
        "readCustomerShipmentTime":"outbound/shipment-priority/#id#",
        //6代 collate
        "getCollate": "outbound/collate-info",
        "getLivePicker": "outbound/live-picker",
        "getPickingCart": "outbound/picking-cart",
        "getZoneBatch": "outbound/zone-picking/batch",
        "getZoneProcessPath": "outbound/zone-picking/process-path",
        "updateProcessPathStatus": "outbound/processpath-state",
        "updateProcessPathCollate": "outbound/processpath-info",
        "getCollateExsdInfo": "outbound/delivery-info",
        // reBatch
        "scanReBatchStations": "outbound/rebatch-stations",
        "scanReBatchContainer": "outbound/rebatch-positions",
        "scanReBatchSlot": "outbound/rebatch-positions/{positionId}/finish",
        "getReBatchInfo": "outbound/rebatch-requests/open",
        "doReBatch": "outbound/rebatch-requests/{requestId}/finish",
        "exitClick":"outbound/rebatch-station/quit",
        //rebin
        "rebinResult":"outbound/rebin-requests/{requestId}/rebin-result",
        "rebinCannotscan": "outbound/rebin-requests/{requestId}/rebin-cannotscan",
        "lockContainer":"outbound/rebin-unitLoad/{requestId}/confirmSKUMore",
        "rebinMore": "outbound/rebin-requests/{requestId}/rebin-more",
        "rebinSkumore": "outbound/rebin-requests/{requestId}/rebin-skumore",
        "rebinLess": "outbound/rebin-requests/{requestId}/rebin-less",
        "scanStation": "outbound/rebin-stations",
        "scanPickingOrder": "outbound/rebin-requests",
        "rebinWalls": "outbound/rebin-requests/{requestId}/rebin-walls/assign",
        "rebinfromUnitloads": "outbound/rebin-requests/{requestId}/rebinfrom-unitloads",
        "scanReBinWall": "outbound/rebin-requests/{requestId}/rebin-walls",
        "scanPickingContainer": "outbound/rebin-requests/{requestId}/rebinfrom-unitloads",
        "scanGoods": "outbound/rebin-requests/{requestId}/rebin-positions",
        "confirmLoseItem":"outbound/rebin-requests/{requestId}/confrimMissItem",
        "rebinPosition": "outbound/rebin-requests/{requestId}/rebin-positions/{positionId}/finish",
        "rebinEnd": "outbound/rebin-requests/{requestId}/finish",
        "finishRebin":"outbound/rebin-station/quit",
        //包装
        "getGoods": "outbound/pack/pickpackcell/find",
        "getGoodsbByShipment":"outbound/pack/shipment/getStockUnit",
        //"getGoods": "modules/outbound/pack/data/pack.json",
        "checkPackStation": "outbound/pack/packStation/check",
        "checkPickPackWall": "outbound/pack/pickPackWall/scan",
        "getMoreItem":"outbound/pack/storageLocation/lock",
        "getCellName":"outbound/pack/pickPackWall/getCellName",
        "getDigitalShipment":"outbound/pack/digital/getShipment",
        "updateDigitalShipment":"outbound/pack/digital/updateShipment",
        "checkRebinWall": "outbound/pack/rebinWall/find",
        //"checkRebinWall": "modules/outbound/pack/data/pack.json",
        "weigh": "outbound/pack/shipment/weight",
        "packing": "outbound/pack/item/confirm",
        "scanSN":"outbound/pack/SN/scan",
        "checkScanItem": "outbound/pack/item/scanData",
        "checkItem": "outbound/pack/item/find",
        "scanShimpment":"outbound/pack/shipmentNo/scan",
        "packFinish": "outbound/pack/scanBox/finished",
        "checkProblemContainer": "outbound/pack/storageLocation/scan",
        "checkProblemShipment":"outbound/pack/checkProblemShipment",
        "checkDeleteContainer" : "outbound/pack/shipment/delete",
        "stopPack": "outbound/pack/finish",
        "submitQuestion": "outboundproblem/generate-obp",
        "getWeight":"outWeight",
        "getPickPackCellName":"modules/outbound/pack/data/pickPackCell.json",
        "getDigitalLabel":"outbound/pack/digital/getDigitalLabel",
        "informationInquiry": "modules/outbound/pack/data/pack.json",
        //发货系统
        "getDeliverPageData": "outgoods/getpagedata",
        "getDeliverySystemData": "outgoods/searchpagedata",
        "changeState": "outgoods/chagngecurrentstate",
        "toinsertRequst": "outgoods/toinsertRequst",
        "getDockDoor": "outgoods/getbinddoors",
        "bindDockDoor": "outgoods/bindoutdoor",
        "reload": "outgoods/reloadgoods",
        "getSortCodePrintInfo": "outgoods/getinterreceipt",
        "print": "outgoods/printreceipt",
        "rePrint": "outgoods/reprintreceipt",
        "getDeliveryShipmentsDetailData": "outgoods/getshipmentsdetail",
        "searchDeliveryShipmentsDetailData": "outgoods/searchshipment",
        "getAllExSd": "outgoods/getallexsd",
        "getShipmentInformation": "outgoods/shipmentdetail",
        "searchQueryCartData": "outgoods/searchstorageone",
        "getQueryCartData": "outgoods/lookallciperdetail",
        "searchCartQueryShipmentData": "outgoods/searchstoragetwo",
        "getCartQueryShipmentData": "outgoods/lookciperdetail",
        "transOutMainPage":"transout/mainpage",
        "transOutDetails":"transout/transoutdetail",
        "transOutConfigs":"transout/transoutconfig",
        "transOutConfigAdd":"transout/transoutconfig/{id}",
        "transOutConfigDelete":"transout/transoutconfig/{id}",
        "transOutConfigUpdate":"transout/transoutconfig",
        "transOutConfigImport":"transout/transoutconfig/import",
        "transOutConfigExport":"transout/transoutconfig/export",
        "getBoxDifference":"outgoods/packed-box-recommend",
        "getUnPickMenuData":"outbound/unpickmenu/stationName",
        //扫描工作站
        "checkStation": "outbound/pick/pickStation/check",
        "checkSingleStation":"outbound/singlePickPack/station/check",
        //扫描pickpackwall
        "findPickPackWall": "outbound/pick/pickPackWall/find",
        //扫描三种货筐
        "checkStorageLocation": "outbound/pick/pickingUnitLoad/check",
        //货筐有商品时，仍然使用该货筐
        "bindStorageLocation": "outbound/pick/pickingUnitLoad/bind",
        //扫描pod
        "findPod": "outbound/pick/pod/find",
        //获取orderposition
        "getOrderPosition": "outbound/pick/pickOrderPosition/find",
        //结满工作站货筐
        "fullAllStorage": "outbound/pick/pickingUnitLoad/reBind",
        //扫描商品
        "scanSKU": "outbound/pick/pickToPack/scan",
        //确认扫描商品
        "confirmScanSKU": "outbound/pick/pickToPack/confirm",
        //确认扫描序列号
        "confirmSerialNo": "outbound/pick/sku/serialNo/confirm",
        //扫描序列号
        "scanSerialNo": "outbound/pick/sku/serialNo/scan",
        //结满货筐
        "fullstorage": "outbound/pick/pickingUnitLoad/fullStorage",
        //货位扫描
        "scanBin": "outbound/pick/storageLocation/scan",
        //获取工作站所有货筐信息
        "getAllstorageInfo": "outbound/pick/pickingUnitLoad/findAll",
        //检查批次是否完成
        "checkOrderFinish": "outbound/pick/pickToTote/confirmFinishOrder",
        //扫描残损商品
        "checkDamagedItem": "outbound/pick/checkDamagedItem",
        //问题商品扫描
        "scanProItem": "outbound/pick/problemItem/scan",
        "checkShipment":"outbound/pick/checkShipment",
        //已扫描完所有商品
        "haveScanedAll": "outbound/pick/haveScanAll",
        //停止工作
        "stopWorking": "outbound/pick/pickStation/disBind",
        "callPod":"outbound/pick/stopCallPod",
        "getCallPod":"outbound/pick/getCallPod",
        "getCallPickOrder":"outbound/pick/getIsCallPickOrder",
        "stopAssignPickOrder":"outbound/pick/pickToTote/stopPickOrder",
        "scanDigitalName":"outbound/pick/scanDigitalName",
        //扫描工作站位置标签获取灯的信息
        "scanPosition":"outbound/pick/scanPositionNo",
        //扫描pickpackcell获取灯的信息
        "scanpickPackCellDigital":"outbound/pick/scanPickPackCell",
        //////////////////////////////////picktotote//////////////////////////////////
        //扫描picktotote工作站
        "scanPickToToteStation": "outbound/pick/pickToTote/checkStation",
        //检查工作站绑定 2 种问题货筐没
        "checkStorage": "outbound/pick/pickToTote/checkStorage",
        "getfinishOrder": "outbound/pick/pickToTote/finishOrder",
        //检查扫描货框
        "bindStorage": "outbound/pick/pickToTote/bindStorage",
        //检查batch对应的货筐
        "checkOrder": "outbound/pick/pickToTote/checkOrderPosition",
        //获取拣货任务
        "getOrder": "outbound/pick/pickToTote/getOrder",
        //扫描商品确认拣货
        "confirmItem": "outbound/pick/pickToTote/checkItem",
        //输入正品数量拣货
        "confirmAmount": "outbound/pick/pickToTote/confirmPick",
        //输入残品数量
        "confirmDamagedAmount": "outbound/pick/pickToTote/confirmDamaged",
        //问题货筐里有商品时，继续使用该货筐
        "bindStorageWithItem": "outbound/pick/pickToTote/bind",
        //商品报残
        "scanDamagedItem": "outbound/pick/pickToTote/scanProblemItem",
        //已经扫描完所有商品
        "haveScanedAllItem":"outbound/pick/pickToTote/haveScanedAllItem",
        //////////////////////////////////////////////outbound问题处理//////////////////////////
        "obproblem": "outboundproblem/generate-obp",
        //////////////////////////////////////暗灯//////////////////////////////////////////
        "getAndonType": "andon-master/types",
        "getAndon": "andon-master/pe-solve",
        "createAndon": "andon-masters",
        "getstorageLocation": "andon-masters/locationName",
        "releasePod1":"wcs/podRelease",
        "refreshNewPod":"wcs/callNewPod",


        //singlePickPack
        "weighShipment": "/outbound/singlePickPack/weight",
        "scanSingleShipment": "/outbound/singlePickPack/scansingleShipment",
        "singlePackFinish": "/outbound/singlePickPack/scanBox",
        "checkPickedContainer": "/outbound/singlePickPack/scanPickedContainer",
        "getSinglePickPack": "/outbound/singlePickPack/getpickOrder",
        "confirmSinlePick": "/outbound/singlePickPack/confirm",
    }));
})();