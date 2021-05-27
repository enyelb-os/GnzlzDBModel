package tools.gnzlz.database.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import tools.gnzlz.database.query.model.Delete;
import tools.gnzlz.database.query.model.Insert;
import tools.gnzlz.database.query.model.Select;
import tools.gnzlz.database.query.model.Update;
import tools.gnzlz.database.query.model.builder.Query;

public class DBModel<M extends DBModel<?>>{
	
	private DBTable table;
	
	private ArrayList<DBObject> columns;
	
	private static ArrayList<DBModel<?>> models;
	
	private boolean exists;
	
    public DBModel(DBTable table) {
    	addTable(table);
	}
    
    public DBModel() {}

	/***********************
	 * Models List
	 ***********************/

	private static ArrayList<DBModel<?>> models() {
		if(models == null) models = new ArrayList<DBModel<?>>();
		return models;
	}
	
	/***********************
	 * Create
	 ***********************/
	
	public static <T extends DBModel<?>> T create(Class<T> c) {
		try {
			return c.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Class Error: ");
	}

	static <T extends DBModel<?>> DBModel<?> model(Class<T> c) {
		for (DBModel<?> dbModel : models()) {
			if(dbModel.getClass().getName().equals(c.getName()))
				return dbModel;
		}
		T model = create(c);
		models().add(model);
		return model;
	}
	
	/***********************
	 * query
	 ***********************/
	
	public DBQuery query(Query<?> query) {
		return table.query(query);
	}
	
	public DBQuery query(String query) {
		return table.query(query);
	}
	
	/***********************
	 * initTable
	 ***********************/
	
	protected M addTable(DBTable table) {
		this.table = table;
		if(table != null) {
			for (DBColumn dbColumn : table.columns()) {
				DBObject dbObject = get(dbColumn.name);
				if(dbObject == null)
					columns().add(new DBObject(dbColumn.name, null, dbColumn));
				else 
					dbObject.column(dbColumn);
			}
		}
		return (M) this;
	}

	/***********************
	 * initColumns
	 ***********************/
	
	void initColumns(ResultSet r) throws SQLException{
		ResultSetMetaData m = r.getMetaData();
		for (int i = 0; i < m.getColumnCount(); i++) {
			String name = m.getColumnLabel(i+1);
			DBObject dbObject = get(name);
			if(dbObject != null)
				dbObject.initObject(r.getObject(i+1));
			else
				columns().add(new DBObject(name, r.getObject(i+1), null));
		}
		exists = true;
	}

	/***********************
	 * PrimaryKey
	 ***********************/
	
	public M primaryKey(Object object) {
		DBObject dbObject = primaryKey();
		if(dbObject != null && object != null)
			dbObject.object(object);
		return (M) this;
	}
	
	public DBObject primaryKey() {
		for (DBObject dbObject : columns()) {
			if (dbObject.column != null && dbObject.column.isPrimaryKey()) {
				return dbObject;
			}
		}
		return null;
	}
	
	/***********************
	 * Exists
	 ***********************/
	
	public boolean isExists() {
		return exists;
	}
	
	/***********************
	 * Table
	 ***********************/
	
	public String table() {
		return table.table();
	}
	
	/***********************
	 * Columns
	 ***********************/
	
	public ArrayList<DBObject> columns() {
		if(columns == null) columns = new ArrayList<DBObject>(); 
		return columns;
	}

	public ArrayList<String> columnsNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (DBObject dbObject: columns()) {
			names.add(dbObject.name);
		}
		return names;
	}

	public String[] columnsNamesArray() {
		return (String[]) columnsNames().toArray();
	}
	
	/***********************
	 * Get
	 ***********************/
	
	public DBObject get(String name) {
		if(name != null) {
			for (DBObject dbObject : columns()) {
				if (dbObject.name().equalsIgnoreCase(name)) {
					return dbObject;
				}
			}
		}
		return null;
	}
	
	public DBObject get(int i) {
		return columns().get(i);
	}
	
	/***********************
	 * Set
	 ***********************/
	
