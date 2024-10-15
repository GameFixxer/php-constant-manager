package com.github.gamefixxer.phpstormconstantmanagerplugin.processor

import com.github.gamefixxer.phpstormconstantmanagerplugin.generator.ConstantNameGenerator
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.intellij.openapi.project.Project
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.PhpModifier

class StringLiteralProcessor(private val project: Project, private val log: Logger) {

    fun processLiterals(phpClass: PhpClass, stringLiterals: List<StringLiteralExpression>) {
        val constantNames = mutableSetOf<String>()
        val replacements = mutableMapOf<PsiElement, String>()

        stringLiterals.forEach { literal ->
            val value = literal.contents
            val constantName = ConstantNameGenerator.generateConstantNameForStringValues(value)
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
}
