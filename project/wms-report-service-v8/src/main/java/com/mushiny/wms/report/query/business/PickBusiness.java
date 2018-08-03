package com.mushiny.wms.report.query.business;

import com.mushiny.wms.report.query.dto.PickDTO;
import com.mushiny.wms.report.query.dto.picked.*;
import com.mushiny.wms.report.query.hql.PickedQuery;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// Pick行转列
@Component
public class PickBusiness implements Serializable {

    //获取 PickExSD total 行转列  #各发货时间 的 拣货总数量，已拣数量，未拣数量
    public List<PickDTO> totalZoneToPick(List<Picked> pickeds) {
        List<PickDTO> dtos = new ArrayList<>();

        PickDTO totals = new PickDTO();
        totals.setPpName("total");

        PickDTO pickedTotals = new PickDTO();
        pickedTotals.setPpName("picked");

        PickDTO notPickedTotals = new PickDTO();
        notPickedTotals.setPpName("notPicked");

        PickDTO pendingTotals = new PickDTO();
        pendingTotals.setPpName("pending");

        for (Picked picked : pickeds) {
            DeliveryDate dateTotal = new DeliveryDate(picked.getDeliveryDate().toLocalDateTime(), new PickAmount(picked.getTotal()));
            totals.getDeliveryDates().add(dateTotal);

            DeliveryDate dateTotalPicked = new DeliveryDate(picked.getDeliveryDate().toLocalDateTime(), new PickAmount(picked.getPicked()));
            pickedTotals.getDeliveryDates().add(dateTotalPicked);

            DeliveryDate dateTotalNotPicked = new DeliveryDate(picked.getDeliveryDate().toLocalDateTime(), new PickAmount(picked.getNotPicked()));
            notPickedTotals.getDeliveryDates().add(dateTotalNotPicked);

            DeliveryDate dateTotalPending = new DeliveryDate(picked.getDeliveryDate().toLocalDateTime(), new PickAmount(picked.getPending()));
            pendingTotals.getDeliveryDates().add(dateTotalPending);
        }
        //排序
        totals.setDeliveryDates(sortDeliveryDate(totals.getDeliveryDates()));
        pickedTotals.setDeliveryDates(sortDeliveryDate(pickedTotals.getDeliveryDates()));
        notPickedTotals.setDeliveryDates(sortDeliveryDate(notPickedTotals.getDeliveryDates()));
        pendingTotals.setDeliveryDates(sortDeliveryDate(pendingTotals.getDeliveryDates()));

        dtos.add(pendingTotals);
        dtos.add(notPickedTotals);
        dtos.add(pickedTotals);
        dtos.add(totals);
        return dtos;
    }

    //获取 PickExSD 明细 行转列  # 各PpName 、区域 ->  发货时间的 已拣数量 和 未拣数量
    public List<PickDTO> toPickExSD(List<Picked> pickeds) {
        //获取 ppnName 和 zone 列表，
        Set<PpNameZone> ppNameZones = pickeds.stream().map(dto ->
                new PpNameZone(dto.getPpName(), dto.getZoneName())).
                collect(Collectors.toSet());

        List<PickDTO> pickDTOS = new ArrayList<>();

        for (PpNameZone ppNameZone : ppNameZones) {
            PickDTO pickDTO = new PickDTO();
            pickDTO.setPpName(ppNameZone.getPpName());
            pickDTO.setZoneName(ppNameZone.getZoneName());
            //相同 ppName 和 区域 ->  添加 出货时间的 已拣数量和未拣数量
            for (Picked picked : pickeds) {
                if (ppNameZone.getPpName().equals(picked.getPpName()) &&
                        ppNameZone.getZoneName().equalsIgnoreCase(picked.getZoneName())) {

//                if(ppNameZone.getPpName().equals(picked.getPpName())) {
//                    if (ppNameZone.getZoneName() != null && picked.getZoneName() != null && ppNameZone.getZoneName().equalsIgnoreCase(picked.getZoneName())) {
                    DeliveryDate deliveryDate = new DeliveryDate(picked.getDeliveryDate().toLocalDateTime(),
                            new PickAmount(picked.getPicked(), picked.getNotPicked()));
                    //添加发货时间点 对应的 已拣数量 和 未拣数量
                    pickDTO.getDeliveryDates().add(deliveryDate);
//                }
//                    }
//                    else if (ppNameZone.getZoneName() == null && picked.getZoneName() == null){
//                        DeliveryDate deliveryDate = new DeliveryDate(picked.getDeliveryDate().toLocalDateTime(),
//                                new PickAmount(picked.getPicked(), picked.getNotPicked()));
//                        //添加发货时间点 对应的 已拣数量 和 未拣数量
//                        pickDTO.getDeliveryDates().add(deliveryDate);
//                    }
                }
            }
            //排序
            pickDTO.setDeliveryDates(sortDeliveryDate(pickDTO.getDeliveryDates()));
            pickDTOS.add(pickDTO);
        }
        return pickDTOS;
    }


