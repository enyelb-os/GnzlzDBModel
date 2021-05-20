package tools.gnzlz.database.model;

import tools.gnzlz.database.properties.PropertiesConnection;
import tools.gnzlz.database.properties.PropertiesMigration;
import tools.gnzlz.database.properties.PropertiesModel;

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
	private PropertiesModel model;
	private PropertiesMigration migration;

	public DBConfiguration(){}

	/**************************
	 * DBProperties Abstract
	 **************************/

	protected abstract void initConnection(PropertiesConnection connection);

	protected abstract void initModel(PropertiesModel model);

	protected abstract void initMigration(PropertiesMigration model);
	
	/*****************
	 * connection
	 *****************/
	
	public DBConnection connection() {
		if(connection == null) {
			PropertiesConnection propertiesConnection = new PropertiesConnection();
			initConnection(propertiesConnection);
			connection = new DBConnection(propertiesConnection);
		}
		return connection;
	}

	/*******************
	 * ModelProperties
	 *******************/

	public PropertiesModel model() {
		if(model == null) {
			model = new PropertiesModel();
			initModel(model);
		}
		return model;
	}

	/**********************
	 * MigrationProperties
	 **********************/

	public PropertiesMigration migration() {
		if(migration == null) {
			migration = new PropertiesMigration();
			initMigration(migration);
		}
		return migration;
	}
}
