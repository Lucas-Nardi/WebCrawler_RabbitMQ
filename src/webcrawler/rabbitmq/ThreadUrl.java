package webcrawler.rabbitmq;



import webcrawler.rabbitmq.Produtor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadUrl extends Thread {
       
    private String site; 
    private Produtor produtor;

    public ThreadUrl(String site) {
        this.site = site;
        produtor = new Produtor();
    }
    
    
    @Override
    public void run() {
        try {
            String html;
            html = getHTML(site);
            String regex = "http(s?)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./]*)+\\.(?:[gG][iI][fF]|[jJ][pP][gG]|[jJ][pP][eE][gG]|[pP][nN][gG]|[bB][mM][pP])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(html);
            String url;
            while (m.find()) {
                url = m.group();
                System.out.println(url);
                produtor.enviarBuffer(url);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    
    private static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        BufferedReader rd = new BufferedReader(isr);
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
