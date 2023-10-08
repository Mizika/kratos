package com.kratos.templateactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.kratos.generalactions.classactions.ActionWithClass;
import com.kratos.templateactions.actions.ActionWithMethod;

public class TemplateTest extends AnAction {

    ActionWithClass actionWithClass = new ActionWithClass();
    ActionWithMethod actionWithMethod = new ActionWithMethod();

    @Override
    public void actionPerformed(AnActionEvent event) {
        PsiElement psiElement = event.getData(CommonDataKeys.PSI_ELEMENT);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (psiElement instanceof PsiClass) {
            PsiClass psiClass = (PsiClass) psiElement;
            actionWithClass.addAnnotationAboveClass(psiClass);
            actionWithClass.addImportToClass(psiFile);
            actionWithClass.addExtendClass(psiClass);
            actionWithMethod.addSetupMethod(psiClass);
            actionWithMethod.addTearDownMethod(psiClass);
            actionWithMethod.addTestMethod(psiClass);
        }
    }
}
