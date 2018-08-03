package com.mushiny.wms.masterdata.general.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.general.domain.UserGroup;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGroupRepository extends BaseRepository<UserGroup, String> {
    @Query("select u from UserGroup u " +
            " where u.entityLock = :entityLock")
    List<UserGroup> getByEntityLock(@Param("entityLock") Integer entityLock);
}
