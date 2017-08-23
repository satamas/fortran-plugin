package org.jetbrains.fortran.lang.resolve

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import junit.framework.Assert
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.impl.FortranComponentDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranConstructNameDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl


class FortranResolveTest : LightCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/resources/resolve"

    // Labels
    fun testFindLabelUsages() {
        val usageInfos = myFixture.testFindUsages("Label.f")
        Assert.assertEquals(3, usageInfos.size)
    }

    fun testLabelReference() {
        myFixture.configureByFiles("Label.f")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals(10, (element.references[0].resolve() as FortranLabelDeclImpl).gelLabelValue())
    }

    // Construct Names
    fun testConstructNameUsages() {
        val usageInfos = myFixture.testFindUsages("ConstructName.f95")
        Assert.assertEquals(2, usageInfos.size)
    }

    fun testConstructNameReference() {
        myFixture.configureByFiles("ConstructName.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("ifconstruct", (element.references[0].resolve() as FortranConstructNameDeclImpl).gelLabelValue())
    }

    // Name references (variables and fields)
    fun testUseOnlyAvailable() {
        myFixture.configureByFiles("UseOnlyAvailable.f95", "Module.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("x", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    fun testUseOnlyNotAvailable() {
        myFixture.configureByFiles("UseOnlyNotAvailable.f95", "Module.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        Assert.assertNull(element.reference?.resolve())
    }

    fun testComponents() {
        myFixture.configureByFiles("Components.f95", "Module.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("x", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    fun testNoExcessiveUsages() {
        val usageInfos = myFixture.testFindUsages("Components.f95", "Module.f95")
        Assert.assertEquals(3, usageInfos.size)
    }

    fun testRenamedType() {
        myFixture.configureByFiles("RenamedType.f95", "Module.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("x", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    fun testDeepUsage() {
        myFixture.configureByFiles("DeepUsage.f95", "CloseModule.f95", "AwayModule.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("data", (element.reference?.resolve() as FortranEntityDecl).name)
    }
}