package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.fortran.lang.lexer.FortranToken;

import static org.jetbrains.fortran.lang.lexer.FortranTokens.WHITE_SPACES;

public class AbstractFortranParsing {
    protected final PsiBuilder builder;

    public AbstractFortranParsing(PsiBuilder builder) {
        this.builder = builder;
    }

    protected boolean eof() {
        return builder.eof();
    }

    protected PsiBuilder.Marker mark() {
        return builder.mark();
    }

    protected boolean at(IElementType expectation) {
        IElementType token = tt();
        return tokenMatches(token, expectation);
    }


    protected boolean atSet(IElementType... tokens) {
        return atSet(TokenSet.create(tokens));
    }

    protected boolean atSet(TokenSet set) {
        IElementType token = tt();
        if (set.contains(token)) return true;
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
        while(true) {
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
