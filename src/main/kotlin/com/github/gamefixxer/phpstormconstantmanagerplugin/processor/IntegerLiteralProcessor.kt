package com.github.gamefixxer.phpstormconstantmanagerplugin.processor

import com.github.gamefixxer.phpstormconstantmanagerplugin.generator.ConstantNameGenerator
import com.github.gamefixxer.phpstormconstantmanagerplugin.utils.ContextUtils
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.intellij.openapi.project.Project
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.PhpModifier

class IntegerLiteralProcessor(private val project: Project, private val log: Logger) {

    fun processLiterals(phpClass: PhpClass, integerLiterals: List<PsiElement>) {
        val (constantNames, replacements) = generateIntegerConstantsAndReplacements(integerLiterals)
        val lBrace = phpClass.node.getChildren(null).find { it.text == "{" }?.psi

        WriteCommandAction.runWriteCommandAction(project) {
            val anchor = lBrace?.nextSibling

            addConstantsToClass(phpClass, constantNames, replacements, anchor)

            log.info("Finished processing integer literals in class: ${phpClass.name}")
        }
    }

    private fun generateIntegerConstantsAndReplacements(integerLiterals: List<PsiElement>): Pair<Set<String>, Map<PsiElement, String>> {
        return integerLiterals.fold(Pair(emptySet(), emptyMap())) { acc, literal ->
            val value = literal.text.toInt()
            val contextType = determineContextType(literal)
            log.info("Logging constant type: $contextType")

            val constantName = ConstantNameGenerator.generateConstantNameForInteger(
                ContextUtils.getContextName(literal),
                value,
                contextType,
                ContextUtils.getArrayKeyName(literal),
                ContextUtils.getArrayValueName(literal),
                ContextUtils.getParamName(literal)
            )

            log.info("Found integer literal: $value, generated constant name: $constantName")

            Pair(
                acc.first + constantName,
                acc.second + (literal to constantName)
            )
        }
    }

    private fun addConstantsToClass(
        phpClass: PhpClass,
        constantNames: Set<String>,
        replacements: Map<PsiElement, String>,
        anchor: PsiElement?
    ) {
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
                phpClass.addAfter(constantDeclaration, anchor)
            } else {
                phpClass.add(constantDeclaration)
            }
            log.info("Added constant declaration: private const $constantName = $constantValue; to class ${phpClass.name}")
        }

        replacements.forEach { (element, constantName) ->
            val constantReference = PhpPsiElementFactory.createClassConstantReferenceUsingSelf(project, constantName)
            element.replace(constantReference)
            log.info("Replaced magic integer $element with constant reference: self::$constantName in class ${phpClass.name}")
        }
    }

    private fun determineContextType(literal: PsiElement): ConstantNameGenerator.ContextType = when {
        ContextUtils.isArrayKey(literal) -> ConstantNameGenerator.ContextType.ARRAY_KEY
        ContextUtils.isArrayValue(literal) -> ConstantNameGenerator.ContextType.ARRAY_VALUE
        ContextUtils.isMethodParameter(literal) -> ConstantNameGenerator.ContextType.METHOD_PARAMETER
        else -> ConstantNameGenerator.ContextType.DEFAULT
    }
}
