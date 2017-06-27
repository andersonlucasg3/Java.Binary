package br.com.insanitech.javabinary;

public enum DataType implements IShort {
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

    private final short raw;

    DataType(short raw) {
        this.raw = raw;
    }

    DataType(int raw) {
        this.raw = (short) raw;
    }

    @Override
    public short getRaw() {
        return this.raw;
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
                type == Byte[].class;
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

//    public Decodable getIvarInstance() throws TypeNotSupportedException {
//        switch (this) {
//            case OBJECT: return new IvarObject();
//            case INT8: return new IvarToken<Byte>();
//            case INT16: return new IvarToken<Short>();
//            case INT32: return new IvarToken<Integer>();
//            case INT64: return new IvarToken<Long>();
//            case FLOAT: return new IvarToken<Float>();
//            case DOUBLE: return new IvarToken<Double>();
//            case STRING: return new IvarToken<String>();
//            case DATA: return new IvarToken<Byte[]>();
//            // array types
//            case ARRAY_OBJECT: return new IvarArray<IvarObject>();
//            case ARRAY_INT8: return new IvarArray<Byte>();
//            case ARRAY_INT16: return new IvarArray<Short>();
//            case ARRAY_INT32: return new IvarArray<Integer>();
//            case ARRAY_INT64: return new IvarArray<Long>();
//            case ARRAY_FLOAT: return new IvarArray<Float>();
//            case ARRAY_DOUBLE: return new IvarArray<Double>();
//            case ARRAY_STRING: return new IvarArray<String>();
//            case ARRAY_DATA: return new IvarArray<Byte[]>();
//        }
//        return null;
//    }
}
