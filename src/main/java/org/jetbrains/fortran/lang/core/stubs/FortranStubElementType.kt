package org.jetbrains.fortran.lang.core.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IStubFileElementType
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.psi.FortranCompositeElement

abstract class FortranStubElementType<StubT : StubElement<*>, PsiT : FortranCompositeElement>(
    debugName: String
) : IStubElementType<StubT, PsiT>(debugName, FortranLanguage) {

    final override fun getExternalId(): String = "fortran.${super.toString()}"

    protected fun createStubIfParentIsStub(node: ASTNode): Boolean {
        val parent = node.treeParent
        val parentType = parent.elementType
        return (parentType is IStubElementType<*, *> && parentType.shouldCreateStub(parent)) ||
            parentType is IStubFileElementType<*>
    }
}