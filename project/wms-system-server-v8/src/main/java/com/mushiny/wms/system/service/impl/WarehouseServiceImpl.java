package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.context.PermissionsContext;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;
import com.mushiny.wms.system.crud.mapper.WarehouseMapper;
import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.domain.Warehouse;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.repository.UserWarehouseRoleRepository;
import com.mushiny.wms.system.repository.WarehouseRepository;
import com.mushiny.wms.system.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final PermissionsContext permissionsContext;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                UserWarehouseRoleRepository userWarehouseRoleRepository,
                                PermissionsContext permissionsContext,
                                WarehouseMapper warehouseMapper) {
        this.warehouseRepository = warehouseRepository;
        this.userWarehouseRoleRepository = userWarehouseRoleRepository;
        this.permissionsContext = permissionsContext;
        this.warehouseMapper = warehouseMapper;
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
        Warehouse entity = warehouseRepository.retrieve(id);
        if(!entity.getUsers().isEmpty()){
            throw new ApiException("该仓库已绑定了用户");
        }
        // delete many to many
        entity.setClients(null);
        entity.setUsers(null);
        userWarehouseRoleRepository.deleteByWarehouseId(id);
        // update delete flag
        entity.setEntityLock(Constant.GOING_TO_DELETE);
        warehouseRepository.save(entity);
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
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(), sort);
        }
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
