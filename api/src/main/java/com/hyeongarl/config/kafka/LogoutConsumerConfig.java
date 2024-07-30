package com.hyeongarl.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
public class LogoutConsumerConfig {
    @Bean
    public ConsumerFactory logoutConsumer() {
        Map<String, Object> config = CustomKafkaConfig.customConsumer2();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "logout");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean(name = "logoutListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Long> logoutListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Long> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(logoutConsumer());
        return factory;
    }
}
