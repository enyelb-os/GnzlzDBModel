package tools.gnzlz.database.autocode;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.model.DBModel;

public class ACRelation {


	/**
	 * pkName
	 */
	public final String pkName;

	/**
	 * fkName
	 */
	public final String fkName;

	/**
	 * keySeq
	 */
	public final String keySeq;

	/**
	 * updateRule
	 */
	public final String updateRule;

	/**
	 * deleteRule
	 */
	public final String deleteRule;

	/**
	 * deferrability
	 */
	public final String deferrability;

	/**
	 * pkColumn
	 */
	public final ACColumn pkColumn;

	/**
	 * fkColumn
	 */
	public final ACColumn fkColumn;

	/**
	 * ACRelation
	 * @param model model
	 * @param pk pk
	 * @param fk fk
	 */
	ACRelation(DBModel<?> model, ACColumn pk, ACColumn fk) {
		this.pkName = model.get(Definition.PK_NAME).stringValue();
		this.fkName = model.get(Definition.FK_NAME).stringValue();
		this.keySeq = model.get(Definition.KEY_SEQ).stringValue();
		this.updateRule = model.get(Definition.DELETE_RULE).stringValue();
		this.deleteRule = model.get(Definition.DELETE_RULE).stringValue();
		this.deferrability = model.get(Definition.DEFERRABILITY).stringValue();

		this.pkColumn = pk;
		this.fkColumn = fk;
	}

	/**
	 * equals
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof DBModel<?> model){
			return model.get(Definition.PKTABLE_CAT).stringValue().equals(pkColumn.table.scheme.catalog.name) &&
				model.get(Definition.FKTABLE_CAT).stringValue().equals(fkColumn.table.scheme.catalog.name) &&
				model.get(Definition.PKTABLE_SCHEM).stringValue().equals(pkColumn.table.scheme.name) &&
				model.get(Definition.FKTABLE_SCHEM).stringValue().equals(fkColumn.table.scheme.name) &&
				model.get(Definition.PKTABLE_NAME).stringValue().equals(pkColumn.table.name) &&
				model.get(Definition.FKTABLE_NAME).stringValue().equals(fkColumn.table.name) &&
				model.get(Definition.PKCOLUMN_NAME).stringValue().equals(pkColumn.name) &&
				model.get(Definition.FKCOLUMN_NAME).stringValue().equals(fkColumn.name);
		}else if(o instanceof ACRelation relation){
			return relation.pkColumn.table.scheme.catalog.name.equals(pkColumn.table.scheme.catalog.name) &&
				relation.fkColumn.table.scheme.catalog.name.equals(fkColumn.table.scheme.catalog.name) &&
				relation.pkColumn.table.scheme.name.equals(pkColumn.table.scheme.name) &&
				relation.fkColumn.table.scheme.name.equals(fkColumn.table.scheme.name) &&
				relation.pkColumn.table.name.equals(pkColumn.table.name) &&
				relation.fkColumn.table.name.equals(fkColumn.table.name) &&
				relation.pkColumn.name.equals(pkColumn.name) &&
				relation.fkColumn.name.equals(fkColumn.name);
		} else {
			return super.equals(o);
		}
	}
}
