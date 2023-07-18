package tools.gnzlz.database.model;

import tools.gnzlz.database.properties.PTConnection;
import tools.gnzlz.database.properties.PropertiesConnection;
import tools.gnzlz.database.query.migration.CreateDB;
import tools.gnzlz.database.query.migration.CreateTable;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DBConnection{

	private PTConnection properties;
	private DBConfiguration configuration;
	private Connection connection;

	/***********************
	 * constructor
	 ***********************/

	public DBConnection(PropertiesConnection properties, DBConfiguration configuration){
		this.configuration = configuration;
		this.properties = new PTConnection(properties);
		if(this.properties.driver() != null)
			try {
				DriverManager.registerDriver(this.properties.driver());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		createDataBaseifNoExists();
		shutdown();
	}
	
	/**********************
	 * Create db
	 **********************/
	
	private void createDataBaseifNoExists() {
		try {
			boolean loadScript = !dbIsFile();
			open();
			if (connection !=null) {
				connection.getMetaData(); // create database if file
			}
			boolean dbIsFile = dbIsFile();
			if(dbIsFile && loadScript){
				migrate(configuration.migration().migrations());
				executeScriptInitial();
			}else if( connection == null || (!dbIsFile && connection.getCatalog() == null)){
				createDB();
			}
			close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**********************
	 * Shutdown
	 **********************/

	private void shutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				close();
			}
		});
	}

	/**********************
	 * Create db
	 **********************/

	private void createDB(){
		openForce(properties.urlHost());
		if(connection != null) {
			query(CreateDB.create().database(properties.database())).execute();
			openForce();
			if(connection != null) {
				migrate(configuration.migration().migrations());
				executeScriptInitial();
			}
		}
		close();
	}

	/**********************
	 * Create db
	 **********************/

	private boolean dbIsFile(){
		String file = "";
		if(properties.path() != null) file = properties.path();
		if(properties.database() != null) file = file + properties.database();
		return new File(file).exists();
	}
	
	/**********************
	 * Script Initial
	 **********************/
	
	private void executeScriptInitial() {
		try {
        	openForce();
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
		return open(properties.urlDB().toString());
	}
	
	private synchronized DBConnection open(String url){
		try {
			if((connection == null || connection.isClosed())) {
				if(properties.user() !=null && properties.password() !=null)
					connection = connection(url,properties.user(), properties.password());
				else if(properties.user() != null)
					connection = connection(url,properties.user(), "");
				else
					connection = connection(url);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return this;
	}

	/**********************
	 * OpenForce
	 **********************/

	public synchronized DBConnection openForce(){
		return openForce(properties.urlDB().toString());
	}

	public synchronized DBConnection openForce(String url){
		close();
		connection = null;
		return open(url);
	}

	/**********************
	 * connection
	 **********************/

	private Connection connection(String url) throws SQLException {
		if(properties.dataSource() != null)
			return properties.dataSource().getConnection();
		else
			return DriverManager.getConnection(url);
	}

	private Connection connection(String url,String user, String password) throws SQLException {
		if(properties.dataSource() != null)
			return properties.dataSource().getConnection(user,password);
		else
			return DriverManager.getConnection(url,user,password);
	}

	/**********************
	 * migrate
	 **********************/

	public synchronized void migrate(DBMigration migration){
		migrate(migration,tables(properties.database(), null));
	}

	synchronized void migrate(ArrayList<DBMigration> migrations){
		if(migrations != null && !migrations.isEmpty()) {
			ArrayList<DBMigration> newTables = new ArrayList<DBMigration>();
			ArrayList<DBModel<?>> tables = tables(properties.database(), null);
			migrations.forEach(m -> {
				if(migrate(m, tables))
					newTables.add(m);
			});
			//repair
			//ACDataBase.autocodeMigrations(this,newTables);
		}
	}

	synchronized boolean migrate(DBMigration migration, ArrayList<DBModel<?>> dbModels){
		boolean exists = false;
		for (DBModel<?> table: dbModels) {
			if (table.get("TABLE_NAME").stringValue().equalsIgnoreCase(migration.tableName())){
				exists = true;
			}
		}

		if(!exists){
			CreateTable queryTable = CreateTable.create();
			queryTable.table(migration.tableName());
			migration.table().columns().forEach(c->{
				queryTable.column(c.column()).type(c.type().toString()).length(c.length()).isDefault(c.isDefault());
				if(c.isPrimaryKey()) queryTable.primaryKey();
				if(c.isAutoincrement()) queryTable.autoincrement();
				if(c.isNotNull()) queryTable.notNull();
				if(c.isUnique()) queryTable.unique();
				if(c.foreignKey() != null) queryTable.foreignKey(c.foreignKey().table(),c.foreignKey().column());
			});
			return query(queryTable).execute();
		}else{
			//update
		}

		return false;
	}
	/**********************
	 * debug
	 **********************/

	public static boolean debug = true;

	/**********************
	 * catalog
	 **********************/

	public synchronized ArrayList<DBModel<?>> catalogs(){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
            if(debug) System.out.println("searching catalogs : " + properties.database());
			ResultSet r = metaData.getCatalogs();
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
	 * schemes
	 **********************/

	public synchronized ArrayList<DBModel<?>> schemes(String catalog){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
			DatabaseMetaData metaData = connection.getMetaData();
			if(debug) System.out.println("searching scheme in the catalog : " + catalog);
			ResultSet r = metaData.getSchemas(catalog, null);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(dbModels.isEmpty()){
			dbModels.add(DBModel.create(DBModel.class).set("TABLE_SCHEM",""));
		}

		return dbModels;
	}

	/**********************
	 * table
	 **********************/

	public synchronized ArrayList<DBModel<?>> tables(String catalog, String scheme){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
			DatabaseMetaData metaData = connection.getMetaData();
			if(debug) System.out.println("searching the tables in the database: " + catalog);
			ResultSet r = metaData.getTables(catalog, scheme, "%", new String[]{"TABLE"});
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
	
	public synchronized ArrayList<DBModel<?>> columns(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
			if(debug) System.out.println("database: " + catalog + " | searching the columns in the tabla: " + table);
            DatabaseMetaData metaData = connection.getMetaData();
			ResultSet r = metaData.getColumns(catalog, scheme, table, null);
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
	
	public synchronized ArrayList<DBModel<?>> primaryKeys(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
            if(debug) System.out.println("database: " + catalog + " | searching the primary key in the tabla: " + table);
			ResultSet r = metaData.getPrimaryKeys(catalog, scheme, table);
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
	
	public synchronized ArrayList<DBModel<?>> exportedKeys(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
            if(debug) System.out.println("database: " + catalog + " | searching the foreign keys in the tabla: " + table);
			ResultSet r = metaData.getExportedKeys(catalog, scheme, table);
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

	public synchronized ArrayList<DBModel<?>> importedKeys(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			open();
			if(debug) System.out.println("database: " + catalog + " | searching the primary keys of other tables, for the table: " + table);
            DatabaseMetaData metaData = connection.getMetaData();
			ResultSet r = metaData.getImportedKeys(catalog, scheme, table);
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
	 * Query
	 **********************/
	
	public DBExecuteQuery query(tools.gnzlz.database.query.model.builder.Query<?,?> query){
        try {
        	open();
			DBExecuteQuery dbQuery = new DBExecuteQuery(connection.prepareStatement(query.dialect(properties.dialect()).query(),Statement.RETURN_GENERATED_KEYS));
        	Object [] objects = query.objects();
        	for (int i = 0; i < objects.length; i++)
        		dbQuery.value(i+1, objects[i]);
			return dbQuery;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
	}

	public DBExecuteQuery query(tools.gnzlz.database.query.migration.builder.Query<?> query){
		try {
			open();
			DBExecuteQuery dbQuery = new DBExecuteQuery(connection.prepareStatement(query.dialect(properties.dialect()).query(),Statement.RETURN_GENERATED_KEYS));
			return dbQuery;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**********************
	 * Query
	 **********************/
	
	public DBExecuteQuery query(String query){
        try {
        	open();
        	return new DBExecuteQuery(connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS));
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**********************
	 * Close
	 **********************/
	
	public synchronized void close(){
		try {
			if(connection != null && !connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**********************
	 * configuration
	 **********************/

	public DBConfiguration configuration() {
		return configuration;
	}

	/*******************
	 * ConnectionProperties
	 *******************/

	public PTConnection properties() {
		return properties;
	}
}
