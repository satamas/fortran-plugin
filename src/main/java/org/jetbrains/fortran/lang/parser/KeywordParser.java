package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORD;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.WORD;

public class KeywordParser implements GeneratedParserUtilBase.Parser {

    private final String keyword_text;
    public KeywordParser(String keywordType) {
        keyword_text = keywordType;
    }

    @Override
    public boolean parse(final PsiBuilder builder, final int level) {
        if (!recursion_guard_(builder, level, "Identifier")) return false;
        boolean result = false;
        PsiBuilder.Marker marker = enter_section_(builder);
        if (builder.getTokenType() == IDENTIFIER || builder.getTokenType() == WORD
                || builder.getTokenType() == KEYWORD) {
            if (keyword_text.equalsIgnoreCase(builder.getTokenText())) {
                builder.remapCurrentToken(KEYWORD);
                result = consumeToken(builder, KEYWORD);
            }
        }
        exit_section_(builder, marker, null, result);
        return result;
    }
}

