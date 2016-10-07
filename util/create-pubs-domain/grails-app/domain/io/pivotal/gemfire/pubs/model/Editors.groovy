package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Editors implements Serializable {

	String edId
	String edLname
	String edFname
	String edPos
	String phone
	String address
	String city
	String state
	String zip
	String edBoss

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append edId
		builder.append edLname
		builder.append edFname
		builder.append edPos
		builder.append phone
		builder.append address
		builder.append city
		builder.append state
		builder.append zip
		builder.append edBoss
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append edId, other.edId
		builder.append edLname, other.edLname
		builder.append edFname, other.edFname
		builder.append edPos, other.edPos
		builder.append phone, other.phone
		builder.append address, other.address
		builder.append city, other.city
		builder.append state, other.state
		builder.append zip, other.zip
		builder.append edBoss, other.edBoss
		builder.isEquals()
	}

	static mapping = {
		id composite: ["edId", "edLname", "edFname", "edPos", "phone", "address", "city", "state", "zip", "edBoss"]
		version false
	}

	static constraints = {
		edId maxSize: 11, unique: true
		edLname maxSize: 40
		edFname maxSize: 20
		edPos nullable: true, maxSize: 12
		phone nullable: true, maxSize: 12
		address nullable: true, maxSize: 40
		city nullable: true, maxSize: 20
		state nullable: true, maxSize: 2
		zip nullable: true, maxSize: 5
		edBoss nullable: true, maxSize: 11
	}
}
