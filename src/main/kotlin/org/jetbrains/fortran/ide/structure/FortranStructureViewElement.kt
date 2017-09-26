package org.jetbrains.fortran.ide.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.pom.Navigatable
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.FortranIcons
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

class FortranStructureViewElement(
        val psi: FortranCompositeElement
) : StructureViewTreeElement, Navigatable by (psi as NavigatablePsiElement) {

    override fun getValue() = psi

    override fun getPresentation(): ItemPresentation = when (psi) {
    // files
        is FortranFile -> PresentationData(psi.name, null, FortranIcons.fileTypeIcon, null)
        is FortranFixedFormFile -> PresentationData(psi.name, null, FortranIcons.fileTypeIcon, null)

    // program units
        is FortranMainProgram -> PresentationData(psi.programStmt?.text ?: "Program", null, FortranIcons.mainProgramIcon, null)
        is FortranFunctionSubprogram -> PresentationData(psi.functionStmt.text, null, FortranIcons.functionIcon, null)
        is FortranSubroutineSubprogram -> PresentationData(psi.subroutineStmt.text, null, FortranIcons.subroutineIcon, null)
        is FortranModule -> PresentationData(psi.moduleStmt.text, null, FortranIcons.moduleIcon, null)
        is FortranSubmodule -> PresentationData(psi.submoduleStmt.text, null, FortranIcons.submoduleIcon, null)
        is FortranSeparateModuleSubprogram -> PresentationData(psi.mpSubprogramStmt.text, null, FortranIcons.separateModuleSubprogramIcon, null)
        is FortranBlockData -> PresentationData(psi.blockDataStmt.text, null, FortranIcons.blockDataIcon, null)

    // types
        is FortranDerivedTypeDef -> PresentationData(psi.derivedTypeStmt.typeDecl.text, null, FortranIcons.typeIcon, null)
        is FortranInterfaceBlock -> PresentationData(psi.interfaceStmt.text, null, FortranIcons.interfaceIcon, null)
        else -> PresentationData(psi.text, null, null, null)
    }

    override fun getChildren(): Array<TreeElement> =
            childElements.sortedBy { it.textOffset }
                    .map(::FortranStructureViewElement).toTypedArray()

    private val childElements: List<FortranCompositeElement>
        get() {
            return when (psi) {
                is FortranFile -> psi.programUnits
                is FortranProgramUnit -> {
                    val subprograms = psi.subprograms.filter { it.parent is FortranBeginUnitStmt }.map { it.parent.parent as FortranCompositeElement }
                    val types = psi.types.map { it.parent.parent as FortranCompositeElement }
                    val interfaces = PsiTreeUtil.getChildrenOfType((psi as FortranProgramUnitImpl).getBlock(), FortranInterfaceBlock::class.java)?.toList() ?: emptyList()
                    subprograms + types + interfaces
                }
                is FortranInterfaceBlock -> psi.interfaceSpecification?.programUnitList?.filterNotNull() ?: emptyList()

                else -> emptyList()
            }
        }
}