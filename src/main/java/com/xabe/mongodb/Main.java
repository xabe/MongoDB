package com.xabe.mongodb;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xabe.mongodb.client.ClientProvider;
import com.xabe.mongodb.modelo.Persona;
import com.xabe.mongodb.persistence.GeneralDAO;
import com.xabe.mongodb.persistence.PersonaDAO;
import com.xabe.mongodb.persistence.impl.GeneralDAOImpl;
import com.xabe.mongodb.persistence.impl.PersonaDAOImpl;
import com.xabe.mongodb.util.Constant;

public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void showMongoDB(){
		GeneralDAO generalDAO = new GeneralDAOImpl();
		List<String> databases = generalDAO.showDatabases();
		
		Set<String> collections;
		for(String database : databases){
			collections = generalDAO.showColectionsDataBase(database);
			for(String collection: collections){
				generalDAO.showDocumentsColection(database, collection);
			}
		}
	}
	
	public static void main(String[] args) {

		PersonaDAO personaDAO = new PersonaDAOImpl();
		try
		{			
			
			//Creamos la base datos personas, la coleccion de persona y insertamos un documento persona
			Persona persona = new Persona();
			persona.setName("chabir");
			persona.setSurname("Atrahouch");
			persona.setTelephone(9999999);
			persona.setJob("programdor");
			
			
			ObjectId id = personaDAO.create(persona);
			LOGGER.info("Creada la persona : " + personaDAO.searchId(id).toString());
			
			persona.setJob("Javero");
			personaDAO.update(id, persona);
			
			LOGGER.info("Actualizada la persona : " +personaDAO.searchId(id).toString());
			
			persona.setName("Pepe");
			persona.setSurname("Perez");
			ObjectId id2 = personaDAO.create(persona);
			LOGGER.info("Creada la persona : " + personaDAO.searchId(id2).toString());
			
			List<Persona> personas = personaDAO.searchFiled(Constant.FIELD_JOB, "javero");
			
			LOGGER.info("Numero de persona que son javeras : " +personas.size());
			for(Persona persona2 : personas){
				LOGGER.info(persona2.toString());
			}
			
			personaDAO.deleteId(id);
			personaDAO.deleteId(id2);
			LOGGER.info("Borrada la persona ");
			
			
			showMongoDB();

			
		}catch(Exception e){
			LOGGER.error("Error : "+e.getMessage());
		}
		ClientProvider.getInstance().closeClient();
	}

}
