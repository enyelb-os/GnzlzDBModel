package tools.gnzlz.database.query.model;

import tools.gnzlz.database.model.DBModel;
import tools.gnzlz.database.query.model.builder.IDelete;
import tools.gnzlz.database.query.model.builder.Query;
import tools.gnzlz.database.query.model.builder.data.GDelete;
import tools.gnzlz.database.query.model.builder.data.GWhere;

public class Delete<M extends DBModel<M>> extends Query<Delete<M>,M> implements IDelete<Delete<M>>{
	
	public Delete() {}

	public static <M extends DBModel<M>> Delete<M> create() {;
		return new Delete<M>();
	}
	
	/***************************
	 * where
	 ***************************/
	
	private GDelete dDelete;
	
	@Override
	public GDelete gDelete() {
		if(dDelete == null) dDelete = new GDelete(); 
		return dDelete;
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
	public Delete<M> type() {
		return this;
	}

	@Override
	public String query() {
		StringBuilder builder = generateDetele();
		return builder.toString();
	}

	
}
