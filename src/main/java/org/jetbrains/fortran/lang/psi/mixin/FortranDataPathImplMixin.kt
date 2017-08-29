package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.core.stubs.FortranDataPathStub
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranPathReferenceImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranReference

abstract class FortranDataPathImplMixin : FortranStubbedElementImpl<FortranDataPathStub>, FortranDataPath {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranDataPathStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getReference(): FortranReference = FortranPathReferenceImpl(this)

    override val referenceNameElement: PsiElement  get() = checkNotNull(findChildByType(FortranTypes.IDENTIFIER)) {
        "Path must contain identifier: $this ${this.text} at ${this.containingFile.virtualFile.path}"
    }

    override val referenceName: String get() = stub?.name ?: referenceNameElement.text
}


