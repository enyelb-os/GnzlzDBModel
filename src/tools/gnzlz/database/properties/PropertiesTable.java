package tools.gnzlz.database.properties;

import tools.gnzlz.database.migration.MigrationColumn;
import tools.gnzlz.database.migration.MigrationTable;

import java.util.ArrayList;

public class PropertiesTable {

    protected MigrationTable table;

    MigrationTable migrationTable() {
        if (table == null) table = new MigrationTable();
        return table;
    }

    /*****************
     * table
     *****************/

    public PropertiesTable table(String table) {
        migrationTable().table(table);
        return this;
    }

    String table() {
        return migrationTable().table();
    }

    /*****************
     * column
     *****************/

    public MigrationColumn column(String column) {
        MigrationColumn migrationColumn = new MigrationColumn().column(column);
        migrationTable().colmun(migrationColumn);
        return migrationColumn;
    }

    ArrayList<MigrationColumn> columns() {
        return migrationTable().columns();
    }
}
