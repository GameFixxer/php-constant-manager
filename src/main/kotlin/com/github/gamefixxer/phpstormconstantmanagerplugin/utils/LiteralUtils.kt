package com.github.gamefixxer.phpstormconstantmanagerplugin.utils

import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.Constant
import com.jetbrains.php.lang.psi.elements.Field

object LiteralUtils {

    fun isPartOfConstantOrVariable(literal: PsiElement): Boolean {
        val parent = literal.parent
        return parent is Field || parent is Constant
    }
}
