package br.com.insanitech.javabinary;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.storage.DataWriter;
import br.com.insanitech.javabinary.tokenizing.IvarArray;
import br.com.insanitech.javabinary.tokenizing.IvarObject;
import br.com.insanitech.javabinary.tokenizing.IvarToken;
import br.com.insanitech.javabinary.tokenizing.Token;

import static br.com.insanitech.javabinary.tokenizing.DataType.DATA;
import static br.com.insanitech.javabinary.tokenizing.DataType.DOUBLE;
import static br.com.insanitech.javabinary.tokenizing.DataType.FLOAT;
import static br.com.insanitech.javabinary.tokenizing.DataType.INT16;
import static br.com.insanitech.javabinary.tokenizing.DataType.INT32;
import static br.com.insanitech.javabinary.tokenizing.DataType.INT64;
import static br.com.insanitech.javabinary.tokenizing.DataType.INT8;
import static br.com.insanitech.javabinary.tokenizing.DataType.OBJECT;
import static br.com.insanitech.javabinary.tokenizing.DataType.STRING;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by anderson on 27/06/2017.
 */

class SubClass {
    private Integer value = 10;
    private Integer value2 = 25;
    private List<DataReader> array;

    public SubClass() {
        DataWriter writer;
        try {
            writer = new DataWriter(new ByteArrayOutputStream());
            writer.writeString("testando", "testando".length());

            this.array = new ArrayList<>();
            this.array.add(new DataReader(writer));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        this.value2 = value2;
    }

    public List<DataReader> getArray() {
        return array;
    }

    public void setArray(List<DataReader> array) {
        this.array = array;
    }
}

class TestCommand {
    private Integer int1 = 123;
    private Long int2 = 225L;
    private String string = "Testando";
    private SubClass sub;
    private List<Integer> array;
    private List<SubClass> classArray;
    private List<Float> emptyArray = new ArrayList<>();

    public TestCommand() {
        this.array = Arrays.asList( 1, 2, 3, 4, 5 );
        this.classArray = Arrays.asList( new SubClass(), new SubClass() );
    }

    public Integer getInt1() {
        return int1;
    }

    public void setInt1(Integer int1) {
        this.int1 = int1;
    }

    public Long getInt2() {
        return int2;
    }

    public void setInt2(Long int2) {
        this.int2 = int2;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public SubClass getSub() {
        return sub;
    }

    public void setSub(SubClass sub) {
        this.sub = sub;
    }

    public List<Integer> getArray() {
        return array;
    }

    public void setArray(List<Integer> array) {
        this.array = array;
    }

    public List<SubClass> getClassArray() {
        return classArray;
    }

    public void setClassArray(List<SubClass> classArray) {
        this.classArray = classArray;
    }

    public List<Float> getEmptyArray() {
        return emptyArray;
    }

    public void setEmptyArray(List<Float> emptyArray) {
        this.emptyArray = emptyArray;
    }
}

public class JavaBrinaryTests {
    @Test
    public void testIvarToken() throws IOException {
        // Testing integer value as int64
        IvarToken<Long> token1 = new IvarToken<>("value", 25L, Long.class);
        DataReader data1 = token1.encode();

        IvarToken<Long> decodedToken1 = new IvarToken<>(Long.class);
        decodedToken1.decode(data1);
        assertEquals(decodedToken1.getType(), INT64);
        assertEquals(decodedToken1.getName(), "value");
        assertEquals(decodedToken1.getValue(), (Long) 25L);

        // Testing string value
        IvarToken<String> token2 = new IvarToken<>("title", "Mr. Anderson", String.class);
        DataReader data2 = token2.encode();

        IvarToken<String> decodedToken2 = new IvarToken<>(String.class);
        decodedToken2.decode(data2);
        assertEquals(decodedToken2.getType(), STRING);
        assertEquals(decodedToken2.getName(), "title");
        assertEquals(decodedToken2.getValue(), "Mr. Anderson");

        // Testing double value as float
        IvarToken<Double> token3 = new IvarToken<>("value", 50.555D, Double.class);
        DataReader data3 = token3.encode();

        IvarToken<Double> decodedToken3 = new IvarToken<>(Double.class);
        decodedToken3.decode(data3);
        assertEquals(decodedToken3.getType(), DOUBLE);
        assertEquals(decodedToken3.getName(), "value");
        assertEquals(decodedToken3.getValue(), 50.555D);

        // Testing float value
        IvarToken<Float> token4 = new IvarToken<>("value", 1123.23F, Float.class);
        DataReader data4 = token4.encode();

        IvarToken<Float> decodedToken4 = new IvarToken<>(Float.class);
        decodedToken4.decode(data4);
        assertEquals(decodedToken4.getType(), FLOAT);
        assertEquals(decodedToken4.getName(), "value");
        assertEquals(decodedToken4.getValue(), 1123.23F);
    }

    @Test
    public void testIvarTokenData() throws IOException {
        String comparable = "Eu sou legal em data";

        DataWriter writer = new DataWriter(new ByteArrayOutputStream());
        writer.writeString(comparable, comparable.length());

        IvarToken<DataReader> token = new IvarToken<>("data", new DataReader(writer), DataReader.class);
        DataReader data = token.encode();

        IvarToken<DataReader> decodedToken = new IvarToken<>(DataReader.class);
        decodedToken.decode(data);

        String recoveredString = decodedToken.getValue().readString(comparable.length());
        assertEquals(recoveredString, comparable);
    }

