package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.fortran.FortranLanguage;
import org.jetbrains.fortran.lang.FortranTypes;

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

    public static IElementType LINE_COMMENT = new FortranTokenType("comment");

    public static IElementType FIRST_WHITE_SPACE = new FortranTokenType("white_space");

    public static IElementType LINE_CONTINUE = new FortranTokenType("line_continue");

    public static IElementType WORD = new FortranTokenType("identifier_or_keyword");

    public static IElementType KEYWORD = new FortranTokenType("keyword");

    public static IElementType CPP = new FortranTokenType("c_pre_processor");

    public static TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE, FIRST_WHITE_SPACE);

    public static TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, CPP, LINE_CONTINUE);

    public static TokenSet STRINGS = TokenSet.create(STRINGLITERAL);

    public static TokenSet WORD_OR_ILITERAL= TokenSet.create(WORD, FortranTypes.INTEGERLITERAL);

}