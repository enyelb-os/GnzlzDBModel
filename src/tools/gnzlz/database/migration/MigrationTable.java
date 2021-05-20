package tools.gnzlz.database.migration;

import java.util.ArrayList;

public class MigrationTable {

	private String table;
	private ArrayList<MigrationColumn> columns;

	/*****************
	 * table
	 *****************/

	public MigrationTable table(String table) {
		this.table = table;
		return this;
	}

	public String table() {
		return table;
	}

	/*****************
	 * column
	 *****************/

	public MigrationTable colmun(MigrationColumn column) {
		columns().add(column);
		return this;
	}

	public ArrayList<MigrationColumn> columns() {
		if (columns == null) columns = new ArrayList<MigrationColumn>();
		return columns;
	}
}
