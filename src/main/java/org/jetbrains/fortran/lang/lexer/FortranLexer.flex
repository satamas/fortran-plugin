package org.jetbrains.fortran.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import java.util.Stack;

import static com.intellij.psi.TokenType.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
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

LINE_COMMENT="!"[^\r\n]*
WHITE_SPACE_CHAR=[\ \t\f]
EOL=(\n|\r|\r\n)

OPERATOR='.'[:letter:]+'.'

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

(("&"){WHITE_SPACE_CHAR}*{EOL}) { return WHITE_SPACE; }
({WHITE_SPACE_CHAR})+ { return WHITE_SPACE; }
(({WHITE_SPACE_CHAR})*({EOL}|(";")))+ { return EOL; }
{LINE_COMMENT} { return LINE_COMMENT; }

{STRING_LITERAL} { return STRING_LITERAL; }
{INTEGER_LITERAL} { return INTEGER_LITERAL; }
{FLOATING_POINT_LITERAL} { return FLOATING_POINT_LITERAL; }
{DOUBLE_PRECISION_LITERAL} { return DOUBLE_PRECISION_LITERAL; }

"=" { return EQ; }
"==" { return EQEQ; }
"/=" { return NEQ; }
":" { return COLON; }
"::" { return COLONCOLON; }
"+" { return PLUS; }
"-" { return MINUS; }
"*" { return MUL; }
"**" { return POWER; }
"/" { return DIV; }
"//" { return DIVDIV; }
"(" { return LPAR; }
")" { return RPAR; }
"[" { return LBRACKET; }
"]" { return RBRACKET; }
"," { return COMMA; }
"." { return DOT; }
"$" { return DOLLAR; }
"%" { return PERC; }
"&" { return AMP; }
//";" { return SEMICOLON; }
"<" { return LT; }
"<=" { return LE; }
">" { return GT; }
">=" { return GE; }
"=>" { return POINTER_ASSMNT; }
"?" { return QUEST; }

".eqv." { return LOGICAL_EQ; }
".neqv." { return LOGICAL_NEQ; }
".and." { return AND; }
".or." { return OR; }
".not." { return NOT; }
".eq." { return EQEQ; }
".ne." { return NEQ; }
".lt." { return LT; }
".le." { return LE; }
".gt." { return GT; }
".ge." { return GE; }

".true." { return TRUE; }
".false." { return FALSE; }

"all" { return ALL; }
"allocatable" { return ALLOCATABLE; }/*
"allocate" { return FortranTokens.ALLOCATE_KEYWORD; }
"assign" { return FortranTokens.ASSIGN_KEYWORD; }*/
"assignment" { return ASSIGNMENT; }
"associate" { return ASSOCIATE; }
"asynchronous" { return ASYNCHRONOUS; }
"backspace" { return BACKSPACE; }
"bind" { return BIND; }
"block" { return BLOCKKWD; }
"blockdata" { return BLOCKDATA; }
/*"call" { return FortranTokens.CALL_KEYWORD; }*/
"case" { return CASE; }
"character" { return CHARACTER; }
"class" { return CLASSKWD; }
"close" { return CLOSE; }
"codimension" { return CODIMENSION; } /*
"common" { return FortranTokens.COMMON_KEYWORD; }*/
"complex" { return COMPLEX; }
"concurrent" { return CONCURRENT; } /*
"contains" { return FortranTokens.CONTAINS_KEYWORD; }*/
"contiguous" { return CONTIGUOUS; }
"continue" { return CONTINUE; }
"critical" { return CRITICAL; }
"cycle" { return CYCLE; }
"data" { return DATA; }/*
"deallocate" { return FortranTokens.DEALLOCATE_KEYWORD; }*/
"default" { return DEFAULT; }
"dimension" { return DIMENSION; }
"do" { return DO; }
"double" { return DOUBLE; }
"precision" { return PRECISION; }
"else" { return ELSE; }
"elseif" { return ELSEIF; }
"elsewhere" { return ELSEWHERE; }/*
"entry" { return FortranTokens.ENTRY_KEYWORD; }*/
"error" { return ERROR; }/*
"equivalence" { return FortranTokens.EQUIVALENCE_KEYWORD; }*/
"exit" { return EXIT; }
"external" { return EXTERNAL; }
"flush" { return FLUSH; }
"function" { return FUNCTION; }
"forall" { return FORALL; }
"formatted" { return FORMATTED; }
"go" { return GO; }
"goto" { return GOTO; }
"if" { return IF; }
"images" { return IMAGES; }
"implicit" { return IMPLICIT; }
"import" { return IMPORT; }
"in" { return IN; }
"inout" { return INOUT; }
"integer" { return INTEGER; }
"intent" { return INTENT; }/*
"interface" { return FortranTokens.INTERFACE_KEYWORD; }*/
"intrinsic" { return INTRINSIC; }
"inquire" { return INQUIRE; }
"iolength" { return IOLENGTH; }
"kind" { return KIND; }
/*"len" { return FortranTokens.LEN_KEYWORD; }*/
"lock" { return LOCK; }
"logical" { return LOGICAL; }
"module" { return MODULEKWD; }
"memory" { return MEMORY; }
"name" { return NAMEKWD; }/*
"namelist" { return FortranTokens.NAMELIST_KEYWORD; }*/
"none" { return NONE; }
"non_intrinsic" { return NON_INTRINSIC; } /*
"nullify" { return FortranTokens.NULLIFY_KEYWORD; }*/
"only" { return ONLY; }
"open" { return OPEN; }
"operator" { return OPERATOR; }
"optional" { return OPTIONAL; }
"out" { return OUT; }
"parameter" { return PARAMETER; }/*
"pause" { return FortranTokens.PAUSE_KEYWORD; }*/
"pointer" { return POINTER; }
"print" { return PRINT; }
"private" { return PRIVATE; }
"program" { return PROGRAMKWD; }
"protected" { return PROTECTED; }
"public" { return PUBLIC; }
"read" { return READ; }
"real" { return REAL; }/*
"recursive" { return FortranTokens.RECURSIVE_KEYWORD; }
"result" { return FortranTokens.RESULT_KEYWORD; }
"return" { return FortranTokens.RETURN_KEYWORD; }*/
"save" { return SAVE; }
"select" { return SELECT; }
"stop" { return STOP; }
"sync" { return SYNC; }
"syncall" { return SYNCALL; }
"syncimages" { return SYNCIMAGES; }
"syncmemory" { return SYNCMEMORY; }
"subroutine" { return SUBROUTINE; }
"submodule" { return SUBMODULE; }
"target" { return TARGET; }
"then" { return THEN; }
"to" { return TO; }
"type" { return TYPE; }
"use" { return USE; }
"unformatted" {return UNFORMATTED; }
"unlock" { return UNLOCK; }
"value" { return VALUE; }
"volatile" { return VOLATILE; }
"wait" { return WAIT; }
"where" { return WHERE; }
"while" { return WHILE; }/*
"format" { return FortranTokens.FORMAT_KEYWORD; }*/
/*
"inquire" { return FortranTokens.INQUIRE_KEYWORD; }*/
"rewind" { return REWIND; }
"write" { return WRITE; }

