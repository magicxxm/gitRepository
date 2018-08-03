package com.mushiny.wms.masterdata.mdbasics.util;

import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.general.repository.WarehouseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ItemDataTypeGradeStats;
import com.mushiny.wms.masterdata.ibbasics.domain.ReplenishStrategy;
import com.mushiny.wms.masterdata.ibbasics.repository.ItemDataTypeGradeStatsRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.ReplenishStrategyRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.TimeConfigRepository;
import com.mushiny.wms.masterdata.mdbasics.business.ItemDateInCostomerPosition;
import com.mushiny.wms.masterdata.mdbasics.domain.CustomerShipmentPosition;
import com.mushiny.wms.masterdata.mdbasics.domain.InventoryAnalysis;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;
import com.mushiny.wms.masterdata.mdbasics.repository.CustomerShipmentPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.InventoryAnalysisRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.SabcRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class CountGradeServiceImpl implements CountGradeService {

    private final SabcRuleRepository sabcRuleRepository;
    private final ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final InventoryAnalysisRepository inventoryAnalysisRepository;
    private final ReplenishStrategyRepository replenishStrategyRepository;
    private final TimeConfigRepository timeConfigRepository;

    @Autowired
    public CountGradeServiceImpl(SabcRuleRepository sabcRuleRepository,
                                 ItemDataTypeGradeStatsRepository itemDataTypeGradeStatsRepository,
                                 CustomerShipmentPositionRepository customerShipmentPositionRepository,
                                 WarehouseRepository warehouseRepository,
                                 ClientRepository clientRepository, InventoryAnalysisRepository inventoryAnalysisRepository, ReplenishStrategyRepository replenishStrategyRepository, TimeConfigRepository timeConfigRepository) {
        this.sabcRuleRepository = sabcRuleRepository;
        this.itemDataTypeGradeStatsRepository = itemDataTypeGradeStatsRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.inventoryAnalysisRepository = inventoryAnalysisRepository;
        this.replenishStrategyRepository = replenishStrategyRepository;
        this.timeConfigRepository = timeConfigRepository;
    }

    public void getRule() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTimeTow = localDateTime.minus(2, ChronoUnit.DAYS);
        //获取所有wareHouse
        List<Warehouse> WarehouseList = warehouseRepository.getWarehouses(0);
        //获取所有的client
        List<Client> clientList = clientRepository.findIdByEntityLock(0);
        List<CustomerShipmentPosition> positionListAll = customerShipmentPositionRepository.getByTime(localDateTime, localDateTimeTow);
        //首先要有订单明细  有设置的级别
        if (positionListAll.size() != 0) {
            //该仓库下该客户的数据
            for (Warehouse Warehouse : WarehouseList) {
//                timeConfigRepository.getByWarehouseId();
                //查询所有级别
                List<SabcRule> list = sabcRuleRepository.getMaxRule(Warehouse.getId());
                List<SabcRule> smlist = sabcRuleRepository.getSmRule(Warehouse.getId());
                if (list.size() != 0) {
                    for (Client client : clientList) {
                        //在补货策略表中查出s/d   u/s  因为只有一条数据，所以根据client和wearhouse查询
                        ReplenishStrategy replenishStrategy = replenishStrategyRepository.getByClient(Warehouse.getId(), client.getId());
                        //该仓库该客户的所有明细
                        List<CustomerShipmentPosition> positionListAllwareHouseAndClient = new ArrayList<>();
                        //  获取所有商品每一个的订单数量的集合
                        List<ItemDateInCostomerPosition> itemDateInCostomerPositionList = new ArrayList<>();
                        //该客户该仓库的所有商品等级表
                        List<ItemDataTypeGradeStats> ItemDataTypeGradeStatsList = itemDataTypeGradeStatsRepository.
                                getByWarehouseIdAndClient(Warehouse.getId(), client.getId());
                        for (CustomerShipmentPosition CustomerShipmentPosition1 : positionListAll) {
                            if (CustomerShipmentPosition1.getWarehouseId().equals(Warehouse.getId()) && CustomerShipmentPosition1.getClientId().equals(client.getId())) {
                                positionListAllwareHouseAndClient.add(CustomerShipmentPosition1);
                            }
                        }
                        //该客户该仓库必须有订单明细 必须有商品
                        if (positionListAllwareHouseAndClient.size() != 0 && ItemDataTypeGradeStatsList.size() != 0) {
                            for (ItemDataTypeGradeStats itemData : ItemDataTypeGradeStatsList) {
                                ItemDateInCostomerPosition itemDateInCostomerPosition = new ItemDateInCostomerPosition();
                                itemDateInCostomerPosition.setItemId(itemData.getItemData().getId());
                                int itemNumber = 0;//商品的累计订单
                                int itemno = 0;//商品的累计数量
                                for (CustomerShipmentPosition customerShipmentPosition : positionListAllwareHouseAndClient) {
                                    String id = itemData.getItemData().getId();
                                    String id1 = customerShipmentPosition.getItemDataId();
                                    if (id.equals(id1)) {
                                        itemno += customerShipmentPosition.getAmount().intValue();
                                        itemNumber++;
                                    }
                                }
                                if (itemNumber != 0) {
                                    itemDateInCostomerPosition.setItemNumber(new BigDecimal(itemNumber));
                                    itemDateInCostomerPosition.setItemN(itemno);
                                } else {
                                    itemDateInCostomerPosition.setItemNumber(itemData.getShipmentDay().multiply(new BigDecimal(2)));
                                    itemDateInCostomerPosition.setItemN(0);
                                }
                                itemDateInCostomerPositionList.add(itemDateInCostomerPosition);
                            }
                            //给数据排序
                            Collections.sort(itemDateInCostomerPositionList, new Comparator<ItemDateInCostomerPosition>() {
                                @Override
                                public int compare(ItemDateInCostomerPosition o1, ItemDateInCostomerPosition o2) {
                                    if (o1.getItemNumber().compareTo(o2.getItemNumber()) == -1) {
                                        return 1;
                                    } else if (o1.getItemNumber().compareTo(o2.getItemNumber()) == 1) {
                                        return -1;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                            //计算商品的总订单数 30天；
                            int c = 0;
                            for (ItemDateInCostomerPosition itemDateInCostomerPosition : itemDateInCostomerPositionList) {
                                c += itemDateInCostomerPosition.getItemNumber().intValue();
                            }
                            //获取该仓库该客户所有商品，遍历所有商品，在再订单中找到对应的商品
                            //a 为累计的发货数量
                            int a = 0;
                            int b = 0;
                            for (ItemDateInCostomerPosition ItemDateInCostomerPosition : itemDateInCostomerPositionList) {
                                b++;
                                a += ItemDateInCostomerPosition.getItemNumber().intValue();//每个商品的订单
                                //发货量累计占比
                                BigDecimal number = new BigDecimal(a).divide(new BigDecimal(c), 4, RoundingMode.CEILING);//发货量累计占比
                                //给商品等级页面数据设置值
                                for (ItemDataTypeGradeStats ItemDataTypeGrade : ItemDataTypeGradeStatsList) {
                                    String sabcSize = "";
                                    //将叠加后的数据跟商品等级页面，如果是同一个商品，设置它的商品等级
                                    if (ItemDateInCostomerPosition.getItemId().equals(ItemDataTypeGrade.getItemData().getId())) {
                                        InventoryAnalysis inventoryAnalysis = inventoryAnalysisRepository.getByItem(Warehouse.getId(), client.getId(), ItemDataTypeGrade.getItemData());
                                        //有订单明细，不是计算出来默认的
                                        if (ItemDateInCostomerPosition.getItemN() != 0) {
                                            //某个商品的订单数
                                            BigDecimal shipmentDay = ItemDateInCostomerPosition.getItemNumber().divide(new BigDecimal(2), 0, RoundingMode.CEILING);
                                            //这个商品必须有订单
                                            ItemDataTypeGrade.setShipmentDay(shipmentDay);
                                            //30天的商品数除以30天，每天的商品数
                                            BigDecimal unitshipent = (new BigDecimal(ItemDateInCostomerPosition.getItemN())).divide(new BigDecimal(2), 0, RoundingMode.CEILING);
                                            BigDecimal unitsShipment = (unitshipent).divide(shipmentDay, 0, RoundingMode.CEILING);
                                            ItemDataTypeGrade.setUnitsShipment(unitsShipment);
                                            ItemDataTypeGrade.setUnitsDay(unitshipent);
                                            //遍历级别，设置级别
                                            for (SabcRule SabcRule : list) {
                                                BigDecimal size = new BigDecimal(b).divide(new BigDecimal(ItemDataTypeGradeStatsList.size()), 4, RoundingMode.CEILING);
                                                if ((size.compareTo(SabcRule.getFromNo())) == 1 && (size.compareTo(SabcRule.getToNo())) == -1 || (size.compareTo(SabcRule.getToNo())) == 0) {
                                                    //刷新库存分析
                                                    inventoryAnalysis.setMaxDoc(unitshipent.multiply(SabcRule.getMaxDoc()));
                                                    inventoryAnalysis.setSafetyDoc(unitshipent.multiply(SabcRule.getSafelyDoc()));
                                                    inventoryAnalysis.setReplenishDoc(unitshipent.multiply(SabcRule.getReplenDoc()));
                                                }
                                            }
                                        }
                                        for (SabcRule SabcRule : list) {
                                            BigDecimal size = new BigDecimal(b).divide(new BigDecimal(ItemDataTypeGradeStatsList.size()), 4, RoundingMode.CEILING);
                                            if ((size.compareTo(SabcRule.getFromNo())) == 1 && (size.compareTo(SabcRule.getToNo())) == -1 || (size.compareTo(SabcRule.getToNo())) == 0) {
                                                BigDecimal ud = new BigDecimal(ItemDateInCostomerPosition.getItemN()).divide(new BigDecimal(2), 0, RoundingMode.CEILING);
                                                //刷新库存分析
                                                ItemDataTypeGrade.setSkuGrade(SabcRule.getSkuTypeName());
                                                if (ItemDataTypeGrade.getAlterState() == 0) {
                                                    inventoryAnalysis.setLevel(SabcRule.getSkuTypeName());
                                                    ItemDataTypeGrade.setSkuAdjustGrade(SabcRule.getSkuTypeName());
                                                }
                                                if (sabcSize != null && !sabcSize.equals(SabcRule.getSkuTypeName())) {
                                                    SabcRule.setSalesPro(number);
                                                    sabcRuleRepository.save(SabcRule);
                                                } else {
                                                    sabcSize = SabcRule.getSkuTypeName();
                                                }
                                            }
                                        }
                                        inventoryAnalysisRepository.save(inventoryAnalysis);
                                        itemDataTypeGradeStatsRepository.save(ItemDataTypeGrade);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}