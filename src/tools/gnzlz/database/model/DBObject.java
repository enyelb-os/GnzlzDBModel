package tools.gnzlz.database.model;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.Struct;
import java.text.NumberFormat;
import java.util.Date;

public class DBObject {
	
	protected String name;
	protected Object object;
	protected boolean isChange;
	
	protected DBColumn column;
	
	public DBObject(String name, Object object, DBColumn column) {
		this.name = name;
		this.object = object;
		this.column = column;
	}	
	
	/****************************
	 * Name
	 ****************************/
	
	public String name() {
		return name;
	}
	
	/****************************
	 * Object
	 ****************************/
	
	void initObject(Object object) {
		isChange = false;
		this.object = object;
	}
	
	public DBObject object(Object object) {
		if(!equals(this.object, object)) {
			isChange = true;
			this.object = object;
		}
		return this;
	}
	
	public Object object() {
		return object;
	}
	
	/****************************
	 * Change
	 ****************************/
	
	DBObject change(boolean change) {
		this.isChange = change;
		return this;
	}
	
	public boolean isChange() {
		return isChange;
	}
	
	public boolean change() {
		return isChange;
	}
	
	/****************************
	 * Column
	 ****************************/
	
	DBObject column(DBColumn column) {
		this.column = column;
		return this;
	}
	
	public DBColumn column() {
		return column;
	}
	
	/****************************
	 * Parse
	 ****************************/
	
	public int intValue(){
		if(object instanceof Number) {
			return ((Number) object).intValue();
		} else if(object instanceof String){
			try {
				return Integer.parseInt(object.toString());
			}catch (NumberFormatException e){
				return -1;
			}
		}
		return -1;
	}
	
	public long longValue(){
		if(object instanceof Number) {
			return ((Number) object).longValue();
		} else if(object instanceof String){
			try {
				return Long.parseLong(object.toString());
			}catch (NumberFormatException e){
				return -1;
			}
		}
		return -1;
	}
	
	public short shortValue(){
		if(object instanceof Number) {
			return ((Number) object).shortValue();
		} else if(object instanceof String){
			try {
				return Short.parseShort(object.toString());
			}catch (NumberFormatException e){
				return -1;
			}
		}
		return -1;
	}
	
	public float floatValue(){
		if(object instanceof Number) {
			return ((Number) object).floatValue();
		} else if(object instanceof String){
			try {
				return Float.parseFloat(object.toString());
			}catch (NumberFormatException e){
				return -1;
			}
		}
		return -1;
	}
	
	public double doubleValue(){
		if(object instanceof Number) {
			return ((Number) object).doubleValue();
		} else if(object instanceof String){
			try {
				return Double.parseDouble(object.toString());
			}catch (NumberFormatException e){
				return -1;
			}
		}
		return -1;
	}
	
	public BigDecimal decimalValue(){
		if(object instanceof BigDecimal) {
			return (BigDecimal) object;
		} else if(object instanceof String){
			try {
				return new BigDecimal(object.toString());
			}catch (NumberFormatException e){
				return new BigDecimal(-1);
			}
		}
		return new BigDecimal(-1);
	}
	
	public byte byteValue(){
		if(object instanceof Number)
			return ((Number) object).byteValue();
		return -1;
	}
	
	public String stringValue(){
		if(object instanceof String)
			return object.toString();
		if(object instanceof Number)
			return String.valueOf(object);
		if(object != null)
			return object.toString();
		return "";
	}
	
	public boolean booleanValue(){
		if(object instanceof Boolean)
			return (Boolean) object;
		if(object instanceof Number)
			return ((Number)object).intValue() == 1;
		if(object instanceof String)
			return ((String)object).equals("1");
		return false;
	}
	
	public Date dateValue(){
		if(object instanceof Date)
			return ((Date)object);
		return null;
	}
	
	public byte[] byteArrayValue(){
		if(object instanceof Byte[])
			return ((byte[])object);
		return null;
	}
	
	public Clob clobValue(){
		if(object instanceof Clob)
			return ((Clob)object);
		return null;
	}
	
	public Blob blobValue(){
		if(object instanceof Blob)
			return ((Blob)object);
		return null;
	}
	
	public Array arrayValue(){
		if(object instanceof Array)
			return ((Array)object);
		return null;
	}
	
	public Ref refValue(){
		if(object instanceof Ref)
			return ((Ref)object);
		return null;
	}
	
	public Struct structValue(){
		if(object instanceof Struct)
			return ((Struct)object);
		return null;
	}
	
	public String currencyValue(NumberFormat format){
		if(object instanceof Number)
			return format.format(object);
		return "";
	}
	
	/****************************
	 * Equals
	 ****************************/
	
	public boolean equals(Object object1, Object object2) {
		if(object1 == null && object2 == null)
			return true;
		else if(object1 == null && object2 != null)
			return false;
		
		if(object1.getClass() == object2.getClass()) {
			if(object1 instanceof Number) {
				Number one = (Number) object1;
				Number two = (Number) object2;
				
				if(one instanceof Double || one instanceof Float)
					return one.doubleValue() == two.doubleValue() ? true :
						   one.floatValue() == two.floatValue() ? true : false;
				else
					return one.longValue() == two.longValue() ? true :
						   one.intValue() == two.intValue() ? true :
					       one.shortValue() == two.shortValue() ? true : false;
			}else if(object1 instanceof String) {
				return object1.equals(object2);
			}else {
				return object1.equals(object2);
			} 
		}
		
		return false;
	}
	
	/****************************
	 * ToString
	 ****************************/
	
	@Override
	public String toString() {
		if(object != null)
			return "( ".concat(name).concat(",").concat(object.toString()).concat(" )");
		return "( ".concat(name).concat(",").concat("null").concat(" )");
	}
}
