package com.github.gamefixxer.phpstormconstantmanagerplugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project

class ReplaceMagicVariablesGroup : DefaultActionGroup() {

    override fun update(event: AnActionEvent) {
        val project: Project? = event.project
        val editor: Editor? = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)

        // Enable and show the group only if a project and an active editor are available
        event.presentation.isEnabledAndVisible = (project != null && editor != null)
    }
}
