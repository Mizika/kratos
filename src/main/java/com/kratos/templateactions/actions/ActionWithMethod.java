package com.kratos.templateactions.actions;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;

import java.beans.Introspector;
import java.util.List;

public class ActionWithMethod {

    public void addTestMethod(PsiClass psiClass) {
        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
            PsiElementFactory elementFactory = PsiElementFactory.getInstance(psiClass.getProject());
            String methodName = Introspector.decapitalize(psiClass.getName());
            PsiMethod testMethod = elementFactory.createMethod(methodName, PsiType.VOID);
            PsiModifierList modifierList = testMethod.getModifierList();
            List<String> annotationForTestMethod = List.of(
                    "io.qameta.allure.Severity(SeverityLevel.CRITICAL)",
                    "io.qameta.allure.Features({@Feature(\"KP-...\")})",
                    "io.qameta.allure.TmsLink(\"...\")",
                    "org.junit.jupiter.api.DisplayName(\"...\")",
                    "org.junit.jupiter.api.Test"
            );
            annotationForTestMethod.forEach(modifierList::addAnnotation);
            psiClass.add(testMethod);
        });
    }

    public void addSetupMethod(PsiClass psiClass) {
        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
            PsiElementFactory elementFactory = PsiElementFactory.getInstance(psiClass.getProject());
            String methodName = "setup";
            PsiMethod setupMethod = elementFactory.createMethod(methodName, PsiType.VOID);
            PsiModifierList modifierList = setupMethod.getModifierList();
            List<String> annotationSetupMethod = List.of(
                    "BeforeEach"
            );
            annotationSetupMethod.forEach(modifierList::addAnnotation);
            psiClass.add(setupMethod);
        });
    }

    public void addTearDownMethod(PsiClass psiClass) {
        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
            PsiElementFactory elementFactory = PsiElementFactory.getInstance(psiClass.getProject());
            String methodName = "tearDown";
            PsiMethod tearDownMethod = elementFactory.createMethod(methodName, PsiType.VOID);
            PsiModifierList modifierList = tearDownMethod.getModifierList();
            List<String> annotationTearDownMethod = List.of(
                    "AfterEach"
            );
            annotationTearDownMethod.forEach(modifierList::addAnnotation);
            psiClass.add(tearDownMethod);
        });
    }
}
