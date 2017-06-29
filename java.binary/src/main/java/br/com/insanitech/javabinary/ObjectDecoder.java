package br.com.insanitech.javabinary;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.insanitech.javabinary.exceptions.TypeNotSupportedException;
import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.tokenizing.DataType;
import br.com.insanitech.javabinary.tokenizing.IvarArray;
import br.com.insanitech.javabinary.tokenizing.IvarObject;
import br.com.insanitech.javabinary.tokenizing.IvarToken;
import br.com.insanitech.javabinary.tokenizing.Token;

import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_DATA;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_DOUBLE;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_FLOAT;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_INT16;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_INT32;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_INT64;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_INT8;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_OBJECT;
import static br.com.insanitech.javabinary.tokenizing.DataType.ARRAY_STRING;
import static br.com.insanitech.javabinary.tokenizing.DataType.STRING;

/**
 * Created by anderson on 28/06/2017.
 */

public class ObjectDecoder {
    public <T> T decode(DataReader data, Class<T> type) throws IOException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        IvarObject object = new IvarObject();
        object.decode(data);

        T instance = type.newInstance();
        this.populateFields(instance, object);
        return instance;
    }

    public void decode(DataReader data, Object instance) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        IvarObject object = new IvarObject();
        object.decode(data);

        this.populateFields(instance, object);
    }

    @SuppressWarnings("unchecked")
    private void populateFields(Object instance, IvarObject object) throws NoSuchFieldException, IllegalAccessException, InstantiationException, TypeNotSupportedException {
        for (Token field : object.getValue()) {
            if (DataType.isFixedSize(field.getType())) {
                this.setFixedSize(field, instance);
            } else if (DataType.isSizeable(field.getType())) {
                if (field.getType() == STRING) {
                    this.populateIvarToken((IvarToken<String>) field, instance);
                } else {
                    this.populateIvarToken((IvarToken<DataReader>) field, instance);
                }
            } else {
                if (DataType.isArray(field.getType())) {
                    this.populateTokenArray(field, instance);
                } else {
                    Object childInstance = this.populateObject(field, field.getName(), instance);
                    if (childInstance != null) {
                        this.setIvarValue(instance, field.getName(), childInstance);
                    }
                }
            }
        }
    }

    private Object populateObject(Token value, String name, Object instance) throws IllegalAccessException, InstantiationException, NoSuchFieldException, TypeNotSupportedException {
        Class<?> type = this.findObjectType(name, instance);
        Object typeInstance = type.newInstance();
        this.populateFields(typeInstance, (IvarObject) value);
        return typeInstance;
    }

    private List<Field> getAllFieldsFromClass(Class<?> classType) {
        List<Field> fields = new ArrayList<>();
        while (classType != null) {
            fields.addAll(Arrays.asList(classType.getDeclaredFields()));

            classType = classType.getSuperclass();
        }
        return fields;
    }

    private Field findField(String fieldName, List<Field> fields) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    private Class<?> findObjectType(String fieldName, Object instance) throws NoSuchFieldException {
        Class<?> instanceClass = instance.getClass();
        Field field = this.findField(fieldName, this.getAllFieldsFromClass(instanceClass));
        assert field != null;
        field.setAccessible(true);
        return field.getType();
    }

    private Class<?> findArrayType(String fieldName, Object instance) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Class<?> instanceClass = instance.getClass();
        Field field = this.findField(fieldName, this.getAllFieldsFromClass(instanceClass));
        assert field != null;
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        return (Class<?>) genericType.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    private void populateTokenArray(Token token, Object instance) throws TypeNotSupportedException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        if (token.getType() == ARRAY_INT8) {
            this.populateArray(Byte.class, (IvarArray<Byte>) token, instance);
        } else if (token.getType() == ARRAY_INT16) {
            this.populateArray(Short.class, (IvarArray<Short>) token, instance);
        } else if (token.getType() == ARRAY_INT32) {
            this.populateArray(Integer.class, (IvarArray<Integer>) token, instance);
        } else if (token.getType() == ARRAY_INT64) {
            this.populateArray(Long.class, (IvarArray<Long>) token, instance);
        } else if (token.getType() == ARRAY_FLOAT) {
            this.populateArray(Float.class, (IvarArray<Float>) token, instance);
        } else if (token.getType() == ARRAY_DOUBLE) {
            this.populateArray(Double.class, (IvarArray<Double>) token, instance);
        } else if (token.getType() == ARRAY_STRING) {
            this.populateArray(String.class, (IvarArray<String>) token, instance);
        } else if (token.getType() == ARRAY_DATA) {
            this.populateArray(DataReader.class, (IvarArray<DataReader>) token, instance);
        } else if (token.getType() == ARRAY_OBJECT) {
            this.populateArray(IvarObject.class, (IvarArray<IvarObject>) token, instance);
        } else {
            throw new TypeNotSupportedException(token.getType());
        }
    }

    private <T> void populateArray(Class<T> type, IvarArray<T> values, Object instance) throws NoSuchFieldException, IllegalAccessException, InstantiationException, TypeNotSupportedException {
        if (type == IvarObject.class) {
            List<Object> array = new ArrayList<>();
            Class<?> itemType = this.findArrayType(values.getName(), instance);
            if (itemType == null) {
                throw new TypeNotSupportedException(this.findObjectType(values.getName(), instance));
            }
            for (T value : values.getValue()) {
                Object itemInstance = itemType.newInstance();
                this.populateFields(itemInstance, (IvarObject) value);
                array.add(itemInstance);
            }
            this.setIvarValue(instance, values.getName(), array);
        } else {
            this.populateIvarToken(values, instance);
        }
    }

    @SuppressWarnings("unchecked")
    private void setFixedSize(Token value, Object instance) throws NoSuchFieldException, IllegalAccessException {
        switch (value.getType()) {
            case INT8:
                this.populateIvarToken((IvarToken<Byte>) value, instance);
                break;
            case INT16:
                this.populateIvarToken((IvarToken<Short>) value, instance);
                break;
            case INT32:
                this.populateIvarToken((IvarToken<Integer>) value, instance);
                break;
            case INT64:
                this.populateIvarToken((IvarToken<Long>) value, instance);
                break;
            case FLOAT:
                this.populateIvarToken((IvarToken<Float>) value, instance);
                break;
            case DOUBLE:
                this.populateIvarToken((IvarToken<Double>) value, instance);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private Object getAnyObject(Token value) {
        switch (value.getType()) {
            case INT8:
                return ((IvarToken<Byte>) value).getValue();
            case INT16:
                return ((IvarToken<Short>) value).getValue();
            case INT32:
                return ((IvarToken<Integer>) value).getValue();
            case INT64:
                return ((IvarToken<Long>) value).getValue();
            case FLOAT:
                return ((IvarToken<Float>) value).getValue();
            case DOUBLE:
                return ((IvarToken<Double>) value).getValue();
            case DATA:
                return ((IvarToken<DataReader>) value).getValue();
            case STRING:
                return ((IvarToken<String>) value).getValue();
            default:
                return null;
        }
    }

    private <T> void populateIvarToken(IvarToken<T> value, Object instance) throws NoSuchFieldException, IllegalAccessException {
        this.setIvarValue(instance, value.getName(), value.getValue());
    }

    private <T> void setIvarValue(Object instance, String fieldName, T value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> instanceClass = instance.getClass();
        Field field = instanceClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
}
