package tools.gnzlz.database.properties;

import tools.gnzlz.database.model.DBMigration;

import java.util.ArrayList;

public class PTMigration {

	protected PropertiesMigration migration;

	public PTMigration(PropertiesMigration migration) {
		this.migration = migration;
	}

	/*****************
	 * migration
	 *****************/

	public ArrayList<DBMigration> migrations() {
		return migration.migrations;
	}
}
