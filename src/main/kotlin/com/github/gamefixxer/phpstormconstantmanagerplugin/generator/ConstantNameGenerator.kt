package com.github.gamefixxer.phpstormconstantmanagerplugin.generator

import com.intellij.openapi.diagnostic.thisLogger

object ConstantNameGenerator {

    enum class ContextType {
        DEFAULT, ARRAY_KEY, ARRAY_VALUE, METHOD_PARAMETER
    }

    private val validChars = Regex("[A-Za-z0-9_]")

    private val replacementTable = mapOf(
        "*" to "STAR",
        "&" to "AND",
        "%" to "PERCENT",
        "@" to "AT"
    )

    fun generateConstantNameForStringValues(value: String, contextName: String? = null, paramName: String? = null): String {
        val logger = thisLogger()
        logger.warn("Values:$value-$contextName-$paramName-")
        val formattedValue = value.map { char ->
            if (char.toString().matches(validChars)) {
                char.uppercaseChar()
            } else {
                replacementTable[char.toString()] ?: "_"
            }
        }.joinToString("")

        return paramName?.let {
            "${it.uppercase()}_$formattedValue"
        } ?: contextName?.let {
            "${it.uppercase()}_$formattedValue"
        } ?: formattedValue
    }

    fun generateConstantNameForInteger(
        contextName: String?,
        value: Int,
        contextType: ContextType,
        arrayKeyName: String? = null,
        arrayValueName: String? = null,
        paramName: String? = null
    ): String {
        return when (contextType) {
            ContextType.ARRAY_KEY -> "${contextName?.uppercase() ?: "ARRAY"}_${arrayKeyName?.uppercase() ?: "KEY"}_$value"
            ContextType.ARRAY_VALUE -> "${contextName?.uppercase() ?: "ARRAY"}_${arrayValueName?.uppercase() ?: "VALUE"}_$value"
            ContextType.METHOD_PARAMETER -> "${paramName?.uppercase() ?: "PARAM"}_$value"
            else -> "${contextName?.uppercase() ?: "CONSTANT"}_$value"
        }
    }
    /*
    fun generateForMethodParameter(value: Any, paramName: String): String =
        "${paramName.uppercase()} ${when (value) {
            is String -> generateConstantNameForStringValues(value)
            is Int -> value.toString()
            else -> "UNKNOWN"
        }}"

    fun generateBasedOnContext(value: Any, contextName: String?, paramName: String? = null): String = when (value) {
            is String -> generateConstantNameForStringValues(value, contextName, paramName)
            is Int -> generateConstantNameForInteger(contextName, value, ContextType.DEFAULT, paramName = paramName)
            else -> contextName?.uppercase() ?: "LITERAL"
        }
*/
}
