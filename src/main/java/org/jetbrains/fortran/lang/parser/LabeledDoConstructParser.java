package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.FortranTypes.COLON;
import static org.jetbrains.fortran.lang.parser.FortranParser.end_do;
import static org.jetbrains.fortran.lang.parser.FortranParser.execution_part_construct;
import static org.jetbrains.fortran.lang.parser.FortranParser.loop_control;
import static org.jetbrains.fortran.lang.parser.FortranParserUtil.parseLabel;

public class LabeledDoConstructParser implements GeneratedParserUtilBase.Parser {

    @Override
    public boolean parse(PsiBuilder builder_, int level_) {
        if (!recursion_guard_(builder_, level_, "label_do_construct")) return false;
        if (!nextTokenIs(builder_, "<label do construct>", DO, IDENTIFIER)) return false;
        boolean result_, pinned_;
        PsiBuilder.Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, LABELED_DO_CONSTRUCT, "<label do construct>");
        int labelValue = parseLabelDoStmt(builder_, level_ + 1);
        result_ = (labelValue != -1);
        pinned_ = result_; // pin = 1
        result_ = result_ && report_error_(builder_, parseLabeledDoBlock(builder_, level_ + 1, labelValue));
        result_ = pinned_ && (end_do(builder_, level_+1) || doTermActionStmt(builder_, level_ + 1)) && result_;
        exit_section_(builder_, level_, marker_, result_, pinned_, null);
        return result_;/* || pinned_;*/
    }
//
//  do_stmt
//
    public static int parseLabelDoStmt(PsiBuilder builder_, int level_) {
        if (!recursion_guard_(builder_, level_, "label_do_stmt")) return -1;
        if (!nextTokenIs(builder_, "<label do stmt>", DO, IDENTIFIER)) return -1;
        boolean result_, pinned_;
        PsiBuilder.Marker marker_ = enter_section_(builder_, level_, _NONE_, LABEL_DO_STMT, "<label do stmt>");
        result_ = label_do_stmt_0(builder_, level_ + 1);
        result_ = result_ && consumeToken(builder_, DO);
        int labelValue = new LabelParser().parseAndGetLabel(builder_, level_ + 1);
        result_ = result_ && (labelValue != -1);
        System.out.println("Label value is" + labelValue);
        pinned_ = result_; // pin = 3
        result_ = result_ && report_error_(builder_, label_do_stmt_3(builder_, level_ + 1));
        result_ = pinned_ && consumeToken(builder_, EOL) && result_;
        exit_section_(builder_, level_, marker_, result_, pinned_, null);
        if (!(result_ || pinned_)) return -1;
        return labelValue;
    }

    private static boolean label_do_stmt_0(PsiBuilder builder_, int level_) {
        if (!recursion_guard_(builder_, level_, "label_do_stmt_0")) return false;
        label_do_stmt_0_0(builder_, level_ + 1);
        return true;
    }

    // identifier ':'
    private static boolean label_do_stmt_0_0(PsiBuilder builder_, int level_) {
        if (!recursion_guard_(builder_, level_, "label_do_stmt_0_0")) return false;
        boolean result_;
        PsiBuilder.Marker marker_ = enter_section_(builder_);
        result_ = consumeTokens(builder_, 0, IDENTIFIER, COLON);
        exit_section_(builder_, marker_, null, result_);
        return result_;
    }

    // loop_control?
    private static boolean label_do_stmt_3(PsiBuilder builder_, int level_) {
        if (!recursion_guard_(builder_, level_, "label_do_stmt_3")) return false;
        loop_control(builder_, level_ + 1);
        return true;
    }

//
//  block
//

    public static boolean parseLabeledDoBlock (PsiBuilder builder_, int level_, int stopLabel) {
        if (!recursion_guard_(builder_, level_, "block")) return false;
        PsiBuilder.Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK, "<block>");
        int pos_ = current_position_(builder_);
        while (true) {
            if (!doblock_0(builder_, level_ + 1, stopLabel)) break;
            if (!empty_element_parsed_guard_(builder_, "block", pos_)) break;
            pos_ = current_position_(builder_);
        }
        exit_section_(builder_, level_, marker_, true, false, null);
        return true;
    }

    private static boolean doblock_0(PsiBuilder builder_, int level_, int stopLabel) {
        if (!recursion_guard_(builder_, level_, "block_0")) return false;
        boolean result_;
        PsiBuilder.Marker marker_ = enter_section_(builder_);
        result_ = doblock_0_0(builder_, level_ + 1, stopLabel);
        result_ = result_ && doblock_0_1(builder_, level_ + 1);
        exit_section_(builder_, marker_, null, result_);
        return result_;
    }

    // label?
    private static boolean doblock_0_0(PsiBuilder builder_, int level_, int stopLabel) {
        if (!recursion_guard_(builder_, level_, "block_0_0")) return false;
        boolean res = new LabelParser().parseOptionalAndDifferent(builder_, level_ + 1, stopLabel);
        System.out.println(res);
        return res;
    }

    // eol | execution_part_construct
    private static boolean doblock_0_1(PsiBuilder builder_, int level_) {
        if (!recursion_guard_(builder_, level_, "block_0_1")) return false;
        boolean result_;
        PsiBuilder.Marker marker_ = enter_section_(builder_);
        result_ = consumeToken(builder_, EOL);
        if (!result_) result_ = execution_part_construct(builder_, level_ + 1);
        exit_section_(builder_, marker_, null, result_);
        return result_;
    }

    public static boolean doTermActionStmt(PsiBuilder builder_, int level_) {
        if (!recursion_guard_(builder_, level_, "do_term_action_stmt")) return false;
        boolean result_, pinned_;
        PsiBuilder.Marker marker_ = enter_section_(builder_);
        new LabelParser().parse(builder_, level_ + 1);
        result_ = execution_part_construct(builder_, level_ + 1);
        exit_section_(builder_, marker_, null, result_);
        return result_;
    }
}
