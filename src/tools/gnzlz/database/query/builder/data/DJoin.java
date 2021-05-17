package tools.gnzlz.database.query.builder.data;

public class DJoin{
	
	public enum TYPE{
		INNER, LEFT, RIGHT, FULL
	};
	
	private boolean show1;
	private boolean show2;
	
	public TYPE type;
	public DFrom table1;
	public String column1;
	public DFrom table2;
	public String column2;
	
	
	public DJoin(DFrom table1, String column1, DFrom table2, String column2, TYPE type){
		this.column1 = column1;
		this.column2 = column2;
		
		this.table1 = table1;
		this.show1 = table1.dJoin == null;
		this.table1.dJoin = this;
		
		this.table2 = table2;
		this.show2 = table2.dJoin == null;
		this.table2.dJoin = this;
		
		this.type = type;
	}
	
	public String join() {
		if(!show1)
			return joinShowOne(column1, table1, column2, table2);
		else if(!show2)
			return joinShowOne(column2, table2, column1, table1);
		else
			return table1.table().concat(" ").concat(typeJoin()).concat(" ").concat(table2.table()).concat(" ON ")
				.concat(table1.as()).concat(".").concat(column1).concat("=")
				.concat(table2.as()).concat(".").concat(column2);
	}
	
	private String joinShowOne(String column1,DFrom table1, String column2,DFrom table2) {
		return typeJoin().concat(" ").concat(table2.table()).concat(" ON ")
				.concat(table1.as()).concat(".").concat(column1).concat("=")
				.concat(table2.as()).concat(".").concat(column2);
	}
	
	private String typeJoin(){
		return type == TYPE.INNER ? "INNER JOIN" : type == TYPE.LEFT ? "LEFT JOIN" : type == TYPE.RIGHT ? "RIGHT JOIN" : "FULL JOIN"; 
	}
	
	@Override
	public String toString() {
		return join();
	}
}
