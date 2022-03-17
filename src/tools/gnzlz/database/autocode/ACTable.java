package tools.gnzlz.database.autocode;

import java.util.ArrayList;

import tools.gnzlz.database.model.DBModel;

public class ACTable {
	
	private final String table;

	private final String packegeName;
	
	private ArrayList<ACColumn> columns;
	
	final ACDataBase dataBase;
	
	ACTable(String table, String packegeName, ACDataBase acDataBase) {
		this.table = table;
		this.packegeName = packegeName;
		this.dataBase = acDataBase;
	}

	String packegeName() {
		return packegeName;
	}
	
	String table() {
		return table;
	}
	
	String tableCamelCase() {
		return ACFormat.camelCaseClass(table);
	}
	
	/***********************
	 * Columns
	 ***********************/
	
	ArrayList<ACColumn> columns() {
		if(columns == null) columns = new ArrayList<ACColumn>(); 
		return columns;
	}
	
	/***********************
	 * Get
	 ***********************/
	
	ACColumn get(String name) {
		if(name != null) {
			for (ACColumn acColumn : columns()) {
				if (acColumn.name().equals(name)) {
					return acColumn;
				}
			}
		}
		return null;
	}
	
	/***********************
	 * initColumns
	 ***********************/
	
	void addColumns(ArrayList<DBModel<?>> list) {
		for (DBModel<?> dbModel : list) {
			String column = dbModel.get("COLUMN_NAME").stringValue();
			ACColumn acColumn = get(column);
			if(acColumn == null) {
				acColumn = new ACColumn(column);
				columns().add(acColumn);
			}
			acColumn.type(dbModel.get("TYPE_NAME").stringValue());
		}
	}
	
	/***********************
	 * intPrimaryKey
	 ***********************/
	
	void addPrimaryKeys(ArrayList<DBModel<?>> list) {
		for (DBModel<?> dbModel : list) {
			String column = dbModel.get("COLUMN_NAME").stringValue();
			ACColumn acColumn = get(column);
			if(acColumn == null) {
				acColumn = new ACColumn(column);
				columns().add(acColumn);
			}
			acColumn.primaryKey(true);
		}
	}
	
	/***********************
	 * intHasOne
	 ***********************/
	
	void addHasOne(ArrayList<DBModel<?>> list) {
		for (DBModel<?> dbModel : list) {
			String column = dbModel.get("FKCOLUMN_NAME").stringValue();
			ACColumn acColumn = get(column);
			if(acColumn == null) {
				acColumn = new ACColumn(column);
				columns().add(acColumn);
			}
			acColumn.addHasOne(dbModel.get("PKCOLUMN_NAME").stringValue(), dbModel.get("PKTABLE_NAME").stringValue());
		}
	}
	
	/***********************
	 * intHasMany
	 ***********************/
	
	void addHasMany(ArrayList<DBModel<?>> list) {
		for (DBModel<?> dbModel : list) {
			String column = dbModel.get("PKCOLUMN_NAME").stringValue();
			ACColumn acColumn = get(column);
			if(acColumn == null) {
				acColumn = new ACColumn(column);
				columns().add(acColumn);
			}
			acColumn.addHasMany(dbModel.get("FKCOLUMN_NAME").stringValue(), dbModel.get("FKTABLE_NAME").stringValue());
		}
	}
	
	/***********************
	 * intBelongsToMany
	 ***********************/
	
	void addBelongsToMany(ArrayList<DBModel<?>> list) {
		for (DBModel<?> dbModel : list) {
			String column = dbModel.get("PKCOLUMN_NAME").stringValue();
			ACColumn acColumn = get(column);
			if(acColumn == null) {
				acColumn = new ACColumn(column);
				columns().add(acColumn);
			}
			acColumn.addBelongsToMany(
				dbModel.get("ICOLUMN_NAME1").stringValue(), 
				dbModel.get("ITABLE_NAME").stringValue(),
				dbModel.get("ICOLUMN_NAME2").stringValue(),
				dbModel.get("FKTABLE_NAME").stringValue(),
				dbModel.get("FKCOLUMN_NAME").stringValue()
			);
		}
	}
	
	
	/***********************
	 * PrimaryKey
	 ***********************/
	
	ACColumn primaryKey() {
		for (ACColumn acColumn : columns()) {
			if (acColumn.isPrimaryKey()) {
				return acColumn;
			}
		}
		return null;
	}

}
