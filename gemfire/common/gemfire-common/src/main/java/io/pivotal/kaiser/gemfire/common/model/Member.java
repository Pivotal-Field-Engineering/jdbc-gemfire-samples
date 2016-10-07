package io.pivotal.kaiser.gemfire.common.model;



import java.math.BigInteger;
import java.sql.Timestamp;

import io.pivotal.kaiser.gemfire.common.key.MemberKey;

public class Member{

	private String  regionCode;
	private BigInteger guid;
	private String  uid;
	private String  mrn;
	private String  mrnPrefix;
	private String  firstName;
	private  String  lastName;
	private  String  middleName;
	private String  displayName;
	private  String  gender;
	private Timestamp birthDate;
	private String  deathFlag;
	private Timestamp deathDate;
	private  String  emailAddress;
	private String  blindFlag;
	private String  deafFlag;
	private String  muteFlag;
	private String  kpmcpEmpFlag;
	private String  intrptrReqCode;
	private String  invalidMrnCode;
	private String  mrnReplacedBy;
	private Timestamp lastRevDatetime;
	private Timestamp lastVerifyDate;
	private Timestamp updateDatetime;
	private String  updateFlag;
	private String  suffix;
   
	
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public BigInteger getGuid() {
		return guid;
	}
	public void setGuid(BigInteger guid) {
		this.guid = guid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Timestamp getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Timestamp birthDate) {
		this.birthDate = birthDate;
	}
	public String getDeathFlag() {
		return deathFlag;
	}
	public void setDeathFlag(String deathFlag) {
		this.deathFlag = deathFlag;
	}
	public Timestamp getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(Timestamp deathDate) {
		this.deathDate = deathDate;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getBlindFlag() {
		return blindFlag;
	}
	public void setBlindFlag(String blindFlag) {
		this.blindFlag = blindFlag;
	}
	public String getDeafFlag() {
		return deafFlag;
	}
	public void setDeafFlag(String deafFlag) {
		this.deafFlag = deafFlag;
	}
	public String getMuteFlag() {
		return muteFlag;
	}
	public void setMuteFlag(String muteFlag) {
		this.muteFlag = muteFlag;
	}
	public String getKpmcpEmpFlag() {
		return kpmcpEmpFlag;
	}
	public void setKpmcpEmpFlag(String kpmcpEmpFlag) {
		this.kpmcpEmpFlag = kpmcpEmpFlag;
	}
	public String getIntrptrReqCode() {
		return intrptrReqCode;
	}
	public void setIntrptrReqCode(String intrptrReqCode) {
		this.intrptrReqCode = intrptrReqCode;
	}
	public String getInvalidMrnCode() {
		return invalidMrnCode;
	}
	public void setInvalidMrnCode(String invalidMrnCode) {
		this.invalidMrnCode = invalidMrnCode;
	}
	public String getMrnReplacedBy() {
		return mrnReplacedBy;
	}
	public void setMrnReplacedBy(String mrnReplacedBy) {
		this.mrnReplacedBy = mrnReplacedBy;
	}
	public Timestamp getLastRevDatetime() {
		return lastRevDatetime;
	}
	public void setLastRevDatetime(Timestamp lastRevDatetime) {
		this.lastRevDatetime = lastRevDatetime;
	}
	public Timestamp getLastVerifyDate() {
		return lastVerifyDate;
	}
	public void setLastVerifyDate(Timestamp lastVerifyDate) {
		this.lastVerifyDate = lastVerifyDate;
	}
	public Timestamp getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(Timestamp updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	public String getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
