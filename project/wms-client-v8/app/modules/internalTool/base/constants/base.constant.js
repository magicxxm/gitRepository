/**
 * Created by bian on 2016/9/19.
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
  var items = ["barCode", "inputValidity", "inputValidityQuery", "inventAdjust", "measure", "measureQuery", "moveTool","stockUnitMeasure","lotManager"];
  var baseConstant = {};
  for (var i = 0; i < items.length; i++) {
    var item = items[i],
      key = item.substring(0, 1).toUpperCase() + item.substring(1);
    item = transformItem(item); // 转换item
    baseConstant["find" + key] = item + "s";
    baseConstant["create" + key] = item + "s";
    baseConstant["get" + key] = item + "s";
    baseConstant["read" + key] = item + "s/#id#";
    baseConstant["update" + key] = item + "s";
    baseConstant["delete" + key] = item + "s/#id#";
  }
  angular.module("myApp").constant("INTERNAL_TOOL_FILTER", {
    // "area": [{field: "client.id"}, {"field": "name"}]

  }).constant("INTERNAL_TOOL_CONSTANT", angular.extend(baseConstant, {
    //测量临时
    "inWeight":"http://192.168.1.88:9090/inWeight/",
   // 
    "publicGetItemData":"internal-tool/common/itemdata",
    //移货工具
    "orgContainer": "",
    "getWarehouse": "warehouses",
    "getClient": "system/clients",
    //"saveMeasure": "cubi-scan",
    "selectMeasureSource": "cubi-scan/source",
    "selectMeasureSku": "cubi-scan/sku",
    "selectInventory": "searching/storage-items-records",
    "selectHistory": "searching/storage-items-past-records",
    "selectSKU": "searching/sku",

    "historySelect": "searching/item-storage-past-records",
    "getInputValidityQuery": "",
    "getValidityChangeRecord": "",
    "getGoodsDetail": "",
    "getSizeChangeRecordGrid": "",
    //测量查询
    "getMeasureQuery": "internal-tool/search-measure-goods/records",
    //测量工具
    "scanningSource": "internal-tool/measure-goods/scanning/source",
    "scanningItemData": "internal-tool/measure-goods/scanning/sku",
    "scanningDestination": "internal-tool/measure-goods/scanning/destination",
    "saveMeasure": "internal-tool/measure-goods/measuring",
    //容器查询
    "storageRecords": "internal-tool/search-inventory/storage-records",
    //商品查询
    "getByItemDataNo": "internal-tool/search-inventory/item-data-global/sku",
    "getItemAdjustRecords": "internal-tool/search-inventory/item-adjust-records",
    "getItemPurchasingRecords": "internal-tool/search-inventory/item-purchasing-records",
    "getItemRecords": "internal-tool/search-inventory/item-records",
    //移货
    "moveScanningSource": "internal-tool/move-goods/scanning/source",
    "moveScanningItemData": "internal-tool/move-goods/scanning/sku",
    "moveScanningDestination": "internal-tool/move-goods/scanning/destination",
    "moveMeasuring": "internal-tool/move-goods/moving",
    //有效期录入     /internal-tool/entry-lot
    "validityScanningSource": "internal-tool/entry-lot/scanning/source",
    "validityEntering": "internal-tool/entry-lot/entering",
    "validityScanningItemData": "internal-tool/entry-lot/scanning/sku",
    //条码补打工具 /internal-tool/print-barcode
    "barcodeScanningSKU": "internal-tool/print-barcode/scanning/sku",
    //库存调整工具   /internal-tool/adjust-inventory
    "adjustScanningSource": "internal-tool/adjust-inventory/scanning/source",
    "adjustScanningItemData": "internal-tool/adjust-inventory/scanning/sku",
    "adjustScanningItemDataGlobal":"internal-tool/adjust-inventory/scanning/global-sku",
    "adjustScanningDestination":"internal-tool/adjust-inventory/scanning/destination",
    "adjustCheckUser":"internal-tool/adjust-inventory/checking/username",
    "adjustUpdateInventoryAttributes":"internal-tool/adjust-inventory/inventory/attributes",
    "adjustMoveAllGoods":"internal-tool/adjust-inventory/moving/goods/all",
    "moveGoods":"internal-tool/adjust-inventory/moving/goods",
    //工具—库存调整工具
    "overageGoods":"internal-tool/adjust-inventory/overage-goods",
    "lossGoods":"internal-tool/adjust-inventory/loss-goods",
    "getItemData":"internal-tool/adjust-inventory/storage-location/items",
    //有效期录入查询 /internal-tool/search-entry-lot
    "getBySearchTerm":"internal-tool/search-entry-lot/records",
    //获取规则尺寸
    "getSizeFilterRule":"masterdata/size-filter-rules",
    //获取商品库存信息
    "getAllStockUnit":"internal-tool/stockUnit-measures",
    //获取商品信息
    "getItemdata":"internal-tool/findItem",
    //有效期管理初始化
    "getStockByLot":"internal-tool/findStock",
      //有效期管理查询
    "getByParam":"internal-tool/findByParam",
    //库存查询
    "getByParamSearch":"internal-tool/searchStockUnit",
    //导出所有库存
    "getStockUnitMeasure":"internal-tool/export-stockUnit-measures"
}));
})();