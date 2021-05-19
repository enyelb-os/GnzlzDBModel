package tools.gnzlz.database.data;

import tools.gnzlz.database.properties.DBPropertiesConnection;
import tools.gnzlz.database.properties.DBPropertiesMigration;
import tools.gnzlz.database.properties.DBPropertiesModel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class DBConfiguration {
	
	/*****************
	 * Static
	 *****************/
	
	private static ArrayList<DBConfiguration> configurations;
	
	private static ArrayList<DBConfiguration> configurations() {
		if(configurations == null) configurations = new ArrayList<DBConfiguration>();
		return configurations;
	}
	
	/***********************
	 * Create
	 ***********************/
	
	public static <T extends DBConfiguration> T create(Class<T> c) {
		try {
			return c.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Class Error: ");
	}
	
	public static <T extends DBConfiguration> DBConfiguration configuration(Class<T> c) {
		for (DBConfiguration dbConfiguration : configurations()) {
			if(dbConfiguration.getClass().getName().equals(c.getName()))
				return dbConfiguration;
		}	
		T configuration = create(c);
		configurations().add(configuration);
		return configuration;
	}

	/*****************
	 * Constructor
	 *****************/

	private DBConnection connection;
	private DBPropertiesModel model;
	private DBPropertiesMigration migration;

	public DBConfiguration(){
		initModel(new DBPropertiesModel());
	}

	/**************************
	 * DBProperties Abstract
	 **************************/

	protected abstract void initConnection(DBPropertiesConnection connection);

	protected abstract void initModel(DBPropertiesModel model);

	protected abstract void initMigration(DBPropertiesMigration model);
	
	/*****************
	 * connection
	 *****************/
	
	public DBConnection connection() {
		if(connection == null) {
			DBPropertiesConnection propertiesConnection = new DBPropertiesConnection();
			initConnection(propertiesConnection);
			connection = new DBConnection(propertiesConnection);
		}
		return connection;
	}

	/*******************
	 * ModelProperties
	 *******************/

	public DBPropertiesModel model() {
		if(model == null) {
			model = new DBPropertiesModel();
			initModel(model);
		}
		return model;
	}

	/**********************
	 * MigrationProperties
	 **********************/

	public DBPropertiesMigration migration() {
		if(migration == null) {
			migration = new DBPropertiesMigration();
			initMigration(migration);
		}
		return migration;
	}
}
