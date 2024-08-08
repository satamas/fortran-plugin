package org.jetbrains.fortran.ide.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import org.jetbrains.fortran.lang.psi.FortranDerivedTypeDef
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranFile
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile
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
                is FortranDerivedTypeDef -> true
                else -> false
            }
}

