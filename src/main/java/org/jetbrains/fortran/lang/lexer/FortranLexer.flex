package org.jetbrains.fortran.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import java.util.Stack;

import static com.intellij.psi.TokenType.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.LINE_COMMENT;
%%

%class _FortranLexer
%implements FlexLexer
%unicode
%caseless
%function advance
%type IElementType
%ctorarg boolean fFixedForm
%{
boolean fFixedForm_;
%}
%init{
fFixedForm_ = fFixedForm;
%init}
%eof{  return;
%eof}

BDIGIT=[0-1](\040*[0-1])*
BINARY_LITERAL="B"\'{BDIGIT}\'|"B"\"{BDIGIT}\"

ODIGIT=[0-7](\040*[0-7])*
OCTAL_LITERAL="O"\'{ODIGIT}\'|"O"\"{ODIGIT}\"

HDIGIT=[0-9A-Fa-f](\040*[0-9A-Fa-f])*
HEX_LITERAL="Z"\'{HDIGIT}\'|"Z"\"{HDIGIT}\"

IDENTIFIER_PART=[:digit:]|[:letter:]|_
IDENTIFIER=[:letter:]{IDENTIFIER_PART}*

LINE_COMMENT="!"[^\r\n]*
WHITE_SPACE_CHAR=[\ \t\f]
EOL=(\n|\r|\r\n)

DEFOPERATOR=\.[:letter:]+\.

DIGIT=[0-9](\040*[0-9])*
SIGNIFICAND={DIGIT}\.(\040*[0-9])+|\.\040*{DIGIT}
FLOATING_POINT_EXPONENT_PART=[eE]\040*(\+|\-)?\040*{DIGIT}
DOUBLE_PRECISION_EXPONENT_PART=[dD]\040*(\+|\-)?\040*{DIGIT}
KIND_PARAM={DIGIT}|{IDENTIFIER}

//xIcon
INTEGER_LITERAL={DIGIT}(_{KIND_PARAM})?
//xRcon
FLOATING_POINT_LITERAL={DIGIT}(\.)?{FLOATING_POINT_EXPONENT_PART}(_{KIND_PARAM})?
                      |{SIGNIFICAND}({FLOATING_POINT_EXPONENT_PART})?(_{KIND_PARAM})?
                      |{DIGIT}\._{KIND_PARAM}
//xDcon
DOUBLE_PRECISION_LITERAL={DIGIT}(\.)?{DOUBLE_PRECISION_EXPONENT_PART}(_{KIND_PARAM})?
                      |{SIGNIFICAND}{DOUBLE_PRECISION_EXPONENT_PART}(_{KIND_PARAM})?


EOL_ESC=\\[\ \t]*\n
ESCAPE_SEQUENCE=\\[^\n]|{EOL_ESC}

FREE_LINE_CONTINUE=(("&"){WHITE_SPACE_CHAR}*{EOL}({WHITE_SPACE_CHAR}?{LINE_COMMENT}{EOL})*({WHITE_SPACE_CHAR}*"&")?)
FIXED_LINE_CONTINUE={WHITE_SPACE_CHAR}*{EOL}(({WHITE_SPACE_CHAR}*{LINE_COMMENT}{EOL})|([cC*][^\r\n]*{EOL}))*(\040{5}[^0\040])

