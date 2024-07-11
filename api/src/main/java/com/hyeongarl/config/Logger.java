package com.hyeongarl.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {
    public static void logging(String message) {
        log.info("=============== {} ===============", message);
    }
    public static void servicelogging(String message) {
        log.info("----- {} -----", message);
    }
}
