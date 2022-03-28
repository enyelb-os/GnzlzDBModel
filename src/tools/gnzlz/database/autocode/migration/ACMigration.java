package tools.gnzlz.database.autocode.migration;

import tools.gnzlz.database.autocode.model.ACFileAttributeModel;
import tools.gnzlz.database.autocode.model.ACFileBaseModel;
import tools.gnzlz.database.autocode.model.ACFileCustomModel;
import tools.gnzlz.database.autocode.model.ACFileModel;
import tools.gnzlz.database.model.DBConfiguration;
import tools.gnzlz.database.model.DBConnection;
import tools.gnzlz.database.model.DBMigration;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;

public class ACMigration {

	DBConfiguration configuration;
	ArrayList<DBMigration> migrations;
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
		new ACMigration(connection,connection.tables());
	}

	public static void autocode(DBConnection connection, String table) {
		ArrayList<DBModel<?>> dbtables = new ArrayList<DBModel<?>>();
		connection.tables().stream().forEach(e ->{
			if (e.get("TABLE_NAME").stringValue().equalsIgnoreCase(table))
				dbtables.add(e);
		});

		new ACMigration(connection,dbtables);
	}

	public static void autocode(DBConnection connection, ArrayList<String> tables) {
		ArrayList<DBModel<?>> dbtables = new ArrayList<DBModel<?>>();
		connection.tables().stream().forEach(e ->{
			tables.stream().forEach(table->{
				if (e.get("TABLE_NAME").stringValue().equalsIgnoreCase(table))
					dbtables.add(e);
			});
		});

		new ACMigration(connection,dbtables);
	}
	
	<T extends DBConfiguration> ACMigration(DBConnection connection, ArrayList<DBModel<?>> tables) {

		if(tables != null && tables.size() > 0) {
			configuration = connection.configuration();
			migrations = configuration.migration().migrations();
			tables.stream().forEach(e -> {
				if(!isMigrationExists(e.get("TABLE_NAME").stringValue())){
					String packageName = e.get("PACKAGE_NAME") == null ? "" : "." + e.get("PACKAGE_NAME").stringValue();
					ACTable table = table(e.get("TABLE_NAME").stringValue(), packageName);
					table.addColumns(connection.columns(table.table()));
					table.addPrimaryKeys(connection.primaryKeys(table.table()));
					table.addHasOne(connection.importedKeys(table.table()));
				}
			});

			ACFileMigration.createFile(this);
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

	/***********************
	 * is Exists Migration
	 ***********************/

	private boolean isMigrationExists(String table) {
		for (DBMigration migration : migrations) {
			if(migration.tableName().equals(table))
				return true;
		}
		return false;
	}

	/***********************
	 * migrationClass
	 ***********************/

	String migrationClass(String table) {
		for (DBMigration migration : migrations) {
			if(migration.tableName().equals(table))
				return migration.getClass().getSimpleName();
		}
		return "";
	}

	/***********************
	 * migrationPackage
	 ***********************/

	String migrationPackage(String table) {
		for (DBMigration migration : migrations) {
			if(migration.tableName().equals(table))
				return migration.getClass().getPackage().getName().concat(".").concat(migration.getClass().getSimpleName());
		}
		return "";
	}


}
