package tools.gnzlz.database.migration;

import tools.gnzlz.database.properties.PropertiesTable;

@FunctionalInterface
public interface IDBMigration {

    void initTable(PropertiesTable table);
}
