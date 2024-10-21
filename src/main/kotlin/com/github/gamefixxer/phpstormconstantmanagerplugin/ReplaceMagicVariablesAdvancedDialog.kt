package com.github.gamefixxer.phpstormconstantmanagerplugin

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.table.JBTable
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel

class ReplaceMagicVariablesAdvancedDialog : DialogWrapper(true) {

    init {
        init()
        title = "Replace Magic Variables"
    }

    override fun createCenterPanel(): JComponent? {
        // Replacing JPanel with JBPanel (IntelliJ's version of JPanel)
        val panel = JBPanel<Nothing>()

        // Example: Use a JBTable to list constants
        val columnNames = arrayOf("Value", "Occurrences", "Action")
        val data = arrayOf(
            arrayOf("MAGIC_VALUE_1", "5", "Replace"),
            arrayOf("MAGIC_VALUE_2", "2", "Replace")
        )
        val table = JBTable(DefaultTableModel(data, columnNames))
        panel.add(table)

        // Replacing JLabel with JBLabel (IntelliJ's version of JLabel)
        panel.add(JBLabel("List of constants to be replaced"))

        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(okAction, cancelAction)
    }
}
