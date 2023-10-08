package com.kratos.generalactions.methodsactions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.kratos.testItactions.testitclient.model.TestCaseForCreateClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с методом прекондишн
 */
public class SetupMethod {

    /**
     * Метод для добавления прекондишена
     * @param event AnActionEvent
     * @param testCaseForCreateClass передается модель с данными полученными из TestIT
     */
    public void addSetupMethod(AnActionEvent event, TestCaseForCreateClass testCaseForCreateClass) {
        PsiElement psiElement = event.getData(CommonDataKeys.PSI_ELEMENT);
        if (psiElement instanceof PsiClass) {
            PsiClass psiClass = (PsiClass) psiElement;
            if (!testCaseForCreateClass.getPreconditionStep().isEmpty()) {
                WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
                    PsiElementFactory elementFactory = PsiElementFactory.getInstance(psiClass.getProject());
                    String methodName = "setup";
                    PsiMethod setupMethod = elementFactory.createMethod(methodName, PsiType.VOID);
                    PsiModifierList modifierList = setupMethod.getModifierList();
                    modifierList.addAnnotation("BeforeEach");
                    addStepsFromTestItToSetupMethod(elementFactory, setupMethod, testCaseForCreateClass);
                    psiClass.add(setupMethod);
                });
            }
        }
    }

    /**
     * Метод для формирования шагов в прекондишене
     * @param elementFactory PsiElementFactory
     * @param testMethod PsiMethod
     * @param testCaseForCreateClass передается модель с данными полученными из TestIT
     */
    private void addStepsFromTestItToSetupMethod(PsiElementFactory elementFactory,
                                                 PsiMethod testMethod,
                                                 TestCaseForCreateClass testCaseForCreateClass) {
        List<PsiMethodCallExpression> testSteps = new ArrayList<>();
        testCaseForCreateClass.getPreconditionStep().forEach(s -> {
            PsiMethodCallExpression stepMethodCall = (PsiMethodCallExpression) elementFactory
                    .createExpressionFromText("step(\"" + s + "\")", testMethod);
            testSteps.add(stepMethodCall);
        });
        PsiCodeBlock createMethodBody = testMethod.getBody();
        if (createMethodBody != null) {
            testSteps.forEach(addSteps -> createMethodBody.add(elementFactory.createStatementFromText(addSteps.getText() + ";", testMethod)));
        }
    }
}
