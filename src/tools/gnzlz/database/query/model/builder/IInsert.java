package tools.gnzlz.database.query.model.builder;

import tools.gnzlz.database.query.model.builder.data.GInsert;

public interface IInsert<Type extends Query<?>> extends IValues<Type>{
	
	Type type();
	
	GInsert gInsert();
	
	/***************************
	 * columns
	 ***************************/
	
	public default Type columns(String ... columns){
		if(columns != null){
			for (String column : columns) {
				gInsert().inserts(column);
			}
		}
		return type();
	}
	/***************************
	 * table
	 ***************************/
	
	public default Type table(String table){
		gInsert().tables(table);
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateInsert(){
		StringBuilder str = new StringBuilder("INSERT INTO");
		if(gInsert().table() == null || gInsert().table().isEmpty())
			throw new RuntimeException("Insert Into error invalid table");
		else
			str.append(" ").append(gInsert().table());
		
		for (int i = 0; i < gInsert().inserts().size(); i++) {
			str.append(i == 0 ? " (":", ");
			str.append(gInsert().inserts().get(i));
		}
		
		if(gInsert().inserts().size() > 0) str.append(") ");
		else throw new RuntimeException("INSERT INTO error invalid columns size 0");
		
		return str.append(generateValues());
	}
}
