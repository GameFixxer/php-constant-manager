package com.github.gamefixxer.phpstormconstantmanagerplugin

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.PhpFile

class MagicVariableInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: com.intellij.psi.PsiElement) {
                if (element is PhpFile) {
                    element.accept(MagicVariableVisitor(holder.project))
                }
            }
        }
    }
}
