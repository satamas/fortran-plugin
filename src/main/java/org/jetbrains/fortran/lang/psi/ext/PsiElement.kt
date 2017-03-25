package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

inline fun <reified T : PsiElement> PsiElement.childOfType(strict: Boolean = true): T? =
    PsiTreeUtil.findChildOfType(this, T::class.java, strict)