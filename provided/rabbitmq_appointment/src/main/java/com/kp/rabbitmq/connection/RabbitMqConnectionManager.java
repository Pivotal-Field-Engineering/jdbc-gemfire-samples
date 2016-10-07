package com.kp.rabbitmq.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqConnectionManager {
	// private static Map<String, Connection> connections = null;
	private Connection connection = null;
	private static Logger logger = Logger
			.getLogger(RabbitMqConnectionManager.class);
	private static RabbitMqConnectionManager rabbitMqConnectionManager = new RabbitMqConnectionManager();

	private RabbitMqConnectionManager() {

	}

	public static RabbitMqConnectionManager getInstance() {
		return rabbitMqConnectionManager;
	}

	public void initialize() {
		logger.info("RabbitMqConnectionManager -> initialize connections ....");
		try {
			// close all previous stalled connections first if exists.
			closeConnections();
			// connections = new WeakHashMap<String, Connection>();
			Address[] addrArr = new Address[] {
					new Address("TTGNAPR0VRHMQ01.ttgtpmg.net", 5672),
					new Address("TTGNAPR0VRHMQ02.ttgtpmg.net", 5672),
					new Address("TTGNAPR0VRHMQ03.ttgtpmg.net", 5672),
					new Address("TTGNAPR0VRHMQ04.ttgtpmg.net", 5672)};
			// if (connections.size() == 0) {
			/*
			 * List<String> qNames = new ArrayList<String>();
			 * qNames.add("WSMQRequestQueueJ");
			 */
			ConnectionFactory factory = createConnectionFactory();
			if (factory != null) {
				/*
				 * for (int i = 0; i < qNames.size(); i++) {
				 * connections.put(qNames.get(i),
				 * factory.newConnection(addrArr)); }
				 */
				connection = factory.newConnection(addrArr);
			}

			// }
		} catch (IOException e) {
			logger.error("Error while intializing Rabbit MQ connections", e);
		}
	}

	private ConnectionFactory createConnectionFactory() {
		logger.info("RabbitMqConnectionManager -> createConnectionFactory");

		ConnectionFactory factory = new ConnectionFactory();
		factory.setVirtualHost("/");
		factory.setUsername("mobile_user");
		factory.setPassword("Mobusr#@!");

		return factory;
	}

	public Connection getConnection() {
		if (connection == null) {
			initialize();
		}

		return connection;
	}

	@Override
	protected void finalize() throws Throwable {
		logger.info("RabbitMqConnectionManager -> finalize.....");
		closeConnections();
	}

	private void closeConnections() {

	}
}