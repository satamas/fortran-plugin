package org.jetbrains.fortran.lang.types.infer

import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranEntitiesOwner
import org.jetbrains.fortran.lang.psi.impl.FortranDataPathImpl
import org.jetbrains.fortran.lang.types.ty.*
import org.jetbrains.fortran.lang.utils.FortranDiagnostic

fun inferTypesIn(element: FortranEntitiesOwner): FortranInferenceResult {
    return FortranInferenceContext(element).infer()
}

class FortranInferenceContext(val element: FortranEntitiesOwner) {
    private val exprTypes: MutableMap<FortranExpr, FortranType> = mutableMapOf()
    private val noDeclarationVariableTypes: MutableMap<String, FortranType> = mutableMapOf()
    val diagnostics: MutableList<FortranDiagnostic> = mutableListOf()

    fun infer(): FortranInferenceResult {

        if (element is FortranMainProgram) {
            for (actionStmt in element.block!!.actionStmtList) {
                processActionStatement(actionStmt)
            }
        }

        for (v in element.variables) {
            val typeDeclarationStmt = v.parent as? FortranTypeDeclarationStmt ?: continue
            val expectedType = processTypeDeclarationStatement(typeDeclarationStmt)
            processVariable(typeDeclarationStmt.entityDeclList, expectedType)
        }

        return FortranInferenceResult(exprTypes, diagnostics)
    }

    private fun processTypeDeclarationStatement(typeDeclarationStmt: FortranTypeDeclarationStmt) : FortranType {
        return if (!typeDeclarationStmt.attrSpecList.isEmpty() &&
                typeDeclarationStmt.attrSpecList[0].text.substring(0, 9) == "dimension") {
            inferFortranArrayType(typeDeclarationStmt)
        } else {
            FortranPrimitiveType.fromTypeSpec(typeDeclarationStmt)
        }
    }

    private fun processVariable(entityDeclList: List<FortranEntityDecl>, expected: FortranType) {
        if (entityDeclList.isEmpty()) {
            return
        }
        val entityDecl = entityDeclList[0]
        val expr = entityDecl.expr

        // error diagnostics has been handled inside inferArrayConstructors()
        if (expr is FortranArrayConstructor) {
            inferType(expr, expected)
            return
        }

        if (expr != null) {
            val inferred = inferType(expr, expected)
            if (!combineTypes(expected, inferred)) {
                reportTypeMismatch(expr, expected, inferred)
            }
        }
    }

    private fun processActionStatement(stmt: FortranActionStmt) {
        if (stmt is FortranExpr) {
            inferType(stmt)
        }

        // TODO handle other types of statements
    }

    private fun inferType(expr: FortranExpr, expected: FortranType? = null) : FortranType {

        val type = when (expr) {
            is FortranBinaryExpr -> inferBinaryExprType(expr)
            is FortranUnaryAddExpr -> inferUnaryAddExprType(expr)
            is FortranNotExpr -> inferNotExprType(expr)
            is FortranConstant -> inferConstantType(expr)
            is FortranLogicalLiteral -> FortranLogicalType
            is FortranComplexLiteral -> FortranComplexType
            is FortranParenthesisedExpr -> inferParenthesisedExprType(expr)
            is FortranDesignator -> inferFortranDesignatorType(expr)
            is FortranArrayConstructor -> inferArrayConstructorType(expr, expected!!)
            else -> FortranUnknownType
        }

        writeExprType(expr, type)
        return type
    }

