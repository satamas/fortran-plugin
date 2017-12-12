package org.jetbrains.fortran.lang.parsing

import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.testFramework.ParsingTestCase
import com.intellij.util.PathUtil
import org.jetbrains.fortran.FortranFixedFormLanguage
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.parser.FortranFixedFormParserDefinition
import org.jetbrains.fortran.lang.parser.FortranParserDefinition
import org.jetbrains.fortran.test.FortranTestDataFixture

abstract class FortranBaseParsingTestCase : ParsingTestCase(".", "f", FortranParserDefinition(), FortranFixedFormParserDefinition()) {
    override fun getTestDataPath() = "src/test/resources/psi"

    override fun includeRanges() = true

    @Throws(Exception::class)
    fun doParsingTest(filePath: String) {
        if (filePath.endsWith(".f") || filePath.endsWith(".for")) {
            doBaseTest(filePath, IFileElementType(FortranFixedFormLanguage))
        } else {
            doBaseTest(filePath, IFileElementType(FortranLanguage))
        }
    }

    @Throws(Exception::class)
    private fun doBaseTest(filePath: String, fileType: IElementType) {
        myLanguage = fileType.language
        myFileExt = FileUtilRt.getExtension(PathUtil.getFileName(filePath))
        myFile = createPsiFile(FileUtil.getNameWithoutExtension(PathUtil.getFileName(filePath)), loadFile(filePath))
        doCheckResult(myFullDataPath, filePath.replace("\\.(for|f90|f95|f03|f08|f)".toRegex(), ".txt"), toParseTreeText(myFile, false, false).trim())
    }
}
