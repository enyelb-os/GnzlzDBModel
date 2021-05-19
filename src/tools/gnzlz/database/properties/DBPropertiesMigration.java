package tools.gnzlz.database.properties;

import tools.gnzlz.database.migration.DBMigration;

import java.util.ArrayList;

public class DBPropertiesMigration {

	protected ArrayList<DBMigration> migrations;

	/*****************
	 * migration
	 *****************/

	public DBPropertiesMigration migration(DBMigration migration) {
		migrations().add(migration);
		return this;
	}

	public ArrayList<DBMigration> migrations() {
		if(migrations == null) migrations = new ArrayList<DBMigration>();
		return migrations;
	}
}
