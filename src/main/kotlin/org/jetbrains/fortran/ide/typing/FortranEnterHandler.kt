package org.jetbrains.fortran.ide.typing

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranExecutableConstruct
import org.jetbrains.fortran.lang.psi.FortranNonlabelDoConstruct
import com.intellij.openapi.util.Ref

class FortranEnterHandler : EnterHandlerDelegateAdapter() {

    override fun preprocessEnter(
            file: PsiFile,
            editor: Editor,
            caretOffsetRef: Ref<Int>,
            caretAdvance: Ref<Int>,
            dataContext: DataContext,
            originalHandler: EditorActionHandler?
    ): EnterHandlerDelegate.Result? {
        val offset = editor.caretModel.offset
        val lineStartOffset = editor.caretModel.visualLineStart
        val indentString = CodeStyleManager.getInstance(file.getProject())!!.getLineIndent(editor.document, lineStartOffset)
        val previousElement = file.findElementAt(offset - 1)
        val construct = PsiTreeUtil.getParentOfType(previousElement, FortranExecutableConstruct::class.java)

        if (construct is FortranNonlabelDoConstruct && construct.endDoStmt == null) {
            editor.document.insertString(offset, "\n${indentString}end do")
            return EnterHandlerDelegate.Result.DefaultForceIndent
        }

        return super.postProcessEnter(file, editor, dataContext)
    }

}