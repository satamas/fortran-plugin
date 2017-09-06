package org.jetbrains.fortran.debugger.lang

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import org.jetbrains.fortran.FortranFileType

class FortranDebuggerEditorsProvider : XDebuggerEditorsProvider() {
    override fun getFileType(): FileType = FortranFileType

    override fun createDocument(project: Project, text: String, sourcePosition: XSourcePosition?, mode: EvaluationMode): Document {
        return EditorFactory.getInstance().createDocument(text)
    }
}
