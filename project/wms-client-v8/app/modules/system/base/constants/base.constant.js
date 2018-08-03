/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  // ===================================================================================================================
  var items = ["user", "userGroup", "role", "module", "warehouse", "client", "selection", "resource", "systemProperty"];
  var baseConstant = {};
  for (var i = 0; i < items.length; i++) {
    var item = items[i], key = item.substring(0, 1).toUpperCase() + item.substring(1);
    if (item == "userGroup") item = "usergroup";
    item = "system/" + item;
    baseConstant["find" + key] = item + "s";
    baseConstant["create" + key] = item + "s";
    baseConstant["get" + key] = item + "s";
    baseConstant["read" + key] = item + "s/#id#";
    baseConstant["update" + key] = item + "s";
    baseConstant["delete" + key] = item + "s/#id#";
  }
  angular.module("myApp").constant("SYSTEM_FILTER", {
    "user": [{"field": "username"}, {"field": "client.id"}, {field: "userGroup.name"}, {"field": "entityLock", operator: "eq"}],
    "role": [{"field": "name"}, {"field": "entityLock", operator: "eq"}],
    "module": [{"field": "name"}],
    "userGroup": [{"field": "name"}],
    "warehouse": [{"field": "warehouseNo"}, {"field": "entityLock", operator: "eq"}],
    "client": [{"field": "clientNo"}, {"field": "entityLock", operator: "eq"}],
    "selection": [{"field": "selectionKey"}, {"field": "entityLock", operator: "eq"}],
    "systemProperty": [{"field": "entityLock", operator: "eq"}],
    "resource": [{"field": "resourceKey"}, {"field": "resourceValue"}, {"field": "locale", operator: "eq"}]
  }).constant("SYSTEM_CONSTANT", angular.extend(baseConstant, {
    //保存import数据
    "saveImportUser": "system/users/import/file",
    "saveImportClient": "system/clients/import/file",
    "saveImportRole": "system/roles/import/file",
    // systemProperty
    "findSystemProperty": "system/system-properties",
    "createSystemProperty": "system/system-properties",
    "getSystemProperty": "system/system-properties",
    "readSystemProperty": "system/system-properties/#id#",
    "updateSystemProperty": "system/system-properties",
    "deleteSystemProperty": "system/system-properties/#id#",
    // selection
    "getSelectionBySelectionKey": "system/selections",
    // menu
    "getRootModules": "system/menus/root/modules/assigned",
    "getAssignedModules": "system/menus/#parentId#/modules/assigned",
    "getUnassignedModules": "system/menus/#parentId#/modules/unassigned",
    "saveAssignedModules": "system/menus/#parentId#/menus",
    // rfMenu
    "getRfMenu": "system/rf-menus",
    "getRootRfModules": "system/rf-menus/root/modules/assigned",
    "getAssignedRfModules": "system/rf-menus/#parentId#/modules/assigned",
    "getUnassignedRfModules": "system/rf-menus/#parentId#/modules/unassigned",
    "saveAssignedRfModules": "system/rf-menus/#parentId#/rf-menus",
    // role-module
    "getSelectRole": "system/role-module/roles",
    "getAssignedModuleByRole": "system/roles/#id#/modules/assigned",
    "getUnassignedModuleByRole": "system/roles/#id#/modules/unassigned",
    "saveModulesByRole": "system/roles/#id#/modules",
    "getSelectModule": "system/role-module/modules",
    "getAssignedRoleByModule": "system/modules/#id#/roles/assigned",
    "getUnassignedRoleByModule": "system/modules/#id#/roles/unassigned",
    "saveRolesByModule": "system/modules/#id#/roles",
    // user-warehouse
    "getSelectUser": "system/user-warehouse/users",
    "getAssignedWarehouseByUser": "system/users/#id#/warehouses/assigned",
    "getUnassignedWarehouseByUser": "system/users/#id#/warehouses/unassigned",
    "saveWarehousesByUser": "system/users/#id#/warehouses",
    "getSelectWH": "system/user-warehouse/warehouses",
    "getAssignedUserByWarehouse": "system/warehouses/#id#/users/assigned",
    "getUnassignedUserByWarehouse": "system/warehouses/#id#/users/unassigned",
    "saveUsersByWarehouse": "system/warehouses/#id#/users",
    // warehouse-client
    "getSelectWarehouse": "system/warehouse-client/warehouses",
    "getAssignedClientByWarehouse": "system/warehouses/#id#/clients/assigned",
    "getUnassignedClientByWarehouse": "system/warehouses/#id#/clients/unassigned",
    "saveClientsByWarehouse": "system/warehouses/#id#/clients",
    "getSelectClient": "system/warehouse-client/clients",
    "getAssignedWarehouseByClient": "system/clients/#id#/warehouses/assigned",
    "getUnassignedWarehouseByClient": "system/clients/#id#/warehouses/unassigned",
    "saveWarehousesByClient": "system/clients/#id#/warehouses",
    // user-warehouse-role
    "getWarehouseInRole": "system/warehouses",
    "getUserByWarehouseInRole": "system/user-warehouse-role/warehouses/#id#/users",
    "getUnassignedRoleByWarehouseUser": "system/warehouses/#warehouseId#/users/#userId#/roles/unassigned",
    "getAssignedRoleByWarehouseUser": "system/warehouses/#warehouseId#/users/#userId#/roles/assigned",
    "saveRolesByWarehouseUser": "system/warehouses/#warehouseId#/users/#userId#/roles",
    "getRoleByWarehouse": "system/user-warehouse-role/warehouses/#id#/roles",
    "getUnassignedUserByWarehouseRole": "system/warehouses/#warehouseId#/roles/#roleId#/users/unassigned",
    "getAssignedUserByWarehouseRole": "system/warehouses/#warehouseId#/roles/#roleId#/users/assigned",
    "saveUsersByWarehouseRole": "system/warehouses/#warehouseId#/roles/#roleId#/users"
  }));
})();