package br.com.insanitech.javabinary.tokenizing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.insanitech.javabinary.exceptions.TypeNotSupportedException;
import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.storage.DataWriter;

/**
 * Created by anderson on 22/06/2017.
 */

public class IvarToken<T> extends Token {
    private T value;

    IvarToken() throws IOException {

    }

    public IvarToken(Class<T> type) throws IOException {
        super();
        this.findType(type);
        this.setName("");
    }

    public IvarToken(String name, T value, Class<T> type) throws IOException {
        super();
        this.findType(type);
        this.setName(name);
        this.setValue(value);
    }

    public T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
    }

    public void findType(Class<T> type) throws IOException {
        if (type == Byte.class) {
            this.setType(DataType.INT8);
        } else if (type == Short.class) {
            this.setType(DataType.INT16);
        } else if (type == Integer.class) {
            this.setType(DataType.INT32);
        } else if (type == Long.class) {
            this.setType(DataType.INT64);
        } else if (type == Float.class) {
            this.setType(DataType.FLOAT);
        } else if (type == Double.class) {
            this.setType(DataType.DOUBLE);
        } else if (type == String.class) {
            this.setType(DataType.STRING);
        } else if (type == DataReader.class) {
            this.setType(DataType.DATA);
        } else {
            throw new TypeNotSupportedException(type);
        }
    }

    // MARK: Encoding implementations

    @Override
    public DataReader encode() throws IOException {
        DataWriter data = new DataWriter(new ByteArrayOutputStream());
        this.writeOther(this.getType().getRaw(), data);
        this.writeString(this.getName(), data);
        this.writeValue(data);
        return new DataReader(data);
    }

    public void writeValue(DataWriter inData) throws IOException {
        switch (this.getType()) {
            case STRING: this.writeString((String) this.getValue(), inData); break;
            case DATA: this.writeData((DataReader)this.getValue(), inData); break;
            default: this.writeOther(this.getValue(), inData); break;
        }
    }

    // MARK: Decoding implementations

    @Override
    public void decode(ByteBuffer data) throws IOException {
        DataReader reader = new DataReader(data.array());
        this.decode(reader);
    }

    @Override
    public void decode(DataReader bytes) throws IOException {
        this.setType(DataType.valueOf(this.readOther(Byte.class, bytes)));
        this.setName(this.readString(bytes));
        this.readValue(bytes);
    }

    @SuppressWarnings("unchecked")
    public void readValue(DataReader bytes) throws IOException {
        switch (this.getType()) {
            case STRING: this.setValue((T) this.readString(bytes)); break;
            case DATA: this.setValue((T) this.readData(bytes)); break;
            default: this.setValue((T) this.readOther(this.getType().getType(), bytes));
        }
    }
}
