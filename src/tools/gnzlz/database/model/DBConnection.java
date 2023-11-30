package tools.gnzlz.database.model;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.properties.PTConnection;
import tools.gnzlz.database.properties.PropertiesConnection;
import tools.gnzlz.database.query.migration.CreateDB;
import tools.gnzlz.database.query.migration.CreateTable;
import tools.gnzlz.system.ansi.Color;
import tools.gnzlz.system.io.SystemIO;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class DBConnection{

	/**
	 * properties
	 */
	private final PTConnection properties;

	/**
	 * configuration
	 */
	private final DBConfiguration configuration;

	/**
	 * connection
	 */
	private Connection connection;

	/**
	 * DBConnection
	 * @param properties properties
	 * @param configuration configuration
	 */
	public DBConnection(PropertiesConnection properties, DBConfiguration configuration){
		this.configuration = configuration;
		this.properties = new PTConnection(properties);
		createDataBaseIfNoExists();
		shutdown();
	}

	/**
	 * createDataBaseIfNoExists
	 */
	private void createDataBaseIfNoExists() {
		try {
			boolean loadScript = !dbIsFile();
			open();
			if (connection != null && loadScript) {
				connection.getMetaData(); // create database if file
			}
			boolean dbIsFile = dbIsFile();
			if (dbIsFile && loadScript){
				migrate(configuration.migration().migrations());
				executeScriptInitial();
			}else if( connection == null || (!dbIsFile && connection.getCatalog() == null)){
				openForce(properties.urlHost());
				if(connection != null) {
					query(CreateDB.create().database(properties.database())).execute();
					openForce();
					if(connection != null) {
						migrate(configuration.migration().migrations());
						executeScriptInitial();
					}
				}
			}
			close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	}

	/**
	 * shutdown
	 */
	private void shutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				close();
			}
		});
	}

	/**
	 * dbIsFile
	 */
	private boolean dbIsFile(){
		if (properties.path() != null) {
			return new File(properties.path()).exists();
		}
		return false;
	}

	/**
	 * executeScriptInitial
	 */
	private void executeScriptInitial() {
		try {
        	openForce();
        	if (properties.script() != null && properties.script().script() != null) {
				for (String sql : properties.script().script()) {
					if (!connection.prepareStatement(sql).execute()) {
						//System.out.println(sql);
					}
				}
			}
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
	}
	
	/**
	 * open
	 */
	public synchronized DBConnection open(){
		return open(properties.urlDB().toString());
	}

	/**
	 * open
	 * @param url url
	 */
	private synchronized DBConnection open(String url){
		try {
			if((connection == null || connection.isClosed())) {
				if(properties.user() != null) {
					connection = connection(url, properties.user(), Optional.ofNullable(properties.password()).orElse(""));
				} else {
					connection = connection(url);
				}
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return this;
	}

	/**
	 * openForce
	 */
	public synchronized DBConnection openForce(){
		return openForce(properties.urlDB().toString());
	}

	/**
	 * openForce
	 * @param url url
	 */
	public synchronized DBConnection openForce(String url){
		close();
		connection = null;
		return open(url);
	}

	/**
	 * connection
	 * @param url url
	 */
	private Connection connection(String url) throws SQLException {
		if (properties.dataSource() != null) {
			return properties.dataSource().getConnection();
		} else {
			return DriverManager.getConnection(url);
		}
	}

	/**
	 * connection
	 * @param url url
	 * @param user user
	 * @param password password
	 */
	private Connection connection(String url,String user, String password) throws SQLException {
		if(properties.dataSource() != null) {
			return properties.dataSource().getConnection(user, password);
		} else {
			return DriverManager.getConnection(url, user, password);
		}
	}


	/**
	 * migrate
	 * @param migration migration
	 */
	public synchronized void migrate(DBMigration migration){
		migrate(migration,tables(properties.database(), null));
	}

	/**
	 * migrate
	 * @param migrations migrations
	 */
	synchronized void migrate(ArrayList<DBMigration> migrations){
		if(migrations != null && !migrations.isEmpty()) {
			//ArrayList<DBMigration> newTables = new ArrayList<>();
			//ArrayList<DBModel<?>> tables = tables(properties.database(), null);
			migrations.forEach(m -> {
				//if (migrate(m, tables)) {
				//	newTables.add(m);
				//}
			});
			//repair
			//ACDataBase.autocodeMigrations(this,newTables);
		}
	}

	/**
	 * migrate
	 * @param migration migration
	 * @param dbModels dbModels
	 */
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
		} //else{
			//update
		//}

		return false;
	}

	/**
	 * debug
	 */
	public static boolean outMetaData = true;

	/**
	 * debug
	 */
	public static boolean outModel = true;

	/**
	 * debug
	 */
	public static boolean outMigration = true;

	/**
	 * catalogs
	 */
	public synchronized ArrayList<DBModel<?>> catalogs(){
		ArrayList<DBModel<?>> dbModels = new ArrayList<>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
            SystemIO.OUT.println(outMetaData,"searching catalogs : " + Color.GREEN.print(properties.database()));
			ResultSet r = metaData.getCatalogs();
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException | NullPointerException e) {
			//e.printStackTrace();
		}
		if (dbModels.isEmpty()) {
			dbModels.add(DBModel.create(DBModel.class).set(Definition.TABLE_CAT, properties.database().split("[.]")[0]));
		}
		return dbModels;
	}

	/**
	 * schemes
	 * @param catalog catalog
	 */
	public synchronized ArrayList<DBModel<?>> schemes(String catalog){
		ArrayList<DBModel<?>> dbModels = new ArrayList<>();
		String catalogName = catalog.isEmpty() ? null : catalog;
		try {
			open();
			DatabaseMetaData metaData = connection.getMetaData();
			SystemIO.OUT.println(outMetaData,"searching scheme in the catalog : " + Color.GREEN.print(catalog));
			ResultSet r = metaData.getSchemas(catalogName, null);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e ) {
			//e.printStackTrace();
		}

		if(dbModels.isEmpty()){
			dbModels.add(DBModel.create(DBModel.class).set(Definition.TABLE_SCHEM,""));
		}

		return dbModels;
	}

	/**
	 * tables
	 * @param catalog catalog
	 * @param scheme scheme
	 */
	public synchronized ArrayList<DBModel<?>> tables(String catalog, String scheme){
		ArrayList<DBModel<?>> dbModels = new ArrayList<>();
		String catalogName = catalog.isEmpty() ? null : catalog;
		String schemeName = scheme.isEmpty() ? null : scheme;
		try {
			open();
			DatabaseMetaData metaData = connection.getMetaData();
			SystemIO.OUT.println(outMetaData,"searching the tables in the database: " + Color.GREEN.print(catalog));
			ResultSet r = metaData.getTables(catalogName, schemeName, "%", new String[]{"TABLE"});
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return dbModels;
	}

	/**
	 * columns
	 * @param catalog catalog
	 * @param scheme scheme
	 * @param table table
	 */
	public synchronized ArrayList<DBModel<?>> columns(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<>();
		try {
			open();
			SystemIO.OUT.println(outMetaData,"database: " + Color.GREEN.print(catalog) + " | searching the columns in the tabla: " + Color.BLUE.print(table));
            DatabaseMetaData metaData = connection.getMetaData();
			ResultSet r = metaData.getColumns(catalog, scheme, table, null);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return dbModels;
	}

	/**
	 * primaryKeys
	 * @param catalog catalog
	 * @param scheme scheme
	 * @param table table
	 */
	public synchronized ArrayList<DBModel<?>> primaryKeys(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
			SystemIO.OUT.println(outMetaData,"database: " + Color.GREEN.print(catalog) + " | searching the primary key in the tabla: " + Color.BLUE.print(table));
			ResultSet r = metaData.getPrimaryKeys(catalog, scheme, table);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return dbModels;
	}

	/**
	 * exportedKeys
	 * @param catalog catalog
	 * @param scheme scheme
	 * @param table table
	 */
	public synchronized ArrayList<DBModel<?>> exportedKeys(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<>();
		try {
			open();
            DatabaseMetaData metaData = connection.getMetaData();
			SystemIO.OUT.println(outMetaData,"database: " + Color.GREEN.print(catalog) + " | searching the foreign keys in the tabla: " + Color.BLUE.print(table));
			ResultSet r = metaData.getExportedKeys(catalog, scheme, table);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return dbModels;
	}

	/**
	 * importedKeys
	 * @param catalog catalog
	 * @param scheme scheme
	 * @param table table
	 */
	public synchronized ArrayList<DBModel<?>> importedKeys(String catalog, String scheme, String table){
		ArrayList<DBModel<?>> dbModels = new ArrayList<>();
		try {
			open();
			SystemIO.OUT.println(outMetaData,"database: " + Color.GREEN.print(catalog) + " | searching the primary keys of other tables, for the table: " + Color.BLUE.print(table));
            DatabaseMetaData metaData = connection.getMetaData();
			ResultSet r = metaData.getImportedKeys(catalog, scheme, table);
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return dbModels;
	}

	/**
	 * query
	 * @param query query
	 */
	public DBExecuteQuery query(tools.gnzlz.database.query.model.builder.Query<?,?> query){
        try {
        	open();
			DBExecuteQuery dbQuery = new DBExecuteQuery(connection.prepareStatement(query.dialect(properties.dialect()).query(),Statement.RETURN_GENERATED_KEYS));
        	Object [] objects = query.objects();
        	for (int i = 0; i < objects.length; i++) {
        		dbQuery.value(i+1, objects[i]);
			}
			return dbQuery;
		} catch (SQLException e) {
			//e.printStackTrace();
		}
        return null;
	}

	/**
	 * query
	 * @param query query
	 */
	public DBExecuteQuery query(tools.gnzlz.database.query.migration.builder.Query<?> query){
		try {
			open();
            return new DBExecuteQuery(connection.prepareStatement(query.dialect(properties.dialect()).query(),Statement.RETURN_GENERATED_KEYS));
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return null;
	}

	/**
	 * query
	 * @param query query
	 */
	public DBExecuteQuery query(String query){
        try {
        	open();
        	return new DBExecuteQuery(connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS));
		} catch (SQLException e) {
			//e.printStackTrace();
		}
        return null;
	}

	/**
	 * close
	 */
	public synchronized void close(){
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	}

	/**
	 * configuration
	 */
	public DBConfiguration configuration() {
		return configuration;
	}

	/**
	 * properties
	 */
	public PTConnection properties() {
		return properties;
	}
}
