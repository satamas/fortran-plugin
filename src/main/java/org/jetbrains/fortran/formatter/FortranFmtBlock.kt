package org.jetbrains.fortran.formatter

import com.intellij.formatting.*
import com.intellij.formatting.alignment.AlignmentStrategy
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil
import org.jetbrains.fortran.lang.FortranTypes.*
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

            if (childType === TokenType.WHITE_SPACE || childType === EOL) {
                child = child.treeNext
                continue
            }

            blocks.add(FortranFmtBlock( child,
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
        node.elementType == BLOCK -> Indent.getNormalIndent()

        else -> Indent.getNoneIndent()
    }

    fun computeIndent(child: ASTNode): Indent {
        val parentType = node.elementType
        val parentPsi = node.psi
        val childType = child.elementType
        val childPsi = child.psi
        return when {
            parentType == BLOCK -> Indent.getNormalIndent()

            else -> Indent.getNoneIndent()
        }
    }

    fun computeSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    fun getAlignmentStrategy(): AlignmentStrategy =
            when (node.elementType) {
                else -> AlignmentStrategy.getNullStrategy()
            }

}
