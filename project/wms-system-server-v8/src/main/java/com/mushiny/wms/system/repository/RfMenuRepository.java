package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Module;
import com.mushiny.wms.system.domain.RfMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RfMenuRepository extends BaseRepository<RfMenu, String> {

    void deleteByParentModuleId(String parentModuleId);

    List<RfMenu> findByModuleOrderByOrderIndex(Module module);

    List<RfMenu> findByParentModuleIdOrderByOrderIndex(String parentModuleId);

    @Query(" select m from RfMenu m " +
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
    List<RfMenu> getByParentIdAndWarehouseId(@Param("parentId") String parentId,
                                             @Param("warehouseId") String warehouseId,
                                             @Param("userId") String userId);
}
