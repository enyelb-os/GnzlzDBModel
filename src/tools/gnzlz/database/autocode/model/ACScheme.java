package tools.gnzlz.database.autocode.model;

import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;

public class ACScheme {

	/****************************
	 * properties
	 ****************************/

	public static String TABLE_SCHEM = "TABLE_SCHEM";

	/****************************
	 * vars
	 ****************************/

	public final String name;
	public final ArrayList<ACTable> tables;

	/****************************
	 * ref
	 ****************************/

	public final ACCatalog catalog;

	/****************************
	 * constructor
	 ****************************/

	ACScheme(DBModel scheme, ACCatalog catalog) {
		this(scheme.get(TABLE_SCHEM).stringValue(), catalog);
	}

	/****************************
	 * constructor
	 ****************************/

	ACScheme(String scheme, ACCatalog catalog) {
		this.catalog = catalog;

		this.name = scheme;
		this.tables = new ArrayList<ACTable>();
	}

	/***********************
	 * default
	 ***********************/

	public String nameDefault() {
		return this.name.equals("") ? "default" : this.name;
	}

	/***********************
	 * method
	 ***********************/

	public ACTable addTable(DBModel dbModel){
		for (ACTable table : tables) {
			if(table.equals(dbModel)){
				return table;
			}
		}
		ACTable table = new ACTable(dbModel, this);
		tables.add(table);
		return table;
	}

	/****************************
	 * Override
	 ****************************/

	@Override
	public boolean equals(Object o) {
		if(o instanceof DBModel){
			DBModel model = ((DBModel)o);
			if(model.get(TABLE_SCHEM).stringValue().equals(name) && catalog.equals(model)){
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
