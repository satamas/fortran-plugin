package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranStmt
import org.jetbrains.fortran.lang.psi.impl.FortranConstructNameDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranUnitDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranWriteStmtImpl


class FortranResolveTest : LightCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "src/test/resources/resolve"

    // Labels
    fun testFindLabelUsages() {
        val usageInfos = myFixture.testFindUsages("Label.f")
        assertEquals(3, usageInfos.size)
    }

    fun testLabelReference() {
        myFixture.configureByFiles("Label.f")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals(10, (element.references[0].resolve() as FortranLabelDeclImpl).getLabelValue())
    }

    fun testFindLabelUsagesInSpecification() {
        val usageInfos = myFixture.testFindUsages("LabelsInSpecifications.f95")
        assertEquals(1, usageInfos.size)
    }

    fun testLabelReferenceInSpecification() {
        myFixture.configureByFiles("LabelsInSpecifications.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals(1, (element.references[0].resolve() as FortranLabelDeclImpl).getLabelValue())
    }

    // Construct Names
    fun testConstructNameUsages() {
        val usageInfos = myFixture.testFindUsages("ConstructName.f95")
        assertEquals(2, usageInfos.size)
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
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("x", declaration.name)
        assertEquals("Module.f95", declaration.containingFile.name)
    }

    fun testUseOnlyNotAvailable() {
        myFixture.configureByFiles("UseOnlyNotAvailable.f95", "Module.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertNull(element.reference?.resolve())
    }

    fun testComponents() {
        myFixture.configureByFiles("Components.f95", "Module.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("x", declaration.name)
        assertEquals("Module.f95", declaration.containingFile.name)
    }

    fun testNoExcessiveUsages() {
        val usageInfos = myFixture.testFindUsages("Components.f95", "Module.f95")
        assertEquals(3, usageInfos.size)
    }

    fun testRenamedType() {
        myFixture.configureByFiles("RenamedType.f95", "Module.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("x", declaration.name)
        assertEquals("Module.f95", declaration.containingFile.name)
    }

    fun testRenamedIsNotAvailable() {
        myFixture.configureByFiles("RenamedIsNotAvailable.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranDataPath
        assertEquals("pi", declaration.name)
        assertEquals(FortranWriteStmtImpl::class.java,
                PsiTreeUtil.getParentOfType(declaration, FortranStmt::class.java)!!::class.java)
    }

    fun testRenamedIsNotAvailable2() {
        myFixture.configureByFiles("RenamedIsNotAvailable2.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranDataPath
        assertEquals("pi", declaration.name)
        assertEquals(FortranWriteStmtImpl::class.java,
                PsiTreeUtil.getParentOfType(declaration, FortranStmt::class.java)!!::class.java)
    }

    fun testDeepUsage() {
        myFixture.configureByFiles("DeepUsage.f95", "CloseModule.f95", "AwayModule.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("data", declaration.name)
        assertEquals("FarAwayModule.f95", declaration.containingFile.name)
    }

    fun testDeepPiUsage() {
        myFixture.configureByFiles("DeepPiUsage.f95", "CloseModule.f95", "AwayModule.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("renamed_pi", declaration.name)
        assertEquals("AwayModule.f95", declaration.containingFile.name)
    }

    fun testDeepEUsage() {
        myFixture.configureByFiles("DeepEUsage.f95", "CloseModule.f95", "AwayModule.f95", "FarAwayModule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("E", declaration.name)
        assertEquals("FarAwayModule.f95", declaration.containingFile.name)
    }

    fun testFunction() {
        val usageInfos = myFixture.testFindUsages("Function.f95")
        assertEquals(2, usageInfos.size)
    }

    fun testSpecificationStmts() {
        val usageInfos = myFixture.testFindUsages("SpecificationStmts.f95")
        assertEquals(11, usageInfos.size)
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
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("needle", declaration.name)
        assertEquals("Submodule.f95", declaration.containingFile.name)
    }

    fun testSubmoduleType() {
        myFixture.configureByFiles("ProgramWithSubmoduleType.f95",
                "ModuleWithSubmodule.f95", "SubmoduleWithSubmodule.f95", "Submodule.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranEntityDecl
        assertEquals("subtype", declaration.name)
        assertEquals("Submodule.f95", declaration.containingFile.name)
    }

    fun testMethod() {
        myFixture.configureByFiles("Method.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("print", (element.reference?.resolve() as FortranEntityDecl).name)
    }

    // Common block
    fun testCommonBlocks() {
        // Here we find all N usages of the common block. In IDE only N-1 usage will be shown
        // The block where we use find usages is not shown in IDE
        val usageInfos = myFixture.testFindUsages("CommonBlocksA.f95", "CommonBlocksB.f95")
        assertEquals(3, usageInfos.size)
    }

    // Implicit
    fun testImplicitUsages() {
        val usageInfos = myFixture.testFindUsages("Implicit.f95", "CommonBlocksB.f95", "TwinOne.f95")
        assertEquals(14, usageInfos.size)
    }

    fun testImplicit() {
        myFixture.configureByFiles("Implicit.f95", "CommonBlocksB.f95", "TwinTwo.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        val declaration = element.reference?.resolve() as FortranDataPath
        assertEquals("X", declaration.name)
        assertEquals("Implicit.f95", declaration.containingFile.name)
    }

    fun testImplicit2Usages() {
        val usageInfos = myFixture.testFindUsages("Implicit2.f95")
        assertEquals(3, usageInfos.size)
    }

    fun testNoImplicitStructures() {
        val usageInfos = myFixture.testFindUsages("Implicit3.f95")
        assertEquals(1, usageInfos.size)
    }

    // Interface
    fun testInterface() {
        myFixture.configureByFiles("Interface.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("F", ((element.reference as FortranPathReferenceImpl?)?.multiResolve()?.firstOrNull() as FortranEntityDecl).name)
    }

    fun testInterfaceUsages() {
        val usageInfos = myFixture.testFindUsages("Interface2.f95")
        assertEquals(6, usageInfos.size)
    }

    fun testNamedInterfaceUsages() {
        val usageInfos = myFixture.testFindUsages("NamedInterface.f95", "ProgramWithNamedInterface.f95")
        assertEquals(3, usageInfos.size)
    }

    fun testNamedInterfaceUsages2() {
        val usageInfos = myFixture.testFindUsages("ProgramWithNamedInterface2.f95", "NamedInterface2.f95")
        assertEquals(0, usageInfos.size)
    }

    // enum
    fun testEnum() {
        myFixture.configureByFiles("Enum.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals("RED", ((element.reference as FortranPathReferenceImpl?)?.multiResolve()?.firstOrNull() as FortranEntityDecl).name)
    }

    // file-unit-number
    fun testFileUnitNumber() {
        myFixture.configureByFiles("testFileUnitNumber.f95")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)!!.parent
        assertEquals(1, ((element.reference as FortranUnitReferenceImpl?)?.multiResolve()?.firstOrNull() as FortranUnitDeclImpl).getUnitValue())
    }

    fun testFindFileUnitNumberUsages() {
        val usageInfos = myFixture.testFindUsages("testFileUnitNumberUsages.f95")
        assertEquals(2, usageInfos.size)
    }
}