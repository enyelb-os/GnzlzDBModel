package tools.gnzlz.database.autocode.model;

import tools.gnzlz.database.autocode.ACFormat;
import tools.gnzlz.database.model.DBModel;

import java.util.ArrayList;

public class ACCatalog {

	/****************************
	 * properties
	 ****************************/

	public static String TABLE_CAT = "TABLE_CAT";

	/****************************
	 * vars
	 ****************************/

	public final String name;
	public final ArrayList<ACScheme> schemes;

	/****************************
	 * ref
	 ****************************/

	public final ACDataBase dataBase;

	/****************************
	 * constructor
	 ****************************/

	ACCatalog(DBModel catalog, ACDataBase dataBase) {
		this.dataBase = dataBase;
		this.name = catalog.get(TABLE_CAT).stringValue();
		this.schemes = new ArrayList();
	}

	/***********************
	 * method
	 ***********************/
	
	public String nameCamelCase() {
		return ACFormat.camelCaseClass(name);
	}

	/***********************
	 * method
	 ***********************/

	public ACScheme addScheme(DBModel dbModel){
		for (ACScheme scheme : schemes) {
			if(scheme.equals(dbModel)){
				return scheme;
			}
		}
		ACScheme scheme = new ACScheme(dbModel, this);
		schemes.add(scheme);
		return scheme;
	}

	/****************************
	 * Override
	 ****************************/

	@Override
	public boolean equals(Object o) {
		if(o instanceof DBModel){
			DBModel model = ((DBModel)o);
			if(model.get(TABLE_CAT).stringValue().equals(name) || model.get("table_catalog").equals(name)){
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
