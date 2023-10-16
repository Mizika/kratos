package com.kratos.testItactions.testitclient;


import com.kratos.testItactions.testitclient.model.TestCaseOriginal;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * Клиент с API методами TestIT
 */
public interface TestItClient {

    @RequestLine("GET")
    @Headers("Authorization: PrivateToken {token}")
    TestCaseOriginal getTestCase(@Param("token") String token);
}
