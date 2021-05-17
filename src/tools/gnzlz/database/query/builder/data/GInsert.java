package tools.gnzlz.database.query.builder.data;

import java.util.ArrayList;

public class GInsert {
	
	private ArrayList<String> inserts;
	private String table;
	
	/***************************
	 * insert
	 ***************************/
	
	public ArrayList<String> inserts() {
		if(inserts == null ) inserts = new ArrayList<String>();
		return inserts;
	}
	
	private String createColumn(String column){
		if(column != null && !column.isEmpty()) {
			for (String dColumn : inserts())
				if(dColumn.equalsIgnoreCase(column))
					return dColumn;
					
			this.inserts().add(column);
			return column;
		}
		return null;
	}

	public void inserts(String column) {
		createColumn(column);
	}
	
	/***************************
	 * table
	 ***************************/
	
	public String table() {
		return table;
	}
	
	public void tables(String table) {
		if(table != null && !table.isEmpty()) 
			this.table = table;
	}
}
