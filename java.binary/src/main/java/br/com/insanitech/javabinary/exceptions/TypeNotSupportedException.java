package br.com.insanitech.javabinary.exceptions;

import java.util.Locale;

/**
 * Created by anderson on 22/06/2017.
 */

public class TypeNotSupportedException extends Exception {
    public TypeNotSupportedException(Class<?> type) {
        super(String.format(Locale.getDefault(), "Type %s is not supported yet.", type.toString()));
    }
}
