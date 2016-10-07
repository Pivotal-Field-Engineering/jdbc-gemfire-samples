package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Authors implements Serializable {

	String auId
	String auLname
	String auFname
	String phone
	String address
	String city
	String state
	String zip
	Integer processed

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append auId
		builder.append auLname
		builder.append auFname
		builder.append phone
		builder.append address
		builder.append city
		builder.append state
		builder.append zip
		builder.append processed
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append auId, other.auId
		builder.append auLname, other.auLname
		builder.append auFname, other.auFname
		builder.append phone, other.phone
		builder.append address, other.address
		builder.append city, other.city
		builder.append state, other.state
		builder.append zip, other.zip
		builder.append processed, other.processed
		builder.isEquals()
	}

	static mapping = {
		id composite: ["auId", "auLname", "auFname", "phone", "address", "city", "state", "zip", "processed"]
		version false
	}

	static constraints = {
		auId maxSize: 11, unique: true
		auLname maxSize: 40
		auFname maxSize: 20
		phone nullable: true, maxSize: 12
		address nullable: true, maxSize: 40
		city nullable: true, maxSize: 20
		state nullable: true, maxSize: 2
		zip nullable: true, maxSize: 5
		processed nullable: true
	}
}
