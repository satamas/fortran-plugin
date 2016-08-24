package org.jetbrains.fortran.lang.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public interface FortranTokens {
    IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    FortranToken LINE_COMMENT = new FortranToken("LINE_COMMENT");

    FortranToken INTEGER_LITERAL = new FortranToken("INTEGER_LITERAL");
    FortranToken FLOATING_POINT_LITERAL = new FortranToken("FLOATING_POINT_LITERAL");
    FortranToken DOUBLE_PRECISION_LITERAL = new FortranToken("DOUBLE_PRECISION_LITERAL");
    FortranToken IDENTIFIER = new FortranToken("IDENTIFIER");
    FortranToken DANGLING_NEWLINE = new FortranToken("DANGLING_NEWLINE");

    FortranToken LOGICAL_EQ = new FortranToken(".EQV.");
    FortranToken LOGICAL_NEQ = new FortranToken(".NEQV.");
    FortranToken OR = new FortranToken(".OR.");
    FortranToken AND = new FortranToken(".AND.");
    FortranToken NOT = new FortranToken(".not.");

    FortranSingleValueToken EQ = new FortranSingleValueToken("EQ", "=");
    FortranSingleValueToken NEQ = new FortranSingleValueToken("NEQ", "/=");
    FortranSingleValueToken EQEQ = new FortranSingleValueToken("EQEQ", "==");
    FortranSingleValueToken COLON = new FortranSingleValueToken("COLON", ":");
    FortranSingleValueToken COLONCOLON = new FortranSingleValueToken("COLONCOLON", "::");
    FortranSingleValueToken PLUS = new FortranSingleValueToken("PLUS", "+");
    FortranSingleValueToken MINUS = new FortranSingleValueToken("MINUS", "-");
    FortranSingleValueToken MUL = new FortranSingleValueToken("MUL", "*");
    FortranSingleValueToken POWER = new FortranSingleValueToken("MUL", "**");
    FortranSingleValueToken DIV = new FortranSingleValueToken("DIV", "/");
    FortranSingleValueToken DIVDIV = new FortranSingleValueToken("DIVDIV", "//");
    FortranSingleValueToken LPAR = new FortranSingleValueToken("LPAR", "(");
    FortranSingleValueToken RPAR = new FortranSingleValueToken("RPAR", ")");
    FortranSingleValueToken LBRACKET = new FortranSingleValueToken("LBRACKET", "[");
    FortranSingleValueToken RBRACKET = new FortranSingleValueToken("RBRACKET", "]");
    FortranSingleValueToken COMMA = new FortranSingleValueToken("COMMA", ",");
    FortranSingleValueToken DOT = new FortranSingleValueToken("DOT", ".");
    FortranSingleValueToken SEMICOLON = new FortranSingleValueToken("SEMICOLON", ";");
    FortranSingleValueToken LT = new FortranSingleValueToken("LT", "<");
    FortranSingleValueToken LE = new FortranSingleValueToken("LE", "<=");
    FortranSingleValueToken GT = new FortranSingleValueToken("GT", ">");
    FortranSingleValueToken GE = new FortranSingleValueToken("GE", ">=");
    FortranSingleValueToken QUEST = new FortranSingleValueToken("QUEST", "?");
    FortranSingleValueToken AMP = new FortranSingleValueToken("AMP", "&");
    FortranSingleValueToken PERC = new FortranSingleValueToken("PERC", "%");
    FortranSingleValueToken DOLLAR = new FortranSingleValueToken("DOLLAR", "$");

    FortranToken OPENING_QUOTE = new FortranToken("OPENING_QUOTE");
    FortranToken CLOSING_QUOTE = new FortranToken("CLOSING_QUOTE");
    FortranToken REGULAR_STRING_PART = new FortranToken("REGULAR_STRING_PART");

    FortranToken TRUE_KEYWORD = FortranKeywordToken.keyword(".true.");
    FortranToken FALSE_KEYWORD = FortranKeywordToken.keyword(".false.");

    FortranToken ALLOCATABLE_KEYWORD = FortranKeywordToken.keyword("allocatable");
    FortranToken ALLOCATE_KEYWORD = FortranKeywordToken.keyword("allocate");
    FortranToken ASSIGN_KEYWORD = FortranKeywordToken.keyword("assign");
    FortranToken ASSIGNMENT_KEYWORD = FortranKeywordToken.keyword("assignment");
    FortranToken BLOCK_KEYWORD = FortranKeywordToken.keyword("block");
    FortranToken CALL_KEYWORD = FortranKeywordToken.keyword("call");
    FortranToken CASE_KEYWORD = FortranKeywordToken.keyword("case");
    FortranToken CHARACTER_KEYWORD = FortranKeywordToken.keyword("character");
    FortranToken COMMON_KEYWORD = FortranKeywordToken.keyword("common");
    FortranToken COMPLEX_KEYWORD = FortranKeywordToken.keyword("complex");
    FortranToken CONTAINS_KEYWORD = FortranKeywordToken.keyword("contains");
    FortranToken CONTINUE_KEYWORD = FortranKeywordToken.keyword("continue");
    FortranToken CYCLE_KEYWORD = FortranKeywordToken.keyword("cycle");
    FortranToken DATA_KEYWORD = FortranKeywordToken.keyword("data");
    FortranToken DEALLOCATE_KEYWORD = FortranKeywordToken.keyword("deallocate");
    FortranToken DEFAULT_KEYWORD = FortranKeywordToken.keyword("default");
    FortranToken DIMENSION_KEYWORD = FortranKeywordToken.keyword("dimension");
    FortranToken DO_KEYWORD = FortranKeywordToken.keyword("do");
    FortranToken DOUBLE_KEYWORD = FortranKeywordToken.keyword("double");
    FortranToken PRECISION_KEYWORD = FortranKeywordToken.keyword("precision");
    FortranToken ELSE_KEYWORD = FortranKeywordToken.keyword("else");
    FortranToken IF_KEYWORD = FortranKeywordToken.keyword("if");
    FortranToken ELSEWHERE_KEYWORD = FortranKeywordToken.keyword("elsewhere");
    FortranToken ENTRY_KEYWORD = FortranKeywordToken.keyword("entry");
    FortranToken EQUIVALENCE_KEYWORD = FortranKeywordToken.keyword("equivalence");
    FortranToken EXIT_KEYWORD = FortranKeywordToken.keyword("exit");
    FortranToken EXTERNAL_KEYWORD = FortranKeywordToken.keyword("external");
    FortranToken GO_KEYWORD = FortranKeywordToken.keyword("go");
    FortranToken TO_KEYWORD = FortranKeywordToken.keyword("to");
    FortranToken IMPLICIT_KEYWORD = FortranKeywordToken.keyword("implicit");
    FortranToken IN_KEYWORD = FortranKeywordToken.keyword("in");
    FortranToken INOUT_KEYWORD = FortranKeywordToken.keyword("inout");
    FortranToken INTEGER_KEYWORD = FortranKeywordToken.keyword("integer");
    FortranToken INTENT_KEYWORD = FortranKeywordToken.keyword("intent");
    FortranToken INTERFACE_KEYWORD = FortranKeywordToken.keyword("interface");
    FortranToken INTRINSIC_KEYWORD = FortranKeywordToken.keyword("intrinsic");
    FortranToken KIND_KEYWORD = FortranKeywordToken.keyword("kind");
    FortranToken LEN_KEYWORD = FortranKeywordToken.keyword("len");
    FortranToken LOGICAL_KEYWORD = FortranKeywordToken.keyword("logical");
    FortranToken MODULE_KEYWORD = FortranKeywordToken.keyword("module");
    FortranToken NAMELIST_KEYWORD = FortranKeywordToken.keyword("namelist");
    FortranToken NONE_KEYWORD = FortranKeywordToken.softKeyword("none");
    FortranToken NULLIFY_KEYWORD = FortranKeywordToken.keyword("nullify");
    FortranToken ONLY_KEYWORD = FortranKeywordToken.keyword("only");
    FortranToken OPERATOR_KEYWORD = FortranKeywordToken.keyword("operator");
    FortranToken OPTIONAL_KEYWORD = FortranKeywordToken.keyword("optional");
    FortranToken OUT_KEYWORD = FortranKeywordToken.keyword("out");
    FortranToken PARAMETER_KEYWORD = FortranKeywordToken.keyword("parameter");
    FortranToken PAUSE_KEYWORD = FortranKeywordToken.keyword("pause");
    FortranToken POINTER_KEYWORD = FortranKeywordToken.keyword("pointer");
    FortranToken PRIVATE_KEYWORD = FortranKeywordToken.keyword("private");
    FortranToken PROGRAM_KEYWORD = FortranKeywordToken.keyword("program");
    FortranToken PUBLIC_KEYWORD = FortranKeywordToken.keyword("public");
    FortranToken REAL_KEYWORD = FortranKeywordToken.keyword("real");
    FortranToken RECURSIVE_KEYWORD = FortranKeywordToken.keyword("recursive");
    FortranToken RESULT_KEYWORD = FortranKeywordToken.keyword("result");
    FortranToken RETURN_KEYWORD = FortranKeywordToken.keyword("return");
    FortranToken SAVE_KEYWORD = FortranKeywordToken.keyword("save");
    FortranToken SELECT_KEYWORD = FortranKeywordToken.keyword("select");
    FortranToken STOP_KEYWORD = FortranKeywordToken.keyword("stop");
    FortranToken SUBROUTINE_KEYWORD = FortranKeywordToken.keyword("subroutine");
    FortranToken TARGET_KEYWORD = FortranKeywordToken.keyword("target");
    FortranToken THEN_KEYWORD = FortranKeywordToken.keyword("then");
    FortranToken TYPE_KEYWORD = FortranKeywordToken.keyword("type");
    FortranToken USE_KEYWORD = FortranKeywordToken.keyword("use");
    FortranToken WHERE_KEYWORD = FortranKeywordToken.keyword("where");
    FortranToken WHILE_KEYWORD = FortranKeywordToken.keyword("while");
    FortranToken BACKSPACE_KEYWORD = FortranKeywordToken.keyword("backspace");
    FortranToken CLOSE_KEYWORD = FortranKeywordToken.keyword("close");
    FortranToken ENDFILE_KEYWORD = FortranKeywordToken.keyword("endfile");
    FortranToken FORMAT_KEYWORD = FortranKeywordToken.keyword("format");
    FortranToken INQUIRE_KEYWORD = FortranKeywordToken.keyword("inquire");
    FortranToken OPEN_KEYWORD = FortranKeywordToken.keyword("open");
    FortranToken PRINT_KEYWORD = FortranKeywordToken.keyword("print");
    FortranToken READ_KEYWORD = FortranKeywordToken.keyword("read");
    FortranToken REWIND_KEYWORD = FortranKeywordToken.keyword("rewind");
    FortranToken WRITE_KEYWORD = FortranKeywordToken.keyword("write");
    FortranToken FUNCTION_KEYWORD = FortranKeywordToken.keyword("function");
    FortranToken END_KEYWORD = FortranKeywordToken.keyword("end");

    TokenSet KEYWORDS = TokenSet.create(
            TRUE_KEYWORD, FALSE_KEYWORD,
            ALLOCATABLE_KEYWORD, ALLOCATE_KEYWORD, ASSIGN_KEYWORD, ASSIGNMENT_KEYWORD, BLOCK_KEYWORD,
            CALL_KEYWORD, CASE_KEYWORD, CHARACTER_KEYWORD, COMMON_KEYWORD, COMPLEX_KEYWORD, CONTAINS_KEYWORD,
            CONTINUE_KEYWORD, CYCLE_KEYWORD, DATA_KEYWORD, DEALLOCATE_KEYWORD, DEFAULT_KEYWORD, DIMENSION_KEYWORD, DO_KEYWORD,
            DOUBLE_KEYWORD, PRECISION_KEYWORD, ELSE_KEYWORD, IF_KEYWORD, ELSEWHERE_KEYWORD, ENTRY_KEYWORD,
            EQUIVALENCE_KEYWORD, EXIT_KEYWORD, EXTERNAL_KEYWORD, FUNCTION_KEYWORD, GO_KEYWORD, TO_KEYWORD,
            IMPLICIT_KEYWORD, IN_KEYWORD, INOUT_KEYWORD, INTEGER_KEYWORD, INTENT_KEYWORD, INTERFACE_KEYWORD,
            INTRINSIC_KEYWORD, KIND_KEYWORD, LEN_KEYWORD, LOGICAL_KEYWORD, LINE_COMMENT, MODULE_KEYWORD,
            NAMELIST_KEYWORD, NULLIFY_KEYWORD, ONLY_KEYWORD, OPERATOR_KEYWORD, OPTIONAL_KEYWORD, OUT_KEYWORD,
            PARAMETER_KEYWORD, PAUSE_KEYWORD, POINTER_KEYWORD, PRIVATE_KEYWORD, PUBLIC_KEYWORD, REAL_KEYWORD,
            RECURSIVE_KEYWORD, RESULT_KEYWORD, RETURN_KEYWORD, SAVE_KEYWORD, SELECT_KEYWORD, STOP_KEYWORD,
            SUBROUTINE_KEYWORD, TARGET_KEYWORD, THEN_KEYWORD, TYPE_KEYWORD, USE_KEYWORD, WHERE_KEYWORD,
            WHILE_KEYWORD, BACKSPACE_KEYWORD, CLOSE_KEYWORD, ENDFILE_KEYWORD, FORMAT_KEYWORD, INQUIRE_KEYWORD,
            OPEN_KEYWORD, PRINT_KEYWORD, READ_KEYWORD, REWIND_KEYWORD, WRITE_KEYWORD, PROGRAM_KEYWORD, END_KEYWORD);

    TokenSet SOFT_KEYWORDS = TokenSet.create(NONE_KEYWORD);

    TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE);

    TokenSet COMMENTS = TokenSet.create(LINE_COMMENT);

    TokenSet STRINGS = TokenSet.create(REGULAR_STRING_PART);
}
