package tools.gnzlz.database.query.migration;

import tools.gnzlz.database.query.migration.builder.ICreateTable;
import tools.gnzlz.database.query.migration.builder.Query;
import tools.gnzlz.database.query.migration.builder.data.GCreateTable;

public class CreateTable extends Query<CreateTable> implements ICreateTable<CreateTable>{

	public CreateTable() {}
	
	public static CreateTable create() {
		return new CreateTable();
	}
	
	/***************************
	 * insert
	 ***************************/
	
	private GCreateTable dInsert;
	
	@Override
	public GCreateTable gCreateTable() {
		if(dInsert == null) dInsert = new GCreateTable();
		return dInsert;
	}
	
	@Override
	public CreateTable type() {
		return this;
	}

	@Override
	public String query() {
		StringBuilder builder = generateCreateTable();
		System.out.println(builder);
		return builder.toString();
	}
}
