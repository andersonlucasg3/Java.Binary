package br.com.insanitech.javabinary.storage;

import java.io.IOException;

/**
 * Created by anderson on 27/06/2017.
 */

public abstract class Data {
    public abstract int length() throws IOException;
}
