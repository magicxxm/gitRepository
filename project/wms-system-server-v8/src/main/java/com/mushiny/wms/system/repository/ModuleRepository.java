package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Module;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleRepository extends BaseRepository<Module, String> {

    Module findByName(String name);

    List<Module> findAllByOrderByName();

    @Query(" select m from Module m " +
            " where exists (" +
            " select 1 from Role r " +
            " left join r.modules rm " +
            " where rm.id = m.id " +
            " and exists (" +
            " select 1 from UserWarehouseRole uwr " +
            " where uwr.roleId = r.id " +
            " and uwr.warehouseId = :warehouseId " +
            " and uwr.userId = :userId )" +
            " )" +
            " order by m.name ")
    List<Module> getByWarehouseIdAndUserId(@Param("warehouseId") String warehouseId,
                                           @Param("userId") String userId);

    @Query(" select m from Module m " +
            " where m.id <> 'ROOT' " +
            " and not exists (" +
            " select 1 from Role r " +
            " left join r.modules rm " +
            " where rm.id = m.id " +
            " and r.id = :roleId )" +
            " order by m.name ")
    List<Module> getUnassignedRoleModules(@Param("roleId") String roleId);

    @Query(" select md from Module md " +
            " where md.id <> 'ROOT' " +
            " and md.id <> :parentId " +
            " and md.moduleType in('FORM','MENU') " +
            " and md.dkActive = true " +
            " and not exists (" +
            " select 1 from Menu m " +
            " left join m.module mdl " +
            " where mdl.id = md.id " +
            " and m.parentModule.id = :parentId) " +
            " order by md.name ")
    List<Module> getUnassignedMenuModules(@Param("parentId") String parentModuleId);

    @Query(" select md from Module md " +
            " where md.id <> 'ROOT' " +
            " and md.moduleType = 'MENU' " +
            " and md.rfActive = true " +
            " order by md.name ")
    List<Module> getAllRfMenuModules();

    @Query(" select md from Module md " +
            " where md.id <> 'ROOT' " +
            " and md.id <> :parentId " +
            " and md.moduleType in('FORM','MENU') " +
            " and md.rfActive = true " +
            " and not exists (" +
            " select 1 from RfMenu m " +
            " left join m.module mdl " +
            " where mdl.id = md.id " +
            " and m.parentModule.id = :parentId) " +
            " order by md.name ")
    List<Module> getUnassignedRfMenuModules(@Param("parentId") String parentModuleId);
}
