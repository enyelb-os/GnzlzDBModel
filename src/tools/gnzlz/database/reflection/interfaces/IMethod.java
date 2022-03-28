package tools.gnzlz.database.reflection.interfaces;

import java.lang.reflect.Method;

@FunctionalInterface
public interface IMethod {

    boolean method(Method method);
}
