package com.kratos.testItactions.testitclient;

import com.kratos.testItactions.testitclient.model.TestCaseForCreateClass;
import com.kratos.testItactions.testitclient.model.TestCaseOriginal;
import com.kratos.testItactions.testitclient.utils.CustomFeignLogger;
import com.kratos.testItactions.testitclient.utils.SSLSocketClient;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestItClientImpl {

    /**
     * Метод для отключения SSL
     */
    private Client getClient() {
        return new Client.Default(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getHostnameVerifier());
    }

    /**
     * Общий метод для работы с данными из TestIT
     * @param token токен пользователя TestIT
     * @param url ссылка на кейс из TestIT, формата `https://testit.tools.russianpost.ru/api/v2/WorkItems/152587`
     * @return модель для хранения необходимых данных TestCaseForCreateClass
     */
    public TestCaseForCreateClass getTestCase(String token, String url) {
        //Создание клиента Feign -> TestItClient
        TestItClient testIt = Feign.builder()
                .decoder(new GsonDecoder())
                .client(getClient())
                .logLevel(Logger.Level.FULL)
                .logger(new CustomFeignLogger())
                .target(TestItClient.class, url);

        TestCaseOriginal testCase = testIt.getTestCase(token);
        TestCaseForCreateClass testCaseToCreate = new TestCaseForCreateClass();
        List<String> testSteps = getTestSteps(testCase);
        List<String> preconditionSteps = getPreconditionSteps(testCase);
        List<String> postconditionSteps = getPostconditionSteps(testCase);
        List<String> tasks = getTasks(testCase);
        testCaseToCreate.setTestCaseName(testCase.getName());
        testCaseToCreate.setTestStep(testSteps);
        testCaseToCreate.setPreconditionStep(preconditionSteps);
        testCaseToCreate.setPostconditionStep(postconditionSteps);
        testCaseToCreate.setPriority(testCase.getPriority());
        testCaseToCreate.setGlobalId(testCase.getGlobalId());
        testCaseToCreate.setTasks(tasks);
        return testCaseToCreate;
    }

    /**
     * Метод для парсинга шагов из теста
     */
    private List<String> getTestSteps(TestCaseOriginal testCase) {
        List<String> steps = new ArrayList<>();
        testCase.getSteps().forEach(basicSteps -> {
            if (basicSteps.getWorkItem() != null) {
                if (!basicSteps.getWorkItem().getSteps().isEmpty()) {
                    basicSteps.getWorkItem().getSteps().forEach(ns -> {
                        if (extractStrongPart(ns.getAction()) != null) {
                            steps.add(/*basicSteps.getWorkItem().getName() +": "+*/extractStrongPart(ns.getAction()));
                        }
                    });
                }
            }else {
                if (extractStrongPart(basicSteps.getAction()) != null) {
                    steps.add(extractStrongPart(basicSteps.getAction()));
                }
            }
        });
        return steps;
    }


    /**
     * Метод для парсинга шагов из прекондишена
     */
    private List<String> getPreconditionSteps(TestCaseOriginal testCase) {
        List<String> preconditionSteps = new ArrayList<>();
        if (!testCase.getSectionPreconditionSteps().isEmpty()){
            testCase.getSectionPreconditionSteps().forEach(sp -> {
                if (!sp.getAction().isEmpty()){
                    if (extractStrongPart(sp.getAction()) != null) {
                        preconditionSteps.add(extractStrongPart(sp.getAction()));
                    }
                }
            });
        }
        testCase.getPreconditionSteps().forEach(s -> {
            if (extractStrongPart(s.getAction()) != null) {
                preconditionSteps.add(extractStrongPart(s.getAction()));
            }
        });
        return preconditionSteps;
    }

    /**
     * Метод для парсинга шагов из посткондишена
     */
    private List<String> getPostconditionSteps(TestCaseOriginal testCase) {
        List<String> postconditionSteps = new ArrayList<>();
        if (!testCase.getSectionPostconditionSteps().isEmpty()){
            testCase.getSectionPostconditionSteps().forEach(sp -> {
                if (!sp.getAction().isEmpty()){
                    if (extractStrongPart(sp.getAction()) != null) {
                        postconditionSteps.add(extractStrongPart(sp.getAction()));
                    }
                }
            });
        }
        testCase.getPostconditionSteps().forEach(s -> {
            if (extractStrongPart(s.getAction()) != null) {
                postconditionSteps.add(extractStrongPart(s.getAction()));
            }
        });
        return postconditionSteps;
    }

    /**
     * Метод для получения тасок для аннотации @Feature("...")
     */
    private List<String> getTasks(TestCaseOriginal testCase) {
        return testCase.getLinks().stream()
                .filter(t -> t.getType().equals("Issue"))
                .map(TestCaseOriginal.Link::getTitle)
                .collect(Collectors.toList());
    }

    /**
     * Метод для поиска строки жирным шрифтом из полученных данных TestIT
     */
    private String extractStrongPart(String input) {
        String strongText = null;
        String pattern = "<strong[^>]*>(.*?)</strong>";

        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(input);

        if (matcher.find()) {
            strongText = matcher.group(1)
                    .replace("\"", "\\\"");
        }

        return strongText;
    }
}
