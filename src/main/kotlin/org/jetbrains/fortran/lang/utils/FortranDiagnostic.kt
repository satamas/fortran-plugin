package org.jetbrains.fortran.lang.utils

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.ide.inspections.FortranTypeCheckInspection
import org.jetbrains.fortran.lang.psi.FortranCompositeElementType
import org.jetbrains.fortran.lang.types.ty.FortranType
import org.jetbrains.fortran.lang.types.ty.escaped

sealed class FortranDiagnostic(
        val element: PsiElement,
        val endElement: PsiElement? = null,
        val inspectionClass: Class<*>) {
    abstract fun prepare(): PreparedAnnotation

    fun addToHolder(holder: ProblemsHolder) {
        val prepared = prepare()
        val descriptor = holder.manager.createProblemDescriptor(
                element,
                endElement ?: element,
                "<html>${prepared.header}<br>${prepared.description}</html>",
                prepared.severity.toProblemHighlightType(),
                holder.isOnTheFly,
                *prepared.fixes.toTypedArray()
        )
        holder.registerProblem(descriptor)
    }



    class TypeError(
            element: PsiElement,
            private val expectedTy: FortranType,
            private val actualTy: FortranType
    ) : FortranDiagnostic(element, inspectionClass = FortranTypeCheckInspection::class.java) {
        override fun prepare(): PreparedAnnotation {
            return PreparedAnnotation(
                    Severity.ERROR,
                    "mismatched types",
                    expectedFound(expectedTy, actualTy),
                    fixes = emptyList()
            )
        }

        private fun expectedFound(expectedTy: FortranType, actualTy: FortranType): String {
            return "expected `${expectedTy.escaped}`, found `${actualTy.escaped}`"
        }
    }

    class MalformedBinaryExpression(
            element: PsiElement,
            private val leftArgumentType: FortranType,
            private val rightArgumentType: FortranType
    ) : FortranDiagnostic(element, inspectionClass = FortranTypeCheckInspection::class.java) {
        override fun prepare(): PreparedAnnotation {
            return PreparedAnnotation(
                    Severity.ERROR,
                    "mismatched argument types", differentArgumentTypes(leftArgumentType, rightArgumentType),
                    fixes = emptyList()

            )
        }

        private fun differentArgumentTypes(leftArgumentType: FortranType, rightArgumentType: FortranType): String {
            return "left argument: ${leftArgumentType.escaped}, right argument: ${rightArgumentType.escaped}"
        }
    }

    class MalformedArrayConstructor(element: PsiElement,
                                    private val arrayBaseType: FortranType,
                                    private val mismatchedType: FortranType
    ) : FortranDiagnostic(element, inspectionClass = FortranTypeCheckInspection::class.java) {
        override fun prepare(): PreparedAnnotation {
            return PreparedAnnotation(
                    Severity.ERROR,
                    "mismatched array constructor element type", mismatchedTypes(arrayBaseType, mismatchedType),
                    fixes = emptyList()
            )
        }

        private fun mismatchedTypes(arrayBaseType: FortranType, mismatchedType: FortranType): String {
            return "array base type: ${arrayBaseType.escaped}, element type: ${mismatchedType.escaped}"
        }
    }

    class MalformedImplicitDoLoop(element: PsiElement,
                                  private val elementType: FortranType
    ) : FortranDiagnostic(element, inspectionClass = FortranTypeCheckInspection::class.java) {
        override fun prepare(): PreparedAnnotation {
            return PreparedAnnotation(
                    Severity.ERROR,
                    "incorrect implicit do loop", mismatchedType(elementType),
                    fixes = emptyList()
            )
        }

        private fun mismatchedType(elementType: FortranType): String {
            return "expected: integer, got: ${elementType.escaped}"
        }
    }
}


class PreparedAnnotation(
        val severity: Severity,
        val header: String,
        val description: String,
        val fixes: List<LocalQuickFix> = emptyList()
)

enum class Severity {
    INFO, WARN, ERROR;

    fun toProblemHighlightType(): ProblemHighlightType = when (this) {
        Severity.INFO -> ProblemHighlightType.INFORMATION
        Severity.WARN -> ProblemHighlightType.WEAK_WARNING
        Severity.ERROR -> ProblemHighlightType.GENERIC_ERROR_OR_WARNING
    }
}