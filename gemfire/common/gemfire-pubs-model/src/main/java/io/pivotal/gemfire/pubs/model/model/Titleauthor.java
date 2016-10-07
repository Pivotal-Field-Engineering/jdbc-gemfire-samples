package io.pivotal.gemfire.pubs.model.model;

import java.math.BigDecimal;

class TitleAuthor {

     private String auId;
     private String titleId;
     private BigDecimal auOrd;
     private BigDecimal royaltyshare;

    @Override
    public String toString() {
        return "TitleAuthor{" +
                "auId='" + auId + '\'' +
                ", titleId='" + titleId + '\'' +
                ", auOrd=" + auOrd +
                ", royaltyshare=" + royaltyshare +
                '}';
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public BigDecimal getAuOrd() {
        return auOrd;
    }

    public void setAuOrd(BigDecimal auOrd) {
        this.auOrd = auOrd;
    }

    public BigDecimal getRoyaltyshare() {
        return royaltyshare;
    }

    public void setRoyaltyshare(BigDecimal royaltyshare) {
        this.royaltyshare = royaltyshare;
    }
}
