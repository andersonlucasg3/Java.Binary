package br.com.insanitech.javabinary;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.insanitech.javabinary.storage.DataReader;

/**
 * Created by anderson on 22/06/2017.
 */

public abstract class Decodable {
    public abstract void decode(ByteBuffer data) throws Exception;

    public abstract void decode(DataReader bytes) throws Exception;

    public String readString(DataReader bytes) throws IOException {
        Integer length = this.readOther(Integer.class, bytes);
        return bytes.readString(length);
    }

    public ByteBuffer readData(DataReader bytes) throws IOException {
        Integer length = this.readOther(Integer.class, bytes);
        byte[] buffer = new byte[length];
        bytes.readBytes(buffer, length);
        return ByteBuffer.wrap(buffer);
    }

    public <T extends Object> T readOther(Class<T> type, DataReader bytes) throws IOException {
        return this.readOther(type, bytes, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T readOther(Class<T> type, DataReader bytes, boolean advance) throws IOException {
        // need to do lots o code here because java does not support type casting like swift
        int position = bytes.position();
        if (type == Byte.class) {
            T value = (T) bytes.readByte();
            if (!advance) bytes.seek(position-1);
            return value;
        } else if (type == Short.class) {
            T value = (T) bytes.readShort();
            if (!advance) bytes.seek(position-2);
            return value;
        } else if (type == Integer.class) {
            T value = (T) bytes.readInt();
            if (!advance) bytes.seek(position-4);
            return value;
        } else if (type == Long.class) {
            T value = (T) bytes.readLong();
            if (!advance) bytes.seek(position-8);
            return value;
        } else if (type == Float.class) {
            T value = (T) bytes.readFloat();
            if (!advance) bytes.seek(position-4);
            return value;
        } else if (type == Double.class) {
            T value = (T) bytes.readDouble();
            if (!advance) bytes.seek(position-8);
            return value;
        }
        return null;
    }
}
