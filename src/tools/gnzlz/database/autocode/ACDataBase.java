package tools.gnzlz.database.autocode;

import java.util.ArrayList;

import tools.gnzlz.database.data.DBConfiguration;

public class ACDataBase {

	DBConfiguration configuration;
	
	ArrayList<ACTable> tables;
	
	public static <T extends DBConfiguration> ACDataBase autocode(Class<T> c) {
		return new ACDataBase(c);
	}
	
	<T extends DBConfiguration> ACDataBase(Class<T> c) {
		configuration = DBConfiguration.configuration(c);
		configuration.connection().tables().stream().forEach(e ->{
			ACTable table = table(e.get("TABLE_NAME").stringValue());
			table.addColumns(configuration.connection().columns(table.table()));
			table.addPrimaryKeys(configuration.connection().primaryKeys(table.table()));
			table.addHasOne(configuration.connection().importedKeys(table.table()));
			table.addHasMany(configuration.connection().exportedKeys(table.table()));
			table.addBelongsToMany(configuration.connection().manyToManyKeys(table.table()));
		});
		
		ACFileModel.createFile(this);
		ACFileCustomModel.createFile(this);
	}
	
	ACTable table(String table) {
		for (ACTable acTable : tables()) {
			if(acTable.table().equals(table))
				return acTable;
		}
		ACTable acTable = new ACTable(table, this);
		tables().add(acTable);
		return acTable;
	}
	
	/***********************
	 * Tables
	 ***********************/
	
	ArrayList<ACTable> tables() {
		if(tables == null) tables = new ArrayList<ACTable>(); 
		return tables;
	}
}
