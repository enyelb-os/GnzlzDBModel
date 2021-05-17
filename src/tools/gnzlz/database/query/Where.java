package tools.gnzlz.database.query;

import tools.gnzlz.database.query.builder.IWhere;
import tools.gnzlz.database.query.builder.Query;
import tools.gnzlz.database.query.builder.data.GWhere;

public class Where extends Query<Where> implements IWhere<Where>{
	
	private Where() {}
	
	public static Where newI() {
		return new Where();
	}
	
	/***************************
	 * where
	 ***************************/
	
	private GWhere dWhere;
	
	@Override
	public GWhere gWhere() {
		if(dWhere == null) dWhere = new GWhere(null); 
		return dWhere;
	}
	
	@Override
	public Where type() {
		return this;
	}

	@Override
	public String query() {
		return generate().toString();
	}
}
