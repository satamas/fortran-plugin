package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.fortran.FortranLanguage;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.jetbrains.fortran.lang.FortranTypes.*;

/**
 * Created by sergei on 13.03.17.
 */
public class FortranTokenType extends IElementType {
    public FortranTokenType(String debug) {
        super(debug, FortranLanguage.INSTANCE);
    }

    public static TokenSet KEYWORDS = TokenSet.create(
            ALLOCATABLE,
            ASSIGNMENT,
            ASYNCHRONOUS,
            BIND,
            BINDNAME,
            CHARACTER,
            CODIMENSION,
            COMPLEX,
            CONCURRENT,
            CONTIGUOUS,
            CONTINUE,
            DIMENSION,
            DO,
            DOUBLE,
            ELSE,
            ELSEIF,
            END,
            ENDDO,
            ENDIF,
            ENDPROGRAM,
            EXTERNAL,
            FORMATTED,
            IF,
            IMPLICIT,
            IMPORT,
            IN,
            INITIALIZATION,
            INOUT,
            INTEGER,
            INTENT,
            INTRINSIC,
            KIND,
            LOGICAL,
            NONE,
            NON_INTRINSIC,
            ONLY,
            OPERATOR,
            OPTIONAL,
            OUT,
            PARAMETER,
            POINTER,
            PRECISION,
            PRIVATE,
            PROGRAM,
            PROTECTED,
            PUBLIC,
            READ,
            REAL,
            SAVE,
            TARGET,
            THEN,
            UNFORMATTED,
            USE,
            VALUE,
            VOLATILE,
            WHILE,
            WRITE

    );

//    public static TokenSet SOFT_KEYWORDS = TokenSet.create(NONE_KEYWORD, END_KEYWORD, FILE_KEYWORD);

    public static TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE);

    public static TokenSet COMMENTS = TokenSet.create(LINE_COMMENT);

    public static TokenSet STRINGS = TokenSet.create(STRING_LITERAL);

}