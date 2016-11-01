package io.pivotal.gemfire.pubs.model;

import java.math.BigDecimal;

public class TitleEditor {

    private String edId;
    private String titleId;
    private BigDecimal edOrd;

    @Override
    public String toString() {
        return "TitleEditor{" +
                "edId='" + edId + '\'' +
                ", titleId='" + titleId + '\'' +
                ", edOrd=" + edOrd +
                '}';
    }

    public String getEdId() {
        return edId;
    }

    public void setEdId(String edId) {
        this.edId = edId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public BigDecimal getEdOrd() {
        return edOrd;
    }

    public void setEdOrd(BigDecimal edOrd) {
        this.edOrd = edOrd;
    }

}
