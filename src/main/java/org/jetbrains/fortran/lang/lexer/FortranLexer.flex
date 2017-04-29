package org.jetbrains.fortran.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import java.util.Stack;

import static com.intellij.psi.TokenType.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.LINE_COMMENT;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.LINE_CONTINUE;
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

    private static final class State {
        final int state;

        public State(int state	) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "yystate = " + state;
        }
    }

    private final Stack<State> states = new Stack<State>();


    private void pushState(int state) {
        states.push(new State(yystate()));
        yybegin(state);
    }

    private void popState() {
        State state = states.pop();
        yybegin(state.state);
    }
%}
%init{
fFixedForm_ = fFixedForm;
if (fFixedForm_)
    pushState(FIXEDFORM);
else
    pushState(FREEFORM);
%init}
%eof{  return;
%eof}

%xstate FREEFORM FIXEDFORM QUOTE_FIXED_STRING QUOTE_FREE_STRING APOSTR_FIXED_STRING APOSTR_FREE_STRING

BDIGIT=[0-1](\040*[0-1])*
BINARY_LITERAL=[Bb]\'{BDIGIT}\'|[Bb]\"{BDIGIT}\"|\'{BDIGIT}\'[Bb]|\"{BDIGIT}\"[Bb]

ODIGIT=[0-7](\040*[0-7])*
OCTAL_LITERAL=[Oo]\'{ODIGIT}\'|[Oo]\"{ODIGIT}\"|\'{ODIGIT}\'[Oo]|\"{ODIGIT}\"[Oo]

HDIGIT=[0-9A-Fa-f](\040*[0-9A-Fa-f])*
HEX_LITERAL=[XZxz]\'{HDIGIT}\'|[XZxz]\"{HDIGIT}\"|\'{HDIGIT}\'"Z"|\"{HDIGIT}\""Z"

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

FREE_LINE_CONTINUE=((("&"){WHITE_SPACE_CHAR}*{EOL}({WHITE_SPACE_CHAR}?{LINE_COMMENT}{EOL})*({WHITE_SPACE_CHAR}*"&")?))+
FIXED_LINE_CONTINUE=({WHITE_SPACE_CHAR}*{EOL}(({WHITE_SPACE_CHAR}*{LINE_COMMENT}{EOL})|([cC*][^\r\n]*{EOL}))*((\040{5}[^0\040\r\n]|\040{0,4}\t[1-9])))+

