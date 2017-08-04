package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import org.jetbrains.fortran.lang.psi.FortranCompositeElement
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranReferenceElement

abstract class FortranReferenceBase<T : FortranReferenceElement>(element: T)
    : PsiPolyVariantReferenceBase<T>(element),
        FortranReference {

        abstract fun resolveInner(): List<FortranCompositeElement>

        final override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> =
                ResolveCache.getInstance(element.project)
                        .resolveWithCaching(this, { r, _ ->
                            r.resolveInner().map(::PsiElementResolveResult).toTypedArray()
                        }, true, false)

        final override fun multiResolve(): List<FortranNamedElement> = multiResolve(false).asList().mapNotNull { it.element as? FortranNamedElement }

        abstract val T.referenceAnchor: PsiElement

        final override fun getRangeInElement(): TextRange = super.getRangeInElement()

        final override fun calculateDefaultRangeInElement(): TextRange {
            val anchor = element.referenceAnchor
            check(anchor.parent === element)
            return TextRange.from(anchor.startOffsetInParent, anchor.textLength)
        }

        override fun equals(other: Any?): Boolean = other is FortranReferenceBase<*> && element === other.element

        override fun hashCode(): Int = element.hashCode()
}