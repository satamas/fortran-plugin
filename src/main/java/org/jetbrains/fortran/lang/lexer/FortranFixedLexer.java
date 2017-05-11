package org.jetbrains.fortran.lang.lexer;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class FortranFixedLexer extends FlexAdapter{
    public final static int INITIAL = _FortranLexer.FIXEDFORM;

    public FortranFixedLexer(boolean fFixedForm) {
        super(new _FortranLexer((Reader) null, fFixedForm));
    }
}
