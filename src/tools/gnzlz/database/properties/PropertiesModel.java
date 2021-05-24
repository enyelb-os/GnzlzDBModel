package tools.gnzlz.database.properties;

public class PropertiesModel {

	protected String modelPackage;
	protected boolean refresh;

	/*****************
	 * modelPackage
	 *****************/

	public PropertiesModel modelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
		return this;
	}

	/*****************
	 * refresh
	 *****************/

	public PropertiesModel refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}
}
