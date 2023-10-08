package com.kratos.generalactions.classactions.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Енам для добавления аннотаций над класом
 */
@Getter
public enum AnnotationsForClass {
    DISPLAY_NAME("DisplayName(\"...\")"),
    TAGS("Tags({@Tag(\"...\")})");

    private final String annotationString;

    AnnotationsForClass(String annotationString) {
        this.annotationString = annotationString;
    }

    /**
     * Получаем лист енамов
     */
    public static List<String> getAnnotationForClass() {
        List<String> annotationStrings = new ArrayList<>();
        Arrays.stream(values()).forEach(value -> annotationStrings.add(value.getAnnotationString()));
        return annotationStrings;
    }
}
