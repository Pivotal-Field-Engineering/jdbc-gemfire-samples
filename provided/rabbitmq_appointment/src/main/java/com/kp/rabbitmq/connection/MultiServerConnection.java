package com.kp.rabbitmq.connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class MultiServerConnection {
    //private static final String EXCHANGE = "TPMG.TTG.CHL7.EXCHANGE.APPT.QA";
	private static final Logger log = LoggerFactory.getLogger(MultiServerConnection.class);
	
	private static final String EXCHANGE_TYPE = "headers";
    private static final String HEADER_ROUTING_KEY = "queueId" ;
    @SuppressWarnings("deprecation")
    public static boolean publishMessage(String message, String exchange, String routingKey) {
        log.debug("publish message...IBM MQ ="+routingKey);
        Channel channel = null;
        Connection connection = null;
        try {
            if (StringUtils.isBlank(routingKey)) {
                log.error("Routing key is NULL/Empty");
                return false;
            }
            
            if (StringUtils.isBlank(message)) {
                log.error("HL7 Message is NULL/Empty for Routing key = " + routingKey);
                return false;
            }
            connection = RabbitMqConnectionManager.getInstance().getConnection();
            channel = createChannel(connection, exchange,routingKey);
            if (channel != null) {
                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put(HEADER_ROUTING_KEY, routingKey);

                BasicProperties pros = new AMQP.BasicProperties();
                pros.setDeliveryMode(2);  //make message persistent
                pros.setHeaders(headers); //routing key

                channel.basicPublish(exchange, "", pros, message.getBytes());
                // channel.basicPublish(EXCHANGE, routingKey,null,message.getBytes());
                log.info("Routing Key=" + routingKey + " HL7 Message successfully sent to MQ.");
                return true;
            } else {
                // log it and throw exception ??
                log.error("Rabbit MQ channel is NULL.");
            }
        } catch (Exception e) {
            log.error("Routing Key=" + routingKey + " error while publishing message to Rabbit MQ:", e);
        } finally {
            closeChannel(channel);
        }
        return false;
    }

    private static Channel createChannel(Connection connection, String exchange,String routingKey) throws Exception {
        Channel channel = null;
        try{
            channel = connection.createChannel();
        }catch(com.rabbitmq.client.AlreadyClosedException e){
            log.error(e.getMessage() +" recreating connections.");
            RabbitMqConnectionManager.getInstance().initialize();
            connection = RabbitMqConnectionManager.getInstance().getConnection();
        }
        
        log.info("channel created and initialized....");
        // channel.exchangeDeclare(EXCHANGE, "direct", true);
        channel.exchangeDeclare(exchange, EXCHANGE_TYPE, true);
        return channel;
        
    }

    private static void closeChannel(Channel channel) {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
                log.info("channel closed.");
            }
        } catch (Exception e) {
            log.error("error closing channel.");
        }
    }
    
   
 
}