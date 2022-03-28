package tools.gnzlz.database.properties;

import tools.gnzlz.database.model.DBMigration;
import tools.gnzlz.database.migration.interfaces.IMigration;

import java.util.ArrayList;

public class PropertiesMigration {

	protected ArrayList<DBMigration> migrations;

	protected String migrationPackage;

	/*****************
	 * migrationPackage
	 *****************/

	public PropertiesMigration migrationPackage(String migrationPackage) {
		this.migrationPackage = migrationPackage;
		return this;
	}

	/*****************
	 * migration
	 *****************/

	public <M extends DBMigration> PropertiesMigration add(Class<M> migration) {
		migrations().add(DBMigration.migration(migration));
		return this;
	}

	public PropertiesMigration add(String table,IMigration migration) {
		return add(table,"",migration);
	}

	public PropertiesMigration add(String table, String packageName,IMigration migration) {
		migrations().add(new DBMigration() {
			public String tableName() {
				return table;
			}

			@Override
			public String packageName() {
				return packageName;
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
