package com.github.gamefixxer.phpstormconstantmanagerplugin

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.PhpFile
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class MagicVariableInspectionTest {

    @Test
    fun `test buildVisitor with PhpFile element`() {
        // Arrange
        val holder = mock(ProblemsHolder::class.java)
        val phpFile = mock(PhpFile::class.java)
        val project = mock(com.intellij.openapi.project.Project::class.java)

        `when`(holder.project).thenReturn(project)

        val inspection = MagicVariableInspection()
        val visitor = inspection.buildVisitor(holder, false)

        // Act
        visitor.visitElement(phpFile)

        // Assert
        verify(phpFile).accept(any(MagicVariableVisitor::class.java))
    }

    @Test
    fun `test buildVisitor with non PhpFile element`() {
        // Arrange
        val holder = mock(ProblemsHolder::class.java)
        val nonPhpElement = mock(PsiElement::class.java)

        val inspection = MagicVariableInspection()
        val visitor = inspection.buildVisitor(holder, false)

        // Act
        visitor.visitElement(nonPhpElement)

        // Assert
        verify(nonPhpElement, never()).accept(any(MagicVariableVisitor::class.java))
    }
}
