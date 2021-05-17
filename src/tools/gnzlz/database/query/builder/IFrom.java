package tools.gnzlz.database.query.builder;

import tools.gnzlz.database.query.builder.data.GSelect;
import tools.gnzlz.database.query.builder.data.DJoin;
import tools.gnzlz.database.query.builder.data.DJoin.TYPE;
import tools.gnzlz.database.query.builder.data.DFrom;

public interface IFrom<Type extends Query<?>>{
	
	Type type();
	
	GSelect gSelect();
	
	/***************************
	 * select
	 ***************************/
	
	public default Type from(String ... tables){
		if(tables != null){
			for (int i=0; i<tables.length; i++) {
				gSelect().froms(tables[i], null);
			}
		}
		
		return type();
	}
	
	public default Type fromAS(String table, String as){
		gSelect().froms(table, as);
		return type();
	}
	
	/***************************
	 * join
	 ***************************/
	
	default Type join(String tableL, String columnL, String tableR, String columnR, TYPE type){
		if(validate(tableL) && validate(columnL) && validate(tableR) && validate(columnR))
			gSelect().joins(tableL, columnL, tableR, columnR, type);
		return type();
	}
	
	public default Type join(String tableL, String columnL, String tableR, String columnR){
		return join(tableL, columnL, tableR, columnR, TYPE.INNER);
	}
	
	public default Type leftJoin(String tableL, String columnL, String tableR, String columnR){
		return join(tableL, columnL, tableR, columnR, TYPE.LEFT);
	}
	
	public default Type rightJoin(String tableL, String columnL, String tableR, String columnR){
		return join(tableL, columnL, tableR, columnR, TYPE.RIGHT);
	}
	
	public default Type fullJoin(String tableL, String columnL, String tableR, String columnR){
		return join(tableL, columnL, tableR, columnR, TYPE.FULL);
	}
		
	public default boolean validate(String value){
		return value != null && !value.isEmpty();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateForm(){
		StringBuilder str = new StringBuilder("FROM");
		
		for (DFrom table : gSelect().froms()) {
			if(!table.isJoin()) {
				str.append(" ").append(table.table());
			}
		}
		
		for (DJoin join : gSelect().joins()) {
			str.append(" ").append(join.join());
		}
		
		if(str.length() == 4)
			throw new RuntimeException("SELECT error invalid FROM");
		
		return str.append(" ");
	}
}
