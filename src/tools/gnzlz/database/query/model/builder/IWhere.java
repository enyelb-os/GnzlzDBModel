package tools.gnzlz.database.query.model.builder;

import tools.gnzlz.database.query.model.builder.Query.Value;
import tools.gnzlz.database.query.model.builder.data.GWhere;

public interface IWhere<Type extends Query<Type,?>> {
	
	Type type();
	
	GWhere gWhere();
	
	/***************************
	 * WHERE
	 ***************************/
	
	public default Type add(Query<?,?> query){
		if(gWhere().query(query)){
			type().addValues(query.values());
		}
		return type();
	}
	
	/***************************
	 * WHERE
	 ***************************/
	
	public default Type where(String column, String operator, Object value){
		IWhere.validateOperator(operator);
		gWhere().where(column, operator);
		type().addValue(Value.WHERE,value);
		return type();
	}
	
	public default Type where(String column, String operator){
		IWhere.validateOperator(operator);
		gWhere().where(column, operator);
		return type();
	}
	
	public default Type where(int column, String operator, Object value){
		IWhere.validateOperator(operator);
		gWhere().where(column, operator);
		type().addValue(Value.WHERE,value);
		return type();
	}
	
	public default Type where(int column, String operator){
		IWhere.validateOperator(operator);
		gWhere().where(column, operator);
		return type();
	}
	
	public default Type where(String column, Object value){
		return where(column, "=", value);
	}
	
	public default Type where(String column){
		return where(column, "=");
	}
	
	public default Type where(int column, Object value){
		return where(column, "=", value);
	}
	
	public default Type where(int column){
		return where(column, "=");
	}
	
	public default Type where(int column, String operator, int value){
		IWhere.validateOperator(operator);
		gWhere().where(column, operator, value);
		return type();
	}
	
	public default Type where(int column, int value){
		return where(column, "=", value);
	}
		
	/***************************
	 * WHERE FOR
	 ***************************/
	
	public default Type whereFor(String column, String operator, Object ... values){
		if(values != null)
			for (Object object : values)
				if(object != null)
					where(column, operator, object);
		return type();
	}
	
	public default Type whereFor(String column, String operator, int size){
		if(size > 0)
			for (int i = 0; i < size; i++)
				where(column, operator);
		return type();
	}
	
	public default Type whereFor(int column, String operator, Object ... values){
		if(values != null)
			for (Object object : values)
				if(object != null)
					where(column, operator, object);
		return type();
	}
	
	public default Type whereFor(int column, String operator, int size){
		if(size > 0)
			for (int i = 0; i < size; i++)
				where(column, operator);
		return type();
	}
	
	public default Type whereFor(String column, Object ... values){
		return whereFor(column,"=",values);
	}
	
	public default Type whereFor(String column, int size){
		return whereFor(column, "=",size);
	}
	
	public default Type whereFor(int column, Object ... values){
		return whereFor(column, "=", values);
	}
	
	public default Type whereFor(int column, int size){
		return whereFor(column, "=", size);
	}
	
	/***************************
	 * IN
	 ***************************/
	
	public default Type in(String column, Object ... values){
		if(values != null) {
			int size = 0;
			for (int i = 0; i < values.length; i++) {
				if(values[i] != null) {
					type().addValue(Value.WHERE,values[i]);
					size++;
				}
			}
			return in(column, size);
		}
		return type();
	}
	
	public default Type in(int column, Object ... values){
		if(values != null) {
			int size = 0;
			for (int i = 0; i < values.length; i++) {
				if(values[i] != null) {
					type().addValue(Value.WHERE,values[i]);
					size++;
				}
			}
			return in(column, size);
		}
		return type();
	}
	
	public default Type in(String column, int size){
		if(size > 0) {
			gWhere().in(column, size);
		}
		return type();
	}
	
	public default Type in(int column, int size){
		if(size > 0) {
			gWhere().in(column, size);
		}
		return type();
	}
	
	/***************************
	 * WHERE LITERAL
	 ***************************/
	
	public default Type wherel(String column, String operator, String value){
		IWhere.validateOperator(operator);
		gWhere().where(column, operator, value);	
		return type();
	}
	
	public default Type wherel(int column, String operator, String value){
		IWhere.validateOperator(operator);
		gWhere().where(column, operator, value);	
		return type();
	}
	
	public default Type wherel(String column, String value){
		return wherel(column, "=", value);
	}
	
	public default Type wherel(int column, String value){
		return wherel(column, "=", value);
	}
	
	/***************************
	 * NOT
	 ***************************/
	
	public default Type not(){
		gWhere().NOT();
		return type();
	}
	
	/***************************
	 * AND
	 ***************************/
	
	public default Type and(){
		gWhere().AND();
		return type();
	}
	
	/***************************
	 * OR
	 ***************************/
	
	public default Type or(){
		gWhere().OR();
		return type();
	}
	
	/***************************
	 * LIKE
	 ***************************/
	
	public default Type like(String column,Object value){
		gWhere().like(column);
		type().addValue(Value.WHERE,value);
		return type();
	}
	
	public default Type like(String column){
		gWhere().like(column);
		return type();
	}
	
	public default Type like(int column,Object value){
		gWhere().like(column);
		type().addValue(Value.WHERE,value);
		return type();
	}
	
	public default Type like(int column){
		gWhere().like(column);
		return type();
	}
	
	/***************************
	 * BETWEEN
	 ***************************/
	
	public default Type between(String column,Object value1,Object value2){
		gWhere().between(column);
		type().addValue(Value.WHERE,value1);
		type().addValue(Value.WHERE,value2);
		return type();
	}
	
	public default Type between(String column){
		gWhere().between(column);
		return type();
	}
	
	public default Type between(int column,Object value1,Object value2){
		gWhere().between(column);
		type().addValue(Value.WHERE,value1);
		type().addValue(Value.WHERE,value2);
		return type();
	}
	
	public default Type between(int column){
		gWhere().between(column);
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateWhere(){
		StringBuilder str = new StringBuilder("WHERE");

		for (Object object : gWhere().wheres()) {
			str.append(" ").append(object.toString());
		}
		
		if(str.length() == 5) str.setLength(0);
		
		return str.append(" ");
	}
	
	public default StringBuilder generate(){
		StringBuilder str = new StringBuilder();

		for (Object object : gWhere().wheres()) {
			str.append(" ").append(object.toString());
		}
		
		if(str.length() == 0) str.setLength(0);
		
		return str.append(" ");
	}
	
	/***************************
	 * Validate Operator
	 ***************************/
	
	static boolean validateOperator(String operator){
		if(operator.equals("=") || operator.equals(">") || operator.equals("<")
				|| operator.equals(">=") || operator.equals("<=") || operator.equals("<>")){
			return true;
		}else{
			throw new RuntimeException("Operator invalid " + operator);
		}
	}
}
