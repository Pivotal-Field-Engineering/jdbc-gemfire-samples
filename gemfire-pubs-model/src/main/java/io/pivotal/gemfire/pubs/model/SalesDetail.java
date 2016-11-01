package io.pivotal.gemfire.pubs.model;

import java.math.BigDecimal;
import java.util.Date;

public class SalesDetail {

    private BigDecimal sonum;
    private BigDecimal qtyOrdered;
    private BigDecimal qtyShipped;
    private String titleId;
    private Date dateShipped;


    @Override
    public String toString() {
        return "SalesDetail{" +
                "sonum=" + sonum +
                ", qtyOrdered=" + qtyOrdered +
                ", qtyShipped=" + qtyShipped +
                ", titleId='" + titleId + '\'' +
                ", dateShipped=" + dateShipped +
                '}';
    }

    public BigDecimal getSonum() {
        return sonum;
    }

    public void setSonum(BigDecimal sonum) {
        this.sonum = sonum;
    }

    public BigDecimal getQtyOrdered() {
        return qtyOrdered;
    }

    public void setQtyOrdered(BigDecimal qtyOrdered) {
        this.qtyOrdered = qtyOrdered;
    }

    public BigDecimal getQtyShipped() {
        return qtyShipped;
    }

    public void setQtyShipped(BigDecimal qtyShipped) {
        this.qtyShipped = qtyShipped;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public Date getDateShipped() {
        return dateShipped;
    }

    public void setDateShipped(Date dateShipped) {
        this.dateShipped = dateShipped;
    }
}
