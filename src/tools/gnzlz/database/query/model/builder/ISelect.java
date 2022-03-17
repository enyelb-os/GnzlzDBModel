package tools.gnzlz.database.query.model.builder;

import tools.gnzlz.database.query.model.builder.data.DSelect;
import tools.gnzlz.database.query.model.builder.data.GSelect;

public interface ISelect<Type extends Query<Type,?>> extends IFrom<Type> , ILimit<Type>, IOrderBy<Type>, IWhere<Type>{
	
	public Type type();
	
	public GSelect gSelect();
	
	/***************************
	 * select
	 ***************************/
	
	public default Type select(String ... columns){
		return table(null, columns);
	}
	
	public default Type table(String table, String ... columns){
		if(columns != null){
			for (int i=0; i<columns.length; i++) {
				gSelect().selects(table,columns[i]);
			}
		}
		
		return type();
	}
	
	/***************************
	 * select
	 ***************************/
	
	public default Type distinct(){
		gSelect().distinct(true);
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateSelect(){
		StringBuilder str = new StringBuilder("SELECT" + (gSelect().isDistint() ? " DISTINCT" : ""));
		int i = 0;
		for (DSelect select : gSelect().selects()) {
			str.append(i > 0 ? ", ":" ").append(select.column());
			i++;
		}
		
		str.append(i == 0 ? " * ": " ");
		
		return str.append(generateForm())
				  .append(generateWhere())
				  .append(generateOrderBy())
				  .append(generateLimit());
	}
}
