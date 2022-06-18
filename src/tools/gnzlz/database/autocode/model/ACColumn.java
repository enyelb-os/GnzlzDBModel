package tools.gnzlz.database.autocode.model;

import tools.gnzlz.database.autocode.ACFormat;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ACColumn {

	/****************************
	 * properties
	 ****************************/

	public static String COLUMN_NAME = "COLUMN_NAME";
	public static String DATA_TYPE = "DATA_TYPE";
	public static String TYPE_NAME = "TYPE_NAME";
	public static String COLUMN_SIZE = "COLUMN_SIZE";
	public static String BUFFER_LENGTH = "BUFFER_LENGTH";
	public static String DECIMAL_DIGITS = "DECIMAL_DIGITS";
	public static String NULLABLE = "NULLABLE";
	public static String REMARKS = "REMARKS";
	public static String COLUMN_DEF = "COLUMN_DEF";
	public static String SQL_DATA_TYPE = "SQL_DATA_TYPE";
	public static String SQL_DATETIME_SUB = "SQL_DATETIME_SUB";
	public static String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";
	public static String ORDINAL_POSITION = "ORDINAL_POSITION";
	public static String IS_NULLABLE = "IS_NULLABLE";
	public static String SCOPE_CATALOG = "SCOPE_CATALOG";
	public static String SCOPE_SCHEMA = "SCOPE_SCHEMA";
	public static String SCOPE_TABLE = "SCOPE_TABLE";
	public static String SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";
	public static String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
	public static String IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

	/****************************
	 * vars
	 ****************************/
	
	public final String name;
	public final String type;
	public final String length;
	public final boolean nullable;
	public final String def;
	public final boolean autoincrement;
	public final ACPrimaryKey primaryKey;
	public final ArrayList<ACRelation> relations;

	/****************************
	 * ref
	 ****************************/

	public final ACTable table;

	/****************************
	 * constructor
	 ****************************/
	
	ACColumn(DBModel model, DBModel primaryKey, ACTable table) {
		this.table = table;

		this.name = model.get(COLUMN_NAME).stringValue();
		this.type = model.get(TYPE_NAME).stringValue();
		this.length = model.get(COLUMN_SIZE).stringValue();
		this.def = model.get(COLUMN_DEF).stringValue();
		this.nullable = model.get(IS_NULLABLE).stringValue().equalsIgnoreCase("YES");
		this.autoincrement = model.get(IS_AUTOINCREMENT).stringValue().equalsIgnoreCase("YES");
		if(primaryKey != null){
			this.primaryKey = new ACPrimaryKey(primaryKey, this);
		}else{
			this.primaryKey = null;
		}
		this.relations = new ArrayList<ACRelation>();
	}
	
	/****************************
	 * Name
	 ****************************/
	
	public String nameCamelCase() {
		return ACFormat.camelCaseMethod(ACFormat.beginValidNumber(name));
	}

	public String nameUpperCase() {
		return ACFormat.beginValidNumber(name).toUpperCase();
	}

	public String tableColumnUpperCase() {
		return table.nameCamelCase().concat(".").concat(nameUpperCase());
	}

	/****************************
	 * PrimaryKey
	 ****************************/

	public boolean isPrimaryKey() {
		return primaryKey != null;
	}

	/****************************
	 * addRelation
	 ****************************/
	
	void addRelation(ACRelation relation) {
		ACRelation existrelations = relation(relation);
		if(existrelations == null) {
			relations.add(relation);
		}
	}
	
	ACRelation relation(ACRelation relation) {
		for (ACRelation relations : relations)
			if(relations.equals(relation))
				return relations;
		return null;
	}

	/***********************
	 * HasOne
	 ***********************/

	public ArrayList<ACRelation> hasOne() {
		return relations.stream().filter(relation -> {
			if(relation.fkColumn == this) return true;
			return false;
		}).collect(Collectors.toCollection(ArrayList::new));
	}

	/***********************
	 * HasMany
	 ***********************/

	public ArrayList<ACManyToMany> belongsToManys() {
		ArrayList<ACManyToMany> manyToManies = new ArrayList<ACManyToMany>();
		hasMany().forEach(relation1 -> {
			relation1.fkColumn.table.columns.forEach(column -> {
				if(column != relation1.fkColumn){
					column.hasOne().forEach(relation2 -> {
						manyToManies.add(new ACManyToMany(relation1,relation2));
					});
				}
			});
		});
		return manyToManies;
	}

	/***********************
	 * HasMany
	 ***********************/

	public ArrayList<ACRelation> hasMany() {
		return relations.stream().filter(relation -> {
			if(relation.pkColumn == this) return true;
			return false;
		}).collect(Collectors.toCollection(ArrayList::new));
	}

	/****************************
	 * Override
	 ****************************/

	@Override
	public boolean equals(Object o) {
		if(o instanceof DBModel){
			DBModel model = ((DBModel)o);
			if(model.get(COLUMN_NAME).stringValue().equals(name)){
				return true;
			} else {
				return false;
			}
		}else{
			super.equals(o);
		}
		return false;
	}
}
