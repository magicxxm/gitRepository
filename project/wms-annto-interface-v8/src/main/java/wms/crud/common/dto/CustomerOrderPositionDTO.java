package wms.crud.common.dto;

/**
 * Created by PC-4 on 2017/8/4.
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import wms.common.crud.dto.BaseClientAssignedDTO;
import wms.domain.CustomerOrderPosition;
import java.math.BigDecimal;

public class CustomerOrderPositionDTO extends BaseClientAssignedDTO {

    private static final long serialVersionUID = 1L;

    private BigDecimal amount;

    private int orderIndex;

    private int positionNo;

    private int state = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String itemDataId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderId;

    private ItemDataDTO itemData;

    private CustomerOrderDTO customerOrder;

    private String primarywaybillcode;

    private String containercode;

    private String containertype;

    private BigDecimal totalweight;

    private BigDecimal totalvolume;

    private String itemcode;

    private int quantity;

    private String lineno;

    private String inventorytype;

    private String unit;

    private String lotno;


    public CustomerOrderPositionDTO() {
    }
    public CustomerOrderPositionDTO(CustomerOrderPosition entity) {
        super(entity);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ItemDataDTO getItemData() {
        return itemData;
    }

    public void setItemData(ItemDataDTO itemData) {
        this.itemData = itemData;
    }

    public CustomerOrderDTO getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrderDTO customerOrder) {
        this.customerOrder = customerOrder;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrimarywaybillcode() {
        return primarywaybillcode;
    }

    public void setPrimarywaybillcode(String primarywaybillcode) {
        this.primarywaybillcode = primarywaybillcode;
    }

    public String getContainercode() {
        return containercode;
    }

    public void setContainercode(String containercode) {
        this.containercode = containercode;
    }

    public String getContainertype() {
        return containertype;
    }

    public void setContainertype(String containertype) {
        this.containertype = containertype;
    }

    public BigDecimal getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(BigDecimal totalweight) {
        this.totalweight = totalweight;
    }

    public BigDecimal getTotalvolume() {
        return totalvolume;
    }

    public void setTotalvolume(BigDecimal totalvolume) {
        this.totalvolume = totalvolume;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLineno() {
        return lineno;
    }

    public void setLineno(String lineno) {
        this.lineno = lineno;
    }

    public String getInventorytype() {
        return inventorytype;
    }

    public void setInventorytype(String inventorytype) {
        this.inventorytype = inventorytype;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }
}
