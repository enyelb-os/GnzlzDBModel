package tools.gnzlz.database.data;

import java.util.ArrayList;

public class DBColumn {
	
	protected String name;
	
	protected boolean primaryKey;
	
	protected ArrayList<DBROneToOne> hasOne;
	protected ArrayList<DBROneToMany> hasMany;
	protected ArrayList<DBRManyToMany> belongsToMany;
	
	public DBColumn(String name) {
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
	
	/****************************
	 * PrimaryKey
	 ****************************/
	
	void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	DBColumn primaryKey(boolean primaryKey) {
		setPrimaryKey(primaryKey);
		return this;
	}
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	public boolean primaryKey() {
		return primaryKey;
	}
	
	/****************************
	 * HasOne
	 ****************************/
	
	ArrayList<DBROneToOne> hasOnes() {
		if(hasOne == null) hasOne = new ArrayList<DBROneToOne>();
		return hasOne;
	}
	
	<T extends DBModel<?>> void addHasOne(String column, Class<T> c) {
		DBROneToOne relations = getHasOne(c, column);
		if(relations == null) {
			hasOnes().add(new DBROneToOne(column,c));
		}
	}
	
	<T extends DBModel<?>> DBROneToOne getHasOne(Class<T> c) {
		for (DBROneToOne relations : hasOnes())
			if(relations.isClass(c))
				return relations;
		return null;
	}
	
	<T extends DBModel<?>> DBROneToOne getHasOne(Class<T> c, String column) {
		for (DBROneToOne relations : hasOnes())
			if(relations.isClass(c, column))
				return relations;
		return null;
	}
	
	/****************************
	 * HasMany
	 ****************************/
	
	ArrayList<DBROneToMany> hasManys() {
		if(hasMany == null) hasMany = new ArrayList<DBROneToMany>();
		return hasMany;
	}
	
	<T extends DBModel<?>> void addHasMany(String column, Class<T> c) {
		DBROneToMany relations = getHasMany(c, column);
		if(relations == null) {
			hasManys().add(new DBROneToMany(column,c));
		}
	}
	
	<T extends DBModel<?>> DBROneToMany getHasMany(Class<T> c) {
		for (DBROneToMany relations : hasManys())
			if(relations.isClass(c))
				return relations;
		return null;
	}
	
	<T extends DBModel<?>> DBROneToMany getHasMany(Class<T> c, String column) {
		for (DBROneToMany relations : hasManys())
			if(relations.isClass(c, column))
				return relations;
		return null;
	}
	
	/****************************
	 * BelongsToMany
	 ****************************/
	
	ArrayList<DBRManyToMany> belongsToManys() {
		if(belongsToMany == null) belongsToMany = new ArrayList<DBRManyToMany>();
		return belongsToMany;
	}
	
	<T,I extends DBModel<?>> void addBelongsToMany(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		DBRManyToMany relations = getBelongsToMany(relationInternal, relationForeign);
		if(relations == null) {
			belongsToManys().add(new DBRManyToMany(internalKey1, relationInternal, internalKey2, relationForeign,foreignKey));
		}
	}
	
	<T,I extends DBModel<?>> DBRManyToMany getBelongsToMany(Class<I> internal, Class<T> foreign) {
		for (DBRManyToMany relations : belongsToManys())
			if(relations.isClass(internal,foreign))
				return relations;
		return null;
	}
	
	<T,I extends DBModel<?>> DBRManyToMany getBelongsToMany(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign) {
		for (DBRManyToMany relations : belongsToManys())
			if(relations.isClass(internalKey1, relationInternal, internalKey2, relationForeign))
				return relations;
		return null;
	}
	
	<T,I extends DBModel<?>> DBRManyToMany getBelongsToMany(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		for (DBRManyToMany relations : belongsToManys())
			if(relations.isClass(internalKey1, relationInternal, internalKey2, relationForeign, foreignKey))
				return relations;
		return null;
	}
	
	<T,I extends DBModel<?>> DBRManyToMany getBelongsToMany(Class<I> relationInternal, Class<T> relationForeign, String foreignKey) {
		for (DBRManyToMany relations : belongsToManys())
			if(relations.isClass(relationInternal,relationForeign, foreignKey))
				return relations;
		return null;
	}
	
}
