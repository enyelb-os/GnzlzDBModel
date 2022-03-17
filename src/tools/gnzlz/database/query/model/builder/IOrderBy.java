package tools.gnzlz.database.query.model.builder;

import tools.gnzlz.database.query.model.builder.data.DOrderBy.TYPE;
import tools.gnzlz.database.query.model.builder.data.GSelect;

public interface IOrderBy<Type extends Query<?,?>>{

	Type type();
	
	GSelect gSelect();
	
	/***************************
	 * Order By
	 ***************************/
	
	public default Type order(String ... columns){
		if(columns != null){
			for (String string : columns) {
				gSelect().orders(string,null);
			}
		}
		
		return type();
	}
	
	public default Type order(int ... columns){
		if(columns != null){
			for (int i : columns) {
				gSelect().orders(i,null);
			}
		}
		
		return type();
	}
	
	public default Type orderAsc(String ... columns){
		if(columns != null){
			for (String string : columns) {
				gSelect().orders(string, TYPE.ASC);
			}
		}
		
		return type();
	}
	
	public default Type orderAsc(int ... columns){
		if(columns != null){
			for (int i : columns) {
				gSelect().orders(i, TYPE.ASC);
			}
		}
		
		return type();
	}
	
	public default Type orderDesc(String ... columns){
		if(columns != null){
			for (String string : columns) {
				gSelect().orders(string, TYPE.DESC);
			}
		}
		return type();
	}
	
	public default Type orderDesc(int ... columns){
		if(columns != null){
			for (int i : columns) {
				gSelect().orders(i, TYPE.DESC);
			}
		}
		return type();
	}
	
	
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateOrderBy(){
		StringBuilder str = new StringBuilder("ORDER BY");
		if(gSelect().isOrders()) {
			for (int i = 0; i < gSelect().orders().size(); i++) {
				if(i>0) str.append(", ");
				else str.append(" ");
				str.append(gSelect().orders().get(i).orderBy());
			}
		}else{
			str.setLength(0);
			return str;
		}
		return str.append(" ");
	}
}
