package com.github.gamefixxer.phpstormconstantmanagerplugin

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpPsiElement
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.intellij.openapi.project.Project
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.PhpModifier
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.Constant

class MagicVariableVisitor(private val project: Project) : PsiElementVisitor() {

    private val log = Logger.getInstance(MagicVariableVisitor::class.java)

    override fun visitElement(element: PsiElement) {
        if (element is PhpFile) {
            log.info("Visiting PHP file: ${element.name}")
            visitPhpFile(element)
        } else {
            log.info("Skipping non-PHP file element: ${element.javaClass.simpleName}")
        }
        super.visitElement(element)
    }

    private fun visitPhpFile(phpFile: PhpFile) {
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

    private fun isTestClass(phpClass: PhpClass): Boolean = phpClass.name.endsWith("Test") ?: false

    private fun processClass(phpClass: PhpClass) {
        log.info("Processing class: ${phpClass.name}")

        // Collect and process string literals
        val stringLiterals = PsiTreeUtil.collectElementsOfType(phpClass, StringLiteralExpression::class.java)
            .filter { literal ->
                !isPartOfConstantOrVariable(literal)
            }
        if (stringLiterals.isNotEmpty()) {
            processStringLiterals(phpClass, stringLiterals)
        } else {
            log.info("No string literals found in class: ${phpClass.name}")
        }

        // Collect and process integer literals
        val integerLiterals = collectIntegerLiterals(phpClass)
        if (integerLiterals.isNotEmpty()) {
            processIntegerLiterals(phpClass, integerLiterals)
        } else {
            log.info("No integer literals found in class: ${phpClass.name}")
        }
    }

    private fun collectIntegerLiterals(phpClass: PhpClass): List<PsiElement>  = PsiTreeUtil.collectElements(phpClass) {
        it is PhpPsiElement && it.text.matches("^\\d+$".toRegex())
    }.filter { literal -> !isPartOfConstantOrVariable(literal) }

    private fun processStringLiterals(phpClass: PhpClass, stringLiterals: List<StringLiteralExpression>) {
        val constantNames = mutableSetOf<String>()
        val replacements = mutableMapOf<PsiElement, String>()

        stringLiterals.forEach { literal ->
            val value = literal.contents
            val constantName = generateConstantName(value)
            constantNames.add(constantName)
            replacements[literal] = constantName
            log.info("Found string literal: \"$value\", generated constant name: $constantName")
        }

        // Find the opening brace {
        val lBrace = phpClass.node.getChildren(null).find { it.text == "{" }?.psi

        // Generate and insert constants
        WriteCommandAction.runWriteCommandAction(project) {
            val anchor = lBrace?.nextSibling

            constantNames.forEach { constantName ->
                val constantValue = replacements.entries.find { it.value == constantName }?.key?.text?.replace("\"", "\\\"") ?: ""
                val constantDeclaration = PhpPsiElementFactory.createClassConstant(
                    project,
                    PhpModifier.instance(
                        PhpModifier.Access.PRIVATE,
                        PhpModifier.Abstractness.IMPLEMENTED,
                        PhpModifier.State.DYNAMIC,
                    ),
                    constantName,
                    constantValue
                )
                if (anchor != null) {
                    phpClass.addAfter(constantDeclaration, lBrace)
                } else {
                    phpClass.add(constantDeclaration)
                }
                log.info("Added constant declaration: private const $constantName = \"$constantValue\"; to class ${phpClass.name}")
            }

            // Replace magic variables with constants
            replacements.forEach { (element, constantName) ->
                val constantReference = PhpPsiElementFactory.createClassConstantReferenceUsingSelf(project, constantName)
                element.replace(constantReference)
                log.info("Replaced magic variable \"$element\" with constant reference: self::$constantName in class ${phpClass.name}")
            }
        }

        log.info("Finished processing string literals in class: ${phpClass.name}")
    }

    private fun processIntegerLiterals(phpClass: PhpClass, integerLiterals: List<PsiElement>) {
        val constantNames = mutableSetOf<String>()
        val replacements = mutableMapOf<PsiElement, String>()

        integerLiterals.forEach { literal ->
            val value = literal.text.toInt()
            val contextName = getContextName(literal)
            val constantName = generateConstantNameForInteger(contextName, value)
            constantNames.add(constantName)
            replacements[literal] = constantName
            log.info("Found integer literal: $value, generated constant name: $constantName")
        }

        // Find the opening brace {
        val lBrace = phpClass.node.getChildren(null).find { it.text == "{" }?.psi

        // Generate and insert constants
        WriteCommandAction.runWriteCommandAction(project) {
            val anchor = lBrace?.nextSibling

            constantNames.forEach { constantName ->
                val constantValue = replacements.entries.find { it.value == constantName }?.key?.text ?: ""
                val constantDeclaration = PhpPsiElementFactory.createClassConstant(
                    project,
                    PhpModifier.instance(
                        PhpModifier.Access.PRIVATE,
                        PhpModifier.Abstractness.IMPLEMENTED,
                        PhpModifier.State.DYNAMIC,
                    ),
                    constantName,
                    constantValue
                )
                if (anchor != null) {
                    phpClass.addAfter(constantDeclaration, lBrace)
                } else {
                    phpClass.add(constantDeclaration)
                }
                log.info("Added constant declaration: private const $constantName = $constantValue; to class ${phpClass.name}")
            }

            // Replace magic integers with constants
            replacements.forEach { (element, constantName) ->
                val constantReference = PhpPsiElementFactory.createClassConstantReferenceUsingSelf(project, constantName)
                element.replace(constantReference)
                log.info("Replaced magic integer $element with constant reference: self::$constantName in class ${phpClass.name}")
            }
        }

        log.info("Finished processing integer literals in class: ${phpClass.name}")
    }

    private fun generateConstantName(value: String): String {
        val validChars = Regex("[A-Za-z0-9_]")
        val replacementTable = mapOf("*" to "STAR")

        return value.map { char ->
            if (char.toString().matches(validChars)) {
                char.uppercaseChar()
            } else {
                replacementTable[char.toString()] ?: "_"
            }
        }.joinToString("")
    }

    private fun generateConstantNameForInteger(contextName: String?, value: Int): String {
        if (contextName == null) return "CONSTANT_$value"
        return contextName.uppercase() + "_" + value
    }

    private fun getContextName(element: PsiElement): String? {
        var contextElement = element.parent
        while (contextElement != null && contextElement !is PhpClass) {
            contextElement = contextElement.parent
        }

        if (contextElement is PhpClass) {
            return contextElement.name
        }
        return null
    }

    private fun isPartOfConstantOrVariable(literal: PsiElement): Boolean {
        val parent = literal.parent
        return parent is Field || parent is Constant
    }
}
