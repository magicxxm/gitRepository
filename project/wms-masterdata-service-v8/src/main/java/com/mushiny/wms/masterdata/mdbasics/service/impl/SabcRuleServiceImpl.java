package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.domain.ItemDataTypeGradeStats;
import com.mushiny.wms.masterdata.ibbasics.repository.ItemDataTypeGradeStatsRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SabcRuleDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.SabcRuleMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.InventoryAnalysis;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;
import com.mushiny.wms.masterdata.mdbasics.repository.InventoryAnalysisRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.SabcRuleRepository;
import com.mushiny.wms.masterdata.mdbasics.service.SabcRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class SabcRuleServiceImpl implements SabcRuleService {

    private final SabcRuleRepository sabcRuleRepository;
    private final ApplicationContext applicationContext;
    private final SabcRuleMapper sabcRuleMapper;
    private final InventoryAnalysisRepository inventoryAnalysisRepository;
    private final ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository;

    @Autowired
    public SabcRuleServiceImpl(SabcRuleRepository sabcRuleRepository,
                               ApplicationContext applicationContext,
                               SabcRuleMapper sabcRuleMapper,
                               InventoryAnalysisRepository inventoryAnalysisRepository,
                               ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository) {
        this.sabcRuleRepository = sabcRuleRepository;
        this.applicationContext = applicationContext;
        this.sabcRuleMapper = sabcRuleMapper;
        this.inventoryAnalysisRepository = inventoryAnalysisRepository;
        this.itemDataTypeGradeStatsRepository = itemDataTypeGradeStatsRepository;
    }

    @Override
    public List<SabcRuleDTO> getByClientId(String clientId) {
        return null;
    }

    @Override
    public List<SabcRuleDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "skuTypeName"));
        List<SabcRule> entities = sabcRuleRepository.getList(null, sort);
        return sabcRuleMapper.toDTOList(entities);
    }

    @Override
    public SabcRuleDTO create(SabcRuleDTO dto) {
        SabcRule entity = sabcRuleMapper.toEntity(dto);
        checkSabcRuleName(entity.getSkuTypeName());
        return sabcRuleMapper.toDTO(sabcRuleRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        SabcRule entity = sabcRuleRepository.retrieve(id);
        sabcRuleRepository.delete(entity);
    }

    @Override
    public SabcRuleDTO update(SabcRuleDTO dto) {
        SabcRule entity = sabcRuleRepository.retrieve(dto.getId());
        sabcRuleMapper.updateEntityFromDTO(dto, entity);
        entity = sabcRuleRepository.save(entity);
        /**如果sabc等级中的doc发生变化，
         * 1.我们拿出商品库存分析表中该级别的数据，遍历所有数据
         * 2.拿出每一条数据找到商品id,通过商品id找到对应的商品等级计算页面的ud*/
        List<InventoryAnalysis> inventoryAnalysisList = inventoryAnalysisRepository.getBySabcRule
                (applicationContext.getCurrentWarehouse(), applicationContext.getCurrentClient(), entity.getSkuTypeName());
        for (InventoryAnalysis inventoryAnalysis : inventoryAnalysisList) {
            ItemDataTypeGradeStats itemDataTypeGradeStats = itemDataTypeGradeStatsRepository.
                    getByItemData(inventoryAnalysis.getItemData());
            BigDecimal ud = itemDataTypeGradeStats.getUnitsDay();
            inventoryAnalysis.setMaxDoc(ud.multiply(entity.getMaxDoc()));
            inventoryAnalysis.setReplenishDoc(ud.multiply(entity.getSafelyDoc()));
            inventoryAnalysis.setSafetyDoc(ud.multiply(entity.getSafelyDoc()));
            inventoryAnalysisRepository.save(inventoryAnalysis);
        }
        return sabcRuleMapper.toDTO(entity);
    }

    @Override
    public SabcRuleDTO retrieve(String id) {
        return sabcRuleMapper.toDTO(sabcRuleRepository.retrieve(id));
    }

    @Override
    public List<SabcRuleDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<SabcRule> entities = sabcRuleRepository.getBySearchTerm(searchTerm, sort);
        return sabcRuleMapper.toDTOList(entities);
    }

    @Override
    public Page<SabcRuleDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "fromNo"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<SabcRule> entities = sabcRuleRepository.getBySearchTerm(searchTerm, pageable);
        return sabcRuleMapper.toDTOPage(pageable, entities);
    }

    private void checkSabcRuleName(String skuTypeName) {
        SabcRule sabcRule = sabcRuleRepository.getByName(skuTypeName, applicationContext.getCurrentWarehouse());
        if (sabcRule != null) {
            throw new ApiException("该skuTypeName已经创建");
        }
    }

}