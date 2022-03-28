package tools.gnzlz.database.reflection;

import tools.gnzlz.database.model.DBTable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class DBTableReflection {

    public static DBTable dbTable(Class c){
        Field field = DBRefection.field(c, f -> {
            return f.getType() == DBTable.class && Modifier.isStatic(f.getModifiers());
        });
        try {
            field.setAccessible(true);
            return (DBTable) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
