package org.jetbrains.fortran.lang.types.ty

import org.jetbrains.fortran.lang.psi.FortranTokenType
import org.jetbrains.fortran.lang.psi.FortranTypeDeclarationStmt

sealed class FortranPrimitiveType : FortranType() {
    companion object {
        fun fromTypeSpec(typeSpec: FortranTypeDeclarationStmt): FortranType {

            if (typeSpec.characterTypeSpec != null) {
                return FortranCharacterType
            }

            val keyword = typeSpec.numberTypeSpec?.node?.firstChildNode

            return when (keyword?.elementType) {
                FortranTokenType.LOGICAL_KEYWORD -> FortranLogicalType
                FortranTokenType.COMPLEX_KEYWORD -> FortranComplexType
                FortranTokenType.INTEGER_KEYWORD -> FortranIntegerType
                FortranTokenType.REAL_KEYWORD -> FortranRealType
                FortranTokenType.DOUBLE_KEYWORD -> {
                    var nonWhitespaceElement = keyword.treeNext ?: return FortranUnknownType
                    while (FortranTokenType.WHITE_SPACES.contains(nonWhitespaceElement.elementType)) {
                        nonWhitespaceElement = nonWhitespaceElement.treeNext
                    }
                    if (nonWhitespaceElement.elementType == FortranTokenType.PRECISION_KEYWORD) {
                        FortranDoublePrecisionType
                    } else {
                        FortranUnknownType
                    }
                }
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

object FortranDoubleComplexType : FortranPrimitiveType() {
    override fun toString(): String {
        return "double precision"
    }
}