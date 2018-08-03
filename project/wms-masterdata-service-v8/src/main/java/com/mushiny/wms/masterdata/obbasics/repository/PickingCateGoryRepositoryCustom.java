package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;

import java.util.List;

public interface PickingCateGoryRepositoryCustom {

    List<PickingCateGory> getList(Warehouse warehouse, Client client);

    List<PickingCateGory> getListByProcessPath(Warehouse warehouse, ProcessPath processPath);

    PickingCateGory getByName(String warehouse, String client, String name);

    boolean existsByName(Warehouse warehouse, Client client, String name);

    int getMaxIndex(String warehouse, String client);
}
