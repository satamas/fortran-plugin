package org.jetbrains.fortran.lang.preprocessor

import gnu.trove.THashMap
import org.jetbrains.fortran.lang.FortranTypes.*
import java.util.*


interface FortranMacrosContext {

    fun add(macro: FortranMacro)

    fun isDefined(name: String?): Boolean
    fun inEvaluatedContext(): Boolean

}

class FortranMacrosContextImpl : FortranMacrosContext {


    private fun ifDecisionEvaluator(directive_content: CharSequence): Boolean {
        return !directive_content.endsWith("/* macro_eval: false */")
    }

    override fun add(macro: FortranMacro) {
        when (macro.type) {
            DEFINE_DIRECTIVE -> processDefine(macro)
            UNDEFINE_DIRECTIVE -> processUndefine(macro)
            IF_DIRECTIVE -> processIf(macro)
            IF_DEFINED_DIRECTIVE -> processIfDefined(macro)
            IF_NOT_DEFINED_DIRECTIVE -> processIfNotDefined(macro)
            ELIF_DIRECTIVE -> processElseIf(macro)
            ELSE_DIRECTIVE -> processElse()
            ENDIF_DIRECTIVE -> processEndIf()
            UNKNOWN_DIRECTIVE -> processUnknown()
        }
    }

    class Condition(
            val decision: Boolean,
            val isElseIf: Boolean = false
    )

    private val macros = THashMap<String, FortranMacro>()
    private val nestedConditions = ArrayDeque<Condition>()

    private fun processDefine(macro: FortranMacro) {
        if (inEvaluatedContext()){
            macros[macro.name ?: return] = macro
        }
    }

    private fun processUndefine(macro: FortranMacro) {
        if (inEvaluatedContext()){
            macros.remove(macro.name ?: return)
        }
    }

    private fun processIf(macro: FortranMacro) {
        nestedConditions.push(Condition(ifDecisionEvaluator(macro.text)))
    }

    private fun processIfDefined(macro: FortranMacro) {
        nestedConditions.push(Condition(isDefined(macro.name)))
    }

    private fun processIfNotDefined(macro: FortranMacro) {
        nestedConditions.push(Condition(!isDefined(macro.name)))
    }

    private fun processEndIf() {
        if (nestedConditions.size != 0) {
            var cnd = nestedConditions.pop()
            while (nestedConditions.size != 0 && cnd.isElseIf) {
                cnd = nestedConditions.pop()
            }
        }
    }

    private fun processElse() {
        if (nestedConditions.size != 0) {
            val ifCond = nestedConditions.pop()
            nestedConditions.push(Condition(!ifCond.decision, ifCond.isElseIf))
        }
    }

    private fun processElseIf(macro: FortranMacro) {
        if (nestedConditions.size != 0) {
            val ifCond = nestedConditions.pop()
            nestedConditions.push(Condition(!ifCond.decision, ifCond.isElseIf))
            nestedConditions.push(Condition(ifDecisionEvaluator(macro.text), isElseIf = true))
        }
    }

    private fun processUnknown() {}

    override fun isDefined(name: String?): Boolean {
        return name != null && macros.contains(name)
    }

    override fun inEvaluatedContext(): Boolean {
        return nestedConditions.fold(true) { total, el -> total && el.decision }
    }
}

