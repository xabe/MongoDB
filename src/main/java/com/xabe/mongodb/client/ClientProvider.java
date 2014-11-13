package com.xabe.mongodb.client;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public final class ClientProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientProvider.class);
	private static ClientProvider instance;
	private static Object lock = new Object();
	private MongoClient client;

	
	private ClientProvider() {
		try
		{
			createClient();
		}catch(UnknownHostException e){
			LOGGER.error("Error al crear la conexion a la base de datos, motivo: "+e.getMessage(),e);
		}
	}
	
	public static ClientProvider getInstance() {
		if (instance == null) 
		{
			synchronized (lock) 
			{
				if (null == instance) 
				{
					instance = new ClientProvider();					
				}
			}
		}
		return instance;
	}

	private void createClient() throws UnknownHostException {
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).readPreference(ReadPreference.secondaryPreferred()).build();
		client = new MongoClient(new ServerAddress("localhost", 27017), options);
		LOGGER.info("Conectado a la base de datos mongo");
	}

	public void closeClient() {
		client.close();
		LOGGER.info("Cerrado la conexion a la base de datos");
	}

	public MongoClient getClient() {
		return client;
	}
}
