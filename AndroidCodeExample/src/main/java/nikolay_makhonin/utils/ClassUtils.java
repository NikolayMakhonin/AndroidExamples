package nikolay_makhonin.utils;

import java.lang.reflect.Field;

import nikolay_makhonin.utils.logger.Log;

public class ClassUtils {
    public static boolean IsSubClassOrInterface(final Class subClass, final Class baseClass) {
        if (subClass == baseClass || baseClass.isAssignableFrom(subClass)) {
            return true;
        }
        if (!baseClass.isInterface()) {
            return false;
        }
        final Class[] interfaces = subClass.getInterfaces();
        final int     length     = interfaces.length;
        for (int i = 0; i < length; i++) {
            if (IsSubClassOrInterface(interfaces[i], baseClass)) {
                return true;
            }
        }
        return false;
    }

    public static Field getPrivateField(Class type, final String fieldName) {
        while (type != null) {
            final Field[] fields = type.getDeclaredFields();
            final int length = fields.length;
            for (int i = 0; i < length; i++) {
                final Field field = fields[i];
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            type = type.getSuperclass();
        }
        Log.e("ClassUtils", "getPrivateField, field not found: " + fieldName + " in class: " + type.getName());
        return null;
    }
}
