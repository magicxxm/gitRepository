package com.mushiny.wms.masterdata.obbasics.common.service;

import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.obbasics.common.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BaseService<T extends BaseEntity> {

    T create() throws BusinessObjectExistsException, BusinessObjectCreationException, BusinessObjectSecurityException;

    T create(T entity) throws BusinessObjectExistsException, BusinessObjectCreationException, BusinessObjectSecurityException;

    void delete(String id) throws BusinessObjectNotFoundException, BusinessObjectDeleteException, BusinessObjectSecurityException;

    void delete(T entity) throws BusinessObjectNotFoundException, BusinessObjectDeleteException, BusinessObjectSecurityException;

    void delete(List<T> entities) throws BusinessObjectNotFoundException, BusinessObjectDeleteException, BusinessObjectSecurityException;

    T update(T entity) throws BusinessObjectNotFoundException, BusinessObjectExistsException, BusinessObjectModifiedException, BusinessObjectMergeException, BusinessObjectSecurityException;

    T get(String id) throws BusinessObjectNotFoundException, BusinessObjectSecurityException;

    void mergeInto(T from, T to) throws BusinessObjectMergeException, BusinessObjectSecurityException;

    void lock(T entity, int lock, String lockCause) throws BusinessObjectSecurityException;

    List<T> getList();

    List<T> getList(Sort sort);

    Page<T> getList(Pageable pageable);

    List<T> getListBySearchTerm(String searchTerm) throws BusinessObjectQueryException;

    List<T> getListBySearchTerm(String searchTerm, Sort sort) throws BusinessObjectQueryException;

    Page<T> getListBySearchTerm(String searchTerm, Pageable pageable) throws BusinessObjectQueryException;
}
