package wms.crud.common.dto;

import wms.common.crud.dto.BaseWarehouseAssignedDTO;
import wms.domain.Stocktaking;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/4.
 */
public class StocktakingDTO extends BaseWarehouseAssignedDTO {

    private static final long serialVersionUID = 1L;

    private String stocktakingNo;

    private String name;

    private String zone;

    private int amount = 0;

    private String stocktakingType;

    private LocalDateTime ended;

    private LocalDateTime started;

    private BigInteger totalQty;

    private BigInteger okQty;

    private BigInteger ngQty;

    private BigDecimal dpmo;

    private BigInteger times;

    private BigDecimal rate;

    private String createdByUser;

    private LocalDateTime closeDate;

    private String originalCountId;

    private int counttype;

    private int status;

    private String remark;

    private List<StocktakingOrderDTO> invoices = new ArrayList<>();

    public StocktakingDTO() {
    }

    public StocktakingDTO(Stocktaking entity) {
        super(entity);
    }

    public String getStocktakingNo() {
        return stocktakingNo;
    }

    public void setStocktakingNo(String stocktakingNo) {
        this.stocktakingNo = stocktakingNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStocktakingType() {
        return stocktakingType;
    }

    public void setStocktakingType(String stocktakingType) {
        this.stocktakingType = stocktakingType;
    }

    public LocalDateTime getEnded() {
        return ended;
    }

    public void setEnded(LocalDateTime ended) {
        this.ended = ended;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public BigInteger getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigInteger totalQty) {
        this.totalQty = totalQty;
    }

    public BigInteger getOkQty() {
        return okQty;
    }

    public void setOkQty(BigInteger okQty) {
        this.okQty = okQty;
    }

    public BigInteger getNgQty() {
        return ngQty;
    }

    public void setNgQty(BigInteger ngQty) {
        this.ngQty = ngQty;
    }

    public BigDecimal getDpmo() {
        return dpmo;
    }

    public void setDpmo(BigDecimal dpmo) {
        this.dpmo = dpmo;
    }

    public BigInteger getTimes() {
        return times;
    }

    public void setTimes(BigInteger times) {
        this.times = times;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public int getCounttype() {
        return counttype;
    }

    public void setCounttype(int counttype) {
        this.counttype = counttype;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<StocktakingOrderDTO> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<StocktakingOrderDTO> invoices) {
        this.invoices = invoices;
    }

    public String getOriginalCountId() {
        return originalCountId;
    }

    public void setOriginalCountId(String originalCountId) {
        this.originalCountId = originalCountId;
    }
}