FREE_STRING_LITERAL=({KIND_PARAM}_)?(\"([^\\\"\n]|{ESCAPE_SEQUENCE}|{FREE_LINE_CONTINUE})*(\"|\\)?)| ({KIND_PARAM}_)?('([^\\'\n]|{ESCAPE_SEQUENCE}|{FREE_LINE_CONTINUE})*('|\\)?)
FIXED_STRING_LITERAL=({KIND_PARAM}_)?(\"([^\\\"\n]|{ESCAPE_SEQUENCE}|{FIXED_LINE_CONTINUE})*(\"|\\)?)| ({KIND_PARAM}_)?('([^\\'\n]|{ESCAPE_SEQUENCE}|{FIXED_LINE_CONTINUE})*('|\\)?)

FREE_FORMAT="format"\040*{FREE_LINE_CONTINUE}?"("([^\r\n]|{FREE_LINE_CONTINUE})*")"
FIXED_FORMAT="format"\040*{FIXED_LINE_CONTINUE}"("([^\r\n]|{FIXED_LINE_CONTINUE})*")"

%state FIXEDFORM FREEFORM
%%

<YYINITIAL> {
. { yypushback(1);
    if (fFixedForm_)
        yybegin(FIXEDFORM);
    else
        yybegin(FREEFORM);
  }
}

<FREEFORM> {
     {FREE_LINE_CONTINUE} { return WHITE_SPACE; }
     {FREE_STRING_LITERAL} { return STRINGLITERAL; }
     {FREE_FORMAT} { return FORMATSTMT; }
}

<FIXEDFORM> {
    ^[cC*][^\r\n]* {  return LINE_COMMENT; }
    {FIXED_LINE_CONTINUE} { return WHITE_SPACE; }
    {FIXED_STRING_LITERAL} { return STRINGLITERAL; }
    {FIXED_FORMAT} { return FORMATSTMT; }
    ^{WHITE_SPACE_CHAR}*{LINE_COMMENT} { return LINE_COMMENT; }
    ^[^0-9cC*!\040][^0-9!\040]{4}. { return BAD_CHARACTER; }
    ^[0-9\040][0-9\040]{4}[^\040\n\r] { return BAD_CHARACTER; }
}

<FREEFORM, FIXEDFORM> {
    ({WHITE_SPACE_CHAR})+ { return WHITE_SPACE; }
    (({WHITE_SPACE_CHAR})*({EOL}|(";")))+ { return EOL; }
    {LINE_COMMENT} { return LINE_COMMENT; }

    {INTEGER_LITERAL} { return INTEGERLITERAL; }
    {BINARY_LITERAL} { return BINARYLITERAL; }
    {OCTAL_LITERAL} { return OCTALLITERAL; }
    {HEX_LITERAL} { return HEXLITERAL; }
    {FLOATING_POINT_LITERAL} { return FLOATINGPOINTLITERAL; }
    {DIGIT}\./[^A-Za-z0-9_] { return FLOATINGPOINTLITERAL; }
    {DOUBLE_PRECISION_LITERAL} { return DOUBLEPRECISIONLITERAL; }

    ".true."(_{KIND_PARAM})? { return TRUEKWD; }
    ".false."(_{KIND_PARAM})? { return FALSEKWD; }

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
    "(/" { return ARRAYLBR; }
    "/)" { return ARRAYRBR; }
    "," { return COMMA; }
    "." { return DOT; }
    "$" { return DOLLAR; }
    "%" { return PERC; }
    "&" { return AMP; }
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

    "abstract" { return ABSTRACT; }
    "all" { return ALL; }
    "allocatable" { return ALLOCATABLE; }
    "allocate" { return ALLOCATE; }
    "assign" { return ASSIGN; }
    "assignment" { return ASSIGNMENT; }
    "associate" { return ASSOCIATE; }
    "asynchronous" { return ASYNCHRONOUS; }
    "backspace" { return BACKSPACE; }
    "bind" { return BIND; }
    "block" { return BLOCKKWD; }
    "blockdata" { return BLOCKDATA; }
    "byte" { return BYTE; } // nonstandard data type
    "call" { return CALL; }
    "case" { return CASE; }
    "character" { return CHARACTER; }
    "class" { return CLASSKWD; }
    "close" { return CLOSE; }
    "codimension" { return CODIMENSION; }
    "common" { return COMMON; }
    "complex" { return COMPLEX; }
    "concurrent" { return CONCURRENT; }
    "contains" { return CONTAINS; }
    "contiguous" { return CONTIGUOUS; }
    "continue" { return CONTINUE; }
    "critical" { return CRITICAL; }
    "cycle" { return CYCLE; }
    "data" { return DATA; }
    "deallocate" { return DEALLOCATE; }
    "default" { return DEFAULT; }
    "deferred" { return DEFERRED; }
    "dimension" { return DIMENSION; }
    "do" { return DO; }
    "double" { return DOUBLE; }
    "doubleprecision" { return DOUBLEPRECISION; }
    "precision" { return PRECISION; }
    "elemental" { return ELEMENTAL; }
    "else" { return ELSE; }
    "elseif" { return ELSEIF; }
    "elsewhere" { return ELSEWHERE; }
    "entry" { return ENTRY; }
    "enum" { return ENUM; }
    "enumerator" { return ENUMERATORKWD; }
    "error" { return ERROR; }
    "equivalence" { return EQUIVALENCE; }
    "exit" { return EXIT; }
    "extends" { return EXTENDS; }
    "external" { return EXTERNALKWD; }
    "final" { return FINAL; }
    "flush" { return FLUSH; }
    "function" { return FUNCTION; }
    "forall" { return FORALL; }
    "formatted" { return FORMATTED; }
    "generic" { return GENERIC; }
    "go" { return GO; }
    "goto" { return GOTO; }
    "if" { return IF; }
    "images" { return IMAGES; }
    "implicit" { return IMPLICIT; }
    "import" { return IMPORT; }
    "impure" { return IMPURE; }
    "in" { return IN; }
    "include" { return INCLUDE; }
    "inout" { return INOUT; }
    "integer" { return INTEGER; }
    "intent" { return INTENT; }
    "interface" { return INTERFACE; }
    "intrinsic" { return INTRINSIC; }
    "inquire" { return INQUIRE; }
    "iolength" { return IOLENGTH; }
    "kind" { return KIND; }
    "len" { return LEN; }
    "lock" { return LOCK; }
    "logical" { return LOGICAL; }
    "module" { return MODULEKWD; }
    "memory" { return MEMORY; }
    "name" { return NAMEKWD; }
    "namelist" { return NAMELIST; }
    "none" { return NONE; }
    "non_intrinsic" { return NONINTRINSIC; }
    "non_overridable" { return NONOVERRIDABLE; }
    "nopass" { return NOPASS; }
    "nullify" { return NULLIFY; }
    "only" { return ONLY; }
    "open" { return OPEN; }
    "operator" { return OPERATOR; }
    "optional" { return OPTIONAL; }
    "out" { return OUT; }
    "parameter" { return PARAMETER; }
    "pass" { return PASS; }
    "pause" { return PAUSE; }
    "pointer" { return POINTER; }
    "print" { return PRINT; }
    "private" { return PRIVATEKWD; }
    "procedure" { return PROCEDURE; }
    "program" { return PROGRAMKWD; }
    "protected" { return PROTECTED; }
    "public" { return PUBLICKWD; }
    "pure" { return PURE; }
    "read" { return READ; }
    "real" { return REAL; }
    "recursive" { return RECURSIVE; }
    "result" { return RESULT; }
    "return" { return RETURNKWD; }
    "save" { return SAVE; }
    "select" { return SELECT; }
    "sequence" { return SEQUENCE; }
    "stop" { return STOP; }
    "sync" { return SYNC; }
    "syncall" { return SYNCALL; }
    "syncimages" { return SYNCIMAGES; }
    "syncmemory" { return SYNCMEMORY; }
    "subroutine" { return SUBROUTINE; }
    "submodule" { return SUBMODULEKWD; }
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
    "while" { return WHILE; }
    "rewind" { return REWIND; }
    "write" { return WRITE; }

    "end" { return END; }
    "endassociate" { return ENDASSOCIATE; }
    "endblock" { return ENDBLOCK; }
    "endcritical" { return ENDCRITICAL; }
    "endenum" { return ENDENUM; }
    "endfile" { return ENDFILE; }
    "endif" { return ENDIF; }
    "endprocedure" { return ENDPROCEDURE; }
    "endprogram" { return ENDPROGRAM; }
    "endfunction" { return ENDFUNCTION; }
    "endforall" { return ENDFORALL; }
    "endsubroutine" { return ENDSUBROUTINE; }
    "endsubmodule" { return ENDSUBMODULE; }
    "endtype" { return ENDTYPE; }
    "endwhere" { return ENDWHERE; }
    "endselect" { return ENDSELECT; }
    "enddo" { return ENDDO; }
    "endmodule" { return ENDMODULE; }
    "endblockdata"    { return ENDBLOCKDATA; }
    "endinterface"    { return ENDINTERFACE; }

    {DEFOPERATOR} { return DEFOPERATOR; }
    {IDENTIFIER} { return IDENTIFIER; }

    . { return BAD_CHARACTER; }
}
