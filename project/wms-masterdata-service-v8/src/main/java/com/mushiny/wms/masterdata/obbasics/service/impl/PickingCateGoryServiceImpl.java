package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.business.EntityGenerator;
import com.mushiny.wms.masterdata.obbasics.common.exception.*;
import com.mushiny.wms.masterdata.obbasics.common.service.BaseServiceImpl;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCateGoryRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickingCateGoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PickingCateGoryServiceImpl extends BaseServiceImpl<PickingCateGory> implements PickingCateGoryService {

    private final Logger log = LoggerFactory.getLogger(PickingCateGoryServiceImpl.class);

    private final PickingCateGoryRepository pickingCategoryRepository;

    private final ApplicationContext applicationContext;

    public PickingCateGoryServiceImpl(ContextService context,
                                      EntityGenerator entityGenerator,
                                      BaseRepository<PickingCateGory, String> baseRepository,
                                      PickingCateGoryRepository pickingCategoryRepository,
                                      ApplicationContext applicationContext) {
        super(context, applicationContext, entityGenerator, baseRepository);
        this.pickingCategoryRepository = pickingCategoryRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    public PickingCateGory create(PickingCateGory pickingCategory) throws BusinessObjectExistsException, BusinessObjectCreationException, BusinessObjectSecurityException {
        PickingCateGory created;
        PickingCateGory exists;
        exists = pickingCategoryRepository.getByName(pickingCategory.getWarehouseId(), pickingCategory.getClientId(), pickingCategory.getName());
        if (exists != null) {
            throw new BusinessObjectExistsException(exists);
        }

        created = super.create(pickingCategory);
        return created;
    }

    @Override
    public PickingCateGory update(PickingCateGory pickingCategory) throws BusinessObjectNotFoundException, BusinessObjectExistsException, BusinessObjectModifiedException, BusinessObjectMergeException, BusinessObjectSecurityException {
        PickingCateGory updated;
        PickingCateGory exists;
        updated = pickingCategoryRepository.findOne(pickingCategory.getId());
        exists = pickingCategoryRepository.getByName(pickingCategory.getWarehouseId(), pickingCategory.getClientId(), pickingCategory.getName());
        if (exists != null && !exists.equals(updated)) {
            throw new BusinessObjectExistsException(exists);
        }

        updated = super.update(pickingCategory);
        return updated;
    }
}
