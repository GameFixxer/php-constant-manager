package com.github.gamefixxer.phpstormconstantmanagerplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.jetbrains.php.lang.psi.PhpFile
import com.intellij.openapi.diagnostic.thisLogger
import com.github.gamefixxer.phpstormconstantmanagerplugin.visitor.PhpFileVisitor

class ReplaceMagicVariablesAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val logger = thisLogger()
        logger.info("ReplaceMagicVariablesAction triggered")

        val project: Project? = event.project
        if (project == null) {
            logger.warn("Project is null")
            return
        }

        val editor = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)
        if (editor == null) {
            logger.warn("Editor is null")
            return
        }

        val document = editor.document
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        if (virtualFile == null) {
            logger.warn("VirtualFile is null")
            return
        }

        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
        if (psiFile !is PhpFile) {
            logger.warn("PsiFile is not a PhpFile")
            return
        }

        logger.info("Processing PhpFile: ${psiFile.name}")

        val visitor = PhpFileVisitor(project)
        WriteCommandAction.runWriteCommandAction(project) {
            psiFile.accept(visitor)
            logger.info("PhpFileVisitor applied to ${psiFile.name}")
        }
    }
}
