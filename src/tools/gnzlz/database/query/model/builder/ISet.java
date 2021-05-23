package tools.gnzlz.database.query.model.builder;

import tools.gnzlz.database.query.model.builder.Query.Value;
import tools.gnzlz.database.query.model.builder.data.GUpdate;

public interface ISet<Type extends Query<?>>{
	
	Type type();
	
	GUpdate gUpdate();
	
	/***************************
	 * columns
	 ***************************/
	
	public default Type columns(String ... columns){
		if(columns != null){
			for (String column : columns) {
				gUpdate().sets(column);
			}
		}
		return type();
	}
	
	/***************************
	 * value
	 ***************************/
	
	public default Type values(Object ... values){
		if(values != null){
			for (Object value : values) {
				if(value != null)
					type().addValue(Value.SET,value);
			}
		}
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateSet(){
		StringBuilder str = new StringBuilder("SET");
		
		for (int i = 0; i < gUpdate().sets().size(); i++) {
			str.append(i == 0 ? "":", ").append(gUpdate().sets().get(i)).append(" = ?");
		}
		
		if(gUpdate().sets().size() > 0) str.append(" ");
		else throw new RuntimeException("SET error invalid columns size 0");
		
		return str.append(" ");
	}
	
	
}
