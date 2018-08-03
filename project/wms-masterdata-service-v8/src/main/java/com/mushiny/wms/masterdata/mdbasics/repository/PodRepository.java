package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Pod;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PodRepository extends BaseRepository<Pod, String> {

//    @Modifying
//    @Query(" update Pod set podIndex = podIndex - 1 " +
//            " where podIndex > :podIndex and warehouseId = :warehouse ")
//    void updateDeletePodIndex(@Param("podIndex") int podIndex,
//                              @Param("warehouse") String warehouse);

    @Query(" select b from Pod b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    Pod getByName(@Param("warehouse") String warehouse,
                  @Param("name") String name);

    @Query(" select b from Pod b " +
            " where b.warehouseId = :warehouse and b.id = :id")
    Pod getById(@Param("warehouse") String warehouse,
                  @Param("id") String id);
//    @Query(" select b from Pod b " +
//            " where b.warehouseId = :warehouse and b.podIndex = (" +
//            " select coalesce(max(mb.podIndex), 0) from Pod mb where mb.warehouseId = b.warehouseId)")
//    Pod getMaxIndexPod(@Param("warehouse") String warehouse);

    @Query(" select coalesce(max(mb.podIndex), 0) from Pod mb where mb.warehouseId = :warehouse")
    int getMaxIndexPod(@Param("warehouse") String warehouse);

    @Query(" select p from Pod p where p.podIndex = :podIndex and p.warehouseId = :warehouse")
    Pod getPod(@Param("podIndex") int podIndex,@Param("warehouse") String warehouse);
}
