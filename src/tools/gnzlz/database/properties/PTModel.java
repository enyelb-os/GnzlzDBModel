package tools.gnzlz.database.properties;

public class PTModel {

	protected PropertiesModel model;

	public PTModel(PropertiesModel model){
		this.model = model;
	}

	/*****************
	 * modelPackage
	 *****************/

	public String modelPackage() {
		return model.modelPackage;
	}

	/*****************
	 * refresh
	 *****************/

	public boolean refresh() {
		return model.refresh;
	}
}
