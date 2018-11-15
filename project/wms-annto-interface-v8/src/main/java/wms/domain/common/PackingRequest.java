package wms.domain.common;


import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name="OB_PACKINGREQUEST")
public class PackingRequest extends BaseClientAssignedEntity {

    @Column(name="PACKING_NO")
    private String packingNo;

    @Column(name="STATE")
    private String state;

    @ManyToOne
    @JoinColumn(name="CUSTOMERSHIPMENT_ID")
    private CustomerShipment customerShipment;

    @ManyToOne
    @JoinColumn(name="FROMUNITLOAD_ID")
    private UnitLoad fromUnitLoad;

    @ManyToOne
    @JoinColumn(name="TOUNITLOAD_ID")
    private UnitLoad toUnitLoad;

    @ManyToOne
    @JoinColumn(name="OPERATOR_ID")
    private User operator;

    @ManyToOne
    @JoinColumn(name="PACKINGSTATION_ID")
    private PackingStation packingStation;

    @ManyToOne
    @JoinColumn(name="BOXTYPE_ID")
    private BoxType boxType;

    @Column(name="WEIGHT")
    private BigDecimal weight;

    @OneToMany(mappedBy = "packingRequest", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PackingRequestPosition> positions = new ArrayList<>();

    public void addPosition(PackingRequestPosition position) {
        getPositions().add(position);
        position.setPackingRequest(this);
    }
    public String getPackingNo() {
        return packingNo;
    }

    public void setPackingNo(String packingNo) {
        this.packingNo = packingNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CustomerShipment getCustomerShipment() {
        return customerShipment;
    }

    public void setCustomerShipment(CustomerShipment customerShipment) {
        this.customerShipment = customerShipment;
    }

    public UnitLoad getFromUnitLoad() {
        return fromUnitLoad;
    }

    public void setFromUnitLoad(UnitLoad fromUnitLoad) {
        this.fromUnitLoad = fromUnitLoad;
    }

    public UnitLoad getToUnitLoad() {
        return toUnitLoad;
    }

    public void setToUnitLoad(UnitLoad toUnitLoad) {
        this.toUnitLoad = toUnitLoad;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public PackingStation getPackingStation() {
        return packingStation;
    }

    public void setPackingStation(PackingStation packingStation) {
        this.packingStation = packingStation;
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public void setBoxType(BoxType boxType) {
        this.boxType = boxType;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public List<PackingRequestPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PackingRequestPosition> positions) {
        this.positions = positions;
    }
}
