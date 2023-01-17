package tools.gnzlz.database.properties;

import java.text.SimpleDateFormat;

public class PTModel {

	protected PropertiesModel model;

	public PTModel(PropertiesModel model){
		this.model = model;
	}

	/*****************
	 * refresh
	 *****************/

	public boolean refresh() {
		return model.refresh;
	}

	/*****************
	 * dateFormat
	 *****************/

	public SimpleDateFormat dateFormat() {
		if(model.simpleDateFormat == null)
			model.simpleDateFormat = new SimpleDateFormat();
		return model.simpleDateFormat;
	}
}
