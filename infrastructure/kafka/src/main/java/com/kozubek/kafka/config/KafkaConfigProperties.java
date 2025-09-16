package com.kozubek.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "kafka-config")
@Configuration
@Data
public class KafkaConfigProperties {

    private String bootstrapServers;
    private Integer numOfPartitions;
    private Integer replicationFactor;
    private Producer producer;

    @Data
    public static class Producer {
        private String keySerializerClass;
        private String valueSerializerClass;
        private String compressionType;
        private String acks;
        private Integer batchSize;
        private Integer batchSizeBoostFactor;
        private Integer lingerMs;
        private Integer requestTimeoutMs;
        private Integer retryCount;
    }
}
