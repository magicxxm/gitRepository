package com.mushiny.wms.report.service.impl;

import com.mushiny.wms.report.domain.*;
import com.mushiny.wms.report.query.business.PickBusiness;
import com.mushiny.wms.report.query.business.PpWorkBusiness;
import com.mushiny.wms.report.query.business.WorkPpTotalBusiness;
import com.mushiny.wms.report.query.dto.*;
import com.mushiny.wms.report.query.dto.capacityTotal.CapacityDTO;
import com.mushiny.wms.report.query.dto.capacityTotal.CapacityBinType;
import com.mushiny.wms.report.query.dto.capacityTotal.Capacity;
import com.mushiny.wms.report.query.dto.picked.*;
import com.mushiny.wms.report.query.dto.pp_work.PpWorkDTO;
import com.mushiny.wms.report.query.hql.*;
import com.mushiny.wms.report.repository.CustomerShipmentPositionRepository;
import com.mushiny.wms.report.repository.CustomerShipmentRepository;
import com.mushiny.wms.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final LegacyDataAndFudQuery legacyDataAndFudQuery;
    private final ReportQuery reportQuery;
    private final WorkFlowTotalQuery workFlowTotalQuery;
    private final WorkFlowDetailQuery workFlowDetailQuery;
    private final WorkPpTotalBusiness workPpTotalBusiness;
    private final PpWorkBusiness ppWorkBusiness;
    private final CapacityPodQuery capacityPodQuery;
    private final CapacitySideQuery capacitySideQuery;
    private final CapacityBinQuery capacityBinQuery;
    private final CapacityTotalQuery capacityTotalQuery;
    private final PickedQuery pickedQuery;
    private final PickBusiness pickBusiness;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;

    @Autowired
    public ReportServiceImpl(LegacyDataAndFudQuery legacyDataAndFudQuery,
                             ReportQuery reportQuery,
                             WorkFlowTotalQuery workFlowTotalQuery,
                             WorkFlowDetailQuery workFlowDetailQuery,
                             WorkPpTotalBusiness workPpTotalBusiness,
                             PpWorkBusiness ppWorkBusiness,
                             CapacityPodQuery capacityPodQuery,
                             CapacitySideQuery capacitySideQuery,
                             CapacityBinQuery capacityBinQuery,
                             CapacityTotalQuery capacityTotalQuery,
                             PickedQuery pickedQuery,
                             PickBusiness pickBusiness,
                             CustomerShipmentRepository customerShipmentRepository,
                             CustomerShipmentPositionRepository customerShipmentPositionRepository) {
        this.legacyDataAndFudQuery = legacyDataAndFudQuery;
        this.reportQuery = reportQuery;
        this.workFlowTotalQuery = workFlowTotalQuery;
        this.workFlowDetailQuery = workFlowDetailQuery;
        this.workPpTotalBusiness = workPpTotalBusiness;
        this.ppWorkBusiness = ppWorkBusiness;
        this.capacityPodQuery = capacityPodQuery;
        this.capacitySideQuery = capacitySideQuery;
        this.capacityBinQuery = capacityBinQuery;
        this.capacityTotalQuery = capacityTotalQuery;
        this.pickedQuery = pickedQuery;
        this.pickBusiness = pickBusiness;
        this.customerShipmentRepository = customerShipmentRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
    }

    //获取FUD数据
    @Override
    public List<FudDTO> getFud() {
//        return legacyDataAndFudQuery.getFuds();
//        return getFuds();
//        return getNewFuds();
        return getFinal();
    }

    public List<FudDTO> getFinal() {
        List<FudDTO> fudDTOS = new ArrayList<>();  // 给页面返的数据
        // FUD 订单明细
        Map<CustomerShipmentPosition, BigDecimal> orderTest = getNewFud(); // (itemdataID , overTime, amount)
        if (orderTest != null) {

            // 判断不同订单中是否有相同商品
            Map<String, Boolean> flagSet = new ConcurrentHashMap<>();
            for (CustomerShipmentPosition entityKey : orderTest.keySet()) {
                String itemDateIdRepeat = entityKey.getItemData().getId();
                int size = flagSet.size();
                flagSet.put(itemDateIdRepeat, true);
                if (flagSet.size() <= size) {
                    flagSet.put(itemDateIdRepeat, false); // 重复了
                }
            }

            for (String flagKey : flagSet.keySet()) {
                //获取商品bin位库存
                BigDecimal binAmount = legacyDataAndFudQuery.getFudStoreBin(flagKey);
                //获取商品货筐库存
                List<FudDTO> stores = legacyDataAndFudQuery.containerStockerUnit(flagKey);

                for (CustomerShipmentPosition key : orderTest.keySet()) {
                    String itemDateId = key.getItemData().getId();

                    if (flagKey.equalsIgnoreCase(itemDateId)) {
                        String overTime = legacyDataAndFudQuery.getOverTime(key.getCustomerShipment().getId());
                        BigDecimal amountValue = orderTest.get(key);
                        System.out.println(itemDateId + "---" + overTime + "---" + amountValue);

                        //订单数量
                        BigDecimal amount = amountValue;

                        BigDecimal containerAmount = BigDecimal.ZERO;

                        if (binAmount.compareTo(BigDecimal.ZERO) >= 0) {
                            binAmount = binAmount.subtract(amount);
                            // 在货框中未及时上架的总数量 = 订单数量-bin数量
                            containerAmount = BigDecimal.ZERO.subtract(binAmount);
                        } else {
                            // 订单数量
                            containerAmount = amount;
                        }
                        // bin 数量 大于 订单中商品数量
                        if (binAmount.compareTo(BigDecimal.ZERO) >= 0) {
                            // break;
                        } else {
                            //订单数量 - 库存数量 = 初始值 0
                            BigDecimal difference = BigDecimal.ZERO;

                            if (stores != null) {
                                //遍历库存
//                                for (FudDTO fd : stores) {
                                for (int i = 0; i < stores.size(); i++) {
                                    FudDTO fd = stores.get(i);
                                    BigDecimal amountValues = fd.getAmount();
                                    // 如果容器相同则需取容器的最后一次操作
//                                  List<Object[]> stockUnitRecord = legacyDataAndFudQuery.getRecordMaxTime(fd.getContainerName(), itemDateId);
//                                  if (stockUnitRecord.get(0)[1] != null && "" != stockUnitRecord.get(0)[1]) {
//                                    String time = String.valueOf(stockUnitRecord.get(0)[1]);
//                                    List<String> recordType = legacyDataAndFudQuery.getRecordType(itemDateId, time);
//                                    fd.setActivityCode(recordType.get(0));
//                                   }
                                    //订单数量 - 库存数量
                                    difference = containerAmount.subtract(fd.getAmount());
                                    // 如果订单数量大于库存数量，返回库存数量
                                    if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) >= 0) {
                                        fd.setOverTime(overTime);
                                        fd.setAmount(fd.getAmount());
                                        fudDTOS.add(fd);
                                        containerAmount = containerAmount.subtract(fd.getAmount());

                                        stores.remove(fd);
                                        i--;

                                        // 如果订单数量小于库存数量，返回订单数量
                                    } else if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) < 0) {
                                        FudDTO fudDTO = new FudDTO();
                                        fd.setOverTime(overTime);
                                        fd.setAmount(containerAmount);
                                        fudDTOS.add(fd);
                                        containerAmount = containerAmount.subtract(fd.getAmount());

                                        fudDTO.setSkuNo(fd.getSkuNo());
                                        fudDTO.setSkuId(fd.getSkuId());
                                        fudDTO.setSkuName(fd.getSkuName());
                                        fudDTO.setClientName(fd.getClientName());
                                        fudDTO.setContainerName(fd.getContainerName());
                                        fudDTO.setAmount(amountValues.subtract(fd.getAmount()));
                                        fudDTO.setState(fd.getState());
                                        fudDTO.setModifiedDate(fd.getModifiedDate());
                                        fudDTO.setActivityCode(fd.getActivityCode());
                                        fudDTO.setTotalTime(fd.getTotalTime());

                                        stores.remove(fd);
                                        stores.add(fudDTO);
                                        //  break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
       /*如果 影响客户订单时间 大于 累计时间， 影响订单时间 =  累计时间 */
        List<FudDTO> fudDTOList = new ArrayList<>();
        for (FudDTO fudDTO : fudDTOS) {
            String overTime = legacyDataAndFudQuery.totalByOverTime(fudDTO.getTotalTime(), fudDTO.getOverTime());
            fudDTO.setOverTime(overTime);
            fudDTOList.add(fudDTO);
        }
        // 数据相同的合并
//        fudDTOList = groupingBy(fudDTOList);
        // 按影响订单时间排序
        Collections.sort(fudDTOList, new sortOverTimes());
        return fudDTOList;
    }


    //明细 数据相同的合并
    public List<FudDTO> groupingBy(List<FudDTO> fudDTOList) {
        if (fudDTOList.isEmpty()) {
            return new ArrayList<>();
        }
        List<FudDTO> dtos = new ArrayList<>();
        fudDTOList.stream().collect(
                Collectors.groupingBy(w -> w.getSkuId(),
                        Collectors.groupingBy(w -> w.getTotalTime(),
                                Collectors.groupingBy(w -> w.getOverTime(),
                                        Collectors.groupingBy(w -> w.getContainerName(),
                                                Collectors.groupingBy(w -> checkNull(w.getState()),
                                                        Collectors.groupingBy(w -> checkNull(w.getActivityCode())))))))).forEach((k, v) -> {
            v.forEach((k1, v1) -> {
                v1.forEach((k2, v2) -> {
                    v2.forEach((k3, v3) -> {
                        v3.forEach((k4, v4) -> {
                            v4.forEach((k5, v5) -> {
                                BigDecimal amount = BigDecimal.ZERO;
                                FudDTO dto = new FudDTO();
                                for (FudDTO d : v5) {
                                    amount = amount.add(d.getAmount());

                                    dto.setSkuNo(d.getSkuNo());
                                    dto.setSkuId(d.getSkuId());
                                    dto.setSkuName(d.getSkuName());
                                    dto.setClientName(d.getClientName());
                                    dto.setContainerName(d.getContainerName());
                                    dto.setState(d.getState());
                                    dto.setModifiedDate(d.getModifiedDate());
                                    dto.setActivityCode(d.getActivityCode());
                                    dto.setTotalTime(d.getTotalTime());
                                    dto.setOverTime(d.getOverTime());

                                }
                                dto.setAmount(amount);
                                dtos.add(dto);
                            });
                        });
                    });
                });
            });
        });
        return dtos;
    }

    public String checkNull(String obj) {
        if (obj == null) {
            return "";
        }
        return obj;
    }

    // 按影响订单时间排序
    private class sortOverTimes implements Comparator {
        public int compare(Object o1, Object o2) {
            FudDTO d1 = (FudDTO) o1;
            FudDTO d2 = (FudDTO) o2;
            return d2.getOverTime().compareTo(d1.getOverTime());
        }
    }


    // 按影响订单时间排序
    private TreeSet sortOverTime(List<FudDTO> fudDTOList) {

        TreeSet ts = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                FudDTO d1 = (FudDTO) o1;
                FudDTO d2 = (FudDTO) o2;
                return d2.getOverTime().compareTo(d1.getOverTime());
            }
        });
        ts.addAll(fudDTOList);
        return ts;
    }


    /* 获取  数据明细    2018-01-08  new */
    public List<FudDTO> getFuds() {
        List<FudDTO> fudDTOS = new ArrayList<>();
        // FUD 订单明细
        Map<CustomerShipmentPosition, BigDecimal> orderTest = getNewFud(); // (itemdataID , overTime, amount)
        if (orderTest != null) {
            Map<String, Boolean> flagSet = new ConcurrentHashMap<>();
            for (CustomerShipmentPosition key : orderTest.keySet()) {
                String itemDateId = key.getItemData().getId();
                int size = flagSet.size();
                flagSet.put(itemDateId, true);
                if (flagSet.size() <= size) {
                    flagSet.put(itemDateId, false); // 重复了
                }
            }
            for (String flagKey : flagSet.keySet()) {
                if (flagSet.get(flagKey)) {
                    // 没重复的
                    fudDTOS = getFudDate(orderTest);
                } else {
                    //  为 false的
                    //  重复了把重复的商品所对应的进单amount相加
                    BigDecimal amountSum = BigDecimal.ZERO;
                    String overTime = "";
                    for (CustomerShipmentPosition key : orderTest.keySet()) {
                        String itemDateId = key.getItemData().getId();
                        if (flagKey.equalsIgnoreCase(itemDateId)) {
                            overTime = legacyDataAndFudQuery.getOverTime(key.getCustomerShipment().getId());
                            BigDecimal amountValue = orderTest.get(key);
                            // 数量累加
                            amountSum = amountSum.add(amountValue);
                            System.out.println(itemDateId + "-1111111111-2222222222-" + overTime + "---" + amountValue + "---" + amountSum);

                        }
                    }
                    BigDecimal binAmount = legacyDataAndFudQuery.getFudStoreBin(flagKey);
                    fudDTOS = finalishAmount(amountSum, binAmount, flagKey, overTime);
                }
            }
        }

        /*如果 影响客户订单时间 大于 累计时间， 影响订单时间 =  累计时间 */
        List<FudDTO> fudDTOList = new ArrayList<>();
        for (FudDTO fudDTO : fudDTOS) {
            String overTime = legacyDataAndFudQuery.totalByOverTime(fudDTO.getTotalTime(), fudDTO.getOverTime());
            fudDTO.setOverTime(overTime);
            fudDTOList.add(fudDTO);
        }
        return fudDTOList;
    }

    public List<FudDTO> getFudDate(Map<CustomerShipmentPosition, BigDecimal> orderTest) {
        List<FudDTO> fudDTOS = new ArrayList<>();
        for (CustomerShipmentPosition key : orderTest.keySet()) {

            String itemDateId = key.getItemData().getId();

            String overTime = legacyDataAndFudQuery.getOverTime(key.getCustomerShipment().getId());
            BigDecimal amountValue = orderTest.get(key);
            System.out.println(itemDateId + "---" + overTime + "---" + amountValue);
            //获取商品bin位库存
            BigDecimal binAmount = legacyDataAndFudQuery.getFudStoreBin(itemDateId);

            //订单数量
            BigDecimal amount = amountValue;

            if (binAmount.compareTo(amount) >= 0) {
//                    break;
            } else {
                // 在货框中未及时上架的总数量 = 订单数量-bin数量
                BigDecimal containerAmount = amount.subtract(binAmount);
                //获取商品货筐库存
                List<FudDTO> stores = legacyDataAndFudQuery.containerStockerUnit(itemDateId);
                //订单数量 - 库存数量 = 初始值 0
                BigDecimal difference = BigDecimal.ZERO;

                if (stores != null) {
                    //遍历库存
                    for (FudDTO fd : stores) {
                        // 如果容器相同则需取容器的最后一次操作
                        List<Object[]> stockUnitRecord = legacyDataAndFudQuery.getRecordMaxTime(fd.getContainerName(), itemDateId);
                        if (stockUnitRecord.get(0)[1] != null && "" != stockUnitRecord.get(0)[1]) {
                            String time = String.valueOf(stockUnitRecord.get(0)[1]);
                            List<String> recordType = legacyDataAndFudQuery.getRecordType(itemDateId, time);
                            fd.setActivityCode(recordType.get(0));
                        }
                        //订单数量 - 库存数量
                        difference = containerAmount.subtract(fd.getAmount());
                        // 如果订单数量大于库存数量，返回库存数量
                        if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) >= 0) {
                            fd.setOverTime(overTime);
                            fd.setAmount(fd.getAmount());
                            fudDTOS.add(fd);
                            containerAmount = containerAmount.subtract(fd.getAmount());
//                            //
//                            amount = amount.subtract(fd.getAmount());
                            // 如果订单数量小于库存数量，返回订单数量
                        } else if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) < 0) {
                            fd.setOverTime(overTime);
                            fd.setAmount(containerAmount);
                            fudDTOS.add(fd);
                            containerAmount = containerAmount.subtract(fd.getAmount());
//                              break;
                        }
                    }
                }
            }
        }
        return fudDTOS;
    }

    private List<FudDTO> getBinContainerRestAmount(BigDecimal amount, BigDecimal binAmount, String itemDateId, String overTime) { // 订单数量，Bin中数量，订单中的商品ID，
        List<FudDTO> fudDTOS = new ArrayList<>();  // 给页面返的数据
        Map<String, BigDecimal> binTest = new ConcurrentHashMap(); // 存每种商品的可用bin数量
        Map<FudDTO, String> fudTest = new ConcurrentHashMap();  // 存每种商品的可用库存

        if (binAmount.compareTo(amount) >= 0) {
            binTest.put(itemDateId, binAmount.subtract(amount));
            //  break;
        } else {
            // 在货框中未及时上架的总数量 = 订单数量-bin数量
            BigDecimal containerAmount = amount.subtract(binAmount);

            //获取商品货筐库存
            List<FudDTO> stores = legacyDataAndFudQuery.containerStockerUnit(itemDateId);
            //订单数量 - 库存数量 = 初始值 0
            BigDecimal difference = BigDecimal.ZERO;

            if (stores != null) {
                //遍历库存
                for (FudDTO fd : stores) {
                    // 如果容器相同则需取容器的最后一次操作
                    List<Object[]> stockUnitRecord = legacyDataAndFudQuery.getRecordMaxTime(fd.getContainerName(), itemDateId);
                    if (stockUnitRecord.get(0)[1] != null && "" != stockUnitRecord.get(0)[1]) {
                        String time = String.valueOf(stockUnitRecord.get(0)[1]);
                        List<String> recordType = legacyDataAndFudQuery.getRecordType(itemDateId, time);
                        fd.setActivityCode(recordType.get(0));
                    }
                    //订单数量 - 库存数量
                    difference = containerAmount.subtract(fd.getAmount());

                    // 如果订单数量大于库存数量，返回库存数量
                    if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) >= 0) {
                        fd.setOverTime(overTime);
                        fd.setAmount(fd.getAmount());
                        fudDTOS.add(fd);
                        containerAmount = containerAmount.subtract(fd.getAmount());
                        // 保存该商品的剩余库存
                        fd.setAmount(BigDecimal.ZERO);
                        fudTest.put(fd, itemDateId);

                        // 如果订单数量小于库存数量，返回订单数量
                    } else if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) < 0) {
                        fd.setOverTime(overTime);
                        fd.setAmount(containerAmount);
                        fudDTOS.add(fd);
                        containerAmount = containerAmount.subtract(fd.getAmount());
                        // 保存该商品的剩余库存
                        fd.setAmount(fd.getAmount().subtract(containerAmount));
                        fudTest.put(fd, itemDateId);
                        //  break;
                    }
                }
            }
        }
        return fudDTOS;
    }




    private List<FudDTO> finalishAmount(BigDecimal amount, BigDecimal binAmount, String itemDateId, String overTime) { // 订单数量，Bin中数量，订单中的商品ID，
        List<FudDTO> fudDTOS = new ArrayList<>();

        if (binAmount.compareTo(amount) >= 0) {
//           break;
        } else {
            // 在货框中未及时上架的总数量 = 订单数量-bin数量
            BigDecimal containerAmount = amount.subtract(binAmount);
            //获取商品货筐库存
            List<FudDTO> stores = legacyDataAndFudQuery.containerStockerUnit(itemDateId);
            //订单数量 - 库存数量 = 初始值 0
            BigDecimal difference = BigDecimal.ZERO;

            if (stores != null) {
//                        FudDTO fudDTO = null;
                //遍历库存
                for (FudDTO fd : stores) {
                    // 如果容器相同则需取容器的最后一次操作
                    List<Object[]> stockUnitRecord = legacyDataAndFudQuery.getRecordMaxTime(fd.getContainerName(), itemDateId);
                    if (stockUnitRecord.get(0)[1] != null && "" != stockUnitRecord.get(0)[1]) {
                        String time = String.valueOf(stockUnitRecord.get(0)[1]);
                        List<String> recordType = legacyDataAndFudQuery.getRecordType(itemDateId, time);
                        fd.setActivityCode(recordType.get(0));
                    }
                    //订单数量 - 库存数量
                    difference = containerAmount.subtract(fd.getAmount());
                    // 如果订单数量大于库存数量，返回库存数量
                    if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) >= 0) {
                        fd.setOverTime(overTime);
                        fd.setAmount(fd.getAmount());
                        fudDTOS.add(fd);
                        containerAmount = containerAmount.subtract(fd.getAmount());
//                            //
//                            amount = amount.subtract(fd.getAmount());
                        // 如果订单数量小于库存数量，返回订单数量
                    } else if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) < 0) {
                        fd.setOverTime(overTime);
                        fd.setAmount(containerAmount);
                        fudDTOS.add(fd);
                        containerAmount = containerAmount.subtract(fd.getAmount());
//                              break;
                    }
                }
            }
        }
        return fudDTOS;
    }

    // 获取订单中尚未生成拣货单的订单行，包含从未生成的以及由于其他原因取消拣货任务的  pending
    public Map<CustomerShipmentPosition, BigDecimal> getNewFud() {
        Map<CustomerShipmentPosition, BigDecimal> orderPositionsRemaining = new ConcurrentHashMap<>();
        List<CustomerShipmentTest> customerShipmentTest = customerShipmentRepository.getCustomerShipment(600);
        for (CustomerShipmentTest customerShipment : customerShipmentTest) {
            List<CustomerShipmentPosition> position = customerShipmentPositionRepository.getPosition(customerShipment.getId());

            for (CustomerShipmentPosition customerOrderPos : position) {
                int posStateOld = customerOrderPos.getState();
                if (posStateOld >= 600) {
                    continue;
                }
                List<PickingOrderPosition> pickPositions = customerShipmentRepository.getByCustomerOrderPosition(customerOrderPos);
                BigDecimal amountPicked = BigDecimal.ZERO;
                for (PickingOrderPosition pickingOrderPos : pickPositions) {
                    if (pickingOrderPos.getState() == 800) {
                        continue;
                    } else if (pickingOrderPos.getState() >= 600) {
                        amountPicked = amountPicked.add(pickingOrderPos.getAmountPicked());
                    } else if (pickingOrderPos.getState() < 600 && pickingOrderPos.getState() >= 300) {
                        amountPicked = amountPicked.add(pickingOrderPos.getAmount());
                    }
                }
                BigDecimal amount = customerOrderPos.getAmount().subtract(amountPicked);
                if (BigDecimal.ZERO.compareTo(amount) >= 0) {
                    continue;
                }
                orderPositionsRemaining.put(customerOrderPos, amount);
            }
        }
        return orderPositionsRemaining;
    }

    //获取遗留数据
    @Override
    public List<FudDTO> getContainerLegacyData() {
        List<FudDTO> legacys = new ArrayList<>();
        List<FudDTO> legacyDates = legacyDataAndFudQuery.getByContainerLegacyData();
        for (FudDTO legacyDate : legacyDates) {
            if (legacyDate.getContainerName().contains("OBPS") || legacyDate.getContainerName().contentEquals("Problem Solved")
                    || legacyDate.getContainerName().contentEquals("Problem Solving") || legacyDate.getContainerName().contains("Problem")) {
                if (legacyDate.getActivityCode().contentEquals("OB PROBLEM SOLVE")
                        && legacyDate.getState().contentEquals("正品")) {
                    legacys.add(legacyDate);
//                    legacyDates.remove(legacyDate);
                }
            }
        }
        for (FudDTO legacy : legacys) {
            legacyDates.remove(legacy);
        }
        return legacyDates;
    }

    //获取 WorkFlowDTO 合计数据
    @Override
    public List<WorkFlowDTO> getWorkFlows(String startTime, String endTime, String goodsType) {
        List<WorkFlowDTO> workFlows = new ArrayList<>();
        List<LocalDateTime> times = new ArrayList<>();
        times = reportQuery.getByStartOrEndTime(startTime, endTime);
//        }

        //获取每个时间点的 workflow 数据
        for (LocalDateTime exsdTime : times) {
            WorkFlowDTO workFlowDTO = new WorkFlowDTO(exsdTime, workFlowTotalQuery.getWorkFlowCutline(exsdTime));
            //过滤大于0的
            if (workFlowDTO.getCutline().getTotal().compareTo(BigDecimal.ZERO) > 0) {
                workFlows.add(workFlowDTO);
            }
        }

        return workFlows;
    }

    //获取 WorkPool-Process Path 合计
    @Override
    public List<WorkPpDTO> getByWorkPps(String startTime, String endTime, String goodsType) {

        List<WorkPpDTO> workPp = new ArrayList<>();
        //获取时间列表
        List<LocalDateTime> times = reportQuery.getByStartOrEndTime(startTime, endTime);
        //获取ppName列表
        List<String> ppNames = reportQuery.getProcessPath();
        //循环遍历所有ppName的时间
        for (String ppName : ppNames) {
            workPp.add(workPpTotalBusiness.getWorkPpNames(ppName, times));
        }
        return workPp;
    }

    //获取Process Path-Work Pool 合计
    @Override
    public PpWorkDTO getPpWork(String startTime, String endTime, String goodsType) {
        List<LocalDateTime> times = reportQuery.getByStartOrEndTime(startTime, endTime);
        return ppWorkBusiness.getPpWorkDTO(times);
    }


    //获取 workflow各属性某个时间点的明细数据
    @Override
    public List<WorkFlowDetailDTO> getWorkFlowDetails(String ppName, String exsdTime, String workflowType) {
        List<WorkFlowDetailDTO> workFlowDetailDTOS = new ArrayList<>();

        //如果时间为空，获取全部
        if (exsdTime == null) {
            workFlowDetailDTOS = workFlowDetailQuery.getWorkflowTotalDeail(ppName, workflowType);
            return workFlowDetailDTOS;
        }
        List<WorkFlowDetailDTO> workflowDetail = workFlowDetailQuery.getWorkflowDetail(ppName, exsdTime, workflowType);
        workFlowDetailDTOS = workflowDetail;

        return workFlowDetailDTOS;
    }


    @Override
    //获取 PickArea
    public List<PickDTO> getPickArea(String ppName, String zoneName, String deliveryDate) {

        List<Picked> dataTotal = new ArrayList<>();
        List<Picked> pickAreas = new ArrayList<>();

        pickAreas = pickedQuery.getPickedTest(ppName, zoneName, deliveryDate).stream().
                filter(picked -> picked.getTotal().compareTo(picked.getPicked()) != 0).collect(Collectors.toList());
        List<String> zones = pickAreas.stream().map(Picked::getZoneName).distinct().collect(Collectors.toList());
        for (String z : zones) {
            Picked pi = new Picked();
            pi.setZoneName(z);
            BigDecimal p = BigDecimal.ZERO;
            BigDecimal np = BigDecimal.ZERO;
            BigDecimal to = BigDecimal.ZERO;
            BigDecimal pt = BigDecimal.ZERO;
            for (Picked picked : pickAreas) {
                if (z.equals(picked.getZoneName())) {
                    p = p.add(picked.getPicked());
                    pt = pt.add(BigDecimal.ZERO);
                    np = np.add(picked.getNotPicked());
                    to = to.add(picked.getTotal());
                }
            }
            pi.setPicked(p);
            pi.setPending(pt);
            pi.setNotPicked(np);
            pi.setTotal(to);
            dataTotal.add(pi);
        }
        if (pickedQuery.getPendingArea(deliveryDate).size() > 0 && pickedQuery.getPendingArea(deliveryDate).get(0).getPending().compareTo(BigDecimal.ZERO) > 0) {
            Picked piPending = new Picked();
            piPending.setZoneName(pickedQuery.getPendingArea(deliveryDate).get(0).getZoneName());
            piPending.setPicked(BigDecimal.ZERO);
            piPending.setPending(pickedQuery.getPendingArea(deliveryDate).get(0).getPending());
            piPending.setNotPicked(BigDecimal.ZERO);
            piPending.setTotal(pickedQuery.getPendingArea(deliveryDate).get(0).getPending());
            dataTotal.add(piPending);
        }
        List<PickDTO> dtos = new ArrayList<>();
        dtos.addAll(pickBusiness.totalDateToPick(dataTotal));
        dtos.addAll(pickBusiness.toPickArea(pickAreas));
        return dtos;
    }

    @Override
    //获取 PickExSD
    public List<PickDTO> getPickExSD(String ppName, String zoneName, String deliveryDate) {

        List<Picked> zoneTotals = new ArrayList<>();
        List<Picked> pickZones = new ArrayList<>();
        pickZones = pickedQuery.getPickedTest(ppName, zoneName, deliveryDate).
                stream().filter(picked -> picked.getTotal().compareTo(picked.getPicked()) != 0).collect(Collectors.toList());
        List<Timestamp> timestamps = pickZones.stream().map(Picked::getDeliveryDate).distinct().collect(Collectors.toList());
        for (Timestamp timestamp : timestamps) {
            Picked pi = new Picked();
            pi.setDeliveryDate(timestamp);
            BigDecimal p = BigDecimal.ZERO;
            BigDecimal np = BigDecimal.ZERO;
            BigDecimal to = BigDecimal.ZERO;
            BigDecimal pt = BigDecimal.ZERO;

            String time = String.valueOf(timestamp);
            List<Picked> pendingTime = pickedQuery.getPendingExsd(time);

            for (Picked picked : pickZones) {
                // 列时间和 所有时间对比 ，相同时间的合并(累加)
                if (timestamp.equals(picked.getDeliveryDate())) {
                    p = p.add(picked.getPicked());
                    np = np.add(picked.getNotPicked());
                    if (pendingTime.size() > 0) {
                        for (Picked pendingtime : pendingTime) {
                            if (timestamp.equals(pendingtime.getDeliveryDate())) {
                                pt = pt.add(pendingtime.getPending());
                                to = to.add(picked.getTotal().add(pt));
                            } else {
                                pt = pt.add(BigDecimal.ZERO);
                            }
                        }
                    } else {
                        pt = pt.add(BigDecimal.ZERO);
                        to = to.add(picked.getTotal().add(pt));
                    }
                }
            }
            pi.setPending(pt);
            pi.setPicked(p);
            pi.setNotPicked(np);
            pi.setTotal(to);
            zoneTotals.add(pi);
        }
        // 插入只有pending的数据列
        List<Picked> onlyPendingTime = pickedQuery.getPendingExsd(null);
        if (onlyPendingTime.size() > 0) {
            Boolean bool = false;
            Timestamp times = null;
            for (Picked pendingtime : onlyPendingTime) {
                if ( pendingtime.getPending().compareTo(BigDecimal.ZERO) != 0) {
                    if (timestamps.size() > 0) {
                        for (Timestamp timestamp : timestamps) {
                            if (!pendingtime.getDeliveryDate().equals(timestamp)) {
                                bool = true;
                                times = pendingtime.getDeliveryDate();
                                continue;   // pending 中的时间和列表中的时间挨个对比
                            } else {
                                bool = false;
                                break;
                            }
                        }
                        //  只有pending
                    } else {
                        bool = true;
                        times = pendingtime.getDeliveryDate();
                    }
                }
                if (bool) {
                    Picked piPending = new Picked();
                    piPending.setDeliveryDate(times);
                    piPending.setPicked(BigDecimal.ZERO);
                    piPending.setNotPicked(BigDecimal.ZERO);
                    piPending.setPending(pendingtime.getPending());
                    piPending.setTotal(pendingtime.getPending());
                    zoneTotals.add(piPending);

                }

            }
        }
        List<PickDTO> dtos = new ArrayList<>();
        dtos.addAll(pickBusiness.totalZoneToPick(zoneTotals));
        dtos.addAll(pickBusiness.toPickExSD(pickZones));
        return dtos;
    }


    // 获取Capacity-total 数据
    @Override
    public List<CapacityDTO> getCapacity() {
        List<Capacity> dtos = capacityTotalQuery.getCapacityTotal();


        //获取所有不重复 zoneName
        List<String> zoneName = dtos.stream().map(capacity ->
                capacity.getZoneName()).
                distinct().collect(Collectors.toList());


        List<CapacityDTO> capacities = new ArrayList<>();


        // 遍历 所有 zoneName ，并赋值
        for (String name : zoneName) {
            CapacityDTO dto = new CapacityDTO();
            dto.setZoneName(name);
            //遍历 sql查询结果
            for (Capacity capacity : dtos) {
                // 汇总相同 zoneName 的 货位类型 数据
                if (name.equals(capacity.getZoneName())) {

                    CapacityBinType capacityBinType = new CapacityBinType();
                    capacityBinType.setBinType(capacity.getBinType());
                    capacityBinType.setBinTypeUtilization((capacity.getBinTypeUtilization().
                            multiply(new BigDecimal(100)).
                            setScale(2, BigDecimal.ROUND_HALF_UP)).toString() + "%");

                    dto.getBinTypes().add(capacityBinType);

                    dto.setTotalUtilization((capacity.getTotalUtilization().
                            multiply(new BigDecimal(100)).
                            setScale(2, BigDecimal.ROUND_HALF_UP)).toString() + "%");
                }
            }
            //        排序
            TreeSet ts = new TreeSet(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    CapacityBinType c1 = (CapacityBinType) o1;
                    CapacityBinType c2 = (CapacityBinType) o2;

                    return c1.getBinType().compareTo(c2.getBinType());
                }
            });
            ts.addAll(dto.getBinTypes());
            dto.setBinTypes(ts);
            capacities.add(dto);
        }

        //获取总计
        List<Capacity> totalTypes = capacityTotalQuery.totalCapacity();
        CapacityDTO total = new CapacityDTO();
        total.setZoneName("总 计");
        for (Capacity capacity : totalTypes) {

            total.getBinTypes().add(new CapacityBinType(
                    capacity.getBinType(), (capacity.getBinTypeUtilization().
                    multiply(new BigDecimal("100")).
                    setScale(2, BigDecimal.ROUND_HALF_UP)).toString() + "%"));
        }

        total.setTotalUtilization((capacityTotalQuery.getTotalToTotal().
                multiply(new BigDecimal("100")).
                setScale(2, BigDecimal.ROUND_HALF_UP)).toString() + "%");
//        排序
        TreeSet ts = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CapacityBinType c1 = (CapacityBinType) o1;
                CapacityBinType c2 = (CapacityBinType) o2;

                return c1.getBinType().compareTo(c2.getBinType());
            }
        });
        ts.addAll(total.getBinTypes());
        total.setBinTypes(ts);
        capacities.add(total);

        return capacities;
    }

    //获取Capacity-Pod 数据
    @Override
    public List<CapacityPodDTO> getPods() {
        return capacityPodQuery.getPod();
    }

    //获取Capacity-pod-side 所有面 数据
    @Override
    public List<CapacityPodDTO> getByPodName(String podName) {
        return capacitySideQuery.getByPodName(podName);
    }

    //获取某个pod下 -> 某个面下 -> 所有bin(货位) 数据
    @Override
    public List<CapacityPodDTO> getByPodNameAndFace(String podName) {
        return capacityBinQuery.getByPodNameAndFace(podName);
    }


}
