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

	PropertiesModel internalPackage(String modelPackage) {
		return modelPackage(this.getClass().getPackage().getName()+"."+modelPackage);
	}

	public String modelPackage() {
		return modelPackage;
	}

	/*****************
	 * refresh
	 *****************/

	public PropertiesModel refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}

	public boolean refresh() {
		return refresh;
	}
}
