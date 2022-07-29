package org.jetbrains.fortran.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import java.util.Stack;

import static com.intellij.psi.TokenType.*;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import static org.jetbrains.fortran.lang.psi.FortranTokenType.*;
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

%xstate FREEFORM FIXEDFORM QUOTE_FIXED_STRING QUOTE_FREE_STRING APOSTR_FIXED_STRING APOSTR_FREE_STRING DIRECTIVE
%xstate FIXED_FORMAT_STR FREE_FORMAT_STR FREEFORM_LINE_CONTINUE OPERATOR_DIV_FIXED OPERATOR_DIV_FREE

BDIGIT=[0-1](\040*[0-1])*
BINARY_LITERAL=[Bb]\'{BDIGIT}\'|[Bb]\"{BDIGIT}\"|\'{BDIGIT}\'[Bb]|\"{BDIGIT}\"[Bb]

ODIGIT=[0-7](\040*[0-7])*
OCTAL_LITERAL=[Oo]\'{ODIGIT}\'|[Oo]\"{ODIGIT}\"|\'{ODIGIT}\'[Oo]|\"{ODIGIT}\"[Oo]

HDIGIT=[0-9A-Fa-f](\040*[0-9A-Fa-f])*
HEX_LITERAL=[XZxz]\'{HDIGIT}\'|[XZxz]\"{HDIGIT}\"|\'{HDIGIT}\'[XZxz]|\"{HDIGIT}\"[XZxz]

IDENTIFIER_PART=[:digit:]|[:letter:]|_|"$"
IDENTIFIER=([:letter:]|_){IDENTIFIER_PART}*

LINE_COMMENT="!"[^\r\n]*
WHITE_SPACE_CHAR=[\ \t\f]
EOL=(\n|\r|\r\n)

DEFOPERATOR=\.[:letter:]+\.

DIGIT=[0-9](\040*[0-9])*
SIGNIFICAND={DIGIT}\.(\040*[0-9])+|\.\040*{DIGIT}
FLOATING_POINT_EXPONENT_PART=\040*[eE]\040*(\+|\-)?\040*{DIGIT}
DOUBLE_PRECISION_EXPONENT_PART=\040*[dDqQ]\040*(\+|\-)?\040*{DIGIT}
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

FREE_LINE_CONTINUE=((("&"){WHITE_SPACE_CHAR}*{LINE_COMMENT}?{EOL}({WHITE_SPACE_CHAR}*{LINE_COMMENT}?{EOL})*({WHITE_SPACE_CHAR}*"&")?))+
FIXED_LINE_CONTINUE=({WHITE_SPACE_CHAR}*{EOL}(({WHITE_SPACE_CHAR}*{LINE_COMMENT}?{EOL})|(([cC*][^\r\n]*)?{EOL}))*(([\040dD]\040{4}[^0\040\r\n\t]|\040{0,4}\t[1-9])))+

