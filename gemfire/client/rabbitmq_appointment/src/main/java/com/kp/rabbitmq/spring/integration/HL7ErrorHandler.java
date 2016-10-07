package com.kp.rabbitmq.spring.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.kp.rabbitmq.connection.MultiServerConnection;


@Component("HL7ErrorHandler")
public class HL7ErrorHandler {
	private static final Logger log = LoggerFactory.getLogger(HL7ErrorHandler.class);
	
	@Value("${queue1RoutingKey}") private String queue1RoutingKey;
	@Value("${reQueueRoutingKey}") private String reQueueRoutingKey;
	@Value("${exchange}") private String exchange;
	@Value("${reprocess_exchange}") private String reprocess_exchange;
	
	private static final String RECOVERABLE = "recoverable";
	
    public void  handleFailedMessage(Message<MessageHandlingException> message) {
    	try{
    		MultiServerConnection.publishMessage(new String((byte[])message.getPayload().getFailedMessage().getPayload(), "UTF-8"), reprocess_exchange, reQueueRoutingKey);
    	}catch(Exception ex){
    		log.error("Message is not recoverable :"+ex.getMessage());
    	}
        
    }
    
    public void sendtoRabitMQ(String message, String status){
    	if (status.equals(RECOVERABLE)){
    		log.debug("sending to :"+exchange+ ":"+ queue1RoutingKey);
    		MultiServerConnection.publishMessage(message, exchange, queue1RoutingKey);
    	}else{
    		log.debug("sending to :"+reprocess_exchange+ ":"+ reQueueRoutingKey);
    		MultiServerConnection.publishMessage(message, reprocess_exchange, reQueueRoutingKey);
    	}
    }

}
