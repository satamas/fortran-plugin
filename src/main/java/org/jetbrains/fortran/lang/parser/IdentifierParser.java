package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORDS;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.WORD;

public class IdentifierParser implements GeneratedParserUtilBase.Parser {
    @Override
    public boolean parse(final PsiBuilder builder, final int level) {
        if (!recursion_guard_(builder, level, "Identifier")) return false;
        boolean result;
        PsiBuilder.Marker marker = enter_section_(builder);
        result = consumeToken(builder, IDENTIFIER);
        if (!result) {
            if (builder.getTokenType() == WORD || KEYWORDS.contains(builder.getTokenType())) {
                builder.remapCurrentToken(IDENTIFIER);
                result = consumeToken(builder, IDENTIFIER);
            }
        }
        exit_section_(builder, marker, null, result);
        return result;
    }
}
