package org.jetbrains.fortran.lang.preprocessor

import gnu.trove.THashMap


interface FortranMacrosContext {
    fun define(macro: FortranMacro)
    fun undefine(name: String)
    fun isDefined(name: String): Boolean
}

class FortranMacrosContextImpl : FortranMacrosContext {
    private val macros = THashMap<String, FortranMacro>()

    override fun define(macro: FortranMacro) {
        macros[macro.name] = macro
    }

    override fun undefine(name: String) {
        macros.remove(name)
    }

    override fun isDefined(name: String) = macros.contains(name)
}