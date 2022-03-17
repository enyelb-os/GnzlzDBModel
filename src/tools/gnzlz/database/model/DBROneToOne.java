package tools.gnzlz.database.model;

import tools.gnzlz.database.query.model.Select;

public class DBROneToOne {
	
	private String localaKey;
	private Class<?> relationLocal;
	private DBModel<?> modelLocal;
	
	<T extends DBModel<?>> DBROneToOne(String localKey, Class<T> relationLocal) {
		this.relationLocal = relationLocal;
		this.localaKey = localKey;
	}

	Class<?> Class() {
		return relationLocal;
	}


	<T extends DBModel<?>> DBModel<?> modelLocal() {
		if(modelLocal == null) modelLocal = DBModel.create((Class<T>) relationLocal);
		return modelLocal;
	}

	String localaKey() {
		return localaKey;
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
}
