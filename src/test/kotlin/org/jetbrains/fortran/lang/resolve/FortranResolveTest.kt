package org.jetbrains.fortran.lang.resolve

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import junit.framework.Assert
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
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
        assertEquals("ifconstruct", (element.references[0].resolve() as FortranConstructNameDeclImpl).getLabelValue())
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

    fun testDeepPiUsage() {
        myFixture.configureByFiles("DeepPiUsage.f95", "CloseModule.f95", "AwayModule.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("renamed_pi", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    fun testDeepEUsage() {
        myFixture.configureByFiles("DeepEUsage.f95", "CloseModule.f95", "AwayModule.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("E", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    fun testFunction() {
        val usageInfos = myFixture.testFindUsages("Function.f95")
        Assert.assertEquals(2, usageInfos.size)
    }

    fun testSpecificationStmts() {
        val usageInfos = myFixture.testFindUsages("SpecificationStmts.f95")
        Assert.assertEquals(11, usageInfos.size)
    }

    fun testTypeSearch() {
        myFixture.configureByFiles("typeSearch.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("x", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    fun testNoLoops() {
        myFixture.configureByFiles("TwinOne.f95", "TwinTwo.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertNull(element.reference?.resolve())
    }

    fun testSubmodule() {
        myFixture.configureByFiles("ProgramWithSubmodule.f95",
                "ModuleWithSubmodule.f95", "SubmoduleWithSubmodule.f95", "Submodule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("needle", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    fun testSubmoduleType() {
        myFixture.configureByFiles("ProgramWithSubmoduleType.f95",
                "ModuleWithSubmodule.f95", "SubmoduleWithSubmodule.f95", "Submodule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("subtype", (element.reference?.resolve() as FortranEntityDecl).name)
    }
}