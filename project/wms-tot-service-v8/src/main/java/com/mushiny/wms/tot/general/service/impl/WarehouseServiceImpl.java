package com.mushiny.wms.tot.general.service.impl;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.tot.general.context.PermissionsContext;
import com.mushiny.wms.tot.general.crud.dto.WarehouseDTO;
import com.mushiny.wms.tot.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.domain.Warehouse;
import com.mushiny.wms.tot.general.exception.SystemException;
import com.mushiny.wms.tot.general.repository.WarehouseRepository;
import com.mushiny.wms.tot.general.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    @PersistenceContext
    private EntityManager entityManager;

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final PermissionsContext permissionsContext;
    private final ApplicationContext applicationContext;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                WarehouseMapper warehouseMapper,
                                PermissionsContext permissionsContext,
                                ApplicationContext applicationContext) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.permissionsContext = permissionsContext;
        this.applicationContext = applicationContext;
    }

    @Override
    public WarehouseDTO create(WarehouseDTO dto) {
        Warehouse entity = warehouseMapper.toEntity(dto);
        checkWarehouseName(entity.getName());
        checkWarehouseNo(entity.getWarehouseNo());
        return warehouseMapper.toDTO(warehouseRepository.save(entity));
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public WarehouseDTO update(WarehouseDTO dto) {
        Warehouse entity = warehouseRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkWarehouseName(dto.getName());
        }
        if (!entity.getWarehouseNo().equals(dto.getWarehouseNo())) {
            checkWarehouseNo(dto.getWarehouseNo());
        }
        warehouseMapper.updateEntityFromDTO(dto, entity);
        return warehouseMapper.toDTO(warehouseRepository.save(entity));
    }

    @Override
    public WarehouseDTO retrieve(String id) {
        return warehouseMapper.toDTO(warehouseRepository.retrieve(id));
    }

    @Override
    public List<WarehouseDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Warehouse> entities = warehouseRepository.getBySearchTerm(searchTerm, sort);
        return warehouseMapper.toDTOList(entities);
    }

    @Override
    public Page<WarehouseDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<Warehouse> entities = warehouseRepository.getBySearchTerm(searchTerm, pageable);
        return warehouseMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<WarehouseDTO> getByCurrentUserId() {
        // 登录后选择仓库和具体的客户
        User user = permissionsContext.getCurrentUser();
        List<Warehouse> entities = new ArrayList<>();
        entities.addAll(user.getWarehouses());
        return warehouseMapper.toDTOList(entities);
    }

    @Override
    public List<WarehouseDTO> getCurrentWarehouse() {
        String currentWarehouseId = applicationContext.getCurrentWarehouse();
//        String sql ="SELECT NAME from SYS_WAREHOUSE WHERE ID = :currentWarehouseId";
//        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("currentWarehouseId", currentWarehouseId);
//        List<String> warehouseEntity = query.getResultList();
//        String currentWarehouseName = warehouseEntity.get(0);
//        //只有findByName才会用到表SYS_WAREHOUSE中的name
//        Warehouse warehouse = warehouseRepository.findByName(currentWarehouseName);
        Warehouse warehouse = warehouseRepository.retrieve(currentWarehouseId);
        List<Warehouse> entities = new ArrayList<>();
        entities.add(warehouse);
        return warehouseMapper.toDTOList(entities);
    }

    private void checkWarehouseName(String warehouseName) {
        Warehouse warehouse = warehouseRepository.findByName(warehouseName);
        if (warehouse != null) {
            throw new ApiException(SystemException.EX_SYS_WAREHOUSE_NAME_UNIQUE.toString(), warehouseName);
        }
    }

    private void checkWarehouseNo(String warehouseNo) {
        Warehouse warehouse = warehouseRepository.findByWarehouseNo(warehouseNo);
        if (warehouse != null) {
            throw new ApiException(SystemException.EX_SYS_WAREHOUSE_NO_UNIQUE.toString(), warehouseNo);
        }
    }
}
