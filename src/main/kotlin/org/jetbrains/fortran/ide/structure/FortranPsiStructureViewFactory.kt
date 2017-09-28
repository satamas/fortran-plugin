package org.jetbrains.fortran.ide.structure

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import org.jetbrains.fortran.lang.psi.FortranFile
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile

class FortranPsiStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        if (psiFile !is FortranFile && psiFile !is FortranFixedFormFile) return null
        return object : TreeBasedStructureViewBuilder() {

            override fun createStructureViewModel(editor: Editor?): StructureViewModel
                    = FortranStructureViewModel(editor, psiFile)

        }
    }
}