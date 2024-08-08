package org.jetbrains.fortran.lang.types.ty

object FortranUnknownType : FortranType() {
    override fun toString(): String {
        return "unknown type"
    }
}