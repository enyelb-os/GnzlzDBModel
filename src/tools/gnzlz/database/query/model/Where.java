package tools.gnzlz.database.query.model;

import tools.gnzlz.database.model.DBModel;
import tools.gnzlz.database.query.model.builder.IWhere;
import tools.gnzlz.database.query.model.builder.Query;
import tools.gnzlz.database.query.model.builder.data.GWhere;

public class Where<M extends DBModel<M>> extends Query<Where<M>,M> implements IWhere<Where<M>>{
	
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
