package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase.*
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.parser.FortranParser.*
import org.jetbrains.fortran.lang.parser.FortranParserUtil.parseKeyword
import org.jetbrains.fortran.lang.psi.FortranTokenType
import java.lang.Integer.parseInt

class LabeledDoConstructParser : Parser {

    override fun parse(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "labeled_do_construct")) return false
        if (!nextTokenIs(builder, "<labeled do construct>", FortranTokenType.WORD, FortranTokenType.KEYWORD, IDENTIFIER, INTEGERLITERAL)) return false
        var result: Boolean
        val pinned: Boolean
        val marker = enter_section_(builder, level, _COLLAPSE_, LABELED_DO_CONSTRUCT, "<labeled do construct>")
        val labelValue = parseLabelDoStmt(builder, level + 1)
        result = labelValue != -1
        pinned = result // pin = 1
        result = result && report_error_(builder, eols(builder, level + 1))
        result = result && report_error_(builder, parseLabeledDoBlock(builder, level + 1, labelValue))
        result = pinned && (end_do(builder, level + 1)
                || LabeledDoConstructParser().parse(builder, level + 1)
                || doTermActionStmt(builder, level + 1)) && result
        exit_section_(builder, level, marker, result, pinned, null)
        return result || pinned
    }

    private fun parseDoStmtWithTheSameLabel(builder: PsiBuilder, level: Int, testLabel: Int): Boolean {
        if (!recursion_guard_(builder, level, "label_do_stmt")) return false
        if (!nextTokenIs(builder, "<label do stmt>", FortranTokenType.WORD, FortranTokenType.KEYWORD, IDENTIFIER)) return false
        var result: Boolean
        var pinned = false
        val marker_ = enter_section_(builder, level, _NONE_, LABEL_DO_STMT, "<label do stmt>")
        construct_name_decl(builder, level + 1)
        result = parseKeyword(builder, level + 1, "DO")
        // The label must be here
        if (!nextTokenIs(builder, INTEGERLITERAL)) {
            result = false
        } else {
            val text = builder.tokenText
            if (text != null) result = result && testLabel == parseInt(text)
            result = result && label(builder, level + 1)
            pinned = result // pin = 3
            result = result && report_error_(builder, loop_control(builder, level + 1))
            result = pinned && consumeToken(builder, EOL) && result
        }
        exit_section_(builder, level, marker_, false, false, null)
        return result || pinned
    }

    // eol+
    private fun eols(builder_: PsiBuilder, level_: Int): Boolean {
        if (!recursion_guard_(builder_, level_, "labeled_do_construct_eol")) return false
        val marker_ = enter_section_(builder_)
        val result_: Boolean = consumeToken(builder_, EOL)
        var pos_ = current_position_(builder_)
        while (result_) {
            if (!consumeToken(builder_, EOL)) break
            if (!empty_element_parsed_guard_(builder_, "labeled_do_construct_eol", pos_)) break
            pos_ = current_position_(builder_)
        }
        exit_section_(builder_, marker_, null, result_)
        return result_
    }

    //
    //  do_stmt
    //
    private fun parseLabelDoStmt(builder: PsiBuilder, level: Int): Int {
        if (!recursion_guard_(builder, level, "label_do_stmt")) return -1
        if (!nextTokenIs(builder, "<label do stmt>", FortranTokenType.WORD, FortranTokenType.KEYWORD, IDENTIFIER, INTEGERLITERAL)) return -1
        var result: Boolean
        var pinned = false
        var labelValue = -1
        val marker_ = enter_section_(builder, level, _NONE_, LABEL_DO_STMT, "<label do stmt>")
        label_decl(builder, level + 1)
        construct_name_decl(builder, level + 1)
        result = parseKeyword(builder, level + 1, "DO")
        // The label must be here
        if (!nextTokenIs(builder, INTEGERLITERAL)) {
            result = false
        } else {
            val text = builder.tokenText
            if (text != null) labelValue = parseInt(text)
            result = result && labelValue != -1
            result = result && label(builder, level + 1)
            pinned = result // pin = 3
            consumeToken(builder, COMMA)
            result = result && report_error_(builder, loop_control(builder, level + 1))
        }
        exit_section_(builder, level, marker_, result, pinned, null)
        return if (!(result || pinned)) -1 else labelValue
    }

    //
    //  block
    //
    private fun parseLabeledDoBlock(builder: PsiBuilder, level: Int, stopLabel: Int): Boolean {
        if (!recursion_guard_(builder, level, "block")) return false
        val marker_ = enter_section_(builder, level, _COLLAPSE_, BLOCK, "<block>")
        var pos_ = current_position_(builder)
        while (true) {
            if (!doblock_line(builder, level + 1, stopLabel)) break
            if (!empty_element_parsed_guard_(builder, "block", pos_)) break
            pos_ = current_position_(builder)
        }
        exit_section_(builder, level, marker_, true, false, null)
        return true
    }

    private fun doblock_line(builder: PsiBuilder, level: Int, stopLabel: Int): Boolean {
        if (!recursion_guard_(builder, level, "block_0")) return false
        var result_ = true
        val marker_ = enter_section_(builder)
        // The label may be here
        if (nextTokenIs(builder, INTEGERLITERAL)) {
            val text = builder.tokenText
            if (text != null) result_ = stopLabel != parseInt(text)
        }
        // !LabeledDoConstruct with the same label
        result_ = result_ && !parseDoStmtWithTheSameLabel(builder, level + 1, stopLabel)
        // eol | execution_part_construct
        result_ = result_ && (consumeToken(builder, EOL) || execution_part_construct(builder, level + 1))
        exit_section_(builder, marker_, null, result_)
        return result_
    }

    private fun doTermActionStmt(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "do_term_action_stmt")) return false
        if (!nextTokenIs(builder, INTEGERLITERAL)) return false
        val marker = enter_section_(builder)
        val result: Boolean = execution_part_construct(builder, level + 1)
        exit_section_(builder, marker, null, result)
        return result
    }
}
