package com.mushiny.wms.masterdata.obbasics.common.crud;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.obbasics.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import com.mushiny.wms.masterdata.obbasics.common.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class BaseCRUDImpl<T extends BaseEntity, S extends BaseDTO> implements BaseCRUD<T, S> {

    private final Logger log = LoggerFactory.getLogger(BaseCRUDImpl.class);

    private final BaseService<T> baseService;

    private final BaseMapper<T, S> baseMapper;

    public BaseCRUDImpl(BaseService<T> baseService, BaseMapper<T, S> baseMapper) {
        this.baseService = baseService;
        this.baseMapper = baseMapper;
    }

    @Override
    public S create(S dto) throws FacadeException {
        T entity = baseMapper.mapDTOIntoEntity(dto);
        T created = baseService.create(entity);
        return baseMapper.mapEntityIntoDTO(created);
    }

    @Override
    public void delete(String id) throws FacadeException {
        baseService.delete(id);
    }

    @Override
    public void delete(S dto) throws FacadeException {
        T entity = baseMapper.mapDTOIntoEntity(dto);
        baseService.delete(entity);
    }

    @Override
    public void delete(List<S> dtos) throws FacadeException {
        List<T> entities = baseMapper.mapDTOsIntoEntities(dtos);
        baseService.delete(entities);
    }

    @Override
    public S update(S dto) throws FacadeException {
        T updated = baseService.get(dto.getId());
        baseMapper.updateEntityFromDTO(dto, updated);
        updated = baseService.update(updated);
        return baseMapper.mapEntityIntoDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public S get(String id) throws FacadeException {
        T entity = baseService.get(id);
        return baseMapper.mapEntityIntoDTO(entity);
    }

    @Override
    public void lock(S dto, int lock, String lockCause) throws FacadeException {
        T entity = baseMapper.mapDTOIntoEntity(dto);
        baseService.lock(entity, lock, lockCause);
    }

    @Override
    @Transactional(readOnly = true)
    public List<S> getList() {
        List<T> entities = baseService.getList();
        return baseMapper.mapEntitiesIntoDTOs(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<S> getList(Sort sort) {
        List<T> entities = baseService.getList(sort);
        return baseMapper.mapEntitiesIntoDTOs(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<S> getList(Pageable pageable) {
        Page<T> page = baseService.getList(pageable);
        return baseMapper.mapEntityPageIntoDTOPage(pageable, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<S> getListBySearchTerm(String searchTerm) throws FacadeException {
        List<T> entities = baseService.getListBySearchTerm(searchTerm);
        return baseMapper.mapEntitiesIntoDTOs(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<S> getListBySearchTerm(String searchTerm, Sort sort) throws FacadeException {
        List<T> entities = baseService.getListBySearchTerm(searchTerm, sort);
        return baseMapper.mapEntitiesIntoDTOs(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<S> getListBySearchTerm(String searchTerm, Pageable pageable) throws FacadeException {
        Page<T> page = baseService.getListBySearchTerm(searchTerm, pageable);
        return baseMapper.mapEntityPageIntoDTOPage(pageable, page);
    }
}
