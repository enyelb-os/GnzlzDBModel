package tools.gnzlz.database.query.model;

import tools.gnzlz.database.model.DBModel;
import tools.gnzlz.database.query.model.builder.IUpdate;
import tools.gnzlz.database.query.model.builder.Query;
import tools.gnzlz.database.query.model.builder.data.GUpdate;
import tools.gnzlz.database.query.model.builder.data.GWhere;

public class Update<M extends DBModel<M>> extends Query<Update<M>,M> implements IUpdate<Update<M>>{
	
	public Update() {}
	
	public static <M extends DBModel<M>> Update<M> create(String table, String ... columns) {
		return new Update<M>().table(table, columns);
	}
	
	public static <M extends DBModel<M>> Update<M> create() {
		return new Update<M>();
	}
	
	public Update<M> table(String table, String ... columns){
		if(table == null || table.isEmpty()) return this;
		
		String[] columnsT = columns != null ? columns : new String[0];
		if(columnsT.length == 1 && !columns[0].isEmpty()){
			columnsT = columns[0].trim().split(",");
		}
		
		for (int i = 0; i < columnsT.length; i++) {
			if(!columnsT[i].isEmpty())
				columns(table.concat(".").concat(columnsT[i]));
		}
		
		return this; 
	}
	
	/***************************
	 * where
	 ***************************/
	
	private GWhere dWhere;
	
	@Override
	public GWhere gWhere() {
		if(dWhere == null) dWhere = new GWhere(gUpdate); 
		return dWhere;
	}
	
	/***************************
	 * update
	 ***************************/
	
	private GUpdate gUpdate;
	
	@Override
	public GUpdate gUpdate() {
		if(gUpdate == null) gUpdate = new GUpdate();
		return gUpdate;
	}
	
	@Override
	public Update type() {
		return this;
	}

	@Override
	public String query() {
		return generateUpdate().toString();
	}
}
