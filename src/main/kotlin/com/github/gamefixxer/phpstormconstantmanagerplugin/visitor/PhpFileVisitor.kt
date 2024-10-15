package com.github.gamefixxer.phpstormconstantmanagerplugin.visitor

import com.github.gamefixxer.phpstormconstantmanagerplugin.processor.PhpClassProcessor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.PhpFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.diagnostic.Logger

class PhpFileVisitor(private val project: Project) : PsiElementVisitor() {

    private val log = Logger.getInstance(PhpFileVisitor::class.java)

    override fun visitElement(element: PsiElement): Unit =
        (element as? PhpFile)?.let {
            log.info("Visiting PHP file: ${it.name}")
            PhpClassProcessor(project, log).visitPhpFile(it)
        } ?: super.visitElement(element)
}
