package com.github.gamefixxer.phpstormconstantmanagerplugin.processor

import com.intellij.psi.PsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.intellij.openapi.diagnostic.Logger
import com.github.gamefixxer.phpstormconstantmanagerplugin.utils.LiteralUtils
import com.github.gamefixxer.phpstormconstantmanagerplugin.collector.IntegerLiteralCollector

class PhpClassProcessor(private val project: Project, private val log: Logger) : PsiElementVisitor() {

    fun visitPhpFile(phpFile: PhpFile) {
        log.info("Traversing PHP file: ${phpFile.name}")
        phpFile.accept(object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                log.info("Traversing element: ${element.javaClass.simpleName}")
                if (element is PhpClass && !isTestClass(element)) {
                    log.info("Processing PHP class: ${element.name}")
                    processClass(element)
                } else {
                    log.info("Skipping element: ${element.javaClass.simpleName}")
                }
                element.acceptChildren(this)
                super.visitElement(element)
            }
        })
    }

    private fun isTestClass(phpClass: PhpClass): Boolean {
        val result = phpClass.name.endsWith("Test") ?: false
        log.info("Class ${phpClass.name} isTestClass: $result")
        return result
    }

    private fun processClass(phpClass: PhpClass) {
        log.info("Processing class: ${phpClass.name}")

        // Collect and process string literals
        val stringLiterals = PsiTreeUtil.collectElementsOfType(phpClass, StringLiteralExpression::class.java)
            .filter { literal -> !LiteralUtils.isPartOfConstantOrVariable(literal) }

        if (stringLiterals.isNotEmpty()) {
            StringLiteralProcessor(project, log).processLiterals(phpClass, stringLiterals)
        } else {
            log.info("No string literals found in class: ${phpClass.name}")
        }

        // Collect and process integer literals
        val integerLiterals = IntegerLiteralCollector().collectLiterals(phpClass)
        if (integerLiterals.isNotEmpty()) {
            IntegerLiteralProcessor(project, log).processLiterals(phpClass, integerLiterals)
        } else {
            log.info("No integer literals found in class: ${phpClass.name}")
        }
    }
}
