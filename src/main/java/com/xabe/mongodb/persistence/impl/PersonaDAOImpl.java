package com.xabe.mongodb.persistence.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.xabe.mongodb.client.ClientProvider;
import com.xabe.mongodb.modelo.Persona;
import com.xabe.mongodb.persistence.PersonaDAO;
import com.xabe.mongodb.util.Constant;
import com.xabe.mongodb.util.JsonUtils;


public class PersonaDAOImpl implements PersonaDAO{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonaDAOImpl.class);

	private ClientProvider provider =   ClientProvider.getInstance();
	
	public DB getDatabase() {
		return provider.getClient().getDB(Constant.DATABASE_PERSONA);
	}
	
	public DBCollection getColecction() {
		return getDatabase().getCollection(Constant.COLLECTION_PERSONA);
	}
	
	public ObjectId create(Persona persona) throws IOException {
		DBObject dbObject = JsonUtils.getDbObject(persona);
		getColecction().insert(dbObject);
		return (ObjectId) dbObject.get(Constant.ID);
	}
	
	public Persona searchId(ObjectId id) throws IOException {
		return JsonUtils.getPojo(getColecction().findOne(new BasicDBObject(Constant.ID, id)),Persona.class);
	}
	
	public void deleteId(ObjectId id) {
		getColecction().remove(new BasicDBObject(Constant.ID, id));
	}
	
	public void update(ObjectId id, Persona persona) throws IOException {		
		getColecction().update(new BasicDBObject(Constant.ID, id), JsonUtils.getDbObject(persona));
	}
	
	public List<Persona> searchFiled(String filed, String value) throws IOException {
		DBCursor cursor = getColecction().find(new BasicDBObject(filed, Pattern.compile(value, Pattern.CASE_INSENSITIVE)));
		List<Persona> personas = new ArrayList<Persona>();
		try 
		{
			while (cursor.hasNext()) {
				DBObject obj = (DBObject) cursor.next();
				personas.add(JsonUtils.getPojo(obj, Persona.class));
			}

		} catch(Exception e){
			LOGGER.error("Error en el cursor de la colleccion "+Constant.COLLECTION_PERSONA+" : " + e.getMessage(),e);
		}
		finally {
			cursor.close();
		}
		return  personas;
	}
}
