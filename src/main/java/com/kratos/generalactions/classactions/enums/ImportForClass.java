package com.kratos.generalactions.classactions.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Енам для добавления импортов в класс с тестом
 */
@Getter
public enum ImportForClass {

    JUNIT_TAG("org.junit.jupiter.api.Tag"),
    JUNIT_DISPLAY_NAME("org.junit.jupiter.api.DisplayName"),
    JUNIT_TAGS("org.junit.jupiter.api.Tags"),
    RUSSIANPOST_TEST_BASE("ru.russianpost.utils.TestBase"),
    JUNIT_TEST("org.junit.jupiter.api.Test"),
    ALLURE_TMS_LINK("io.qameta.allure.TmsLink"),
    ALLURE_FEATURE("io.qameta.allure.Feature"),
    ALLURE_SEVERITY("io.qameta.allure.Severity"),
    ALLURE_SEVERITY_LEVEL("io.qameta.allure.SeverityLevel"),
    ALLURE_FEATURES("io.qameta.allure.Features"),
    JUNIT_AFTER_EACH("org.junit.jupiter.api.AfterEach"),
    JUNIT_BEFORE_EACH("org.junit.jupiter.api.BeforeEach");

    private final String annotationName;

    ImportForClass(String annotationName) {
        this.annotationName = annotationName;
    }

    public static List<String> getImportList() {
        List<String> annotationNames = new ArrayList<>();
        Arrays.stream(values()).forEach(value -> annotationNames.add(value.getAnnotationName()));
        return annotationNames;
    }
}
