package MonitoringService.config;

//import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;


import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    public class RabbitMqConfig {
        @Value("${rabbitmq.queue.name}")
        private String queue;

        @Value("${rabbitmq.routing.key}")
        private String routingKey;
        @Value("${rabbitmq.exchange.name}")
        private String exchange;


        @Value("${rabbitmq.queue.name2}")
        private String queue2;


        @Value("${rabbitmq.routing.key2}")
        private String routingKey2;
        @Value("${rabbitmq.exchange.name2}")
        private String exchange2;




        @Bean
        public Jackson2JsonMessageConverter jsonMessageConverter() {
            return new Jackson2JsonMessageConverter();
        }


        @Bean
        public Queue queue() {
            return new Queue(queue);

        }
        @Bean
        public Queue queue2() {
            return new Queue(queue2);

        }
        @Bean
        public TopicExchange exchange(){
            return new TopicExchange(exchange);
        }

        @Bean
        public TopicExchange exchange2(){
            return new TopicExchange(exchange2);
        }


        @Bean
        public Binding binding(){

        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
        }


        @Bean
        public Binding binding2(){

            return BindingBuilder
                    .bind(queue2())
                    .to(exchange2())
                    .with(routingKey2);
        }
        @Bean
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(messageConverter);
            return rabbitTemplate;
        }


}
