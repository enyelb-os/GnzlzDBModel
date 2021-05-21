package tools.gnzlz.database.properties;

import tools.gnzlz.database.migration.MGColumn;
import tools.gnzlz.database.migration.interfaces.Type;

import java.util.ArrayList;

public class PropertiesTable {

    protected ArrayList<MGColumn> columns;

    /*****************
     * column
     *****************/

    public MGColumn column(String column) {
        MGColumn migrationColumn = new MGColumn(column);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn primaryKey(String column) {
        MGColumn migrationColumn = new MGColumn(column,true);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn unique(String column) {
        MGColumn migrationColumn = new MGColumn(column,false,false,true);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn column(String column, Type type) {
        MGColumn migrationColumn = new MGColumn(column).type(type);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn primaryKey(String column, Type type) {
        MGColumn migrationColumn = new MGColumn(column,true).type(type);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn unique(String column, Type type) {
        MGColumn migrationColumn = new MGColumn(column,false,false,true).type(type);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    ArrayList<MGColumn> columns() {
        if (columns == null) columns = new ArrayList<MGColumn>();
        return columns;
    }
}
