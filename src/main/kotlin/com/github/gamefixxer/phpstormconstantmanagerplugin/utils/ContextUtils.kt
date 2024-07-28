package com.github.gamefixxer.phpstormconstantmanagerplugin.utils

import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.*

object ContextUtils {

    fun getContextName(element: PsiElement): String? {
        var contextElement = element.parent
        while (contextElement != null && contextElement !is PhpClass) {
            contextElement = contextElement.parent
        }

        return if (contextElement is PhpClass) {
            contextElement.name
        } else {
            null
        }
    }

    fun isArrayKey(element: PsiElement): Boolean {
        val parent = element.parent
        return parent is ArrayAccessExpression && parent.index == element
    }

    fun isArrayValue(element: PsiElement): Boolean {
        val parent = element.parent
        if (parent is PhpPsiElement) {
            return parent is ArrayHashElement && parent.value == element
        }
        return false
    }

    fun isMethodParameter(element: PsiElement): Boolean {
        val parent = element.parent
        return parent is Parameter && parent.defaultValue == element
    }

    fun getArrayKeyName(element: PsiElement): String? {
        val parent = element.parent
        return if (parent is ArrayAccessExpression && parent.index == element) {
            (parent.firstChild as? PhpNamedElement)?.name
        } else {
            null
        }
    }

    fun getArrayValueName(element: PsiElement): String? {
        val parent = element.parent
        return if (parent is ArrayHashElement && parent.value == element) {
            (parent.key as? PhpNamedElement)?.name
        } else {
            null
        }
    }

    fun getParamName(element: PsiElement): String? {
        val parent = element.parent
        return if (parent is Parameter && parent.defaultValue == element) {
            parent.name
        } else {
            null
        }
    }
}
