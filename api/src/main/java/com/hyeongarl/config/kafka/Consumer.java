package com.hyeongarl.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
@EnableKafka
public class Consumer {

    @Primary
    @Bean
    public ConsumerFactory<String, Object> urlConsumer() {
        Map<String, Object> configProps = CustomKafkaConfig.customConsumer();
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "url");
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Primary
    @Bean(name = "urlListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> urlListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(urlConsumer());
        return factory;
    }
}