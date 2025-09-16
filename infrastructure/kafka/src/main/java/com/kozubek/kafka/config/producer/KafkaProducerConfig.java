package com.kozubek.kafka.config.producer;

import com.kozubek.kafka.config.KafkaConfigProperties;
import com.kozubek.kafka.config.serialization.MessageKafkaDto;
import com.kozubek.kafka.config.serialization.MessageKafkaDtoJsonSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaProducerConfig<T extends Serializable> {

    private final KafkaConfigProperties kafkaConfigProperties;

    @Bean
    public Map<String, Object> producerConfigs() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfigProperties.getProducer().getBatchSize() * kafkaConfigProperties.getProducer().getBatchSizeBoostFactor());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaConfigProperties.getProducer().getLingerMs());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaConfigProperties.getProducer().getRequestTimeoutMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaConfigProperties.getProducer().getRetryCount());

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageKafkaDtoJsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        return props;
    }

    @Bean
    public ProducerFactory<String, MessageKafkaDto<?>> producerFactory(final Map<String, Object> producerConfigs) {
        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }

    @Bean
    public KafkaTemplate<String, MessageKafkaDto<?>> kafkaTemplate(final ProducerFactory<String, MessageKafkaDto<?>> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
