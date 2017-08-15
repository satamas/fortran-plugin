package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranPathReferenceImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranReference

abstract class FortranDataPathImplMixin(node: ASTNode) : FortranCompositeElementImpl(node), FortranDataPath {
    override fun getReference(): FortranReference = FortranPathReferenceImpl(this)

    override val referenceNameElement: PsiElement  get() = checkNotNull(findChildByType(FortranTypes.IDENTIFIER)) {
        "Path must contain identifier: $this ${this.text} at ${this.containingFile.virtualFile.path}"
    }

    override val referenceName: String get() = referenceNameElement.text
}


