package com.kratos.testItactions.testitclient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
public class TestCaseOriginal {
    private String versionId;
    private int medianDuration;
    private boolean isDeleted;
    private String projectId;
    private String entityTypeName;
    private boolean isAutomated;
    private ArrayList<Object> autoTests;
    private ArrayList<Object> attachments;
    private ArrayList<SectionPreconditionStep> sectionPreconditionSteps;
    private ArrayList<SectionPostconditionStep> sectionPostconditionSteps;
    private int versionNumber;
    private ArrayList<Iteration> iterations;
    private String createdDate;
    private String modifiedDate;
    private String createdById;
    private String modifiedById;
    private int globalId;
    private String id;
    private String sectionId;
    private String description;
    private String state;
    private String priority;
    private ArrayList<Step> steps;
    private ArrayList<PreconditionStep> preconditionSteps;
    private ArrayList<PostconditionStep> postconditionSteps;
    private int duration;
    private Map<String, String> attributes;
    private ArrayList<Object> tags;
    private ArrayList<Link> links;
    private String name;

    @Data
    public static class Iteration{
        private String id;
        private ArrayList<Parameter> parameters;
    }

    @Data
    public static class Link{
        private String id;
        private String title;
        private String url;
        private String description;
        private String type;
        private boolean hasInfo;
    }

    @Data
    public static class Parameter{
        private String id;
        private String parameterKeyId;
        private String value;
        private String name;
    }

    @Data
    public static class PostconditionStep{
        private Object workItem;
        private String id;
        private String action;
        private String expected;
        private String testData;
        private String comments;
        private Object workItemId;
    }

    @Data
    public static class PreconditionStep{
        private Object workItem;
        private String id;
        private String action;
        private String expected;
        private String testData;
        private String comments;
        private Object workItemId;
    }

    @Data
    public static class SectionPostconditionStep{
        private Object workItem;
        private String id;
        private String action;
        private String expected;
        private String testData;
        private String comments;
        private Object workItemId;
    }

    @Data
    public static class SectionPreconditionStep{
        private Object workItem;
        private String id;
        private String action;
        private String expected;
        private String testData;
        private String comments;
        private Object workItemId;
    }

    @Data
    public static class Step{
        private WorkItem workItem;
        private String id;
        private String action;
        private String expected;
        private String testData;
        private String comments;
        private String workItemId;
    }

    @Data
    public static class WorkItem{
        private String versionId;
        private int globalId;
        private String name;
        private ArrayList<Step> steps;
        private boolean isDeleted;
    }
}
