package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ItemDataTypeGradeStats;
import com.mushiny.wms.masterdata.ibbasics.domain.ReplenishStrategy;
import com.mushiny.wms.masterdata.ibbasics.repository.ItemDataTypeGradeStatsRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.ReplenishStrategyRepository;
import com.mushiny.wms.masterdata.mdbasics.business.ItemDateInCostomerPosition;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.CustomerShipmentPosition;
import com.mushiny.wms.masterdata.mdbasics.domain.InventoryAnalysis;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.*;
import com.mushiny.wms.masterdata.mdbasics.service.ItemDataService;
import com.mushiny.wms.masterdata.mdbasics.util.ReadExcelItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ItemDataServiceImpl implements ItemDataService {

    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataMapper itemDataMapper;
    private final ClientRepository clientRepository;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository;
    private final ReplenishStrategyRepository replenishStrategyRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final SabcRuleRepository sabcRuleRepository;
    private final InventoryAnalysisRepository inventoryAnalysisRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ItemDataServiceImpl(ItemDataRepository itemDataRepository,
                               ApplicationContext applicationContext,
                               ItemDataMapper itemDataMapper, ClientRepository clientRepository, ItemDataGlobalRepository itemDataGlobalRepository, ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository, ReplenishStrategyRepository replenishStrategyRepository, CustomerShipmentPositionRepository customerShipmentPositionRepository, SabcRuleRepository sabcRuleRepository, InventoryAnalysisRepository inventoryAnalysisRepository) {
        this.itemDataRepository = itemDataRepository;
        this.applicationContext = applicationContext;
        this.itemDataMapper = itemDataMapper;
        this.clientRepository = clientRepository;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.itemDataTypeGradeStatsRepository = itemDataTypeGradeStatsRepository;
        this.replenishStrategyRepository = replenishStrategyRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.sabcRuleRepository = sabcRuleRepository;
        this.inventoryAnalysisRepository = inventoryAnalysisRepository;
    }

    @Override
    public ItemDataDTO create(ItemDataDTO dto) {
        ItemData entity = itemDataMapper.toEntity(dto);
        checkItemNo(entity.getWarehouseId(), entity.getClientId(), entity.getItemNo());
        ItemData ItemData = itemDataRepository.save(entity);
        /*****************************添加到热销度状态表中******添加到库存分心表*********************************************/
        //计算热消度等级，刷新所有商品的的热消度等级
//        List<ItemDataTypeGradeStats> ItemDataTypeGradeStatsList = itemDataTypeGradeStatsRepository.
//                getByWarehouseIdAndClient(entity.getWarehouseId(), entity.getClientId());
//        //在补货策略表中查出s/d   u/s  因为只有一条数据，所以根据client和wearhouse查询
//        ReplenishStrategy replenishStrategy = replenishStrategyRepository.getByClient(applicationContext.getCurrentWarehouse(), applicationContext.getCurrentClient());
//        List<SabcRule> SabcRuleList = sabcRuleRepository.getMaxRule(entity.getWarehouseId());
//        List<SabcRule> sabcRuleSmList = sabcRuleRepository.getSmRule(entity.getWarehouseId());
//        /**1.sd和su 可以在补货策略表中找到，ud可以计算出来，2.设置该商品的所有信息，3.拿出商品等级计算表中的所有数据，
//         * 4.在该集合中加入新增的等级计算数据，5.将该集合排序，6，遍历商品等级计算表中的每一条数据，累计到了第几条数据
//         * 7，计算出sku累计占比，遍历热消度等级表，判断是否属于该等级，*/
//        if (replenishStrategy != null) {
//            if (ItemDataTypeGradeStatsList.size() == 0) {
//                //设置商品等级计算页面数据和商品库存分析页面数据
//                addItemDataTypeGradeStatsAnd(ItemData, replenishStrategy, sabcRuleSmList.get(0));
//            } else {
//                ItemDataTypeGradeStats itemDataTypeGradeStats = new ItemDataTypeGradeStats();
//                itemDataTypeGradeStats.setClientId(ItemData.getClientId());
//                itemDataTypeGradeStats.setWarehouseId(ItemData.getWarehouseId());
//                itemDataTypeGradeStats.setAdjustExpireDate(ItemData.getCreatedDate());
//                itemDataTypeGradeStats.setUnitsShipment(replenishStrategy.getUnitsShipment());
//                itemDataTypeGradeStats.setShipmentDay(replenishStrategy.getShipmentDay());
//                BigDecimal ud = replenishStrategy.getUnitsShipment().multiply(replenishStrategy.getShipmentDay());
//                itemDataTypeGradeStats.setUnitsDay(ud);
//                itemDataTypeGradeStats.setItemData(ItemData);
//                itemDataTypeGradeStats.setAlterState(0);
//                //添加该数据然后排序
//                ItemDataTypeGradeStatsList.add(itemDataTypeGradeStats);
//                Collections.sort(ItemDataTypeGradeStatsList, new Comparator<ItemDataTypeGradeStats>() {
//                    @Override
//                    public int compare(ItemDataTypeGradeStats o1, ItemDataTypeGradeStats o2) {
//                        if (o1.getShipmentDay().intValue() < o2.getShipmentDay().intValue()) {
//                            return 1;
//                        } else if (o1.getShipmentDay().intValue() > o2.getShipmentDay().intValue()) {
//                            return -1;
//                        } else {
//                            return 0;
//                        }
//                    }
//                });
//                int a = 0;
//                int addNo=0;
//                for (ItemDataTypeGradeStats itemDataTypeGrade : ItemDataTypeGradeStatsList) {
//                    String sabcSizeName="";
//                    a++;
//                    for (SabcRule sabcRule : SabcRuleList) {
//                        BigDecimal b = new BigDecimal(a).divide(new BigDecimal(ItemDataTypeGradeStatsList.size()), 4, RoundingMode.CEILING);
//                        if ((b.compareTo(sabcRule.getFromNo())) == 1 && (b.compareTo(sabcRule.getToNo())) == -1 || (b.compareTo(sabcRule.getToNo())) == 0) {
//                            itemDataTypeGrade.setSkuGrade(sabcRule.getSkuTypeName());
//                            //修改等级没有修改过，跟实际等级一样,修改等级发生过变化，我们只设置实际等级
//                            if (itemDataTypeGrade.getAlterState() == 0) {
//                                itemDataTypeGrade.setSkuGrade(sabcRule.getSkuTypeName());
//                                itemDataTypeGrade.setSkuAdjustGrade(sabcRule.getSkuTypeName());
//                                //根据商品id去库存分析表中查找，如果没有找到，证明是刚才新增加的商品
//                                InventoryAnalysis inventoryAnalysis = inventoryAnalysisRepository.getByItem(entity.getWarehouseId(),
//                                        entity.getClientId(), itemDataTypeGrade.getItemData());
//                                if (inventoryAnalysis == null) {
//                                    inventoryAnalysis=new InventoryAnalysis();
//                                    inventoryAnalysis.setWarehouseId(ItemData.getWarehouseId());
//                                    inventoryAnalysis.setClientId(ItemData.getClientId());
//                                    inventoryAnalysis.setItemData(ItemData);
//                                    inventoryAnalysis.setAvailableAmount(0);
//                                    inventoryAnalysis.setOnpodAmount(0);
//                                }
//                                inventoryAnalysis.setLevel(sabcRule.getSkuTypeName());
//                                inventoryAnalysis.setMaxDoc(sabcRule.getMaxDoc().multiply(ud));
//                                inventoryAnalysis.setReplenishDoc(sabcRule.getReplenDoc().multiply(ud));
//                                inventoryAnalysis.setSafetyDoc(sabcRule.getSafelyDoc().multiply(ud));
//                                inventoryAnalysisRepository.save(inventoryAnalysis);
//                            } else {
//                                itemDataTypeGrade.setSkuGrade(sabcRule.getSkuTypeName());
//                            }
//                            itemDataTypeGradeStatsRepository.save(itemDataTypeGrade);
//                        }
//                    }
//                }
//            }
//        } else {
//            throw new ApiException("请配置本仓库本客户的补货策略");
//        }
        return itemDataMapper.toDTO(ItemData);
    }

    /**
     * 给商品库存分析表和商品等级计算表添加默认数据
     */
    private void addItemDataTypeGradeStatsAnd(ItemData ItemData, ReplenishStrategy replenishStrategy, SabcRule sabcRuleSm) {
        ItemDataTypeGradeStats itemDataTypeGradeStats = new ItemDataTypeGradeStats();
        InventoryAnalysis inventoryAnalysis = new InventoryAnalysis();
        itemDataTypeGradeStats.setClientId(ItemData.getClientId());
        itemDataTypeGradeStats.setWarehouseId(ItemData.getWarehouseId());
        itemDataTypeGradeStats.setAdjustExpireDate(ItemData.getCreatedDate());
        itemDataTypeGradeStats.setUnitsShipment(replenishStrategy.getUnitsShipment());
        itemDataTypeGradeStats.setShipmentDay(replenishStrategy.getShipmentDay());
        BigDecimal ud = replenishStrategy.getUnitsShipment().multiply(replenishStrategy.getShipmentDay());
        itemDataTypeGradeStats.setUnitsDay(ud);
        itemDataTypeGradeStats.setSkuAdjustGrade(sabcRuleSm.getSkuTypeName());//设置修改等级
        itemDataTypeGradeStats.setSkuGrade(sabcRuleSm.getSkuTypeName());
        itemDataTypeGradeStats.setItemData(ItemData);
        itemDataTypeGradeStats.setAlterState(0);

        inventoryAnalysis.setWarehouseId(ItemData.getWarehouseId());
        inventoryAnalysis.setClientId(ItemData.getClientId());
        inventoryAnalysis.setItemData(ItemData);
        inventoryAnalysis.setAvailableAmount(0);
        inventoryAnalysis.setOnpodAmount(0);
        inventoryAnalysis.setLevel(sabcRuleSm.getSkuTypeName());
        inventoryAnalysis.setMaxDoc(sabcRuleSm.getMaxDoc().multiply(ud));
        inventoryAnalysis.setReplenishDoc(sabcRuleSm.getReplenDoc().multiply(ud));
        inventoryAnalysis.setSafetyDoc(sabcRuleSm.getSafelyDoc().multiply(ud));
        itemDataTypeGradeStatsRepository.save(itemDataTypeGradeStats);
        inventoryAnalysisRepository.save(inventoryAnalysis);
    }

    @Override
    public void delete(String id) {
        ItemData entity = itemDataRepository.retrieve(id);
        InventoryAnalysis inventoryAnalysis = inventoryAnalysisRepository.
                getByItem(entity.getWarehouseId(), entity.getClientId(), entity);
        ItemDataTypeGradeStats itemDataTypeGradeStats = itemDataTypeGradeStatsRepository.
                getByItemDataAnd(entity.getWarehouseId(), entity.getClientId(), entity);
        itemDataTypeGradeStatsRepository.delete(itemDataTypeGradeStats);
        inventoryAnalysisRepository.delete(inventoryAnalysis);
        itemDataRepository.delete(entity);
    }

    @Override
    public ItemDataDTO update(ItemDataDTO dto) {
        ItemData entity = itemDataRepository.retrieve(dto.getId());
        //更新因为客户，仓库，商品唯一编码不更新，所以不需要验证
        itemDataMapper.updateEntityFromDTO(dto, entity);
        return itemDataMapper.toDTO(itemDataRepository.save(entity));
    }

    @Override
    public ItemDataDTO retrieve(String id) {
        return itemDataMapper.toDTO(itemDataRepository.retrieve(id));
    }

    @Override
    public List<ItemDataDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ItemData> entities = itemDataRepository.getBySearchTerm(searchTerm, sort);
        return itemDataMapper.toDTOList(entities);
    }

    @Override
    public Page<ItemDataDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ItemData> entities = itemDataRepository.getBySearchTerm(searchTerm, pageable);
        return itemDataMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ItemDataDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "itemNo"));
        applicationContext.isCurrentClient(clientId);
        List<ItemData> entities = itemDataRepository.getList(clientId, sort);
        return itemDataMapper.toDTOList(entities);
    }

    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelItemData readExcel = new ReadExcelItemData(clientRepository, itemDataGlobalRepository);
        //解析excel，获取上传的事件单
        List<ItemDataDTO> itemDataDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(itemDataDTOList);
    }

    private void checkItemNo(String warehouse, String client, String itemNo) {
        ItemData itemData = itemDataRepository.getByItemNo(warehouse, client, itemNo, Constant.NOT_LOCKED);
        if (itemData != null) {
            throw new ApiException(MasterDataException.EX_MD_ITEM_DATA_NO_UNIQUE.toString(), client);
        }
    }

    public void createImport(List<ItemDataDTO> itemDataDTOList) {
        for (ItemDataDTO itemDataDTO : itemDataDTOList) {
            ItemData entity = itemDataMapper.importToEntity(itemDataDTO);
            checkItemNo(entity.getWarehouseId(), entity.getClientId(), entity.getItemNo());
            itemDataRepository.save(entity);
        }
    }

    //定时任务添加商品热销度
