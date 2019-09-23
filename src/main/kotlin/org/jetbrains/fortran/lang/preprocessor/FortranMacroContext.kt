package org.jetbrains.fortran.lang.preprocessor

import gnu.trove.THashMap
import java.util.*


interface FortranMacrosContext {
    fun define(macro: FortranMacro)
    fun undefine(name: String)
    fun isDefined(name: String): Boolean
    fun enterIf(decision: Boolean)
    fun exitIf()
    fun enterElse()
    fun enterElseIf(decision: Boolean)
    fun inEvaluatedContext(): Boolean
}

class FortranMacrosContextImpl : FortranMacrosContext {

    class Condition(
            val decision: Boolean,
            val isElseIf: Boolean = false
    )

    private val macros = THashMap<String, FortranMacro>()
    private val nestedConditions = ArrayDeque<Condition>()

    override fun define(macro: FortranMacro) {
        macros[macro.name] = macro
    }

    override fun undefine(name: String) {
        macros.remove(name)
    }

    override fun isDefined(name: String) = macros.contains(name)

    override fun enterIf(decision: Boolean) {
        nestedConditions.push(Condition(decision))
    }

    override fun exitIf() {
        if (nestedConditions.size != 0) {
            var cnd = nestedConditions.pop()
            while (nestedConditions.size != 0 && cnd.isElseIf){
                cnd = nestedConditions.pop()
            }
        }
    }

    override fun enterElse() {
        if (nestedConditions.size != 0) {
            val ifCond = nestedConditions.pop()
            nestedConditions.push(Condition(!ifCond.decision, ifCond.isElseIf))
        }
    }

    override fun enterElseIf(decision: Boolean) {
        if (nestedConditions.size != 0) {
            val ifCond = nestedConditions.pop()
            nestedConditions.push(Condition(!ifCond.decision, ifCond.isElseIf))
            nestedConditions.push(Condition(decision, isElseIf = true))
        }
    }

    override fun inEvaluatedContext(): Boolean {
        return nestedConditions.fold(true) { total, el -> total && el.decision }
    }
}