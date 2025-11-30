package com.kozubek.kafka.config.consumer;

import com.kozubek.kafka.config.serialization.MessageKafkaDto;

import java.util.List;

public interface KafkaConsumer <M extends MessageKafkaDto> {

    void receive(List<M> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
