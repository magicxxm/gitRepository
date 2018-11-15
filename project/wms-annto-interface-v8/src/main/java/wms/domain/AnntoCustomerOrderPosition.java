package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/10.
 */
@Entity
@Table(name = "ANNTO_CUSTOMERORDERPOSITION")
public class AnntoCustomerOrderPosition extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ORDER_ID",nullable = false)
    private AnntoCustomerOrder anntoCustomerOrder;

    //发票类型（VINVOICE：增值税专用发票、GVINVOICE：增值税普通发票、BTINVOICE：营业税发票、INVOICE：普通发票、EVINVOICE：电子增票、OTHER：其它发票）
    @Column(name = "INVOICETYPE")
    private String invoicetype;

    //发票抬头
    @Column(name = "INVOICEHEADER")
    private String invoiceheader;

    //发票总金额
    @Column(name = "INVOICEAMOUNT")
    private double invoiceamount;

    //折扣总金额
    @Column(name = "DISCOUNT_AMOUNT")
    private double discountAmount;

    @OneToMany(mappedBy = "anntoCustomerOrderPosition", cascade = {CascadeType.ALL}, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<AnntoCustomerInvoiceItem> invoiceItems = new ArrayList<>();

    public AnntoCustomerOrder getAnntoCustomerOrder() {
        return anntoCustomerOrder;
    }

    public void setAnntoCustomerOrder(AnntoCustomerOrder anntoCustomerOrder) {
        this.anntoCustomerOrder = anntoCustomerOrder;
    }

    public String getInvoicetype() {
        return invoicetype;
    }

    public void setInvoicetype(String invoicetype) {
        this.invoicetype = invoicetype;
    }

    public String getInvoiceheader() {
        return invoiceheader;
    }

    public void setInvoiceheader(String invoiceheader) {
        this.invoiceheader = invoiceheader;
    }

    public double getInvoiceamount() {
        return invoiceamount;
    }

    public void setInvoiceamount(double invoiceamount) {
        this.invoiceamount = invoiceamount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
