package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Sales implements Serializable {

	BigDecimal sonum
	String storId
	String ponum
	Date sdate

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append sonum
		builder.append storId
		builder.append ponum
		builder.append sdate
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append sonum, other.sonum
		builder.append storId, other.storId
		builder.append ponum, other.ponum
		builder.append sdate, other.sdate
		builder.isEquals()
	}

	static mapping = {
		id composite: ["sonum", "storId", "ponum", "sdate"]
		version false
	}

	static constraints = {
		sonum unique: true
		storId maxSize: 4
		ponum maxSize: 20
		sdate nullable: true
	}
}
