package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

/**
 * Created by Sergei on 13.04.17.
 * The class for manual parsing of the external rules in the bnf grammar
 * This is a copy of generated grammar to test, how it compiles
 */
public class FortranParserUtil extends GeneratedParserUtilBase {

    public static boolean parseIdentifier(PsiBuilder builder_, int level_) {
        return new IdentifierParser().parse(builder_, level_);
    }

    public static boolean parseKeyword(PsiBuilder builder_, int level_, String keyword) {
        return new KeywordParser(keyword).parse(builder_, level_);
    }

    public static boolean parseLabeledDoConstruct(PsiBuilder builder_, int level_) {
        return new LabeledDoConstructParser().parse(builder_, level_);
    }

}
