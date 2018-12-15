package org.jetbrains.fortran.ide.typing

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
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
        val superProcessor = super.postProcessEnter(file, editor, dataContext)
        // we don't want to deal with fixed form now
        if (file is FortranFixedFormFile) return superProcessor
        val offset = editor.caretModel.offset
        val lineStartOffset = editor.caretModel.visualLineStart
        val indentString = CodeStyleManager.getInstance(file.project)!!.getLineIndent(editor.document, lineStartOffset)
        val previousElement = file.findElementAt(offset - 1)
        val constructOrUnit = PsiTreeUtil.getParentOfType(previousElement,
                FortranExecutableConstruct::class.java,
                FortranProgramUnit::class.java,
                FortranDeclarationConstruct::class.java
        )

        // check we are not in the middle of the line
        val endFirstStmtOffset = constructOrUnit?.firstChild?.textRange?.endOffset ?: return superProcessor
        if (offset < endFirstStmtOffset) return superProcessor

        when (constructOrUnit) {
        // program units
            is FortranProgramUnit -> if ((constructOrUnit.beginUnitStmt != null && constructOrUnit.endUnitStmt == null)
                    || sameTypeParentUnitHasNoEnd(constructOrUnit)) {
                val beginStmt = constructOrUnit.beginUnitStmt!!
                val programUnitName = beginStmt.entityDecl?.name
                val unit = constructOrUnit.unitType!!
                insertEndString(editor, offset, indentString, unit, programUnitName, beginStmtStyle(beginStmt))
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

        // declaration constructs
            is FortranEnumDef -> if (constructOrUnit.endEnumStmt == null) {
                val enumStmt = constructOrUnit.enumDefStmt
                insertEndString(editor, offset, indentString, "enum", null, beginStmtStyle(enumStmt))
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranDerivedTypeDef -> if (constructOrUnit.endTypeStmt == null) {
                val typeStmt = constructOrUnit.derivedTypeStmt
                val typeName = typeStmt.typeDecl.name
                insertEndString(editor, offset, indentString, "type", typeName, beginStmtStyle(typeStmt))
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranInterfaceBlock -> if (constructOrUnit.endInterfaceStmt == null) {
                val interfaceStmt = constructOrUnit.interfaceStmt
                val interfaceName = interfaceStmt.entityDecl?.name
                insertEndString(editor, offset, indentString, "interface", interfaceName, beginStmtStyle(interfaceStmt))
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

        // labeled do is not like all other peoples do
            is FortranLabeledDoConstruct -> if (constructOrUnit.endConstructStmt == null
                    && constructOrUnit.doTermActionStmt == null
                    && constructOrUnit.labeledDoTermConstract == null) {
                val beginStmt = constructOrUnit.beginConstructStmt!!
                val constructName = beginStmt.constructNameDecl?.name
                val indentStringWithLabel = indentString + constructOrUnit.labelDoStmt.label.text + " "
                insertEndString(editor, offset, indentStringWithLabel, constructOrUnit.constructType, constructName, beginStmtStyle(beginStmt))
                return EnterHandlerDelegate.Result.DefaultForceIndent

            }
        // executable constructs
            is FortranExecutableConstruct -> if (constructOrUnit.endConstructStmt == null || sameTypeParentConstructHasNoEnd(constructOrUnit)) {
                val beginStmt = constructOrUnit.beginConstructStmt!!
                val constructName = beginStmt.constructNameDecl?.name
                insertEndString(editor, offset, indentString, constructOrUnit.constructType, constructName, beginStmtStyle(beginStmt))
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }
        }

        return super.postProcessEnter(file, editor, dataContext)
    }


    private fun sameTypeParentUnitHasNoEnd(unit: FortranProgramUnit): Boolean {
        var parent = PsiTreeUtil.getParentOfType(unit, FortranProgramUnit::class.java)
        while (parent != null) {
            if (parent.unitType != unit.unitType) return false
            if (parent.endUnitStmt == null) return true
            parent = PsiTreeUtil.getParentOfType(parent, FortranProgramUnit::class.java)
        }
        return false
    }

    private fun sameTypeParentConstructHasNoEnd(construct: FortranExecutableConstruct): Boolean {
        // construct parent is block and blocks parent may be construct
        var grandparent = construct.parent?.parent
        while (grandparent != null) {
            if (grandparent !is FortranExecutableConstruct || grandparent.constructType != construct.constructType) return false
            if (grandparent.endConstructStmt == null) return true
            grandparent = grandparent.parent?.parent
        }
        return false
    }

    private enum class KeywordStyle {
        UNKNOWN,
        UPPERCASE,
        LOWERCASE,
        CAMELCASE
    }

    private fun beginStmtStyle(stmt: FortranStmt): KeywordStyle {
        val keywordText = stmt.node.findChildByType(FortranTokenType.KEYWORD)?.text ?: return KeywordStyle.UNKNOWN
        return when {
            keywordText.all { it.isUpperCase() } -> KeywordStyle.UPPERCASE
            keywordText.all { it.isLowerCase() } -> KeywordStyle.LOWERCASE
            keywordText.first().isUpperCase() && keywordText.substring(1).all { it.isLowerCase() } ->
                KeywordStyle.CAMELCASE
            else -> KeywordStyle.UNKNOWN
        }
    }

    private fun formatEndString(endString: String, style: KeywordStyle): String {
        return when (style) {
            KeywordStyle.UPPERCASE -> endString.toUpperCase()
            KeywordStyle.CAMELCASE -> endString.capitalize()
            else -> endString
        }
    }

    private fun insertEndString(editor: Editor,
                                offset: Int,
                                indentString: String?,
                                type: String?,
                                name: String?,
                                style: KeywordStyle
    ) {
        val end = formatEndString("end", style)
        val formattedType = formatEndString(type ?: "", style)
        if (name != null) {
            editor.document.insertString(offset, "\n$indentString$end $formattedType $name")
        } else {
            editor.document.insertString(offset, "\n$indentString$end $formattedType")
        }
    }

}