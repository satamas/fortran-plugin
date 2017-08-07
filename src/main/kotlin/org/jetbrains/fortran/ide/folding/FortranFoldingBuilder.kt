package org.jetbrains.fortran.ide.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.beginConstructStatement
import org.jetbrains.fortran.lang.psi.ext.endConstructStatement
import org.jetbrains.fortran.lang.psi.ext.lastChildOfType
import java.util.*

class FortranFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun getPlaceholderText(node: ASTNode) = "..."

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<out FoldingDescriptor> {
        if (root !is FortranFile && root !is FortranFixedFormFile) return emptyArray()

        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        val visitor = FoldingVisitor(descriptors)
        PsiTreeUtil.processElements(root) { it.accept(visitor); true }

        return descriptors.toTypedArray()
    }

    private class FoldingVisitor(
            private val descriptors: MutableList<FoldingDescriptor>
    ) : FortranVisitor() {

        val foldableConstructStartStatements = listOf(
                FortranWhereConstructStmt::class,
                FortranMaskedElsewhereStmt::class,
                FortranElsewhereStmt::class,
                FortranForallConstructStmt::class,
                FortranAssociateStmt::class,
                FortranBlockStmt::class,
                FortranCriticalStmt::class,
                FortranNonlabelDoStmt::class,
                FortranLabelDoStmt::class,
                FortranIfThenStmt::class,
                FortranElseIfStmt::class,
                FortranElseStmt::class,
                FortranCaseStmt::class,
                FortranTypeGuardStmt::class,
                FortranProgramStmt::class,
                FortranModuleStmt::class,
                FortranSubmoduleStmt::class,
                FortranBlockDataStmt::class,
                FortranFunctionStmt::class,
                FortranSubroutineStmt::class,
                FortranMpSubprogramStmt::class,
                FortranContainsStmt::class
        )

        val foldableConstructEndStatements = listOf(
                FortranMaskedElsewhereStmt::class,
                FortranElsewhereStmt::class,
                FortranEndWhereStmt::class,
                FortranEndForallStmt::class,
                FortranEndAssociateStmt::class,
                FortranEndBlockStmt::class,
                FortranEndCriticalStmt::class,
                FortranEndDoStmt::class,
                FortranElseIfStmt::class,
                FortranElseStmt::class,
                FortranEndIfStmt::class,
                FortranCaseStmt::class,
                FortranEndSelectStmt::class,
                FortranTypeGuardStmt::class,
                FortranEndProgramStmt::class,
                FortranEndModuleStmt::class,
                FortranEndSubmoduleStmt::class,
                FortranEndBlockDataStmt::class,
                FortranEndFunctionStmt::class,
                FortranEndSubroutineStmt::class,
                FortranEndMpSubprogramStmt::class,
                FortranContainsStmt::class
        )

        override fun visitDeclarationConstruct(o: FortranDeclarationConstruct) =
                foldBetweenStatements(o, o.beginConstructStatement, o.endConstructStatement)

        override fun visitBlock(block: FortranBlock) {
            val parent = block.parent
            if (parent is FortranLabeledDoConstruct) {
                if (parent.doTermActionStmt != null) {
                    foldBetweenStatements(block, parent.labelDoStmt, parent.doTermActionStmt)
                } else if (parent.labeledDoTermConstract != null) {
                    foldBetweenStatements(block, parent.labelDoStmt, parent.labeledDoTermConstract)
                } else if (parent.endDoStmt != null) {
                    foldBetweenStatements(block, parent.labelDoStmt, parent.endDoStmt)
                }
                return
            }

            val prev = PsiTreeUtil.getPrevSiblingOfType(block, FortranCompositeElement::class.java) ?: return
            val startFoldableStatementType = foldableConstructStartStatements.find { it.isInstance(prev) } ?: return

            var next = PsiTreeUtil.getNextSiblingOfType(block, FortranCompositeElement::class.java)
            if (next is FortranInternalSubprogramPart) next = next.containsStmt
            else if (next is FortranTypeBoundProcedurePart) next = next.containsStmt
            else if (next is FortranModuleSubprogramPart) next = next.containsStmt

            val endFoldableStatementType = foldableConstructEndStatements.find { it.isInstance(next) } ?: return

            if (startFoldableStatementType in foldableConstructEndStatements ||
                    endFoldableStatementType in foldableConstructStartStatements) {
                val endStatement = block.lastChildOfType(FortranCompositeElement::class) ?: return
                val range = TextRange(prev.textOffset + prev.textLength, endStatement.textOffset + endStatement.textLength)
                descriptors += FoldingDescriptor(block.node, range)
            } else {
                foldBetweenStatements(block, prev, next)
            }
        }

        private fun foldBetweenStatements(element: PsiElement, left: PsiElement?, right: PsiElement?) {
            if (left != null && right != null) {
                val rightLabel = PsiTreeUtil.getChildOfType(right, FortranLabelDecl::class.java)
                val rightOffset = if (rightLabel != null) {
                    var firstNonLabelElement = rightLabel.nextSibling
                    while (firstNonLabelElement is PsiWhiteSpace) {
                        firstNonLabelElement = firstNonLabelElement.nextSibling
                    }
                    firstNonLabelElement.textOffset
                } else {
                    right.textOffset
                }
                val range = TextRange(left.textOffset + left.textLength, rightOffset)
                descriptors += FoldingDescriptor(element.node, range)
            }
        }
    }

    override fun isCollapsedByDefault(node: ASTNode) = false

}