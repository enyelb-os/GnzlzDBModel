package tools.gnzlz.database.properties;

import tools.gnzlz.database.model.SQLFile;
import tools.gnzlz.database.model.interfaces.Dialect;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Hashtable;
import java.util.Map;

public class PTConnection {

	protected PropertiesConnection connection;
	protected StringBuilder url;

	public PTConnection(PropertiesConnection propertiesConnection){
		this.connection = propertiesConnection;
	}

	/*****************
	 * Url
	 *****************/

	public StringBuilder urlDB() {
		if(url == null) url = urlDBRefresh();
		return url;
	}

	public StringBuilder urlDBRefresh() {
		url = new StringBuilder();
		if(connection.protocol != null) url.append(connection.protocol).append("//");
		if(connection.host != null) url.append(connection.host).append(":");
		if(connection.port != null) url.append(connection.port).append("/");
		if(connection.database != null) url.append(connection.database);
		if(connection.properties != null) url.append(propertiesToString());
		return url;
	}

	/*****************
	 * UrlHost
	 *****************/

	public String urlHost() {
		StringBuilder url = new StringBuilder();
		if(connection.protocol != null) url.append(connection.protocol).append("//");
		if(connection.host != null) url.append(connection.host).append(":");
		if(connection.port != null) url.append(connection.port).append("/");
		if(connection.properties != null) url.append(propertiesToString());
		return url.toString();
	}

	/*****************
	 * dialect
	 *****************/

	public Dialect dialect(){
		return connection.dialect;
	}

	/*****************
	 * properties
	 *****************/

	public Hashtable<String,String> properties(){
		return connection.properties();
	}

	public String propertiesToString() {
		if(connection.properties != null) {
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

	public Driver driver() {
		return  connection.driver;
	}

	/*****************
	 * script
	 *****************/

	public SQLFile script() {
		return  connection.script;
	}

	/*****************
	 * prefix
	 *****************/

	public String protocol() {
		return  connection.protocol;
	}

	/*****************
	 * path
	 *****************/

	public String path() {
		return connection.path;
	}

	/*****************
	 * host
	 *****************/

	public String host() {
		return connection.host;
	}

	/*****************
	 * port
	 *****************/

	public String port() {
		return connection.port;
	}

	/*****************
	 * name
	 *****************/

	public String database() {
		return connection.database;
	}

	/*****************
	 * user
	 *****************/

	public String user() {
		return connection.user;
	}

	/*****************
	 * password
	 *****************/

	public String password() {
		return connection.password;
	}

	/*****************
	 * DataSource
	 *****************/

	public DataSource dataSource() {
		return connection.dataSource;
	}
}
