package tools.gnzlz.database.data;

import tools.gnzlz.database.data.properties.DBPropertiesConnection;
import tools.gnzlz.database.data.properties.DBPropertiesModel;

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

	public DBConfiguration(){
		intModel(new DBPropertiesModel());
	}

	/**************************
	 * DBProperties Abstract
	 **************************/

	protected abstract void intConnection(DBPropertiesConnection connection);

	protected abstract void intModel(DBPropertiesModel model);
	
	/*****************
	 * connection
	 *****************/
	
	public DBConnection connection() {
		if(connection == null) {
			DBPropertiesConnection propertiesConnection = new DBPropertiesConnection();
			intConnection(propertiesConnection);
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
			intModel(model);
		}
		return model;
	}
}
