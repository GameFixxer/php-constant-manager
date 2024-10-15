package com.github.gamefixxer.phpstormconstantmanagerplugin.collector

import com.github.gamefixxer.phpstormconstantmanagerplugin.utils.LiteralUtils
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpPsiElement

class IntegerLiteralCollector {

    fun collectLiterals(phpClass: PhpClass): List<PsiElement> = PsiTreeUtil.collectElements(phpClass) { it is PhpPsiElement && it.text.matches("^\\d+$".toRegex()) }
            .filter { literal -> !LiteralUtils.isPartOfConstantOrVariable(literal) }
}
