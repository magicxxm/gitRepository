package com.mushiny.auth.service;

import com.mushiny.auth.domain.User;
import com.mushiny.auth.domain.Warehouse;

public interface ContextService {

    User getCallersUser();

    String getCallerUsername();

    Warehouse getCallersWarehouse();

    Warehouse getCurrentWarehouse();
}
