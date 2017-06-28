package br.com.insanitech.javabinary.tokenizing;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.insanitech.javabinary.exceptions.TypeNotSupportedException;
import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.storage.DataWriter;

/**
 * Created by anderson on 27/06/2017.
 */

public abstract class Token {
    private DataType type;
    private String name;

    public DataType getType() {
        return type;
    }

    void setType(DataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    // MARK: Encodable

    public abstract DataReader encode() throws IOException;

    public void writeString(String string, DataWriter inData) throws IOException {
        Integer length = string.length();
        inData.write(length);

        inData.writeString(string, length);
    }

    public void writeData(DataReader data, DataWriter inData) throws IOException {
        Integer length = data.length();
        inData.write(length);

        inData.writeData(data);
    }

    public <T extends Object> void writeOther(T other, DataWriter inData) throws IOException {
        Class<?> type = other.getClass();
        if (type == Byte.class) {
            inData.write((Byte) other);
        } else if (type == Short.class) {
            inData.write((Short) other);
        } else if (type == Integer.class) {
            inData.write((Integer) other);
        } else if (type == Long.class) {
            inData.write((Long) other);
        } else if (type == Float.class) {
            inData.write((Float) other);
        } else if (type == Double.class) {
            inData.write((Double) other);
        } else {
            throw new TypeNotSupportedException(type);
        }
    }

    // MARK: Decodable

    public abstract void decode(ByteBuffer data) throws IOException;

    public abstract void decode(DataReader bytes) throws IOException;

    public String readString(DataReader bytes) throws IOException {
        Integer length = this.readOther(Integer.class, bytes);
        return bytes.readString(length);
    }

    public DataReader readData(DataReader bytes) throws IOException {
        Integer length = this.readOther(Integer.class, bytes);
        byte[] buffer = new byte[length];
        bytes.readBytes(buffer, length);
        return new DataReader(buffer);
    }

    public <T extends Object> T readOther(Class<T> type, DataReader bytes) throws IOException {
        return this.readOther(type, bytes, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T readOther(Class<T> type, DataReader bytes, boolean advance) throws IOException {
        // need to do lots o code here because java does not support type casting like swift
        if (type == Byte.class) {
            T value = (T) bytes.readByte();
            if (!advance) bytes.seek(bytes.position() - 1);
            return value;
        } else if (type == Short.class) {
            T value = (T) bytes.readShort();
            if (!advance) bytes.seek(bytes.position() - 2);
            return value;
        } else if (type == Integer.class) {
            T value = (T) bytes.readInt();
            if (!advance) bytes.seek(bytes.position() - 4);
            return value;
        } else if (type == Long.class) {
            T value = (T) bytes.readLong();
            if (!advance) bytes.seek(bytes.position() - 8);
            return value;
        } else if (type == Float.class) {
            T value = (T) bytes.readFloat();
            if (!advance) bytes.seek(bytes.position() - 4);
            return value;
        } else if (type == Double.class) {
            T value = (T) bytes.readDouble();
            if (!advance) bytes.seek(bytes.position() - 8);
            return value;
        }
        return null;
    }
}
