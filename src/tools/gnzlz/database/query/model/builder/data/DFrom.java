package tools.gnzlz.database.query.model.builder.data;

public class DFrom{
	
	final String table;
	String as;
	DJoin dJoin;
	
	public DFrom(String table, String as) {
		this.table = table;
		this.as = as;
	}
	
	public boolean isJoin() {
		return dJoin != null;
	}
	
	public String table() {
		if(as != null && !as.isEmpty())
			return table.concat(" AS ").concat(as);
		return table;
	}
	
	public String as() {
		if(as != null && !as.isEmpty())
			return as;
		return table;
	}
	
	@Override
	public String toString() {
		return table();
	}
}