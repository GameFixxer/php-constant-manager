package com.github.gamefixxer.phpstormconstantmanagerplugin.visitor

import com.github.gamefixxer.phpstormconstantmanagerplugin.processor.PhpClassProcessor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.PhpFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.diagnostic.Logger

class PhpFileVisitor(private val project: Project) : PsiElementVisitor() {

    private val log = Logger.getInstance(PhpFileVisitor::class.java)

    override fun visitElement(element: PsiElement) {
        if (element is PhpFile) {
            log.info("Visiting PHP file: ${element.name}")
            PhpClassProcessor(project, log).visitPhpFile(element)
        } else {
            log.info("Skipping non-PHP file element: ${element.javaClass.simpleName}")
        }
        super.visitElement(element)
    }
}
