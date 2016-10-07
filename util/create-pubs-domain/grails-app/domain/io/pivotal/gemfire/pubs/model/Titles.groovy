package io.pivotal.gemfire.pubs.model

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Titles implements Serializable {

	String titleId
	String title
	String type
	String pubId
	BigDecimal price
	BigDecimal advance
	Integer ytdSales
	Character contract
	String notes
	Date pubdate

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append titleId
		builder.append title
		builder.append type
		builder.append pubId
		builder.append price
		builder.append advance
		builder.append ytdSales
		builder.append contract
		builder.append notes
		builder.append pubdate
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append titleId, other.titleId
		builder.append title, other.title
		builder.append type, other.type
		builder.append pubId, other.pubId
		builder.append price, other.price
		builder.append advance, other.advance
		builder.append ytdSales, other.ytdSales
		builder.append contract, other.contract
		builder.append notes, other.notes
		builder.append pubdate, other.pubdate
		builder.isEquals()
	}

	static mapping = {
		id composite: ["titleId", "title", "type", "pubId", "price", "advance", "ytdSales", "contract", "notes", "pubdate"]
		version false
	}

	static constraints = {
		titleId maxSize: 6, unique: true
		title maxSize: 80
		type nullable: true, maxSize: 12
		pubId nullable: true, maxSize: 4
		price nullable: true
		advance nullable: true
		ytdSales nullable: true
		contract maxSize: 1
		notes nullable: true, maxSize: 200
		pubdate nullable: true
	}
}
