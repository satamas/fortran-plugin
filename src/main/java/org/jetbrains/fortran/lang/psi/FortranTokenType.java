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
            ACCESS,
            ACTION,
            ADVANCE,
            ALL,
            ALLOCATABLE,
            ASSIGNMENT,
            ASSOCIATE,
            ASYNCHRONOUS,
            BACKSPACE,
            BIND,
            BLANK,
            BLOCK,
            CASE,
            CHARACTER,
            FCLASS, // class
            CLOSE,
            CODIMENSION,
            COMPLEX,
            CONCURRENT,
            CONTIGUOUS,
            CONTINUE,
            CRITICAL,
            CYCLE,
            DECIMAL,
            DEFAULT,
            DELIM,
            DIMENSION,
            DIRECT,
            DO,
            DOUBLE,
            ELSE,
            ELSEIF,
            ELSEWHERE,
            ENCODING,
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
            EOR,
            ERR,
            ERRMSG,
            ERROR,
            EXIST,
            EXIT,
            EXTERNAL,
            FILE,
            FLUSH,
            FMT,
            FORALL,
            FORM,
            FORMATTED,
            GO,
            GOTO,
            ID,
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
            IOMSG,
            IOSTAT,
            KIND,
            LOCK,
            LOGICAL,
            MEMORY,
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
            SELECT,
            SEQUENTIAL,
            SIGN,
            SIZE,
            STREAM,
            STAT,
            STATUS,
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

}