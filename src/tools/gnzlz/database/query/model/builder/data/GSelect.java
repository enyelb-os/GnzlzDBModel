package tools.gnzlz.database.query.model.builder.data;

import java.util.ArrayList;

public class GSelect {
	
	private boolean distint;
	private ArrayList<DSelect> selects;
	private ArrayList<DFrom> froms;
	private ArrayList<DJoin> joins;
	private ArrayList<DOrderBy> orders;
	private DLimit limit;
	
	/***************************
	 * distint
	 ***************************/
	
	public boolean isDistint() {
		return distint;
	}

	public void distinct(boolean distint) {
		this.distint = distint;
	}
	
	/***************************
	 * select
	 ***************************/
	
	public ArrayList<DSelect> selects() {
		if(selects == null ) selects = new ArrayList<DSelect>();
		return selects;
	}
	
	private DSelect createColumn(DFrom table, String column){
		if(column != null && !column.isEmpty()) {
			for (DSelect dColumn : selects())
				if(dColumn.table == table && dColumn.column.equalsIgnoreCase(column))
					return dColumn;
					
			DSelect dSelect = new DSelect(table, column);
			this.selects().add(dSelect);
			return dSelect;
		}
		return null;
	}
	
	private DSelect createColumn(String column){
		if(column != null && !column.isEmpty()) {
			for (DSelect dColumn : selects())
				if(dColumn.column.equalsIgnoreCase(column))
					return dColumn;
					
			DSelect dSelect = new DSelect(null, column);
			this.selects().add(dSelect);
			return dSelect;
		}
		return null;
	}

	public void selects(String column) {
		createColumn(null, column);
	}
	
	public void selects(String table, String column) {
		createColumn(createTable(table, null), column);
	}
	
	/***************************
	 * from
	 ***************************/
	
	public ArrayList<DFrom> froms() {
		if(froms == null ) froms = new ArrayList<DFrom>();
		return froms;
	}
	
	private DFrom createTable(String table, String as){
		if(table != null && !table.isEmpty()) {
			for (DFrom dTable : froms()) {
				if(dTable.table.equalsIgnoreCase(table)) {
					if(dTable.as == null && as != null) dTable.as = as;
					return dTable;
				}
			}
			DFrom dTable = new DFrom(table, as);
			this.froms().add(dTable);
			return dTable;
		}
		return null;
	}

	public void froms(String table) {
		createTable(table, null);
	}
	
	public void froms(String table, String as) {
		createTable(table, as);
	}
	
	/***************************
	 * join
	 ***************************/
	
	public ArrayList<DJoin> joins() {
		if(joins == null ) joins = new ArrayList<DJoin>();
		return joins;
	}
	
	public void joins(String table1, String column1, String table2, String column2, DJoin.TYPE type) {
		DFrom dtable1 = createTable(table1, null);
		DFrom dtable2 = createTable(table2, null);
		this.joins().add(new DJoin(dtable1, column1, dtable2, column2, type));
	}
	
	public void joins(String table1, String as1,  String column1, String table2, String as2, String column2, DJoin.TYPE type) {
		DFrom dtable1 = createTable(table1, as1);
		DFrom dtable2 = createTable(table2, as2);		
		this.joins().add(new DJoin(dtable1, column1, dtable2, column2, type));
	}
	
	/***************************
	 * order by
	 ***************************/
	
	public ArrayList<DOrderBy> orders() {
		if(orders == null ) orders = new ArrayList<DOrderBy>();
		return orders;
	}
	
	public boolean isOrders() {
		if(orders != null && !orders.isEmpty())
			return true;
		return false;
	}
	
	public DSelect col(int column) {
		if(column >= 0) {
			if(column < selects().size())
				return selects().get(column);
			return null;
		}
			
		return null;
	}
	
	private boolean orderExist(String column) {
		for (DOrderBy dOrderBy : orders()) {
			return dOrderBy.isColumn(column);		
		}
		return false;
	}
	
	public void orders(String column, DOrderBy.TYPE type) {
		if(!orderExist(column))
			orders().add(new DOrderBy(createColumn(column),type));
	}
	
	public void orders(int column, DOrderBy.TYPE type) {
		DSelect dSelect = col(column);
		if(dSelect!=null && !orderExist(dSelect.column))
			orders().add(new DOrderBy(dSelect,type));
	}
	
	/***************************
	 * limit
	 ***************************/

	public void limits(int ini, int end) {
		if(limit == null) limit = new DLimit(ini, end);
		else limit.limit(ini, end);
	}
	
	public void limits(int ini) {
		limits(ini, -1);
	}
	
	public void limits() {
		limits(-1, -1);
	}
	
	public DLimit dlimit() {
		return limit;
	}
}
