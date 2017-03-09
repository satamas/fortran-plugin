package org.jetbrains.fortran.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import java.util.Stack;

%%

%class _FortranLexer
%implements FlexLexer
%unicode
%caseless
%function advance
%type IElementType
%eof{  return;
%eof}

IDENTIFIER_PART=[:digit:]|[:letter:]|_
IDENTIFIER=[:letter:]{IDENTIFIER_PART}*

LINE_COMMENT="!"[^\n]*
WHITE_SPACE_CHAR=[\ \n\t\f]

DIGIT=[0-9](\040*[0-9])*
SIGNIFICAND={DIGIT}\040*\.(\040*[0-9])*|\.\040*{DIGIT}
FLOATING_POINT_EXPONENT_PART=[e]\040*(\+|\-)?\040*{DIGIT}
DOUBLE_PRECISION_EXPONENT_PART=[d]\040*(\+|\-)?\040*{DIGIT}

//xIcon
INTEGER_LITERAL={DIGIT}
//xRcon
FLOATING_POINT_LITERAL={DIGIT}{FLOATING_POINT_EXPONENT_PART}|{SIGNIFICAND}({FLOATING_POINT_EXPONENT_PART})?
//xDcon
DOUBLE_PRECISION_LITERAL={DIGIT}{DOUBLE_PRECISION_EXPONENT_PART}|{SIGNIFICAND}{DOUBLE_PRECISION_EXPONENT_PART}

