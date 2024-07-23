package com.hyeongarl.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class LogoutProducerConfig {
    @Bean
    public ProducerFactory<String, Long> logoutProducer() {
        Map<String, Object> config = CustomKafkaConfig.customProducer();
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean(name = "logoutTemplate")
    public KafkaTemplate<String, Long> loginTemplate() {
        return new KafkaTemplate<>(logoutProducer());
    }
}
