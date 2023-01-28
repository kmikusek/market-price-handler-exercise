package com.fx.santander.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer


@Configuration
class KafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServers: String

    @Bean
    fun consumerConfig(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return props
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> =
        DefaultKafkaConsumerFactory(consumerConfig())

    @Bean
    fun factory(
        consumerFactory: ConsumerFactory<String, String>
    ): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> =
        ConcurrentKafkaListenerContainerFactory<String, String>().also {
            it.consumerFactory = consumerFactory
        }
}