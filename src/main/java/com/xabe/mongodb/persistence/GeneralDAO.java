package com.xabe.mongodb.persistence;

import java.util.List;
import java.util.Set;


public interface GeneralDAO {
	
	List<String> showDatabases();
	
	Set<String> showColectionsDataBase(String nameDatabase);
	
	void showDocumentsColection(String nameDatabase, String nameColection);

}