STRING_LITERAL=({KIND_PARAM}_)?(\"([^\"\n]|(\"\"))*\")| ({KIND_PARAM}_)?('([^\'\n]|(\'\'))*\')

STR_AMP=(\&{WHITE_SPACE_CHAR}*)+[^\ \t\f\n\r\&]
QUOTE_FIXED_STRING_PART=[^\"\n]|(\"\")
AP_FIXED_STRING_PART=[^\'\n]|(\'\')
QUOTE_FREE_STRING_PART=[^\"\n\&]|(\"\")|{STR_AMP}
AP_FREE_STRING_PART=[^\'\n\&]|(\'\')|{STR_AMP}

HASH=#

DIRECTIVE_CONTENT=[^\r\n]*
CPP="#"[^\r\n]*{EOL}
CPPCOMMENT="#"\040*"if"\040*0({EOL}[^\r\n]*)*{EOL}"#"\040*"endif"{EOL}
%%



<YYINITIAL> {
{EOL}|";" { return EOL; }
. { yypushback(1);
    if (fFixedForm_)
        pushState(FIXEDFORM);
    else
        pushState(FREEFORM);
  }
}


<YYINITIAL, FREEFORM_LINE_CONTINUE, FREEFORM, FIXEDFORM>{
    {HASH} {WHITE_SPACE_CHAR}* "define" { pushState(DIRECTIVE); return DEFINE_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* "undef" { pushState(DIRECTIVE); return UNDEFINE_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* "ifdef" { pushState(DIRECTIVE); return IF_DEFINED_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* "ifndef" { pushState(DIRECTIVE); return IF_NOT_DEFINED_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* "if" { pushState(DIRECTIVE); return IF_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* "elif" { pushState(DIRECTIVE); return ELIF_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* "else" { pushState(DIRECTIVE); return ELSE_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* "endif" { pushState(DIRECTIVE); return ENDIF_DIRECTIVE; }
    {HASH} {WHITE_SPACE_CHAR}* {IDENTIFIER} { pushState(DIRECTIVE); return UNKNOWN_DIRECTIVE; }
}


<QUOTE_FREE_STRING> {
    {QUOTE_FREE_STRING_PART}+ { return(STRINGMIDDLE); }
    {QUOTE_FREE_STRING_PART}+({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); return(STRINGMIDDLE); }
    {QUOTE_FREE_STRING_PART}*\" { popState(); return(STRINGEND); }
    {FREE_LINE_CONTINUE} { pushState(FREEFORM_LINE_CONTINUE); yypushback(yylength()); }
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
    {FREE_LINE_CONTINUE} { pushState(FREEFORM_LINE_CONTINUE); yypushback(yylength()); }
}

<APOSTR_FIXED_STRING> {
    {AP_FIXED_STRING_PART}+ { return(STRINGMIDDLE); }
    {AP_FIXED_STRING_PART}*\' { popState(); return(STRINGEND); }
    {FIXED_LINE_CONTINUE} { return LINE_CONTINUE; }
}

<FREE_FORMAT_STR> {
    {FREE_LINE_CONTINUE} { pushState(FREEFORM_LINE_CONTINUE); yypushback(yylength()); }
    ({KIND_PARAM}_)?\"{QUOTE_FREE_STRING_PART}* { pushState(QUOTE_FREE_STRING); return(STRINGSTART); }
    ({KIND_PARAM}_)?\'{AP_FREE_STRING_PART}* { pushState(APOSTR_FREE_STRING); return(STRINGSTART); }
    ({KIND_PARAM}_)?\"{QUOTE_FREE_STRING_PART}*({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); pushState(QUOTE_FREE_STRING); return(STRINGSTART); }
    ({KIND_PARAM}_)?\'{AP_FREE_STRING_PART}*({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); pushState(APOSTR_FREE_STRING); return(STRINGSTART); }
}

<FIXED_FORMAT_STR> {
    ^[cC*][^\r\n]* {  return LINE_COMMENT; }
    {FIXED_LINE_CONTINUE} { return LINE_CONTINUE; }
    ({KIND_PARAM}_)?\"{QUOTE_FIXED_STRING_PART}* { pushState(QUOTE_FIXED_STRING); return(STRINGSTART); }
    ({KIND_PARAM}_)?\'{AP_FIXED_STRING_PART}* { pushState(APOSTR_FIXED_STRING); return(STRINGSTART); }
}

<OPERATOR_DIV_FIXED> {
    ({WHITE_SPACE_CHAR})+ { return WHITE_SPACE; }
    {FIXED_LINE_CONTINUE} { return LINE_CONTINUE; }
    "/" { return DIV; }
    "//" { return DIVDIV; }
    ")" { popState(); return RPAR; }
}

<OPERATOR_DIV_FREE> {
    ({WHITE_SPACE_CHAR})+ { return WHITE_SPACE; }
    {FREE_LINE_CONTINUE} { return LINE_CONTINUE; }
    "/" { return DIV; }
    "//" { return DIVDIV; }
    ")" { popState(); return RPAR; }
}

<FREE_FORMAT_STR, FIXED_FORMAT_STR> {
    ({WHITE_SPACE_CHAR})+ { return WHITE_SPACE; }
    {LINE_COMMENT} { return LINE_COMMENT; }
    {STRING_LITERAL} { return STRINGLITERAL; }
    {INTEGER_LITERAL} { return INTEGERLITERAL; }
    ":" { return COLON; }
    "(" { return LPAR; }
    ")" { return RPAR; }
    "+" { return PLUS; }
    "-" { return MINUS; }
    "*" { return MUL; }
    "/" { return DIV; }
    "," { return COMMA; }
    "$" { return DOLLAR; }
    [:letter:]+([:digit:]+(\.[:digit:]+([:letter:][:digit:]+)?)?)? { return DATAEDIT; }
    {EOL}|(";") { popState(); return EOL; }
    . { popState(); return(BAD_CHARACTER); }
}

<QUOTE_FREE_STRING, QUOTE_FIXED_STRING, APOSTR_FREE_STRING, APOSTR_FIXED_STRING> {
    {EOL} { popState(); return(EOL); }
}

<FREEFORM> {
     {FREE_LINE_CONTINUE} { pushState(FREEFORM_LINE_CONTINUE); yypushback(yylength()); }
     ({KIND_PARAM}_)?\"{QUOTE_FREE_STRING_PART}* { pushState(QUOTE_FREE_STRING); return(STRINGSTART); }
     ({KIND_PARAM}_)?\'{AP_FREE_STRING_PART}* { pushState(APOSTR_FREE_STRING); return(STRINGSTART); }
     ({KIND_PARAM}_)?\"{QUOTE_FREE_STRING_PART}*({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); pushState(QUOTE_FREE_STRING); return(STRINGSTART); }
     ({KIND_PARAM}_)?\'{AP_FREE_STRING_PART}*({WHITE_SPACE_CHAR}*\&)+ { yypushback(1); pushState(APOSTR_FREE_STRING); return(STRINGSTART); }
     "format" { return WORD; }
     "format"{WHITE_SPACE_CHAR}*"(" { yypushback(yylength()-6); pushState(FREE_FORMAT_STR); return WORD; }
      "("({WHITE_SPACE_CHAR}|{FREE_LINE_CONTINUE})*(("/")|("//"))({WHITE_SPACE_CHAR}|{FREE_LINE_CONTINUE})*")" { yypushback(yylength()-1); pushState(OPERATOR_DIV_FREE); return(LPAR); }
}

<FIXEDFORM> {
    ^[cC*][^\r\n]* {  return LINE_COMMENT; }
    ^{WHITE_SPACE_CHAR}*{LINE_COMMENT} { return LINE_COMMENT; }
    {FIXED_LINE_CONTINUE} { return LINE_CONTINUE; }
    ({KIND_PARAM}_)?\"{QUOTE_FIXED_STRING_PART}* { pushState(QUOTE_FIXED_STRING); return(STRINGSTART); }
    ({KIND_PARAM}_)?\'{AP_FIXED_STRING_PART}* { pushState(APOSTR_FIXED_STRING); return(STRINGSTART); }
    "format" { return WORD; }
    "format"{WHITE_SPACE_CHAR}*"(" { yypushback(yylength()-6); pushState(FIXED_FORMAT_STR); return WORD; }
    "("({WHITE_SPACE_CHAR}|{FIXED_LINE_CONTINUE})*(("/")|("//"))({WHITE_SPACE_CHAR}|{FIXED_LINE_CONTINUE})*")" { yypushback(yylength()-1); pushState(OPERATOR_DIV_FIXED); return(LPAR); }

    ^[dD][\0400-9]{4} { yypushback(yylength()-1); return LINE_COMMENT; }
    ^({WHITE_SPACE_CHAR})+ { if (yylength() > 6) yypushback(yylength()-6); return FIRST_WHITE_SPACE; }
    ^[^0-9cCdD#*!\040\t\n\r][^\n\r]* { return BAD_CHARACTER; }
    ^[0-9\040dD][^0-9!\040\t\n\r][^\n\r]* { return BAD_CHARACTER; }
    ^[0-9\040dD]{2}[^0-9!\040\t\n\r][^\n\r]* { return BAD_CHARACTER; }
    ^[0-9\040dD]{3}[^0-9!\040\t\n\r][^\n\r]* { return BAD_CHARACTER; }
    ^[0-9\040dD]{4}[^0-9!\040\t\n\r][^\n\r]* { return BAD_CHARACTER; }
    ^[0-9\040dD]{5}[^0-9!\040\t\n\r][^\n\r]* { return BAD_CHARACTER; }
}

<FREEFORM_LINE_CONTINUE> {
    "&" { return LINE_CONTINUE; }
    ({WHITE_SPACE_CHAR}|{EOL})+ { return WHITE_SPACE; }
    {LINE_COMMENT} { return LINE_COMMENT; }
    . { popState(); yypushback(1); }
}

<DIRECTIVE> {
    {DIRECTIVE_CONTENT} {return DIRECTIVE_CONTENT; }
    {EOL} { popState(); return EOL; }
}

<FREEFORM,FIXEDFORM> {
    ({WHITE_SPACE_CHAR})+ { return WHITE_SPACE; }
    ^({WHITE_SPACE_CHAR})*{EOL} { return WHITE_SPACE; }
    {EOL}|(";") { return EOL; }
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
    "_" { return UNDERSCORE; }
    "&" { return AMPERSAND; }

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

    "include" { return INCLUDE_KEYWORD; }
    "character" { return CHARACTER_KEYWORD; }
    "logical" { return LOGICAL_KEYWORD; }
    "complex" { return COMPLEX_KEYWORD; }
    "integer" { return INTEGER_KEYWORD; }
    "real" { return REAL_KEYWORD; }
    "double" { return DOUBLE_KEYWORD; }
    "precision" { return PRECISION_KEYWORD; }

    {DEFOPERATOR} { return DEFOPERATOR; }
    {IDENTIFIER} { return WORD; }
}


<FREEFORM, FIXEDFORM, FREE_FORMAT_STR, FIXED_FORMAT_STR> {
    . { return BAD_CHARACTER; }
}