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
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil
import org.jetbrains.fortran.lang.psi.*

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
            is FortranMainProgram -> if (constructOrUnit.programStmt != null && constructOrUnit.endProgramStmt == null) {
                val programName = constructOrUnit.programStmt!!.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end program $programName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranFunctionSubprogram -> if (constructOrUnit.endFunctionStmt == null) {
                val functionName = constructOrUnit.functionStmt.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end function $functionName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranSubroutineSubprogram -> if (constructOrUnit.endSubroutineStmt == null) {
                val subroutineName = constructOrUnit.subroutineStmt.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end subroutine $subroutineName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranModule -> if (constructOrUnit.endModuleStmt == null) {
                val moduleName = constructOrUnit.moduleStmt.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end module $moduleName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranSubmodule -> if (constructOrUnit.endSubmoduleStmt == null) {
                val submoduleName = constructOrUnit.submoduleStmt.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end submodule $submoduleName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranBlockData -> if (constructOrUnit.endBlockDataStmt == null) {
                val blockDataName = constructOrUnit.blockDataStmt.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end block data $blockDataName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranSeparateModuleSubprogram -> if (constructOrUnit.endMpSubprogramStmt == null) {
                val subprogramName = constructOrUnit.mpSubprogramStmt.entityDecl!!.name
                editor.document.insertString(offset, "\n${indentString}end procedure $subprogramName")
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
            is FortranAssociateConstruct -> if (constructOrUnit.endAssociateStmt == null) {
                val constructName = constructOrUnit.associateStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "associate", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranBlockConstruct -> if (constructOrUnit.endBlockStmt == null) {
                val constructName = constructOrUnit.blockStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "block", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranCaseConstruct -> if (constructOrUnit.endSelectStmt == null) {
                val constructName = constructOrUnit.selectCaseStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "select", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranCriticalConstruct -> if (constructOrUnit.endCriticalStmt == null) {
                val constructName = constructOrUnit.criticalStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "critical", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranNonlabelDoConstruct -> if (constructOrUnit.endDoStmt == null) {
                val constructName = constructOrUnit.nonlabelDoStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "do", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranForallConstruct -> if (constructOrUnit.endForallStmt == null) {
                val constructName = constructOrUnit.forallConstructStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "forall", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranIfConstruct -> if (constructOrUnit.endIfStmt == null) {
                val constructName = constructOrUnit.ifThenStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "if", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranSelectTypeConstruct -> if (constructOrUnit.endSelectStmt == null) {
                val constructName = constructOrUnit.selectTypeStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "select", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranWhereConstruct -> if (constructOrUnit.endWhereStmt == null) {
                val constructName = constructOrUnit.whereConstructStmt.constructNameDecl?.name
                insertEndConstructString(editor, offset, indentString, "where", constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }
        }

        return super.postProcessEnter(file, editor, dataContext)
    }

    private fun insertEndConstructString(editor: Editor, offset: Int, indentString: String?, construct: String, constructName: String?) {
        if (constructName != null){
            editor.document.insertString(offset, "\n${indentString}end $construct $constructName")
        } else {
            editor.document.insertString(offset, "\n${indentString}end $construct")
        }
    }

}