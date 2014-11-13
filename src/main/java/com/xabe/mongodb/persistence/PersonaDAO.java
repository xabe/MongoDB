package com.xabe.mongodb.persistence;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.xabe.mongodb.modelo.Persona;


public interface PersonaDAO {
	
	DB getDatabase();
	
	DBCollection getColecction();
	
	ObjectId create(Persona persona) throws IOException;
	
	Persona searchId(ObjectId id) throws IOException;
	
	void deleteId(ObjectId id);
	
	void update(ObjectId id, Persona persona) throws IOException;
	
	List<Persona> searchFiled(String filed, String value) throws IOException;

}
