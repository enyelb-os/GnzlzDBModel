package tools.gnzlz.database.query.model;

import tools.gnzlz.database.model.DBModel;
import tools.gnzlz.database.query.model.builder.IInsert;
import tools.gnzlz.database.query.model.builder.Query;
import tools.gnzlz.database.query.model.builder.data.GInsert;

public class Insert<M extends DBModel<M>> extends Query<Insert<M>,M> implements IInsert<Insert<M>>{

	public Insert() {}
	
	public static <M extends DBModel<M>> Insert<M> create() {
		return new Insert<M>();
	}
	
	/***************************
	 * insert
	 ***************************/
	
	private GInsert dInsert;
	
	@Override
	public GInsert gInsert() {
		if(dInsert == null) dInsert = new GInsert();
		return dInsert;
	}
	
	@Override
	public Insert type() {
		return this;
	}

	@Override
	public String query() {
		return generateInsert().toString();
	}
}
