package tools.gnzlz.database.query.model.builder;

import tools.gnzlz.database.query.model.builder.data.GSelect;

public interface ILimit<Type extends Query<?,?>>{
	
	Type type();
	
	GSelect gSelect();
	
	/***************************
	 * limit
	 ***************************/
	
	public default Type limit(int l1, int l2){
		gSelect().limits(l1,l2);
		return type();
	}
	
	public default Type limit(int l1){
		gSelect().limits(l1);
		return type();
	}
	
	public default Type limit(){
		gSelect().limits();
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateLimit(){
		StringBuilder str = new StringBuilder("LIMIT");
		if(gSelect().dlimit() != null)
			str.append(gSelect().dlimit().limit());
		
		if(str.length() == 5) str.setLength(0);
		else str.append(" ");
		return str;
	}
}
