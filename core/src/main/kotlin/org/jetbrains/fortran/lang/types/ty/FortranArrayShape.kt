package org.jetbrains.fortran.lang.types.ty

open class FortranArrayShape(val dimensions: List<Pair<Int, Int>>) {
    override fun equals(other: Any?): Boolean {
        if (other !is FortranArrayShape) {
            return false
        }

        if (this === FortranUnknownShape) {
            return other === FortranUnknownShape
        }

        if (other === FortranUnknownShape) { // and this is not FortranUnknownShape
            return false
        }

        return dimensions == other.dimensions
    }

    override fun toString(): String {
        val res = StringBuilder("(")
        for (p in dimensions) {
            res.append(p.first)
            res.append(":")
            res.append(p.second)
            res.append(",")
        }
        res.deleteCharAt(res.length - 1)
        res.append(")")
        return res.toString()
    }

    override fun hashCode(): Int {
        return dimensions.hashCode()
    }
}

object FortranUnknownShape : FortranArrayShape(mutableListOf())