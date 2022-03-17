package tools.gnzlz.database.query.migration.builder.data;

public class DForeignKey {

	String column;
	String table;
	
	public DForeignKey(String table,String column) {
		this.table = table;
		this.column = column;
	}
	
	@Override
	public String toString() {
		return column;
	}
}