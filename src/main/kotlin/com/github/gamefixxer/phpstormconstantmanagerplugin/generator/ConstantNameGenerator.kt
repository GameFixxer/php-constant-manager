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

    fun generateForInteger(contextName: String?, value: Int): String {
        if (contextName == null) return "CONSTANT_$value"
        return contextName.uppercase() + "_" + value
    }
}
