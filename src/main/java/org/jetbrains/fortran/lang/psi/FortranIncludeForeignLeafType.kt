package org.jetbrains.fortran.lang.psi

import com.intellij.lang.ForeignLeafType
import com.intellij.psi.tree.IElementType

class FortranIncludeForeignLeafType(val type: IElementType, val tokenText: CharSequence) : ForeignLeafType(type, tokenText)