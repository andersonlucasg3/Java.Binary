package br.com.insanitech.javabinary.tokenizing;

import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import br.com.insanitech.javabinary.storage.DataReader;
import br.com.insanitech.javabinary.storage.DataWriter;

/**
 * Created by anderson on 22/06/2017.
 */

public class IvarObject extends IvarToken<List<Token>> {
    public IvarObject() throws IOException {
        super(null);
        this.findType(null);
        this.setName("");
    }

    @SuppressWarnings("unchecked")
    public IvarObject(String name, @NonNull List<Token> value) throws IOException {
        super((Class<List<Token>>) value.getClass());
        this.findType(null);
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public void findType(Class<List<Token>> type) throws IOException {
        this.setType(DataType.OBJECT);
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
        for (Token token : this.getValue()) {
            inData.writeData(token.encode());
        }
    }

    // MARK: Decoding implementations

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

    @Override
    public void readValue(DataReader bytes) throws IOException {
        Integer count = this.readOther(Integer.class, bytes);

        ArrayList<Token> value = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DataType type = DataType.valueOf(this.readOther(Byte.class, bytes, false));
            Token token = type.getIvarInstance();
            assert token != null;
            token.decode(bytes);
            value.add(i, token);
        }
        this.setValue(value);
    }
}
