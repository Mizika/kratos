package com.kratos.generalactions.classactions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.kratos.generalactions.classactions.enums.AnnotationsForClass;
import com.kratos.generalactions.classactions.enums.ImportForClass;

import java.util.List;

/**
 * Класс для действий над классами
 * Добавление аннотаций над классом
 * Добавление импортов в класс
 * Обозначение класса родителя
 */
public class ActionWithClass {

    /**
     * Метод для общего вызова всех реализованных методов
     */
    public void actionWithClass(AnActionEvent event) {
        PsiElement psiElement = event.getData(CommonDataKeys.PSI_ELEMENT);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (psiElement instanceof PsiClass) {
            PsiClass psiClass = (PsiClass) psiElement;
            addAnnotationAboveClass(psiClass);
            addImportToClass(psiFile);
            addExtendClass(psiClass);
        }
    }

    /**
     * Метод для добавления аннотаций над классом
     * @param psiClass
     */
    public void addAnnotationAboveClass(PsiClass psiClass) {
        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), "Add Annotation", "Add Annotation", () -> {
            List<String> annotationForClass = AnnotationsForClass.getAnnotationForClass();
            annotationForClass.forEach(ac -> psiClass.getModifierList().addAnnotation(ac));
        });
    }

    /**
     * Метод для добавления ипортов в класс
     * @param psiFile
     */
    public void addImportToClass(PsiFile psiFile) {
        if (!(psiFile instanceof PsiJavaFile)) {
            return;
        }
        final PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        final PsiImportList importList = javaFile.getImportList();
        WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
            PsiElementFactory elementFactory = PsiElementFactory.getInstance(psiFile.getProject());
            List<String> importNames = ImportForClass.getImportList();
            importNames.forEach(i -> {
                PsiClass psiClass = elementFactory.createTypeByFQClassName(i).resolve();
                PsiImportStatement psiImportStatement = elementFactory.createImportStatement(psiClass);
                importList.add(psiImportStatement);
            });
        });
    }

    /**
     * Метод для добавления класса родителя (extends TestBase)
     * @param psiClass
     */
    public void addExtendClass(PsiClass psiClass) {
        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
            PsiElementFactory elementFactory = PsiElementFactory.getInstance(psiClass.getProject());
            PsiJavaCodeReferenceElement superClassReference = elementFactory.createReferenceFromText("TestBase", null);
            PsiReferenceList extendsList = psiClass.getExtendsList();
            if (extendsList != null) {
                extendsList.add(superClassReference);
            }
        });
    }

    /**
     * Метод для оптимизации импортов в классе
     * @param event
     */
    public void optimizeImport(AnActionEvent event){
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (!(psiFile instanceof PsiJavaFile)) {
            return;
        }
        WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
            JavaCodeStyleManager codeStyleManager = JavaCodeStyleManager.getInstance(psiFile.getProject());
            codeStyleManager.optimizeImports(psiFile);
        });

    }
}
