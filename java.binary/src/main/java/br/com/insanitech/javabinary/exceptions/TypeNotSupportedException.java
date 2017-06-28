package br.com.insanitech.javabinary.exceptions;

import java.io.IOException;
import java.util.Locale;

import br.com.insanitech.javabinary.tokenizing.DataType;

/**
 * Created by anderson on 22/06/2017.
 */

public class TypeNotSupportedException extends IOException {
    public TypeNotSupportedException(Class<?> type) {
        super(String.format(Locale.getDefault(), "Type %s is not supported yet.", type.toString()));
    }

    public TypeNotSupportedException(DataType type) {
        super(String.format(Locale.getDefault(), "Type %s is not supported yet.", type.toString()));
    }
}
