package com.mushiny.wms.masterdata.obbasics.common.business;


import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.domain.Warehouse;

public interface ContextService {

    User getCallersUser();

    String getCallerUsername();

    Warehouse getCallersWarehouse();

    Client getCallersClient();

    String getCallersLocale();

    boolean checkClient(BaseEntity be);

    BaseEntity eagerRead(BaseEntity be);
}
