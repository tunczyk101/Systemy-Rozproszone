package queue;

import com.rabbitmq.client.*;
import topic.ConsoleColors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class QueueOut implements Runnable{
    private String key;
    private java.util.function.Consumer<String> handler;


    public QueueOut(String key, java.util.function.Consumer<String> handler) {
        this.key = key;
        this.handler = handler;

    }



    @Override
    public void run() {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();;
            channel.queueDeclare(key, false, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println(ConsoleColors.RECEIVED + "Received: " + message + "\n" + ConsoleColors.RESET);
                    handler.accept(message);
                    }
                };

            channel.basicConsume(key, true, consumer);

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