	public void set(String name, Object object) {
		if(name != null && object != null) {
			boolean exists = false;
			for (DBObject dbObject : columns()) {
				if (dbObject.name().equals(name)) {
					exists = true;
					if(object instanceof Date)
						dbObject.object(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)object));
					else
						dbObject.object(object);
					break;
				}
			}
			
			if(!exists)
				columns().add(new DBObject(name, object, null));
		}
	}
	
	public void set(int i, Object object) {
		if(object != null) {
			DBObject dbObject = columns().get(i);
			if(dbObject != null) dbObject.object(object);
		}
	}
	
	/***********************
	 * isChange
	 ***********************/
	
	public boolean isChange() {
		boolean isChange = false;
		for (DBObject dbObject : columns()) {
			if(dbObject.isChange()) {
				dbObject.change(false);
				isChange = true;
			}
		}
		return isChange;
	}
	
	/***********************
	 * Save
	 ***********************/
	
	public void save() {
		if(exists)
			update();
		else 
			insert();
	}
	
	/*******************************
	 * Create And Update And Delete
	 *******************************/
	
	private void insert(){
		if(table != null && columns().size() > 0) {
			Insert insert = Insert.create().table(table.table());
			for (DBObject dbObject : columns())
				if(dbObject.object() != null && dbObject.column() != null) 
					insert.columns(dbObject.name()).values(dbObject.object());
			
			table.configuration().connection().query(insert).executeID(this);
		}
	}
	
	private void update(){
		DBObject primaryKey = primaryKey();
		if(table != null && columns().size() > 0 && primaryKey != null) {
			Update update = Update.create().table(table.table());
			for (DBObject dbObject : columns()) {
				if(dbObject.object() != null && dbObject.column() != null) 
					update.columns(dbObject.name()).values(dbObject.object());
			}
			update.where(primaryKey.name(), primaryKey.object());
			table.configuration().connection().query(update).execute();
		}
	}
	
	public void delete(){
		if(exists) {
			DBObject primaryKey = primaryKey();
			if(table != null && columns().size() > 0 && primaryKey != null) {
				Delete delete = Delete.create()
					.table(table.table()).where(primaryKey.name(), primaryKey.object());
				table.configuration().connection().query(delete).execute();
			}
		}
	}
	
	/***********************
	 * toString
	 ***********************/
	
	@Override
	public String toString() {
		return Arrays.toString(columns().toArray());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {	
			if(obj != null) {
				Object aux = obj;
				if(obj instanceof DBObject) {
					aux = ((DBObject)obj).object;
				}else if(obj.getClass().getName().equals(this.getClass().getName())) {
			    	DBObject dbObject = ((DBModel<?>)obj).primaryKey();
			    	aux = dbObject != null ? dbObject.object : null ;
			    }
				
				if(aux != null)
					if(primaryKey() != null && primaryKey().object() != null)
						return primaryKey().object.equals(aux);
				
				return false;
			}
		}
		return true;
	}
	
	/***********************
	 * hasOne - foreignkey
	 ***********************/
	
	private <T extends DBModel<?>> T hasOne(DBObject dbObject, Class<T> c, String localKey) {
		if(dbObject != null) {
			if(dbObject.column != null) {
				DBROneToOne relations = localKey != null ? dbObject.column.getHasOne(c, localKey) : dbObject.column.getHasOne(c);
				if(relations != null) 
					return (T) relations.hasOne(this,dbObject);
			}
		}
		return null;
	}
	
	private <T extends DBModel<?>> T hasOneFor(Class<T> c, String localKey) {
		for (DBObject dbObject : columns()) {
			T dbModel = hasOne(dbObject, c, localKey);
			if(dbModel != null) return dbModel;
		}
		return null;
	}
	
	
	public <T extends DBModel<?>> T hasOne(String foreignkey, Class<T> c, String localKey) {			
		return hasOne(get(foreignkey), c, localKey);
	}
	
	public <T extends DBModel<?>> T hasOne(Class<T> c, String localKey) {			
		return hasOneFor(c, localKey);
	}
	
	public <T extends DBModel<?>> T hasOne(String foreignkey, Class<T> c) {			
		return hasOne(get(foreignkey), c, null);
	}
	
	public <T extends DBModel<?>> T hasOne(Class<T> c) {
		return hasOneFor(c, null);
	}
	
	/***********************
	 * hasMany - foreignkey
	 ***********************/
	
	private <T extends DBModel<?>> ArrayList<T> hasMany(DBObject dbObject, Class<T> c, String foreignKey) {
		if(dbObject != null) {
			if(dbObject.column != null) {
				DBROneToMany relations = foreignKey != null ? dbObject.column.getHasMany(c, foreignKey) : dbObject.column.getHasMany(c);
				if(relations != null)
					return (ArrayList<T>) relations.hasMany(this,dbObject);
			}
		}
		return null;
	}
	
	private <T extends DBModel<?>> ArrayList<T> hasManyFor(Class<T> c, String foreignKey) {
		for (DBObject dbObject : columns()) {
			ArrayList<T> list = hasMany(dbObject, c, foreignKey);
			if(list != null) return list;
		}
		return null;
	}
	
	public <T extends DBModel<?>> ArrayList<T> hasMany(String localkey, Class<T> c, String foreignKey) {			
		return hasMany(get(localkey), c, foreignKey);
	}
	
	public <T extends DBModel<?>> ArrayList<T> hasMany(Class<T> c, String foreignKey) {
		return hasManyFor(c, foreignKey);
	}
	
	public <T extends DBModel<?>> ArrayList<T> hasMany(String localkey, Class<T> c) {			
		return hasMany(get(localkey), c, null);
	}
	
	public <T extends DBModel<?>> ArrayList<T> hasMany(Class<T> c) { 
		return hasManyFor(c, null);
	}
	
	/***********************
	 * hasMany - foreignkey
	 ***********************/
	
	private <T,I extends DBModel<?>> ArrayList<T> belongsToMany(DBObject dbObject, String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		if(dbObject != null) {
			if(dbObject.column != null) {
				DBRManyToMany relations = 
						foreignKey == null && (internalKey1 == null || internalKey2 == null) ? 
								dbObject.column.getBelongsToMany(relationInternal, relationForeign) :
						foreignKey == null ? 
								dbObject.column.getBelongsToMany(internalKey1, relationInternal, internalKey2, relationForeign) : 
						internalKey1 == null || internalKey2 == null ? 
								dbObject.column.getBelongsToMany(relationInternal, relationForeign, foreignKey):
								dbObject.column.getBelongsToMany(internalKey1, relationInternal, internalKey2, relationForeign, foreignKey);
				if(relations != null)
					return (ArrayList<T>) relations.belongsToMany(this,dbObject);
			}
		}
		return null;
	}
	
	private <T,I extends DBModel<?>> ArrayList<T> belongsToManyFor(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {
		for (DBObject dbObject : columns()) {
			ArrayList<T> list = belongsToMany(dbObject, internalKey1, relationInternal, internalKey2, relationForeign, foreignKey);
			if(list != null) return list;
		}
		return null;
	}
	
	public <T,I extends DBModel<?>>  ArrayList<T> belongsToMany(Class<I> internal, Class<T> foreign) {
		return belongsToManyFor(null, internal, null, foreign, null);
	}
	
	public <T,I extends DBModel<?>>  ArrayList<T> belongsToMany(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign) {			
		return belongsToManyFor(internalKey1, relationInternal, internalKey2, relationForeign, null);
	}
	
	public <T,I extends DBModel<?>>  ArrayList<T> belongsToMany(String localKey, String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign) {			
		return belongsToMany(get(localKey), null, relationInternal, null, relationForeign, null);
	}
	
	public <T,I extends DBModel<?>>  ArrayList<T> belongsToMany(String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {			
		return belongsToManyFor(internalKey1, relationInternal, internalKey2, relationForeign, foreignKey);
	}
	
	public <T,I extends DBModel<?>>  ArrayList<T> belongsToMany(String localKey, String internalKey1, Class<I> relationInternal, String internalKey2, Class<T> relationForeign, String foreignKey) {			
		return belongsToMany(get(localKey), internalKey1, relationInternal, internalKey2, relationForeign, foreignKey);
	}
	
	public <T,I extends DBModel<?>>  ArrayList<T> belongsToMany(Class<I> relationInternal, Class<T> relationForeign, String foreignKey) {			
		return belongsToManyFor(null, relationInternal, null, relationForeign, foreignKey);
	}
	
	public <T,I extends DBModel<?>>  ArrayList<T> belongsToMany(String localKey, Class<I> relationInternal, Class<T> relationForeign, String foreignKey) {			
		return belongsToMany(get(localKey), null, relationInternal, null, relationForeign, foreignKey);
	}
	
	/***********************
	 * Find
	 ***********************/
	
	protected M find(DBObject column) {
		if(table != null && column !=null && column.object() != null && (column.isChange() || table.configuration().model().refresh()))
			return (M) table.query(Select.create().from(table.table()).where(column.name(), column.object())).executeSingle(this);
		return (M) this;
	}

	public M find() {
		return find(primaryKey());
	}
	
	public M find(String column) {
		return find(get(column));
	}
	
	/***********************
	 * FindAll
	 ***********************/
	
	protected ArrayList<M> findAll(DBObject column) {
		if(table != null && column !=null && column.object() != null && column.isChange())
			return (ArrayList<M>) table.query(Select.create().from(table.table()).where(column.name(), column.object())).executeQuery(getClass());
		return new ArrayList<M>();
	}

	public ArrayList<M> findAll() {
		return findAll(primaryKey());
	}
	
	public ArrayList<M> findAll(String column) {
		return findAll(get(column));
	}
	
	/***********************
	 * Find IN
	 ***********************/
	
	ArrayList<M> findIn(DBObject column, Object ... primaryKeys) {
		if(table != null && column !=null) {
			if(primaryKeys != null) {
				return (ArrayList<M>) table.query(Select.create().from(table.table()).in(column.name(), primaryKeys)).executeQuery(getClass());
			}
		}
		return new ArrayList<M>();
	}
	
	protected ArrayList<M> findIn(Object ... primaryKeys) {
		return findIn(primaryKey(), primaryKeys);
	}
	
	protected ArrayList<M> findIn(String column,Object ... primaryKeys) {
		return findIn(get(column), primaryKeys);
	}
	
	/***********************
	 * Find Static
	 ***********************/
	
	public static <T extends DBModel<?>> T find(Class<T> c, Object primaryKey) {
		T model = create(c); 
		model.primaryKey().object(primaryKey);
		return (T) model.find();
	}
	
	public static <T extends DBModel<?>> T find(Class<T> c, String column, Object value) {
		T model = create(c); 
		model.set(column, value);
		return (T) model.find(column);
	}
	
	public static <T extends DBModel<?>> ArrayList<T> findIn( Class<T> c, Object ... primaryKeys) {
		return (ArrayList<T>) create(c).findIn(primaryKeys);
	}
	
	public static <T extends DBModel<?>> ArrayList<T> findIn(Class<T> c, String column, Object ... value) {
		return (ArrayList<T>) create(c).findIn(column,value);
	}
	
	public static <T extends DBModel<?>> ArrayList<T> findAll(Class<T> c, String column, Object value) {
		T model = create(c); 
		model.set(column, value);
		return (ArrayList<T>) model.findAll(column);
	}
	
	
	/***********************
	 * All
	 ***********************/
	
	public ArrayList<M> all() {
		if(table != null)
			return (ArrayList<M>) table.query(Select.create().from(table.table())).executeQuery(getClass());
		return null;
	}
	
	public static <T extends DBModel<?>> ArrayList<T> all(Class<T> c) {
		T model = create(c);
		return (ArrayList<T>) model.all();
	}
}
