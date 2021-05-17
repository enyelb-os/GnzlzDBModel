package tools.gnzlz.database.query;

import tools.gnzlz.database.query.builder.IDelete;
import tools.gnzlz.database.query.builder.Query;
import tools.gnzlz.database.query.builder.data.GDelete;
import tools.gnzlz.database.query.builder.data.GWhere;

public class Delete extends Query<Delete> implements IDelete<Delete>{
	
	private Delete() {}
	
	public static Delete create() {
		return new Delete();
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
	public Delete type() {
		return this;
	}

	@Override
	public String query() {
		StringBuilder builder = generateDetele();
		System.out.println(builder);
		return builder.toString();
	}

	
}
