package br.com.insanitech.javabinary.tokenizing;

import android.support.annotation.NonNull;

import java.io.IOException;

import br.com.insanitech.javabinary.storage.DataReader;

public enum DataType implements UInt8 {
    OBJECT(0),
    INT8(1),
    INT16(2),
    INT32(3),
    INT64(4),
    FLOAT(5),
    DOUBLE(6),
    STRING(7),
    DATA(8),

    ARRAY_OBJECT(9),
    ARRAY_INT8(10),
    ARRAY_INT16(11),
    ARRAY_INT32(12),
    ARRAY_INT64(13),
    ARRAY_FLOAT(14),
    ARRAY_DOUBLE(15),
    ARRAY_STRING(16),
    ARRAY_DATA(17);

    private final byte raw;

    public static DataType valueOf(byte value) {
        for (DataType type : DataType.values()) {
            if (type.getRaw() == value) {
                return type;
            }
        }
        return null;
    }

    public static DataType valueOf(int value) {
        return DataType.valueOf((byte) value);
    }

    DataType(byte raw) {
        this.raw = raw;
    }

    DataType(int raw) {
        this.raw = (byte) raw;
    }

    @Override
    public byte getRaw() {
        return this.raw;
    }

    public boolean isEqualsType(@NonNull Class<?> type) {
        return (this == INT8 && type == Byte.class) ||
                (this == INT16 && type == Short.class) ||
                (this == INT32 && type == Integer.class) ||
                (this == INT64 && type == Long.class) ||
                (this == FLOAT && type == Float.class) ||
                (this == DOUBLE && type == Double.class) ||
                (this == STRING && type == String.class) ||
                (this == DATA && type == DataReader.class) ||
                (this == OBJECT && type == IvarObject.class);
    }

    public Class<? extends Object> getType() {
        switch (this) {
            case ARRAY_INT8:
            case INT8:
                return Byte.class;
            case ARRAY_INT16:
            case INT16:
                return Short.class;
            case ARRAY_INT32:
            case INT32:
                return Integer.class;
            case ARRAY_INT64:
            case INT64:
                return Long.class;
            case ARRAY_FLOAT:
            case FLOAT:
                return Float.class;
            case ARRAY_DOUBLE:
            case DOUBLE:
                return Double.class;
            case ARRAY_STRING:
            case STRING:
                return String.class;
            case ARRAY_DATA:
            case DATA:
                return DataReader.class;
            case ARRAY_OBJECT:
            case OBJECT:
                return IvarObject.class;
        }
        return null;
    }

    public static <T extends Object> boolean isFixedSize(Class<T> type) {
        return type == Byte.class ||
                type == Short.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Float.class ||
                type == Double.class;
    }

    public static boolean isFixedSize(DataType type) {
        return type == INT8 ||
                type == INT16 ||
                type == INT32 ||
                type == INT64 ||
                type == FLOAT ||
                type == DOUBLE;
    }

    public static <T extends Object> boolean isSizeable(Class<T> type) {
        return type == String.class ||
                type == DataReader.class;
    }

    public static boolean isSizeable(DataType type) {
        return type == STRING ||
                type == DATA;
    }

    public static boolean isArray(DataType type) {
        switch (type) {
            case ARRAY_OBJECT:
            case ARRAY_INT8:
            case ARRAY_INT16:
            case ARRAY_INT32:
            case ARRAY_INT64:
            case ARRAY_FLOAT:
            case ARRAY_DOUBLE:
            case ARRAY_STRING:
            case ARRAY_DATA:
                return true;
        }
        return false;
    }

    public Token getIvarInstance() throws IOException {
        switch (this) {
            case OBJECT:
                return new IvarObject();
            case INT8:
                return new IvarToken<>(Byte.class);
            case INT16:
                return new IvarToken<>(Short.class);
            case INT32:
                return new IvarToken<>(Integer.class);
            case INT64:
                return new IvarToken<>(Long.class);
            case FLOAT:
                return new IvarToken<>(Float.class);
            case DOUBLE:
                return new IvarToken<>(Double.class);
            case STRING:
                return new IvarToken<>(String.class);
            case DATA:
                return new IvarToken<>(DataReader.class);
            // array types
            case ARRAY_OBJECT:
                return new IvarArray<>(IvarObject.class);
            case ARRAY_INT8:
                return new IvarArray<>(Byte.class);
            case ARRAY_INT16:
                return new IvarArray<>(Short.class);
            case ARRAY_INT32:
                return new IvarArray<>(Integer.class);
            case ARRAY_INT64:
                return new IvarArray<>(Long.class);
            case ARRAY_FLOAT:
                return new IvarArray<>(Float.class);
            case ARRAY_DOUBLE:
                return new IvarArray<>(Double.class);
            case ARRAY_STRING:
                return new IvarArray<>(String.class);
            case ARRAY_DATA:
                return new IvarArray<>(DataReader.class);
        }
        return null;
    }
}
