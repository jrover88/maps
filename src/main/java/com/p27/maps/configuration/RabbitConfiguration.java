package com.p27.maps.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@EnableRabbit
@Configuration
public class RabbitConfiguration implements RabbitListenerConfigurer {

    @Value("${leg.one.maps.to.pz.queue}")
    private String legOneMapsToPz;

    @Value("${leg.two.maps.to.rix.queue}")
    private String legTwoMapsToRix;

    @Value("${leg.three.maps.to.pz.queue}")
    private String legThreeMapsToPz;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public AmqpAdmin amqpAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        rabbitTemplate.setExchange("exchange");
        return rabbitTemplate;
    }

    @Bean
    public Queue legOneMapsToPzQueue() {
        return new Queue(legOneMapsToPz);
    }

    @Bean
    public Queue legTwoMapsToRixQueue() {
        return new Queue(legTwoMapsToRix);
    }

    @Bean
    public Queue legThreeMapsToPzQueue() {
        return new Queue(legThreeMapsToPz);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("exchange");
    }

    @Bean
    public Binding legOneMapsToPzQueueBinding(){
        return BindingBuilder.bind(legOneMapsToPzQueue()).to(directExchange()).with("l1-maps-to-pz");
    }

    @Bean
    public Binding legTwoMapsToRixQueueBinding(){
        return BindingBuilder.bind(legTwoMapsToRixQueue()).to(directExchange()).with("l2-maps-to-rix");
    }

    @Bean
    public Binding legThreeMapsToPzQueueBinding(){
        return BindingBuilder.bind(legThreeMapsToPzQueue()).to(directExchange()).with("l3-maps-to-pz");
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }
}
