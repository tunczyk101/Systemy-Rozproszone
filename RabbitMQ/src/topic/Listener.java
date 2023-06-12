package topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Listener implements Runnable{
    private final String EXCHANGE_NAME = "spacex";
    private String key;

    public Listener(String key){
        this.key = key;
//        System.out.println(key);
    }


    @Override
    public void run() {
        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, key);

            Consumer consumer = new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println(ConsoleColors.RECEIVED + "Received: " + message + "\n" + ConsoleColors.RESET);
                }
            };

            channel.basicConsume(queueName, true, consumer);

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }
}
