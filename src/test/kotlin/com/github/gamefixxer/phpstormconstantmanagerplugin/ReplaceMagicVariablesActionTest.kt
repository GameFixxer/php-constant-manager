package com.github.gamefixxer.phpstormconstantmanagerplugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.lang.psi.PhpFile
import org.mockito.Mockito.*

class ReplaceMagicVariablesActionTest : BasePlatformTestCase() {

    private val action = ReplaceMagicVariablesAction()

    fun `test action with null project`() {
        val event = mock(AnActionEvent::class.java)
        `when`(event.project).thenReturn(null)

        action.actionPerformed(event)

        verify(event, times(1)).project
        // Add verification for logger.warn if needed
    }

    fun `test action with null editor`() {
        val event = mock(AnActionEvent::class.java)
        val project = mock(Project::class.java)
        `when`(event.project).thenReturn(project)
        `when`(event.getData(CommonDataKeys.EDITOR)).thenReturn(null)

        action.actionPerformed(event)

        verify(event, times(1)).getData(CommonDataKeys.EDITOR)
        // Add verification for logger.warn if needed
    }

    fun `test action with null virtual file`() {
        val event = mock(AnActionEvent::class.java)
        val project = mock(Project::class.java)
        val editor = mock(Editor::class.java)
        `when`(event.project).thenReturn(project)
        `when`(event.getData(CommonDataKeys.EDITOR)).thenReturn(editor)
        `when`(event.getData(CommonDataKeys.VIRTUAL_FILE)).thenReturn(null)

        action.actionPerformed(event)

        verify(event, times(1)).getData(CommonDataKeys.VIRTUAL_FILE)
        // Add verification for logger.warn if needed
    }

    fun `test action with non-php file`() {
        val event = mock(AnActionEvent::class.java)
        val project = mock(Project::class.java)
        val editor = mock(Editor::class.java)
        val virtualFile = mock(VirtualFile::class.java)
        val psiFile = mock(PsiFile::class.java)
        val psiManager = mock(PsiManager::class.java)

        `when`(event.project).thenReturn(project)
        `when`(event.getData(CommonDataKeys.EDITOR)).thenReturn(editor)
        `when`(event.getData(CommonDataKeys.VIRTUAL_FILE)).thenReturn(virtualFile)
        `when`(PsiManager.getInstance(project)).thenReturn(psiManager)
        `when`(psiManager.findFile(virtualFile)).thenReturn(psiFile)

        action.actionPerformed(event)

        verify(psiManager, times(1)).findFile(virtualFile)
        // Add verification for logger.warn if needed
    }

    fun `test action with php file`() {
        val event = mock(AnActionEvent::class.java)
        val project = mock(Project::class.java)
        val editor = mock(Editor::class.java)
        val document = mock(com.intellij.openapi.editor.Document::class.java)
        val virtualFile = mock(VirtualFile::class.java)
        val psiFile = mock(PhpFile::class.java)
        val psiManager = mock(PsiManager::class.java)

        `when`(event.project).thenReturn(project)
        `when`(event.getData(CommonDataKeys.EDITOR)).thenReturn(editor)
        `when`(editor.document).thenReturn(document)
        `when`(event.getData(CommonDataKeys.VIRTUAL_FILE)).thenReturn(virtualFile)
        `when`(PsiManager.getInstance(project)).thenReturn(psiManager)
        `when`(psiManager.findFile(virtualFile)).thenReturn(psiFile)

        doNothing().`when`(psiFile).accept(any(MagicVariableVisitor::class.java))

        action.actionPerformed(event)

        verify(psiManager, times(1)).findFile(virtualFile)
        verify(psiFile, times(1)).accept(any(MagicVariableVisitor::class.java))
    }
}
