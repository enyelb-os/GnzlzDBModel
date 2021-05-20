package tools.gnzlz.database.model;

import java.util.ArrayList;

import tools.gnzlz.database.query.Select;

public class DBRManyToMany {
	
	private String internalKey1;
	private Class<?> relationInternal;
	private String internalKey2;
	private DBModel<?> modelInternal;
	
	private Class<?> relationForeign;
	private String foreignKey;
	private DBModel<?> modelForeign;
	
	<T,I extends DBModel<?>> DBRManyToMany(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		this.internalKey1 = internalKey1;
		this.relationInternal = relationInternal;
		this.internalKey2 = internalKey2;
		this.relationForeign = relationForeign;
		this.foreignKey = foreignKey;
	}
	
	public <T,I extends DBModel<?>> boolean isClass(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		return isClass(internalKey1, relationInternal, internalKey2, relationForeign) && this.foreignKey.equalsIgnoreCase(foreignKey); 
	}
	
	public <T,I extends DBModel<?>> boolean isClass(Class<I> relationInternal, Class<T> relationForeign, String foreignKey) {
		return isClass(relationInternal, relationForeign) && this.foreignKey.equalsIgnoreCase(foreignKey); 
	}
	
	public <T,I extends DBModel<?>> boolean isClass(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign) {
		return isClass(relationInternal, relationForeign) && this.internalKey1.equalsIgnoreCase(internalKey1) && this.internalKey2.equalsIgnoreCase(internalKey2); 
	}
	
	public <T,I extends DBModel<?>> boolean isClass(Class<I> internal, Class<T> foreign) {
		return relationInternal.getName().equals(internal.getName()) && relationForeign.getName().equals(foreign.getName());
	}
	
	public ArrayList<DBModel<?>> belongsToMany(DBModel<?> modelLocal, DBObject localKey) {
		Select select = Select.create()
				.table(modelForeign().table(), "", toArray(modelForeign().columns()))
				.join(modelLocal.table(), localKey.name, modelInternal().table(), internalKey1)
				.join(modelInternal().table(), internalKey2, modelForeign().table(), foreignKey)
				.where(modelLocal.table()+"."+localKey.name, localKey.object);
		
		return (ArrayList<DBModel<?>>) modelForeign().query(select).executeQuery(modelForeign().getClass());
	}
	
	private String[] toArray(ArrayList<DBObject> dbObjects){
		String[] list = new String[dbObjects.size()];
		for (int i = 0; i < list.length; i++) {
			list[i] = dbObjects.get(i).name;
		}
		return list;
	}
	
	private <T extends DBModel<?>> DBModel<?> modelForeign() {
		if(modelForeign == null) modelForeign = DBModel.create((Class<T>) relationForeign);
		return modelForeign;
	}
	
	private <T extends DBModel<?>> DBModel<?> modelInternal() {
		if(modelInternal == null) modelInternal = DBModel.create((Class<T>) relationInternal);
		return modelInternal;
	}
}
