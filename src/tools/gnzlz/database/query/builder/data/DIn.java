package tools.gnzlz.database.query.builder.data;

public class DIn{
	
	boolean not; 
	Object column;
	int size;
	
	public DIn(Object column, int size, boolean not) {
		this.column = column;
		this.size = size;
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
	
	public String size() {
		StringBuilder str = new StringBuilder(not()+" IN (");
		for (int i = 0; i < size; i++) {
				str.append("?");
				if(i != size-1)
					str.append(",");
		}
		return str.append(")").toString();
	}
	
	public String in() {
		return column().concat(size());
	}
	
	@Override
	public String toString() {
		return in();
	}
}