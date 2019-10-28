package webcrawler.rabbitmq;



import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Produtor {

    private final static String TASK_QUEUE_NAME = "Buffer";
    
    public void enviarBuffer(String mensagem) throws Exception {
        boolean durable = true; //RabbitMQ will never lose our queue
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagem.getBytes("UTF-8"));            
        }
    }
}
