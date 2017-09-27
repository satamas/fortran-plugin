package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager
import kotlin.reflect.KClass

fun PsiElement.lastChildOfType(type: KClass<out PsiElement>) = children.findLast { type.isInstance(it) }

fun PsiElement.smartPointer()
        = SmartPointerManager.getInstance(this.project).createSmartPsiElementPointer(this)
