package com.mushiny.auth.repository;

import com.mushiny.auth.domain.Warehouse;

public interface WarehouseRepositoryCustom {

    Warehouse getByNumber(String number);

    Warehouse getByName(String name);

    Warehouse getSystemWarehouse();
}
