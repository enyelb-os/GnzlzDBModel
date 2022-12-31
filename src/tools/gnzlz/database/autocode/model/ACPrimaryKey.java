package tools.gnzlz.database.autocode.model;

import tools.gnzlz.database.model.DBModel;

public class ACPrimaryKey {

	/****************************
	 * properties
	 ****************************/

	public static String KEY_SEQ = "KEY_SEQ";
	public static String PK_NAME = "PK_NAME";

	/****************************
	 * vars
	 ****************************/

	public final String name;
	public final String keySeq;

	/****************************
	 * ref
	 ****************************/

	public final ACColumn column;

	/****************************
	 * constructor
	 ****************************/


	ACPrimaryKey(DBModel model, ACColumn column) {
		this.column = column;

		this.name = model.get(PK_NAME).stringValue();
		this.keySeq = model.get(KEY_SEQ).stringValue();;
	}
}
