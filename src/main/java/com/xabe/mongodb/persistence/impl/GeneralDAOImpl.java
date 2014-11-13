package com.xabe.mongodb.persistence.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.xabe.mongodb.client.ClientProvider;
import com.xabe.mongodb.persistence.GeneralDAO;
import com.xabe.mongodb.util.Constant;

public class GeneralDAOImpl implements GeneralDAO{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralDAOImpl.class);
	private ClientProvider provider =   ClientProvider.getInstance();
	
	
	public List<String> showDatabases() {
		LOGGER.info("Listado de bases de datos : ");
		List<String> databases = provider.getClient().getDatabaseNames();
		for (String basedato : databases) {
			LOGGER.info(Constant.TAB + Constant.SPACE + basedato);
		}
		LOGGER.info("Fin del listado de las bases de datos : ");
		return databases;
	}

	public Set<String> showColectionsDataBase(String nameDatabase) {
		DB database = provider.getClient().getDB(nameDatabase);
		Set<String> colecciones = database.getCollectionNames();
		LOGGER.info("Listado de colecciones de la base de datos : "+ database.getName());
		for (String coleccion : colecciones) {
			LOGGER.info(Constant.TAB + Constant.TAB + Constant.SPACE + coleccion);
		}
		LOGGER.info("Fin de listado de colecciones de la base de datos : "+ database.getName());
		return colecciones;
	}
	
	public void showDocumentsColection(String nameDatabase, String nameColection) {
		DB database = provider.getClient().getDB(nameDatabase);
		DBCollection colection = database.getCollection(nameColection);
		DBCursor cursor = colection.find();
		try 
		{
			while (cursor.hasNext()) {
				DBObject obj = (DBObject) cursor.next();
				LOGGER.info(Constant.TAB + Constant.TAB + Constant.TAB + obj.toString());
			}

		} catch(Exception e){
			LOGGER.error("Error en el cursor de la colleccion "+nameColection+" : " + e.getMessage(),e);
		}
		finally {
			cursor.close();
		}
	}
}
