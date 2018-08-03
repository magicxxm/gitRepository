package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGroupRepository extends BaseRepository<UserGroup, String> {

    UserGroup findByName(String name);

    List<UserGroup> findAllByOrderByName();

    @Query("select u.id from UserGroup u where  u.entityLock = :entityLock and u.name=:name")
    String findIdByName(@Param("entityLock") Integer entityLock, @Param("name")String name);

}
