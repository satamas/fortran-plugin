package org.jetbrains.fortran.ide.commenter

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FortranCommenterTest : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/commenter/"

    private fun doTest(fileName: String, fileAfterName: String) {
        myFixture.configureByFile(fileName)
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE)
        myFixture.checkResultByFile(fileAfterName)
    }

    fun testFreeFormComment() = doTest("FreeFormComment.f95", "FreeFormComment_after.f95")
    fun testFreeFormStarComment() = doTest("FreeFormStarComment.f95", "FreeFormStarComment_after.f95")
    fun testFreeFormUncomment() = doTest("FreeFormUncomment.f95", "FreeFormUncomment_after.f95")

    fun testCComment() = doTest("CComment.f", "CComment_after.f")
    fun testCNotComment() = doTest("CNotComment.f", "CNotComment_after.f")
    fun testMarkComment() = doTest("MarkComment.f", "MarkComment_after.f")
    fun testStarComment() = doTest("StarComment.f", "CComment_after.f")
}