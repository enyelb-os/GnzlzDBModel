package tools.gnzlz.database.autocode.migration;

import tools.gnzlz.database.autocode.ACFormat;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;

public class ACTable {
	
	private final String table;

	private final String packegeName;
	
	private ArrayList<ACColumn> columns;
	
	final ACMigration dataBase;
	
	ACTable(String table, String packegeName, ACMigration acDataBase) {
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
			acColumn.type(dbModel.string("TYPE_NAME"));
			acColumn.size(dbModel.string("COLUMN_SIZE"));
			acColumn.nullable(dbModel.string("IS_NULLABLE").equals("YES"));
			acColumn.isDefault(dbModel.string("COLUMN_DEF"));
			acColumn.autoincrement(dbModel.string("IS_AUTOINCREMENT").equals("YES"));
		}
	}
	
	/***********************
	 * initPrimaryKey
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
	 * initHasOne
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
	 * PrimaryKey
	 ***********************/
	
	ACColumn primaryKey() {
		for (ACColumn acColumn : columns()) {
			if (acColumn.primaryKey()) {
				return acColumn;
			}
		}
		return null;
	}

}
