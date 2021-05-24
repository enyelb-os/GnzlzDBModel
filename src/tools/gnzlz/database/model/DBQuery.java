package tools.gnzlz.database.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBQuery {
	
	private PreparedStatement sql;
	
	protected DBQuery(PreparedStatement sql) {
		this.sql = sql;
	}
	
	/**********************
	 * Value
	 **********************/
	
	public DBQuery value(int i, Object object){
		try {
			sql.setObject(i, object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**********************
	 * Execute
	 **********************/
	
	public boolean execute(){
		try {
			sql.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**********************
	 * Execute
	 **********************/
	
	public int executeID(){
		try {
			sql.executeUpdate();
			ResultSet r = sql.getGeneratedKeys();
			while (r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**********************
	 * Execute
	 **********************/
	
	public void executeID(DBModel<?> dbModel){
		try {
			sql.executeUpdate();
			ResultSet r = sql.getGeneratedKeys();
			while (r.next()) {
				DBObject dbObject = dbModel.primaryKey();
				if(dbObject!=null)
					dbObject.object(r.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**********************
	 * ExecuteQuery
	 **********************/
	
	public ArrayList<DBModel<?>> executeQuery(){
		ArrayList<DBModel<?>> dbModels = new ArrayList<DBModel<?>>();
		try {
			ResultSet r = sql.executeQuery();
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbModels;
	}
	
	public <DB extends DBModel<?>> ArrayList<DB> executeQuery(Class<DB> c){
		ArrayList<DB> dbModels = new ArrayList<DB>();
		try {
			ResultSet r = sql.executeQuery();
			while (r.next()) {
				DB dbModel = DBModel.create(c);
				dbModel.initColumns(r);
				dbModels.add(dbModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbModels;
	}
	
	/**********************
	 * ExecuteSingel
	 **********************/
	
	public DBModel<?> executeSingle(){
		try {
			ResultSet r = sql.executeQuery();
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				return dbModel;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <DB extends DBModel<?>> DB executeSingle(Class<DB> c){
		return executeSingle(DBModel.create(c));
	}
		
	
	public <DB extends DBModel<?>> DB executeSingle(DB dbModel){
		try {
			ResultSet r = sql.executeQuery();
			while (r.next()) {
				dbModel.initColumns(r);
				return dbModel;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
