package com.mushiny.wms.report.query.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.report.query.dto.picked.DeliveryDate;
import com.mushiny.wms.report.query.dto.picked.PickAmount;
import com.mushiny.wms.report.query.dto.picked.Zones;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PickDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ppName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deliveryDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String zoneName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<String, BigDecimal>> dateTotal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<String, BigDecimal>> dateTotalPicked;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<String, BigDecimal>> dateTotalNotPicked;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<String, BigDecimal>> dateTotalPending;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<String, PickAmount>> zones;

//    //新
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private Set<Zones> totalDate = new HashSet<>();

    //新
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Zones> zonesSet = new HashSet<>();

    //新
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<DeliveryDate> deliveryDates = new HashSet<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<LocalDateTime, BigDecimal>> zoneTotal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<LocalDateTime, BigDecimal>> zoneTotalPicked;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<LocalDateTime, BigDecimal>> zoneTotalNotPicked;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Map<LocalDateTime, PickAmount>> dates;


    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Set<Map<String, BigDecimal>> getDateTotal() {
        return dateTotal;
    }

    public void setDateTotal(Set<Map<String, BigDecimal>> dateTotal) {
        this.dateTotal = dateTotal;
    }

    public Set<Map<String, BigDecimal>> getDateTotalPicked() {
        return dateTotalPicked;
    }

    public void setDateTotalPicked(Set<Map<String, BigDecimal>> dateTotalPicked) {
        this.dateTotalPicked = dateTotalPicked;
    }

    public Set<Map<String, BigDecimal>> getDateTotalNotPicked() {
        return dateTotalNotPicked;
    }

    public void setDateTotalNotPicked(Set<Map<String, BigDecimal>> dateTotalNotPicked) {
        this.dateTotalNotPicked = dateTotalNotPicked;
    }

    public Set<Map<String, PickAmount>> getZones() {
        return zones;
    }

    public void setZones(Set<Map<String, PickAmount>> zones) {
        this.zones = zones;
    }

    public Set<Map<LocalDateTime, BigDecimal>> getZoneTotal() {
        return zoneTotal;
    }

    public void setZoneTotal(Set<Map<LocalDateTime, BigDecimal>> zoneTotal) {
        this.zoneTotal = zoneTotal;
    }

    public Set<Map<LocalDateTime, BigDecimal>> getZoneTotalPicked() {
        return zoneTotalPicked;
    }

    public void setZoneTotalPicked(Set<Map<LocalDateTime, BigDecimal>> zoneTotalPicked) {
        this.zoneTotalPicked = zoneTotalPicked;
    }

    public Set<Map<LocalDateTime, BigDecimal>> getZoneTotalNotPicked() {
        return zoneTotalNotPicked;
    }

    public void setZoneTotalNotPicked(Set<Map<LocalDateTime, BigDecimal>> zoneTotalNotPicked) {
        this.zoneTotalNotPicked = zoneTotalNotPicked;
    }

    public Set<Map<LocalDateTime, PickAmount>> getDates() {
        return dates;
    }

    public void setDates(Set<Map<LocalDateTime, PickAmount>> dates) {
        this.dates = dates;
    }

    public Set<Zones> getZonesSet() {
        return zonesSet;
    }

    public void setZonesSet(Set<Zones> zonesSet) {
        this.zonesSet = zonesSet;
    }

    public Set<DeliveryDate> getDeliveryDates() {
        return deliveryDates;
    }

    public void setDeliveryDates(Set<DeliveryDate> deliveryDates) {
        this.deliveryDates = deliveryDates;
    }

    public Set<Map<String, BigDecimal>> getDateTotalPending() {
        return dateTotalPending;
    }

    public void setDateTotalPending(Set<Map<String, BigDecimal>> dateTotalPending) {
        this.dateTotalPending = dateTotalPending;
    }


//    public Set<Zones> getTotalDate() {
//        return totalDate;
//    }
//
//    public void setTotalDate(Set<Zones> totalDate) {
//        this.totalDate = totalDate;
//    }
}
