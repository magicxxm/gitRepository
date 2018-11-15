package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.schedule.common.RebatchRequestPositionState;

import javax.persistence.*;

@Entity
@Table(name = "OB_REBATCHREQUESTPOSITION")
public class RebatchRequestPosition extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private RebatchRequestPositionState rebatchState = RebatchRequestPositionState.RAW;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SOURCE_ID")
    private UnitLoad source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBATCHSLOT_ID")
    private RebatchSlot rebatchSlot;

    @ManyToOne(optional = true)
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "REBATCHSTATION_ID")
    private RebatchStation rebatchStation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBATCHREQUEST_ID")
    private RebatchRequest rebatchRequest;

    public RebatchRequestPositionState getRebatchState() {
        return rebatchState;
    }

    public void setRebatchState(RebatchRequestPositionState rebatchState) {
        this.rebatchState = rebatchState;
    }

    public UnitLoad getSource() {
        return source;
    }

    public void setSource(UnitLoad source) {
        this.source = source;
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

    public RebatchRequest getRebatchRequest() {
        return rebatchRequest;
    }

    public void setRebatchRequest(RebatchRequest rebatchRequest) {
        this.rebatchRequest = rebatchRequest;
    }
}
