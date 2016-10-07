package io.pivotal.kaiser.gemfire.common.model;

import java.math.BigInteger;
import java.sql.Timestamp;

import io.pivotal.kaiser.gemfire.common.key.MemberPhoneKey;

public class MemberPhone {

	BigInteger uid;
	BigInteger memberId;
	String mrn;
	String mrnPrefix;
	String memberPhoneTypeCode;
	String memberPhoneLocCode;
	String memberPhoneAreaCode;
	String memberPhoneNumberPrefix;
	String memberPhoneNumberSuffix;
	String memberPhoneNumberXtn;
	String memberPhoneStsCode;
	String entryId;
	Timestamp entryDatetime;
	String updateId;
	Timestamp updateDatetime;
	Timestamp ttgUpdatetime;
	String activeFlag;

	public BigInteger getUid() {
		return uid;
	}

	public void setUid(BigInteger uid) {
		this.uid = uid;
	}

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	public String getMrnPrefix() {
		return mrnPrefix;
	}

	public void setMrnPrefix(String mrnPrefix) {
		this.mrnPrefix = mrnPrefix;
	}

	public String getMemberPhoneTypeCode() {
		return memberPhoneTypeCode;
	}

	public void setMemberPhoneTypeCode(String memberPhoneTypeCode) {
		this.memberPhoneTypeCode = memberPhoneTypeCode;
	}

	public String getMemberPhoneLocCode() {
		return memberPhoneLocCode;
	}

	public void setMemberPhoneLocCode(String memberPhoneLocCode) {
		this.memberPhoneLocCode = memberPhoneLocCode;
	}

	public String getMemberPhoneAreaCode() {
		return memberPhoneAreaCode;
	}

	public void setMemberPhoneAreaCode(String memberPhoneAreaCode) {
		this.memberPhoneAreaCode = memberPhoneAreaCode;
	}

	public String getMemberPhoneNumberPrefix() {
		return memberPhoneNumberPrefix;
	}

	public void setMemberPhoneNumberPrefix(String memberPhoneNumberPrefix) {
		this.memberPhoneNumberPrefix = memberPhoneNumberPrefix;
	}

	public String getMemberPhoneNumberSuffix() {
		return memberPhoneNumberSuffix;
	}

	public void setMemberPhoneNumberSuffix(String memberPhoneNumberSuffix) {
		this.memberPhoneNumberSuffix = memberPhoneNumberSuffix;
	}

	public String getMemberPhoneNumberXtn() {
		return memberPhoneNumberXtn;
	}

	public void setMemberPhoneNumberXtn(String memberPhoneNumberXtn) {
		this.memberPhoneNumberXtn = memberPhoneNumberXtn;
	}

	public String getMemberPhoneStsCode() {
		return memberPhoneStsCode;
	}

	public void setMemberPhoneStsCode(String memberPhoneStsCode) {
		this.memberPhoneStsCode = memberPhoneStsCode;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public Timestamp getEntryDatetime() {
		return entryDatetime;
	}

	public void setEntryDatetime(Timestamp entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public Timestamp getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Timestamp updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public Timestamp getTtgUpdatetime() {
		return ttgUpdatetime;
	}

	public void setTtgUpdatetime(Timestamp ttgUpdatetime) {
		this.ttgUpdatetime = ttgUpdatetime;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

}
