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
            ACCESS,
            ACTION,
            ADVANCE,
            ALLOCATABLE,
            ASSIGNMENT,
            ASYNCHRONOUS,
            BACKSPACE,
            BIND,
            BLANK,
            CHARACTER,
            FCLASS, // class
            CLOSE,
            CODIMENSION,
            COMPLEX,
            CONCURRENT,
            CONTIGUOUS,
            CONTINUE,
            DECIMAL,
            DELIM,
            DIMENSION,
            DIRECT,
            DO,
            DOUBLE,
            ELSE,
            ELSEIF,
            ENCODING,
            END,
            ENDDO,
            ENDFILE,
            ENDIF,
            ENDPROGRAM,
            EOR,
            ERR,
            EXIST,
            EXTERNAL,
            FILE,
            FLUSH,
            FMT,
            FORM,
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
            INQUIRE,
            IOLENGTH,
            IOMSG,
            IOSTAT,
            KIND,
            LOGICAL,
            FNAME, // name
            NAMED,
            NEXTREC,
            NEWUNIT,
            NML,
            NONE,
            NON_INTRINSIC,
            NUMBER,
            ONLY,
            OPEN,
            OPENED,
            OPERATOR,
            OPTIONAL,
            OUT,
            PARAMETER,
            PAD,
            PENDING,
            POS,
            POSITION,
            POINTER,
            PRECISION,
            PRINT,
            PRIVATE,
            PROGRAM,
            PROTECTED,
            PUBLIC,
            READ,
            READWRITE,
            REAL,
            REC,
            RECL,
            REWIND,
            ROUND,
            SAVE,
            SEQUENTIAL,
            SIGN,
            SIZE,
            STREAM,
            STATUS,
            TARGET,
            THEN,
            UNFORMATTED,
            USE,
            VALUE,
            VOLATILE,
            WAIT,
            WHILE,
            WRITE

    );

//    public static TokenSet SOFT_KEYWORDS = TokenSet.create(NONE_KEYWORD, END_KEYWORD, FILE_KEYWORD);

    public static TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE);

    public static TokenSet COMMENTS = TokenSet.create(LINE_COMMENT);

    public static TokenSet STRINGS = TokenSet.create(STRING_LITERAL);

}