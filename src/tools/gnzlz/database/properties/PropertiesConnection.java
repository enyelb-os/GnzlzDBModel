package tools.gnzlz.database.properties;

import tools.gnzlz.database.model.SQLFile;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class PropertiesConnection {

	protected SQLFile script;
	protected Driver driver;
	protected String prefix;
	protected String path;
	protected String name;
	protected String user;
	protected String password;
	protected Hashtable<String,String> properties;
	protected StringBuilder url;
	protected DataSource dataSource;

	/*****************
	 * Url
	 *****************/

	public StringBuilder url() {
		if(url == null) url = urlRefresh();
		return url;
	}

	public StringBuilder urlRefresh() {
		url = new StringBuilder();
		if(prefix != null) url.append(prefix);
		if(path != null) url.append(path);
		if(name != null) url.append(name);
		if(properties != null) url.append(propertiesToString());
		return url;
	}

	/*****************
	 * properties
	 *****************/

	private Hashtable<String,String> properties(){
		if(properties == null) properties = new Hashtable<String, String>();
		return properties;
	}

	public PropertiesConnection property(String key, String value) {
		properties().put(key,value);
		return this;
	}

	public String propertiesToString() {

		if(properties != null) {
			StringBuilder string = new StringBuilder();
			int i = 0;
			for (Map.Entry<String, String> entry : properties().entrySet()) {
				string.append(i == 0 ? "?" : "&").append(entry.getKey()).append("=").append(entry.getValue());
				i++;
			}
			return string.toString();
		}
		return "";
	}

	/*****************
	 * drivar
	 *****************/

	public PropertiesConnection driver(Driver driver) {
		this.driver = driver;
		return this;
	}

	public Driver driver() {
		return driver;
	}

	/*****************
	 * script
	 *****************/

	public PropertiesConnection script(SQLFile script) {
		this.script = script;
		return this;
	}

	public SQLFile script() {
		return script;
	}

	/*****************
	 * prefix
	 *****************/

	public PropertiesConnection prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public String prefix() {
		return prefix;
	}

	/*****************
	 * path
	 *****************/

	public PropertiesConnection path(String path) {
		this.path = path;
		return this;
	}

	public String path() {
		return path;
	}

	/*****************
	 * name
	 *****************/

	public PropertiesConnection name(String name) {
		this.name = name;
		return this;
	}

	public String name() {
		return name;
	}

	/*****************
	 * user
	 *****************/

	public PropertiesConnection user(String user) {
		this.user = user;
		return this;
	}

	public String user() {
		return user;
	}

	/*****************
	 * password
	 *****************/

	public PropertiesConnection password(String password) {
		this.password = password;
		return this;
	}

	public String password() {
		return password;
	}

	/*****************
	 * DataSource
	 *****************/

	public PropertiesConnection dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	public DataSource dataSource() {
		return dataSource;
	}
}