    private fun inferBinaryExprType(expr: FortranBinaryExpr) : FortranType {

        if (expr is FortranPowerExpr) {
            return inferBinaryPowerExprType(expr)
        }

        if (expr is FortranConcatExpr) {
            return inferBinaryConcatExprType(expr)
        }

        val leftType = inferType(expr.left())

        val rightExpr = expr.right() ?: return leftType
        val rightType = if (expr is FortranAssignmentStmt) inferType(rightExpr, leftType)
                        else inferType(rightExpr)

        // specific handler for complex numbers
        if (leftType === FortranComplexType && rightType === FortranComplexType &&
                (expr is FortranAddExpr || expr is FortranMultExpr)) {
            return FortranComplexType
        }

        if (expr is FortranAssignmentStmt) {
            if (rightType is FortranArrayType) {}
            if (!combineTypes(leftType, rightType)) {
                val exprRight = expr.right()
                if (exprRight !is FortranArrayConstructor && exprRight != null) {
                    reportTypeMismatch(exprRight, leftType, rightType)
                }
                return FortranUnknownType
            }

            return leftType
        }

        val exprType = if (expr is FortranAddExpr || expr is FortranMultExpr ||
                expr is FortranRelExpr)
            inferArithmBinaryExprType(leftType, rightType)
        else inferLogicalBinaryExprType(leftType, rightType)

        if (exprType === FortranUnknownType) {
            reportMalformedBinaryExpression(expr, leftType, rightType)
        }

        return exprType
    }

    private fun inferArithmBinaryExprType(leftType: FortranType, rightType: FortranType) : FortranType {
        if (leftType === FortranIntegerType) {
            when (rightType) {
                is FortranIntegerType -> return FortranIntegerType
                is FortranRealType -> return FortranRealType
                is FortranDoublePrecisionType -> return FortranDoublePrecisionType
            }
        }

        if (leftType === FortranRealType) {
            when (rightType) {
                is FortranIntegerType, FortranRealType -> return FortranRealType
                is FortranDoublePrecisionType -> return FortranDoublePrecisionType
            }
        }

        if (leftType === FortranDoublePrecisionType &&
                (rightType === FortranIntegerType ||
                 rightType === FortranRealType ||
                 rightType === FortranDoublePrecisionType)) {
            return FortranDoublePrecisionType
        }

        if (leftType is FortranArrayType) {
            return inferArrayArithmBinaryExprType(leftType, rightType)
        } else if (rightType is FortranArrayType) {
            return inferArrayArithmBinaryExprType(rightType, leftType)
        }

        return FortranUnknownType
    }

    private fun inferArrayArithmBinaryExprType(leftType: FortranArrayType, rightType: FortranType) : FortranType {
        when (rightType) {
            is FortranArrayType -> {
                if (leftType.shape == rightType.shape) {
                    val baseType = inferArithmBinaryExprType(leftType.base, rightType.base)
                    return if (baseType !== FortranUnknownType) {
                        FortranArrayType(baseType, leftType.shape)
                    } else {
                        FortranUnknownType
                    }
                }

                return FortranUnknownType
            }
            else -> {
                val baseType = inferArithmBinaryExprType(leftType.base, rightType)
                return if (baseType !== FortranUnknownType) {
                    FortranArrayType(baseType, leftType.shape)
                } else {
                    FortranUnknownType
                }
            }
        }
    }

    private fun inferLogicalBinaryExprType(leftType: FortranType, rightType: FortranType) : FortranType {
        if (leftType === FortranLogicalType && rightType === FortranLogicalType) {
            return FortranLogicalType
        }

        return FortranUnknownType
    }

    private fun inferBinaryPowerExprType(expr: FortranPowerExpr): FortranType {
        val leftType = inferType(expr.left())
        if (expr.right() == null) {
            return if (leftType === FortranIntegerType ||
                       leftType === FortranRealType ||
                       leftType === FortranDoublePrecisionType) {
                leftType
            } else {
                FortranUnknownType
            }
        }

        val rightType = inferType(expr.right()!!)
        if (rightType !== FortranIntegerType) {
            reportMalformedBinaryExpression(expr, leftType, rightType)
            return FortranUnknownType
        }

        return when (leftType) {
            is FortranIntegerType -> FortranIntegerType
            is FortranRealType -> FortranRealType
            is FortranDoublePrecisionType -> FortranDoublePrecisionType
            else -> {
                reportMalformedBinaryExpression(expr, leftType, rightType)
                FortranUnknownType
            }
        }
    }