"end" { return END; }
"endassociate" { return ENDASSOCIATE; }
"endblock" { return ENDBLOCK; }
"endcritical" { return ENDCRITICAL; }
"endfile" { return ENDFILE; }
"endif" { return ENDIF; }
"endprogram" { return ENDPROGRAM; }

"endfunction" { return ENDFUNCTION; }
"endforall" { return ENDFORALL; }
"endsubroutine" { return ENDSUBROUTINE; }
"endsubmodule" { return SUBMODULE; }/*
"endtype" { return FortranTokens.ENDTYPE_KEYWORD; }*/
"endwhere" { return ENDWHERE; }
"endselect" { return ENDSELECT; }
"enddo" { return ENDDO; }
"endmodule" { return ENDMODULE; }
"endblockdata"    { return ENDBLOCKDATA; }/*
"endinterface"    { return FortranTokens.ENDINTERFACE_KEYWORD; }*/

{IDENTIFIER} { return IDENTIFIER; }
{OPERATOR} { return DEFOPERATOR; }

. { return BAD_CHARACTER; }

// These keywords are used for specification parameters names
// We probably don't need them in lexer
/*
"access" { return ACCESS; }
"action" { return ACTION; }
"advance" { return ADVANCE; }
"acquired" { return ACQIRED; }
"blank" { return BLANK; }
"decimal" { return DECIMAL; }
"delim" { return DELIM; }
"direct" { return DIRECT; }
"encoding" { return ENCODING; }
"eor" { return EOR; }
"err" { return ERR; }
"errmsg" { return ERRMSG; }
"exist" { return EXIST; }
"file" { return FILE; }
"fmt" { return FMT; }
"form" { return FORM; }
"id" { return ID; }
"iomsg" { return IOMSG; }
"iostat" { return IOSTAT; }
"named" { return NAMED; }
"nextrec" { return NEXTREC; }
"newunit" { return NEWUNIT; }
"nml" { return NML; }
"number" { return NUMBER; }
"opened" { return OPENED; }
"pad" { return PAD; }
"pending" { return PENDING; }
"pos" { return POS; }
"position" { return POSITION; }
"readwrite" { return READWRITE; }
"rec" { return REC; }
"recl" { return RECL; }
"round" { return ROUND; }
"sequential" { return SEQUENTIAL; }
"sign" { return SIGN; }
"size" { return SIZE; }
"stat" { return STAT; }
"status" { return STATUS; }
"stream" { return STREAM; }
"unit" { return UNIT; }*/
