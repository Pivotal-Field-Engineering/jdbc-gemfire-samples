package io.pivotal.gemfire.pubs.model;

import java.math.BigDecimal;
import java.util.Date;

public class Sale {
	private BigDecimal sonum;
    private String storId;
    private String ponum;
    private Date sdate;
    
    public BigDecimal getSonum() {
		return sonum;
	}
	public void setSonum(BigDecimal sonum) {
		this.sonum = sonum;
	}
	public String getStorId() {
		return storId;
	}
	public void setStorId(String storId) {
		this.storId = storId;
	}
	public String getPonum() {
		return ponum;
	}
	public void setPonum(String ponum) {
		this.ponum = ponum;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	@Override
	public String toString() {
		return "Sale [sonum=" + sonum + "]";
	}


}
