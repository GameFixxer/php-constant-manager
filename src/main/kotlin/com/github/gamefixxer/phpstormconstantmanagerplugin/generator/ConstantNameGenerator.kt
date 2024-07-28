package com.github.gamefixxer.phpstormconstantmanagerplugin.generator

object ConstantNameGenerator {

    fun generate(value: String): String {
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

    fun generateForInteger(contextName: String?, value: Int, contextType: ContextType, arrayKeyName: String? = null, arrayValueName: String? = null, paramName: String? = null): String {
        return when (contextType) {
            ContextType.ARRAY_KEY -> "${contextName?.uppercase() ?: "ARRAY"}_${arrayKeyName?.uppercase() ?: "KEY"}_$value"
            ContextType.ARRAY_VALUE -> "${contextName?.uppercase() ?: "ARRAY"}_${arrayValueName?.uppercase() ?: "KEY"}_$value"
            ContextType.METHOD_PARAMETER -> "${paramName?.uppercase() ?: "PARAM"}_$value"
            else -> "${contextName?.uppercase() ?: "CONSTANT"}_$value"
        }
    }

    enum class ContextType {
        DEFAULT, ARRAY_KEY, ARRAY_VALUE, METHOD_PARAMETER
    }
}
