import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MotDePasseHache {

    public interface PasswordCracker {
        String crackPassword(String hashValue);
    }

    public static class BruteForceCracker implements PasswordCracker {
        private String characters;
        private int passwordLength;

        public BruteForceCracker(String characters, int passwordLength) {
            this.characters = characters;
            this.passwordLength = passwordLength;
        }

        @Override
        public String crackPassword(String hashValue) {
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
    }

    public static class DictionaryCracker implements PasswordCracker {
        private List<String> dictionary;

        public DictionaryCracker(List<String> dictionary) {
            this.dictionary = dictionary;
        }

        @Override
        public String crackPassword(String hashValue) {
            for (String password : dictionary) {
                String hashedPassword = getMD5Hash(password);
                if (hashedPassword.equals(hashValue)) {
                    return password;
                }
            }
            return null;
        }
    }

    private static List<String> loadDictionary(String fileName) {
        List<String> dictionary = new ArrayList<>();

        // Chargement du dictionnaire depuis un fichier ou une autre source
        // Ajouter les mots de passe potentiels au dictionnaire

        dictionary.add("test");
        dictionary.add("lait");
        dictionary.add("love");
        dictionary.add("peur");
        dictionary.add("yeux");

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

        // Création du cracker en utilisant la Factory Method
        PasswordCracker cracker = createCracker();

        String bruteForcePassword = cracker.crackPassword(hashedPassword);

        if (bruteForcePassword != null) {
            System.out.println("Le mot de passe trouvé par force brute est : " + bruteForcePassword);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par force brute.");
        }

        String dictionaryPassword = cracker.crackPassword(hashedPassword);

        if (dictionaryPassword != null) {
            System.out.println("Le mot de passe trouvé par attaque par dictionnaire est : " + dictionaryPassword);
        } else {
            System.out.println("Le mot de passe n'a pas été trouvé par attaque par dictionnaire.");
        }
    }

    private static PasswordCracker createCracker() {
        // Choix du type de cracker (BruteForce ou Dictionary)
        String crackerType = "BruteForce";

        if (crackerType.equalsIgnoreCase("BruteForce")) {
            String characters = "abcdefghijklmnopqrstuvwxyz";
            int passwordLength = 4;
            return new BruteForceCracker(characters, passwordLength);
        } else if (crackerType.equalsIgnoreCase("Dictionary")) {
            List<String> dictionary = loadDictionary("dictionnaire.txt");
            return new DictionaryCracker(dictionary);
        }

        throw new IllegalArgumentException("Type de cracker invalide.");
    }
}
