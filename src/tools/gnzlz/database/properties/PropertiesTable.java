package tools.gnzlz.database.properties;

import tools.gnzlz.database.migration.MGColumn;

import java.util.ArrayList;

public class PropertiesTable {

    private ArrayList<MGColumn> columns;

    /*****************
     * column
     *****************/

    public MGColumn column(String column) {
        MGColumn migrationColumn = new MGColumn(column);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn colmun(MGColumn column) {
        columns().add(column);
        return column;
    }

    ArrayList<MGColumn> columns() {
        if (columns == null) columns = new ArrayList<MGColumn>();
        return columns;
    }
}
