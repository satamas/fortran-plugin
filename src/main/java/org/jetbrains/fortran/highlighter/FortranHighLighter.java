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

        keys1.put(INTEGER_LITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(FLOATING_POINT_LITERAL, FortranHighlightingColors.NUMBER);
        keys1.put(DOUBLE_PRECISION_LITERAL, FortranHighlightingColors.NUMBER);

        keys1.put(LINE_COMMENT, FortranHighlightingColors.LINE_COMMENT);
        keys1.put(STRING_LITERAL, FortranHighlightingColors.STRING);

        keys1.put(TokenType.BAD_CHARACTER, FortranHighlightingColors.BAD_CHARACTER);
    }
}
