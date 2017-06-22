package org.jetbrains.fortran

import com.intellij.lang.Commenter

class FortranCommenter : Commenter {
    override fun getCommentedBlockCommentPrefix() = null

    override fun getCommentedBlockCommentSuffix() = null

    override fun getBlockCommentPrefix() = null

    override fun getBlockCommentSuffix() = null

    override fun getLineCommentPrefix() = "!"
}
