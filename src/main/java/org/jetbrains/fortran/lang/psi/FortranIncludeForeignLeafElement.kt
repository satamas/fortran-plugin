package org.jetbrains.fortran.lang.psi

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.TextRange
import com.intellij.psi.ExternallyAnnotated
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.ForeignLeafPsiElement
import com.intellij.psi.util.PsiTreeUtil

class FortranIncludeForeignLeafElement(type: FortranIncludeForeignLeafType, text: CharSequence) :
        ForeignLeafPsiElement(type, text), ExternallyAnnotated {
    private val startOffsetCache = Key.create<Pair<Long, Int>>("START_OFFSET")

    private fun getCachedStartOffset(timeStamp: Long): Int? {
        val pair = startOffsetCache.get(this)
        return if (pair != null && pair.first == timeStamp) {
            pair.second
        } else null
    }


    override fun getStartOffset(): Int {
        val stamp = containingFile.modificationStamp
        var offset = getCachedStartOffset(stamp)
        if (offset != null) {
            return offset
        }

        val array = arrayListOf(this)
        var cur = PsiTreeUtil.prevLeaf(this)
        while (offset == null && cur != null && cur !is PsiFile) {
            if (cur is ForeignLeafPsiElement) {
                if (cur is FortranIncludeForeignLeafElement) {
                    offset = cur.getCachedStartOffset(stamp)
                    if (offset == null) {
                        array.add(cur)
                    }
                }
            } else if (cur.textRange != null) {
                offset = cur.textRange.endOffset
            }
            cur = PsiTreeUtil.prevLeaf(cur)
        }
        if (offset == null) {
            return 0
        }
        val value = Pair.create<Long, Int>(stamp, offset)
        for (macroForeignLeafElement in array) {
            startOffsetCache.set(macroForeignLeafElement, value)
        }
        return offset
    }

    override fun getTextRange(): TextRange {
        val start = startOffset
        return TextRange(start, start)
    }

    override fun getAnnotationRegion(): TextRange? = null
}