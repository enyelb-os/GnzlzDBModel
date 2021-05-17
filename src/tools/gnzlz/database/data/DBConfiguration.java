package tools.gnzlz.database.data;

import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
import java.util.ArrayList;

public class DBConfiguration {
	
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
	
	protected SQLFile script;
	protected Driver driver;
	protected String modelPackage;
	protected String prefix;
	protected String path;
	protected String name;
	protected String user;
	protected String password;
	protected String properties;
	protected boolean refresh;
	private DBConnection connection;
	
	private StringBuilder url;
	
	/*****************
	 * 
	 *****************/
	
	protected StringBuilder url() {
		if(url == null) url = urlRefresh();
		return url;
	}
	
	protected StringBuilder urlRefresh() {
		url = new StringBuilder();
		if(prefix != null) url.append(prefix);
		if(path != null) url.append(path);
		if(name != null) url.append(name);
		if(properties != null) url.append("?").append(properties);
		return url;
	}
	
	/*****************
	 * properties
	 *****************/
	
	protected DBConfiguration properties(String properties) {
		this.properties = properties;
		return this;
	}
	
	/*****************
	 * script
	 *****************/
	
	protected DBConfiguration script(SQLFile script) {
		this.script = script;
		return this;
	}
	
	/*****************
	 * script
	 *****************/
	
	protected DBConfiguration driver(Driver driver) {
		this.driver = driver;
		return this;
	}
	
	/*****************
	 * prefix
	 *****************/
	
	protected DBConfiguration prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	/*****************
	 * path
	 *****************/
	
	protected DBConfiguration path(String path) {
		this.path = path;
		return this;
	}
	
	/*****************
	 * name
	 *****************/
	
	protected DBConfiguration name(String name) {
		this.name = name;
		return this;
	}
	
	/*****************
	 * user
	 *****************/
	
	protected void user(String user) {
		this.user = user;
	}
	
	/*****************
	 * password
	 *****************/
	
	protected DBConfiguration password(String password) {
		this.password = password;
		return this;
	}
	
	/*****************
	 * modelPackage
	 *****************/
	
	protected DBConfiguration modelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
		return this;
	}
	
	protected DBConfiguration internalPackage(String modelPackage) {
		return modelPackage(this.getClass().getPackage().getName()+"."+modelPackage);
	}
	
	public String modelPackage() {
		return modelPackage;
	}
	
	/*****************
	 * refresh
	 *****************/
	
	protected DBConfiguration refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}
	
	/*****************
	 * connection
	 *****************/
	
	public DBConnection connection() {
		if(connection == null) {
			connection = new DBConnection(this);
		}
		return connection;
	}
}
