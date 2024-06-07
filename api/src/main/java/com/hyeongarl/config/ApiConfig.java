package com.hyeongarl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hyeongarl.service.TokenService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    
    @Bean   // Filter 설정
    public FilterRegistrationBean<ApiFilter> filterRegistrationBean(TokenService tokenService) {
        FilterRegistrationBean<ApiFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ApiFilter(tokenService));
        filterRegistrationBean.addUrlPatterns("/url");
        filterRegistrationBean.addUrlPatterns("/category");
        filterRegistrationBean.setOrder(Integer.MIN_VALUE); // 적용 순서
        filterRegistrationBean.setName("ApiFilter");

        return filterRegistrationBean;
    }
}
