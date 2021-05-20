package tools.gnzlz.database.migration;

public class MigrationColumn {

    private String column;
    private String type;

    /*****************
     * column
     *****************/

    public MigrationColumn column(String column) {
        this.column = column;
        return this;
    }

    public String column() {
        return column;
    }

    /*****************
     * type
     *****************/

    public MigrationColumn type(String type) {
        this.type = type;
        return this;
    }

    public String type() {
        return type;
    }
}
