package br.com.insanitech.javabinary;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by anderson on 26/06/2017.
 */

public abstract class Encodable {
    public abstract ByteBuffer encode() throws IOException;

    public void writeString(String string) {

    }
}
