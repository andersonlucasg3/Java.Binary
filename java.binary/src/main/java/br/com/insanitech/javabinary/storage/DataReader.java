package br.com.insanitech.javabinary.storage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import br.com.insanitech.javabinary.exceptions.DataInputReadException;

/**
 * Created by anderson on 27/06/2017.
 */

public class DataReader extends Data {
    private int position = 0;
    private ByteArrayInputStream inputStream;
    private DataInputStream dataInput;

    public DataReader(ByteArrayInputStream data) {
        this.inputStream = data;
        this.dataInput = new DataInputStream(data);
    }

    public DataReader(DataWriter writer) {
        this(new ByteArrayInputStream(writer.outputStream.toByteArray()));
    }

    public int position() {
        return this.position;
    }

    @Override
    public int length() throws IOException {
        return this.dataInput.available();
    }

    public Byte readByte() throws IOException {
        Byte value = this.dataInput.readByte();
        this.position += 1;
        return value;
    }

    public Short readShort() throws IOException {
        byte[] bytes = new byte[2];
        this.readBytes(bytes, 2);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 2).order(ByteOrder.BIG_ENDIAN);

        Short value = buffer.asShortBuffer().get();
        this.position += 2;
        return value;
    }

    public Integer readInt() throws IOException {
        byte[] bytes = new byte[4];
        this.readBytes(bytes, 4);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.BIG_ENDIAN);

        Integer value = buffer.asIntBuffer().get();
        this.position += 4;
        return value;
    }

    public Long readLong() throws IOException {
        byte[] bytes = new byte[8];
        this.readBytes(bytes, 8);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 8).order(ByteOrder.BIG_ENDIAN);

        Long value = buffer.asLongBuffer().get();
        this.position += 8;
        return value;
    }

    public Float readFloat() throws IOException {
        byte[] bytes = new byte[4];
        this.readBytes(bytes, 4);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.BIG_ENDIAN);

        Float value = buffer.asFloatBuffer().get();
        this.position += 4;
        return value;
    }

    public Double readDouble() throws IOException {
        byte[] bytes = new byte[8];
        this.readBytes(bytes, 8);

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 8).order(ByteOrder.BIG_ENDIAN);

        Double value = buffer.asDoubleBuffer().get();
        this.position += 8;
        return value;
    }

    public void readBytes(byte[] buffer, int length) throws IOException {
        int read = this.dataInput.read(buffer, 0, length);
        this.position += read;
        if (read != length) {
            throw new DataInputReadException(length, read);
        }
    }

    public Data readData(int length) throws IOException {
        byte[] buffer = new byte[length];
        this.readBytes(buffer, length);
        return new DataReader(new ByteArrayInputStream(buffer));
    }

    public String readString(int length) throws IOException {
        byte[] buffer = new byte[length];
        this.readBytes(buffer, length);
        return new String(buffer, Charset.forName("UTF-8"));
    }

    public void seek(int position) throws IOException {
        this.dataInput.reset();
        this.dataInput.skip(position);
        this.position = position;
    }
}
