package tools.gnzlz.database.properties;

import tools.gnzlz.database.model.DBMigration;
import tools.gnzlz.database.migration.interfaces.IMigration;

import java.util.ArrayList;

public class PropertiesMigration {

	protected ArrayList<DBMigration> migrations;

	/*****************
	 * migration
	 *****************/

	public <M extends DBMigration> PropertiesMigration add(Class<M> migration) {
		migrations().add(DBMigration.create(migration));
		return this;
	}

	public PropertiesMigration add(String table,IMigration migration) {
		migrations().add(new DBMigration() {
			@Override
			protected String table() {
				return table;
			}

			@Override
			protected void initTable(PropertiesTable table) {
				migration.initTable(table);
			}
		});
		return this;
	}

	ArrayList<DBMigration> migrations() {
		if(migrations == null) migrations = new ArrayList<DBMigration>();
		return migrations;
	}
}
