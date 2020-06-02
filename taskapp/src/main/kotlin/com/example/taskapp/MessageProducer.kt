package com.example.taskapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback

class MessageProducer {

    @Autowired
    private val kafkaTemplate: KafkaTemplate<String?, String>? = null

    @Autowired
    private val greetingKafkaTemplate: KafkaTemplate<String, Greeting>? = null

    @Value(value = "\${message.topic.name}")
    private val topicName: String? = null

    @Value(value = "\${greeting.topic.name}")
    private val greetingTopicName: String? = null

    fun sendMessage(message: String) {
        val future = kafkaTemplate!!.send(topicName!!, message)
        future.addCallback(object : ListenableFutureCallback<SendResult<String?, String?>?> {
            override fun onSuccess(result: SendResult<String?, String?>?) {
                println("Sent message=[$message] with offset=[" + result!!.recordMetadata
                        .offset() + "]")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message=[" + message + "] due to : " + ex.message)
            }
        })
    }

    fun sendGreetingMessage(greeting: Greeting) {
        greetingKafkaTemplate!!.send(greetingTopicName!!, greeting)
    }
}