package tools.gnzlz.database.query.migration.builder;

import tools.gnzlz.database.query.migration.builder.data.DColumn;
import tools.gnzlz.database.query.migration.builder.data.DForeignKey;
import tools.gnzlz.database.query.migration.builder.data.GCreateTable;

public interface IColumn<Type extends Query<?>> {
	
	public Type type();

	GCreateTable gCreateTable();
	
	/***************************
	 * column
	 ***************************/
	
	public default Type column(String column){
		if(column != null && !column.isEmpty())
			gCreateTable().column(column);
		return type();
	}

	/***************************
	 * foreignKey
	 ***************************/

	public default Type foreignKey(String table,String column){
		if(table != null && !table.isEmpty() && column != null && !column.isEmpty())
			gCreateTable().column().foreignKey = new DForeignKey(table,column);
		return type();
	}

	public default Type foreignKey(String localColumn,String table,String column){
		if(table != null && !table.isEmpty() && column != null && !column.isEmpty())
			gCreateTable().column(localColumn).foreignKey = new DForeignKey(table,column);
		return type();
	}

	/***************************
	 * isDefault
	 ***************************/

	public default Type isDefault(String isDefault){
		if(isDefault != null && !isDefault.isEmpty())
			gCreateTable().column().isDefault = isDefault;
		return type();
	}

	public default Type isDefault(String column, String isDefault){
		if(isDefault != null && !isDefault.isEmpty())
			gCreateTable().column(column).isDefault = isDefault;
		return type();
	}

	/***************************
	 * length
	 ***************************/

	public default Type length(int length){
		gCreateTable().column().length = String.valueOf(length);
		return type();
	}

	public default Type length(String column,int length){
		gCreateTable().column(column).length = String.valueOf(length);
		return type();
	}

	public default Type length(String length){
		gCreateTable().column().length = String.valueOf(length);
		return type();
	}

	public default Type length(String column,String length){
		gCreateTable().column(column).length = length;
		return type();
	}

	/***************************
	 * notNull
	 ***************************/

	public default Type notNull(){
		gCreateTable().column().notNull = true;
		return type();
	}

	public default Type notNull(String column){
		gCreateTable().column(column).notNull = true;
		return type();
	}

	/***************************
	 * autoincrement
	 ***************************/

	public default Type autoincrement(){
		gCreateTable().column().autoincrement = true;
		return type();
	}

	public default Type autoincrement(String column){
		gCreateTable().column(column).autoincrement = true;
		return type();
	}

	/***************************
	 * primaryKey
	 ***************************/

	public default Type unique(){
		gCreateTable().column().unique = true;
		return type();
	}

	public default Type unique(String column){
		gCreateTable().column(column).unique = true;
		return type();
	}

	/***************************
	 * primaryKey
	 ***************************/

	public default Type primaryKey(){
		gCreateTable().column().primaryKey = true;
		return type();
	}

	public default Type primaryKey(String column){
		gCreateTable().column(column).primaryKey = true;
		return type();
	}

	/***************************
	 * type
	 ***************************/

	public default Type type(String type){
		if(type != null && !type.isEmpty())
			gCreateTable().column().type = type;
		return type();
	}

	public default Type type(String column, String type){
		if(type != null && !type.isEmpty())
			gCreateTable().column(column).type = type;
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateColumns(){
		StringBuilder str = new StringBuilder();
		int i = 0, size = gCreateTable().columns().size();

		if(size == 0) throw new RuntimeException("error invalid columns size 0");

		for (DColumn column : gCreateTable().columns()) {
			str.append(i == 0 ? "":",").append(i == 0 ? "":System.lineSeparator())
					.append("\t").append(column.column());
			i++;
		}

		for (DColumn column : gCreateTable().columns()) {
			String foreignKey = column.foreignKey();
			if(!foreignKey.isEmpty())
				str.append(",").append(System.lineSeparator()).append("\t").append(foreignKey);
		}
		
		return str;
	}
}
