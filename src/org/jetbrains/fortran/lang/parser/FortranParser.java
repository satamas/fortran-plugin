package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class FortranParser implements PsiParser{

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType iElementType, @NotNull PsiBuilder psiBuilder) {
        new FortranParsing(psiBuilder).parseFile(iElementType);
        return psiBuilder.getTreeBuilt();
    }
}
