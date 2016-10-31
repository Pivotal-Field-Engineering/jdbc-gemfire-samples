package io.pivotal.gemfire.pubs.model;

import java.math.BigDecimal;

public class RoyaltySchedule {

    private String titleId;
    private BigDecimal lorange;
    private BigDecimal hirange;

    @Override
    public String toString() {
        return "RoyaltySchedule{" +
                "titleId='" + titleId + '\'' +
                ", lorange=" + lorange +
                ", hirange=" + hirange +
                ", royalty=" + royalty +
                '}';
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public BigDecimal getLorange() {
        return lorange;
    }

    public void setLorange(BigDecimal lorange) {
        this.lorange = lorange;
    }

    public BigDecimal getHirange() {
        return hirange;
    }

    public void setHirange(BigDecimal hirange) {
        this.hirange = hirange;
    }

    public BigDecimal getRoyalty() {
        return royalty;
    }

    public void setRoyalty(BigDecimal royalty) {
        this.royalty = royalty;
    }

    private BigDecimal royalty;

}
