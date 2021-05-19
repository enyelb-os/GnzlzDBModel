package tools.gnzlz.database.properties;

import tools.gnzlz.database.data.SQLFile;

import javax.sql.DataSource;
import java.sql.*;

public class DBPropertiesConnection {

	protected SQLFile script;
	protected Driver driver;
	protected String prefix;
	protected String path;
	protected String name;
	protected String user;
	protected String password;
	protected String properties;
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
		if(properties != null) url.append("?").append(properties);
		return url;
	}

	/*****************
	 * properties
	 *****************/

	public DBPropertiesConnection properties(String properties) {
		this.properties = properties;
		return this;
	}

	public String properties() {
		return properties;
	}

	/*****************
	 * drivar
	 *****************/

	public DBPropertiesConnection driver(Driver driver) {
		this.driver = driver;
		return this;
	}

	public Driver driver() {
		return driver;
	}

	/*****************
	 * script
	 *****************/

	public DBPropertiesConnection script(SQLFile script) {
		this.script = script;
		return this;
	}

	public SQLFile script() {
		return script;
	}

	/*****************
	 * prefix
	 *****************/

	public DBPropertiesConnection prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public String prefix() {
		return prefix;
	}

	/*****************
	 * path
	 *****************/

	public DBPropertiesConnection path(String path) {
		this.path = path;
		return this;
	}

	public String path() {
		return path;
	}

	/*****************
	 * name
	 *****************/

	public DBPropertiesConnection name(String name) {
		this.name = name;
		return this;
	}

	public String name() {
		return name;
	}

	/*****************
	 * user
	 *****************/

	public DBPropertiesConnection user(String user) {
		this.user = user;
		return this;
	}

	public String user() {
		return user;
	}

	/*****************
	 * password
	 *****************/

	public DBPropertiesConnection password(String password) {
		this.password = password;
		return this;
	}

	public String password() {
		return password;
	}

	/*****************
	 * DataSource
	 *****************/

	public DBPropertiesConnection dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	public DataSource dataSource() {
		return dataSource;
	}
}
