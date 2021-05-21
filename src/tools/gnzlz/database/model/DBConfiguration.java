package tools.gnzlz.database.model;

import tools.gnzlz.database.migration.interfaces.ITypes;
import tools.gnzlz.database.properties.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class DBConfiguration implements ITypes {
	
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
	
	private static <T extends DBConfiguration> T create(Class<T> c) {
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
	private PTModel model;
	private PTMigration migration;

	public DBConfiguration(){
	}

	/**************************
	 * DBProperties Abstract
	 **************************/

	protected abstract void initConnection(PropertiesConnection connection);

	protected abstract void initModel(PropertiesModel model);

	protected abstract void initMigration(PropertiesMigration migration);
	
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

	public PTModel model() {
		if(model == null) {
			PropertiesModel propertiesModel = new PropertiesModel();
			model = new PTModel(propertiesModel);
			initModel(propertiesModel);
		}
		return model;
	}

	/**********************
	 * MigrationProperties
	 **********************/

	public PTMigration migration() {
		if(migration == null) {
			PropertiesMigration propertiesMigration = new PropertiesMigration();
			migration = new PTMigration(propertiesMigration);
			initMigration(propertiesMigration);
		}
		return migration;
	}

	@Override
	public String toString() {
		if(migration().migrations()!=null)
			migration().migrations().forEach(m -> {
				System.out.println("migration -> " + m.tableName());
				m.table().columns().forEach(c->{
					System.out.println("\t" + c.toString());
				});
			});
		return "";
	}
}
