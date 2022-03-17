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
        return column(column,null,"");
    }

    public MGColumn column(String column, Type type) {
        return column(column,type,"");
    }

    public MGColumn column(String column, Type type, int length) {
        return column(column,type,String.valueOf(length));
    }

    public MGColumn column(String column, Type type, String length) {
        MGColumn migrationColumn = new MGColumn(column).type(type).length(length);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn primaryKey(String column) {
        return primaryKey(column,null,"");
    }

    public MGColumn primaryKey(String column, Type type) {
        return primaryKey(column,type,"");
    }

    public MGColumn primaryKey(String column, Type type, int length) {
        return primaryKey(column,type,String.valueOf(length));
    }

    public MGColumn primaryKey(String column, Type type, String length) {
        MGColumn migrationColumn = new MGColumn(column,true).type(type).length(length);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    public MGColumn unique(String column) {
        return unique(column,null,"");
    }

    public MGColumn unique(String column, Type type) {
        return unique(column,type,"");
    }

    public MGColumn unique(String column, Type type,int length) {
        return unique(column,type,String.valueOf(length));
    }

    public MGColumn unique(String column, Type type,String length) {
        MGColumn migrationColumn = new MGColumn(column,false,false,true).type(type).length(length);
        columns().add(migrationColumn);
        return migrationColumn;
    }

    ArrayList<MGColumn> columns() {
        if (columns == null) columns = new ArrayList<MGColumn>();
        return columns;
    }
}
