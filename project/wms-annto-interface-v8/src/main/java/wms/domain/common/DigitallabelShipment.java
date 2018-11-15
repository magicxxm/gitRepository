package wms.domain.common;

import wms.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/9/25.
 */
@Entity
@Table(name = "OB_DIGITALLABEL_SHIPMENT")
public class DigitallabelShipment extends BaseEntity {

    @Column(name = "STATE", nullable = false)
    private int state;

    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private CustomerShipment shipment;

    @Column(name = "DIGITALLABEL2")
    private String digitalLabel2;

    @ManyToOne
    @JoinColumn(name = "STATION_ID")
    private PackingStation packingStation;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @Column(name = "PICKPACKWALL_ID")
    private String pickPackWallId;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public CustomerShipment getShipment() {
        return shipment;
    }

    public void setShipment(CustomerShipment shipment) {
        this.shipment = shipment;
    }

    public String getDigitalLabel2() {
        return digitalLabel2;
    }

    public void setDigitalLabel2(String digitalLabel2) {
        this.digitalLabel2 = digitalLabel2;
    }

    public PackingStation getPackingStation() {
        return packingStation;
    }

    public void setPackingStation(PackingStation packingStation) {
        this.packingStation = packingStation;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public String getPickPackWallId() {
        return pickPackWallId;
    }

    public void setPickPackWallId(String pickPackWallId) {
        this.pickPackWallId = pickPackWallId;
    }
}