    @Test
    public void testEqualOperatorForDataType() {
        assertTrue(INT8.isEqualsType(Byte.class));
        assertTrue(INT16.isEqualsType(Short.class));
        assertTrue(INT32.isEqualsType(Integer.class));
        assertTrue(INT64.isEqualsType(Long.class));
        assertTrue(FLOAT.isEqualsType(Float.class));
        assertTrue(DOUBLE.isEqualsType(Double.class));
        assertTrue(STRING.isEqualsType(String.class));
        assertTrue(DATA.isEqualsType(DataReader.class));
        assertTrue(OBJECT.isEqualsType(IvarObject.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIvarObjectEncodeDecode() throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new IvarToken<>("name", "Ainda vou por um nome!", String.class));
        tokens.add(new IvarToken<>("age", 5, Integer.class));
        IvarObject childObject = new IvarObject("child", tokens);

        ArrayList<Token> fields = new ArrayList<>();
        fields.add(new IvarToken<>("name", "Anderson Lucas C. Ramos", String.class));
        fields.add(new IvarToken<>("age", 28L, Long.class));
        fields.add(new IvarToken<>("weight", 100.0D, Double.class));
        fields.add(childObject);

        IvarObject object = new IvarObject("client", fields);
        DataReader data = object.encode();

        object = new IvarObject();
        object.decode(data);

        IvarToken<String> name = (IvarToken<String>) object.getValue().get(0);
        IvarToken<Long> age = (IvarToken<Long>) object.getValue().get(1);
        IvarToken<Double> weight = (IvarToken<Double>) object.getValue().get(2);
        IvarObject child = (IvarObject) object.getValue().get(3);
        IvarToken<String> childName = (IvarToken<String>) child.getValue().get(0);
        IvarToken<Integer> childAge = (IvarToken<Integer>) child.getValue().get(1);

        assertEquals(name.getValue(), "Anderson Lucas C. Ramos");
        assertEquals(age.getValue(), (Long) 28L);
        assertEquals(weight.getValue(), 100D);

        assertEquals(childName.getValue(), "Ainda vou por um nome!");
        assertEquals(childAge.getValue(), (Integer) 5);
    }

    @Test
    public void testIvarArrayEncodeDecode() throws IOException {
        List<Long> values = Arrays.asList(50L, 40L, 30L, 20L, 10L);
        IvarArray<Long> array = new IvarArray<>("teste", values, Long.class);
        DataReader data = array.encode();

        IvarArray<Long> newArray = new IvarArray<>(Long.class);
        newArray.decode(data);

        assertEquals(newArray.getValue().get(0), (Long) 50L);
        assertEquals(newArray.getValue().get(1), (Long) 40L);
        assertEquals(newArray.getValue().get(2), (Long) 30L);
        assertEquals(newArray.getValue().get(3), (Long) 20L);
        assertEquals(newArray.getValue().get(4), (Long) 10L);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIvarObjectArrayEncodeDecode() throws IOException {
        List<Token> tokens1 = new ArrayList<>();
        tokens1.add(new IvarToken<>("token1", 100, Integer.class));
        List<Token> tokens2 = new ArrayList<>();
        tokens2.add(new IvarToken<>("token2", 200, Integer.class));
        List<IvarObject> objects = Arrays.asList(new IvarObject("object1", tokens1), new IvarObject("object2", tokens2));
        IvarArray<IvarObject> array = new IvarArray<>("teste", objects, IvarObject.class);
        DataReader data = array.encode();

        IvarArray<IvarObject> newArray = new IvarArray<>(IvarObject.class);
        newArray.decode(data);

        assertEquals(newArray.getValue().get(0).getName(), "object1");
        assertEquals(((IvarToken<Integer>) newArray.getValue().get(0).getValue().get(0)).getValue(), (Integer) 100);
        assertEquals(newArray.getValue().get(1).getName(), "object2");
        assertEquals(((IvarToken<Integer>) newArray.getValue().get(1).getValue().get(0)).getValue(), (Integer) 200);
    }

    @Test
    public void testObjectEncDec() throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        ObjectEncoder encoder = new ObjectEncoder();
        ObjectDecoder decoder = new ObjectDecoder();

        TestCommand first = new TestCommand();
        first.setSub(new SubClass());
        DataReader data = encoder.encode(first);

        TestCommand command = new TestCommand();
        decoder.decode(data, command);

        assertEquals (command.getInt1(), (Integer)123);
        assertEquals (command.getInt2(), (Long)225L);
        assertEquals (command.getString(), "Testando");
        assertEquals (command.getSub().getValue(), (Integer)10);
        assertEquals (command.getSub().getValue2(), (Integer)25);

        DataReader reader = command.getSub().getArray().get(0);
        assertEquals (reader.readString("testando".length()),"testando");
        assertEquals (command.getArray().get(0), (Integer)1);
        assertEquals (command.getArray().get(4), (Integer)5);
        assertEquals (command.getClassArray().get(0).getValue(), (Integer)10);
        assertEquals (command.getClassArray().get(1).getValue2(), (Integer)25);
        assertEquals (command.getEmptyArray().size(), 0);
    }
}
