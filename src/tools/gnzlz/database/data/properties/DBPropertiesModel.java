package tools.gnzlz.database.data.properties;

public class DBPropertiesModel {

	protected String modelPackage;
	protected boolean refresh;

	/*****************
	 * modelPackage
	 *****************/

	public DBPropertiesModel modelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
		return this;
	}

	public DBPropertiesModel internalPackage(String modelPackage) {
		return modelPackage(this.getClass().getPackage().getName()+"."+modelPackage);
	}

	public String modelPackage() {
		return modelPackage;
	}

	/*****************
	 * refresh
	 *****************/

	public DBPropertiesModel refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}

	public boolean refresh() {
		return refresh;
	}
}
