package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement
import kotlin.reflect.KClass

fun PsiElement.lastChildOfType(type: KClass<out PsiElement>) = children.findLast { type.isInstance(it) }