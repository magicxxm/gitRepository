package wms.crud.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class InvoicesDTO {

    //"invoicetype": "发票类型（VINVOICE：增值税专用发票、GVINVOICE：增值税普通发票、BTINVOICE：营业税发票、INVOICE：普通发票、EVINVOICE：电子增票、OTHER：其它发票），string (1)，必填",
    private String invoicetype;

    //"invoiceheader": "发票抬头，string (200)，必填",
    private String invoiceheader;

    //"invoiceamount": "发票总金额，double (18, 2)，必填",
    private double invoiceamount;

    //"discountAmount": "折扣总金额，double (18, 2)",
    private double discountAmount;

    //"number": "发票号码，string(50)，必填",
    private String number;

    private List<InvoiceItemsDTO> invoiceItems = new ArrayList<>();


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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<InvoiceItemsDTO> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemsDTO> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

}
