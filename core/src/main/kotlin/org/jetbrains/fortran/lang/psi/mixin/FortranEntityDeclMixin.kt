package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.FortranIcons
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl
import javax.swing.Icon

abstract class FortranEntityDeclMixin :
    FortranStubbedNamedElementImpl<org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub>, FortranEntityDecl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub, nodeType: IStubElementType<*, *>) : super(
        stub,
        nodeType
    )

    override fun getNameIdentifier(): PsiElement? = stub?.psi ?: findChildByType(FortranTypes.IDENTIFIER)

    override fun getTextOffset(): Int = node.startOffset

    override fun getName(): String? = stub?.name ?: nameIdentifier?.text

    override fun setName(name: String): PsiElement? {
        return this
    }

    override fun getIcon(flags: Int): Icon? {
        val grandParent = parent.parent
        if (grandParent is FortranProgramUnit) {
            return when(grandParent) {
                is FortranMainProgram -> FortranIcons.mainProgramIcon
                is FortranFunctionSubprogram -> FortranIcons.functionIcon
                is FortranSubroutineSubprogram -> FortranIcons.subroutineIcon
                is FortranModule -> FortranIcons.moduleIcon
                is FortranSubmodule -> FortranIcons.submoduleIcon
                is FortranSeparateModuleSubprogram -> FortranIcons.separateModuleSubprogramIcon
                is FortranBlockData -> FortranIcons.blockDataIcon

                else -> super.getIcon(flags)
            }
        }
        if (grandParent is FortranDerivedTypeDef) return FortranIcons.typeIcon
        if (grandParent is FortranDataComponentDefStmt) return FortranIcons.variableIcon
        if (parent is FortranTypeDeclarationStmt) return FortranIcons.variableIcon
        return super.getIcon(flags)
    }
}