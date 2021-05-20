package tools.gnzlz.database.migration;

public class MGColumn {

    private String column;
    private String type;

    /*****************
     * column
     *****************/

    public MGColumn(String column) {
        this.column = column;
    }

    public String column() {
        return column;
    }

    /*****************
     * type
     *****************/

    public MGColumn type(String type) {
        this.type = type;
        return this;
    }

    public String type() {
        return type;
    }
}
