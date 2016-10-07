package com.kp.rabbitmq.util;

public class FieldDetail

{
	String sqlField;
	String dataType;
	FieldDetail(String sqlField, String dataType){
		this.sqlField=sqlField;
		this.dataType=dataType;
	}
	public String getSqlField() {
		return sqlField;
	}
	public void setSqlField(String sqlField) {
		this.sqlField = sqlField;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
