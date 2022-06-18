package tools.gnzlz.database.autocode.model;

import tools.gnzlz.database.model.DBModel;

public class ACRelation {

	/****************************
	 * onAction
	 ****************************/

	public static int SET_NULL = 1;
	public static int RESTRICT = 1;
	public static int NO_ACTION = 1;
	public static int CASCADE = 1;

	/****************************
	 * properties
	 ****************************/

	public static String PKTABLE_CAT = "PKTABLE_CAT";
	public static String PKTABLE_SCHEM = "PKTABLE_SCHEM";
	public static String PKTABLE_NAME = "PKTABLE_NAME";
	public static String PKCOLUMN_NAME = "PKCOLUMN_NAME";
	public static String PK_NAME = "PK_NAME";
	public static String FKTABLE_CAT = "FKTABLE_CAT";
	public static String FKTABLE_SCHEM = "FKTABLE_SCHEM";
	public static String FKTABLE_NAME = "FKTABLE_NAME";
	public static String FKCOLUMN_NAME = "FKCOLUMN_NAME";
	public static String FK_NAME = "FK_NAME";
	public static String KEY_SEQ = "KEY_SEQ";
	public static String UPDATE_RULE = "UPDATE_RULE";
	public static String DELETE_RULE = "DELETE_RULE";
	public static String DEFERRABILITY = "DEFERRABILITY";

	/****************************
	 * vars
	 ****************************/

	public final String pkName;
	public final String fkName;
	public final String keySeq;
	public final String updateRule;
	public final String deleteRule;
	public final String deferrability;

	/****************************
	 * ref
	 ****************************/

	public final ACColumn pkColumn;
	public final ACColumn fkColumn;

	/****************************
	 * Constructor
	 ****************************/

	ACRelation(DBModel model, ACColumn pk, ACColumn fk) {
		this.pkName = model.get(PK_NAME).stringValue();
		this.fkName = model.get(FK_NAME).stringValue();
		this.keySeq = model.get(KEY_SEQ).stringValue();
		this.updateRule = model.get(DELETE_RULE).stringValue();
		this.deleteRule = model.get(DELETE_RULE).stringValue();
		this.deferrability = model.get(DEFERRABILITY).stringValue();

		this.pkColumn = pk;
		this.fkColumn = fk;
	}

	/****************************
	 * Override
	 ****************************/

	@Override
	public boolean equals(Object o) {
		if(o instanceof DBModel){
			DBModel model = ((DBModel)o);
			if(model.get(PKTABLE_CAT).stringValue().equals(pkColumn.table.scheme.catalog.name) &&
				model.get(FKTABLE_CAT).stringValue().equals(fkColumn.table.scheme.catalog.name) &&
				model.get(PKTABLE_SCHEM).stringValue().equals(pkColumn.table.scheme.name) &&
				model.get(FKTABLE_SCHEM).stringValue().equals(fkColumn.table.scheme.name) &&
				model.get(PKTABLE_NAME).stringValue().equals(pkColumn.table.name) &&
				model.get(FKTABLE_NAME).stringValue().equals(fkColumn.table.name) &&
				model.get(PKCOLUMN_NAME).stringValue().equals(pkColumn.name) &&
				model.get(FKCOLUMN_NAME).stringValue().equals(fkColumn.name)){
				return true;
			} else {
				return false;
			}
		}else if(o instanceof ACRelation){
			ACRelation relation = ((ACRelation)o);
			if(relation.pkColumn.table.scheme.catalog.name.equals(pkColumn.table.scheme.catalog.name) &&
				relation.fkColumn.table.scheme.catalog.name.equals(fkColumn.table.scheme.catalog.name) &&
				relation.pkColumn.table.scheme.name.equals(pkColumn.table.scheme.name) &&
				relation.fkColumn.table.scheme.name.equals(fkColumn.table.scheme.name) &&
				relation.pkColumn.table.name.equals(pkColumn.table.name) &&
				relation.fkColumn.table.name.equals(fkColumn.table.name) &&
				relation.pkColumn.name.equals(pkColumn.name) &&
				relation.fkColumn.name.equals(fkColumn.name)){
				return true;
			} else {
				return false;
			}
		}
		else{
			super.equals(o);
		}
		return false;
	}
}
