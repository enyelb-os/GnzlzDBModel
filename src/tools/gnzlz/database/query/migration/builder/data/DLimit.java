package tools.gnzlz.database.query.migration.builder.data;

public class DLimit{
	
	private int limit1;
	private int limit2;
	
	public DLimit(int l1, int l2){
		limit1 = l1;
		limit2 = l2;
	}
	
	public void limit(int l1, int l2){
		limit1 = l1;
		limit2 = l2;
	}
	
	public String limit() {
		if(limit1 == -1)
			return " ?,?";
		else if(limit2 == -1)
			return " ".concat(String.valueOf(limit1));
		else if(limit1 > 0 && limit2 >= 0)
			return " ".concat(String.valueOf(limit1)).concat(" OFFSET ").concat(String.valueOf(limit2));
		else 
			return "";
	}
	
	@Override
	public String toString() {
		return limit();
	}
}
