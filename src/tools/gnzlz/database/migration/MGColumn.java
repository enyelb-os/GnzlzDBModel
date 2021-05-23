package tools.gnzlz.database.migration;

import tools.gnzlz.database.migration.interfaces.Type;
import tools.gnzlz.database.model.DBMigration;

public class MGColumn {

    private String column;
    private Type type;
    private boolean primaryKey;
    private boolean unique;
    private boolean autoincrement;
    private boolean notNull;
    private String length;
    private Number max;
    private Number min;
    private String isDefault;
    private MGForeignKey foreignKey;


    /*****************
     * column
     *****************/

    public MGColumn(String column) {
        this.column = column;
    }

    public MGColumn(String column,boolean primaryKey) {
        this(column,primaryKey,primaryKey);
    }

    public MGColumn(String column,boolean primaryKey, boolean autoincrement) {
        this(column,primaryKey,autoincrement,false);
    }

    public MGColumn(String column,boolean primaryKey, boolean autoincrement,boolean unique) {
        this.column = column;
        this.primaryKey = primaryKey;
        this.autoincrement = autoincrement;
        this.unique = unique;
    }

    public String column() {
        return column;
    }

    /*****************
     * type
     *****************/

    public MGColumn type(Type type) {
        this.type = type;
        return this;
    }

    public Type type() {
        return type;
    }

    /*****************
     * primaryKey
     *****************/

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /*****************
     * autoincrement
     *****************/

    public boolean isAutoincrement() {
        return autoincrement;
    }


    /*****************
     * unique
     *****************/

    public boolean isUnique() {
        return unique;
    }

    /*****************
     * notNull
     *****************/

    public MGColumn notNull() {
        this.notNull = true;
        return this;
    }

    public boolean isNotNull() {
        return notNull;
    }

    /*****************
     * default
     *****************/

    public MGColumn isDefault(String isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public String isDefault() {
        return isDefault;
    }

    /*****************
     * length
     *****************/

    public MGColumn length(String length) {
        this.length = length;
        return this;
    }

    public MGColumn length(int length) {
        this.length = String.valueOf(length);
        return this;
    }

    public String length() {
        return length;
    }

    /*****************
     * foreignKey
     *****************/

    public MGColumn foreignKey(String table,String column) {
        this.foreignKey = new MGForeignKey(table,column);
        return this;
    }

    public  <M extends DBMigration> MGColumn foreignKey(Class<M> migration,String column) {
        DBMigration dbMigration = DBMigration.migration(migration);
        this.foreignKey = new MGForeignKey(dbMigration.tableName(),column);
        return this;
    }

    public MGForeignKey foreignKey() {
        return foreignKey;
    }
}
