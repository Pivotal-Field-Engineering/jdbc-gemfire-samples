package io.pivotal.gemfire.pubs.model;

import java.math.BigDecimal;
import java.util.Date;

public class Title  {

    private String titleId;
    private String title;
    private String type;
    private String pubId;
    private BigDecimal price;
    private BigDecimal advance;
    private Integer ytdSales;
    private Character contract;
    private String notes;
    private Date pubdate;

    @Override
    public String toString() {
        return "Title{" +
                "titleId='" + titleId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", pubId='" + pubId + '\'' +
                ", price=" + price +
                ", advance=" + advance +
                ", ytdSales=" + ytdSales +
                ", contract=" + contract +
                ", notes='" + notes + '\'' +
                ", pubdate=" + pubdate +
                '}';
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public Integer getYtdSales() {
        return ytdSales;
    }

    public void setYtdSales(Integer ytdSales) {
        this.ytdSales = ytdSales;
    }

    public Character getContract() {
        return contract;
    }

    public void setContract(Character contract) {
        this.contract = contract;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }
}
