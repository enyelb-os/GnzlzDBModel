package tools.gnzlz.database.model;

import java.text.NumberFormat;

public class DBCurrency {
	
	protected NumberFormat format;
	protected DBObject dbObject;
	
	public DBCurrency(Object object, NumberFormat format) {
		this("Currency", object, format);
	}
	
	public DBCurrency(String name, Object object,NumberFormat format) {
		this.dbObject = new DBObject(name, object, null);
		this.format = format;
	}
	
	public DBObject dbObject() {
		return dbObject;
	}
	
	public String currency(NumberFormat format) {
		return dbObject.currencyValue(format);
	}
	
	public String currency() {
		return dbObject.currencyValue(format);
	}
}
