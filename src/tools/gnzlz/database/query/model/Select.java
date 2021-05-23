package tools.gnzlz.database.query.model;

import tools.gnzlz.database.query.model.builder.ISelect;
import tools.gnzlz.database.query.model.builder.Query;
import tools.gnzlz.database.query.model.builder.data.GSelect;
import tools.gnzlz.database.query.model.builder.data.GWhere;

public class Select extends Query<Select> implements ISelect<Select>{
	
	public Select() {}
	
	public static Select create(String table,String as, String ... columns) {
		return new Select().table(table, as, columns);
	}
	
	public static Select create() {
		return new Select();
	}
	
	public Select table(String table,String as, String ... columns){
		if(table == null || table.isEmpty()) return this;
		
		String[] columnsT = columns != null ? columns : new String[0];
		if(columnsT.length == 1 && !columns[0].isEmpty()){
			columnsT = columns[0].trim().split(",");
		}
		
		if(!as.isEmpty()){
			fromAS(table, as);
			for (int i = 0; i < columnsT.length; i++) {
				table(table,columnsT);
			}
		}else{
			from(table);
			for (int i = 0; i < columnsT.length; i++) {
				table(table,columnsT);
			}
		}
		
		return this; 
	}
	
	/***************************
	 * where
	 ***************************/
	
	private GWhere dWhere;
	
	@Override
	public GWhere gWhere() {
		if(dWhere == null) dWhere = new GWhere(gSelect()); 
		return dWhere;
	}
	
	/***************************
	 * select
	 ***************************/
	
	private GSelect dSelect;
	
	@Override
	public GSelect gSelect() {
		if(dSelect == null) dSelect = new GSelect(); 
		return dSelect;
	}
	
	@Override
	public Select type() {
		return this;
	}

	@Override
	public String query() {
		StringBuilder select = new StringBuilder(generateSelect());
        System.out.println(select.toString());
		return select.toString();
	}
}
