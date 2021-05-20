package tools.gnzlz.database.model;

import java.util.ArrayList;

import tools.gnzlz.database.query.Select;

public class DBROneToOne {
	
	private String localaKey;
	private Class<?> relationLocal;
	private DBModel<?> modelLocal;
	
	<T extends DBModel<?>> DBROneToOne(String localKey, Class<T> relationLocal) {
		this.relationLocal = relationLocal;
		this.localaKey = localKey;
	}
	
	public <T extends DBModel<?>> boolean isClass(Class<T> c) {
		return relationLocal.getName().equals(c.getName());
	}
	
	public <T extends DBModel<?>> boolean isClass(Class<T> c, String column) {
		return relationLocal.getName().equals(c.getName()) && localaKey.equalsIgnoreCase(column);
	}

	public DBModel<?> hasOne(DBModel<?> modelForeign, DBObject foreignKey) {
		Select select = Select.create()
				.table(modelLocal().table(), "", modelLocal().columnsNamesArray())
				.join(modelForeign.table(), foreignKey.name, modelLocal().table(), localaKey)
				.where(modelForeign.table()+"."+foreignKey.name, foreignKey.object);
		
		return (DBModel<?>) modelLocal().query(select).executeSingle(modelLocal().getClass());
	}
	
	private <T extends DBModel<?>> DBModel<?> modelLocal() {
		if(modelLocal == null) modelLocal = DBModel.create((Class<T>) relationLocal);
		return modelLocal;
	}
}