EOL_ESC=\\[\ \t]*\n
ESCAPE_SEQUENCE=\\[^\n]|{EOL_ESC}
STRING_LITERAL=(\"([^\\\"\n]|{ESCAPE_SEQUENCE})*(\"|\\)?)| ('([^\\'\n]|{ESCAPE_SEQUENCE})*('|\\)?)

//REGULAR_STRING_PART=[^\\\'\n]+
//REGULAR_DQ_STRING_PART=[^\\\"\n]+

%%

({WHITE_SPACE_CHAR})+ { return FortranTokens.WHITE_SPACE; }
{LINE_COMMENT} { return FortranTokens.LINE_COMMENT; }
{INTEGER_LITERAL} { return FortranTokens.INTEGER_LITERAL; }

{STRING_LITERAL} { return FortranTokens.STRING_LITERAL; }

{INTEGER_LITERAL} { return FortranTokens.INTEGER_LITERAL; }
{FLOATING_POINT_LITERAL} { return FortranTokens.FLOATING_POINT_LITERAL; }
{DOUBLE_PRECISION_LITERAL} { return FortranTokens.DOUBLE_PRECISION_LITERAL; }

"=" { return FortranTokens.EQ; }
"==" { return FortranTokens.EQEQ; }
"/=" { return FortranTokens.NEQ; }
":" { return FortranTokens.COLON; }
"::" { return FortranTokens.COLONCOLON; }
"+" { return FortranTokens.PLUS; }
"-" { return FortranTokens.MINUS; }
"*" { return FortranTokens.MUL; }
"**" { return FortranTokens.POWER; }
"/" { return FortranTokens.DIV; }
"//" { return FortranTokens.DIVDIV; }
"(" { return FortranTokens.LPAR; }
")" { return FortranTokens.RPAR; }
"[" { return FortranTokens.LBRACKET; }
"]" { return FortranTokens.RBRACKET; }
"," { return FortranTokens.COMMA; }
"." { return FortranTokens.DOT; }
"$" { return FortranTokens.DOLLAR; }
"%" { return FortranTokens.PERC; }
"&" { return FortranTokens.AMP; }
";" { return FortranTokens.SEMICOLON; }
"<" { return FortranTokens.LT; }
"<=" { return FortranTokens.LE; }
">" { return FortranTokens.GT; }
">=" { return FortranTokens.GE; }
"?" { return FortranTokens.QUEST; }

".eqv." { return FortranTokens.LOGICAL_EQ; }
".neqv." { return FortranTokens.LOGICAL_NEQ; }
".and." { return FortranTokens.AND; }
".or." { return FortranTokens.OR; }
".not." { return FortranTokens.NOT; }
".eq." { return FortranTokens.EQEQ; }
".ne." { return FortranTokens.NEQ; }
".lt." { return FortranTokens.LT; }
".le." { return FortranTokens.LE; }
".gt." { return FortranTokens.GT; }
".ge." { return FortranTokens.GE; }

".true." { return FortranTokens.TRUE_KEYWORD; }
".false." { return FortranTokens.FALSE_KEYWORD; }

"allocatable" { return FortranTokens.ALLOCATABLE_KEYWORD; }
"allocate" { return FortranTokens.ALLOCATE_KEYWORD; }
"assign" { return FortranTokens.ASSIGN_KEYWORD; }
"assignment" { return FortranTokens.ASSIGNMENT_KEYWORD; }
"block" { return FortranTokens.BLOCK_KEYWORD; }
"blockdata" { return FortranTokens.BLOCKDATA_KEYWORD; }
"call" { return FortranTokens.CALL_KEYWORD; }
"case" { return FortranTokens.CASE_KEYWORD; }
"character" { return FortranTokens.CHARACTER_KEYWORD; }
"common" { return FortranTokens.COMMON_KEYWORD; }
"complex" { return FortranTokens.COMPLEX_KEYWORD; }
"contains" { return FortranTokens.CONTAINS_KEYWORD; }
"continue" { return FortranTokens.CONTINUE_KEYWORD; }
"cycle" { return FortranTokens.CYCLE_KEYWORD; }
"data" { return FortranTokens.DATA_KEYWORD; }
"deallocate" { return FortranTokens.DEALLOCATE_KEYWORD; }
"default" { return FortranTokens.DEFAULT_KEYWORD; }
"dimension" { return FortranTokens.DIMENSION_KEYWORD; }
"do" { return FortranTokens.DO_KEYWORD; }
"double" { return FortranTokens.DOUBLE_KEYWORD; }
"precision" { return FortranTokens.PRECISION_KEYWORD; }
"else" { return FortranTokens.ELSE_KEYWORD; }
"elseif" { return FortranTokens.ELSEIF_KEYWORD; }
"if" { return FortranTokens.IF_KEYWORD; }
"elsewhere" { return FortranTokens.ELSEWHERE_KEYWORD; }
"entry" { return FortranTokens.ENTRY_KEYWORD; }
"equivalence" { return FortranTokens.EQUIVALENCE_KEYWORD; }
"exit" { return FortranTokens.EXIT_KEYWORD; }
"external" { return FortranTokens.EXTERNAL_KEYWORD; }
"function" { return FortranTokens.FUNCTION_KEYWORD; }
"file" { return FortranTokens.FILE_KEYWORD; }
"go" { return FortranTokens.GO_KEYWORD; }
"to" { return FortranTokens.TO_KEYWORD; }
"goto" { return FortranTokens.GOTO_KEYWORD; }
"if" { return FortranTokens.IF_KEYWORD; }
"implicit" { return FortranTokens.IMPLICIT_KEYWORD; }
"in" { return FortranTokens.IN_KEYWORD; }
"inout" { return FortranTokens.INOUT_KEYWORD; }
"integer" { return FortranTokens.INTEGER_KEYWORD; }
"intent" { return FortranTokens.INTENT_KEYWORD; }
"interface" { return FortranTokens.INTERFACE_KEYWORD; }
"intrinsic" { return FortranTokens.INTRINSIC_KEYWORD; }
"kind" { return FortranTokens.KIND_KEYWORD; }
"len" { return FortranTokens.LEN_KEYWORD; }
"logical" { return FortranTokens.LOGICAL_KEYWORD; }
"module" { return FortranTokens.MODULE_KEYWORD; }
"namelist" { return FortranTokens.NAMELIST_KEYWORD; }
"none" { return FortranTokens.NONE_KEYWORD; }
"nullify" { return FortranTokens.NULLIFY_KEYWORD; }
"only" { return FortranTokens.ONLY_KEYWORD; }
"operator" { return FortranTokens.OPERATOR_KEYWORD; }
"optional" { return FortranTokens.OPTIONAL_KEYWORD; }
"out" { return FortranTokens.OUT_KEYWORD; }
"parameter" { return FortranTokens.PARAMETER_KEYWORD; }
"pause" { return FortranTokens.PAUSE_KEYWORD; }
"pointer" { return FortranTokens.POINTER_KEYWORD; }
"private" { return FortranTokens.PRIVATE_KEYWORD; }
"program" { return FortranTokens.PROGRAM_KEYWORD; }
"public" { return FortranTokens.PUBLIC_KEYWORD; }
"real" { return FortranTokens.REAL_KEYWORD; }
"recursive" { return FortranTokens.RECURSIVE_KEYWORD; }
"result" { return FortranTokens.RESULT_KEYWORD; }
"return" { return FortranTokens.RETURN_KEYWORD; }
"save" { return FortranTokens.SAVE_KEYWORD; }
"select" { return FortranTokens.SELECT_KEYWORD; }
"case" { return FortranTokens.CASE_KEYWORD; }
"stop" { return FortranTokens.STOP_KEYWORD; }
"subroutine" { return FortranTokens.SUBROUTINE_KEYWORD; }
"target" { return FortranTokens.TARGET_KEYWORD; }
"then" { return FortranTokens.THEN_KEYWORD; }
"type" { return FortranTokens.TYPE_KEYWORD; }
"use" { return FortranTokens.USE_KEYWORD; }
"where" { return FortranTokens.WHERE_KEYWORD; }
"while" { return FortranTokens.WHILE_KEYWORD; }
"backspace" { return FortranTokens.BACKSPACE_KEYWORD; }
"close" { return FortranTokens.CLOSE_KEYWORD; }
"format" { return FortranTokens.FORMAT_KEYWORD; }
"inquire" { return FortranTokens.INQUIRE_KEYWORD; }
"open" { return FortranTokens.OPEN_KEYWORD; }
"print" { return FortranTokens.PRINT_KEYWORD; }
"read" { return FortranTokens.READ_KEYWORD; }
"rewind" { return FortranTokens.REWIND_KEYWORD; }
"write" { return FortranTokens.WRITE_KEYWORD; }

"end" { return FortranTokens.END_KEYWORD; }
"endfile" { return FortranTokens.ENDFILE_KEYWORD; }
"endif" { return FortranTokens.ENDIF_KEYWORD; }
"endprogram" { return FortranTokens.ENDPROGRAM_KEYWORD; }

"endfunction" { return FortranTokens.ENDFUNCTION_KEYWORD; }
"endsubroutine" { return FortranTokens.ENDSUBROUTINE_KEYWORD; }
"endtype" { return FortranTokens.ENDTYPE_KEYWORD; }
"endwhere" { return FortranTokens.ENDWHERE_KEYWORD; }
"endselect" { return FortranTokens.ENDSELECT_KEYWORD; }
"enddo" { return FortranTokens.ENDDO_KEYWORD; }
"endmodule" { return FortranTokens.ENDMODULE_KEYWORD; }
"endblockdata"    { return FortranTokens.ENDBLOCKDATA_KEYWORD; }
"endblock"    { return FortranTokens.ENDBLOCK_KEYWORD; }
"endinterface"    { return FortranTokens.ENDINTERFACE_KEYWORD; }

{IDENTIFIER} { return FortranTokens.IDENTIFIER; }

. { return TokenType.BAD_CHARACTER; }