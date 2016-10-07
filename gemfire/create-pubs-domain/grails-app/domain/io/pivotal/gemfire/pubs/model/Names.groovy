package io.pivotal.gemfire.pubs.model

class Names {

	String name

	static mapping = {
		id name: "name", generator: "assigned"
		version false
	}

	static constraints = {
		name nullable: true
	}
}
