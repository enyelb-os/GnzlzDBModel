package tools.gnzlz.database.autocode.model;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import tools.gnzlz.database.autocode.ACFormat;
import tools.gnzlz.database.model.DBModel;

public class ACTable {

	/****************************
	 * properties
	 ****************************/

	public static String TABLE_NAME = "TABLE_NAME";
	public static String TABLE_TYPE = "TABLE_TYPE";
	public static String REMARKS = "REMARKS";
	public static String TYPE_CAT = "TYPE_CAT";
	public static String TYPE_SCHEM = "TYPE_SCHEM";
	public static String TYPE_NAME = "TYPE_NAME";
	public static String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";
	public static String REF_GENERATION = "REF_GENERATION";

	/****************************
	 * vars
	 ****************************/

	public final String name;
	public final String type;
	public final String remarks;
	public final String typeCatalog;
	public final String typeScheme;
	public final String typeName;
	public final ArrayList<ACColumn> columns;

	public final String packegeName;

	/****************************
	 * ref
	 ****************************/

	public final ACScheme scheme;

	/****************************
	 * constructor
	 ****************************/

	ACTable(DBModel model, ACScheme scheme) {
		this.scheme = scheme;

		packegeName = "";

		this.name = model.get(TABLE_NAME).stringValue();
		this.type = model.get(TABLE_TYPE).stringValue();
		this.remarks = model.get(REMARKS).stringValue();
		this.typeCatalog = model.get(TYPE_CAT).stringValue();
		this.typeScheme = model.get(TYPE_SCHEM).stringValue();
		this.typeName = model.get(TYPE_NAME).stringValue();

		columns = new ArrayList<ACColumn>();
	}

	/***********************
	 * table
	 ***********************/

	public String packegeName() {
		return packegeName;
	}
	
	public String nameCamelCase() {
		return ACFormat.camelCaseClass(name);
	}

	public String nameCamelCaseClass() {
		return nameCamelCase().concat(".class");
	}

	/***********************
	 * Get
	 ***********************/
	
	public ACColumn column(DBModel name) {
		if(name != null) {
			for (ACColumn column : columns) {
				if (column.equals(name)) {
					return column;
				}
			}
		}
		return null;
	}

	/***********************
	 * Get
	 ***********************/

	public ACColumn column(String name) {
		if(name != null) {
			for (ACColumn column : columns) {
				if (column.name.equals(name)) {
					return column;
				}
			}
		}
		return null;
	}
	
	/***********************
	 * initColumns
	 ***********************/
	
	public void addColumns(ArrayList<DBModel<?>> columns, ArrayList<DBModel<?>> primaryKeys) {
		for (DBModel<?> columnModel : columns) {
			String columnName = columnModel.get(ACColumn.COLUMN_NAME).stringValue();
			DBModel primaryKeyModel = null;
			for (DBModel primary: primaryKeys) {
				if(primary.get(ACColumn.COLUMN_NAME).stringValue().equals(columnName)){
					primaryKeyModel = primary;
				}
			}

			ACColumn column = column(columnModel);
			if(column == null) {
				column = new ACColumn(columnModel, primaryKeyModel, this);
				this.columns.add(column);
			}
		}

		//addRelation(foreignKeys);
	}
	
	/***********************
	 * pkColumns
	 ***********************/

	ACColumn pkColumn(DBModel model){
		for (ACCatalog catalog : this.scheme.catalog.dataBase.catalogs) {
			if(catalog.name.equals(model.get(ACRelation.PKTABLE_CAT).stringValue())) {
				for (ACScheme scheme : this.scheme.catalog.schemes) {
					if(scheme.name.equals(model.get(ACRelation.PKTABLE_SCHEM).stringValue())) {
						for (ACTable table : scheme.tables) {
							if(table.name.equals(model.get(ACRelation.PKTABLE_NAME).stringValue())) {
								for (ACColumn column : table.columns) {
									if(column.name.equals(model.get(ACRelation.PKCOLUMN_NAME).stringValue())) {
										return column;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	/***********************
	 * createRelations
	 ***********************/
	
	public void addRelation(ArrayList<DBModel<?>> list) {
		for (DBModel<?> model : list) {
			String columnName = model.get(ACRelation.FKCOLUMN_NAME).stringValue();
			ACColumn fkColumn = column(columnName);
			ACColumn pkColumn = pkColumn(model);

			ACRelation relation = new ACRelation(model,pkColumn,fkColumn);

			pkColumn.addRelation(relation);
			fkColumn.addRelation(relation);
		}
	}


	
	/***********************
	 * intHasMany
	 ***********************/
	
	void addHasMany(ArrayList<DBModel<?>> list) {
		for (DBModel<?> dbModel : list) {
			String column = dbModel.get("PKCOLUMN_NAME").stringValue();
			ACColumn acColumn = column(column);
			/*acColumn.addHasMany(
				dbModel.get("FKCOLUMN_NAME").stringValue(),
				dbModel.get("FKTABLE_NAME").stringValue(),
				dbModel.get("FK_NAME").stringValue(),
				dbModel.get("UPDATE_RULE").intValue(),
				dbModel.get("DELETE_RULE").intValue()
			);*/
		}
	}
	
	/***********************
	 * intBelongsToMany
	 ***********************/
	
	void addBelongsToMany(ArrayList<DBModel<?>> list) {
		for (DBModel<?> dbModel : list) {
			String column = dbModel.get("PKCOLUMN_NAME").stringValue();
			ACColumn acColumn = column(column);
			/*acColumn.addBelongsToMany(
				dbModel.get("ICOLUMN_NAME1").stringValue(), 
				dbModel.get("ITABLE_NAME").stringValue(),
				dbModel.get("ICOLUMN_NAME2").stringValue(),
				dbModel.get("FKTABLE_NAME").stringValue(),
				dbModel.get("FKCOLUMN_NAME").stringValue()
			);*/
		}
	}

	/***********************
	 * PrimaryKey
	 ***********************/
	
	public ACColumn primaryKey() {
		for (ACColumn acColumn : columns) {
			if (acColumn.isPrimaryKey()) {
				return acColumn;
			}
		}
		return null;
	}

	/***********************
	 * PrimaryKeyNeme
	 ***********************/

	public String primaryKeyName() {
		for (ACColumn acColumn : columns) {
			if (acColumn.isPrimaryKey()) {
				return acColumn.name;
			}
		}
		return "";
	}

	/***********************
	 * Imports
	 ***********************/

	public ArrayList<ACRelation> hasOneNameImports() {
		ArrayList<ACRelation> relations = new ArrayList<>();
		for (ACColumn column : this.columns) {
			for (ACRelation relation : column.hasOne()) {
				if (hasOneExists(relations, relation))
					relations.add(relation);
			}
		}
		return relations;
	}

	/***********************
	 * Imports
	 ***********************/

	public boolean hasOneExists(ArrayList<ACRelation> relations, ACRelation relation) {
		for (ACRelation relation2 : relations) {
			if (relation.pkColumn.table.name.equals(relation2.pkColumn.table.name))
				return false;
		}
		return true;
	}

}
