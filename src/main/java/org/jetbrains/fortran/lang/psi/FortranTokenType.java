package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.fortran.FortranLanguage;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.jetbrains.fortran.lang.FortranTypes.*;

/**
 * Created by sergei on 13.03.17.
 * to have keywords highlighting
 * parameters type keywords must be deleted from here
 */
public class FortranTokenType extends IElementType {
    public FortranTokenType(String debug) {
        super(debug, FortranLanguage.INSTANCE);
    }

    public static TokenSet KEYWORDS = TokenSet.create(
            ALL,
            ALLOCATABLE,
            ASSIGNMENT,
            ASSOCIATE,
            ASYNCHRONOUS,
            BACKSPACE,
            BIND,
            BLOCKKWD,
            CASE,
            CHARACTER,
            CLASSKWD,
            CLOSE,
            CODIMENSION,
            COMPLEX,
            CONCURRENT,
            CONTIGUOUS,
            CONTINUE,
            CRITICAL,
            CYCLE,
            DEFAULT,
            DIMENSION,
            DO,
            DOUBLE,
            ELSE,
            ELSEIF,
            ELSEWHERE,
            END,
            ENDASSOCIATE,
            ENDBLOCK,
            ENDCRITICAL,
            ENDDO,
            ENDFILE,
            ENDFORALL,
            ENDIF,
            ENDPROGRAM,
            ENDSELECT,
            ENDWHERE,
            ERROR,
            EXIT,
            EXTERNAL,
            FLUSH,
            FORALL,
            FORMATTED,
            GO,
            GOTO,
            IF,
            IMAGES,
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
            KIND,
            LOCK,
            LOGICAL,
            MEMORY,
            NAMEKWD,
            NONE,
            NON_INTRINSIC,
            ONLY,
            OPEN,
            OPERATOR,
            OPTIONAL,
            OUT,
            PARAMETER,
            POINTER,
            PRECISION,
            PRINT,
            PRIVATE,
            PROGRAMKWD,
            PROTECTED,
            PUBLIC,
            READ,
            REAL,
            REWIND,
            SAVE,
            SELECT,
            STOP,
            SYNC,
            SYNCALL,
            SYNCIMAGES,
            SYNCMEMORY,
            TARGET,
            THEN,
            TO,
            TYPE,
            UNFORMATTED,
            UNLOCK,
            USE,
            VALUE,
            VOLATILE,
            WAIT,
            WHERE,
            WHILE,
            WRITE
    );

//    public static TokenSet SOFT_KEYWORDS = TokenSet.create(NONE_KEYWORD, END_KEYWORD, FILE_KEYWORD);

    public static TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE);

    public static TokenSet COMMENTS = TokenSet.create(LINE_COMMENT);

    public static TokenSet STRINGS = TokenSet.create(STRING_LITERAL);

// These keywords are used for specification parameters names
// We'll use them one day
/*   public static TokenSet PARAM_KEYWORDS = TokenSet.create(
        ACCESS,
        ACTION,
        ACQUIRED,
        ADVANCE,
        BLANK,
        DECIMAL,
        DELIM,
        DIRECT,
        ENCODING,
        EOR,
        ERR,
        ERRMSG,
        EXIST,
        FILE,
        FMT,
        FORM,
        ID,
        IOMSG,
        IOSTAT,
        NAMED,
        NEXTREC,
        NEWUNIT,
        NML,
        NUMBER,
        OPENED,
        PAD,
        PENDING,
        POS,
        POSITION,
        READWRITE,
        REC,
        RECL,
        ROUND,
        SEQUENTIAL,
        SIGN,
        SIZE,
        STAT,
        STATUS,
        STREAM,
        UNIT
    );*/
}