package org.jetbrains.fortran.ide.typing

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.openapi.util.Ref
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.*

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
        val indentString = CodeStyleManager.getInstance(file.project)!!.getLineIndent(editor.document, lineStartOffset)
        val previousElement = file.findElementAt(offset - 1)
        val constructOrUnit = PsiTreeUtil.getParentOfType(previousElement,
                FortranExecutableConstruct::class.java,
                FortranProgramUnit::class.java,
                FortranDeclarationConstruct::class.java
        )

        when (constructOrUnit) {
            // program units
            is FortranProgramUnit -> if (constructOrUnit.beginUnitStmt != null && constructOrUnit.endUnitStmt == null) {
                val programUnitName = constructOrUnit.beginUnitStmt!!.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end ${constructOrUnit.unitType} $programUnitName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            // declaration constructs
            is FortranEnumDef -> if (constructOrUnit.endEnumStmt == null) {
                editor.document.insertString(offset, "\n${indentString}end enum")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranDerivedTypeDef -> if (constructOrUnit.endTypeStmt == null) {
                val typeName = constructOrUnit.derivedTypeStmt.typeDecl.name
                editor.document.insertString(offset, "\n${indentString}end type $typeName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranInterfaceBlock -> if (constructOrUnit.endInterfaceStmt == null) {
                val interfaceName = constructOrUnit.interfaceStmt.entityDecl?.name
                if (interfaceName != null){
                    editor.document.insertString(offset, "\n${indentString}end interface $interfaceName")
                } else {
                    editor.document.insertString(offset, "\n${indentString}end interface")
                }
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            // executable constructs
            is FortranExecutableConstruct -> if (constructOrUnit.endConstructStmt == null) {
                val constructName = constructOrUnit.beginConstructStmt!!.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, constructOrUnit.constructType, constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }
        }

        return super.postProcessEnter(file, editor, dataContext)
    }

    private fun insertEndConstructString(editor: Editor, offset: Int, indentString: String?, construct: String?, constructName: String?) {
        if (constructName != null){
            editor.document.insertString(offset, "\n${indentString}end $construct $constructName")
        } else {
            editor.document.insertString(offset, "\n${indentString}end $construct")
        }
    }

}