package tools.gnzlz.database.query.migration.builder.data;

public class GCreateDB {
	
	private String database;
	
	/***************************
	 * database
	 ***************************/
	
	public String database() {
		return database;
	}
	
	public void database(String database) {
		if(database != null && !database.isEmpty()) 
			this.database = database;
	}
}
