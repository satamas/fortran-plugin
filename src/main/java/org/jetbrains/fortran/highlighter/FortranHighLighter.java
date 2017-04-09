package org.jetbrains.fortran.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.lang.lexer.FortranLexer;
import org.jetbrains.fortran.lang.psi.FortranTokenType;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.LINE_COMMENT;

import java.util.HashMap;
import java.util.Map;

public class FortranHighLighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> keys1;
    private static final Map<IElementType, TextAttributesKey> keys2;

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FortranLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(keys1.get(tokenType), keys2.get(tokenType));
    }

    static {
        keys1 = new HashMap<>();
        keys2 = new HashMap<>();

        fillMap(keys1, FortranTokenType.KEYWORDS, FortranHighlightingColors.KEYWORD);

        keys1.put(INTEGERLITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(FLOATINGPOINTLITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(DOUBLEPRECISIONLITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(BINARYLITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(OCTALLITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(HEXLITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(TRUE, FortranHighlightingColors.STRING);
        keys1.put(FALSE, FortranHighlightingColors.STRING);
        keys1.put(LINE_COMMENT, FortranHighlightingColors.LINE_COMMENT);
        keys1.put(STRINGLITERAL, FortranHighlightingColors.STRING);


        keys1.put(TokenType.BAD_CHARACTER, FortranHighlightingColors.BAD_CHARACTER);
    }
}
