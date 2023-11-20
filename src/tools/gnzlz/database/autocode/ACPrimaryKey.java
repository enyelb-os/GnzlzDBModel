package tools.gnzlz.database.autocode;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.model.DBModel;

public class ACPrimaryKey {

	/**
	 * name
	 */
	public final String name;

	/**
	 * keySeq
	 */
	public final String keySeq;

	/**
	 * column
	 */
	final ACColumn column;

	/**
	 * ACPrimaryKey
	 * @param model model
	 * @param column column
	 */
	ACPrimaryKey(DBModel<?> model, ACColumn column) {
		this.column = column;

		this.name = model.get(Definition.PK_NAME).stringValue();
		this.keySeq = model.get(Definition.KEY_SEQ).stringValue();;
	}
}
