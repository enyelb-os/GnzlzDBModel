package tools.gnzlz.database.query.builder.data;

public class DSelect{
	
	public DFrom table;
	public String column;
	
	public DSelect(DFrom table, String column) {
		this.table = table;
		this.column = column;
	}
	
	public String column() {
		if(table != null )
			return table.as().concat(".").concat(column);
		return column;
	}
	
	@Override
	public String toString() {
		return column();
	}
}