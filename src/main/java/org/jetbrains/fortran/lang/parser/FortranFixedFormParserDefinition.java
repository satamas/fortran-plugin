package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.lang.FortranTypes;
import org.jetbrains.fortran.lang.core.stubs.FortranFixedFormFileStub;
import org.jetbrains.fortran.lang.lexer.FortranLexer;
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile;

import static org.jetbrains.fortran.lang.psi.FortranTokenType.*;

public class FortranFixedFormParserDefinition implements ParserDefinition {
    @NotNull
    @Override
    public Lexer createLexer(Project project) { return new FortranLexer(true); }

    @Override
    public PsiParser createParser(Project project) {
        return new FortranParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FortranFixedFormFileStub.Type.INSTANCE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRINGS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode astNode) {
        PsiElement element = FortranManualPsiElementFactory.createElement(astNode);
        return element == null ? FortranTypes.Factory.createElement(astNode) : element;
    }

    @Override
    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new FortranFixedFormFile(fileViewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
        return SpaceRequirements.MAY;
    }
}