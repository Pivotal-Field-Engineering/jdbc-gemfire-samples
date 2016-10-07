package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Salesdetails implements Serializable {

	BigDecimal sonum
	BigDecimal qtyOrdered
	BigDecimal qtyShipped
	String titleId
	Date dateShipped

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append sonum
		builder.append qtyOrdered
		builder.append qtyShipped
		builder.append titleId
		builder.append dateShipped
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append sonum, other.sonum
		builder.append qtyOrdered, other.qtyOrdered
		builder.append qtyShipped, other.qtyShipped
		builder.append titleId, other.titleId
		builder.append dateShipped, other.dateShipped
		builder.isEquals()
	}

	static mapping = {
		id composite: ["sonum", "qtyOrdered", "qtyShipped", "titleId", "dateShipped"]
		version false
	}

	static constraints = {
		qtyShipped nullable: true
		titleId maxSize: 6, unique: ["sonum"]
		dateShipped nullable: true
	}
}
