package tools.gnzlz.database.form;

import tools.gnzlz.database.model.DBModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class DBFormModel<M extends DBModel<M>> {

    private ArrayList<DBFormColumn> columns;
    private static ArrayList<DBFormModel<?>> models;

    public DBFormModel(){
        forMethods(this,this.getClass(),fields());
    }

    /***********************
     * fields List
     ***********************/

    public ArrayList<DBFormColumn> fields(){
        if(columns == null) columns = new ArrayList<DBFormColumn>();
        return columns;
    }

    /***********************
     * Models List
     ***********************/

    private static ArrayList<DBFormModel<?>> models() {
        if(models == null) models = new ArrayList<DBFormModel<?>>();
        return models;
    }

    /***********************
     * Create
     ***********************/

    static <M extends DBModel<M>,T extends DBFormModel<M>> T model(Class<T> c) {
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Class Error: ");
    }

    public static <M extends DBModel<M>,T extends DBFormModel<M>> DBFormModel<?> create(Class<T> c) {
        for (DBFormModel<?> dbModel : models()) {
            if(dbModel.getClass().getName().equals(c.getName())) {
                return dbModel;
            }
        }
        T model = model(c);
        models().add(model);
        return model;
    }

    /***********************
     * Create
     ***********************/

    private static void forMethods(DBFormModel model, Class c, ArrayList<DBFormColumn> fields){
        Method[] methods = c.getDeclaredMethods();
        if(c.getSuperclass() != null && c.getSuperclass() != DBModel.class){
            forMethods(model,c.getSuperclass(), fields);
        }
        for (Method method : methods) {
            if(method.getReturnType() == DBFormColumn.class) {
                try {
                    DBFormColumn field = (DBFormColumn) method.invoke(model);
                    fields.add(field);
                } catch (Exception e){ e.printStackTrace();}
            }
        }
    }
}
