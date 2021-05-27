package tools.gnzlz.database.properties;

import tools.gnzlz.database.migration.MGColumn;
import tools.gnzlz.database.model.DBMigration;

import java.util.ArrayList;

public class PTMigration {

	protected PropertiesMigration migration;

	public PTMigration(PropertiesMigration migration) {
		this.migration = migration;
	}

	/*****************
	 * migrations
	 *****************/

	public ArrayList<DBMigration> migrations() {
		return migration.migrations;
	}

	/*******************
	 * migrations
	 *******************/

	public ArrayList<DBMigration> orderMigrations() {
		if(migration.migrations != null) {
			for(int i = 0; i < migration.migrations.size(); i++){
				DBMigration dbMigration = migration.migrations.get(i);
				boolean exists = false;
				for (MGColumn column: dbMigration.table().columns()){
					if(column.foreignKey() != null){
						for (int j = i+1 ; j < migration.migrations.size() - 1; j++){
							DBMigration dbMigrationMove = migration.migrations.get(j);
							if(column.foreignKey().table().equalsIgnoreCase(dbMigrationMove.tableName())){
								migration.migrations.remove(dbMigrationMove);
								migration.migrations.add(i,dbMigrationMove);
								exists = true;
								break;
							}
						}
					}
				}
				i = exists ? i-1 : i;
			}
		}
		return migration.migrations;
	}
}
