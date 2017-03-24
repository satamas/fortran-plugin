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
            ADVANCE,
            ALLOCATABLE,
            ASSIGNMENT,
            ASYNCHRONOUS,
            BIND,
            BINDNAME,
            BLANK,
            CHARACTER,
            CODIMENSION,
            COMPLEX,
            CONCURRENT,
            CONTIGUOUS,
            CONTINUE,
            DECIMAL,
            DELIM,
            DIMENSION,
            DO,
            DOUBLE,
            ELSE,
            ELSEIF,
            END,
            ENDDO,
            ENDIF,
            ENDPROGRAM,
            EOR,
            ERR,
            EXTERNAL,
            FMT,
            FORMATTED,
            ID,
            IF,
            IMPLICIT,
            IMPORT,
            IN,
            INITIALIZATION,
            INOUT,
            INTEGER,
            INTENT,
            INTRINSIC,
            IOMSG,
            IOSTAT,
            KIND,
            LOGICAL,
            NML,
            NONE,
            NON_INTRINSIC,
            ONLY,
            OPERATOR,
            OPTIONAL,
            OUT,
            PARAMETER,
            PAD,
            POS,
            POINTER,
            PRECISION,
            PRINT,
            PRIVATE,
            PROGRAM,
            PROTECTED,
            PUBLIC,
            READ,
            REAL,
            REC,
            ROUND,
            SAVE,
            SIGN,
            SIZE,
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