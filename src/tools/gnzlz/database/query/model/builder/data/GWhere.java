package tools.gnzlz.database.query.model.builder.data;

import java.util.ArrayList;

import tools.gnzlz.database.query.model.builder.IWhere;
import tools.gnzlz.database.query.model.builder.Query;

public class GWhere {
	
	private boolean priority = true;
	private boolean isAND = priority;
	private boolean isNOT;
	private ArrayList<Object> wheres;
	
	private Object col;
	
	public GWhere(Object col) {
		this.col = col;
	}
	
	/***************************
	 * AND - OR - NOT
	 ***************************/
	
	public void priority(boolean priority) {
		this.priority = priority;
	}
	
	public void priorityOR() {
		priority(false);
	}
	
	public void priorityAND() {
		priority(true);
	}
	
	public void AND() {
		isAND = true;
	}

	public void OR() {
		isAND = false;
	}
	
	public void NOT() {
		isNOT = true;
	}

	public boolean isAND() {
		boolean is = isAND;
		isAND = priority;
		return is;
	}
	
	public boolean isNOT() {
		boolean is = isNOT;
		isNOT = false;
		return is;
	}
	
	private void addIsAND() {
		if(!wheres().isEmpty())
			if(isAND())
				wheres().add("AND");
			else
				wheres().add("OR");
	}
	
	/***************************
	 * cols
	 ***************************/
	
	public Object col(String column) {
		if(col != null && !column.isEmpty()) {
			if(col instanceof GSelect)
				for (DSelect dSelect : ((GSelect)col).selects()) {
					if(dSelect.column.equalsIgnoreCase(column))
						return dSelect;
				}
			else if(col instanceof GUpdate)
				for (String string : ((GUpdate)col).sets()) {
					if(string.equalsIgnoreCase(column))
						return string;
				}
		}
			
		return column;
	}
	
	public Object col(int column) {
		if(col != null && column>=0) {
			if(col instanceof GSelect) {
				if(column < ((GSelect)col).selects().size())
					return ((GSelect)col).selects().get(column);
			}else if(col instanceof GUpdate)
				if(column < ((GUpdate)col).sets().size())
					return ((GUpdate)col).sets().get(column);
			return null;
		}
			
		return null;
	}
	
	/***************************
	 * query
	 ***************************/
	
	public boolean query(Query<?> query){
		if(query != null){
			String s = "";
			if(query instanceof IWhere<?>)
				s = ((IWhere) query).generate().toString(); 
			else
				s = query.query();
			
			if(!s.isEmpty()) {
				addIsAND();
				wheres().add("(".concat(s).concat(")"));
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Object> wheres() {
		if(wheres == null ) wheres = new ArrayList<Object>();
		return wheres;
	}
	
	/***************************
	 * where
	 ***************************/
	
	public void where(String column, String operator){
		addIsAND();
		wheres().add(new DWhere(col(column), operator, "?", isNOT()));
	}
	
	public void where(int column, String operator){
		addIsAND();
		wheres().add(new DWhere(col(column), operator, "?", isNOT()));
	}
	
	public void where(String column, String operator, String value){
		addIsAND();
		wheres().add(new DWhere(col(column), operator, value, isNOT()));
	}
	
	public void where(int column, String operator, String value){
		addIsAND();
		wheres().add(new DWhere(col(column), operator, value, isNOT()));
	}
	
	public void where(int column, String operator, int value){
		addIsAND();
		wheres().add(new DWhere(col(column), operator, col(value), isNOT()));
	}
	
	/***************************
	 * in
	 ***************************/
	
	public void in(String column, int size){
		addIsAND();
		wheres().add(new DIn(col(column), size, isNOT()));
	}
	
	public void in(int column, int size){
		addIsAND();
		wheres().add(new DIn(col(column), size, isNOT()));
	}
	
	/***************************
	 * like
	 ***************************/
	
	public void like(String column){
		addIsAND();
		wheres().add(new DLike(col(column), isNOT()));
	}
	
	public void like(int column){
		addIsAND();
		wheres().add(new DLike(col(column), isNOT()));
	}
	
	/***************************
	 * between
	 ***************************/
	
	public void between(String column){
		addIsAND();
		wheres().add(new DBetween(col(column), isNOT()));
	}
	
	public void between(int column){
		addIsAND();
		wheres().add(new DBetween(col(column), isNOT()));
	}
}
