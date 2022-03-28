package tools.gnzlz.database.model;

import java.util.ArrayList;

import tools.gnzlz.database.query.model.Select;
import tools.gnzlz.database.reflection.DBTableReflection;

public class DBROneToMany<M extends DBModel<M>> {
	
	private String foreignKey;
	private Class<M> relationForeign;
	private DBTable table;
	
	DBROneToMany(String foreignKey, Class<M> relationForeign) {
		this.relationForeign = relationForeign;
		this.foreignKey = foreignKey;
	}
	
	public <T extends DBModel<?>> boolean isClass(Class<T> c) {
		return relationForeign.getName().equals(c.getName());
	}
	
	public <T extends DBModel<?>> boolean isClass(Class<T> c,String column) {
		return relationForeign.getName().equals(c.getName()) && foreignKey.equalsIgnoreCase(column) ;
	}
	
	public ArrayList<M> hasMany(DBModel<?> modelLocal, DBObject localKey) {
		Select select = Select.create()
				.table(table().table(), "", table().columnsNamesArray())
				.join(modelLocal.table(), localKey.name, table().table(), foreignKey)
				.where(modelLocal.table()+"."+localKey.name, localKey.object);
		
		return (ArrayList<M>) DBModel.query(table(),select).executeQuery(relationForeign);
	}

	private DBTable table() {
		if(table == null)
			table = DBTableReflection.dbTable(relationForeign);
		return table;
	}
}
