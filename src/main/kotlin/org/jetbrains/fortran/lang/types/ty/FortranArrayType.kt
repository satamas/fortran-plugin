package org.jetbrains.fortran.lang.types.ty

data class FortranArrayType(val base: FortranType, val shape: FortranArrayShape) : FortranType() {
    override fun toString(): String {
        return base.toString() + " array" + if (shape !== FortranUnknownShape) " of shape " + shape.toString() else ""
    }
}