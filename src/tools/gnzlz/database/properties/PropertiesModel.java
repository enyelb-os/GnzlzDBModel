package tools.gnzlz.database.properties;

import java.text.SimpleDateFormat;

public class PropertiesModel {
	protected boolean refresh;
	protected SimpleDateFormat simpleDateFormat;

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
