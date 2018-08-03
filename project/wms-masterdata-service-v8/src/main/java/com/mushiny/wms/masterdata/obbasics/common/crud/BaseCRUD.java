package com.mushiny.wms.masterdata.obbasics.common.crud;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BaseCRUD<T extends BaseEntity, S extends BaseDTO> {

    S create(S dto) throws FacadeException;

    void delete(String id) throws FacadeException;

    void delete(S dto) throws FacadeException;

    void delete(List<S> dtos) throws FacadeException;

    S update(S dto) throws FacadeException;

    S get(String id) throws FacadeException;

    void lock(S dto, int lock, String lockCause) throws FacadeException;

    List<S> getList();

    List<S> getList(Sort sort);

    Page<S> getList(Pageable pageable);

    List<S> getListBySearchTerm(String searchTerm) throws FacadeException;

    List<S> getListBySearchTerm(String searchTerm, Sort sort) throws FacadeException;

    Page<S> getListBySearchTerm(String searchTerm, Pageable pageable) throws FacadeException;
}
