import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    public static void main(String[] args) {
        String serverUrl = "http://192.168.1.13:8080/var/www/html/serveur.php"; // URL du serveur PHP
        String username = "anne"; 
        String password = "work"; 

        performAttack(password);
        
        boolean isPasswordCorrect = sendRequest(serverUrl, username, password); 
        
        if (isPasswordCorrect) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Connexion échouée !");
        }
    }


    public static void performAttack(String password) {
        
    }

    public static boolean sendRequest(String serverUrl, String username, String password) {
        try {
            
            
            URL url = new URL(serverUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String parameters = "username=" + username + "&password=" + password;

            byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);

            
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postData.length));

            connection.getOutputStream().write(postData);

        
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();

           
            if (response != null && response.contains("success")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}



