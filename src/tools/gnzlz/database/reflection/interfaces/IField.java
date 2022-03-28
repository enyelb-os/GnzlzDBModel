package tools.gnzlz.database.reflection.interfaces;

import java.lang.reflect.Field;

@FunctionalInterface
public interface IField {

    boolean field(Field field);
}
