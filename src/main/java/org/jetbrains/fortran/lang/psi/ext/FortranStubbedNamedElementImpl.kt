package org.jetbrains.fortran.lang.psi.ext

import com.intellij.ide.projectView.PresentationData
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.core.stubs.FortranNamedStub

abstract class FortranStubbedNamedElementImpl<StubT> : FortranStubbedElementImpl<StubT>,
        FortranNamedElement,
        PsiNameIdentifierOwner
where StubT : FortranNamedStub, StubT : StubElement<*> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement?
            = findChildByType(FortranTypes.IDENTIFIER)

    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    override fun getPresentation(): ItemPresentation {
        return PresentationData(
                name, "SOMEWHERE", getIcon(0), null)
    }
}