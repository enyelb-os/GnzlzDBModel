package tools.gnzlz.database.query.migration;

import tools.gnzlz.database.query.migration.builder.ICreateDB;
import tools.gnzlz.database.query.migration.builder.Query;
import tools.gnzlz.database.query.migration.builder.data.GCreateDB;

public class CreateDB extends Query<CreateDB> implements ICreateDB<CreateDB>{
	
	private CreateDB() {}
	
	public static CreateDB create() {
		return new CreateDB();
	}
	
	/***************************
	 * where
	 ***************************/
	
	private GCreateDB gCreateDB;
	
	@Override
	public GCreateDB gCreateDB() {
		if(gCreateDB == null) gCreateDB = new GCreateDB();
		return gCreateDB;
	}
	
	@Override
	public CreateDB type() {
		return this;
	}

	@Override
	public String query() {
		StringBuilder builder = generateCreateDB();
		System.out.println(builder);
		return builder.toString();
	}
}
