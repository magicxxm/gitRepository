package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;

public interface PickingCategoryPositionRepositoryCustom {

     PickingCateGoryPosition getByNumber(String number);

     boolean existsByNumber(String number);

     PickingCateGoryPosition getById(String id);

}
