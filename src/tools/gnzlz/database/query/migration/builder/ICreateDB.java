package tools.gnzlz.database.query.migration.builder;

import tools.gnzlz.database.query.migration.builder.data.GCreateDB;

public interface ICreateDB<Type extends Query<?>> {
	
	Type type();
	
	GCreateDB gCreateDB();
	
	/***************************
	 * database
	 ***************************/
	
	public default Type database(String database){
		gCreateDB().database(database);
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateCreateDB(){
		StringBuilder str = new StringBuilder("CREATE DATABASE");
		if(gCreateDB().database() == null || gCreateDB().database().isEmpty())
			throw new RuntimeException("CreateDb error invalid database");
		else
			str.append(" ").append(gCreateDB().database());
		
		return str;
	}
}
