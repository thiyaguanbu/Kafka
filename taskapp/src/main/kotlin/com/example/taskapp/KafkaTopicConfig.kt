package com.example.taskapp

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin
import java.util.*
import kotlin.collections.HashMap

@Configuration
class KafkaTopicConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Value(value = "\${message.topic.name}")
    private val topicName: String? = null

    @Value(value = "\${partitioned.topic.name}")
    private val partionedTopicName: String? = null

    @Value(value = "\${filtered.topic.name}")
    private val filteredTopicName: String? = null

    @Value(value = "\${greeting.topic.name}")
    private val greetingTopicName: String? = null

    @Bean
    fun kafkaAdmin(): KafkaAdmin? {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    @Bean
    fun topic1(): NewTopic? {
        return NewTopic("task", 1, 1.toShort())
    }

    fun greetingTopic(): NewTopic? {
        return NewTopic(greetingTopicName, 1, 1.toShort())
    }
}