package br.com.insanitech.javabinary;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.storage.DataWriter;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataReadWriteUnitTest {
    private void read(DataReader readable) throws Exception {
        int int1 = 1;
        int int2 = 2;
        int int3 = 3;
        int int4 = 4;

        float float1 = 10f;
        float float2 = 20f;
        float float3 = 50f;
        float float4 = 75f;

        int oint1 = readable.readInt();
        float ofloat1 = readable.readFloat();
        int oint3 = readable.readInt();
        float ofloat3 = readable.readFloat();
        int oint4 = readable.readInt();
        float ofloat4 = readable.readFloat();
        int oint2 = readable.readInt();
        float ofloat2 = readable.readFloat();

        assertEquals(int1, oint1);
        assertEquals(int2, oint2);
        assertEquals(int3, oint3);
        assertEquals(int4, oint4);

        assertEquals((int)float1, (int)ofloat1);
        assertEquals((int)float2, (int)ofloat2);
        assertEquals((int)float3, (int)ofloat3);
        assertEquals((int)float4, (int)ofloat4);
    }

    @Test
    public void dataWriteRead() throws Exception {
        int int1 = 1;
        int int2 = 2;
        int int3 = 3;
        int int4 = 4;

        float float1 = 10f;
        float float2 = 20f;
        float float3 = 50f;
        float float4 = 75f;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataWriter writable = new DataWriter(outputStream);
        writable.write(int1);
        writable.write(float1);
        writable.write(int3);
        writable.write(float3);
        writable.write(int4);
        writable.write(float4);
        writable.write(int2);
        writable.write(float2);

        outputStream.flush();

        byte[] written = outputStream.toByteArray();

        DataReader readable = new DataReader(written);

        this.read(readable);

        readable.seek(0);
        writable = new DataWriter(new ByteArrayOutputStream());
        writable.writeData(readable);

        readable = new DataReader(writable);
        this.read(readable);
    }

    @Test
    public void testDataReaderSeek() throws IOException {
        DataWriter writer = new DataWriter(new ByteArrayOutputStream());

        writer.write(10);
        writer.write(20);
        writer.write(30);

        DataReader reader = new DataReader(writer);

        reader.seek(4 * 2);
        int first = reader.readInt();
        reader.seek(4);
        int second = reader.readInt();
        reader.seek(0);
        int third = reader.readInt();

        assertEquals(first, 30);
        assertEquals(second, 20);
        assertEquals(third, 10);
    }
}