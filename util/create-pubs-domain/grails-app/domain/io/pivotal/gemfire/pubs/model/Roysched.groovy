package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Roysched implements Serializable {

	String titleId
	BigDecimal lorange
	BigDecimal hirange
	BigDecimal royalty

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append titleId
		builder.append lorange
		builder.append hirange
		builder.append royalty
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append titleId, other.titleId
		builder.append lorange, other.lorange
		builder.append hirange, other.hirange
		builder.append royalty, other.royalty
		builder.isEquals()
	}

	static mapping = {
		id composite: ["titleId", "lorange", "hirange", "royalty"]
		version false
	}

	static constraints = {
		titleId maxSize: 6
		lorange nullable: true
		hirange nullable: true
		royalty nullable: true
	}
}
