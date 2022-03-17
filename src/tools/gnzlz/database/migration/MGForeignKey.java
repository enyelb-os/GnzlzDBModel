package tools.gnzlz.database.migration;

public class MGForeignKey {

    private String column;
    private String table;

    /*****************
     * column
     *****************/

    public MGForeignKey(String table,String column) {
        this.table = table;
        this.column = column;
    }

    public String table() {
        return table;
    }

    public String column() {
        return column;
    }

    @Override
    public String toString() {
        return "ForeignKey{" +
                "column='" + column + '\'' +
                ", table='" + table + '\'' +
                '}';
    }
}
