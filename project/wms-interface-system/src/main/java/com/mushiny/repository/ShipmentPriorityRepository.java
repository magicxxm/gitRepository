package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ShipmentPriority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2018/3/21.
 */
public interface ShipmentPriorityRepository extends BaseRepository<ShipmentPriority,String> {

    @Query("select s from ShipmentPriority s where s.name = :name")
    ShipmentPriority getByName(@Param("name")String name);

    @Query("select s from ShipmentPriority s where s.entityLock = 0")
    List<ShipmentPriority> getAll();
}
