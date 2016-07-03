package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.fortran.lang.lexer.FortranKeywordToken;
import org.jetbrains.fortran.lang.lexer.FortranToken;
import org.jetbrains.fortran.lang.lexer.FortranTokens;

import java.util.HashMap;
import java.util.Map;

import static org.jetbrains.fortran.lang.lexer.FortranTokens.IDENTIFIER;
import static org.jetbrains.fortran.lang.lexer.FortranTokens.WHITE_SPACES;

public class AbstractFortranParsing {
    protected final WhitespaceAwarePsiBuilder builder;
    private static final Map<String, FortranKeywordToken> SOFT_KEYWORD_TEXTS = new HashMap<>();

    public AbstractFortranParsing(WhitespaceAwarePsiBuilder builder) {
        this.builder = builder;
    }

    static {
        for (IElementType type : FortranTokens.SOFT_KEYWORDS.getTypes()) {
            FortranKeywordToken keywordToken = (FortranKeywordToken) type;
            assert keywordToken.isSoft();
            SOFT_KEYWORD_TEXTS.put(keywordToken.getValue(), keywordToken);
        }
    }

    protected boolean eof() {
        return builder.eof();
    }

    protected PsiBuilder.Marker mark() {
        return builder.mark();
    }

    protected boolean _at(IElementType expectation) {
        IElementType token = tt();
        return tokenMatches(token, expectation);
    }

    protected boolean at(IElementType expectation) {
        if (_at(expectation)) return true;
        IElementType token = tt();
        if (token == IDENTIFIER && expectation instanceof FortranKeywordToken) {
            FortranKeywordToken expectedKeyword = (FortranKeywordToken) expectation;
            if (expectedKeyword.isSoft() && expectedKeyword.getValue().equals(builder.getTokenText())) {
                builder.remapCurrentToken(expectation);
                return true;
            }
        }
        if (expectation == IDENTIFIER && token instanceof FortranKeywordToken) {
            FortranKeywordToken keywordToken = (FortranKeywordToken) token;
            if (keywordToken.isSoft()) {
                builder.remapCurrentToken(IDENTIFIER);
                return true;
            }
        }
        return false;
    }


    protected boolean atSet(IElementType... tokens) {
        return atSet(TokenSet.create(tokens));
    }

    protected boolean _atSet(TokenSet set) {
        IElementType token = tt();
        if (set.contains(token)) return true;
        return false;
    }

    protected boolean atSet(TokenSet set) {
        if (_atSet(set)) return true;
        IElementType token = tt();
        if (token == IDENTIFIER) {
            FortranKeywordToken keywordToken = SOFT_KEYWORD_TEXTS.get(builder.getTokenText());
            if (keywordToken != null && set.contains(keywordToken)) {
                builder.remapCurrentToken(keywordToken);
                return true;
            }
        } else {
            // We know at this point that <code>set</code> does not contain <code>token</code>
            if (set.contains(IDENTIFIER) && token instanceof FortranKeywordToken) {
                if (((FortranKeywordToken) token).isSoft()) {
                    builder.remapCurrentToken(IDENTIFIER);
                    return true;
                }
            }
        }
        return false;
    }

    protected void error(String message) {
        builder.error(message);
    }

    protected boolean errorAndAdvance(String message) {
        return errorAndAdvance(message, 1);
    }

    protected boolean errorAndAdvance(String message, int advanceTokenCount) {
        PsiBuilder.Marker err = mark();
        advance(advanceTokenCount);
        err.error(message);
        return false;
    }

    protected void advance() {
        builder.advanceLexer();
    }


    protected IElementType tt() {
        IElementType token;
        while (true) {
            token = builder.getTokenType();
            if (WHITE_SPACES.contains(token))
                builder.advanceLexer();
            else
                break;
        }
        return token;
    }


    protected void advance(int advanceTokenCount) {
        for (int i = 0; i < advanceTokenCount; i++) {
            advance(); // erroneous token
        }
    }

    private boolean tokenMatches(IElementType token, IElementType expectation) {
        return token == expectation;
    }

    protected boolean expect(FortranToken expectation, String message) {
        if (at(expectation)) {
            advance(); // expectation
            return true;
        }

        error(message);
        return false;
    }
}
