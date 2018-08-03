package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu, String> {

    void deleteByParentModuleId(String parentModuleId);

    List<Menu> findByParentModuleIdOrderByOrderIndex(String parentModuleId);

    @Query(" select m from Menu m " +
            " left join m.module md " +
            " left join m.parentModule pmd " +
            " where pmd.id = :parentId " +
            " and exists (" +
            " select 1 from Role r " +
            " left join r.modules rm " +
            " where rm.id = md.id" +
            " and exists (" +
            " select 1 from UserWarehouseRole uwr " +
            " where uwr.roleId = r.id " +
            " and uwr.warehouseId = :warehouseId " +
            " and uwr.userId = :userId )" +
            " )" +
            " order by m.orderIndex ")
    List<Menu> getByParentIdAndWarehouseId(@Param("parentId") String parentId,
                                           @Param("warehouseId") String warehouseId,
                                           @Param("userId") String userId);
}
