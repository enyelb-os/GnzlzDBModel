package tools.gnzlz.database.model;

import java.util.ArrayList;

import tools.gnzlz.database.query.builder.Query;

public class DBTable {
	
	private DBConfiguration configuration;
	
	private String table;
	
	private ArrayList<DBColumn> columns;
	
	/***********************
	 * Create
	 ***********************/
	
	public static DBTable create(String table) {
		return new DBTable(table);
	}
	
	public static DBTable create() {
		return new DBTable();
	}
	
	private DBTable(String table) {
		this.table = table;
	}
	
	private DBTable() {
	}
	
	/***********************
	 * initColumns
	 ***********************/
	
	public DBTable addColumns(String ... columns) {
		if(columns != null && columns.length > 0) {
			for (int i = 0; i < columns.length; i++) {
				DBColumn dbColumn = get(columns[i]);
				if(dbColumn == null) {
					columns().add(new DBColumn(columns[i]));
				}
			}
		}
		
		return this;
	}
	
	/***********************
	 * initConfiguration
	 ***********************/
	
	public <T extends DBConfiguration> DBTable addConfiguration(Class<T> c) {
		configuration = DBConfiguration.configuration(c);
		return this;
	}
	
	/***********************
	 * addHasOne - Foreignkey
	 ***********************/
	
	public <T extends DBModel<?>> DBTable addHasOne(String localKey, Class<T> c, String foreignKey) {
		if(localKey != null) {
			DBColumn dbColumn = get(localKey);
			if(dbColumn != null) {
				dbColumn.addHasOne(foreignKey,c);
			} else {
				dbColumn = new DBColumn(localKey);
				columns().add(dbColumn);
				dbColumn.addHasOne(foreignKey,c);
			}
		}
		return this;
	}
	
	/***********************
	 * addHasMany - Foreignkey
	 ***********************/
	
	public <T extends DBModel<?>> DBTable addHasMany(String localKey, Class<T> c, String foreignKey) {
		if(localKey != null) {
			DBColumn dbObject = get(localKey);
			if(dbObject != null) {
				dbObject.addHasMany(foreignKey,c);
			} else {
				dbObject = new DBColumn(localKey);
				columns().add(dbObject);
				dbObject.addHasMany(foreignKey,c);
			}
		}
		return this;
	}
	
	/***********************
	 * addHasMany - Foreignkey
	 ***********************/
	
	public <T,I extends DBModel<?>> DBTable addBelongsToMany(String localKey, String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		if(localKey != null) {
			DBColumn dbObject = get(localKey);
			if(dbObject != null) {
				dbObject.addBelongsToMany(internalKey1, relationInternal, internalKey2, relationForeign, foreignKey);
			} else {
				dbObject = new DBColumn(localKey);
				columns().add(dbObject);
				dbObject.addBelongsToMany(internalKey1, relationInternal, internalKey2, relationForeign, foreignKey);
			}
		}
		return this;
	}
	
	/***********************
	 * intPrimaryKey
	 ***********************/
	
	public DBTable addPrimaryKey(String primaryKey) {
		if(primaryKey != null && !primaryKey.isEmpty()) {
			DBColumn dbPrimaryKey = primaryKey();
			DBColumn dbColumn = get(primaryKey);
			if(dbPrimaryKey != null) {
				if(dbPrimaryKey != dbColumn) {
					dbPrimaryKey.primaryKey(false);
				}
			}
			
			if(dbColumn != null) {
				dbColumn.primaryKey(true);
			}else {
				dbColumn = new DBColumn(primaryKey);
				columns().add(0, dbColumn.primaryKey(true));
			}
		}
		return this;
	}
	
	/***********************
	 * PrimaryKey
	 ***********************/
	
	public DBColumn primaryKey() {
		for (DBColumn dbColumn : columns()) {
			if (dbColumn.isPrimaryKey()) {
				return dbColumn;
			}
		}
		return null;
	}
	
	/***********************
	 * initTable
	 ***********************/
	
	public DBTable addTable(String table) {
		this.table = table;
		return this;
	}
	
	/***********************
	 * DBconfiguration
	 ***********************/
	
	public DBConfiguration configuration() {
		return configuration;
	}
	
	public DBConnection connection() {
		return configuration.connection();
	}
	
	public DBQuery query(Query<?> query) {
		return configuration.connection().query(query);
	}
	
	public DBQuery query(String query) {
		return configuration.connection().query(query);
	}
	
	/***********************
	 * Table
	 ***********************/
	
	public DBTable table(String table) {
		this.table = table;
		return this;
	}
	
	public String table() {
		return table;
	}
	
	/***********************
	 * Columns
	 ***********************/
	
	public ArrayList<DBColumn> columns() {
		if(columns == null) columns = new ArrayList<DBColumn>(); 
		return columns;
	}
	
	/***********************
	 * Get
	 ***********************/
	
	public DBColumn get(String name) {
		if(name != null) {
			for (DBColumn dbColumn : columns()) {
				if (dbColumn.name().equals(name)) {
					return dbColumn;
				}
			}
		}
		return null;
	}
	
	public DBColumn get(int i) {
		return columns().get(i);
	}
}
