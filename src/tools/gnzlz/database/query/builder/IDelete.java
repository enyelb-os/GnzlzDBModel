package tools.gnzlz.database.query.builder;

import tools.gnzlz.database.query.builder.data.GDelete;

public interface IDelete<Type extends Query<?>> extends IWhere<Type>{
	
	Type type();
	
	GDelete gDelete();
	
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
	
	public default StringBuilder generateDetele(){
		StringBuilder str = new StringBuilder("DELETE FROM");
		if(gDelete().table() == null || gDelete().table().isEmpty())
			throw new RuntimeException("Delete error invalid table");
		else
			str.append(" ").append(gDelete().table());
		
		return str.append(" ").append(generateWhere());
	}
}
