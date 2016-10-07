package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Publishers implements Serializable {

	String pubId
	String pubName
	String address
	String city
	String state

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append pubId
		builder.append pubName
		builder.append address
		builder.append city
		builder.append state
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append pubId, other.pubId
		builder.append pubName, other.pubName
		builder.append address, other.address
		builder.append city, other.city
		builder.append state, other.state
		builder.isEquals()
	}

	static mapping = {
		id composite: ["pubId", "pubName", "address", "city", "state"]
		version false
	}

	static constraints = {
		pubId maxSize: 4, unique: true
		pubName nullable: true, maxSize: 40
		address nullable: true, maxSize: 40
		city nullable: true, maxSize: 20
		state nullable: true, maxSize: 2
	}
}
