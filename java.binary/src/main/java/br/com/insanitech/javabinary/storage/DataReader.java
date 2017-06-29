package br.com.insanitech.javabinary.storage;

import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import br.com.insanitech.javabinary.exceptions.DataInputReadException;

/**
 * Created by anderson on 27/06/2017.
 */

public class DataReader extends Data {
    private int position = 0;
    private byte[] buffer;

    public DataReader(@NonNull byte[] buffer) {
        this.buffer = buffer;
    }

    public DataReader(DataWriter writer) {
        this(writer.outputStream.toByteArray());
    }

    public int position() {
        return this.position;
    }

    @Override
    public int length() {
        return this.buffer.length;
    }

    public Byte readByte() throws DataInputReadException {
        byte[] bytes = new byte[1];
        this.readBytes(bytes, 1);

        return bytes[0];
    }

    public Short readShort() throws DataInputReadException {
        byte[] bytes = new byte[2];
        this.readBytes(bytes, 2);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 2).order(ByteOrder.BIG_ENDIAN);

        return buffer.asShortBuffer().get();
    }

    public Integer readInt() throws DataInputReadException {
        byte[] bytes = new byte[4];
        this.readBytes(bytes, 4);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.BIG_ENDIAN);

        return buffer.asIntBuffer().get();
    }

    public Long readLong() throws DataInputReadException {
        byte[] bytes = new byte[8];
        this.readBytes(bytes, 8);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 8).order(ByteOrder.BIG_ENDIAN);

        return buffer.asLongBuffer().get();
    }

    public Float readFloat() throws DataInputReadException {
        byte[] bytes = new byte[4];
        this.readBytes(bytes, 4);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.BIG_ENDIAN);

        return buffer.asFloatBuffer().get();
    }

    public Double readDouble() throws DataInputReadException {
        byte[] bytes = new byte[8];
        this.readBytes(bytes, 8);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 8).order(ByteOrder.BIG_ENDIAN);

        return buffer.asDoubleBuffer().get();
    }

    public void readBytes(byte[] buffer, int length) throws DataInputReadException {
        int available = this.buffer.length - this.position;
        int read = length <= available ? length : available;

        System.arraycopy(this.buffer, this.position, buffer, 0, read);

        this.position += read;
        if (read != length) {
            throw new DataInputReadException(length, read);
        }
    }

    public Data readData(int length) throws DataInputReadException {
        byte[] buffer = new byte[length];
        this.readBytes(buffer, length);
        return new DataReader(buffer);
    }

    public String readString(int length) throws DataInputReadException {
        byte[] buffer = new byte[length];
        this.readBytes(buffer, length);
        return new String(buffer, Charset.forName("UTF-8"));
    }

    public void seek(int position) {
        this.position = position;
    }
}
