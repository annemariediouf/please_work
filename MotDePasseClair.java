import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MotDePasseClair {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Veuillez saisir votre mot de passe : ");
        String password = scanner.nextLine();

        String hashedPassword = getMD5Hash(password);

        // Recherche par force brute
        String bruteForcePassword = bruteForce(hashedPassword);
        if (bruteForcePassword != null) {
            System.out.println("Le mot de passe trouvé par force brute est : " + bruteForcePassword);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par force brute.");
        }

        // Recherche par attaque par dictionnaire
        String dictionaryPassword = dictionaryAttack(hashedPassword);
        if (dictionaryPassword != null) {
            System.out.println("Le mot de passe trouvé par attaque par dictionnaire est : " + dictionaryPassword);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par attaque par dictionnaire.");
        }

        scanner.close();
    }

    public static String bruteForce(String hashValue) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        int maxLength = 4; // Longueur maximale du mot de passe à tester

        // Parcourir toutes les longueurs possibles de mot de passe
        for (int length = 1; length <= maxLength; length++) {
            // Génération de toutes les combinaisons possibles pour la longueur donnée
            StringBuilder password = new StringBuilder(length);

            generatePasswords(characters, password, length, hashValue);

            if (password.length() > 0) {
                return password.toString();
            }
        }

        return null;
    }
    private static boolean generatePasswords(String characters, StringBuilder password, int length, String hashValue) {
        if (password.length() == length) {
            // Calcul du hachage de la combinaison
            String hashedPassword = getMD5Hash(password.toString());

            // Comparaison des hachages
            if (hashedPassword.equals(hashValue)) {
                return true;
            }
            return false;
        }

   

       for (int i = 0; i < characters.length(); i++) {
            password.append(characters.charAt(i));
            if (generatePasswords(characters, password, length, hashValue)) {
                return true;
            }
            password.setLength(password.length() - 1);
        }

        return false;
    }


    public static String dictionaryAttack(String hashValue) {
        List<String> dictionary = loadDictionary(); // Chargement du dictionnaire depuis un fichier

        // Parcourir le dictionnaire et vérifier les hachages
        for (String password : dictionary) {
            String hashedPassword = getMD5Hash(password);
            if (hashedPassword.equals(hashValue)) {
                return password;
            }
        }

        return null;
    }

    private static List<String> loadDictionary() {
        List<String> dictionary = new ArrayList<>();
        // Charger le dictionnaire depuis un fichier ou une autre source
        // Ajoutez les mots de passe potentiels au dictionnaire

        dictionary.add("pile");
        dictionary.add("lait");
        dictionary.add("main");
        dictionary.add("test");
        dictionary.add("voir");
        dictionary.add("toit");
        dictionary.add("love");

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
}
