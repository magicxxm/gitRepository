package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.schedule.common.RebatchRequestState;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "OB_REBATCHREQUEST")
public class RebatchRequest extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "REBATCHREQUEST_NO", unique = true, nullable = false)
    private String number;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private RebatchRequestState rebatchState = RebatchRequestState.RAW;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PICKINGORDER_ID")
    private PickingOrder pickingOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBATCHSLOT_ID")
    private RebatchSlot rebatchSlot;

    @ManyToOne(optional = true)
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "REBATCHSTATION_ID")
    private RebatchStation rebatchStation;

    //    @OneToMany(mappedBy = "rebatchRequest", cascade = {CascadeType.ALL}, orphanRemoval = true,fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "rebatchRequest", fetch = FetchType.LAZY)
    private List<RebatchRequestPosition> positions;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RebatchRequestState getRebatchState() {
        return rebatchState;
    }

    public void setRebatchState(RebatchRequestState rebatchState) {
        this.rebatchState = rebatchState;
    }

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public RebatchSlot getRebatchSlot() {
        return rebatchSlot;
    }

    public void setRebatchSlot(RebatchSlot rebatchSlot) {
        this.rebatchSlot = rebatchSlot;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public RebatchStation getRebatchStation() {
        return rebatchStation;
    }

    public void setRebatchStation(RebatchStation rebatchStation) {
        this.rebatchStation = rebatchStation;
    }
    //    public String getRebatchStationId() {
//        return rebatchStationId;
//    }
//
//    public void setRebatchStationId(String rebatchStationId) {
//        this.rebatchStationId = rebatchStationId;
//    }

    public List<RebatchRequestPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<RebatchRequestPosition> positions) {
        this.positions = positions;
    }

}
