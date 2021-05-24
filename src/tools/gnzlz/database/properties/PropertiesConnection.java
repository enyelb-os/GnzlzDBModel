package tools.gnzlz.database.properties;

import tools.gnzlz.database.model.SQLFile;
import tools.gnzlz.database.model.interfaces.Dialect;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Hashtable;

public class PropertiesConnection {

	protected Dialect dialect = Dialect.MySQL;
	protected SQLFile script;
	protected Driver driver;
	protected String prefix;
	protected String path;
	protected String dbname;
	protected String user;
	protected String password;
	protected Hashtable<String,String> properties;
	protected DataSource dataSource;

	/*****************
	 * dialect
	 *****************/

	public PropertiesConnection dialect(Dialect dialect) {
		this.dialect = dialect;
		return this;
	}

	/*****************
	 * properties
	 *****************/

	Hashtable<String,String> properties(){
		if(properties == null) properties = new Hashtable<String, String>();
		return properties;
	}

	public PropertiesConnection property(String key, String value) {
		properties().put(key,value);
		return this;
	}

	/*****************
	 * drivar
	 *****************/

	public PropertiesConnection driver(Driver driver) {
		this.driver = driver;
		return this;
	}

	/*****************
	 * script
	 *****************/

	public PropertiesConnection script(SQLFile script) {
		this.script = script;
		return this;
	}

	/*****************
	 * prefix
	 *****************/

	public PropertiesConnection prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	/*****************
	 * path
	 *****************/

	public PropertiesConnection path(String path) {
		this.path = path;
		return this;
	}

	/*****************
	 * name
	 *****************/

	public PropertiesConnection dbname(String name) {
		this.dbname = name;
		return this;
	}

	/*****************
	 * user
	 *****************/

	public PropertiesConnection user(String user) {
		this.user = user;
		return this;
	}

	/*****************
	 * password
	 *****************/

	public PropertiesConnection password(String password) {
		this.password = password;
		return this;
	}

	/*****************
	 * DataSource
	 *****************/

	public PropertiesConnection dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}
}
