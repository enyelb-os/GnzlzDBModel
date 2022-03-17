package tools.gnzlz.database.model;

import java.util.ArrayList;

import tools.gnzlz.database.query.model.Select;

public class DBROneToMany {
	
	private String foreignKey;
	private Class<?> relationForeign;
	private DBModel<?> modelForeign;
	
	<T extends DBModel<?>> DBROneToMany(String foreignKey, Class<T> relationForeign) {
		this.relationForeign = relationForeign;
		this.foreignKey = foreignKey;
	}
	
	public <T extends DBModel<?>> boolean isClass(Class<T> c) {
		return relationForeign.getName().equals(c.getName());
	}
	
	public <T extends DBModel<?>> boolean isClass(Class<T> c,String column) {
		return relationForeign.getName().equals(c.getName()) && foreignKey.equalsIgnoreCase(column) ;
	}
	
	public ArrayList<DBModel<?>> hasMany(DBModel<?> modelLocal, DBObject localKey) {
		Select select = Select.create()
				.table(modelForeign().table(), "", modelForeign().columnsNamesArray())
				.join(modelLocal.table(), localKey.name, modelForeign().table(), foreignKey)
				.where(modelLocal.table()+"."+localKey.name, localKey.object);
		
		return (ArrayList<DBModel<?>>) modelForeign().query(select).executeQuery(modelForeign().getClass());
	}
	
	private <T extends DBModel<?>> DBModel<?> modelForeign() {
		if(modelForeign == null) modelForeign = DBModel.create((Class<T>) relationForeign);
		return modelForeign;
	}
}
