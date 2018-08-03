package com.mushiny.wms.masterdata.obbasics.common.service;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.business.EntityGenerator;
import com.mushiny.wms.masterdata.obbasics.common.exception.*;
import com.mushiny.wms.masterdata.obbasics.common.util.GenericTypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    private final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    private final Class<T> entityClass;

    private final ContextService context;

    private final ApplicationContext applicationContext;

    private final EntityGenerator entityGenerator;

    private final BaseRepository<T, String> baseRepository;

    public BaseServiceImpl(ContextService context,
                           ApplicationContext applicationContext,
                           EntityGenerator entityGenerator,
                           BaseRepository<T, String> baseRepository) {
        this.applicationContext = applicationContext;
        Class clazz = getClass();
        GenericTypeResolver<T> genericTypeResolver = new GenericTypeResolver();
        this.entityClass = genericTypeResolver.resolveGenericType(clazz);
        this.context = context;
        this.entityGenerator = entityGenerator;
        this.baseRepository = baseRepository;
    }

    @Override
    public T create() throws BusinessObjectExistsException, BusinessObjectCreationException, BusinessObjectSecurityException {
        try {
            T entity = entityGenerator.generateEntity(entityClass);

            if (!checkClient(entity)) {
                throw new BusinessObjectSecurityException(getCallersUser());
            }
            baseRepository.save(entity);
            return entity;
        } catch (Throwable t) {
            throw new BusinessObjectCreationException();
        }
    }

    @Override
    public T create(T entity) throws BusinessObjectExistsException, BusinessObjectCreationException, BusinessObjectSecurityException {
        T created;

        if (entity == null) {
            throw new BusinessObjectCreationException();
        }

        if (!checkClient(entity)) {
            throw new BusinessObjectSecurityException(getCallersUser());
        }

        try {
            created = entityGenerator.generateEntity(entityClass);
            mergeInto(entity, created);
            baseRepository.save(created);
            return created;
        } catch (Throwable t) {
            throw new BusinessObjectCreationException();
        }
    }

    @Override
    public void delete(String id) throws BusinessObjectNotFoundException, BusinessObjectDeleteException, BusinessObjectSecurityException {
        if (id == null) {
            throw new BusinessObjectDeleteException();
        }

        T deleted = baseRepository.findOne(id);

        if (!checkClient(deleted)) {
            throw new BusinessObjectSecurityException(getCallersUser());
        }

        try {
            baseRepository.delete(deleted);
        } catch (IllegalArgumentException iaex) {
            throw new BusinessObjectNotFoundException();
        } catch (Throwable t) {
            throw new BusinessObjectDeleteException();
        }
    }

    @Override
    public void delete(T entity) throws BusinessObjectNotFoundException, BusinessObjectDeleteException, BusinessObjectSecurityException {
        if (entity == null) {
            throw new BusinessObjectDeleteException();
        }

        if (!checkClient(entity)) {
            throw new BusinessObjectSecurityException(getCallersUser());
        }

        try {
            entity = baseRepository.findOne(entity.getId());
            baseRepository.delete(entity);
        } catch (IllegalArgumentException iaex) {
            log.error(iaex.getMessage(), iaex);
            throw new BusinessObjectNotFoundException();
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new BusinessObjectDeleteException();
        }
    }

    @Override
    public void delete(List<T> entities) throws BusinessObjectNotFoundException, BusinessObjectDeleteException, BusinessObjectSecurityException {
        if (entities == null) {
            throw new BusinessObjectDeleteException();
        }

        int i = 0;
        for (T entity : entities) {
            try {
                T deleted = baseRepository.findOne(entity.getId());
                if (!checkClient(deleted)) {
                    throw new BusinessObjectSecurityException(getCallersUser());
                }
                baseRepository.delete(deleted);
                if (i++ % 30 == 0) {
                    baseRepository.flush();
                }
            } catch (IllegalArgumentException iaex) {
                throw new BusinessObjectNotFoundException();
            } catch (Throwable t) {
                throw new BusinessObjectDeleteException();
            }
        }
    }

    @Override
    public T update(T entity) throws BusinessObjectNotFoundException, BusinessObjectExistsException, BusinessObjectModifiedException, BusinessObjectMergeException, BusinessObjectSecurityException {
        T updated;

        if (entity == null) {
            throw new BusinessObjectNotFoundException();
        }

        if (!checkClient(entity)) {
            throw new BusinessObjectSecurityException(getCallersUser());
        }

        baseRepository.save(entity);

        updated = baseRepository.findOne(entity.getId());
        return updated;
    }

    @Override
    public T get(String id) throws BusinessObjectNotFoundException, BusinessObjectSecurityException {
        T entity;
        try {
            entity = baseRepository.findOne(id);

            if (entity == null) {
                throw new BusinessObjectNotFoundException(id, entityClass);
            }

            if (!checkClient(entity)) {
                throw new BusinessObjectSecurityException(getCallersUser());
            }
            return (T) eagerRead(entity);
        } catch (Throwable t) {
            throw new BusinessObjectNotFoundException(id, entityClass);
        }
    }

    @Override
    public void mergeInto(T from, T to) throws BusinessObjectMergeException, BusinessObjectSecurityException {
//        BaseEntityMerger<T> merger = new BaseEntityMerger<>();
//        try {
//            merger.mergeInto(from, to);
//        } catch (BaseEntityMergeException ex) {
//            throw new BusinessObjectMergeException();
//        }
    }

    @Override
    public void lock(T entity, int lock, String lockCause) throws BusinessObjectSecurityException {
        User user = getCallersUser();

        if (!checkClient(entity)) {
            throw new BusinessObjectSecurityException(user);
        }

        T locked = baseRepository.findOne(entity.getId());
        locked.setEntityLock(lock);
        entity.addAdditionalContent("L " + user.getName() + " : " + lockCause);
        baseRepository.save(locked);
    }

    @Override
    public List<T> getList() {
        List<T> entities = baseRepository.getList(getCallersWarehouse(), getCallersClient());
        return entities;
    }

    @Override
    public List<T> getList(Sort sort) {
//        List<T> entities = baseRepository.getList(getCallersWarehouse(), getCallersClient(), sort);
        List<T> entities = baseRepository.getList(applicationContext.getCurrentWarehouse(), applicationContext.getCurrentClient(), sort);
        return entities;
    }

    @Override
    public Page<T> getList(Pageable pageable) {
        Page<T> page = baseRepository.getList(applicationContext.getCurrentWarehouse(), applicationContext.getCurrentClient(), pageable);
        return page;
    }

    @Override
    public List<T> getListBySearchTerm(String searchTerm) throws BusinessObjectQueryException {
        List<T> entities;
        try {
            entities = baseRepository.getListBySearchTerm(getCallersWarehouse(), getCallersClient(), searchTerm);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new BusinessObjectQueryException();
        }
        return entities;
    }

    @Override
    public List<T> getListBySearchTerm(String searchTerm, Sort sort) throws BusinessObjectQueryException {
        List<T> entities;
        try {
            entities = baseRepository.getListBySearchTerm(getCallersWarehouse(), getCallersClient(), searchTerm, sort);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new BusinessObjectQueryException();
        }
        return entities;
    }

    @Override
    public Page<T> getListBySearchTerm(String searchTerm, Pageable pageable) throws BusinessObjectQueryException {
        Page<T> page;
        try {
            page = baseRepository.getListBySearchTerm(applicationContext.getCurrentWarehouse(), applicationContext.getCurrentClient(), searchTerm, pageable);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new BusinessObjectQueryException();
        }
        return page;
    }

    protected boolean checkClient(BaseEntity entity) {
        return context.checkClient(entity);
    }

    protected User getCallersUser() {
        return context.getCallersUser();
    }

    protected Warehouse getCallersWarehouse() {
        return context.getCallersWarehouse();
    }

    protected Client getCallersClient() {
        return context.getCallersClient();
    }

    protected BaseEntity eagerRead(BaseEntity entity) {
        return context.eagerRead(entity);
    }
}