    private fun inferBinaryConcatExprType(expr: FortranConcatExpr): FortranType {
        val leftType = inferType(expr.left())
        if (expr.right() == null) {
            return if (leftType === FortranCharacterType) {
                leftType
            } else {
                FortranUnknownType
            }
        }

        val rightType = inferType(expr.right()!!)

        return if (leftType === FortranCharacterType && rightType === FortranCharacterType) {
            FortranCharacterType
        } else {
            reportMalformedBinaryExpression(expr, leftType, rightType)
            FortranUnknownType
        }
    }

    private fun inferParenthesisedExprType(expr: FortranParenthesisedExpr) : FortranType {
        val subexpr = expr.expr ?: return FortranUnknownType
        return inferType(subexpr)
    }

    private fun inferUnaryAddExprType(expr: FortranUnaryAddExpr) : FortranType {
        val subexpr = expr.expr ?: return FortranUnknownType
        val subexprType = inferType(subexpr)

        return when (subexprType) {
            is FortranIntegerType -> FortranIntegerType
            is FortranRealType -> FortranRealType
            is FortranDoublePrecisionType -> FortranDoublePrecisionType
            else -> FortranUnknownType
        }
    }

    private fun inferConstantType(expr: FortranConstant) : FortranType {
        when {
            expr.integerliteral != null -> return FortranIntegerType
            expr.floatingpointliteral != null -> return FortranRealType
            expr.doubleprecisionliteral != null -> return FortranDoublePrecisionType
            expr.stringliteral != null -> return FortranCharacterType
        }

        return FortranUnknownType
    }

    private fun inferNotExprType(expr: FortranNotExpr) : FortranType {
        val subexpr = expr.expr ?: return FortranUnknownType
        val subexprType = inferType(subexpr)

        return if (subexprType != FortranLogicalType) FortranUnknownType else FortranLogicalType
    }

    private fun inferArrayConstructorType(expr: FortranArrayConstructor, expected: FortranType) : FortranType {
        val elementTypes: MutableMap<FortranExpr, FortranType> = mutableMapOf()
        var valuesSize = 0
        var extraSize = 0
        var extraType: FortranType? = null
        for (spec in expr.acSpec.acValueList) {
            val specExpr = spec.expr
            if (specExpr != null) {
                valuesSize++
                elementTypes[specExpr] = inferType(specExpr)
            } else {
                val acImpliedDo = spec.acImpliedDo
                if (acImpliedDo != null) {
                    val impliedDoType = handleAcImpliedDo(acImpliedDo)
                            as? FortranArrayType ?: return FortranUnknownType
                    // since implied do loops are always one-dimensional, we can do this
                    extraSize += (impliedDoType.shape.dimensions[0].second - impliedDoType.shape.dimensions[0].first + 1)
                    extraType = impliedDoType.base // should always be Integer
                }
            }
        }

        val arrayBaseType = inferArrayBaseType(elementTypes, extraType)

        if (arrayBaseType !== FortranUnknownType) {
            var arrayStart = 1
            if (expected is FortranArrayType && expected.shape.dimensions.size == 1) {
                arrayStart = expected.shape.dimensions[0].first
            }
            val arrayType = FortranArrayType(arrayBaseType, FortranArrayShape(listOf(arrayStart to arrayStart + valuesSize + extraSize - 1)))
            if (!combineTypes(expected, arrayType)) {
                reportTypeMismatch(expr, expected, arrayType)
            }

            return arrayType
        }

        handleMalformedArrayConstructor(expr, elementTypes, extraType, expected)

        return FortranUnknownType
    }

    private fun inferArrayBaseType(elementTypes: Map<FortranExpr, FortranType>,
                                   extraType: FortranType? = null): FortranType {
        val arrayTypes = elementTypes.values
        var arrayBaseType: FortranType
        if (!arrayTypes.isEmpty()) {
            arrayBaseType = arrayTypes.fold(arrayTypes.toList()[0], {t1, t2 -> unifyTypes(t1, t2)})
            if (extraType != null) {
                arrayBaseType = unifyTypes(arrayBaseType, extraType)
            }
        } else if (extraType != null) {
            arrayBaseType = extraType
        } else {
            arrayBaseType = FortranUnknownType
        }

        return arrayBaseType
    }

