package com.github.gamefixxer.phpstormconstantmanagerplugin.utils

import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass

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
}
