package tools.gnzlz.database.query.migration.builder.data;

import java.util.ArrayList;

public class GCreateTable {

	private String column;
	private ArrayList<DColumn> columns;
	private String table;
	
	/***************************
	 * insert
	 ***************************/
	public ArrayList<DColumn> columns() {
		if(columns == null ) columns = new ArrayList<DColumn>();
		return columns;
	}

	private DColumn createColumn(String column){
		if(column != null && !column.isEmpty()) {
			for (DColumn dColumn : columns())
				if(dColumn.column.equalsIgnoreCase(column))
					return dColumn;

			DColumn dColumn = new DColumn(column);
			this.columns().add(dColumn);
			return dColumn;
		}
		return null;
	}

	public DColumn column(String column) {
		this.column = column;
		return createColumn(column);
	}

	public DColumn column() {
		return createColumn(column);
	}
	
	/***************************
	 * table
	 ***************************/
	
	public String table() {
		return table;
	}
	
	public void table(String table) {
		if(table != null && !table.isEmpty()) 
			this.table = table;
	}
}
