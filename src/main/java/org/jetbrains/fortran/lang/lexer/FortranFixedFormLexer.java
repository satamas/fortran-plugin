package org.jetbrains.fortran.lang.lexer;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class FortranFixedFormLexer extends FlexAdapter{
    public final static int INITIAL = _FortranFixedFormLexer.YYINITIAL;

    public FortranFixedFormLexer() {
        super(new _FortranFixedFormLexer((Reader) null, true));
    }
}
