package tools.gnzlz.database.reflection;

import tools.gnzlz.database.reflection.interfaces.IField;
import tools.gnzlz.database.reflection.interfaces.IMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class DBRefection {

    public static ArrayList<Field> fields(Class c, IField iField){
        return forFields(c,new ArrayList<Field>(),iField);
    }

    public static ArrayList<Method> methods(Class c, IMethod iMethod){
        return forMethods(c,new ArrayList<Method>(),iMethod);
    }

    public static Field field(Class c, IField iField){
        return forField(c, iField);
    }

    public static Method method(Class c, IMethod iMethod){
        return forMethod(c, iMethod);
    }

    private static ArrayList<Field> forFields(Class c, ArrayList<Field> ids, IField iField){
        Field[] fields = c.getDeclaredFields();
        if(c.getSuperclass() != null){
            forFields(c.getSuperclass(), ids, iField);
        }
        for (Field field : fields) {
            if(iField.field(field)) {
                ids.add(field);
            }
        }
        return ids;
    }

    private static Field forField(Class c, IField iField){
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if(iField.field(field)) {
                return field;
            }
        }
        if(c.getSuperclass() != null){
            return forField(c.getSuperclass(), iField);
        }
        return null;
    }

    private static ArrayList<Method> forMethods(Class c, ArrayList<Method> ids, IMethod iMethod){
        Method[] methods = c.getDeclaredMethods();
        if(c.getSuperclass() != null){
            forMethods(c.getSuperclass(), ids, iMethod);
        }
        for (Method method : methods) {
            if(iMethod.method(method)) {
                ids.add(method);
            }
        }
        return ids;
    }

    private static Method forMethod(Class c, IMethod iMethod){
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if(iMethod.method(method)) {
                return method;
            }
        }
        if(c.getSuperclass() != null){
            return forMethod(c.getSuperclass(), iMethod);
        }
        return null;
    }
}
