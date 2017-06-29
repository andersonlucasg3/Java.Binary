package br.com.insanitech.javabinary;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.tokenizing.IvarArray;
import br.com.insanitech.javabinary.tokenizing.IvarObject;
import br.com.insanitech.javabinary.tokenizing.IvarToken;
import br.com.insanitech.javabinary.tokenizing.Token;

/**
 * Created by anderson on 28/06/2017.
 */

public class ObjectEncoder {
    public DataReader encode(Object object) throws IOException, IllegalAccessException {
        IvarObject ivarObject = this.encodeObject(object, "");
        return ivarObject.encode();
    }

    private IvarObject encodeObject(Object object, String name) throws IOException, IllegalAccessException {
        List<Token> tokens = new ArrayList<>();

        Class<?> objectClass = object.getClass();
        while (objectClass != null) {
            Field[] fields = objectClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                Token token = this.getType(field, object);
                if (token != null) {
                    tokens.add(token);
                }
            }

            objectClass = objectClass.getSuperclass();
        }

        if (name == null) {
            name = "";
        }

        return new IvarObject(name, tokens);
    }

    private Token getType(Field field, Object object) throws IOException, IllegalAccessException {
        Object value = field.get(object);
        if (value != null) {
            return this.getAnyType(value, field.getName());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> Token getAnyType(T value, String key) throws IOException, IllegalAccessException {
        Token token = this.getToken(value, key);
        if (token != null) {
            return token;
        } else {
            if (value.getClass().getName().contains("List")) {
                return this.getArray((List<Object>)value, key);
            } else {
                return this.encodeObject(value, key);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Token getArray(List<Object> values, String key) throws IOException, IllegalAccessException {
        if (values.size() > 0) {
            Object value = values.get(0);
            Class<?> type = value.getClass();
            if (type == String.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new String[values.size()])), key, String.class);
            } else if (type == DataReader.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new DataReader[values.size()])), key, DataReader.class);
            } else if (type == Byte.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new Byte[values.size()])), key, Byte.class);
            } else if (type == Short.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new Short[values.size()])), key, Short.class);
            } else if (type == Integer.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new Integer[values.size()])), key, Integer.class);
            } else if (type == Long.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new Long[values.size()])), key, Long.class);
            } else if (type == Float.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new Float[values.size()])), key, Float.class);
            } else if (type == Double.class) {
                return this.getAnyArray(Arrays.asList(values.toArray(new Double[values.size()])), key, Double.class);
            } else {
                List<IvarObject> array = new ArrayList<>();
                for (Object object : values) {
                    array.add(this.encodeObject(object, null));
                }
                return new IvarArray<>(key, array, IvarObject.class);
            }
        }
        return new IvarArray<>(key, new ArrayList<Byte>(), Byte.class);
    }

    private <T> IvarArray<T> getAnyArray(List<T> values, String key, Class<T> type) throws IOException {
        List<T> tokens = new ArrayList<>();
        for (T value : values) {
            tokens.add(value);
        }
        return new IvarArray<>(key, tokens, type);
    }

    @SuppressWarnings("unchecked")
    private <T extends Object> Token getToken(@NonNull T value, String key) throws IOException {
        Class<T> type = (Class<T>) value.getClass();
        if (type == String.class) {
            return new IvarToken<>(key, (String) value, String.class);
        } else if (type == DataReader.class) {
            return new IvarToken<>(key, (DataReader) value, DataReader.class);
        } else if (type == Byte.class) {
            return new IvarToken<>(key, (Byte) value, Byte.class);
        } else if (type == Short.class) {
            return new IvarToken<>(key, (Short) value, Short.class);
        } else if (type == Integer.class) {
            return new IvarToken<>(key, (Integer) value, Integer.class);
        } else if (type == Long.class) {
            return new IvarToken<>(key, (Long) value, Long.class);
        } else if (type == Float.class) {
            return new IvarToken<>(key, (Float) value, Float.class);
        } else if (type == Double.class) {
            return new IvarToken<>(key, (Double) value, Double.class);
        }
        return null;
    }
}
