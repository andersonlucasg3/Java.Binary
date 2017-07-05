package br.com.insanitech.javabinary.storage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Created by anderson on 22/06/2017.
 */

public class DataWriter extends Data {
    ByteArrayOutputStream outputStream;
    DataOutputStream dataOutput;

    public DataWriter(ByteArrayOutputStream data) {
        this.outputStream = data;
        this.dataOutput = new DataOutputStream(data);
    }

    public int length() {
        return this.dataOutput.size();
    }

    public void write(byte value) throws IOException {
        this.writeBytes(new byte[] { value }, 1);
    }

    public void write(short value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(value);
        this.writeBytes(buffer.array(), 2);
    }

    public void write(int value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        this.writeBytes(buffer.array(), 4);
    }

    public void write(long value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(value);
        this.writeBytes(buffer.array(), 8);
    }

    public void write(float value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putFloat(value);
        this.writeBytes(buffer.array(), 4);
    }

    public void write(double value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putDouble(value);
        this.writeBytes(buffer.array(), 8);
    }

    public void writeBytes(byte[] buffer, int length) throws IOException {
        this.dataOutput.write(buffer, 0, length);
    }

    public void writeString(String string, int length) throws IOException {
        byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
        this.writeBytes(bytes, length);
    }

    public void writeData(DataReader data) throws IOException {
        int dataPosition = data.position();
        data.seek(0);
        int length = data.length();
        byte[] buffer = new byte[length];
        data.readBytes(buffer, length);
        this.writeBytes(buffer, length);
        data.seek(dataPosition);
    }
}
