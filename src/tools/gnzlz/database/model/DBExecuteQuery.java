package tools.gnzlz.database.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBExecuteQuery {
	
	private PreparedStatement sql;
	
	protected DBExecuteQuery(PreparedStatement sql) {
		this.sql = sql;
	}
	
	/**********************
	 * Value
	 **********************/
	
	public DBExecuteQuery value(int i, Object object){
		try {
			sql.setObject(i, object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**********************
	 * Value
	 **********************/

	public DBExecuteQuery value(int i, String format, Date date){
		return value(i, new SimpleDateFormat(format).format(date));
	}

	/**********************
	 * Value
	 **********************/

	public DBExecuteQuery value(int i, Date date){
		return value(i,"yyyy-MM-dd", date);
	}
	
	/**********************
	 * Execute
	 **********************/
	
	public synchronized boolean execute(){
		try {
			sql.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**********************
	 * Execute
	 **********************/
	
	public synchronized Object executeID(){
		try {
			sql.executeUpdate();
			ResultSet r = sql.getGeneratedKeys();
			if (r.next()) {
				return r.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**********************
	 * Execute
	 **********************/
	
	public synchronized Object executeID(DBModel<?> dbModel){
		try {
			sql.executeUpdate();
			ResultSet r = sql.getGeneratedKeys();
			if(r.next()) {
				DBObject dbObject = dbModel.primaryKey();
				Object id = r.getObject(1);
				if(dbObject!=null){
					dbObject.object(id);
				}
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	
	/**********************
	 * ExecuteQuery
	 **********************/
	
	public synchronized ArrayList<DBModel<?>> executeQuery(){
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
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dbModels;
	}
	
	public synchronized <DB extends DBModel<?>> ArrayList<DB> executeQuery(Class<DB> c){
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
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dbModels;
	}
	
	/**********************
	 * ExecuteSingel
	 **********************/
	
	public synchronized DBModel<?> executeSingle(){
		try {
			ResultSet r = sql.executeQuery();
			while (r.next()) {
				DBModel<?> dbModel = DBModel.create(DBModel.class);
				dbModel.initColumns(r);
				return dbModel;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public synchronized <DB extends DBModel<?>> DB executeSingle(Class<DB> c){
		return executeSingle(DBModel.create(c));
	}
		
	
	public synchronized <DB extends DBModel<?>> DB executeSingle(DB dbModel){
		try {
			ResultSet r = sql.executeQuery();
			while (r.next()) {
				dbModel.initColumns(r);
				return dbModel;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				sql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
