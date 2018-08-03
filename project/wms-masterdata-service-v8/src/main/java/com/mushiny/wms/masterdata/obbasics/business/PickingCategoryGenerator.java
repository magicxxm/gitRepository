package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import com.mushiny.wms.masterdata.obbasics.constants.PickingOperator;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;

public interface PickingCategoryGenerator {

    PickingCateGory createPickingCategory(String warehouseId, String clientId, String name, ProcessPath processPath) throws FacadeException;

    PickingCateGory addPickingCategoryPosition(PickingCateGory category, PickingCateGoryRule rule, PickingOperator operator, String value) throws FacadeException;
}
