package com.mushiny.wms.schedule.common;

import java.io.Serializable;

public interface BusinessObjectLock extends Serializable {

    int getLock();

    String getMessage();

    String getMessageKey();
}
