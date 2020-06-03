package com.example.taskapp

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

class MessageListener {

    val latch = CountDownLatch(3)

    val greetingLatch = CountDownLatch(1)

        @KafkaListener(topics = ["\${message.topic.name}"], groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
		fun listenGroupFoo(message: String) {
			println("Received Messasge in group 'foo': " + message);
			latch.countDown();
		}

//    @KafkaListener(topics = ["\${greeting.topic.name}"], containerFactory = "greetingKafkaListenerContainerFactory")
//    fun greetingListener(greeting: Greeting) {
//        println("Recieved greeting message: $greeting")
//        greetingLatch.countDown()
//    }
}