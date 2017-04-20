package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORDS;

public class KeywordParser implements GeneratedParserUtilBase.Parser {

    private final IElementType keyword;
    public KeywordParser(IElementType keywordType) {
        keyword = keywordType;
    }

    @Override
    public boolean parse(final PsiBuilder builder, final int level) {
        if (!recursion_guard_(builder, level, "Identifier")) return false;
        boolean result;
        PsiBuilder.Marker marker = enter_section_(builder);
        result = consumeToken(builder, keyword);
        if (!result && builder.getTokenType() == IDENTIFIER) {
            if (builder.getTokenText().equalsIgnoreCase(keyword.toString())
                    || (builder.getTokenText()+"kwd").equalsIgnoreCase(keyword.toString())) {
                builder.remapCurrentToken(keyword);
                result = consumeToken(builder, keyword);
            }
        }
        exit_section_(builder, marker, null, result);
        return result;
    }
}

