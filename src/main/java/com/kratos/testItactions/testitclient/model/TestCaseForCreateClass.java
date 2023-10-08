package com.kratos.testItactions.testitclient.model;

import lombok.Data;

import java.util.List;

@Data
public class TestCaseForCreateClass {
    private String testCaseName;
    private List<String> testStep;
    private List<String> preconditionStep;
    private List<String> postconditionStep;
    private String priority;
    private Integer globalId;
    private List<String> tasks;
}
