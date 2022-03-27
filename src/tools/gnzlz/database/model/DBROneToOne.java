package tools.gnzlz.database.model;

import tools.gnzlz.database.query.model.Select;
import tools.gnzlz.database.reflection.DBTableReflection;

public class DBROneToOne<M extends DBModel<M>> {
	
	private String localaKey;
	private Class<M> relationLocal;
	private DBTable table;

	DBROneToOne(String localKey, Class<M> relationLocal) {
		this.relationLocal = relationLocal;
		this.localaKey = localKey;
	}

	Class<?> Class() {
		return relationLocal;
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

	public M hasOne(DBModel<?> modelForeign, DBObject foreignKey) {
		Select<M> select = (Select<M>) Select.create()
				.table(table().table(), "", table().columnsNamesArray())
				.join(modelForeign.table(), foreignKey.name, table().table(), localaKey)
				.where(modelForeign.table()+"."+foreignKey.name, foreignKey.object);
		
		return (M) DBModel.query(table(),select).executeSingle(relationLocal);
	}

	private DBTable table() {
		if(table == null)
			table = DBTableReflection.dbTable(relationLocal);
		return table;
	}
}