    private fun handleMalformedArrayConstructor(expr: FortranArrayConstructor,
                                                elementTypes: Map<FortranExpr, FortranType>,
                                                extraType: FortranType? = null,
                                                expected: FortranType) {
        if (expected !is FortranArrayType) {
            return
        }

        val expectedBase = expected.base
        if (expectedBase === FortranUnknownType) {
            return
        }

        if (extraType != null && !combineTypes(expectedBase, extraType)) {
            for (spec in expr.acSpec.acValueList) {
                reportMalformedArrayConstructor(expr, expected, extraType)
            }
        }

        if (expectedBase is FortranPrimitiveType) { // as of now we can only handle those
            for (entry in elementTypes) {
                if (!combineTypes(expectedBase, entry.value)) {
                    reportMalformedArrayConstructor(entry.key, expectedBase, entry.value)
                }
            }
        }
    }

    private fun handleAcImpliedDo(ac: FortranAcImpliedDo): FortranType {
        if (ac.idLoopStmt.exprList.size < 3) {
            return FortranUnknownType
        }

        val impliedDoLoopStart = ac.idLoopStmt.exprList[1]
        val impliedDoLoopEnd = ac.idLoopStmt.exprList[2]

        val shape: FortranArrayShape
        shape = try {
            val impliedDoLoopStartValue = impliedDoLoopStart.text.toInt()
            val impliedDoLoopEndValue = impliedDoLoopEnd.text.toInt()
            val size = impliedDoLoopEndValue - impliedDoLoopStartValue + 1
            FortranArrayShape(listOf(1 to size))
        } catch (e: NumberFormatException) {
            FortranUnknownShape
        }

        val impliedDoLoopStartType = inferType(impliedDoLoopStart)
        val impliedDoLoopEndType = inferType(impliedDoLoopEnd)
        if (impliedDoLoopEndType !== FortranIntegerType) {
            reportMalformedImpliedDoLoop(impliedDoLoopEnd, impliedDoLoopEndType)
            return FortranUnknownType
        }

        if (impliedDoLoopStartType !== FortranIntegerType) {
            reportMalformedImpliedDoLoop(impliedDoLoopStart, impliedDoLoopStartType)
            return FortranUnknownType
        }

        return FortranArrayType(FortranIntegerType, shape)
    }

    private fun inferFortranDesignatorType(expr: FortranDesignator) : FortranType {
        val variableEntityDecl = expr.dataPath?.reference?.resolve() as? FortranEntityDecl ?: return FortranUnknownType
        //TODO: more accurate handling
        val variableTypeDecl = variableEntityDecl.parent as? FortranTypeDeclarationStmt ?: return FortranUnknownType
        val variableType = processTypeDeclarationStatement(variableTypeDecl)
        // accessing array element instead of array
        return if ((expr.dataPath as FortranDataPathImpl).getSectionSubscript() != null &&
                variableType is FortranArrayType) {
            variableType.base
        } else {
            variableType
        }
    }

    private fun inferFortranArrayType(declarationStmt: FortranTypeDeclarationStmt): FortranType {
        val primitiveType = FortranPrimitiveType.fromTypeSpec(declarationStmt)
        if (primitiveType === FortranUnknownType) {
            return FortranUnknownType
        }
        val dimensions = mutableListOf<Pair<Int, Int>>()
        for (explicitShapeSpec in declarationStmt.attrSpecList[0].explicitShapeSpecList) {
            val arrayBounds = explicitShapeSpec.exprList
            when {
                arrayBounds.size == 1 -> dimensions.add(1 to explicitShapeSpec.exprList[0].text.toInt())
                explicitShapeSpec.exprList.size == 2 -> {
                    val arrayStart = arrayBounds[0].text.toInt()
                    val arrayEnd = arrayBounds[1].text.toInt()
                    dimensions.add(arrayStart to arrayEnd)
                }
                else -> return FortranArrayType(primitiveType, FortranUnknownShape)
            }
        }

        return FortranArrayType(primitiveType, FortranArrayShape(dimensions))
    }

