package com.example.taskapp



import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootApplication
class TaskappApplication

fun main(args: Array<String>) {
	 val context: ConfigurableApplicationContext = runApplication<TaskappApplication>(*args)
	context.getBean(MessageProducer::class.java)
	context.getBean(MessageListener::class.java)

	val producer: MessageProducers =	context.getBean(MessageProducers::class.java)
	val listener: MessageListeners = context.getBean(MessageListeners::class.java)

	/*
		 * Sending a Hello World message to topic 'baeldung'.
		 * Must be recieved by both listeners with group foo
		 * and bar with containerFactory fooKafkaListenerContainerFactory
		 * and barKafkaListenerContainerFactory respectively.
		 * It will also be recieved by the listener with
		 * headersKafkaListenerContainerFactory as container factory
		 */
	producer.sendMessage("Hello, World!")
	listener.latch.await(10, TimeUnit.SECONDS)


	/*
		 * Sending message to 'greeting' topic. This will send
		 * and recieved a java object with the help of
		 * greetingKafkaListenerContainerFactory.
		 */
	producer.sendGreetingMessage(Greeting("Greetings", "World!"))
	listener.greetingLatch.await(10, TimeUnit.SECONDS)
}



class MessageListeners {

	val latch = CountDownLatch(3)

	val greetingLatch = CountDownLatch(1)

	@KafkaListener(topics = ["\${greeting.topic.name}"], containerFactory = "greetingKafkaListenerContainerFactory")
	fun greetingListener(greeting: Greeting) {
		println("Recieved greeting message: $greeting")
		greetingLatch.countDown()
	}
}
class MessageProducers {

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