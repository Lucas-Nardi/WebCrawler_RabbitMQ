package webcrawler.rabbitmq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WebCrawlerRabbitMQ {
   
    public static String destino;    
    public static Consumidor consumidor = new Consumidor();
    
    public static void LerArquivo(String nome) throws IOException, Exception {

        File arq = new File(nome);
        FileReader fileR = null;
        BufferedReader bufferR = null;
        String readLine = "";
        ThreadUrl thread;
        
        if (arq.exists()) {
            fileR = new FileReader(arq);
            bufferR = new BufferedReader(fileR);
            while (readLine != null) {
                readLine = bufferR.readLine();
                if (readLine == null) {
                    break;
                }                  
                thread = new ThreadUrl(readLine);
                thread.start();
                
            }
            bufferR.close();
            fileR.close();

        } else {
            arq.createNewFile();
        }

    }

    public static void main(String[] args) throws IOException, Exception {       
               
        destino = "imagem";        
        LerArquivo("url.txt");
    }
}
