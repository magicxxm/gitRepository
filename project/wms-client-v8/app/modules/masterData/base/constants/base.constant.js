/**
 * Created by frank.zhou 2017/04/20.
 * Updated by frank.zhou 2017/05/04.
 */
(function () {
    "use strict";

    // ==========================================================================================================
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

    // ==========================================================================================================
    var warehouseOne = ["map", "section"];
    var masterOne = ["area", "sabcRule", "dropZone", "hardware", "itemData", "itemDataGlobal", "itemGroup", "itemUnit", "sizeFilterRule"];
    var masterTwo = ["pod", "bay", "podType", "storageLocation", "storageLocationType", "zone", "workstation", "workstationType"];
    var inboundOne = ["adviceRequest", "goodsReceipt", "receiveCategoryRule", "receiveDestination", "itemDataTypeGradeStat"];
    var inboundTwo = ["receiveStation", "receiveStationType", "receiveThreshold", "stowStation", "stowStationType", "stowThreshold"];
    var robotOne = ["robot", "robotType", "batterConfig", "chargingPile"];
    var outboundOne = ["boxType", "carrier", "deliveryTime", "deliverySortCode", "deliveryPoint", "digitalLabel", "goodsOutDoor", "labelController", "packingStation", "packingStationType"];
    var outboundTwo = ["pickingArea", "pickPackCell", "pickPackCellType", "pickPackFieldType", "pickPackWall", "pickPackWallType"];
    var outboundThree = ["pickStation", "pickStationType", "processPath", "processPathType", "rebatchSlot", "rebatchStation", "rebatchStationType"];
    var outboundFour = ["rebinCell", "rebinCellType", "rebinStation", "rebinStationType", "rebinWall", "rebinWallType", "sortingStation", "sortingStationType"];
    var items = warehouseOne.concat(masterOne, masterTwo, inboundOne, inboundTwo, robotOne, outboundOne, outboundTwo, outboundThree, outboundFour);
    var baseConstant = {};
    for (var i = 0; i < items.length; i++) {
        var item = items[i], key = item.substring(0, 1).toUpperCase() + item.substring(1);
        var mid = (i < 2 ? "wd/" : (i < 19 ? "" : (i < 30 ? "inbound/" : (i < 34 ? "robot/" : "outbound/"))));
        item = "masterdata/" + mid + transformItem(item); // 转换item
        baseConstant["find" + key] = item + "s";
        baseConstant["create" + key] = item + "s" + (item === "masterdata/pod" || item === "masterdata/bay" ? "/storage-locations" : "");
        baseConstant["get" + key] = item + "s";
        baseConstant["read" + key] = item + "s/#id#";
        baseConstant["update" + key] = item + "s";
        baseConstant["delete" + key] = item + "s/#id#";
    }
    angular.module("myApp").constant("MASTER_FILTER", {
        // *****************************************************master****************************************************
        "area": [{field: "client.id"}, {"field": "name"}],
        "sabcRule": [{"field": "skuTypeName"}],
        "dropZone": [{"field": "name"}],
        "hardware": [{"field": "name"}],
        "itemData": [{field: "client.id"}, {"field": "itemNo"}, {"field": "skuNo"}, {"field": "name"}],
        "itemDataGlobal": [{"field": "itemNo"}, {"field": "skuNo"}, {"field": "name"}],
        "itemGroup": [{"field": "name"}],
        "itemUnit": [{"field": "name"}],
        "pod": [{field: "client.id"}, {"field": "name"}],
        "podType": [{"field": "name"}],
        "storageLocation": [{field: "client.id"}, {"field": "name"}, {"field": "storageLocationType.storageType"}],
        "storageLocationType": [{"field": "name"}, {"field": "storageType"}],
        "zone": [{field: "client.id"}, {"field": "name"}],
        "workstation": [{"field": "name"}],
        "workstationType": [{"field": "name"}],
        // *****************************************************inbound*****************************************************
        "adviceRequest": [{"field": "adviceNo"}, {"field": "entityLock", operator: "eq"}],
        "goodsReceipt": [{"field": "grNo"}],
        "receiveCategory": [{"field": "entityLock", operator: "eq"}],
        "receiveCategoryRule": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "receiveDestination": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "receiveStation": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "receiveStationType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "receiveThreshold": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "replenishStrategy": [{"field": "entityLock", operator: "eq"}],
        "stowStation": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "stowStationType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "stowThreshold": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "itemDataTypeGradeStat": [{"field": "entityLock", operator: "eq"}],
        // *****************************************************outbound****************************************************
        "boxType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "carrier": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "digitalLabel": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "goodsOutDoor": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "labelController": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "orderStrategy": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "packingStation": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "packingStationType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "pickingArea": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "pickPackCell": [{"field": "name"}, {"field": "pickPackWall.id", operator: "eq"}, {
            "field": "entityLock",
            operator: "eq"
        }],
        "pickPackCellType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "pickPackFieldType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "pickPackWall": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "pickPackWallType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "pickStation": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "pickStationType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "processPath": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "processPathType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebatchSlot": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebatchStation": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebatchStationType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebinCell": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebinCellType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebinStation": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebinStationType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebinWall": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "rebinWallType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "sortingStation": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        "sortingStationType": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
        // ***************************************************robot*********************************************************
        "batterConfig": [{"field": "name"}],
        "robot": [{"field": "robotId"}],
        "robotType": [{"field": "name"}],
        "chargingPile": [{"field": "name"}],
        // ***************************************************warehouse*****************************************************
        "map": [{"field": "name"}],
        "section": [{"field": "name"}]
    }).constant("MASTER_CONSTANT", angular.extend(baseConstant, {
        "getWarehouse": "system/warehouses",
        "getClient": "system/clients",
        "getSelectionBySelectionKey": "system/selections",
        // ******************************************************master*****************************************************
        "checkSKU": "masterdata/item-data-globals",
        // hardware-workstation
        "getWorkstationList": "masterdata/workstation-hardware/workstations",
        "getUnassignedHardwareByWorkstation": "masterdata/workstations/#id#/hardwares/unassigned",
        "getAssignedHardwareByWorkstation": "masterdata/workstations/#id#/hardwares/assigned",
        "saveHardwaresByWorkstation": "masterdata/workstations/#id#/hardwares",
        // zone-itemGroup
        "getZonesByClient": "masterdata/zones",
        "getUnassignedItemGroupByZone": "masterdata/zones/#id#/item-groups/unassigned",
        "getAssignedItemGroupByZone": "masterdata/zones/#id#/item-groups/assigned",
        "saveItemGroupsByZone": "masterdata/zones/#id#/item-groups",
        "getSelectItemGroup": "masterdata/item-groups",
        "getUnassignedZoneByItemGroup": "masterdata/item-groups/#id#/zones/unassigned",
        "getAssignedZoneByItemGroup": "masterdata/item-groups/#id#/zones/assigned",
        "saveZonesByItemGroup": "masterdata/item-groups/#id#/zones",
        /*********************************semblence 相似度************************************/
        "findSemblence": "masterdata/outbound/getallsemblence",
        "createSemblence": "masterdata/outbound/postsemblence",
        "readSemblence": "masterdata/outbound/semblence/#id#",
        "deleteSemblence": "masterdata/outbound/delete/#id#",
        "updateSemblence": "masterdata/outbound/semblenceupdate",
        "getSemblenceClient": "masterdata/outbound/getallclient",
        "getItemGroup": "masterdata/item-groups",
        "lock": "/masterdata/inbound/advice-requests/lock/dn",
        // ********************************inbound*****************************************************
        "scanDN": "masterdata/inbound/receive/scanning/dn",
        "activateDN": "masterdata/inbound/receive/activating/dn",
        "closedDN": "/masterdata/inbound/goods-receipts/closed/dn",
        "openDN": "/masterdata/inbound/goods-receipts/open/dn",
        // receiveCategory
        "findReceiveCategory": "masterdata/inbound/receive-categories",
        "createReceiveCategory": "masterdata/inbound/receive-categories",
        "readReceiveCategory": "masterdata/inbound/receive-categories/#id#",
        "updateReceiveCategory": "masterdata/inbound/receive-categories",
        "deleteReceiveCategory": "masterdata/inbound/receive-categories/#id#",
        // receiveEligibility
        "getSelectUser": "masterdata/inbound/user-threshold/users",
        "getAssignedThresholdByUser": "masterdata/inbound/users/#id#/thresholds/assigned",
        "getUnassignedThresholdByUser": "masterdata/inbound/users/#id#/thresholds/unassigned",
        "saveThresholdsByUser": "masterdata/inbound/users/#id#/thresholds",
        // replenishStrategy
        "findReplenishStrategy": "masterdata/inbound/replenish-strategies",
        "createReplenishStrategy": "masterdata/inbound/replenish-strategies",
        "readReplenishStrategy": "masterdata/inbound/replenish-strategies/#id#",
        "updateReplenishStrategy": "masterdata/inbound/replenish-strategies",
        "deleteReplenishStrategy": "masterdata/inbound/replenish-strategies/#id#",
        // stowEligibility
        "getSelectStowUser": "masterdata/inbound/user-stowthreshold/users",
        "getAssignedStowThresholdByUser": "masterdata/inbound/users/#id#/stowthresholds/assigned",
        "getUnassignedStowThresholdByUser": "masterdata/inbound/users/#id#/stowthresholds/unassigned",
        "saveStowThresholdsByUser": "masterdata/inbound/users/#id#/stowthresholds",
        // *****************************************************outbound****************************************************
        "getDigitalLabelByLabel": "masterdata/outbound/digital-labels/labelId/#ids#",
        // orderStrategy
        "findOrderStrategy": "masterdata/outbound/order-strategies",
        "createOrderStrategy": "masterdata/outbound/order-strategies",
        "getOrderStrategy": "masterdata/outbound/order-strategies",
        "readOrderStrategy": "masterdata/outbound/order-strategies/#id#",
        "updateOrderStrategy": "masterdata/outbound/order-strategies",
        "deleteOrderStrategy": "masterdata/outbound/order-strategies/#id#",
        // pickingCategory
        "findPickingCategory": "masterdata/outbound/picking-categories",
        "createPickingCategory": "masterdata/outbound/picking-categories",
        "readPickingCategory": "masterdata/outbound/picking-categories/#id#",
        "updatePickingCategory": "masterdata/outbound/picking-categories",
        "deletePickingCategory": "masterdata/outbound/picking-categories/#id#",
        // pickingCategoryRule
        "getPickingCategoryRule": "masterdata/outbound/picking-category-rules",
        // pickingAreaAndPPEligibility
        "getUserGroup": "masterdata/outbound/picking-process-eligibility/users",
        "getUseByUserGroupId": "masterdata/outbound/picking-process-eligibility",
        "getAssignedPPByUser": "masterdata/outbound/picking-process-eligibility/process-paths/#id#/usersID/assigned",
        "getUnassignedPPByUser": "masterdata/outbound/picking-process-eligibility/process-paths/#id#/usersID/unassigned",
        "getAssignedAreaByClient": "masterdata/outbound/picking-area-eligibility",
        "getUnassignedAreaByClient": "masterdata/outbound/picking-area-eligibility",
        "saveAreaToUser": "masterdata/outbound/picking-area-eligibility/picking-areas/#id#/pickArea",
        "savePPToUser": "masterdata/outbound/picking-process-eligibility/process-paths/#id#/processPath",
        // pickingAreaEligibility
        "getPickingAreaByClient": "masterdata/outbound/picking-areas",
        "getAssignedUserByPickingArea": "masterdata/outbound/picking-area-eligibility/picking-areas/#id#/users/assigned",
        "getUnassignedUserByPickingArea": "masterdata/outbound/picking-area-eligibility/picking-areas/#id#/users/unassigned",
        "saveUsersByPickingArea": "masterdata/outbound/picking-area-eligibility/picking-areas/#id#/users",
        // pickingProcessEligibility
        "getSelectProcessPath": "masterdata/outbound/process-paths",
        "getAssignedUserByProcessPath": "masterdata/outbound/picking-process-eligibility/process-paths/#id#/users/assigned",
        "getUnassignedUserByProcessPath": "masterdata/outbound/picking-process-eligibility/process-paths/#id#/users/unassigned",
        "saveUsersByProcessPath": "masterdata/outbound/picking-process-eligibility/process-paths/#id#/users",
        // pickPackCellTypeBoxType
        "getBoxTypeByClient": "masterdata/outbound/pick-pack-cell-type/pick-pack-cell-types",
        "getAssignedPickPackCellTypeByBoxType": "masterdata/outbound/pick-pack-cell-types/pick-pack-cell-types/assigned",
        "getUnassignedPickPackCellTypeByBoxType": "masterdata/outbound/pick-pack-cell-types/pick-pack-cell-types/unassigned",
        "savePickPackCellTypesByBoxType": "masterdata/outbound/pick-pack-cell-types/#id#/pick-pack-cell-types",
        //ItemDataGlobal获取数据，为了设置size
        "getItemDataGlobal": "masterdata/item-data-globals",
        "updateItemDataGlobalSize": "masterdata/item-data-globals/upDateSize",
        "getItemUnit": "masterdata/item-units",
        //StorageLocationType类型下拉框数据获取
        "getStorageLocationType": "masterdata/storage-location-types",
        //导入数据的保存
        "saveImportItemData": "masterdata/item-datas/import/file",
        "saveImportItemDataGlobal": "/masterdata/item-data-globals/import/file",
        "saveImportStorageLocation": "masterdata/storage-locations/import/file",
        "batchUpdate":"masterdata/outbound/pick-pack-cells/batchUpdate",
        // *****************************************************warehouse****************************************************
        "getPodPlaceMark": "masterdata/pods/placeMark",
        "getNodeBySectionId": "/masterdata/workstations/sectionId",
        "getstorageLocationName": "/masterdata/storage-locations/exportname",
        "exitWorkStation": "/masterdata/workstations/exit/#id#",
        "getRefreshTime": "/masterdata/inbound/item-data-type-grade-stats/refreshTime",
        "sureRefreshTime": "/masterdata/inbound/item-data-type-grade-stats/saveRefreshTime/#refreshTime#",
        "saveDayNumber": "/masterdata/inbound/item-data-type-grade-stats/saveDayNumber/#dayNumber#",
         //小车点量
        "getRobotBattery":"/masterdata/robot/robots/robotLaveBattery"
    }));
})();