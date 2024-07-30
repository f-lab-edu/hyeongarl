package com.hyeongarl.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
public class LoginConsumerConfig {

    @Primary
    @Bean
    public ConsumerFactory loginConsumer() {
        Map<String, Object> config = CustomKafkaConfig.customConsumer2();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "login");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Primary
    @Bean(name = "loginListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Long> loginListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Long> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(loginConsumer());
        return factory;
    }
}
