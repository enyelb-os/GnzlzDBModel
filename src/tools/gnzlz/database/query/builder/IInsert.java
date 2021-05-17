package tools.gnzlz.database.query.builder;

import tools.gnzlz.database.query.builder.data.GInsert;

public interface IInsert<Type extends Query<?>> extends IValues<Type>{
	
	Type type();
	
	GInsert gDelete();
	
	/***************************
	 * columns
	 ***************************/
	
	public default Type columns(String ... columns){
		if(columns != null){
			for (String column : columns) {
				gDelete().inserts(column);
			}
		}
		return type();
	}
	/***************************
	 * table
	 ***************************/
	
	public default Type table(String table){
		gDelete().tables(table);
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateInsert(){
		StringBuilder str = new StringBuilder("INSERT INTO");
		if(gDelete().table() == null || gDelete().table().isEmpty())
			throw new RuntimeException("Insert Into error invalid table");
		else
			str.append(" ").append(gDelete().table());
		
		for (int i = 0; i < gDelete().inserts().size(); i++) {
			str.append(i == 0 ? " (":", ");
			str.append(gDelete().inserts().get(i));
		}
		
		if(gDelete().inserts().size() > 0) str.append(") ");
		else throw new RuntimeException("INSERT INTO error invalid columns size 0");
		
		return str.append(generateValues());
	}
}
