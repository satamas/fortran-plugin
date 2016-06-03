package org.jetbrains.fortran.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.lang.lexer.FortranLexer;
import org.jetbrains.fortran.lang.lexer.FortranToken;
import org.jetbrains.fortran.lang.lexer.FortranTokens;

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

        fillMap(keys1, FortranTokens.KEYWORDS, FortranHighlightingColors.KEYWORD);
        keys1.put(FortranTokens.LINE_COMMENT, FortranHighlightingColors.LINE_COMMENT);
    }
}
