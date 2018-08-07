package com.mushiny.repository;

import com.mushiny.model.UnitLoad;

/**
 * Created by 123 on 2018/2/8.
 */
public interface UnitLoadRepositoryCustom {

    UnitLoad getByStorageLocation(String storageLocationName, String id);
}
