package tools.gnzlz.database.model;

import java.util.ArrayList;

import tools.gnzlz.database.query.model.Select;
import tools.gnzlz.database.reflection.DBTableReflection;

public class DBRManyToMany<N extends DBModel<N>,M extends DBModel<M>> {
	
	private String internalKey1;
	private Class<N> relationInternal;
	private String internalKey2;
	private DBTable tableInternal;
	
	private Class<M> relationForeign;
	private String foreignKey;
	private DBTable tableForeign;
	
	DBRManyToMany(String internalKey1, Class<N> relationInternal, String internalKey2, Class<M> relationForeign, String foreignKey) {
		this.internalKey1 = internalKey1;
		this.relationInternal = relationInternal;
		this.internalKey2 = internalKey2;
		this.relationForeign = relationForeign;
		this.foreignKey = foreignKey;
	}
	
	public <T extends DBModel<?>,I extends DBModel<?>> boolean isClass(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		return isClass(internalKey1, relationInternal, internalKey2, relationForeign) && this.foreignKey.equalsIgnoreCase(foreignKey); 
	}
	
	public <T extends DBModel<?>,I extends DBModel<?>> boolean isClass(Class<I> relationInternal, Class<T> relationForeign, String foreignKey) {
		return isClass(relationInternal, relationForeign) && this.foreignKey.equalsIgnoreCase(foreignKey); 
	}
	
	public <T extends DBModel<?>,I extends DBModel<?>> boolean isClass(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign) {
		return isClass(relationInternal, relationForeign) && this.internalKey1.equalsIgnoreCase(internalKey1) && this.internalKey2.equalsIgnoreCase(internalKey2); 
	}
	
	public <T extends DBModel<?>,I extends DBModel<?>> boolean isClass(Class<I> internal, Class<T> foreign) {
		return relationInternal.getName().equals(internal.getName()) && relationForeign.getName().equals(foreign.getName());
	}
	
	public ArrayList<M> belongsToMany(DBModel<?> modelLocal, DBObject localKey) {
		Select select = Select.create()
				.table(tableForeign().table(), "", tableForeign().columnsNamesArray())
				.join(modelLocal.table(), localKey.name, tableInternal().table(), internalKey1)
				.join(tableInternal().table(), internalKey2, tableForeign().table(), foreignKey)
				.where(modelLocal.table()+"."+localKey.name, localKey.object);
		
		return (ArrayList<M>) DBModel.query(tableForeign(),select).executeQuery(relationForeign);
	}

	private DBTable tableInternal() {
		if(tableInternal == null)
			tableInternal = DBTableReflection.dbTable(relationInternal);
		return tableInternal;
	}

	private DBTable tableForeign() {
		if(tableForeign == null)
			tableForeign = DBTableReflection.dbTable(relationForeign);
		return tableForeign;
	}
}