STRING_LITERAL=({KIND_PARAM}_)?(\"([^\"\n]|(\"\"))*\")| ({KIND_PARAM}_)?('([^\'\n]|(\'\'))*\')

STR_AMP=(\&{WHITE_SPACE_CHAR}*)+[^\ \t\f\n\r\&]
QUOTE_FIXED_STRING_PART=[^\"\n]|(\"\")
AP_FIXED_STRING_PART=[^\'\n]|(\'\')
QUOTE_FREE_STRING_PART=[^\"\n\&]|(\"\")|{STR_AMP}
AP_FREE_STRING_PART=[^\'\n\&]|(\'\')|{STR_AMP}

FREE_FORMAT="format"{WHITE_SPACE_CHAR}*{FREE_LINE_CONTINUE}?{WHITE_SPACE_CHAR}*"("([^\r\n]|{FREE_LINE_CONTINUE})*")"
FIXED_FORMAT="format"{WHITE_SPACE_CHAR}*{FIXED_LINE_CONTINUE}?{WHITE_SPACE_CHAR}*"("([^\r\n]|{FIXED_LINE_CONTINUE})*")"

%%

<YYINITIAL> {
. { yypushback(1);
    if (fFixedForm_)
        pushState(FIXEDFORM);
    else
        pushState(FREEFORM);
  }
}


<QUOTE_FREE_STRING> {
    {QUOTE_FREE_STRING_PART}+ { return(STRINGMIDDLE); }
    {QUOTE_FREE_STRING_PART}+({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); return(STRINGMIDDLE); }
    {QUOTE_FREE_STRING_PART}*\" { popState(); return(STRINGEND); }
    {FREE_LINE_CONTINUE} { return LINE_CONTINUE; }
}

<QUOTE_FIXED_STRING> {
    {QUOTE_FIXED_STRING_PART}+ { return(STRINGMIDDLE); }
    {QUOTE_FIXED_STRING_PART}*\" { popState(); return(STRINGEND); }
    {FIXED_LINE_CONTINUE} { return LINE_CONTINUE; }
}

<APOSTR_FREE_STRING> {
    {AP_FREE_STRING_PART}+ { return(STRINGMIDDLE); }
    {AP_FREE_STRING_PART}+({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); return(STRINGMIDDLE); }
    {AP_FREE_STRING_PART}*\' { popState(); return(STRINGEND); }
    {FREE_LINE_CONTINUE} { return LINE_CONTINUE; }
}

<APOSTR_FIXED_STRING> {
    {AP_FIXED_STRING_PART}+ { return(STRINGMIDDLE); }
    {AP_FIXED_STRING_PART}*\' { popState(); return(STRINGEND); }
    {FIXED_LINE_CONTINUE} { return LINE_CONTINUE; }
}

<QUOTE_FREE_STRING, QUOTE_FIXED_STRING, APOSTR_FREE_STRING, APOSTR_FIXED_STRING> {
    {EOL} { popState(); return(EOL); }
}

<FREEFORM> {
     {FREE_LINE_CONTINUE} { return LINE_CONTINUE; }
     ({KIND_PARAM}_)?\"{QUOTE_FREE_STRING_PART}* { pushState(QUOTE_FREE_STRING); return(STRINGSTART); }
     ({KIND_PARAM}_)?\'{AP_FREE_STRING_PART}* { pushState(APOSTR_FREE_STRING); return(STRINGSTART); }
     ({KIND_PARAM}_)?\"{QUOTE_FREE_STRING_PART}*({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); pushState(QUOTE_FREE_STRING); return(STRINGSTART); }
     ({KIND_PARAM}_)?\'{AP_FREE_STRING_PART}*({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); pushState(APOSTR_FREE_STRING); return(STRINGSTART); }
     {FREE_FORMAT} { return FORMATSTMT; }
}

<FIXEDFORM> {
    ^[cC*][^\r\n]* {  return LINE_COMMENT; }
    {FIXED_LINE_CONTINUE} { return LINE_CONTINUE; }
    ({KIND_PARAM}_)?\"{QUOTE_FIXED_STRING_PART}* { pushState(QUOTE_FIXED_STRING); return(STRINGSTART); }
    ({KIND_PARAM}_)?\'{AP_FIXED_STRING_PART}* { pushState(APOSTR_FIXED_STRING); return(STRINGSTART); }
    {FIXED_FORMAT} { return FORMATSTMT; }
    ^{WHITE_SPACE_CHAR}*{LINE_COMMENT} { return LINE_COMMENT; }
    ^[^0-9cC*!\040\t\n\r].{5} { return BAD_CHARACTER; }
    ^[0-9\040][^0-9!\040\t\n\r].{4} { return BAD_CHARACTER; }
    ^[0-9\040]{2}[^0-9!\040\t\n\r].{3} { return BAD_CHARACTER; }
    ^[0-9\040]{3}[^0-9!\040\t\n\r].{2} { return BAD_CHARACTER; }
    ^[0-9\040]{4}[^0-9!\040\t\n\r].{1} { return BAD_CHARACTER; }
}

<FREEFORM,FIXEDFORM> {
    ({WHITE_SPACE_CHAR})+ { return WHITE_SPACE; }
    (({WHITE_SPACE_CHAR})*({EOL}|(";")))+ { return EOL; }
    {LINE_COMMENT} { return LINE_COMMENT; }
    {STRING_LITERAL} { return STRINGLITERAL; }
    {INTEGER_LITERAL} { return INTEGERLITERAL; }
    {BINARY_LITERAL} { return BINARYLITERAL; }
    {OCTAL_LITERAL} { return OCTALLITERAL; }
    {HEX_LITERAL} { return HEXLITERAL; }
    {FLOATING_POINT_LITERAL} { return FLOATINGPOINTLITERAL; }
    {DIGIT}\040*\./[^A-Za-z0-9_] { return FLOATINGPOINTLITERAL; }
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
    "accept" { return ACCEPT; }
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
    "decode" { return DECODE; }
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
    "encode" { return ENCODE; }
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
    "endblockdata" { return ENDBLOCKDATA; }
    "endinterface" { return ENDINTERFACE; }

    {DEFOPERATOR} { return DEFOPERATOR; }
    {IDENTIFIER} { return IDENTIFIER; }
}

. { return BAD_CHARACTER; }

