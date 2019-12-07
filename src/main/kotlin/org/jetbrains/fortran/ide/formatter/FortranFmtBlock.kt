package org.jetbrains.fortran.ide.formatter

import com.intellij.formatting.*
import com.intellij.formatting.alignment.AlignmentStrategy
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.FortranTypes.EOL
import org.jetbrains.fortran.lang.psi.*
import java.util.*

class FortranFmtBlock(
        private val node: ASTNode,
        private val alignment: Alignment?,
        private val indent: Indent,
        private val wrap: Wrap?,
        private val settings: CodeStyleSettings,
        private val spacingBuilder: SpacingBuilder,
        private val isFixedForm: Boolean
) : ASTBlock {
    private val blockIsIncomplete: Boolean by lazy { FormatterUtil.isIncomplete(node) }
    private val blockSubBlocks: List<Block> by lazy { buildChildren() }

    override fun getAlignment(): Alignment? = alignment
    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(newChildIndent(), null)
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
        if (isFixedForm) return emptyList()

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
                    spacingBuilder,
                    isFixedForm))
            child = child.treeNext
        }
        return blocks
    }

    private fun newChildIndent(): Indent? = when {
    // inside blocks
        node.psi is FortranProgramUnit -> Indent.getNormalIndent()
        node.psi is FortranExecutableConstruct -> Indent.getNormalIndent()
        node.psi is FortranDeclarationConstruct -> Indent.getNormalIndent()
        node.psi is FortranInternalSubprogramPart -> Indent.getNormalIndent()
        node.psi is FortranModuleSubprogramPart -> Indent.getNormalIndent()
        node.psi is FortranInterfaceSpecification -> Indent.getNormalIndent()
    //    node.psi is FortranBlock -> Indent.getNormalIndent()
    // line continuation
        oneLineElement() -> Indent.getContinuationIndent()

        else -> Indent.getNoneIndent()
    }

    private fun oneLineElement(): Boolean {
        val parentPsi = node.psi
        if (parentPsi is FortranFile
                || parentPsi is FortranProgramUnit
                || parentPsi is FortranBlock
                || parentPsi is FortranModuleSubprogramPart
                || parentPsi is FortranInternalSubprogramPart
                || parentPsi is FortranDeclarationConstruct
                || parentPsi is FortranExecutableConstruct
                || parentPsi is FortranComponentPart
                || parentPsi is FortranInterfaceSpecification) {
            return false
        }
        return true
    }

    private fun computeIndent(child: ASTNode): Indent {

        return when {
            // always not indented
            child.psi is FortranContainsStmt -> return Indent.getNoneIndent()
            child.psi is FortranMacro -> Indent.getAbsoluteNoneIndent()
            // inside blocks
            node.psi is FortranMainProgram && node.psi.firstChild !is FortranStmt -> Indent.getNoneIndent()
            node.psi is FortranProgramUnit && child.psi !is FortranStmt
                    && child.psi !is FortranInternalSubprogramPart
                    && child.psi !is FortranModuleSubprogramPart -> Indent.getNormalIndent()
            node.psi is FortranDerivedTypeDef && child.psi is FortranTypeBoundProcedurePart -> Indent.getNoneIndent()
            node.psi is FortranTypeBoundProcedurePart -> Indent.getNormalIndent()
            node.psi is FortranExecutableConstruct && child.psi !is FortranStmt -> Indent.getNormalIndent()
            node.psi is FortranEnumDef && child.psi is FortranEnumeratorDefStmt -> Indent.getNormalIndent()
            node.psi is FortranEnumDef && child.psi !is FortranEnumeratorDefStmt -> Indent.getNoneIndent()
            node.psi is FortranInterfaceSpecification -> Indent.getNoneIndent()
            node.psi is FortranDeclarationConstruct && child.psi !is FortranStmt -> Indent.getNormalIndent()
            node.psi is FortranInternalSubprogramPart && child.psi !is FortranStmt -> Indent.getNormalIndent()
            node.psi is FortranModuleSubprogramPart && child.psi !is FortranStmt -> Indent.getNormalIndent()
        // Line continuation
            oneLineElement() && (node.firstChildNode !== child) -> Indent.getContinuationIndent()
            else -> Indent.getNoneIndent()
        }
    }

    private fun computeSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 is ASTBlock && child2 is ASTBlock) {
            val fortranCommonSettings = settings.getCommonSettings(FortranLanguage)
            val node1 = child1.node
            val node2 = child2.node
            val psi1 = node1?.psi
            val psi2 = node2?.psi

            // MAYBE WE CAN MAKE IT BEAUTIFUL
            // NEED TO ADD MORE PSI HERE
            if ((node1?.elementType === FortranTokenType.LINE_COMMENT
                    || psi1 is FortranStmt
                    || psi1 is FortranExecutableConstruct
                    || psi1 is FortranDeclarationConstruct
                    || psi1 is FortranBlock)
                    && (psi2 is FortranStmt
                    || psi2 is FortranExecutableConstruct
                    || psi2 is FortranDeclarationConstruct
                    || psi2 is FortranBlock)) {
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, fortranCommonSettings.KEEP_BLANK_LINES_IN_CODE)
            }
            // before comment
            if (node1?.elementType === EOL && node2?.elementType === FortranTokenType.LINE_COMMENT) {
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, fortranCommonSettings.KEEP_BLANK_LINES_IN_CODE)
            }
            // before subprogram part
            if ((psi1 is FortranBlock || psi1 is FortranStmt)
                    && (psi2 is FortranModuleSubprogramPart || psi2 is FortranInternalSubprogramPart || psi2 is FortranInterfaceSpecification))
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, fortranCommonSettings.KEEP_BLANK_LINES_IN_DECLARATIONS)
            // before subprogram
            if ((psi1 is FortranStmt) && psi2 is FortranProgramUnit)
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, fortranCommonSettings.KEEP_BLANK_LINES_IN_DECLARATIONS)
            // between subprograms
            if (psi1 is FortranProgramUnit && psi2 is FortranProgramUnit)
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, fortranCommonSettings.KEEP_BLANK_LINES_IN_DECLARATIONS)
            // between subprogram and comment
            if (psi1 is FortranProgramUnit && node2?.elementType === FortranTokenType.LINE_COMMENT)
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, fortranCommonSettings.KEEP_BLANK_LINES_IN_DECLARATIONS)
            // after subprogram
            if ((psi1 is FortranModuleSubprogramPart || psi1 is FortranInternalSubprogramPart)
                    && (psi2 is FortranStmt || node2?.elementType === FortranTokenType.LINE_COMMENT))
                return Spacing.createSpacing(0, Int.MAX_VALUE, 1, true, fortranCommonSettings.KEEP_BLANK_LINES_IN_DECLARATIONS)
        }
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    private fun getAlignmentStrategy(): AlignmentStrategy = AlignmentStrategy.getNullStrategy()

}
