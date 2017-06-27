package br.com.insanitech.javabinary;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.insanitech.javabinary.exceptions.TypeNotSupportedException;
import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.storage.DataWriter;

/**
 * Created by anderson on 26/06/2017.
 */

public abstract class Encodable {
    public abstract ByteBuffer encode() throws IOException;

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
            inData.write((Byte)other);
        } else if (type == Short.class) {
            inData.write((Short)other);
        } else if (type == Integer.class) {
            inData.write((Integer)other);
        } else if (type == Long.class) {
            inData.write((Long)other);
        } else if (type == Float.class) {
            inData.write((Float)other);
        } else if (type == Double.class) {
            inData.write((Double)other);
        } else {
            throw new TypeNotSupportedException(type);
        }
    }
}
