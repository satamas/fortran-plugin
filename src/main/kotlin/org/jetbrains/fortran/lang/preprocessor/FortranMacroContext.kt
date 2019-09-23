package org.jetbrains.fortran.lang.preprocessor

import gnu.trove.THashMap
import java.util.*


interface FortranMacrosContext {
    fun define(macro: FortranMacro)
    fun undefine(name: String)
    fun isDefined(name: String): Boolean
    fun enterIf(condition: Boolean)
    fun exitIf()
    fun enterElse()
    fun enterElseIf(condition: Boolean)
    fun inEvaluatedContext(): Boolean
}

class FortranMacrosContextImpl : FortranMacrosContext {

    private val macros = THashMap<String, FortranMacro>()
    private val nestedConditions = ArrayDeque<Boolean>()

    override fun define(macro: FortranMacro) {
        macros[macro.name] = macro
    }

    override fun undefine(name: String) {
        macros.remove(name)
    }

    override fun isDefined(name: String) = macros.contains(name)

    override fun enterIf(condition: Boolean) {
        nestedConditions.push(condition)
    }

    override fun exitIf() {
        if (nestedConditions.size != 0) {
            nestedConditions.pop()
        }
    }

    override fun enterElse() {
        if (nestedConditions.size != 0) {
            nestedConditions.push(!nestedConditions.pop())
        }
    }

    override fun enterElseIf(condition: Boolean) {
        if (nestedConditions.size != 0) {
            nestedConditions.push(!nestedConditions.pop() && condition)
        }
    }

    override fun inEvaluatedContext(): Boolean {
        return nestedConditions.fold(true) { total, el -> total && el }
    }
}