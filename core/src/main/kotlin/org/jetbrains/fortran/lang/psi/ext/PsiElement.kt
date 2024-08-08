package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.util.PsiTreeUtil
import kotlin.reflect.KClass

fun PsiElement.lastChildOfType(type: KClass<out PsiElement>) = children.findLast { type.isInstance(it) }

fun PsiElement.smartPointer()
        = SmartPointerManager.getInstance(this.project).createSmartPsiElementPointer(this)

inline fun <reified T : PsiElement> PsiElement.descendantOfTypeStrict(): T? =
        PsiTreeUtil.findChildOfType(this, T::class.java, /* strict */ true)
