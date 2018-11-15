package wms.crud.common.dto;

/**
 * Created by PC-4 on 2017/8/4.
 */
import wms.common.crud.dto.BaseClientAssignedDTO;
import wms.domain.CustomerOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerOrderDTO extends BaseClientAssignedDTO {

    private String customerName;

    private String customerNo;

    private String deliveryDate;

    private String sortCode;

    private String orderNo;

    private int priority;

    private int state;

    private String strategyId;

    private OrderStrategyDTO strategy;

//    private ClientDTO clientDTO;
//
//    private WarehouseDTO warehouseDTO;

    private String code;

    private String companyCode;

    private String warehouseCode;

    private String shipmenttype;

    private String requesteddeliverydate;

    private String sourceplatform;

    private String carriercode;

    private String carrierservice;

    private String primarywaybillcode;

    private String shortaddress;

    private String storecode;

    private String storename;

    private BigDecimal totalvalue;

    private int coderequired;

    private BigDecimal codevalue;

    private int invoicerequired;

    private String operatetime;

    private String operatorcode;

    private String operatorname;

    private List<CustomerOrderPositionDTO> invoices = new ArrayList<>();

    public CustomerOrderDTO() {
    }
    public CustomerOrderDTO(CustomerOrder entity) {
        super(entity);
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public OrderStrategyDTO getStrategy() {
        return strategy;
    }

    public void setStrategy(OrderStrategyDTO strategy) {
        this.strategy = strategy;
    }

    public List<CustomerOrderPositionDTO> getPositions() {
        return invoices;
    }

    public void setPositions(List<CustomerOrderPositionDTO> invoices) {
        this.invoices = invoices;
    }

//    public ClientDTO getClientDTO() {
//        return clientDTO;
//    }
//    public void setClientDTO(ClientDTO clientDTO) {
//        this.clientDTO = clientDTO;
//    }
//    public WarehouseDTO getWarehouseDTO() {
//        return warehouseDTO;
//    }
//    public void setWarehouseDTO(WarehouseDTO warehouseDTO) {
//        this.warehouseDTO = warehouseDTO;
//    }


    public String getShipmenttype() {
        return shipmenttype;
    }

    public void setShipmenttype(String shipmenttype) {
        this.shipmenttype = shipmenttype;
    }

    public String getRequesteddeliverydate() {
        return requesteddeliverydate;
    }

    public void setRequesteddeliverydate(String requesteddeliverydate) {
        this.requesteddeliverydate = requesteddeliverydate;
    }

    public String getSourceplatform() {
        return sourceplatform;
    }

    public void setSourceplatform(String sourceplatform) {
        this.sourceplatform = sourceplatform;
    }

    public String getCarriercode() {
        return carriercode;
    }

    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
    }

    public String getCarrierservice() {
        return carrierservice;
    }

    public void setCarrierservice(String carrierservice) {
        this.carrierservice = carrierservice;
    }

    public String getPrimarywaybillcode() {
        return primarywaybillcode;
    }

    public void setPrimarywaybillcode(String primarywaybillcode) {
        this.primarywaybillcode = primarywaybillcode;
    }

    public String getShortaddress() {
        return shortaddress;
    }

    public void setShortaddress(String shortaddress) {
        this.shortaddress = shortaddress;
    }

    public String getStorecode() {
        return storecode;
    }

    public void setStorecode(String storecode) {
        this.storecode = storecode;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public BigDecimal getTotalvalue() {
        return totalvalue;
    }

    public void setTotalvalue(BigDecimal totalvalue) {
        this.totalvalue = totalvalue;
    }

    public int getCoderequired() {
        return coderequired;
    }

    public void setCoderequired(int coderequired) {
        this.coderequired = coderequired;
    }

    public BigDecimal getCodevalue() {
        return codevalue;
    }

    public void setCodevalue(BigDecimal codevalue) {
        this.codevalue = codevalue;
    }

    public int getInvoicerequired() {
        return invoicerequired;
    }

    public void setInvoicerequired(int invoicerequired) {
        this.invoicerequired = invoicerequired;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public String getOperatorcode() {
        return operatorcode;
    }

    public void setOperatorcode(String operatorcode) {
        this.operatorcode = operatorcode;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}

