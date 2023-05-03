package org.jetbrains.fortran.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*

class FortranLabeledDoConstructImpl(node: ASTNode?) : FortranExecutableConstructImpl(node), FortranLabeledDoConstruct {
    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is FortranVisitor) accept(visitor) else super.accept(visitor)
    }

    override val exprList: List<FortranExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, FortranExpr::class.java)
    override val labelDoStmt: FortranLabelDoStmt
        get() = findNotNullChildByClass(FortranLabelDoStmt::class.java)
    override val doTermActionStmt: FortranStmt?
        get() = if (lastChild is FortranStmt && lastChild !is FortranEndDoStmt) lastChild as FortranStmt else null
    override val labeledDoTermConstract: FortranLabeledDoConstruct?
        get() = if (lastChild is FortranLabeledDoConstruct) lastChild as FortranLabeledDoConstruct else null
    override val endDoStmt: FortranEndDoStmt?
        get() = findChildByClass(FortranEndDoStmt::class.java)
}
