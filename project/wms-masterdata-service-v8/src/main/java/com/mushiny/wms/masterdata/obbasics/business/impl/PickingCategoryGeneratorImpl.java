package com.mushiny.wms.masterdata.obbasics.business.impl;

import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.obbasics.business.PickingCategoryGenerator;
import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.business.EntityGenerator;
import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import com.mushiny.wms.masterdata.obbasics.constants.PickingOperator;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;
import com.mushiny.wms.masterdata.obbasics.exception.InventoryException;
import com.mushiny.wms.masterdata.obbasics.exception.InventoryExceptionKey;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCateGoryPositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCateGoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;

@Service
public class PickingCategoryGeneratorImpl implements PickingCategoryGenerator {

    private final Logger log = LoggerFactory.getLogger(PickingCategoryGeneratorImpl.class);

    private final EntityManager manager;

    private final EntityGenerator entityGenerator;

    private final ContextService contextService;

    private final PickingCateGoryRepository pickingCategoryRepository;

    private final PickingCateGoryPositionRepository pickingCategoryPositionRepository;

    public PickingCategoryGeneratorImpl(EntityManager manager,
                                        EntityGenerator entityGenerator,
                                        ContextService contextService,
                                        PickingCateGoryRepository pickingCategoryRepository,
                                        PickingCateGoryPositionRepository pickingCategoryPositionRepository) {
        this.manager = manager;
        this.entityGenerator = entityGenerator;
        this.contextService = contextService;
        this.pickingCategoryRepository = pickingCategoryRepository;
        this.pickingCategoryPositionRepository = pickingCategoryPositionRepository;
    }

    @Override
    public PickingCateGory createPickingCategory(String warehouseId, String clientId, String name, ProcessPath processPath) throws FacadeException {
        String logStr = "createPickingCategory ";
        log.debug(logStr);

//        if (warehouse == null) {
//            warehouse = contextService.getCallersWarehouse();
//        }
//        if (client == null) {
//            client = contextService.getCallersClient();
//        }

        int orderIndex = pickingCategoryRepository.getMaxIndex(warehouseId, clientId);
        PickingCateGory category = null;
        category = entityGenerator.generateEntity(PickingCateGory.class);

        category.setName(name);
        category.setWarehouseId(warehouseId);
        category.setClientId(clientId);
        category.setProcessPath(processPath);
        category.setIndex(orderIndex);

        manager.persist(category);
        manager.flush();

        category.setPositions(new ArrayList<>());

        log.debug(logStr + "New picking category created. name=" + name);

        return category;
    }

    @Override
    public PickingCateGory addPickingCategoryPosition(PickingCateGory category, PickingCateGoryRule rule, PickingOperator operator, String value) throws FacadeException {
        String logStr = "addPickingCategoryPosition ";
        log.debug(logStr + " category=" + category.getName() + ", rule=" + rule.getName());

        String nameCategory = category.getName();
        String number = null;
        int idx = category.getPositions().size();
        int i = 0;
        while (true) {
            i++;
            idx++;
//			number = nameCategory + "-" + String.format("%1$03d", idx);
            number = nameCategory + "-" + idx;
            if (!pickingCategoryPositionRepository.existsByNumber(number)) {
                break;
            }
            if (i > 1000) {
                log.error(logStr + "Cannot get unique order number");
                throw new InventoryException(InventoryExceptionKey.ORDER_NO_UNIQUE_NUMBER, "");
            }
            log.warn(logStr + "Position already exists. try next");
        }

        PickingCateGoryPosition pos = null;
        pos = entityGenerator.generateEntity(PickingCateGoryPosition.class);
        pos.setNumber(number);
        pos.setIndex(idx);
        pos.setPickingCateGoryRule(rule);
        pos.setOperator(operator);
        pos.setValue(value);
        pos.setWarehouseId(category.getWarehouseId());
        pos.setClientId(category.getClientId());
        pos.setPickingCategory(category);

        manager.persist(pos);

        category.getPositions().add(pos);

        return category;
    }
}
