package org.jetbrains.fortran.ide.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns.psiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranBlock
import org.jetbrains.fortran.lang.psi.FortranBlockData


class KeywordCompletionContributor : CompletionContributor() {
    private val typeStatementsBeginning = setOf(
            "byte", "character", "complex", "double", "integer", "logical", "real"
    )

    private val blockDataStatementsBeginning = setOf(
            "common", "data", "dimension", "equivalence", "implicit", "parameter", "record", "save", "structure"
    ) + typeStatementsBeginning

    private val allKeywords = blockDataStatementsBeginning + setOf("abstract", "accept", "all", "allocate",
            "allocatable", "assign",
            "assignment", "associate", "asynchronous", "backspace", "bind", "block", "blockdata",
            "call", "case", "class", "close", "codimension", "concurrent", "contains", "contiguous", "continue",
            "critical", "cycle", "deallocate", "decode", "default", "deferred", "do",
            "doubleprecision", "doublecomplex", "elemental", "else", "elseif", "elsewhere", "encode",
            "end", "endassociate", "endblock", "endblockdata", "endcritical", "enddo", "endenum",
            "endfile", "endforall", "endfunction", "endif", "endinterface", "endmodule", "endprocedure",
            "endprogram", "endselect", "endsubmodule", "endsubroutine", "endtype", "endwhere", "entry",
            "enum", "enumerator", "error", "exit", "extends", "external", "final", "flush",
            "forall", "format", "formatted", "function", "generic", "go", "goto", "if", "images",
            "import", "impure", "in", "include", "inout", "intent", "interface", "intrinsic",
            "inquire", "iolength", "is", "kind", "len", "lock", "memory", "module", "name",
            "namelist", "none", "nonintrinsic", "nonoverridable", "nopass", "nullify", "only", "open",
            "operator", "optional", "out", "pass", "pause", "pointer", "precision", "print",
            "private", "procedure", "program", "protected", "public", "pure", "read", "recursive",
            "result", "return", "rewind", "select", "sequence", "stop", "sync", "syncall",
            "syncimages", "syncmemory", "subroutine", "submodule", "target", "then", "to", "type",
            "unformatted", "unlock", "use", "value", "volatile", "wait", "where", "while", "write", "unit")

    init {
        extend(CompletionType.BASIC, blockDataStatementStart(), KeywordCompletionProvider(blockDataStatementsBeginning))
        extend(CompletionType.BASIC, defaultStatementStart(), KeywordCompletionProvider(allKeywords))
    }

    private fun defaultStatementStart() = statementStart()
            .andNot(psiElement()
            .inside(FortranBlockData::class.java))

    private fun blockDataStatementStart() = statementStart()
            .inside(psiElement(FortranBlock::class.java))
            .inside(psiElement(FortranBlockData::class.java))

    private fun statementStart() = psiElement(FortranTypes.IDENTIFIER).afterLeaf(psiElement(FortranTypes.EOL))
}