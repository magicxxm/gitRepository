package wms.domain.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by 123 on 2017/5/11.
 */
@Entity
@Table(name="INV_UNITLOAD_SHIPMENT")
public class UnitLoadShipment implements Serializable{


    @Column(name="UNITLOAD_ID")
    private String unitLoadId;

    @Id
    @Column(name="SHIPMENT_ID")
    private String custometShipmentId;

    public String getUnitLoadId() {
        return unitLoadId;
    }

    public void setUnitLoadId(String unitLoadId) {
        this.unitLoadId = unitLoadId;
    }

    public String getCustometShipmentId() {
        return custometShipmentId;
    }

    public void setCustometShipmentId(String custometShipmentId) {
        this.custometShipmentId = custometShipmentId;
    }
}