    // 出货日期与拣货数量   根据日期排序
    private TreeSet sortDeliveryDate(Set<DeliveryDate> deliveryDates) {

        TreeSet ts = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                DeliveryDate d1 = (DeliveryDate) o1;
                DeliveryDate d2 = (DeliveryDate) o2;
                return d1.getDeliveryDate().compareTo(d2.getDeliveryDate());
            }
        });
        ts.addAll(deliveryDates);
        return ts;
    }


    //获取 PickArea Total  //# 各区域的 拣货总数量，已拣数量，未拣数量
    public List<PickDTO> totalDateToPick(List<Picked> pickeds) {
        List<PickDTO> dtos = new ArrayList<>();

        PickDTO totals = new PickDTO();
        totals.setPpName("total");

        PickDTO pickedTotals = new PickDTO();
        pickedTotals.setPpName("picked");

        PickDTO notPickedTotals = new PickDTO();
        notPickedTotals.setPpName("notPicked");

        PickDTO pendingTotals = new PickDTO();
        pendingTotals.setPpName("pending");

        for (Picked picked : pickeds) {
            Zones zoneTotal = new Zones(picked.getZoneName(), new PickAmount(picked.getTotal()));
            totals.getZonesSet().add(zoneTotal);

            Zones zoneTotalPicked = new Zones(picked.getZoneName(), new PickAmount(picked.getPicked()));
            pickedTotals.getZonesSet().add(zoneTotalPicked);

            Zones zoneTotalNotPicked = new Zones(picked.getZoneName(), new PickAmount(picked.getNotPicked()));
            notPickedTotals.getZonesSet().add(zoneTotalNotPicked);

            Zones zoneTotalPending = new Zones(picked.getZoneName(), new PickAmount(picked.getPending()));
            pendingTotals.getZonesSet().add(zoneTotalPending);
        }
//        if (totals.getZonesSet() == null){
//            totals.setZonesSet(null);
//        }else {
        //排序
//            totals.setZonesSet(totals.getZonesSet());
        totals.setZonesSet(sortZoneName(totals.getZonesSet()));
//        }

//        if (pickedTotals.getZonesSet() == null){
//            pickedTotals.setZonesSet(null);
//        }else {
//            pickedTotals.setZonesSet(pickedTotals.getZonesSet());
        pickedTotals.setZonesSet(sortZoneName(pickedTotals.getZonesSet()));
//        }

//        if (notPickedTotals.getZonesSet() == null){
//            notPickedTotals.setZonesSet(null);
//        }else {
//        notPickedTotals.setZonesSet(notPickedTotals.getZonesSet());
        notPickedTotals.setZonesSet(sortZoneName(notPickedTotals.getZonesSet()));
//        }

//        if (pendingTotals.getZonesSet() == null){
//            pendingTotals.setZonesSet(null);
//        }else {
//        pendingTotals.setZonesSet(pendingTotals.getZonesSet());
        pendingTotals.setZonesSet(sortZoneName(pendingTotals.getZonesSet()));
//        }
        dtos.add(pendingTotals);
        dtos.add(notPickedTotals);
        dtos.add(pickedTotals);
        dtos.add(totals);
        return dtos;
    }


    //获取 PickArea 明细 行转列 # 所有ppName 发货时间，区域，拣货总数量，已拣数量，未拣数量
    public List<PickDTO> toPickArea(List<Picked> pickeds) {

        //获取 ppnName 和 deliveryDate 列表，
        Set<PpNameTime> ppNameZones = pickeds.stream().map(dto ->
                new PpNameTime(dto.getPpName(), dto.getDeliveryDate().toLocalDateTime())).
                collect(Collectors.toSet());
        List<PickDTO> pickDTOS = new ArrayList<>();

        for (PpNameTime ppNameTime : ppNameZones) {
            PickDTO pickDTO = new PickDTO();
            pickDTO.setPpName(ppNameTime.getPpName());
            pickDTO.setDeliveryDate(ppNameTime.getDeliveryDate());
            for (Picked picked : pickeds) {
                //相同 ppName 和 发货点 ->  添加 出货时间的 已拣数量和未拣数量
                if (ppNameTime.getPpName().equals(picked.getPpName()) &&
                        ppNameTime.getDeliveryDate().equals(picked.getDeliveryDate().toLocalDateTime())) {

                    Zones zones = new Zones(picked.getZoneName(),
                            new PickAmount(picked.getPicked(), picked.getNotPicked()));
//                            new PickAmount(picked.getPicked(), picked.getNotPicked(),picked.getPending()));
                    pickDTO.getZonesSet().add(zones);
                }
            }
            //排序
//            pickDTO.setZonesSet(pickDTO.getZonesSet());
            pickDTO.setZonesSet(sortZoneName(pickDTO.getZonesSet()));

            pickDTOS.add(pickDTO);

        }
        return pickDTOS;

    }

    // ppName与拣货数量   根据ppName排序
    private TreeSet sortZoneName(Set<Zones> zones) {
        TreeSet ts = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Zones d1 = (Zones) o1;
                Zones d2 = (Zones) o2;
                return d1.getZoneName().compareTo(d2.getZoneName());
            }
        });
        ts.addAll(zones);
        return ts;
    }
}