package webcrawler.rabbitmq;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ThreadImgBaixada extends Thread {

    private String site;
    private int num;

    public ThreadImgBaixada(String site, int num) {
        this.site = site;
        this.num = num;
    }
    
    @Override
    public void run(){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String de = System.getProperty("user.dir");
        File dir = new File(WebCrawlerRabbitMQ.destino);
        dir.mkdir();        
        String destino = de + "\\" + WebCrawlerRabbitMQ.destino + "\\";
        try {
            URL url = new URL(site);
            inputStream = url.openStream();
            outputStream = new FileOutputStream(destino + "img" + num + ".png");

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

}
