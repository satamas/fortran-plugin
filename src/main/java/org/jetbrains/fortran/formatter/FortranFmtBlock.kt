package org.jetbrains.fortran.formatter

import com.intellij.formatting.*
import com.intellij.formatting.alignment.AlignmentStrategy
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.*
import java.util.*

class FortranFmtBlock(
        private val node: ASTNode,
        private val alignment: Alignment?,
        private val indent: Indent,
        private val wrap: Wrap?,
        private val settings: CodeStyleSettings,
        private val spacingBuilder: SpacingBuilder
) : ASTBlock {
    private val blockIsIncomplete: Boolean by lazy { FormatterUtil.isIncomplete(node) }
    private val blockSubBlocks: List<Block> by lazy { buildChildren() }

    override fun getAlignment(): Alignment? = alignment
    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(newChildIndent(newChildIndex), null)
    override fun getIndent(): Indent? = indent
    override fun getNode(): ASTNode = node
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = computeSpacing(child1, child2)
    override fun getSubBlocks(): List<Block> = blockSubBlocks
    override fun getTextRange(): TextRange = node.textRange
    override fun getWrap(): Wrap? = wrap
    override fun isIncomplete(): Boolean = blockIsIncomplete
    override fun isLeaf(): Boolean = node.firstChildNode == null
    override fun toString() = "${node.text} $textRange"

    private fun buildChildren(): List<Block> {
        val blocks = ArrayList<Block>()

        val alignment = getAlignmentStrategy()
        var child: ASTNode? = node.firstChildNode
        while (child != null) {
            val childType = child.elementType

            if (child.textRange.length == 0) {
                child = child.treeNext
                continue
            }

            if (childType === TokenType.WHITE_SPACE || (childType === EOL && !child.text.contains(";"))) {
                child = child.treeNext
                continue
            }

            blocks.add(FortranFmtBlock(child,
                    alignment.getAlignment(child.elementType),
                    computeIndent(child),
                    null,
                    settings,
                    spacingBuilder))
            child = child.treeNext
        }
        return blocks
    }

    fun newChildIndent(childIndex: Int): Indent? = when {
    // inside blocks
        node.psi is FortranExecutableConstruct -> Indent.getNormalIndent()
        node.psi is FortranDeclarationConstruct -> Indent.getNormalIndent()
        node.elementType === BLOCK -> Indent.getNormalIndent()
    // line continuation
        node.psi is FortranExpr || node.psi is FortranStmt -> Indent.getContinuationIndent()

        else -> Indent.getNoneIndent()
    }

    fun oneLineElement(): Boolean {
        val parentPsi = node.psi
        if (parentPsi is FortranFile
                || parentPsi is FortranProgramUnit
                || parentPsi is FortranBlock
                || parentPsi is FortranInternalSubprogramPart
                || parentPsi is FortranDeclarationConstruct
                || parentPsi is FortranExecutableConstruct) {
            return false
        }
        return true
    }
    fun computeIndent(child: ASTNode): Indent {
        val parentType = node.elementType
        val childType = child.elementType
        return when {
            childType === LABEL -> Indent.getLabelIndent()
        // inside blocks
            parentType === BLOCK -> Indent.getNormalIndent()
            parentType !== BLOCK && childType === FortranTokenType.LINE_COMMENT -> Indent.getNormalIndent()
        // Line continuation
            oneLineElement() && (node.firstChildNode !== child) -> Indent.getContinuationIndent()
            else -> Indent.getNoneIndent()
        }
    }

    fun computeSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 is ASTBlock && child2 is ASTBlock) {
            val node1 = child1.node
            val node2 = child2.node
            val psi1 = node1.psi
            val psi2 = node2.psi
            val blankLinesInCode = if (settings.KEEP_BLANK_LINES_IN_CODE > 0) settings.KEEP_BLANK_LINES_IN_CODE - 1 else 0
            if ((psi1 is FortranStmt || psi1 is FortranExecutableConstruct || psi1 is FortranDeclarationConstruct)
                    && (psi2 is FortranStmt || psi2 is FortranExecutableConstruct || psi2 is FortranDeclarationConstruct)
                    || psi1 is FortranBlock || psi2 is FortranBlock) {
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, blankLinesInCode)
            }
        }
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    fun getAlignmentStrategy(): AlignmentStrategy =
            when (node.elementType) {
                else -> AlignmentStrategy.getNullStrategy()
            }

}
