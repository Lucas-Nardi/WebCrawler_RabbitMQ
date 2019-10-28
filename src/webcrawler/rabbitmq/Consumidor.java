package webcrawler.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Consumidor {

    private final static String TASK_QUEUE_NAME = "Buffer";
    //private static ThreadImgBaixada thread;
    private static int cont = 0;

    private static void  baixar(String site) {

        new Thread() {

           @Override
            public void run() {

                InputStream inputStream = null;
                OutputStream outputStream = null;
                String de = System.getProperty("user.dir");
                File dir = new File("imagem");
                dir.mkdir();
                String destino = de + "\\" + "imagem" + "\\";
                try {
                    URL url = new URL(site);
                    inputStream = url.openStream();
                    outputStream = new FileOutputStream(destino + "img" + cont + ".png");

                    byte[] buffer = new byte[2048];
                    int length;

                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.close();

                } catch (MalformedURLException e) {
                    System.out.println("MalformedURLException :- " + e.getMessage());

                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFoundException :- " + e.getMessage());

                } catch (IOException e) {
                    System.out.println("IOException :- " + e.getMessage());

                }
            }

        }.start();
        
        
    }

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        boolean durable = true;                                                 //RabbitMQ will never lose our queue
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //channel.basicQos(1);                                                   // This tells RabbitMQ not to give more than one message to a worker at a time

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            baixar(message);
            cont++;
//            thread = new ThreadImgBaixada(message, cont);
//            thread.start();
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        boolean autoAck = false; // acknowledgment is covered below
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
        });
    }

}