//    @Scheduled(fixedDelay = 600000)
    public void calculateItemGrade(){
        //查询状态为0的商品
        List<ItemData> itemDatas=getByState();
        for(ItemData itemData:itemDatas){
            //在补货策略表中查出s/d   u/s  因为只有一条数据，所以根据client和wearhouse查询
            ReplenishStrategy replenishStrategy = replenishStrategyRepository.getByClient(itemData.getWarehouseId(), itemData.getClientId());
            if (replenishStrategy == null) {
                throw new ApiException("请配置本仓库本客户的补货策略");
            }
            //计算热消度等级，刷新所有商品的的热消度等级
            List<ItemDataTypeGradeStats> ItemDataTypeGradeStatsList = itemDataTypeGradeStatsRepository.
                    getByWarehouseIdAndClient(itemData.getWarehouseId(), itemData.getClientId());
            List<SabcRule> SabcRuleList = sabcRuleRepository.getMaxRule(itemData.getWarehouseId());
            List<SabcRule> sabcRuleSmList = sabcRuleRepository.getSmRule(itemData.getWarehouseId());
            if(ItemDataTypeGradeStatsList.size()==0) {
                //设置商品等级计算页面数据和商品库存分析页面数据
                addItemDataTypeGradeStatsAnd(itemData, replenishStrategy, sabcRuleSmList.get(0));
            }else {
                ItemDataTypeGradeStats itemDataTypeGradeStats = new ItemDataTypeGradeStats();
                itemDataTypeGradeStats.setClientId(itemData.getClientId());
                itemDataTypeGradeStats.setWarehouseId(itemData.getWarehouseId());
                itemDataTypeGradeStats.setAdjustExpireDate(itemData.getCreatedDate());
                itemDataTypeGradeStats.setUnitsShipment(replenishStrategy.getUnitsShipment());
                itemDataTypeGradeStats.setShipmentDay(replenishStrategy.getShipmentDay());
                BigDecimal ud = replenishStrategy.getUnitsShipment().multiply(replenishStrategy.getShipmentDay());
                itemDataTypeGradeStats.setUnitsDay(ud);
                itemDataTypeGradeStats.setItemData(itemData);
                itemDataTypeGradeStats.setAlterState(0);
                //添加该数据然后排序
                ItemDataTypeGradeStatsList.add(itemDataTypeGradeStats);
                Collections.sort(ItemDataTypeGradeStatsList, new Comparator<ItemDataTypeGradeStats>() {
                    @Override
                    public int compare(ItemDataTypeGradeStats o1, ItemDataTypeGradeStats o2) {
                        if (o1.getShipmentDay().intValue() < o2.getShipmentDay().intValue()) {
                            return 1;
                        } else if (o1.getShipmentDay().intValue() > o2.getShipmentDay().intValue()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                int a = 0;
                for (ItemDataTypeGradeStats itemDataTypeGrade : ItemDataTypeGradeStatsList) {
                    a++;
                    for (SabcRule sabcRule : SabcRuleList) {
                        BigDecimal b = new BigDecimal(a).divide(new BigDecimal(ItemDataTypeGradeStatsList.size()), 4, RoundingMode.CEILING);
                        if ((b.compareTo(sabcRule.getFromNo())) == 1 && (b.compareTo(sabcRule.getToNo())) == -1 || (b.compareTo(sabcRule.getToNo())) == 0) {
                            itemDataTypeGrade.setSkuGrade(sabcRule.getSkuTypeName());
                            //修改等级没有修改过，跟实际等级一样,修改等级发生过变化，我们只设置实际等级
                            if (itemDataTypeGrade.getAlterState() == 0) {
                                itemDataTypeGrade.setSkuGrade(sabcRule.getSkuTypeName());
                                itemDataTypeGrade.setSkuAdjustGrade(sabcRule.getSkuTypeName());
                                //根据商品id去库存分析表中查找，如果没有找到，证明是刚才新增加的商品
                                InventoryAnalysis inventoryAnalysis = inventoryAnalysisRepository.getByItem(itemData.getWarehouseId(),
                                        itemData.getClientId(), itemDataTypeGrade.getItemData());
                                if (inventoryAnalysis == null) {
                                    inventoryAnalysis = new InventoryAnalysis();
                                    inventoryAnalysis.setWarehouseId(itemData.getWarehouseId());
                                    inventoryAnalysis.setClientId(itemData.getClientId());
                                    inventoryAnalysis.setItemData(itemData);
                                    inventoryAnalysis.setAvailableAmount(0);
                                    inventoryAnalysis.setOnpodAmount(0);
                                }
                                inventoryAnalysis.setLevel(sabcRule.getSkuTypeName());
                                inventoryAnalysis.setMaxDoc(sabcRule.getMaxDoc().multiply(ud));
                                inventoryAnalysis.setReplenishDoc(sabcRule.getReplenDoc().multiply(ud));
                                inventoryAnalysis.setSafetyDoc(sabcRule.getSafelyDoc().multiply(ud));
                                inventoryAnalysis.setBufferFudAmount(BigDecimal.ZERO);
                                inventoryAnalysisRepository.save(inventoryAnalysis);
                            } else {
                                itemDataTypeGrade.setSkuGrade(sabcRule.getSkuTypeName());
                            }
                            itemDataTypeGradeStatsRepository.save(itemDataTypeGrade);
                        }
                    }
                }
            }
            itemData.setState(100);
            itemDataRepository.save(itemData);
        }
    }

    private List<ItemData> getByState(){
        String hql="select i from ItemData i where i.state=0";
        TypedQuery<ItemData> query=entityManager.createQuery(hql,ItemData.class);
        query.setFirstResult(0);
        query.setMaxResults(5000);
        List<ItemData> itemDatas=query.getResultList();
        return itemDatas;
    }
}
