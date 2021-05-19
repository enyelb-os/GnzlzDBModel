package tools.gnzlz.database.data;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import tools.gnzlz.database.data.properties.DBPropertiesConnection;
import tools.gnzlz.database.query.builder.Query;

public class DBConnection{

	private DBPropertiesConnection properties;
	
	/**********************
	 * DataBaseConnection
	 **********************/

	private Connection connection;

	/***********************
	 * constructor
	 ***********************/

	public DBConnection(DBPropertiesConnection properties){
		this.properties = properties;
		if(this.properties.driver() != null)
			try {
				DriverManager.registerDriver(this.properties.driver());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		createDataBaseifFile();
	}
	
	/**********************
	 * Create db if is file
	 **********************/
	
	private void createDataBaseifFile() {
		String file = "";
		if(properties.path() != null) file = properties.path();
		if(properties.name() != null) file = file + properties.name();
		if(!new File(file).exists())
	        try {
	        	open();
	            connection.getMetaData();
	            executeScriptInitial();
	            close();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }
	}
	
	/**********************
	 * Script Initial
	 **********************/
	
	private void executeScriptInitial() {
		try {
        	open();
        	if(properties.script() != null && properties.script().script() != null)
            	for (String sql : properties.script().script())
            		if(!connection.prepareStatement(sql).execute()) System.out.println(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
	}
	
	/**********************
	 * Open
	 **********************/
	
	public synchronized DBConnection open(){
		try {
			if((connection == null || connection.isClosed())) {
				if(properties.user() !=null && properties.password() !=null)
					connection = connection(properties.user(), properties.password());
				else if(properties.user() != null)
					connection = connection(properties.user(), "");
				else
					connection = connection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	private Connection connection() throws SQLException {
		if(properties.dataSource() != null)
			return properties.dataSource().getConnection();
		else
			return DriverManager.getConnection(properties.url().toString());
	}

	private Connection connection(String user, String password) throws SQLException {
		if(properties.dataSource() != null)
			return properties.dataSource().getConnection(user,password);
		else
			return DriverManager.getConnection(properties.url().toString(),user,password);
	}
	
	/**********************
	 * table
	 **********************/
	
	private boolean debug = true;
	
	public synchronized ArrayList<DBModel<?>> tables(){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
            if(debug) System.out.println("searching the tables in the database: " + properties.name());
			ResultSet r = metaData.getTables(properties.name(), null, "%", new String[]{"TABLE"});
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbModels;
	}
	
	/**********************
	 * columns
	 **********************/
	
	public synchronized ArrayList<DBModel<?>> columns(String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
			if(debug) System.out.println("database: " + properties.name() + " | searching the columns in the tabla: " + table);
            DatabaseMetaData metaData = connection.getMetaData();
			ResultSet r = metaData.getColumns(properties.name(), null, table, null);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbModels;
	}
	
	/**********************
	 * primaryKeys
	 **********************/
	
	public synchronized ArrayList<DBModel<?>> primaryKeys(String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
            if(debug) System.out.println("database: " + properties.name() + " | searching the primary key in the tabla: " + table);
			ResultSet r = metaData.getPrimaryKeys(properties.name(), null, table);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbModels;
	}
	
	/**********************
	 * exportedKeys
	 **********************/
	
	public synchronized ArrayList<DBModel<?>> exportedKeys(String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
            if(debug) System.out.println("database: " + properties.name() + " | searching the foreign keys in the tabla: " + table);
			ResultSet r = metaData.getExportedKeys(properties.name(), null, table);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbModels;
	}

	/**********************
	 * importedKeys
	 **********************/
	
	public synchronized ArrayList<DBModel<?>> importedKeys(String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
			if(debug) System.out.println("database: " + properties.name() + " | searching the primary keys of other tables, for the table: " + table);
            DatabaseMetaData metaData = connection.getMetaData();
			ResultSet r = metaData.getImportedKeys(properties.name(), null, table);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbModels;
	}
	
	/**********************
	 * ManyToMany
	 **********************/
	
	public synchronized ArrayList<DBModel<?>> manyToManyKeys(String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		boolean nDebug = debug;
		debug = false;
		exportedKeys(table).stream().forEach((db)->{
			importedKeys(db.get("FKTABLE_NAME").stringValue()).stream().forEach((dbf)->{
				if(!dbf.get("PKTABLE_NAME").stringValue().equals(table)){
					if(nDebug) System.out.println("database: " + properties.name() + " | searching relationships many to many (" + table +", "+db.get("FKTABLE_NAME").object+", "+dbf.get("PKTABLE_NAME").object+")");
					DBModel dbModel = DBModel.create(DBModel.class);
					dbModel.set("PKCOLUMN_NAME", db.get("PKCOLUMN_NAME").object);
					dbModel.set("PKTABLE_NAME", db.get("PKTABLE_NAME").object);
					dbModel.set("ICOLUMN_NAME1", db.get("FKCOLUMN_NAME").object);
					dbModel.set("ITABLE_NAME", db.get("FKTABLE_NAME").object);
					dbModel.set("ICOLUMN_NAME2", dbf.get("FKCOLUMN_NAME").object);
					dbModel.set("FKTABLE_NAME", dbf.get("PKTABLE_NAME").object);
					dbModel.set("FKCOLUMN_NAME", dbf.get("PKCOLUMN_NAME").object);
					dbModels.add(dbModel);
				}
			});
		});
		debug = nDebug;
		return dbModels;
	}
	
	/**********************
	 * Query
	 **********************/
	
	public DBQuery query(Query<?> query){
        try {
        	open();
        	DBQuery dbQuery = new DBQuery(connection.prepareStatement(query.query(),Statement.RETURN_GENERATED_KEYS));
        	Object [] objects = query.objects();
        	for (int i = 0; i < objects.length; i++)
        		dbQuery.value(i+1, objects[i]);
			return dbQuery;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**********************
	 * Query
	 **********************/
	
	public DBQuery query(String query){
        try {
        	open();
        	return new DBQuery(connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS));
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**********************
	 * Close
	 **********************/
	
	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
