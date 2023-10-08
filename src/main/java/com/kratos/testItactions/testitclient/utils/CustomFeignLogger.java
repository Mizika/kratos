package com.kratos.testItactions.testitclient.utils;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomFeignLogger extends Logger {

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info(String.format(methodTag(configKey) + format, args));
    }
}
