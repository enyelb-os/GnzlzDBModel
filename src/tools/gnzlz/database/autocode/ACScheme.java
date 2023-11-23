package tools.gnzlz.database.autocode;

import tools.gnzlz.database.autocode.definition.Definition;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;

public class ACScheme {

	/**
	 * name
	 */
	public final String name;

	/**
	 * tables
	 */
	public final ArrayList<ACTable> tables;

	/**
	 * catalog
	 */
	public final ACCatalog catalog;

	/**
	 * ACScheme
	 * @param model model
	 * @param catalog catalog
	 */
	ACScheme(DBModel<?> model, ACCatalog catalog) {
		this.catalog = catalog;
		this.name = model.get(Definition.TABLE_SCHEM).stringValue();
		this.tables = new ArrayList<>();
	}

	/**
	 * nameDefault
	 */
	public String nameDefault() {
		return this.name.isEmpty() ? "default" : this.name;
	}

	/**
	 * equals
	 */
	@Override
	public boolean equals(Object o) {
		if (catalog.equals(o) && o instanceof DBModel model) {
			return model.get(Definition.TABLE_SCHEM).stringValue().equals(name);
		} else {
			return super.equals(o);
		}
	}
}
