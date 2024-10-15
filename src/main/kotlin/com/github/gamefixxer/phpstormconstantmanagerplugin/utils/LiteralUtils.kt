package com.github.gamefixxer.phpstormconstantmanagerplugin.utils

import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.Constant
import com.jetbrains.php.lang.psi.elements.Field

object LiteralUtils {
    fun isPartOfConstantOrVariable(literal: PsiElement): Boolean = literal.parent is Field || literal.parent is Constant
}
