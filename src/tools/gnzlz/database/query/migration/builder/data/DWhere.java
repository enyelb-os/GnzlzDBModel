package tools.gnzlz.database.query.migration.builder.data;

public class DWhere{
	
	boolean not;
	Object column;
	String operator;
	Object value;
	
	public DWhere(Object column, String operator, Object value, boolean not) {
		this.column = column;
		this.operator = operator;
		this.value = value;
		this.not = not;
	}
	
	public String column() {
		if(column instanceof DColumn)
			return ((DColumn)column).column();
		return column.toString();
	}
	
	public String value() {
		if(value instanceof DColumn)
			return ((DColumn)value).column();
		return value.toString();
	}
	
	public String not() {
		return not ? "NOT ":"";
	}
	
	public String where() {
		return not().concat(column()).concat(" ").concat(operator).concat(" ").concat(value());
	}
	
	@Override
	public String toString() {
		return where();
	}
}