package tools.gnzlz.database.model;

import tools.gnzlz.database.properties.PropertiesTable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class DBMigration {

    /*****************
     * Static
     *****************/

    private static ArrayList<DBMigration> migrations;

    private static ArrayList<DBMigration> migrations() {
        if(migrations == null) migrations = new ArrayList<DBMigration>();
        return migrations;
    }

    /***********************
     * Create
     ***********************/

    public static <M extends DBMigration> M create(Class<M> c) {
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Class Error: ");
    }

    public static <M extends DBMigration> DBMigration migration(Class<M> c) {
        for (DBMigration dbMigration : migrations()) {
            if(dbMigration.getClass().getName().equals(c.getName()))
                return dbMigration;
        }
        M migration = create(c);
        migrations().add(migration);
        return migration;
    }

    /*****************
     * Constructor
     *****************/

    private PropertiesTable table;

    /**************************
     * Properties Abstract
     **************************/

    protected abstract void initTable(PropertiesTable table);
}
