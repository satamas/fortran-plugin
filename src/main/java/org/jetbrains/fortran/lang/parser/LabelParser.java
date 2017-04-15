package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static java.lang.Integer.parseInt;
import static org.jetbrains.fortran.lang.FortranTypes.INTEGERLITERAL;
import static org.jetbrains.fortran.lang.FortranTypes.LABEL;

public class LabelParser implements GeneratedParserUtilBase.Parser {

    @Override
    public boolean parse(final PsiBuilder builder_, final int level_) {
        if (!recursion_guard_(builder_, level_, "label")) return false;
        if (!nextTokenIs(builder_, INTEGERLITERAL)) return false;
        boolean result_;
        PsiBuilder.Marker marker_ = enter_section_(builder_);
        result_ = consumeToken(builder_, INTEGERLITERAL);
        exit_section_(builder_, marker_, LABEL, result_);
        return result_;
    }

    public int parseAndGetLabel(final PsiBuilder builder_, final int level_) {
        if (!recursion_guard_(builder_, level_, "label")) return -1;
        if (!nextTokenIs(builder_, INTEGERLITERAL)) return -1;
        boolean result_;
        PsiBuilder.Marker marker_ = enter_section_(builder_);
        String text = builder_.getTokenText();
        int labelValue = -1;
        if (text != null) labelValue = parseInt(text);
        result_ = consumeToken(builder_, INTEGERLITERAL);
        exit_section_(builder_, marker_, LABEL, result_);
        if (!result_) return -1;
        return labelValue;
    }

    public boolean parseOptionalAndDifferent (final PsiBuilder builder_, final int level_, final int value) {
        if (!recursion_guard_(builder_, level_, "label")) return false;
        if (!nextTokenIs(builder_, INTEGERLITERAL)) return true;
        boolean result_;
        PsiBuilder.Marker marker_ = enter_section_(builder_);
        String text = builder_.getTokenText();
        int labelValue = -1;
        if (text != null) labelValue = parseInt(text);
        result_ = (labelValue != value) && consumeToken(builder_, INTEGERLITERAL);
        exit_section_(builder_, marker_, LABEL, result_);
        return result_;
    }
}