    private fun combineTypes(expected: FortranType, inferred: FortranType): Boolean = when {
        expected === inferred -> true
        expected is FortranArrayType && inferred is FortranArrayType -> combineArrayTypes(expected, inferred)
        expected === FortranRealType && inferred === FortranIntegerType -> true
        expected === FortranDoublePrecisionType &&
                (inferred === FortranRealType || inferred === FortranIntegerType) -> true
        else -> false
    }

    private fun unifyTypes(type1: FortranType, type2: FortranType): FortranType {
        if (type1 == type2) {
            return type1
        }

        if (type1 is FortranArrayType && type2 is FortranArrayType) {
            return unifyArrayTypes(type1, type2)
        }

        if (combineTypes(type1, type2)) {
            return unifyCompatibleTypes(type1, type2)
        }

        if (combineTypes(type2, type1)) {
            return unifyCompatibleTypes(type2, type1)
        }

        return FortranUnknownType
    }

    private fun unifyCompatibleTypes(type1: FortranType, type2: FortranType): FortranType {
        if (type1 === FortranRealType && type2 === FortranIntegerType) {
            return FortranRealType
        }

        if (type1 === FortranDoublePrecisionType &&
                (type2 === FortranRealType || type2 === FortranIntegerType)) {
            return FortranDoublePrecisionType
        }

        return FortranUnknownType
    }

    private fun unifyArrayTypes(type1: FortranArrayType, type2: FortranArrayType): FortranType {
        if (type1 == type2) {
            return type1
        }

        if (type1.shape === FortranUnknownShape || type2.shape === FortranUnknownShape) {
            return unifyTypes(type1.base, type2.base)
        }

        if (type1.shape != type2.shape || type1.base === FortranUnknownType || type2.base === FortranUnknownType) {
            return FortranUnknownType
        }

        val unifiedBaseType = unifyTypes(type1.base, type2.base)
        return FortranArrayType(unifiedBaseType, type1.shape)
    }

    private fun combineArrayTypes(expected: FortranArrayType, inferred: FortranArrayType) : Boolean = when {
        expected == inferred -> true
        expected.shape === FortranUnknownShape || inferred.shape === FortranUnknownShape ->
            combineTypes(expected.base, inferred.base)
        expected.shape != inferred.shape -> false
        else -> combineTypes(expected.base, inferred.base)
    }

    private fun addDiagnostic(diagnostic: FortranDiagnostic) {
        diagnostics.add(diagnostic)
    }

    private fun reportTypeMismatch(expr: FortranExpr, expected: FortranType, actual: FortranType) {
        if (expected === FortranUnknownType || actual === FortranUnknownType) {
            return
        }
        addDiagnostic(FortranDiagnostic.TypeError(expr, expected, actual))
    }

    private fun reportMalformedBinaryExpression(expr: FortranBinaryExpr,
                                                leftType: FortranType, rightType: FortranType) {
        addDiagnostic(FortranDiagnostic.MalformedBinaryExpression(expr, leftType, rightType))
    }

    private fun reportMalformedArrayConstructor(expr: FortranExpr, arrayBaseType: FortranType, elementType: FortranType) {
        addDiagnostic(FortranDiagnostic.MalformedArrayConstructor(expr, arrayBaseType, elementType))
    }

    private fun reportMalformedImpliedDoLoop(expr: FortranExpr, type: FortranType) {
        addDiagnostic(FortranDiagnostic.MalformedImplicitDoLoop(expr, type))
    }

    private fun writeExprType(expr: FortranExpr, type: FortranType) {
        exprTypes[expr] = type
    }

    private fun writeNoDeclVariableType(variableName: String, type: FortranType) {
        noDeclarationVariableTypes[variableName] = type
    }
}