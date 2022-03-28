package tools.gnzlz.database.autocode.migration;

import tools.gnzlz.database.autocode.ACFormat;

import java.util.ArrayList;

public class ACColumn {
	
	protected String name;
	protected boolean primaryKey;
	protected boolean autoincrement;
	protected boolean nullable;
	protected String type;
	protected String size;
	protected String isDefault;
	
	protected ArrayList<ACRelation> hasOne;
	
	ACColumn(String name) {
		this.name = name;
	}
	
	/****************************
	 * Name
	 ****************************/
	
	public String name() {
		return name;
	}
	
	public String nameCamelCase() {
		return ACFormat.camelCaseMethod(ACFormat.beginValidNumber(name));
	}
	
	public String nameUpperCase() {
		return ACFormat.beginValidNumber(name).toUpperCase();
	}
	
	/****************************
	 * PrimaryKey
	 ****************************/
	
	ACColumn primaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}
	
	boolean primaryKey() {
		return primaryKey;
	}

	/****************************
	 * AutoIncrement
	 ****************************/

	ACColumn autoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
		return this;
	}

	boolean autoincrement() {
		return autoincrement;
	}

	/****************************
	 * Nullable
	 ****************************/

	ACColumn nullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	boolean nullable() {
		return nullable;
	}
	
	/****************************
	 * Type
	 ****************************/

	ACColumn type(String type) {
		this.type = type;
		return this;
	}
	
	String type() {
		return type;
	}

	/****************************
	 * size
	 ****************************/

	ACColumn size(String size) {
		this.size = size;
		return this;
	}

	String size() {
		return size;
	}

	/****************************
	 * isDefault
	 ****************************/

	ACColumn isDefault(String isDefault) {
		this.isDefault = isDefault;
		return this;
	}

	String isDefault() {
		return isDefault;
	}
	
	/****************************
	 * HasOne
	 ****************************/
	
	ArrayList<ACRelation> hasOnes() {
		if(hasOne == null) hasOne = new ArrayList<ACRelation>();
		return hasOne;
	}
	
	void addHasOne(String column, String c) {
		ACRelation relations = getHasOne(c, column);
		if(relations == null) {
			hasOnes().add(new ACRelation(column,c));
		}
	}
	
	ACRelation getHasOne(String c, String column) {
		for (ACRelation relations : hasOnes())
			if(relations.relation().equalsIgnoreCase(c) && relations.column().equalsIgnoreCase(column))
				return relations;
		return null;
	}
}
