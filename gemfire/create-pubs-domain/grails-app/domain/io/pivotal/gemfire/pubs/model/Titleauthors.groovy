package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Titleauthors implements Serializable {

	String auId
	String titleId
	BigDecimal auOrd
	BigDecimal royaltyshare

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append auId
		builder.append titleId
		builder.append auOrd
		builder.append royaltyshare
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append auId, other.auId
		builder.append titleId, other.titleId
		builder.append auOrd, other.auOrd
		builder.append royaltyshare, other.royaltyshare
		builder.isEquals()
	}

	static mapping = {
		id composite: ["auId", "titleId", "auOrd", "royaltyshare"]
		version false
	}

	static constraints = {
		auId maxSize: 11
		titleId maxSize: 6, unique: ["au_id"]
		royaltyshare nullable: true
	}
}
