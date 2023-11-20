package tools.gnzlz.database.autocode;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;

public class ACCatalog {

	/**
	 * name
	 */
	public final String name;

	/**
	 * schemes
	 */
	public final ArrayList<ACScheme> schemes;

	/**
	 * dataBase
	 */
	protected final ACDataBase dataBase;

	/**
	 * configuration
	 * @param catalog c
	 * @param dataBase d
	 */
	ACCatalog(DBModel<?> catalog, ACDataBase dataBase) {
		this.dataBase = dataBase;
		this.name = catalog.get(Definition.TABLE_CAT).stringValue();
		this.schemes = new ArrayList<>();
	}

	/**
	 * equals
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof DBModel model) {
			return model.get(Definition.TABLE_CAT).stringValue().equals(name) || model.get("table_catalog").stringValue().equals(name);
		} else {
			return super.equals(o);
		}
	}

}
