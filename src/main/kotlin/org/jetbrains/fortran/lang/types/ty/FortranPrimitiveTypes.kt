package org.jetbrains.fortran.lang.types.ty

import org.jetbrains.fortran.lang.psi.FortranIntrinsicTypeSpec

abstract class FortranPrimitiveType : FortranType() {
    companion object {
        fun fromTypeSpec(typeSpec: FortranIntrinsicTypeSpec?): FortranType {

            if (typeSpec?.characterTypeKeyword != null) {
                return FortranCharacterType
            }

            val keyword = typeSpec?.numberTypeKeyword?.text

            return when (keyword) {
                "logical" -> FortranLogicalType
                "character" -> FortranCharacterType
                "complex" -> FortranComplexType
                "integer" -> FortranIntegerType
                "real" -> FortranRealType
                "double precision" -> FortranDoublePrecisionType
                else -> FortranUnknownType
            }
        }
    }
}

object FortranLogicalType : FortranPrimitiveType() {
    override fun toString(): String {
        return "logical"
    }
}

object FortranCharacterType : FortranPrimitiveType() {
    override fun toString(): String {
        return "character"
    }
}

object FortranComplexType : FortranPrimitiveType() {
    override fun toString(): String {
        return "complex"
    }
}

object FortranIntegerType : FortranPrimitiveType() {
    override fun toString(): String {
        return "integer"
    }
}

object FortranRealType : FortranPrimitiveType() {
    override fun toString(): String {
        return "real"
    }
}

object FortranDoublePrecisionType : FortranPrimitiveType() {
    override fun toString(): String {
        return "double precision"
    }
}