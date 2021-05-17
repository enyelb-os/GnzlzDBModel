package tools.gnzlz.database.query.builder.data;

public class DOrderBy{
	
	public enum TYPE{
		ASC, DESC
	};
	
	private DSelect column;
	private TYPE type;
	
	public DOrderBy(DSelect column, TYPE type){
		this.column = column;
		this.type = type;
	}
	
	public boolean isColumn(String column){
		return this.column.column.equalsIgnoreCase(column);
	}
	
	public String orderBy() {
		return column.column().concat( type == TYPE.ASC ? " ASC" : type == TYPE.DESC ? " DESC" : "");
	}
	
	@Override
	public String toString() {
		return orderBy();
	}
}
