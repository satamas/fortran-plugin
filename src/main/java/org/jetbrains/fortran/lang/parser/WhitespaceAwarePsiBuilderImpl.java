/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.fortran.lang.lexer.FortranTokens;

public class WhitespaceAwarePsiBuilderImpl extends PsiBuilderAdapter implements WhitespaceAwarePsiBuilder {
    private final Stack<Boolean> newlinesEnabled = new Stack<Boolean>();

    private final PsiBuilderImpl delegateImpl;

    public WhitespaceAwarePsiBuilderImpl(PsiBuilder delegate) {
        super(delegate);
        newlinesEnabled.push(true);
        delegateImpl = findPsiBuilderImpl(delegate);
    }

    @Nullable
    private static PsiBuilderImpl findPsiBuilderImpl(PsiBuilder builder) {
        // This is a hackish workaround for PsiBuilder interface not exposing isWhitespaceOrComment() method
        // We have to unwrap all the adapters to find an Impl inside
        while (true) {
            if (builder instanceof PsiBuilderImpl) {
                return (PsiBuilderImpl) builder;
            }
            if (!(builder instanceof PsiBuilderAdapter)) {
                return null;
            }

            builder = ((PsiBuilderAdapter) builder).getDelegate();
        }
    }

    @Override
    public boolean isWhitespaceOrComment(@NotNull IElementType elementType) {
        assert delegateImpl != null : "PsiBuilderImpl not found";
        return delegateImpl.whitespaceOrComment(elementType);
    }

    @Override
    public boolean newlineBeforeCurrentToken() {
        if (!newlinesEnabled.peek()) return false;

        if (eof()) return true;

        // TODO: maybe, memoize this somehow?
        for (int i = 1; i <= getCurrentOffset(); i++) {
            IElementType previousToken = rawLookup(-i);

            if (previousToken == FortranTokens.LINE_COMMENT) {
                continue;
            }

            if (previousToken != TokenType.WHITE_SPACE) {
                break;
            }

            int previousTokenStart = rawTokenTypeStart(-i);
            int previousTokenEnd = rawTokenTypeStart(-i + 1);

            assert previousTokenStart >= 0;
            assert previousTokenEnd < getOriginalText().length();

            for (int j = previousTokenStart; j < previousTokenEnd; j++) {
                if (getOriginalText().charAt(j) == '\n') {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void disableNewlines() {
        newlinesEnabled.push(false);
    }

    @Override
    public void enableNewlines() {
        newlinesEnabled.push(true);
    }

    @Override
    public void restoreNewlinesState() {
        assert newlinesEnabled.size() > 1;
        newlinesEnabled.pop();
    }
}
