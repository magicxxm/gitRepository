package wms.crud.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import wms.common.crud.dto.BaseWarehouseAssignedDTO;
import wms.domain.MovementPosition;

import java.time.LocalDateTime;

public class MovementPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String companyCode;

    private String itemCode;

    private String itemName;

    private int allocatedQty;

    private int inventorySts;

    private String batch;

    private String lot;

    private String manufactureDate;

    private String expirationDate;

    private String fromloc;

    private String toloc;

    private String fromZone;

    private String toZone;

    private String fromLpn;

    private String toLpn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String movementID;

    private MovementDTO movement;

    public MovementPositionDTO() {
    }

    public MovementPositionDTO(MovementPosition entity) {
        super(entity);
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAllocatedQty() {
        return allocatedQty;
    }

    public void setAllocatedQty(int allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public int getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(int inventorySts) {
        this.inventorySts = inventorySts;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getFromloc() {
        return fromloc;
    }

    public void setFromloc(String fromloc) {
        this.fromloc = fromloc;
    }

    public String getToloc() {
        return toloc;
    }

    public void setToloc(String toloc) {
        this.toloc = toloc;
    }

    public String getFromZone() {
        return fromZone;
    }

    public void setFromZone(String fromZone) {
        this.fromZone = fromZone;
    }

    public String getToZone() {
        return toZone;
    }

    public void setToZone(String toZone) {
        this.toZone = toZone;
    }

    public String getFromLpn() {
        return fromLpn;
    }

    public void setFromLpn(String fromLpn) {
        this.fromLpn = fromLpn;
    }

    public String getToLpn() {
        return toLpn;
    }

    public void setToLpn(String toLpn) {
        this.toLpn = toLpn;
    }

    public String getMovementID() {
        return movementID;
    }

    public void setMovementID(String movementID) {
        this.movementID = movementID;
    }

    public MovementDTO getMovement() {
        return movement;
    }

    public void setMovement(MovementDTO movement) {
        this.movement = movement;
    }
}
