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
                FortranMpSubprogramStmt::class
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
                FortranEndMpSubprogramStmt::class
        )

        override fun visitModuleSubprogramPart(o: FortranModuleSubprogramPart) {
            foldAfterContains(o)
        }

        override fun visitTypeBoundProcedurePart(o: FortranTypeBoundProcedurePart) {
            foldAfterContains(o)
        }

        override fun visitInternalSubprogramPart(o: FortranInternalSubprogramPart) {
            foldAfterContains(o)
        }

        private fun foldAfterContains(subprogramPart: FortranCompositeElement) {
            val lastElement = subprogramPart.lastChildOfType(FortranCompositeElement::class) ?: return
            if (lastElement == subprogramPart.firstChild) return
            val range = TextRange(subprogramPart.firstChild.node.startOffset + subprogramPart.firstChild.textLength, lastElement.node.startOffset + lastElement.node.textLength)
           createDescriptor(subprogramPart, range)
        }

        override fun visitDeclarationConstruct(o: FortranDeclarationConstruct) =
                foldBetweenStatements(o, o.beginConstructStatement, o.endConstructStatement)

        override fun visitCaseConstruct(o: FortranCaseConstruct) =
                foldBetweenStatements(o, o.selectCaseStmt, o.endSelectStmt)

        override fun visitBlock(block: FortranBlock) {
            val parent = block.parent

            if (parent is FortranLabeledDoConstruct) {
                when {
                    parent.doTermActionStmt != null -> foldBetweenStatements(block, parent.labelDoStmt, parent.doTermActionStmt)
                    parent.labeledDoTermConstract != null -> foldBetweenStatements(block, parent.labelDoStmt, parent.labeledDoTermConstract)
                    parent.endDoStmt != null -> foldBetweenStatements(block, parent.labelDoStmt, parent.endDoStmt)
                }
                return
            }

            val prev = PsiTreeUtil.getPrevSiblingOfType(block, FortranCompositeElement::class.java) ?: return
            val startFoldableStatementType = foldableConstructStartStatements.find { it.isInstance(prev) } ?: return

            val next = PsiTreeUtil.getNextSiblingOfType(block, FortranStmt::class.java)

            val endFoldableStatementType = foldableConstructEndStatements.find { it.isInstance(next) } ?: return

            if (startFoldableStatementType in foldableConstructEndStatements ||
                    endFoldableStatementType in foldableConstructStartStatements) {
                val endStatement = block.lastChildOfType(FortranCompositeElement::class) ?: return
                val range = TextRange(prev.node.startOffset + prev.textLength, endStatement.node.startOffset + endStatement.textLength)
                createDescriptor(block, range)
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
                    firstNonLabelElement.node.startOffset
                } else {
                    right.node.startOffset
                }
                val range = TextRange(left.node.startOffset + left.textLength, rightOffset)
                createDescriptor(element, range)
            }
        }

        private fun createDescriptor(psiElement: PsiElement, range: TextRange) =
                createDescriptor(psiElement.node, range)

        // Range can have zero length if all folded nodes are foreign leafs.
        // For example, we can try to fold included file content
        private fun createDescriptor(node: ASTNode, range: TextRange) {
            if (range.length > 0) {
                descriptors.add(FoldingDescriptor(node, range))
            }
        }
    }

    override fun isCollapsedByDefault(node: ASTNode) = false

}