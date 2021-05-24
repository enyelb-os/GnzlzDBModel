package tools.gnzlz.database.query.migration.builder;

import tools.gnzlz.database.model.interfaces.Dialect;
import tools.gnzlz.database.query.migration.builder.data.GCreateTable;

public interface ICreateTable<Type extends Query<?>> extends IColumn<Type> {
	
	Type type();
	
	GCreateTable gCreateTable();

	/***************************
	 * table
	 ***************************/
	
	public default Type table(String table){
		gCreateTable().table(table);
		return type();
	}
	
	/***************************
	 * get
	 ***************************/
	
	public default StringBuilder generateCreateTable(){
		StringBuilder str = new StringBuilder("CREATE TABLE");
		if(gCreateTable().table() == null || gCreateTable().table().isEmpty())
			throw new RuntimeException("Create error invalid table");
		else
			str.append(" ").append(gCreateTable().table());

		str.append(" (").append(System.lineSeparator()).append(generateColumns()).append(System.lineSeparator()).append(")");

		if (type().dialect() == Dialect.MySQL){
			str.append(" ENGINE=InnoDB");
		}
		
		return str;
	}
}
