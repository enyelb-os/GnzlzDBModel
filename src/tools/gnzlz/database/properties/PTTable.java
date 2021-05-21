package tools.gnzlz.database.properties;

import tools.gnzlz.database.migration.MGColumn;

import java.util.ArrayList;

public class PTTable {

    protected PropertiesTable table;

    public PTTable(PropertiesTable table) {
        this.table = table;
    }

    /*****************
     * column
     *****************/

    public ArrayList<MGColumn> columns() {
        return table.columns;
    }
}
