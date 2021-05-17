package tools.gnzlz.database.query.builder;

import tools.gnzlz.database.query.builder.Query.Value;
import tools.gnzlz.database.query.builder.data.GInsert;

public interface IValues<Type extends Query<?>>{
	
	public Type type();
	
	GInsert gDelete();
	
	/***************************
	 * columns
	 ***************************/
	
	public default Type values(Object ... values){
		if(values != null){
			for (Object value : values) {
				if(value != null) {
					type().addValue(Value.VALUES, value);
				}
			}
		}
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateValues(){
		StringBuilder str = new StringBuilder("VALUES");

		for (int i = 0; i < gDelete().inserts().size(); i++) {
			str.append(i == 0 ? " (":", ").append("?");
			
		}
		
		if(gDelete().inserts().size() > 0) str.append(")");
		else throw new RuntimeException("INSERT INTO error invalid columns size 0");
		
		return str.append(" ");
	}
}
