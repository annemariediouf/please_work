import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MotDePasseHache {

    public static String bruteForce(String hashValue) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        int passwordLength = 4; 

        
        StringBuilder password = new StringBuilder();
        char[] charArray = characters.toCharArray();
        int[] indices = new int[passwordLength];
        boolean found = false;

        while (!found) {
           
            password.setLength(0);
            for (int index : indices) {
                password.append(charArray[index]);
            }

           
            String hashedPassword = getMD5Hash(password.toString());

            
            if (hashedPassword.equals(hashValue)) {
                found = true;
            } else {
                
                int i = passwordLength - 1;
                while (i >= 0 && indices[i] == charArray.length - 1) {
                    indices[i] = 0;
                    i--;
                }
                if (i < 0) {
                    break;  
                }
                indices[i]++;
            }
        }

        if (found) {
            return password.toString();
        } else {
            return null;
        }
    }

    public static String dictionaryAttack(String hashValue) {
        List<String> dictionary = loadDictionary("dictionnaire.txt");

        
        for (String password : dictionary) {
            String hashedPassword = getMD5Hash(password);
            if (hashedPassword.equals(hashValue)) {
                return password;
            }
        }

        return null;
    }

    private static List<String> loadDictionary(String fileName) {
        List<String> dictionary = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    String password = parts[0];
                    dictionary.add("test");
                    dictionary.add("lait");
                    dictionary.add("love");
                    dictionary.add("peur");
                    dictionary.add("yeux");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dictionary;
    }

    private static String getMD5Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String hashedPassword = "098f6bcd4621d373cade4e832627b4f6";
       

        Scanner scanner = new Scanner(System.in);

        System.out.print("Veuillez saisir votre mot de passe : ");
        String password = scanner.nextLine();

        scanner.close();

       
       String bruteForcePassword = bruteForce(hashedPassword);
        if (bruteForcePassword != null) {
            System.out.println("Le mot de passe trouvé par force brute est : " + bruteForcePassword);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par force brute.");
        }

       
        String dictionaryPassword = dictionaryAttack(hashedPassword);
        if (dictionaryPassword != null) {
            System.out.println("Le mot de passe trouvé par attaque par dictionnaire est : " + dictionaryPassword);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par attaque par dictionnaire.");
        }
    }
}

