package org.jetbrains.fortran.lang.resolve

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import junit.framework.Assert
import org.jetbrains.fortran.lang.psi.impl.FortranConstructNameDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl


class FortranResolveTest : LightCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/resources/resolve"

    fun testFindLabelUsages() {
        val usageInfos = myFixture.testFindUsages("Label.f")
        Assert.assertEquals(3, usageInfos.size)
    }

    fun testLabelReference() {
        myFixture.configureByFiles("Label.f")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals(10, (element.references[0].resolve() as FortranLabelDeclImpl).gelLabelValue())
    }

    fun testConstructNameUsages() {
        val usageInfos = myFixture.testFindUsages("ConstructName.f95")
        Assert.assertEquals(2, usageInfos.size)
    }

    fun testConstructNameReference() {
        myFixture.configureByFiles("ConstructName.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("ifconstruct", (element.references[0].resolve() as FortranConstructNameDeclImpl).gelLabelValue())
    }
}