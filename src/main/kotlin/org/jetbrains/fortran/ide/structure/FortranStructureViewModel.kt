package org.jetbrains.fortran.ide.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.pom.Navigatable
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.fortran.FortranIcons
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement


class FortranStructureViewModel(editor: Editor?, file: PsiFile)
        : StructureViewModelBase(file, editor, FortranStructureViewElement((file as? FortranFile) ?: file as FortranFixedFormFile))
        , StructureViewModel.ElementInfoProvider {
    init {
        withSuitableClasses(FortranNamedElement::class.java)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement)
            = element.value is FortranFile || element.value is FortranFixedFormFile

    override fun isAlwaysLeaf(element: StructureViewTreeElement) =
            when (element.value) {
                is FortranEntityDecl -> true
                else -> false
            }
}

private class FortranStructureViewElement(
        val psi: FortranCompositeElement
) : StructureViewTreeElement, Navigatable by (psi as NavigatablePsiElement) {

    override fun getValue() = psi

    override fun getPresentation(): ItemPresentation = when (psi) {
        is FortranFile -> PresentationData(psi.name, null, FortranIcons.fileTypeIcon, null)
        is FortranFixedFormFile -> PresentationData(psi.name, null, FortranIcons.fileTypeIcon, null)

        is FortranMainProgram -> PresentationData(psi.programStmt?.text ?: "Program", null, null, null)
        is FortranFunctionSubprogram -> PresentationData(psi.functionStmt.text, null, null, null)
        is FortranSubroutineSubprogram -> PresentationData(psi.subroutineStmt.text, null, null, null)
        is FortranModule -> PresentationData(psi.moduleStmt.text, null, null, null)
        is FortranSubmodule -> PresentationData(psi.submoduleStmt.text, null, null, null)
        is FortranSeparateModuleSubprogram -> PresentationData(psi.mpSubprogramStmt.text, null, null, null)
        is FortranBlockData -> PresentationData(psi.blockDataStmt.text, null, null, null)

        else -> PresentationData(psi.text, null, null, null)
    }

    override fun getChildren(): Array<TreeElement> =
            childElements.sortedBy { it.textOffset }
                    .map(::FortranStructureViewElement).toTypedArray()

    private val childElements: List<FortranCompositeElement>
        get() {
            return when (psi) {
                is FortranFile -> psi.programUnits
                is FortranProgramUnit -> psi.variables + psi.subprograms + psi.types
            /*    is RsEnumItem -> psi.enumBody?.enumVariantList.orEmpty()
                is RsImplItem, is RsTraitItem -> {
                    val members = (if (psi is RsImplItem) psi.members else (psi as RsTraitItem).members)
                            ?: return emptyList()
                    buildList<RsCompositeElement> {
                        addAll(members.functionList)
                        addAll(members.constantList)
                        addAll(members.typeAliasList)
                    }
                }
                is RsMod -> buildList<RsCompositeElement> {
                    addAll(psi.enumItemList)
                    addAll(psi.functionList)
                    addAll(psi.implItemList)
                    addAll(psi.modDeclItemList)
                    addAll(psi.modItemList)
                    addAll(psi.constantList)
                    addAll(psi.structItemList)
                    addAll(psi.traitItemList)
                    addAll(psi.typeAliasList)
                }
                is RsStructItem -> psi.blockFields?.fieldDeclList.orEmpty()
                is RsEnumVariant -> psi.blockFields?.fieldDeclList.orEmpty()*/
                else -> emptyList()
            }
        }
}