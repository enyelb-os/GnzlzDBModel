package tools.gnzlz.database.autocode;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class
ACColumn {

	/**
	 * name
	 */
	public final String name;

	/**
	 * type
	 */
	public final ACType type;

	/**
	 * length
	 */
	public final String length;

	/**
	 * nullable
	 */
	public final boolean nullable;

	/**
	 * def
	 */
	public final String def;

	/**
	 * autoincrement
	 */
	public final boolean autoincrement;

	/**
	 * primaryKey
	 */
	public final ACPrimaryKey primaryKey;

	/**
	 * relations
	 */
	public final ArrayList<ACRelation> relations;

	/**
	 * table
	 */

	public final ACTable table;

	/**
	 * ACColumn
	 * @param model model
	 * @param primaryKey primaryKey
	 * @param table table
	 */
	ACColumn(DBModel<?> model, DBModel<?> primaryKey, ACTable table) {
		this.table = table;

		this.name = model.get(Definition.COLUMN_NAME).stringValue();
		this.type = new ACType(model);
		this.length = model.get(Definition.COLUMN_SIZE).stringValue();
		this.def = model.get(Definition.COLUMN_DEF).stringValue();
		this.nullable = model.get(Definition.IS_NULLABLE).stringValue().equalsIgnoreCase("YES");
		this.autoincrement = model.get(Definition.IS_AUTOINCREMENT).stringValue().equalsIgnoreCase("YES");
		if(primaryKey != null){
			this.primaryKey = new ACPrimaryKey(primaryKey, this);
		}else{
			this.primaryKey = null;
		}
		this.relations = new ArrayList<>();
	}

	/**
	 * isPrimaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey != null;
	}

	/**
	 * defaultValue
	 */
	public String defaultValue() {

		if(type.type.equalsIgnoreCase("DATETIME") || type.type.equalsIgnoreCase("DATE") ||
				type.type.equalsIgnoreCase("TIMESTAMP")  || type.type.equalsIgnoreCase("VARCHAR") ||
				type.type.equalsIgnoreCase("LONGVARCHAR") || type.type.equalsIgnoreCase("CLOB"))
			return "'" + def + "'";

		return def;
	}

	/****************************
	 * addRelation
	 ****************************/
	
	void addRelation(ACRelation relation) {
		if(relation(relation) == null && relation != null) {
			relations.add(relation);
		}
	}

	/**
	 * relation
	 * @param relation relation
	 */
	ACRelation relation(ACRelation relation) {
		for (ACRelation relations : relations)
			if(relations.equals(relation))
				return relations;
		return null;
	}

	/**
	 * oneToOne
	 */
	public ArrayList<ACRelation> oneToOne() {
		return relations.stream().filter(relation -> relation.fkColumn == this).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * belongsToManys
	 */
	public ArrayList<ACManyToMany> belongsToMany() {
		ArrayList<ACManyToMany> manyToMany = new ArrayList<ACManyToMany>();
		oneToMany().forEach(relation1 -> {
			relation1.fkColumn.table.columns.forEach(column -> {
				if(column != relation1.fkColumn){
					column.oneToOne().forEach(relation2 -> {
						manyToMany.add(new ACManyToMany(relation1,relation2));
					});
				}
			});
		});
		return manyToMany;
	}

	/**
	 * hasMany
	 */
	public ArrayList<ACRelation> oneToMany() {
		return relations.stream().filter(relation -> relation.pkColumn == this).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * equals
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof DBModel) {
			return ((DBModel<?>)o).get(Definition.COLUMN_NAME).stringValue().equals(name);
		} else {
			return super.equals(o);
		}
	}
}
