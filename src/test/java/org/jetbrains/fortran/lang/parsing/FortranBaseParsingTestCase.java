package org.jetbrains.fortran.lang.parsing;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.FortranLanguage;
import org.jetbrains.fortran.lang.parser.FortranParserDefinition;
import org.jetbrains.fortran.test.FortranTestDataFixture;

public abstract class FortranBaseParsingTestCase extends ParsingTestCase {
    @Override
    protected String getTestDataPath() {
        return FortranTestDataFixture.getLangTestData() + "/psi";
    }

    protected FortranBaseParsingTestCase() {
        super(".", "f", new FortranParserDefinition());
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    protected void doParsingTest(@NotNull String filePath) throws Exception {
        doBaseTest(filePath, new IFileElementType(FortranLanguage.INSTANCE));
    }

    private void doBaseTest(@NotNull String filePath, @NotNull IElementType fileType) throws Exception {
        myFileExt = FileUtilRt.getExtension(PathUtil.getFileName(filePath));
        myFile = createPsiFile(FileUtil.getNameWithoutExtension(PathUtil.getFileName(filePath)), loadFile(filePath));
        doCheckResult(myFullDataPath, filePath.replaceAll("\\.(for|f90|f95|f03|f08|f)", ".txt"), toParseTreeText(myFile, false, false).trim());
    }
}
