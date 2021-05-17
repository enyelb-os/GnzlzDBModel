package tools.gnzlz.database.query.builder.data;

import java.util.ArrayList;

public class GUpdate {
	
	private ArrayList<String> sets;
	private String table;
	
	/***************************
	 * insert
	 ***************************/
	
	public ArrayList<String> sets() {
		if(sets == null ) sets = new ArrayList<String>();
		return sets;
	}
	
	private String createColumn(String column){
		if(column != null && !column.isEmpty()) {
			for (String dColumn : sets())
				if(dColumn.equalsIgnoreCase(column))
					return dColumn;
					
			this.sets().add(column);
			return column;
		}
		return null;
	}

	public void sets(String column) {
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
