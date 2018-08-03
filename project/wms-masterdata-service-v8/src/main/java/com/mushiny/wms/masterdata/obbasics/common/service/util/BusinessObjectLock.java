package com.mushiny.wms.masterdata.obbasics.common.service.util;

import java.io.Serializable;

public interface BusinessObjectLock extends Serializable {

    int getLock();

    String getMessage();

    String getMessageKey();
}
