package tools.gnzlz.database.query;

import tools.gnzlz.database.query.builder.IInsert;
import tools.gnzlz.database.query.builder.Query;
import tools.gnzlz.database.query.builder.data.GInsert;

public class Insert extends Query<Insert> implements IInsert<Insert>{

	public Insert() {}
	
	public static Insert create() {
		return new Insert();
	}
	
	/***************************
	 * insert
	 ***************************/
	
	private GInsert dInsert;
	
	@Override
	public GInsert gDelete() {
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
