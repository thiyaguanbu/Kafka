package com.example.taskapp

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import java.util.*

@EnableKafka
@Configuration
class KafkaConsumerConfig{

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    fun consumerFactory(groupId: String) : ConsumerFactory<String, String>{
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }

    fun kafkaListenerContainerFactory(groupId: String?): ConcurrentKafkaListenerContainerFactory<String, String>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory(groupId!!)
        return factory
    }

    fun greetingConsumerFactory(): ConsumerFactory<String?, Greeting?> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        props[ConsumerConfig.GROUP_ID_CONFIG] = "welcome"
//        return DefaultKafkaConsumerFactory<Any?, Any?>(props, StringDeserializer(), JsonDeserializer<Any?>(Greeting::class.java))
        return DefaultKafkaConsumerFactory<String?, Greeting?>(props,StringDeserializer(), JsonDeserializer<Greeting>(Greeting::class.java))
    }

    @Bean
    fun greetingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Greeting>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Greeting>()
        factory.consumerFactory = greetingConsumerFactory()
        return factory
    }
}