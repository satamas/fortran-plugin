package org.jetbrains.fortran.ide.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.pom.Navigatable
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.fortran.FortranIcons
import org.jetbrains.fortran.lang.psi.*

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

    // variables
        is FortranEntityDecl -> {
            when {
                psi.parent is FortranTypeDeclarationStmt -> {
                    val typeName = ((psi.parent as FortranTypeDeclarationStmt).intrinsicTypeSpec
                            ?: (psi.parent as FortranTypeDeclarationStmt).derivedTypeSpec)?.text
                    PresentationData(psi.name + ": " + typeName,
                            null, FortranIcons.variableIcon, null
                    )
                }
                psi.parent.parent is FortranDataComponentDefStmt -> {
                    val typeName = ((psi.parent.parent as FortranDataComponentDefStmt).intrinsicTypeSpec
                            ?: (psi.parent.parent as FortranDataComponentDefStmt).derivedTypeSpec)?.text
                    PresentationData(psi.name + ": " + typeName,
                            null, FortranIcons.variableIcon, null
                    )
                }
                else -> PresentationData(psi.name, null, FortranIcons.methodIcon, null)
            }
        }
        else -> PresentationData(psi.text, null, null, null)
    }

    override fun getChildren(): Array<TreeElement> =
            childElements.sortedBy { it.textOffset }
                    .map(::FortranStructureViewElement).toTypedArray()

    private val childElements: List<FortranCompositeElement>
        get() {
            return when (psi) {
                is FortranFile -> psi.programUnits
                is FortranProgramUnit -> psi.variables +
                        psi.subprograms.filter{ it.parent is FortranNameStmt }.map{it.parent.parent as FortranCompositeElement} +
                        psi.types.map{it.parent.parent as FortranCompositeElement}
                is FortranDerivedTypeDef -> psi.variables.filter{ it.parent !is FortranDerivedTypeStmt }
                else -> emptyList()
            }
        }
}