package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Titleditors implements Serializable {

	String edId
	String titleId
	BigDecimal edOrd

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append edId
		builder.append titleId
		builder.append edOrd
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append edId, other.edId
		builder.append titleId, other.titleId
		builder.append edOrd, other.edOrd
		builder.isEquals()
	}

	static mapping = {
		id composite: ["edId", "titleId", "edOrd"]
		version false
	}

	static constraints = {
		edId maxSize: 11
		titleId maxSize: 6, unique: ["ed_id"]
		edOrd nullable: true
	}
}
