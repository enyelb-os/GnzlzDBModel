package tools.gnzlz.database.query.builder;

import tools.gnzlz.database.query.builder.data.GUpdate;

public interface IUpdate<Type extends Query<?>> extends ISet<Type>, IWhere<Type>{
	
	Type type();
	
	GUpdate gUpdate();
	
	/***************************
	 * table
	 ***************************/
	
	public default Type table(String table){
		gUpdate().tables(table);
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateUpdate(){
		StringBuilder str = new StringBuilder("UPDATE");
		if(gUpdate().table() == null || gUpdate().table().isEmpty())
			throw new RuntimeException("UPDATE error invalid table");
		else
			str.append(" ").append(gUpdate().table());
		
		return str.append(" ")
				  .append(generateSet())
				  .append(generateWhere());
	}
}
