package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.crud.dto.RoleDTO;
import com.mushiny.wms.system.crud.mapper.RoleMapper;
import com.mushiny.wms.system.domain.Role;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.importUitl.ReadExcelRole;
import com.mushiny.wms.system.repository.RoleRepository;
import com.mushiny.wms.system.repository.UserWarehouseRoleRepository;
import com.mushiny.wms.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           UserWarehouseRoleRepository userWarehouseRoleRepository,
                           RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userWarehouseRoleRepository = userWarehouseRoleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDTO create(RoleDTO dto) {
        Role entity = roleMapper.toEntity(dto);
        checkRoleName(entity.getName());
        return roleMapper.toDTO(roleRepository.save(entity));
    }
    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelRole readExcel = new ReadExcelRole();
        //解析excel，获取上传的事件单
        List<RoleDTO> roleDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(roleDTOList);
    }
    @Override
    public void delete(String id) {
        Role entity = roleRepository.retrieve(id);
        // delete many to many
        userWarehouseRoleRepository.deleteByRoleId(id);
        // update delete flag
        roleRepository.delete(entity);
    }

    @Override
    public RoleDTO update(RoleDTO dto) {
        Role entity = roleRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkRoleName(dto.getName());
        }
        roleMapper.updateEntityFromDTO(dto, entity);
        return roleMapper.toDTO(roleRepository.save(entity));
    }

    @Override
    public RoleDTO retrieve(String id) {
        return roleMapper.toDTO(roleRepository.retrieve(id));
    }

    @Override
    public List<RoleDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Role> entities = roleRepository.getBySearchTerm(searchTerm, sort);
        return roleMapper.toDTOList(entities);
    }

    @Override
    public Page<RoleDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), 1000000, sort);
        } else {
            pageable = new PageRequest(pageable.getPageNumber(), 1000000, pageable.getSort());
        }
        Page<Role> entities = roleRepository.getBySearchTerm(searchTerm, pageable);
        return roleMapper.toDTOPage(pageable, entities);
    }

    private void checkRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            throw new ApiException(SystemException.EX_SYS_ROLE_NAME_UNIQUE.toString(), roleName);
        }
    }

    public void createImport( List<RoleDTO> roleDTOList) {
        for (RoleDTO roleDTO : roleDTOList) {
            Role entity = roleMapper.toEntity(roleDTO);
            checkRoleName(entity.getName());
            roleRepository.save(entity);
        }
    }
}
