package com.kratos.generalactions.methodsactions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.kratos.testItactions.testitclient.model.TestCaseForCreateClass;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для работы с тестов
 */
public class TestMethod {

    /**
     * Создание шаблонных тестовых методов
     *
     * @param psiClass               - класс в котором будет создан тестовый метод
     * @param testCaseForCreateClass модель данных по кейсу из TestIt
     */
    public void createTemplateTestMethod(PsiClass psiClass, TestCaseForCreateClass testCaseForCreateClass) {
        addMethodToClass(psiClass, testCaseForCreateClass, addAnnotationTestMethodTemplate());
    }

    /**
     * Создание тестовых методов из TestIt
     *
     * @param testCaseForCreateClass модель данных по кейсу из TestIt
     */
    public void createTestMethodFromTestIt(AnActionEvent event, TestCaseForCreateClass testCaseForCreateClass) {
        PsiElement psiElement = event.getData(CommonDataKeys.PSI_ELEMENT);
        if (psiElement instanceof PsiClass) {
            PsiClass psiClass = (PsiClass) psiElement;
            addMethodToClass(psiClass, testCaseForCreateClass, addAnnotationTestMethodFromTestIt(testCaseForCreateClass));
        }
    }

    /**
     * Создание тестового метода
     *
     * @param psiClass                класс в котором будет создан тестовый метод
     * @param testCaseForCreateClass  модель данных по кейсу из TestIt
     * @param annotationForTestMethod аннотации для тестового метода
     */
    private void addMethodToClass(PsiClass psiClass, TestCaseForCreateClass testCaseForCreateClass,
                                  List<String> annotationForTestMethod) {
        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
            PsiElementFactory elementFactory = PsiElementFactory.getInstance(psiClass.getProject());
            String methodName = Introspector.decapitalize(psiClass.getName());
            PsiMethod testMethod = elementFactory.createMethod(methodName, PsiType.VOID);
            PsiModifierList modifierList = testMethod.getModifierList();
            annotationForTestMethod.forEach(modifierList::addAnnotation);
            addStepsFromTestItToTestMethod(elementFactory, testMethod, testCaseForCreateClass);
            psiClass.add(testMethod);
        });
    }

    /**
     * Подготовка данных для создания тестовых методов из данных TestIt
     *
     * @param testCaseForCreateClass модель данных по кейсу из TestIt
     */
    private List<String> addAnnotationTestMethodFromTestIt(TestCaseForCreateClass testCaseForCreateClass) {
        String displayName = (testCaseForCreateClass.getTestCaseName() != null) ? testCaseForCreateClass.getTestCaseName() : "...";

        String features = "KP-...";
        if (!testCaseForCreateClass.getTasks().isEmpty()) {
            List<String> feature = new ArrayList<>();
            testCaseForCreateClass.getTasks().forEach(t -> feature.add("@Feature(\"" + t + "\")"));
            features= feature.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
        }

        // "priority": "Highest" - самый высокий  = SeverityLevel.BLOCKER
        // "priority": "High" - высокий = SeverityLevel.CRITICAL
        // "priority": "Medium" - средний = SeverityLevel.NORMAL
        // "priority": "Low" - низкий = SeverityLevel.MINOR
        // "priority": "Lowest" - самый низкий = SeverityLevel.TRIVIAL
        String priority;
        switch (testCaseForCreateClass.getPriority()) {
            case "Highest":
                priority = "SeverityLevel.BLOCKER";
                break;
            case "High":
                priority = "SeverityLevel.CRITICAL";
                break;
            case "Medium":
                priority = "SeverityLevel.NORMAL";
                break;
            case "Low":
                priority = "SeverityLevel.MINOR";
                break;
            case "Lowest":
                priority = "SeverityLevel.TRIVIAL";
                break;
            default:
                priority = "SeverityLevel.NORMAL";
        }

        return List.of(
                "io.qameta.allure.Severity(" + priority + ")",
                "io.qameta.allure.Features({" + features + "})",
                "io.qameta.allure.TmsLink(\"" + testCaseForCreateClass.getGlobalId() + "\")",
                "org.junit.jupiter.api.DisplayName(\"" + displayName + "\")",
                "org.junit.jupiter.api.Test"
        );
    }

    /**
     * Подготовка данных для создания шаблонных тестовых методов
     */
    private List<String> addAnnotationTestMethodTemplate() {
        return List.of(
                "io.qameta.allure.Severity(SeverityLevel.CRITICAL)",
                "io.qameta.allure.Features({@Feature(\"KP-...\")})",
                "io.qameta.allure.TmsLink(\"...\")",
                "org.junit.jupiter.api.DisplayName(\"...\")",
                "org.junit.jupiter.api.Test"
        );
    }

    /**
     * Метод для формирования шагов в самом тесте
     * @param elementFactory PsiElementFactory
     * @param testMethod PsiMethod
     * @param testCaseForCreateClass передается модель с данными полученными из TestIT
     */
    private void addStepsFromTestItToTestMethod(PsiElementFactory elementFactory,
                                                PsiMethod testMethod,
                                                TestCaseForCreateClass testCaseForCreateClass) {
        List<PsiMethodCallExpression> testSteps = new ArrayList<>();
        List<String> steps = testCaseForCreateClass.getTestStep();
        if (!steps.isEmpty()) {
            testCaseForCreateClass.getTestStep().forEach(s -> {
                PsiMethodCallExpression stepMethodCall = (PsiMethodCallExpression) elementFactory
                        .createExpressionFromText("step(\"" + s + "\")", testMethod);
                testSteps.add(stepMethodCall);
            });
        } else {
            PsiMethodCallExpression stepMethodCall = (PsiMethodCallExpression) elementFactory
                    .createExpressionFromText("step(\"...\")", testMethod);
            testSteps.add(stepMethodCall);
        }
        PsiCodeBlock createMethodBody = testMethod.getBody();
        if (createMethodBody != null) {
            testSteps.forEach(addSteps -> {
                createMethodBody.add(elementFactory.createStatementFromText(addSteps.getText() + ";", testMethod));
            });
        }
    }
}
