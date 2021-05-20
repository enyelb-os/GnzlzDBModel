package tools.gnzlz.database.autocode;

import java.util.ArrayList;

public class ACColumn {
	
	protected String name;
	
	protected boolean primaryKey;
	protected String type;
	
	protected ArrayList<ACRelation> hasOne;
	protected ArrayList<ACRelation> hasMany;
	protected ArrayList<ACManyToMany> belongsToMany;
	
	ACColumn(String name) {
		this.name = name;
	}
	
	/****************************
	 * Name
	 ****************************/
	
	public String getName() {
		return name;
	}
	
	public String name() {
		return name;
	}
	
	public String nameCamelCase() {
		return ACFormat.camelCaseMethod(name);
	}
	
	public String nameUpperCase() {
		return name.toUpperCase();
	}
	
	/****************************
	 * PrimaryKey
	 ****************************/
	
	void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	ACColumn primaryKey(boolean primaryKey) {
		setPrimaryKey(primaryKey);
		return this;
	}
	
	boolean isPrimaryKey() {
		return primaryKey;
	}
	
	boolean primaryKey() {
		return primaryKey;
	}
	
	/****************************
	 * Type
	 ****************************/
	
	void setType(String type) {
		this.type = type;
	}
	
	ACColumn type(String type) {
		setType(type);
		return this;
	}
	
	String getType() {
		return type;
	}
	
	String type() {
		return type;
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
	
	/****************************
	 * HasMany
	 ****************************/
	
	ArrayList<ACRelation> hasManys() {
		if(hasMany == null) hasMany = new ArrayList<ACRelation>();
		return hasMany;
	}
	
	void addHasMany(String column, String c) {
		ACRelation relations = getHasMany(c,column);
		if(relations == null) {
			hasManys().add(new ACRelation(column,c));
		}
	}
	
	ACRelation getHasMany(String c, String column) {
		for (ACRelation relations : hasManys())
			if(relations.relation().equalsIgnoreCase(c) && relations.column().equalsIgnoreCase(column))
				return relations;
		return null;
	}
	
	/****************************
	 * BelongsToMany
	 ****************************/
	
	ArrayList<ACManyToMany> belongsToManys() {
		if(belongsToMany == null) belongsToMany = new ArrayList<ACManyToMany>();
		return belongsToMany;
	}
	
	void addBelongsToMany(String internalKey1, String relationInternal, String internalKey2, String relationForeign, String foreignKey) {
		ACManyToMany relations = getBelongsToMany(internalKey1, relationInternal, internalKey2, relationForeign, foreignKey);
		if(relations == null) {
			belongsToManys().add(new ACManyToMany(internalKey1, relationInternal, internalKey2, relationForeign, foreignKey));
		}
	}
	
	ACManyToMany getBelongsToMany(String internalKey1, String relationInternal, String internalKey2, String relationForeign, String foreignKey) {
		for (ACManyToMany relations : belongsToManys())
			if(relations.relationInternal().equalsIgnoreCase(relationInternal) && relations.relationForeign().equalsIgnoreCase(relationForeign) &&
			   relations.internalKey1().equalsIgnoreCase(internalKey1) && relations.internalKey2().equalsIgnoreCase(internalKey2) && relations.foreignKey().equalsIgnoreCase(foreignKey))
				return relations;
		return null;
	}

}
