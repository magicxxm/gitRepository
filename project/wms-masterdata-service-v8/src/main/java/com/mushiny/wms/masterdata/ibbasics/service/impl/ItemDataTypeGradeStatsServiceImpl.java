package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ItemDataTypeGradeStatsDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.TimeConfigDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ItemDataTypeGradeStatsMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.TimeConfigMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ItemDataTypeGradeStats;
import com.mushiny.wms.masterdata.ibbasics.domain.TimeConfig;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ItemDataTypeGradeStatsRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.TimeConfigRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ItemDataTypeGradeStatsService;
import com.mushiny.wms.masterdata.mdbasics.domain.InventoryAnalysis;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;
import com.mushiny.wms.masterdata.mdbasics.repository.InventoryAnalysisRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.SabcRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ItemDataTypeGradeStatsServiceImpl implements ItemDataTypeGradeStatsService {

    private final ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataTypeGradeStatsMapper itemDataTypeGradeStatsMapper;
    private final InventoryAnalysisRepository inventoryAnalysisRepository;
    private final SabcRuleRepository sabcRuleRepository;
    private final TimeConfigRepository timeConfigRepository;
    private final TimeConfigMapper timeConfigMapper;

    @Autowired
    public ItemDataTypeGradeStatsServiceImpl(ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository,
                                             ApplicationContext applicationContext,
                                             ItemDataTypeGradeStatsMapper itemDataTypeGradeStatsMapper,
                                             InventoryAnalysisRepository inventoryAnalysisRepository,
                                             SabcRuleRepository sabcRuleRepository,
                                             TimeConfigRepository timeConfigRepository, TimeConfigMapper timeConfigMapper) {
        this.itemDataTypeGradeStatsRepository = itemDataTypeGradeStatsRepository;
        this.applicationContext = applicationContext;
        this.itemDataTypeGradeStatsMapper = itemDataTypeGradeStatsMapper;
        this.inventoryAnalysisRepository = inventoryAnalysisRepository;
        this.sabcRuleRepository = sabcRuleRepository;
        this.timeConfigRepository = timeConfigRepository;
        this.timeConfigMapper = timeConfigMapper;
    }

    @Override
    public ItemDataTypeGradeStatsDTO create(ItemDataTypeGradeStatsDTO dto) {
        ItemDataTypeGradeStats entity = itemDataTypeGradeStatsMapper.toEntity(dto);
        checkGradeStatsItemData(entity.getItemData());
        return itemDataTypeGradeStatsMapper.toDTO(itemDataTypeGradeStatsRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ItemDataTypeGradeStats entity = itemDataTypeGradeStatsRepository.retrieve(id);
        itemDataTypeGradeStatsRepository.delete(entity);
    }

    @Override
    public ItemDataTypeGradeStatsDTO update(ItemDataTypeGradeStatsDTO dto) {
        ItemDataTypeGradeStats entity = itemDataTypeGradeStatsRepository.retrieve(dto.getId());
        /**1。判断sd有没有发生变化，如果sd发生变化整个热消度等级都发生变化，则修改整个表中热消度等级,此商品的sd,ud,
         * 修改商品库存分析表中的maxDoc,safeDoc,replenishDoc,热消度等级，2.如果sd沒有没有发生变化，us发生变化，依然
         * 修改商品库存分析表中的此商品的maxDoc,safeDoc,replenishDoc，修改此商品的us,ud*/
        if (dto != null && dto.getShipmentDay() != null && dto.getShipmentDay().compareTo(entity.getShipmentDay()) != 0) {
            entity = upDateSabcRule(dto, entity);
        } else if (dto != null && dto.getUnitsShipment() != null && dto.getUnitsShipment().compareTo(entity.getUnitsShipment()) != 0) {
            saveInventoryAnalysis(dto, entity);
            entity = upDateItemDataTypeGradeStats(dto, entity);
        } else {
            if (!dto.getSkuAdjustGrade().equals(entity.getSkuAdjustGrade())) {
                InventoryAnalysis inventoryAnalysis = inventoryAnalysisRepository.getByItem(entity.getWarehouseId(), entity.getClientId(), entity.getItemData());
                inventoryAnalysis.setLevel(dto.getSkuAdjustGrade());
            }
            itemDataTypeGradeStatsMapper.updateEntityFromDTO(dto, entity);
        }
        return itemDataTypeGradeStatsMapper.toDTO(itemDataTypeGradeStatsRepository.save(entity));
    }

    @Override
    public ItemDataTypeGradeStatsDTO retrieve(String id) {
        return itemDataTypeGradeStatsMapper.toDTO(itemDataTypeGradeStatsRepository.retrieve(id));
    }

    @Override
    public List<ItemDataTypeGradeStatsDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ItemDataTypeGradeStats> entities = itemDataTypeGradeStatsRepository.getBySearchTerm(searchTerm, sort);
        return itemDataTypeGradeStatsMapper.toDTOList(entities);
    }

    @Override
    public Page<ItemDataTypeGradeStatsDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<ItemDataTypeGradeStats> entities = itemDataTypeGradeStatsRepository.getBySearchTerm(searchTerm, pageable);
        return itemDataTypeGradeStatsMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ItemDataTypeGradeStatsDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "positionNo"));
        List<ItemDataTypeGradeStats> entities = itemDataTypeGradeStatsRepository.getNotLockList(clientId, sort);
        return itemDataTypeGradeStatsMapper.toDTOList(entities);
    }

    @Override
    public int getDayNumber() {
        TimeConfig timeConfig = timeConfigRepository.getByWarehouseId(applicationContext.getCurrentWarehouse());
        if (timeConfig != null) {
            return timeConfig.getRefreshDay();
        } else {
            return 0;
        }
    }

    @Override
    public TimeConfigDTO getRefreshTime() {
        TimeConfig timeConfig = timeConfigRepository.getByWarehouseId(applicationContext.getCurrentWarehouse());
        if (timeConfig != null) {
            return timeConfigMapper.toDTO(timeConfig);
        } else {
            return null;
        }
    }

    @Override
    public TimeConfigDTO saveRefreshTime(String time) {
        TimeConfig timeConfig = timeConfigRepository.getByWarehouseId(applicationContext.getCurrentWarehouse());
        if (timeConfig != null) {
            timeConfig.setRefreshTime(time);

        } else {
            timeConfig = new TimeConfig();
            timeConfig.setRefreshTime(time);
            timeConfigRepository.save(timeConfig);
        }
        return timeConfigMapper.toDTO(timeConfigRepository.save(timeConfig));
    }

    @Override
    public TimeConfigDTO saveDayNumber(int day) {
        TimeConfig timeConfig = timeConfigRepository.getByWarehouseId(applicationContext.getCurrentWarehouse());
        if (timeConfig != null) {
            timeConfig.setRefreshDay(day);
            timeConfig.setWarehouseId(applicationContext.getCurrentWarehouse());
            timeConfigRepository.save(timeConfig);
        } else {
            timeConfig = new TimeConfig();
            timeConfig.setRefreshDay(day);
            timeConfig.setWarehouseId(applicationContext.getCurrentWarehouse());
            timeConfigRepository.save(timeConfig);
        }
        return timeConfigMapper.toDTO(timeConfigRepository.save(timeConfig));
    }


    /**
     * 设置每个商品库存分析表中的数据
     */
    private void saveInventoryAnalysis(ItemDataTypeGradeStatsDTO dto, ItemDataTypeGradeStats entity) {
        InventoryAnalysis inventoryAnalysis = inventoryAnalysisRepository.getByItem(entity.getWarehouseId(),
                entity.getClientId(), entity.getItemData());
        //根据级别名找到该级别
        if (inventoryAnalysis != null) {
            SabcRule sabcRule = sabcRuleRepository.getByName(inventoryAnalysis.getLevel(), inventoryAnalysis.getWarehouseId());
            if (sabcRule != null) {
                BigDecimal ud = dto.getUnitsShipment().multiply(dto.getShipmentDay());
                if (!dto.getSkuAdjustGrade().equals(entity.getSkuAdjustGrade())) {
                    inventoryAnalysis.setLevel(dto.getSkuAdjustGrade());
                }
                inventoryAnalysis.setSafetyDoc(sabcRule.getSafelyDoc().multiply(ud));
                inventoryAnalysis.setReplenishDoc(sabcRule.getReplenDoc().multiply(ud));
                inventoryAnalysis.setMaxDoc(sabcRule.getMaxDoc().multiply(ud));
                inventoryAnalysisRepository.save(inventoryAnalysis);
            } else {
                throw new ApiException("该商品的InventoryAnalysis的级别对应的SabcRule没有获取到");
            }
        } else {
            throw new ApiException("该商品的InventoryAnalysis没有获取到");
        }
    }

    /**
     * 设置每个商品等级计算页面表中的数据
     */
    private ItemDataTypeGradeStats upDateItemDataTypeGradeStats(ItemDataTypeGradeStatsDTO dto, ItemDataTypeGradeStats entity) {
        entity.setShipmentDay(dto.getShipmentDay());
        entity.setUnitsShipment(dto.getUnitsShipment());
        entity.setUnitsDay(dto.getShipmentDay().multiply(dto.getUnitsShipment()));
        entity.setAdjustExpireDate(dto.getAdjustExpireDate());
        if (!dto.getSkuAdjustGrade().equals(entity.getSkuAdjustGrade())) {
            entity.setAlterState(1);
            entity.setSkuAdjustGrade(dto.getSkuAdjustGrade());
        }
        return entity;
    }

    /**
     * 跟新两个表的商品等级
     */
    private ItemDataTypeGradeStats upDateSabcRule(ItemDataTypeGradeStatsDTO dto, ItemDataTypeGradeStats entity) {
        List<ItemDataTypeGradeStats> itemDataTypeGradeStatsList = itemDataTypeGradeStatsRepository.
                getByWarehouseIdAndClient(entity.getWarehouseId(), entity.getClientId());
        /**目前数据库拿出来的数据里面包含了我要修改的这条数据，sd发生了变化，那么它的整个排序都有可能发生变化，
         * 所以：1.首先我们得遍历目前的集合，找出修改的那个商品，
         *      2.更新修改的这条数据的属性，然后排序
         *      3.此时的数据就是更新了后的数据，但是可能sd发生变化，ud就会改变，那么商品库存分析表对应的那条数据就会发生变化，
         *      4.遍历这个集合，拿出每一条数据*/
        for (ItemDataTypeGradeStats ItemDataTypeGradeStats : itemDataTypeGradeStatsList) {
            if (ItemDataTypeGradeStats.getItemData().getId().equals(dto.getItemDataId())) {
                saveInventoryAnalysis(dto, entity);
                entity = upDateItemDataTypeGradeStats(dto, entity);
                ItemDataTypeGradeStats = entity;
            }
        }
        List<SabcRule> list = sabcRuleRepository.getMaxRule(entity.getWarehouseId());
        //给数据排序
        Collections.sort(itemDataTypeGradeStatsList, new Comparator<ItemDataTypeGradeStats>() {
            @Override
            public int compare(ItemDataTypeGradeStats o1, ItemDataTypeGradeStats o2) {
                if (o1.getShipmentDay().compareTo(o2.getShipmentDay()) == -1) {
                    return 1;
                } else if (o1.getShipmentDay().compareTo(o2.getShipmentDay()) == 1) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        int b = 0;
        for (ItemDataTypeGradeStats itemDataTypeGradeStats : itemDataTypeGradeStatsList) {
            b++;
            BigDecimal size = new BigDecimal(b).divide(new BigDecimal(itemDataTypeGradeStatsList.size()), 4, RoundingMode.CEILING);
            InventoryAnalysis inventoryAnalysis = inventoryAnalysisRepository.getByItem(entity.getWarehouseId(),
                    entity.getClientId(), itemDataTypeGradeStats.getItemData());
            for (SabcRule SabcRule : list) {
                if ((size.compareTo(SabcRule.getFromNo())) == 1 && (size.compareTo(SabcRule.getToNo())) == -1 || (size.compareTo(SabcRule.getToNo())) == 0) {
                    if (itemDataTypeGradeStats.getItemData().getId().equals(entity.getItemData().getId())) {
                        entity.setSkuGrade(SabcRule.getSkuTypeName());
                        if (itemDataTypeGradeStats.getAlterState() == 0) {
                            entity.setSkuAdjustGrade(SabcRule.getSkuTypeName());
                            inventoryAnalysis.setLevel(SabcRule.getSkuTypeName());
                        }
                    } else {
                        if (itemDataTypeGradeStats.getAlterState() == 0) {
                            itemDataTypeGradeStats.setSkuAdjustGrade(SabcRule.getSkuTypeName());
                            inventoryAnalysis.setLevel(SabcRule.getSkuTypeName());
                        }
                        itemDataTypeGradeStats.setSkuGrade(SabcRule.getSkuTypeName());
                        itemDataTypeGradeStatsRepository.save(itemDataTypeGradeStats);
                    }
                    inventoryAnalysisRepository.save(inventoryAnalysis);
                    if (itemDataTypeGradeStats.getAlterState() == 0) {

                    }

                }
            }
        }
        return entity;
    }

    private void checkGradeStatsItemData(ItemData itemData) {
        ItemDataTypeGradeStats gradeStats = itemDataTypeGradeStatsRepository.getByItemData(itemData);
        if (gradeStats != null) {
            throw new ApiException(InBoundException
                    .EX_ITEM_DATA_TYPE_GRADE_STATS_ITEM_DATA_UNIQUE.toString(), itemData.getItemNo());
        }
    }
}
