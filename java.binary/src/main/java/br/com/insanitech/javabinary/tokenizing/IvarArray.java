package br.com.insanitech.javabinary.tokenizing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import br.com.insanitech.javabinary.exceptions.TypeNotSupportedException;
import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.storage.DataWriter;

/**
 * Created by anderson on 22/06/2017.
 */

public class IvarArray<T> extends IvarToken<List<T>> {
    public IvarArray(Class<T> type) throws IOException {
        this.findType(type, null);
        this.setName("");
    }

    public IvarArray(String name, List<T> value, Class<T> type) throws IOException {
        super();
        this.findType(type, null);
        this.setName(name);
        this.setValue(value);
    }

    /**
     * Finds the type for this IvarArray, Void ignored is used to differ from the super class findType.
     */
    public void findType(Class<T> type, Void ignored) throws IOException {
        if (type == Byte.class) {
            this.setType(DataType.ARRAY_INT8);
        } else if (type == Short.class) {
            this.setType(DataType.ARRAY_INT16);
        } else if (type == Integer.class) {
            this.setType(DataType.ARRAY_INT32);
        } else if (type == Long.class) {
            this.setType(DataType.ARRAY_INT64);
        } else if (type == Float.class) {
            this.setType(DataType.ARRAY_FLOAT);
        } else if (type == Double.class) {
            this.setType(DataType.ARRAY_DOUBLE);
        } else if (type == String.class) {
            this.setType(DataType.ARRAY_STRING);
        } else if (type == DataReader.class) {
            this.setType(DataType.ARRAY_DATA);
        } else if (type == IvarObject.class) {
            this.setType(DataType.ARRAY_OBJECT);
        } else {
            throw new TypeNotSupportedException(type);
        }
    }

    // MARK: Encoding implementations

    @Override
    public DataReader encode() throws IOException {
        DataWriter dataWriter = new DataWriter(new ByteArrayOutputStream());
        this.writeOther(this.getType().getRaw(), dataWriter);
        this.writeString(this.getName(), dataWriter);
        this.writeValue(dataWriter);
        return new DataReader(dataWriter);
    }

    @Override
    public void writeValue(DataWriter inData) throws IOException {
        this.writeOther(this.getValue().size(), inData);
        if (DataType.isFixedSize(this.getType().getType())) {
            for (T value : this.getValue()) {
                this.writeOther(value, inData);
            }
        } else if (DataType.isSizeable(this.getType().getType())) {
            for (T value : this.getValue()) {
                if (this.getType().getType() == String.class) {
                    this.writeString((String) value, inData);
                } else {
                    this.writeData((DataReader) value, inData);
                }
            }
        } else {
            for (T value : this.getValue()) {
                inData.writeData(((Token)value).encode());
            }
        }
    }

    // MARK: decoding implementations

    @Override
    public void decode(ByteBuffer data) throws IOException {
        super.decode(data);
    }

    @Override
    public void decode(DataReader bytes) throws IOException {
        this.setType(DataType.valueOf(this.readOther(Byte.class, bytes)));
        this.setName(this.readString(bytes));
        this.readValue(bytes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readValue(DataReader bytes) throws IOException {
        Integer count = this.readOther(Integer.class, bytes);

        ArrayList<T> value = (ArrayList<T>) this.getTypedArray();
        if (DataType.isFixedSize(this.getType().getType())) {
            for (int i = 0; i < count; i++) {
                value.add(i, (T) this.readOther(this.getType().getType(), bytes));
            }
        } else if (DataType.isSizeable(this.getType().getType())) {
            for (int i = 0; i < count; i++) {
                Class<? extends Object> type = this.getType().getType();
                if (type == String.class) {
                    value.add(i, (T) this.readString(bytes));
                } else {
                    value.add(i, (T) this.readData(bytes));
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                DataType type = DataType.valueOf(this.readOther(Byte.class, bytes, false));
                Token token = type.getIvarInstance();
                assert token != null;
                token.decode(bytes);
                value.add(i, (T) token);
            }
        }
        this.setValue(value);
    }

    private ArrayList<?> getTypedArray() {
        switch (this.getType()) {
            case ARRAY_INT8: return new ArrayList<Byte>();
            case ARRAY_INT16: return new ArrayList<Short>();
            case ARRAY_INT32: return new ArrayList<Integer>();
            case ARRAY_INT64: return new ArrayList<Long>();
            case ARRAY_FLOAT: return new ArrayList<Float>();
            case ARRAY_DOUBLE: return new ArrayList<Double>();
            case ARRAY_STRING: return new ArrayList<String>();
            case ARRAY_DATA: return new ArrayList<DataReader>();
            default: return new ArrayList<Object>();
        }
    }
}
