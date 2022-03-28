package tools.gnzlz.database.autocode.model;

import java.util.ArrayList;

import tools.gnzlz.database.model.DBConfiguration;
import tools.gnzlz.database.model.DBConnection;
import tools.gnzlz.database.model.DBMigration;
import tools.gnzlz.database.model.DBModel;

public class ACDataBase {

	DBConfiguration configuration;

	ArrayList<ACTable> tables;
	
	public static <T extends DBConfiguration> void autocode(Class<T> c) {
		autocode(DBConfiguration.configuration(c).connection());
	}

	public static <T extends DBConfiguration> void autocode(Class<T> c, String table) {
		autocode(DBConfiguration.configuration(c).connection(),table);
	}

	public static <T extends DBConfiguration> void autocode(Class<T> c, ArrayList<String> tables) {
		autocode(DBConfiguration.configuration(c).connection(),tables);
	}

	public static void autocode(DBConnection connection) {
		new ACDataBase(connection,connection.tables());
	}

	public static void autocode(DBConnection connection, String table) {
		ArrayList<DBModel<?>> dbtables = new ArrayList<DBModel<?>>();
		connection.tables().stream().forEach(e ->{
			if (e.get("TABLE_NAME").stringValue().equalsIgnoreCase(table))
				dbtables.add(e);
		});

		new ACDataBase(connection,dbtables);
	}

	public static void autocode(DBConnection connection, ArrayList<String> tables) {
		ArrayList<DBModel<?>> dbtables = new ArrayList<DBModel<?>>();
		connection.tables().stream().forEach(e ->{
			tables.stream().forEach(table->{
				if (e.get("TABLE_NAME").stringValue().equalsIgnoreCase(table))
					dbtables.add(e);
			});
		});

		new ACDataBase(connection,dbtables);
	}

	public static void autocodeMigrations(DBConnection connection, ArrayList<DBMigration> tables) {
		ArrayList<DBModel<?>> dbtables = new ArrayList<DBModel<?>>();
		connection.tables().stream().forEach(e ->{
			tables.stream().forEach(table->{
				if (e.get("TABLE_NAME").stringValue().equalsIgnoreCase(table.tableName())){
					DBModel<?> model = DBModel.create(DBModel.class);
					model.set("TABLE_NAME", table.tableName());
					model.set("PACKAGE_NAME", table.packageName());
					dbtables.add(model);
				}
			});
		});

		new ACDataBase(connection,dbtables);
	}
	
	<T extends DBConfiguration> ACDataBase(DBConnection connection, ArrayList<DBModel<?>> tables) {
		if(tables != null && tables.size() > 0) {
			configuration = connection.configuration();
			tables.stream().forEach(e -> {
				String packageName = e.get("PACKAGE_NAME") == null ? "" : "." + e.get("PACKAGE_NAME").stringValue();
				ACTable table = table(e.get("TABLE_NAME").stringValue(), packageName);
				table.addColumns(connection.columns(table.table()));
				table.addPrimaryKeys(connection.primaryKeys(table.table()));
				table.addHasOne(connection.importedKeys(table.table()));
				table.addHasMany(connection.exportedKeys(table.table()));
				table.addBelongsToMany(connection.manyToManyKeys(table.table()));
			});

			ACFileBaseModel.createFile(this);
			ACFileCustomModel.createFile(this);
			ACFileModel.createFile(this);
			ACFileAttributeModel.createFile(this);
		}
	}
	
	ACTable table(String table, String packageName) {
		for (ACTable acTable : tables()) {
			if(acTable.table().equals(table))
				return acTable;
		}
		ACTable acTable = new ACTable(table,packageName, this);
		tables().add(acTable);
		return acTable;
	}

	String packageName(String table) {
		for (ACTable acTable : tables()) {
			if(acTable.table().equals(table))
				return acTable.packegeName();
		}
		return "";
	}

	ACTable table(String table) {
		for (ACTable acTable : tables()) {
			if(acTable.table().equals(table))
				return acTable;
		}
		return null;
	}
	
	/***********************
	 * Tables
	 ***********************/
	
	ArrayList<ACTable> tables() {
		if(tables == null) tables = new ArrayList<ACTable>(); 
		return tables;
	}
}
