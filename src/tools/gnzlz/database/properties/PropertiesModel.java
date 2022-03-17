package tools.gnzlz.database.properties;

import java.text.SimpleDateFormat;

public class PropertiesModel {

	protected String modelPackage;
	protected boolean refresh;
	protected SimpleDateFormat simpleDateFormat;

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

	/*****************
	 * dateFormat
	 *****************/

	public PropertiesModel dateFormat(String format) {
		simpleDateFormat = new SimpleDateFormat(format);
		return this;
	}
}
