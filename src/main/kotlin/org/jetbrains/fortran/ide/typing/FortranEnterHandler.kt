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
        // we don't want to deal with fixed form now
        if (file is FortranFixedFormFile) return super.postProcessEnter(file, editor, dataContext)

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
            is FortranProgramUnit -> if ((constructOrUnit.beginUnitStmt != null && constructOrUnit.endUnitStmt == null)
                                             || sameTypeParentUnitHasNoEnd(constructOrUnit)) {
                val programUnitName = constructOrUnit.beginUnitStmt!!.entityDecl!!.name
                var unitText = constructOrUnit.text
                insertEndString(editor, offset, unitText, indentString, constructOrUnit.unitType, programUnitName)
                //editor.document.insertString(offset, "\n${indentString}end ${constructOrUnit.unitType} $programUnitName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            // declaration constructs
            is FortranEnumDef -> if (constructOrUnit.endEnumStmt == null) {
                var unitText = constructOrUnit.text
                insertEndString(editor, offset, unitText, indentString, "enum", null)
                //editor.document.insertString(offset, "\n${indentString}end enum")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            is FortranDerivedTypeDef -> if (constructOrUnit.endTypeStmt == null) {
                val typeName = constructOrUnit.derivedTypeStmt.typeDecl.name
                var unitText = constructOrUnit.text
                insertEndString(editor, offset, unitText, indentString, "type", typeName)
                //editor.document.insertString(offset, "\n${indentString}end type $typeName")
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }
            
            // interface
            is FortranInterfaceBlock -> if (constructOrUnit.endInterfaceStmt == null) {
                val interfaceName = constructOrUnit.interfaceStmt.entityDecl?.name
                var unitText = constructOrUnit.text
                insertEndString(editor, offset, unitText, indentString, "interface", interfaceName)
                //if (interfaceName != null){
                //    editor.document.insertString(offset, "\n${indentString}end interface $interfaceName")
                //} else {
                //    editor.document.insertString(offset, "\n${indentString}end interface")
                //}
                return EnterHandlerDelegate.Result.DefaultForceIndent
            }

            // labeled do is not like all other peoples do
            is FortranLabeledDoConstruct -> if (constructOrUnit.endConstructStmt == null
                    && constructOrUnit.doTermActionStmt == null
                    && constructOrUnit.labeledDoTermConstract == null) {
                val constructName = constructOrUnit.beginConstructStmt!!.constructNameDecl?.name
                val indentStringWithLabel = indentString + constructOrUnit.labelDoStmt.label.text + " "
                var unitText = constructOrUnit.text
                insertEndString(editor, offset, unitText, indentStringWithLabel, constructOrUnit.constructType, constructName)
                //insertEndConstructString(editor, offset, indentStringWithLabel, constructOrUnit.constructType, constructName)
                return EnterHandlerDelegate.Result.DefaultForceIndent

            }
            // executable constructs
            is FortranExecutableConstruct -> if (constructOrUnit.endConstructStmt == null || sameTypeParentConstructHasNoEnd(constructOrUnit)) {
                val constructName = constructOrUnit.beginConstructStmt!!.constructNameDecl?.name
                var unitText = constructOrUnit.text
                insertEndString(editor, offset, unitText, indentString, constructOrUnit.constructType, constructName)
                //insertEndConstructString(editor, offset, indentString, constructOrUnit.constructType, constructName)
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
            parent = PsiTreeUtil.getParentOfType(unit, FortranProgramUnit::class.java)
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
    
    private fun insertEndString(editor: Editor, offset: Int, nodeText: String, indentString: String?, construct: String?, constructName: String?) {
        var endString = "end $construct"
        var nodeTextLines = nodeText.lines()
        var nodeTextFirstLine = nodeTextLines.firstOrNull()
        var nodePos=0
        while(nodeTextFirstLine!!.trim().isEmpty()){
            nodePos++
            nodeTextFirstLine = nodeTextLines.getOrNull(nodePos)
        }
        when(beginStmtCapitalized(nodeTextFirstLine)){
        0 -> {endString = endString.toLowerCase()}
        1 -> {endString = endString.capitalize()}
        2 -> {endString = endString.toUpperCase()}
        }
        if (constructName != null){
            editor.document.insertString(offset, "\n${indentString}${endString} $constructName")
        } else {
            editor.document.insertString(offset, "\n${indentString}${endString}")
        }
    }
    
    private fun beginStmtCapitalized(construct: String): Int {
        if(construct.trim().length<1)
            return 0
        var charCount = 0
        var firstCharIsUpperCase = false
        var subStringUpperCase = false
        var subStirngLowerCase = false
        //println("${firstCharIsUpperCase},${subStringUpperCase},${subStirngLowerCase}")
        for( char: Char in construct) {
            if ( char.isLetter()){
                var charIsUpperCase = char.isUpperCase()
                when(charCount){
                0 -> {
                    firstCharIsUpperCase = charIsUpperCase                    
                }
                1 -> {
                    subStringUpperCase = charIsUpperCase
                    subStirngLowerCase = !charIsUpperCase
                }
                else -> {
                    subStringUpperCase = subStringUpperCase && charIsUpperCase
                    subStirngLowerCase = subStirngLowerCase && (!charIsUpperCase)
                }
                
                }
                charCount++
                //println("${char}, ${char.isUpperCase()}")   
                
            }
        }
        
        //println("${firstCharIsUpperCase},${subStringUpperCase},${subStirngLowerCase}")
        //println("${charCount}")
        if(firstCharIsUpperCase)
        {
            if(subStringUpperCase){
                // all upper case
                return 2
            } else{
                if(subStirngLowerCase)
                {
                    // Captalized
                    return 1
                } else {
                    // Unkonw
                    return -1
                }
            }
        }else{
           if(subStirngLowerCase)
            {
                // all lower case
                return 0
            } else {
                // Unkonw
                return -1
            } 
        }
        return -1
    }

    private fun insertEndConstructString(editor: Editor, offset: Int, indentString: String?, construct: String?, constructName: String?) {
        if (constructName != null){
            editor.document.insertString(offset, "\n${indentString}end $construct $constructName")
        } else {
            editor.document.insertString(offset, "\n${indentString}end $construct")
        }
    }

}
