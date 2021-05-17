package tools.gnzlz.database.query.builder.data;

public class GDelete {
	
	private String table;
	
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
