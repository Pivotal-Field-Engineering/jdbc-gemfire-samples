package com.kp.rabbitmq.spring.integration;

public class HL7Exception extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HL7Exception(String message){
		super ("Message=" + message);
		
	}

	public HL7Exception(int code, String message){
		super("ErrorCode=" + code + ", Message=" + message);
		
	}
	

}
