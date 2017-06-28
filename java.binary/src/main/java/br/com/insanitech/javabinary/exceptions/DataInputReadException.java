package br.com.insanitech.javabinary.exceptions;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by anderson on 26/06/2017.
 */

public class DataInputReadException extends IOException {
    public DataInputReadException(int length, int read) {
        super(String.format(Locale.getDefault(), "Was expecting to read string of length %s, was read %s bytes.", length, read));
    }
}
