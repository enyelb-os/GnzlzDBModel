package tools.gnzlz.database.query.model.builder.data;

public class DBetween{
	
	boolean not;
	Object column;
	
	public DBetween(Object column, boolean not) {
		this.column = column;
		this.not = not;
	}
	
	public String column() {
		if(column instanceof DSelect)
			return ((DSelect)column).column();
		return column.toString();
	}
	
	public String not() {
		return not ? " NOT":"";
	}
	
	public String between() {
		return column().concat(not()).concat(" BETWEEN ? AND ?");
	}
	
	@Override
	public String toString() {
		return between();
	}
}